// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.sl {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.sl;
    exports org.languagetool.rules.sl;
}
