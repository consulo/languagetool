// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.tools {
    requires org.apache.commons.cli;
    requires org.carrot2.morfologik.tools;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;

    exports org.languagetool.tools.dictionary;
}
