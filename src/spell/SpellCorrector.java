package spell;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {


    public Trie myDict;
    Vector<String> validEdits;
    Vector<Integer> editCounts;
    Vector<String> validEdits2;
    Vector<Integer> editCounts2;

    public SpellCorrector() {
        myDict = new Trie();
        validEdits = new Vector();
        editCounts = new Vector();
        validEdits2 = new Vector();
        editCounts2 = new Vector<>();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {


        Scanner dict = new Scanner(new FileReader(dictionaryFileName));
        String word;

        while (dict.hasNext()) {
            word = dict.next();
            word = word.toLowerCase();
            myDict.add(word);
        }


    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if (inputWord == null) {
            return null;
        }
        if (inputWord.length() == 0) {
            return null;
        }

        inputWord = inputWord.toLowerCase();
        if (myDict.find(inputWord) != null) {
            return inputWord;
        }
        else {
            Set<String> deletions = new HashSet<>();
            deletions = deleteWords(inputWord);
            Set<String> transpositions = new HashSet<>();
            if (inputWord.length() >= 2) {
                transpositions = transposeWords(inputWord);
            }
            Set<String> alterations = new HashSet<>();
            alterations = alterWords(inputWord);
            Set<String> insertions = new HashSet<>();
            insertions = insertWords(inputWord);

            Set<String> allVars = new HashSet<>();
            allVars.addAll(deletions);
            allVars.addAll(transpositions);
            allVars.addAll(alterations);
            allVars.addAll(insertions);

            for (String potentialWord : allVars) {
                INode searching = myDict.find(potentialWord);
                if (searching != null) {
                    validEdits.add(potentialWord);
                    int amount = myDict.find(potentialWord).getValue();
                    editCounts.add(amount);
                }
            }
            if (validEdits.size() > 1) {
                int maxCount = Collections.max(editCounts);
                int indexOfMax = editCounts.indexOf(maxCount);
                String bestSuggestion = validEdits.get(indexOfMax);
                validEdits.clear();
                editCounts.clear();
                return bestSuggestion;
            }
            else if (validEdits.size() == 1) {
                String bestSuggestion = validEdits.get(0);
                validEdits.clear();
                editCounts.clear();
                return bestSuggestion;
            }
            else {
                //if no valid edit distance 1 words
                Set<String> allVars2 = new HashSet<>();
                Set<String> deletions2 = new HashSet<>();
                Set<String> transpositions2 = new HashSet<>();
                Set<String> alterations2 = new HashSet<>();
                Set<String> insertions2 = new HashSet<>();
                for (String eachWord : allVars) {
                    deletions2 = deleteWords(eachWord);
                    if (eachWord.length() >= 2) {
                        transpositions2 = transposeWords(eachWord);
                    }
                    alterations2 = alterWords(eachWord);
                    insertions2 = insertWords(eachWord);

                    allVars2.addAll(deletions2);
                    allVars2.addAll(transpositions2);
                    allVars2.addAll(alterations2);
                    allVars2.addAll(insertions2);
                }
                for (String potentialWord2 : allVars2) {
                    INode searching2 = myDict.find(potentialWord2);
                    if (searching2 != null) {
                        validEdits2.add(potentialWord2);
                        int amount2 = myDict.find(potentialWord2).getValue();
                        editCounts2.add(amount2);
                    }
                }
                if (validEdits2.size() > 1) {
                    int maxCount2 = Collections.max(editCounts2);
                    int indexOfMax2 = editCounts2.indexOf(maxCount2);
                    String bestSuggestion2 = validEdits2.get(indexOfMax2);
                    return bestSuggestion2;
                }
                else if (validEdits2.size() == 1) {
                    String bestSuggestion2 = validEdits2.get(0);
                    return bestSuggestion2;
                }
                else {
                    return null;
                }
            }
        }
}

    public Set<String> deleteWords(String word) {

        Set<String> deleted = new HashSet<>();
        String working;
        StringBuilder current = new StringBuilder(word);
        for (int i = 0; i < word.length(); ++i) {
            working = String.valueOf(current.deleteCharAt(i));
            deleted.add(working);
            current = new StringBuilder(word);
        }
        return deleted;
    }

    public Set<String> transposeWords(String word) {

        Set<String> transposed = new HashSet<>();
        String working;
        StringBuilder current = new StringBuilder(word);
        for (int i = 0; i < word.length(); i+=2) {
            if (i == 0) {
                char holder = current.charAt(i);
                current.setCharAt(i, word.charAt(i+1));
                current.setCharAt((i+1), holder);
                working = current.toString();
                transposed.add(working);
                current = new StringBuilder(word);
            }
            else if ((i+1) == word.length()) {
                char holder = current.charAt(i);
                current.setCharAt(i, word.charAt(i-1));
                current.setCharAt((i-1), holder);
                working = current.toString();
                transposed.add(working);
                current = new StringBuilder(word);
            }
            else {
                char holder1 = word.charAt(i);
                current.setCharAt(i, word.charAt(i+1));
                current.setCharAt((i+1), holder1);
                working = current.toString();
                transposed.add(working);
                current = new StringBuilder(word);

                char holder = word.charAt(i);
                current.setCharAt(i, word.charAt(i-1));
                current.setCharAt((i-1), holder);
                working = current.toString();
                transposed.add(working);
                current = new StringBuilder(word);
            }
        }
        return transposed;
    }

    public Set<String> alterWords(String word) {

        Set<String> altered = new HashSet<>();
        String working;
        StringBuilder current = new StringBuilder(word);
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != 'a') {
                current.replace(i, i+1, "a");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'b') {
                current.replace(i, i+1, "b");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'c') {
                current.replace(i, i+1, "c");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'd') {
                current.replace(i, i+1, "d");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'e') {
                current.replace(i, i+1, "e");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'f') {
                current.replace(i, i+1, "f");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'g') {
                current.replace(i, i+1, "g");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'h') {
                current.replace(i, i+1, "h");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'i') {
                current.replace(i, i+1, "i");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'j') {
                current.replace(i, i+1, "j");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'k') {
                current.replace(i, i+1, "k");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'l') {
                current.replace(i, i+1, "l");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'm') {
                current.replace(i, i+1, "m");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'n') {
                current.replace(i, i+1, "n");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'o') {
                current.replace(i, i+1, "o");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'p') {
                current.replace(i, i+1, "p");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'q') {
                current.replace(i, i+1, "q");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'r') {
                current.replace(i, i+1, "r");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 's') {
                current.replace(i, i+1, "s");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 't') {
                current.replace(i, i+1, "t");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'u') {
                current.replace(i, i+1, "u");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'v') {
                current.replace(i, i+1, "v");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'w') {
                current.replace(i, i+1, "w");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'x') {
                current.replace(i, i+1, "x");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'y') {
                current.replace(i, i+1, "y");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
            if (word.charAt(i) != 'z') {
                current.replace(i, i+1, "z");
                working = current.toString();
                altered.add(working);
                current = new StringBuilder(word);
            }
        }
        return altered;
    }

    public Set<String> insertWords(String word) {

        Set<String> inserted = new HashSet<>();
        String working;
        StringBuilder current = new StringBuilder(word);
        for (int i = 0; i < (word.length()+1); i++) {
            current.insert(i, 'a');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'b');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'c');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'd');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'e');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'f');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'g');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'h');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'i');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'j');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'k');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'l');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'm');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'n');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'o');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'p');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'q');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'r');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 's');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 't');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'u');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'v');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'w');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'x');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'y');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);

            current.insert(i, 'z');
            working = current.toString();
            inserted.add(working);
            current = new StringBuilder(word);
        }
        return inserted;
    }
}
