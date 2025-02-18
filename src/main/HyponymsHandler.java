package main;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;
    private NGramMap m;

    HyponymsHandler(WordNet wn, NGramMap map) {
        this.wn = wn;
        this.m = map;
    }

    @Override
    public String handle(NgordnetQuery query) {
        int startYear = query.startYear();
        int endYear = query.endYear();
        int k = query.k();
        List<String> k0Result = k0Case(query);
        if (k == 0) {
            return k0Result.toString();
        } else {
            return kCase(k, startYear, endYear, k0Result);
        }
    }

    private List<String> k0Case(NgordnetQuery query) {
        List<String> words = query.words();
        Set<String> hyponymsSet;
        if (words.size() == 1) {
            hyponymsSet = wn.hyponymMaker(words.get(0));
        } else {
            hyponymsSet = new HashSet<>(sharedHypos(words));
        }
        List<String> orderedHypo = new ArrayList<>(hyponymsSet);
        Collections.sort(orderedHypo);
        return orderedHypo;
    }

    private String kCase(int k, int startYear, int endYear, List<String> nonKVersion) {
        Map<String, Double> wordFrequencies = new HashMap<>();
        for (String word : nonKVersion) {
            TimeSeries singleWordCount = m.countHistory(word, startYear, endYear);
            double freq = 0.0;
            for (double value : singleWordCount.values()) {
                freq += value;
            }
            wordFrequencies.put(word, freq);
        }
        List<String> result = stackMaker(k, nonKVersion, wordFrequencies);
        Collections.sort(result); // Put in proper order
        String returnString = result.toString();
        return returnString;
    }
    private List<String> sharedHypos(List<String> wordList) {
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : wordList) {
            Set<String> hyponyms = wn.hyponymMaker(word);
            for (String hyponym : hyponyms) {
                frequency.put(hyponym, frequency.getOrDefault(hyponym, 0) + 1);
            }
        }
        List<String> sharedHyposList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() == wordList.size()) {
                sharedHyposList.add(entry.getKey());
            }
        }
        return sharedHyposList;
    }
    private List<String> stackMaker(int k, List<String> nonKVersion, Map<String, Double> wordFrequencies) {
        Stack<String> stack = new Stack<>();
        // Changed it up to now use a comparator
        nonKVersion.sort(new Comparator<String>() {
            @Override
            public int compare(String word1, String word2) {
                // First compares how many times every word appears
                return Double.compare(wordFrequencies.get(word2), wordFrequencies.get(word1));
            }
        });
        for (int i = 0; i < nonKVersion.size() && stack.size() < k; i++) {
            // Then obtained top K words and assures word itself isn't included
            // Avoids duplicates or including word itself
            String word = nonKVersion.get(i);
            double value = wordFrequencies.get(word);
            if (value != 0.0 && !stack.contains(word)) {
                stack.add(word);
            }
        }
        return new ArrayList<>(stack);
    }
}
