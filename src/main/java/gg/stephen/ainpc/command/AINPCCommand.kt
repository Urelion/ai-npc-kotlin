package gg.stephen.ainpc.command

import gg.stephen.ainpc.manager.ConfigManager.sendPluginMessage
import gg.stephen.ainpc.manager.PromptsManager
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.logging.Level

class AINPCCommand(private val promptsManager: PromptsManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            Bukkit.getLogger().log(Level.WARNING, "This command should be used in-game.")
            return false
        }

        if (!sender.hasPermission("ainpc.set")) {
            sendPluginMessage(sender, "No permission for this command.")
            return false
        }

        if (args.isEmpty()) {
            sendPluginMessage(sender, "Not enough arguments. Use /ainpc <prompt>.")
            return false
        }

        val npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender)
        if (npc == null) {
            sendPluginMessage(sender, "You have no Citizens NPC selected. Use Citizens command: /npc select <name>.")
            return false
        }

        promptsManager.setPrompt(npc, args.joinToString(separator = " "))
        sendPluginMessage(sender, "Success! The NPC now follows your set prompt when players click to interact. To alter it, run the command with a different prompt.")

        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String>? {
        return mutableListOf("Your prompt here...")
    }

}