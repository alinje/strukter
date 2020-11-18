import java.util.Comparator;

public class Autocomplete {
    private Term[] dictionary;

    // Initializes the dictionary from the given array of terms.
    public Autocomplete(Term[] dictionary) {
        this.dictionary = dictionary;
        sortDictionary();
    }

    // Sorts the dictionary in *case-insensitive* lexicographic order.
    // Complexity: O(N log N), where N is the number of terms
    public void sortDictionary() {
        Comparator<Term> comp = Term.byLexicographicOrder();
        int spanL = 0;
        int spanH = dictionary.length;
        quickSort(dictionary, comp, spanL, spanH-1);
    }

    /**
     * 
     * @param comp comparator dictationg how to sort the Terms
     * @param spanL index of the lowest element to sort
     * @param spanH index of the highest element to sort
     */
    private void quickSort(Term[] toSort, Comparator<Term> comp, int spanL, int spanH){

        if(spanH <= spanL){
            return;
        }

        if (toSort.length < 80){
            insertionSort(toSort, comp, spanL, spanH);
            return;
        }

        //find index of median of three. This is the pivot
        int spanM = (spanL + spanH)/2;
        int pivot = medianOfThree(toSort, spanL, spanM, spanH);

        if(pivot != spanL){
            swap(toSort, pivot, spanL);
            pivot = spanL;
        }

        //
        int lowI = spanL+1;
        int highI = spanH;

        //as long as 
        while (highI >= lowI){
            //as long as the pivot is bigger than what lowI is pointing at
            while(comp.compare(toSort[pivot], toSort[lowI]) >= 0
                &&highI >= lowI){
                lowI++;
            }
            //as long as the pivot is smaller than what highI is pointing at
            while(comp.compare(toSort[pivot], toSort[highI]) <= 0
                &&highI >= lowI){
                highI--;
            }
            if(highI < lowI){
                break;
            }
            //swap the element that is large among small element 
            //with the too small among large elements
            swap(toSort, lowI, highI);

        }
        //
        swap(toSort, pivot, highI);
        pivot = highI;



        quickSort(toSort, comp, spanL, pivot-1); //Sorts left side
        if(pivot > spanH){ //?
            pivot = spanH;
        }
        quickSort(toSort, comp, pivot+1, spanH); //Sorts right side


    }
    
    
    
    private int medianOfThree(Term[] t, int index1, int index2, int index3){
        Comparator<Term> comp = Term.byLexicographicOrder();
        if(comp.compare(t[index1], t[index2]) == -1 || comp.compare(t[index1], t[index2]) == 0){
            if(comp.compare(t[index2], t[index3]) == -1 || comp.compare(t[index2], t[index3]) == 0){
                return index2;
            }
            else if(comp.compare(t[index1], t[index3]) == -1 || comp.compare(t[index1], t[index3]) == 0){
                return index3; 
            }
            else return index1; 
        } 
        else {
            if(comp.compare(t[index3], t[index2]) == -1 || comp.compare(t[index3], t[index2]) == 0) {
                return index2;
            }
            else if(comp.compare(t[index3], t[index1]) == -1 || comp.compare(t[index3], t[index1]) == 0){
                return index3; 
            }
            else return index1;
        }
    }

    private void swap(Term[] array, int i1, int i2){
        Term temp = array[i1];
        array[i1] = array[i2];
        array[i2] = temp;  
    }


    // Returns all terms that start with the given prefix, in descending order of weight.
    // Complexity: O(log N + M log M), where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        //sort by prefix order
        quickSort(dictionary, Term.byPrefixOrder(prefix.length()), 0, dictionary.length-1); //idk

        //below is dumb af on many levels
        //find lower and upper bounds of the matches
        int low = RangeBinarySearch.firstIndexOf(dictionary, new Term(prefix, 0), Term.byPrefixOrder(prefix.length()));
        int high = RangeBinarySearch.lastIndexOf(dictionary, new Term(prefix, 0), Term.byPrefixOrder(prefix.length()));

        //make a new array and copy matches to this
        Term[] matches = new Term[high-low+1];
        for (int i = 0; i < matches.length; i++) {
            matches[i] = dictionary[low + i];
        }

        //sort in descending order of weight
        quickSort(matches, Term.byReverseWeightOrder(), 0, matches.length-1);

        //return the matches
        return matches;
    }

    // Returns the number of terms that start with the given prefix.
    // Complexity: O(log N)
    public int numberOfMatches(String prefix) {
        /* TODO */
        return 0;
    }


	public void insertionSort(Term[] toSort, Comparator<Term> comp, int low, int high) {
        //loop through the unsorted elements
		for (int i = low + 1; i <= high; i++) {
            //val is the element we are to put into "the hand"
			Term val = toSort[i];
            int j = i;
            //as long as the element we are to insert is smaller than the element we are looking at, 
            // we keep shuffling these elements to the right in the hand
 			while (j > low && comp.compare(toSort[j - 1], val) > 0){
                toSort[j] = toSort[j - 1];
				j--;
            }
            //when we quit the loop, j is the index where we should put val
            toSort[j] = val;
		}
    }

}
