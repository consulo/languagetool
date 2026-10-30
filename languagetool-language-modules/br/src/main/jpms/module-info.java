// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.br {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.br;
    exports org.languagetool.rules.br;
    exports org.languagetool.tagging.br;
    exports org.languagetool.tokenizers.br;
}
