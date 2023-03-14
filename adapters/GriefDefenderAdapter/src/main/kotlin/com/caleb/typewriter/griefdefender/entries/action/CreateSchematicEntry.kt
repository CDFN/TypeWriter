package com.caleb.typewriter.griefdefender.entries.action

import com.griefdefender.api.GriefDefender
import com.griefdefender.api.claim.ClaimSchematic
import me.gabber235.typewriter.adapters.Colors
import me.gabber235.typewriter.adapters.Entry
import me.gabber235.typewriter.adapters.modifiers.Help
import me.gabber235.typewriter.entry.Criteria
import me.gabber235.typewriter.entry.Modifier
import me.gabber235.typewriter.entry.entries.ActionEntry
import me.gabber235.typewriter.utils.Icons
import org.bukkit.entity.Player
import java.util.*

@Entry("create_schematic", "Create a schematic for GriefDefender Claim", Colors.RED, Icons.DROPBOX)
class CreateSchematicEntry(
    override val id: String = "",
    override val name: String = "",
    override val criteria: List<Criteria> = emptyList(),
    override val modifiers: List<Modifier> = emptyList(),
    override val triggers: List<String> = emptyList(),
    @Help("The claim to use for schematic.")
    private val claim: String = "",
    @Help("The schematic name.")
    private val schematicName: String = "",
    @Help("Whether entities should be copied.")
    private val includeEntities: Boolean = false,
    @Help("Whether nbt data should be copied.")
    private val includeNBT: Boolean = false,
) : ActionEntry {
    override fun execute(player: Player) {
        super.execute(player)

        val claim = GriefDefender.getCore().getClaim(UUID.fromString(claim)) ?: return
        if (claim.schematics.containsKey(schematicName)) return
        ClaimSchematic.builder()
            .claim(claim)
            .name(schematicName)
            .nbt(includeNBT)
            .entities(includeEntities)
            .build()
    }
}