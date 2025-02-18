package main;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private Graph graph = new Graph();
    private HashMap<Integer, String> IdtoWordMap = new HashMap<>();
    private HashMap<String, List<Integer>> wordToSynSet = new HashMap<>();

    public WordNet(String synsetFilename, String hyponymsFilename) {
        if (synsetFilename.isEmpty() || hyponymsFilename.isEmpty()) {
            throw new IllegalArgumentException("Synset or hyponyms filename is null.");
        }

        processSynsetFile(synsetFilename);
        processHyponymsFile(hyponymsFilename);
    }

    private void processSynsetFile(String synSetFilename) {
        In synSetFileAllData = new In(synSetFilename);
        while (synSetFileAllData.hasNextLine()) {
            String completeDataRow = synSetFileAllData.readLine();
            String[] unsplitData = completeDataRow.split(",");
            int id = Integer.parseInt(unsplitData[0]);
            String[] words = unsplitData[1].split(" ");

            for (String word : words) {
                List<Integer> synsetIdList = wordToSynSet.get(word);
                if (synsetIdList == null) {
                    synsetIdList = new ArrayList<>();
                    wordToSynSet.put(word, synsetIdList);
                }
                synsetIdList.add(id);
            }

            IdtoWordMap.put(id, unsplitData[1]);
        }
    }

    private void processHyponymsFile(String hyponymsFilename) {
        In hyponymsFileAllData = new In(hyponymsFilename);
        while (hyponymsFileAllData.hasNextLine()) {
            String completeDataRow = hyponymsFileAllData.readLine();
            String[] singularDataPoint = completeDataRow.split(",");
            int hypernymId = Integer.parseInt(singularDataPoint[0]);
            for (String hyponymId : Arrays.asList(singularDataPoint).subList(1, singularDataPoint.length)) {
                graph.addEdge(hypernymId, Integer.parseInt(hyponymId));
            }
        }
    }

    public Set<String> hyponymMaker(String word) {
        Set<String> hypoSet = new HashSet<>();
        Set<Integer> checkedIds = new HashSet<>();
        List<Integer> initialId = wordToSynSet.get(word);
        if (initialId != null) {
            for (int id : initialId) {
                checkId(id, hypoSet, checkedIds);
            }
        }
        return hypoSet;
    }

    private void checkId(int id, Set<String> hyponyms, Set<Integer> traversed) {
        Stack<Integer> stack = new Stack<>();
        stack.add(id);

        while (!stack.isEmpty()) {
            int currentId = stack.pop();
            if (!traversed.contains(currentId)) {
                traversed.add(currentId);
                wordProcessing(currentId, hyponyms);
                processNextId(currentId, hyponyms, traversed);
            }
        }
    }

    private void wordProcessing(int id, Set<String> hyponyms) {
        processId(id, hyponyms, IdtoWordMap);
    }

    private void processNextId(int id, Set<String> hyponyms, Set<Integer> traversed) {
        processId(id, hyponyms, IdtoWordMap);
    }

    private void processId(int id, Set<String> hyponyms, Map<Integer, String> dataMap) {
        Set<Integer> visitedNodes = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.add(id);
        while (!stack.isEmpty()) {
            int currentId = stack.pop();
            if (!visitedNodes.contains(currentId)) {
                visitedNodes.add(currentId);
                String wordString = dataMap.get(currentId);
                List<String> hyponymWordList = new ArrayList<>(Arrays.asList(wordString.split(" ")));
                hyponyms.addAll(hyponymWordList);
                stack.addAll(graph.getNeighbors(currentId));
            }
        }
    }
}