package me.gabber235.typewriter.entries.dialogue.messengers.spoken

import me.gabber235.typewriter.adapters.Messenger
import me.gabber235.typewriter.adapters.MessengerFilter
import me.gabber235.typewriter.entries.dialogue.SpokenDialogueEntry
import me.gabber235.typewriter.entry.dialogue.*
import me.gabber235.typewriter.entry.entries.DialogueEntry
import me.gabber235.typewriter.extensions.placeholderapi.parsePlaceholders
import me.gabber235.typewriter.interaction.chatHistory
import me.gabber235.typewriter.snippets.snippet
import me.gabber235.typewriter.utils.*
import net.kyori.adventure.text.*
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import java.time.Duration

val spokenFormat: String by snippet(
	"dialogue.spoken.format",
	"""
		|<gray><st>${" ".repeat(60)}</st>
		|
		|<gray><padding>[ <bold><speaker></bold><reset><gray> ]
		|
		|<message>
		|
		|<next_color>${" ".repeat(20)} Press<white> <key:key.swapOffhand> </white>to <finish_text>
		|<gray><st>${" ".repeat(60)}</st>
		""".trimMargin()
)

val spokenPadding: String by snippet("dialogue.spoken.padding", "    ")
val spokenMinLines: Int by snippet("dialogue.spoken.minLines", 3)
val spokenMaxLineLength: Int by snippet("dialogue.spoken.maxLineLength", 40)


@Messenger(SpokenDialogueEntry::class)
class JavaSpokenDialogueDialogueMessenger(player: Player, entry: SpokenDialogueEntry) :
	DialogueMessenger<SpokenDialogueEntry>(player, entry) {

	companion object : MessengerFilter {
		override fun filter(player: Player, entry: DialogueEntry): Boolean = true
	}

	private var speakerDisplayName = ""
	override fun init() {
		super.init()
		speakerDisplayName = entry.speakerDisplayName.parsePlaceholders(player)

		listen<PlayerSwapHandItemsEvent> { event ->
			if (event.player.uniqueId != player.uniqueId) return@listen
			state = MessengerState.FINISHED
			event.isCancelled = true
		}
	}

	override fun tick(cycle: Int) {
		player.sendSpokenDialogue(entry.text, speakerDisplayName, entry.duration, cycle, triggers.isEmpty())
	}
}

fun Player.sendSpokenDialogue(
	text: String,
	speakerDisplayName: String,
	duration: Duration,
	cycle: Int,
	canFinish: Boolean
) {
	val durationInTicks = duration.toTicks()

	val percentage = (cycle / durationInTicks.toDouble())

	val nextColor = if (cycle > durationInTicks * 2.5 && (cycle / 6) % 3 == 0) "<red>" else "<gray>"

	val continueOrFinish = if (canFinish) "finish" else "continue"

	val message = text.parsePlaceholders(this).asPartialFormattedMini(
		percentage,
		padding = spokenPadding,
		minLines = spokenMinLines,
		maxLineLength = spokenMaxLineLength
	)

	val component = spokenFormat.asMiniWithResolvers(
		Placeholder.parsed("speaker", speakerDisplayName),
		Placeholder.component("message", message),
		Placeholder.parsed("next_color", nextColor),
		Placeholder.parsed("finish_text", continueOrFinish),
		Placeholder.parsed("padding", spokenPadding)
	)

	val componentWithDarkMessages = chatHistory.composeDarkMessage(component)
	sendMessage(componentWithDarkMessages)
}