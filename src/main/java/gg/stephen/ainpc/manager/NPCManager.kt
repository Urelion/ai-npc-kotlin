package gg.stephen.ainpc.manager

import gg.stephen.ainpc.manager.ConfigManager.getWalkedTooFar
import gg.stephen.ainpc.manager.ConfigManager.sendAIMessage
import gg.stephen.ainpc.npc.AITrait
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.concurrent.CompletableFuture

class NPCManager : Listener {

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {

        val player = e.player

        for (npc in CitizensAPI.getNPCRegistry().sorted()) {
            if (
                npc == null ||
                npc.entity == null ||
                npc.getTraitNullable(AITrait::class.java) == null
            ) {
                continue
            }

            val aiTrait: AITrait = npc.getTraitNullable(AITrait::class.java)
            if (aiTrait.conversations.containsKey(player.uniqueId)) {
                e.isCancelled = true

                if (player.location.distance(npc.storedLocation) > 6) {
                    player.sendMessage(getWalkedTooFar(npc.name))
                    aiTrait.conversations.remove(player.uniqueId)
                    return
                }

                ConfigManager.sendYouMessage(player, e.message)
                aiTrait.conversations[player.uniqueId]!!.append("\n\nHuman:").append(e.message).append("\n\nAI:")

                CompletableFuture.runAsync {
                    sendAIMessage(
                        player,
                        npc.name,
                        aiTrait.getResponse(player.uniqueId)
                    )
                }
            }
        }

    }

}