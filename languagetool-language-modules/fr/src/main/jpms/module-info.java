// Maintained by tools/jpms/descriptors.py; see tools/jpms/README.md.
open module org.languagetool.language.fr {
    requires com.fasterxml.jackson.databind;
    requires french.pos.dict;
    requires org.apache.commons.lang3;
    requires static org.jetbrains.annotations;
    requires transitive org.languagetool.core;
    requires org.slf4j;

    exports org.languagetool.language.fr;
    exports org.languagetool.rules.fr;
    exports org.languagetool.synthesis.fr;
    exports org.languagetool.tagging.disambiguation.fr;
    exports org.languagetool.tagging.fr;
    exports org.languagetool.tokenizers.fr;
}
