// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.sv {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.sv;
    exports org.languagetool.rules.sv;
    exports org.languagetool.synthesis.sv;
    exports org.languagetool.tagging.disambiguation.sv;
    exports org.languagetool.tagging.sv;
}
