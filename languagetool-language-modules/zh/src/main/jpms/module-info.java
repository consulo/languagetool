// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.zh {
    requires hanlp.portable;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.zh;
    exports org.languagetool.rules.zh;
    exports org.languagetool.tagging.zh;
    exports org.languagetool.tokenizers.zh;
}
