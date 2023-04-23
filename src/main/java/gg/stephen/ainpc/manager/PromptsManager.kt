package gg.stephen.ainpc.manager

import gg.stephen.ainpc.AINPC
import gg.stephen.ainpc.npc.AITrait
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.event.CitizensEnableEvent
import net.citizensnpcs.api.npc.NPC
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.io.File

class PromptsManager(ainpc: AINPC) : Listener {

    private val prompts: HashMap<Integer, String> = HashMap()

    private val file = File(ainpc.dataFolder, "data.yml")
    private val config: YamlConfiguration

    init {
        if (!file.exists()) {
            file.createNewFile()
        }
        config = YamlConfiguration.loadConfiguration(file)
    }

    @EventHandler
    fun citizensEnable(e: CitizensEnableEvent) {
        initNpcs()
    }

    fun initNpcs() {
        val registry = CitizensAPI.getNPCRegistry()
        for (npcId in config.getConfigurationSection("")!!.getKeys(false)) {
            val npc : NPC = registry.getById(Integer.parseInt(npcId)) ?: continue
            npc.removeTrait(AITrait::class.java)
            setPrompt(npc, config.getString(npcId)!!)
        }
    }

    fun setPrompt(npc: NPC, prompt: String) {
        if (npc.getTraitNullable(AITrait::class.java) != null) {
            npc.removeTrait(AITrait::class.java)
        }
        val aiTrait = AITrait()
        aiTrait.prompt = prompt
        npc.addTrait(aiTrait)
        prompts[npc.id as Integer] = prompt
        config.set(npc.id.toString(), prompt)
        config.save(file)
    }

}