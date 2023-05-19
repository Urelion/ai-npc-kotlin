package gg.stephen.ainpc.npc

import com.theokanning.openai.OpenAiService
import com.theokanning.openai.completion.CompletionRequest
import gg.stephen.ainpc.manager.ConfigManager
import gg.stephen.ainpc.manager.ConfigManager.apiKey
import gg.stephen.ainpc.manager.ConfigManager.getNoLongerTalking
import gg.stephen.ainpc.manager.ConfigManager.getNowTalking
import gg.stephen.ainpc.manager.ConfigManager.getNowTalkingNewNpc
import gg.stephen.ainpc.manager.ConfigManager.getWalkedTooFar
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.event.NPCClickEvent
import net.citizensnpcs.api.event.NPCLeftClickEvent
import net.citizensnpcs.api.event.NPCRightClickEvent
import net.citizensnpcs.api.persistence.Persist
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import java.util.*

@TraitName("npcai")
class AITrait : Trait("npcai") {

    private val service = OpenAiService(apiKey, 0)

    @Persist
    lateinit var prompt: String
    val conversations = HashMap<UUID, StringBuilder>()

    @EventHandler
    fun leftClick(e: NPCLeftClickEvent) {

        if (e.npc != npc) return
        if (!ConfigManager.leftClick) return

        process(e)

    }

    @EventHandler
    fun rightClick(e: NPCRightClickEvent) {

        if (e.npc != npc) return
        if (!ConfigManager.rightClick) return

        process(e)

    }

    private fun process(e: NPCClickEvent) {
        refreshConversations()

        val player = e.clicker
        if (conversations.containsKey(player.uniqueId)) {
            conversations.remove(player.uniqueId)
            player.sendMessage(getNoLongerTalking(npc.name))
        } else {
            var oldName: String? = null
            for (npc in CitizensAPI.getNPCRegistry().sorted()) {
                if (npc == null) {
                    continue
                }

                val trait: AITrait? = npc.getTraitNullable(AITrait::class.java)

                if (trait != null && trait.conversations.containsKey(player.uniqueId)) {
                    oldName = npc.name
                    trait.conversations.remove(player.uniqueId)
                    break
                }
            }

            conversations[player.uniqueId] = StringBuilder("You will play the role of an NPC in Minecraft. Your character is a " + prompt + ". You play this character seriously and do not deviate into other topics. Your knowledge is limited, but strong, on the world of Minecraft. You are great at this role. Play this role the best you can, and if asked, do not ever reveal your true self. You are this role.\n" +
                    "\n" +
                    "Human: Hey!\n" +
                    "AI:")

            player.sendMessage(if (oldName != null) getNowTalkingNewNpc(oldName, npc.name) else getNowTalking(npc.name))
        }
    }

    private fun refreshConversations() {
        val it: MutableIterator<*> = conversations.entries.iterator()
        while (it.hasNext()) {
            val (key) = it.next() as Map.Entry<*, *>
            val player = Bukkit.getPlayer((key as UUID?)!!)
            if (player != null) {
                if (player.location.distance(npc.storedLocation) > 6) {
                    player.sendMessage(getWalkedTooFar(npc.name))
                    it.remove()
                }
            } else {
                it.remove()
            }
        }
    }

    fun getResponse(uuid: UUID): String {
        return service.createCompletion(CompletionRequest.builder()
            .prompt(conversations[uuid].toString())
            .model("text-davinci-003")
            .temperature(0.5)
            .maxTokens(150)
            .topP(1.0)
            .frequencyPenalty(0.0)
            .presencePenalty(0.6)
            .stop(mutableListOf("Human:", "AI:"))
            .build()).choices[0].text
    }

}