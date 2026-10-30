// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ja {
    requires com.ibm.icu;
    requires lucene.gosen;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.ja;
    exports org.languagetool.tagging.ja;
    exports org.languagetool.tokenizers.ja;
}
