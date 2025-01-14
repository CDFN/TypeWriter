package me.gabber235.typewriter.citizens

import App
import me.gabber235.typewriter.Typewriter.Companion.plugin
import me.gabber235.typewriter.adapters.Adapter
import me.gabber235.typewriter.adapters.TypewriteAdapter
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo

@Adapter("Citizens", "For the Citizens plugin", App.VERSION)
object CitizensAdapter : TypewriteAdapter() {
	override fun initialize() {
		if (!plugin.server.pluginManager.isPluginEnabled("Citizens")) {
			plugin.logger.warning("Citizens plugin not found, try installing it or disabling the Citizens adapter")
			return
		}

		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TypewriterTrait::class.java))
	}

	override fun shutdown() {
		CitizensAPI.getTraitFactory().deregisterTrait(TraitInfo.create(TypewriterTrait::class.java))
	}
}