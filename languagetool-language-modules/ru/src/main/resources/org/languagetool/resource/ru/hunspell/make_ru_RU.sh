#!/bin/sh
java -cp  languagetool.jar org.languagetool.tools.dictionary.SpellDictionaryBuilder -i dictionary.dump -info ru_RU.info -freq wordlist_ru.xml -o /tmp/ru_RU.dict
