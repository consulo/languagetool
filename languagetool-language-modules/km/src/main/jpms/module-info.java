// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.km {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.km;
    exports org.languagetool.rules.km;
    exports org.languagetool.tagging.km;
    exports org.languagetool.tokenizers.km;
}
