// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.nl {
    requires com.google.common;
    requires com.hankcs.aho.corasick;
    requires dutch.pos.dict;
    requires it.unimi.dsi.fastutil.core;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.nl;
    exports org.languagetool.rules.nl;
    exports org.languagetool.synthesis.nl;
    exports org.languagetool.tagging.nl;
    exports org.languagetool.tokenizers.nl;
}
