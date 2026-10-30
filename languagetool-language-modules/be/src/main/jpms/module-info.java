// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.be {
    requires linguistics.grammardb.spell.languagetool;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.be;
    exports org.languagetool.rules.be;
    exports org.languagetool.tokenizers.be;
}
