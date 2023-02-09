package com.caleb.typewriter.vault.entries.action

import com.caleb.typewriter.vault.VaultAdapter
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

@Entry("vault_withdraw_balance", "[Vault] Withdraw Balance", Colors.RED, Icons.MONEY_BILL)
data class WithdrawBalanceActionEntry(
	override val id: String = "",
	override val name: String = "",
	override val criteria: List<Criteria> = emptyList(),
	override val modifiers: List<Modifier> = emptyList(),
	override val triggers: List<String> = emptyList(),
	private val amount: Double = 0.0,
) : ActionEntry {
	override fun execute(player: Player) {
		super.execute(player)

		val economy: Economy = VaultAdapter.economy ?: return

		economy.withdrawPlayer(player, amount)
	}
}