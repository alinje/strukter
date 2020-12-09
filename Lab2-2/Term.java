
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
        if(len < 1){
            return "";
        }
        else if(len > word.length()){
            return word;
        }
        else {
            return word.substring(0, len);
        }
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


        return new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                return  o1.getPrefix(k).compareToIgnoreCase(o2.getPrefix(k));
                /*
                if(o1.word.isEmpty() && !o2.word.isEmpty()){
                    //basically return length of o2
                    return o1.word.compareToIgnoreCase(o2.getPrefix(k));
                } else if(!o1.word.isEmpty() && o2.word.isEmpty()) {
                    //basically return length of o1
                    return o1.getPrefix(k).compareToIgnoreCase(o2.word);
                } else if (o1.word.isEmpty() && o2.word.isEmpty()){
                    return 0;
                } else {
                    int nK = k;
                    if(o1.word.length() < nK){
                        nK = o1.word.length();
                    }
                    if(o2.word.length() < nK){
                        nK = o2.word.length();
                    }

                    return  o1.getPrefix(nK).compareToIgnoreCase(o2.getPrefix(nK));
                }
                 */

            }


        };

    }

    // Returns a string representation of this t0erm in the following format:
    // the weight, followed by whitespace, followed by the word.
    public String toString() {
        return String.format("%12d    %s", this.getWeight(), this.getWord());
    }

}
