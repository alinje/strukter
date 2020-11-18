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
        quickSort(comp, spanL, spanH-1);
    }

    /**
     * 
     * @param comp comparator dictationg how to sort the Terms
     * @param spanL index of the lowest element to sort
     * @param spanH index of the highest element to sort
     */
    private void quickSort(Comparator<Term> comp, int spanL, int spanH){

        if(spanH <= spanL){
            return;
        }

        /*if (dictionary.length < 80){
            insertionSort(dictionary, spanL, spanH-1);
            return;
        }*/

        //find index of median of three. This is the pivot
        int spanM = (spanL + spanH)/2;
        int pivot = medianOfThree(dictionary, spanL, spanM, spanH);

        if(pivot != spanL){
            swap(pivot, spanL);
            pivot = spanL;
        }

        //
        int lowI = spanL+1;
        int highI = spanH;

        //as long as 
        while (highI >= lowI){
            //as long as the pivot is bigger than what lowI is pointing at
            while(comp.compare(dictionary[pivot], dictionary[lowI]) >= 0
                &&highI >= lowI){
                lowI++;
            }
            //as long as the pivot is smaller than what highI is pointing at
            while(comp.compare(dictionary[pivot], dictionary[highI]) <= 0
                &&highI >= lowI){
                highI--;
            }
            if(highI < lowI){
                break;
            }
            //swap the element that is large among small element 
            //with the too small among large elements
            swap(lowI, highI);

        }
        //
        swap(pivot, highI);
        pivot = highI;



        quickSort(comp, spanL, pivot-1); //Sorts left side
        if(pivot > spanH){ //?
            pivot = spanH;
        }
        quickSort(comp, pivot+1, spanH); //Sorts right side


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

    private void swap(int i1, int i2){
        Term temp = dictionary[i1];
        dictionary[i1] = dictionary[i2];
        dictionary[i2] = temp;  
    }


    // Returns all terms that start with the given prefix, in descending order of weight.
    // Complexity: O(log N + M log M), where M is the number of matching terms
    public Term[] allMatches(String prefix) {
        /* TODO */
        return null;
    }

    // Returns the number of terms that start with the given prefix.
    // Complexity: O(log N)
    public int numberOfMatches(String prefix) {
        /* TODO */
        return 0;
    }


	public void insertionSort(Term[] a, int low, int high) {
		Comparator<Term> comp = Term.byLexicographicOrder();
		for (int i = low + 1; i <= high; i++) {
			Term val = a[i];
			int j = i;
			while (j > low && comp.compare(a[j - 1], val) == 1){
                a[j] = a
                [j - 1];
				j--;
			}
            a[j] = val;
      
		}
    }

}
