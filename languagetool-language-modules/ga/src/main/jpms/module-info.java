// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ga {
    requires languagetool.ga.dicts;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.ga;
    exports org.languagetool.rules.ga;
    exports org.languagetool.synthesis.ga;
    exports org.languagetool.tagging.disambiguation.ga;
    exports org.languagetool.tagging.ga;
}
