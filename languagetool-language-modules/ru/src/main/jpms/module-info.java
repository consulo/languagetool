// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ru {
    requires com.google.common;
    requires transitive com.hankcs.aho.corasick;
    requires it.unimi.dsi.fastutil.core;
    requires openregex;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.chunking.ru;
    exports org.languagetool.language.ru;
    exports org.languagetool.rules.ru;
    exports org.languagetool.synthesis.ru;
    exports org.languagetool.tagging.disambiguation.ru;
    exports org.languagetool.tagging.ru;
    exports org.languagetool.tokenizers.ru;
}
