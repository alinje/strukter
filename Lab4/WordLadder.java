
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Set;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * A graph that encodes word ladders.
 *
 * The class does not store the full graph in memory, just a dictionary of words. 
 * The edges are then computed on demand. 
 */

public class WordLadder implements DirectedGraph<String> {

    private Set<String> dictionary;

    private Map<String, List<DirectedEdge<String>>> ladderMap;



    private Set<Character> charset;


    /**
     * Creates a new empty graph.
     */
    public WordLadder() {
        dictionary = new HashSet<>();
        charset = new HashSet<>();
    }


    /**
     * Creates a new word ladder graph from the given dictionary file.
     * The file should contain one word per line, except lines starting with "#".
     * @param file  path to a text file
     */
    public WordLadder(String file) throws IOException {
        ladderMap = new HashMap<>();
        
        dictionary = new HashSet<>();
        charset = new HashSet<>();
        Files.lines(Paths.get(file))
            .filter(line -> !line.startsWith("#"))
            .forEach(word -> addWord(word.trim()));
    }


    /**
     * Adds the {@code word} to the dictionary, if it only contains letters.
     * The word is converted to lowercase.
     * @param word  the word
     */
    public void addWord(String word) {
        // 
        if (word.matches("\\p{L}+")) {
            word = word.toLowerCase();
            dictionary.add(word);
            for (char c : word.toCharArray()) {
                charset.add(c);
            }
        }
    }


    /**
     * @return the number of words in the dictionary
     */
    public int nNodes() {
        return dictionary.size();
    }


    /**
     * @param  w  a graph node (a word)
     * @return a list of the graph edges that originate from {@code w}
     */
    public List<DirectedEdge<String>> outgoingEdges(String w) {

        List<DirectedEdge<String>> list = new LinkedList<>(); //We create list to add to and later return
        for (int i = 0; i < w.length(); i++) { //We loop through the length of the word
            Iterator<Character> it = charset.iterator(); //We create an iterator it with charset
            while (it.hasNext()) { //We loop through "it"

                char[] word = w.toCharArray(); //convert w to char
                word[i] = it.next(); //change the letter in the word that we are on to the "it" we are on
                String compString = String.valueOf(word); //convert it back to string
                if (dictionary.contains(compString) && !compString.equals(w)){ //If the new word is a "real" word (is in the dictionary) and is not the word itself
                    list.add(new DirectedEdge<String>(w, compString, guessCost(w, compString))); //we add the word to our list
                }
            }
    
        }

        return list; //return the list
        
    }


    /**
     * @param  w  one node/word
     * @param  u  another node/word
     * @return the guessed best cost for getting from {@code w} to {@code u}
     * (the number of differing character positions)
     */
    public double guessCost(String w, String u) {

        int o = Math.min(w.length(), u.length()); //we set o to the value of the word with minimal length
        int lengthDiff = Math.max(w.length(), u.length())-o; //maximal length - minimal length = length differ

        double cost = lengthDiff; //we set cost to the length differ
        for (int i = 0; i < o; i++) { //loop minimal length
            if (w.charAt(i) != u.charAt(i)){ //if a letter does not match
                cost += 1; //we increase cost
            }
        }
        return cost; //we return the cost
        /*

        int o = Math.min(w.length(), u.length());
        int lengthDiff = Math.max(w.length(), u.length())-o;

        double cost = lengthDiff;
        for (int i = 0; i < o; i++) {
            if (w.charAt(i) != u.charAt(i)){
                cost += 1;
            }
        }
        return cost;*/
    }


    /**
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Word ladder with " + nNodes() + " words, " +
                 "charset: \"" + charset.stream().map(x -> x.toString()).collect(Collectors.joining()) + "\"\n\n");
        int ctr = 0;
        s.append("Example words and ladder steps:\n");
        for (String v : dictionary) {
            if (v.length() != 5) continue;
            List<DirectedEdge<String>> edges = outgoingEdges(v);
            if (edges.isEmpty()) continue;
            if (ctr++ > 10) break;
            s.append(v + " --> " + edges.stream().map(e -> e.to()).collect(Collectors.joining(", ")) + "\n");
        }
        return s.toString();
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new WordLadder(args[0]));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java WordLadder dictionary-file");
            System.exit(1);
        }
    }

}
