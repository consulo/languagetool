// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.pl {
    requires transitive com.hankcs.aho.corasick;
    requires org.carrot2.morfologik.stemming;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.pl;
    exports org.languagetool.rules.pl;
    exports org.languagetool.synthesis.pl;
    exports org.languagetool.tagging.disambiguation.pl;
    exports org.languagetool.tagging.pl;
    exports org.languagetool.tokenizers.pl;
}
