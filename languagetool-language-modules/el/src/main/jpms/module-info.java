// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.el {
    requires morphology.el;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.el;
    exports org.languagetool.rules.el;
    exports org.languagetool.synthesis.el;
    exports org.languagetool.tagging.el;
    exports org.languagetool.tokenizers.el;
}
