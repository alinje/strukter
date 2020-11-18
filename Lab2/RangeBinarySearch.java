
import java.util.Comparator;

public class RangeBinarySearch {

    // Returns the index of the *first* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N), where N is the length of the array
    public static int firstIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        //lowest index we want to check
        int bot = 0;
        //highest index we want to check
        int top = terms.length - 1;
        //next index we want to check
        int nextI = terms.length/2;
        //we want to search as long as 
        while(top - bot > 0){
            //update nextI
            nextI = (top + bot)/2;

            //check if value on nextI is equal to key
            if (comparator.compare(key, terms[nextI]) == 0){
                //if that's the case, continue the check on nextI-1 until nextI == 0 
                //or until terms[nextI] is no longer equal to key
                while (true){
                    if (nextI-1 < 0){
                        return 0;
                    }
                    nextI -= 1;
                    if(comparator.compare(key, terms[nextI]) != 0){
                        return nextI +1;
                    }
                }
            }

            //update top and bot
            if (comparator.compare(key, terms[nextI]) == -1){
                top = nextI;
            } else {
                bot = nextI+1;
            }
        }
        return -1;        
    }

    // Returns the index of the *last* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N)
    public static int lastIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        //TODO kanske undvika kodduplicering?
        int bot = 0;
        int top = terms.length - 1;
        int nextI = terms.length/2;
        while(top - bot > 0){
            nextI = (top + bot)/2;

            if (comparator.compare(key, terms[nextI]) == 0){
                //if that's the case, continue the check on nextI+1 until nextI == terms.length 
                //or until terms[nextI] is no longer equal to key
                while (true){
                    if (nextI+1 >= terms.length){
                        return terms.length-1;
                    }
                    nextI += 1;
                    if(comparator.compare(key, terms[nextI]) != 0){
                        return nextI -1;
                    }
                }
            }

            if (comparator.compare(key, terms[nextI]) == -1){
                top = nextI;
            } else {
                bot = nextI+1;
            }
        }
        return -1;  
    }

}
