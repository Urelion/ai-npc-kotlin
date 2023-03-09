package gg.stephen.ainpc.manager

import gg.stephen.ainpc.AINPC
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ConfigManager {

    private lateinit var pluginFormat: String
    private lateinit var youFormat: String
    private lateinit var aiFormat: String
    lateinit var apiKey: String
    private lateinit var nowTalking: String
    private lateinit var nowTalkingNewNpc: String
    private lateinit var noLongerTalking: String
    private lateinit var walkedTooFar: String
    var leftClick: Boolean = false
    var rightClick: Boolean = false

    fun init(ainpc: AINPC) {
        ainpc.saveDefaultConfig()
        val config = ainpc.config

        pluginFormat = ChatColor.translateAlternateColorCodes('&', config.getString("plugin-message")!!)
        youFormat = ChatColor.translateAlternateColorCodes('&', config.getString("you-message")!!)
        aiFormat = ChatColor.translateAlternateColorCodes('&', config.getString("ai-message")!!)
        apiKey = ChatColor.translateAlternateColorCodes('&', config.getString("api-key")!!)
        nowTalking = ChatColor.translateAlternateColorCodes('&', config.getString("messages.now-talking")!!)
        nowTalkingNewNpc = ChatColor.translateAlternateColorCodes('&', config.getString("messages.now-talking-new-npc")!!)
        noLongerTalking = ChatColor.translateAlternateColorCodes('&', config.getString("messages.no-longer-talking")!!)
        walkedTooFar = ChatColor.translateAlternateColorCodes('&', config.getString("messages.walked-too-far")!!)
        leftClick = config.getBoolean("left-click")
        rightClick = config.getBoolean("right-click")
    }

    fun sendPluginMessage(player: Player, message: String) {
        player.sendMessage(pluginFormat.replace("%message%", message))
    }

    fun sendYouMessage(player: Player, message: String) {
        player.sendMessage(youFormat.replace("%message%", message))
    }

    fun sendAIMessage(player: Player, npcName: String, message: String) {
        player.sendMessage(aiFormat.replace("%message%", message).replace("%name%", npcName))
    }

    fun getNowTalking(npc: String): String {
        return pluginFormat.replace("%message%", nowTalking.replace("%npc%", npc))
    }

    fun getNowTalkingNewNpc(oldNpc: String, newNpc: String): String {
        return pluginFormat.replace("%message%", nowTalkingNewNpc.replace("%oldnpc%", oldNpc).replace("%newnpc%", newNpc))
    }

    fun getNoLongerTalking(npc: String): String {
        return pluginFormat.replace("%message%", noLongerTalking.replace("%npc%", npc))
    }

    fun getWalkedTooFar(npc: String): String {
        return pluginFormat.replace("%message%", walkedTooFar.replace("%npc%", npc))
    }

}