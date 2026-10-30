/* LanguageTool, a natural language style checker
 * Copyright (C) 2014 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.rules.es;

import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.TestTools;
import org.languagetool.language.es.Spanish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MorfologikSpanishSpellerRuleTest {

  @Test
  public void testMorfologikSpeller() throws IOException {
    Spanish language = Spanish.getInstance();
    MorfologikSpanishSpellerRule rule = new MorfologikSpanishSpellerRule(TestTools.getMessages("es"), language, null,
        Collections.emptyList());
    JLanguageTool lt = new JLanguageTool(language);

    assertEquals(0, rule.match(lt.getAnalyzedSentence(
        "Escriba un texto aquí. LanguageTool le ayudará a afrontar algunas dificultades propias de la escritura.")).length);

    assertEquals(0, rule.match(lt
        .getAnalyzedSentence("Hagámosle, deme, démelo, europeízate, homogenéizalo. Anúnciate. Desáhucialos.")).length);
    assertEquals(1, rule.match(lt.getAnalyzedSentence("Veíanse")).length); // This is archaic

    assertEquals(0, rule.match(lt.getAnalyzedSentence("En la p. 25, pp. 33-45. Ctrl+A")).length);

    // ignore tagged words not in the speller dictionary ("anillos")
    assertEquals(0, rule.match(lt.getAnalyzedSentence("Del libro de los cinco anillos")).length);

    RuleMatch[] matches = rule.match(lt.getAnalyzedSentence(
        "Se a hecho un esfuerzo para detectar errores tipográficos, ortograficos y incluso gramaticales."));
    assertEquals(1, matches.length);
    assertEquals(59, matches[0].getFromPos());
    assertEquals(71, matches[0].getToPos());
    assertEquals("ortográficos", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence(
        "Se a 😂 hecho un esfuerzo para detectar errores tipográficos, ortograficos y incluso gramaticales."));
    assertEquals(1, matches.length);
    assertEquals(62, matches[0].getFromPos());
    assertEquals(74, matches[0].getToPos());
    assertEquals("ortográficos", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence(
        "Se a 😂😂 hecho un esfuerzo para detectar errores tipográficos, ortograficos y incluso gramaticales."));
    assertEquals(1, matches.length);
    assertEquals(64, matches[0].getFromPos());
    assertEquals(76, matches[0].getToPos());
    assertEquals("ortográficos", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("Juan -el menor- jugó a la pelota."));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("vilbaino."));
    assertEquals("Bilbaíno", matches[0].getSuggestedReplacements().get(0));

    // This needs to be handled with rules for different variants.
    // In Spain is a spelling error, but not in other countries.
    // matches = rule.match(langTool.getAnalyzedSentence("confirmame."));
    // assertEquals("confírmame", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("confirmame."));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("confírmame."));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("DECANTACION."));
    assertEquals("DECANTACIÓN", matches[0].getSuggestedReplacements().get(0));
    matches = rule.match(lt.getAnalyzedSentence("distopia"));
    assertEquals("distopía", matches[0].getSuggestedReplacements().get(0));
    matches = rule.match(lt.getAnalyzedSentence("Aministraciones"));
    assertEquals("Administraciones", matches[0].getSuggestedReplacements().get(0));
    matches = rule.match(lt.getAnalyzedSentence("respostas"));
    assertEquals("respuestas", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("mui"));
    assertEquals("muy", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("finga"));
    assertEquals("finja", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("esque"));
    assertEquals("es que", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("hicistes"));
    assertEquals("[hiciste, hicisteis]", matches[0].getSuggestedReplacements().toString());

    matches = rule.match(lt.getAnalyzedSentence("Windows10"));
    assertEquals("Windows 10", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("windows10"));
    assertEquals("Windows 10", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("Windows1995"));
    assertEquals("Windows 1995", matches[0].getSuggestedReplacements().get(0));

    //FIXME
    //matches = rule.match(lt.getAnalyzedSentence("windows1995"));
    //assertEquals("Windows 1995", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("windows95"));
    assertEquals("Windows 95", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("en1995"));
    assertEquals("en 1995", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("BretañaItinerante"));
    assertEquals("Bretaña Itinerante", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("sigloXXI"));
    assertEquals("siglo XXI", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("gustarÃ\u00ADa"));
    assertEquals("gustaría", matches[0].getSuggestedReplacements().get(0));

    // currencies
    matches = rule.match(lt.getAnalyzedSentence("$100"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("$10,000"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("10,000 USD"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("10,000 EUR"));
    assertEquals(0, matches.length);

    // emojis
    matches = rule.match(lt.getAnalyzedSentence("🧡"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("🚴"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("🏽"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("♂️"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("🎉"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("💛"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("✈️"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("🧡🚴"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("🧡🚴🏽♂️ , 🎉💛✈️"));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("- Prueva"));
    assertEquals(1, matches.length);
    assertEquals(2, matches[0].getFromPos());
    assertEquals(8, matches[0].getToPos());

    matches = rule.match(lt.getAnalyzedSentence("🧡 Prueva"));
    assertEquals(1, matches.length);
    assertEquals(3, matches[0].getFromPos());
    assertEquals(9, matches[0].getToPos());

    // Combining diacritics
    matches = rule.match(lt.getAnalyzedSentence("publicacio\u0301n"));
    assertEquals("publicación", matches[0].getSuggestedReplacements().get(0));
    // Other rare characters
    /*
     * matches = rule.match(lt.getAnalyzedSentence("𝐩𝐮𝐛𝐥𝐢𝐜𝐚𝐜𝐢𝐨́𝐧"));
     * assertEquals("publicación", matches[0].getSuggestedReplacements().get(0));
     * matches = rule.match(lt.getAnalyzedSentence("se daba cuenta c´"));
     * assertEquals("cm", matches[0].getSuggestedReplacements().get(0)); matches =
     * rule.match(lt.getAnalyzedSentence("𝐩𝐮𝐛𝐥𝐢𝐜𝐚𝐜𝐢𝐨𝐧"));
     * assertEquals("publicación", matches[0].getSuggestedReplacements().get(0));
     * matches = rule.match(lt.getAnalyzedSentence(
     * "𝐩𝐮𝐛𝐥𝐢𝐜𝐛𝐥𝐢𝐜𝐚𝐛𝐥𝐢𝐜𝐜𝐢𝐨𝐧𝐛𝐥𝐢𝐜")); assertEquals(1,
     * matches.length); assertEquals(0,
     * matches[0].getSuggestedReplacements().size());
     */

    // special chars
    assertEquals(0, rule.match(lt.getAnalyzedSentence("33° 5′ 40″ N; 32° 59′ 0″ E.")).length);
    assertEquals(0, rule.match(lt.getAnalyzedSentence("33°5′40″N i 32°59′0″E.")).length);
    assertEquals(0, rule.match(lt.getAnalyzedSentence("5·10-³ metros.")).length);

    // hashtags, domain names, mentions
    assertEquals(0, rule.match(lt.getAnalyzedSentence("Una #mecion de @algunamigo en es.wikipedia.org")).length);
    assertEquals(0, rule.match(lt.getAnalyzedSentence("documentos publicados en ADSLZone.net")).length);
    assertEquals(0,
        rule.match(lt.getAnalyzedSentence("Europa-Agricola.es es una página web dedicada a los anuncios")).length);
    assertEquals(0, rule.match(lt.getAnalyzedSentence("LANGUAGETOOL.ORG")).length);

    matches = rule.match(lt.getAnalyzedSentence("Martin Scorsese"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("Chris Martin"));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence(
        "Chris Martin. Johann Martin Schleyer. Martin Beck. Bob Martin. Gooey Martin. Skip Martin. "
            + "Martin Chivers. Dora Martin. Vincent Martin. George R. Martin. Martin Schulz. "
            + "Martin Rütter. Martin Walser. Rudolph Martin. Martin Blank. Martin Luther King. "
            + "Gregor Martin. Martin Bishop. Ricky Martin. Martin Fierro. Chris Martin."));
    assertEquals(0, matches.length);
    // Martin van Heemskerck. Martin von Klaus. Martin Sylvester Huggins.

    matches = rule.match(lt.getAnalyzedSentence("Martin"));
    assertEquals(1, matches.length);
    assertEquals("[Martín, Mártir, Martina, Mastín, Marin, Marlín, Marti, Martins, Martinů, Martiño, Martí, Marvin, Marín, Martini]", matches[0].getSuggestedReplacements().toString());

    matches = rule.match(lt.getAnalyzedSentence("Dnipro"));
    assertEquals(1, matches.length);
    assertEquals("[Dnipró]", matches[0].getSuggestedReplacements().toString());

    matches = rule.match(lt.getAnalyzedSentence("Dnepr"));
    assertEquals(1, matches.length);
    assertEquals("Dniéper", matches[0].getSuggestedReplacements().get(0));

    matches = rule.match(lt.getAnalyzedSentence("don't, doesn't, don’t, doesn’t"));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("l'Alacantí, l’Alacantí"));
    assertEquals(0, matches.length);

    matches = rule.match(lt.getAnalyzedSentence("medices algo"));
    assertEquals(1, matches.length);
    assertEquals("Me dices", matches[0].getSuggestedReplacements().get(0));

    // coloquialism allowed, but not suggested
    matches = rule.match(lt.getAnalyzedSentence("El munipa"));
    assertEquals(0, matches.length);
    matches = rule.match(lt.getAnalyzedSentence("El munipe"));
    assertEquals(1, matches.length);
    assertFalse(matches[0].getSuggestedReplacements().contains("munipa"));
  }

}
