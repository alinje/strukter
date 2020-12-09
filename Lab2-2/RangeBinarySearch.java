
import java.util.Comparator;

public class RangeBinarySearch {

    // Returns the index of the *first* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N), where N is the length of the array
    public static int firstIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        //lowest index we want to check
        int low = 0;
        //highest index we want to check
        int high = terms.length - 1;
        //index that will hold the return value
        int index = high;
        boolean hasFoundKey = false; //set hasFoundKey to false first, will be true when we find the key

        while(low <= high){             //While low is less or equal to high will we loop
            int mid = (low+high)/2;    //Set mid to the middle element between low and high
            int compareValue = comparator.compare(key, terms[mid]); //we compare the key with the value in the middle

            if(compareValue > 0){ //If compareValue is greater than 0, means that the key is greater than the middle element
                low = mid + 1; //This means that our "new" low is mid + 1, we can ignore the left side of mid (the key isn't there) :)
            } else if(compareValue < 0){ //If compareValue is greater than 0, means that the key is smaller than the middle element
                high = mid - 1; //This means that our "new" high can be mid -1, we can ignore the right side of mid (the key isn't there) :)
            } else { //Else, if compareValue is 0! We have found a value equal to the key
                if(!hasFoundKey){ //If hasFoundKey is false we must now set it to true!
                    hasFoundKey = true; //We set hasFoundKey to true
                }
                index = mid; //We set index to the index (mid) we are on
                high = mid - 1; //Since we want the first index, we put high to mid - 1, to search after more keys to the left of the found key.
            }
        }
        if(!hasFoundKey){ //If we still haven't found the key
            return -1; //We return -1
        }

        return index; //We return index at the ens, which will contain the first index of the matching element.
    }

    // Returns the index of the *last* element in terms[] that equals the search key,
    // according to the given comparator, or -1 if there are no matching elements.
    // Complexity: O(log N)
    public static int lastIndexOf(Term[] terms, Term key, Comparator<Term> comparator) {
        int low = 0;
        //highest index we want to check
        int high = terms.length - 1;
        //index that will hold the return value
        int index = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int compareValue = comparator.compare(key, terms[mid]);

            if (compareValue > 0) {
                low = mid + 1;
            } else if (compareValue < 0) {
                high = mid - 1;
            } else {
                index = mid;
                low = mid + 1;
            }
        }
        return index;
    }

}
