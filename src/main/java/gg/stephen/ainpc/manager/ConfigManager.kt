package gg.stephen.ainpc.manager

import gg.stephen.ainpc.AINPC
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

object ConfigManager {

    private lateinit var config: FileConfiguration
    private val pluginFormat get() = getConfigValue("plugin-message")
    private val youFormat get() = getConfigValue("you-message")
    private val aiFormat: String get() = getConfigValue("ai-message")
    val apiKey: String get() = getConfigValue("api-key")
    private val nowTalking: String get() = getConfigValue("messages.now-talking")
    private val nowTalkingNewNpc: String get() = getConfigValue("messages.now-talking-new-npc")
    private val noLongerTalking: String get() = getConfigValue("messages.no-longer-talking")
    private val walkedTooFar: String get() = getConfigValue("messages.walked-too-far")
    val leftClick: Boolean get() = config.getBoolean("left-click")
    val rightClick: Boolean get() = config.getBoolean("right-click")

    fun init(ainpc: AINPC) {
        ainpc.saveDefaultConfig()
        config = ainpc.config
    }

    private fun getConfigValue(path: String): String {
        return ChatColor.translateAlternateColorCodes('&', config.getString(path)!!)
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