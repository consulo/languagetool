// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.it {
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.it;
    exports org.languagetool.rules.it;
    exports org.languagetool.synthesis.it;
    exports org.languagetool.tagging.disambiguation.rules.it;
    exports org.languagetool.tagging.it;
}
