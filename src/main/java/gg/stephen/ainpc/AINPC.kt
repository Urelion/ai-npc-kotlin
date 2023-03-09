package gg.stephen.ainpc

import gg.stephen.ainpc.command.AINPCCommand
import gg.stephen.ainpc.manager.ConfigManager
import gg.stephen.ainpc.manager.ConfigManager.apiKey
import gg.stephen.ainpc.manager.NPCManager
import gg.stephen.ainpc.manager.PromptsManager
import gg.stephen.ainpc.npc.AITrait
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

class AINPC : JavaPlugin() {

    override fun onEnable() {
        val pluginManager = server.pluginManager

        if (pluginManager.getPlugin("Citizens") == null) {
            logger.log(Level.SEVERE, "Cannot start AI-NPC as Citizens dependency was not found. Please install it from Citizens SpigotMC page.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        ConfigManager.init(this)
        if (apiKey == "") {
            logger.log(Level.SEVERE, "No API key set! See instructions in config.yml.")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(AITrait::class.java))

        val promptsManager = PromptsManager(this)
        pluginManager.registerEvents(promptsManager, this)
        pluginManager.registerEvents(NPCManager(), this)

        val command = AINPCCommand(promptsManager)
        getCommand("ainpc")!!.setExecutor(command)
        getCommand("ainpc")!!.tabCompleter = command

        logger.log(Level.FINE, "AINPC loaded fine. Please note that any messages regarding a trait not loading is a Citizens bug that we cannot fix. Don't worry - your AI NPC's will still work!")
    }

}