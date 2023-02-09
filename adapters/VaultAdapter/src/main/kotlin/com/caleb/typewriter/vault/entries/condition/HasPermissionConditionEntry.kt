package com.caleb.typewriter.vault.entries.condition

import com.caleb.typewriter.vault.VaultAdapter
import com.google.gson.annotations.SerializedName
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.EntryIdentifier
import me.gabber235.typewriter.adapters.modifiers.Triggers
import me.gabber235.typewriter.entry.*
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import net.milkbowl.vault.permission.Permission
import org.bukkit.entity.Player

@Entry("vault_has_permission_condition", "[Vault] Continues if a player has a permission", Colors.PINK, Icons.FILTER)
class HasPermissionConditionEntry(
	override val id: String,
	override val name: String,
	@SerializedName("triggers")
	@Triggers
	@EntryIdentifier(TriggerableEntry::class)
	val nextTriggers: List<String> = emptyList(),
	override val criteria: List<Criteria>,
	override val modifiers: List<Modifier>,
	private var permission: String = ""

) : ActionEntry {

	override val triggers: List<String>
		get() = emptyList()

	override fun execute(player: Player) {
		val permissionHandler: Permission = VaultAdapter.permissions ?: return

		if (permissionHandler.has(player, permission)) {
			super.execute(player)
			nextTriggers triggerEntriesFor player
		}
	}
}