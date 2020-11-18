
import java.util.Comparator;

public class Term {
    private String word;
    private long weight;

    // Initializes a term with a given word and weight.
    public Term(String word, long weight) {
        this.word = word;
        this.weight = weight;
    }

    // Gets the word.
    public String getWord() {
        return word;
    }

    // Gets the weight.
    public long getWeight() {
        return weight;
    }

    // Extracts a prefix from the word.
    public String getPrefix(int len) {
        /* TODO */
        return null;
    }

    // Compares the two terms in case-insensitive lexicographic order.
    public static Comparator<Term> byLexicographicOrder() {
        
        Comparator<Term> comp = (one, other) ->
            one.word.compareToIgnoreCase(other.word);

        return comp;
        
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        
        Comparator<Term> comp = (one, other) ->
            Long.compare(one.weight, other.weight);

        comp = comp.reversed();
        return comp;

    }

    // Compares the two terms in case-insensitive lexicographic order,
    // but using only the first k characters of each word.
    public static Comparator<Term> byPrefixOrder(int k) {

        Comparator<Term> comp = (one, other) -> 
            one.word.substring(0, k).compareToIgnoreCase(other.word.substring(0, k));
        
        return comp;

    }

    // Returns a string representation of this t0erm in the following format:
    // the weight, followed by whitespace, followed by the word.
    public String toString() {
        return String.format("%12d    %s", this.getWeight(), this.getWord());
    }

}
