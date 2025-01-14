import "package:auto_size_text/auto_size_text.dart";
import "package:flutter/material.dart";
import "package:flutter_hooks/flutter_hooks.dart";
import "package:hooks_riverpod/hooks_riverpod.dart";
import "package:riverpod_annotation/riverpod_annotation.dart";
import "package:typewriter/models/adapter.dart";
import "package:typewriter/utils/extensions.dart";
import "package:typewriter/widgets/inspector/inspector.dart";
import "package:url_launcher/url_launcher_string.dart";

part "heading.g.dart";

@riverpod
String _entryId(_EntryIdRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.entry.id ?? "";
}

@riverpod
String _entryName(_EntryNameRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.entry.formattedName ?? "";
}

@riverpod
String _entryType(_EntryTypeRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.name ?? "";
}

@riverpod
String _entryUrl(_EntryUrlRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.wikiUrl ?? "";
}

@riverpod
Color _entryColor(_EntryColorRef ref) {
  final def = ref.watch(inspectingEntryDefinitionProvider);
  return def?.blueprint.color ?? Colors.grey;
}

class Heading extends HookConsumerWidget {
  const Heading({
    super.key,
  }) : super();

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final id = ref.watch(_entryIdProvider);
    final name = ref.watch(_entryNameProvider);
    final type = ref.watch(_entryTypeProvider);
    final url = ref.watch(_entryUrlProvider);
    final color = ref.watch(_entryColorProvider);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _Title(
          color: color,
          title: name,
        ),
        Row(
          children: [
            _Type(type: type, url: url, color: color),
            const SizedBox(width: 8),
            _Identifier(id: id),
          ],
        ),
      ],
    );
  }
}

class _Title extends StatelessWidget {
  const _Title({
    required this.title,
    required this.color,
  });
  final String title;
  final Color color;

  @override
  Widget build(BuildContext context) => AutoSizeText(
        title,
        style: TextStyle(color: color, fontSize: 40, fontWeight: FontWeight.bold),
        maxLines: 1,
      );
}

class _Identifier extends StatelessWidget {
  const _Identifier({
    required this.id,
  });
  final String id;

  @override
  Widget build(BuildContext context) {
    return SelectableText(id, style: Theme.of(context).textTheme.bodySmall?.copyWith(color: Colors.grey));
  }
}

class _Type extends HookWidget {
  const _Type({
    required this.type,
    required this.url,
    required this.color,
  });
  final String type;
  final String url;
  final Color color;

  Future<void> _launceUrl() async {
    if (url.isEmpty) return;
    if (!await canLaunchUrlString(url)) return;
    await launchUrlString(url);
  }

  @override
  Widget build(BuildContext context) {
    final hovering = useState(false);
    return MouseRegion(
      cursor: SystemMouseCursors.click,
      onEnter: (_) => hovering.value = true,
      onExit: (_) => hovering.value = false,
      child: GestureDetector(
        onTap: _launceUrl,
        child: Text(
          type.formatted,
          style: Theme.of(context).textTheme.bodySmall?.copyWith(
                color: color.withOpacity(0.9),
                decoration: hovering.value ? TextDecoration.underline : null,
              ),
        ),
      ),
    );
  }
}
