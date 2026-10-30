// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.ast {
    requires asturian.pos.dict;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.language.ast;
    exports org.languagetool.language.rules.ast;
    exports org.languagetool.tagging.ast;
}
