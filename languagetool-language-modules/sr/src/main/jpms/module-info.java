// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.sr {
    requires transitive org.languagetool.core;

    exports org.languagetool.language.sr;
    exports org.languagetool.rules.sr;
    exports org.languagetool.rules.sr.ekavian;
    exports org.languagetool.rules.sr.jekavian;
    exports org.languagetool.synthesis.sr;
    exports org.languagetool.tagging.disambiguation.sr;
    exports org.languagetool.tagging.sr;
}
