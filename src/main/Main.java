package main;

import browser.NgordnetServer;
import ngrams.NGramMap;

public class Main {

    public static void main(String[] args) {
        NgordnetServer hns = new NgordnetServer();
        String hyponymsFile = "./data/ngrams/hyponyms11.csv";
        String synsetFile = "./data/ngrams/synsets11.csv";

        NGramMap ngm = new NGramMap(hyponymsFile, synsetFile);

        // The following code might be useful to you.
        /*
        String countFile = "./data/ngrams/total_counts.csv";
        NGramMap ngm = new NGramMap(wordFile, countFile);
        */
        hns.startUp();

        /*
        hns.register("history", new DummyHistoryHandler());
        hns.register("historytext", new DummyHistoryTextHandler());
        */
        WordNet wn = new WordNet(hyponymsFile, synsetFile);
        hns.register("hyponyms", new HyponymsHandler(wn, ngm));

        System.out.println("Finished server startup! Visit http://localhost:4567/ngordnet.html");
    }
}
// BOTTOM-UP
// Graph class has makes graph and runs operation that is passed onto the wordNet. --> wordnet
// Actually calls functions on the passed in graph --> this is then passed onto the Hyponym handler which
// formats and displays

// TOP-DOWN SD
// Graph instance -->  Wordnet instance --> Hyponym handler, calls functions on WordNet instance given -->
// and calls functions upon the wordnet (defined in graph class)
// wordNet calls functions on graph class
// BOTTOM-Up: Graph class bunch of edges and words  and runs an operation, WordN
