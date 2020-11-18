public class Test {


    public static void main(String[] args) {
        Term[] testW = {new Term("a", 11), new Term("ab", 11), new Term("hej", 12), new Term("hej", 13), new Term("hej", 11), new Term("tjej", 11)};
        Term[] testUnS = {new Term("groda", 22), new Term("gab", 0), new Term("ask", 0), new Term("groda", 33), new Term("trollslända", 0), new Term("solen", 0), new Term("groda", 11), new Term("SäL", 0), new Term("tomten", 33), new Term("öö", 11), new Term("naTT", 0), new Term("Aline", 20)};
        //System.out.println(RangeBinarySearch.firstIndexOf(testW, new Term("hej", 22), Term.byLexicographicOrder()));
        //System.out.println(RangeBinarySearch.lastIndexOf(testW, new Term("hej", 22), Term.byLexicographicOrder()));
        Autocomplete ac = new Autocomplete(testUnS);
        for (Term term : testUnS) {
            System.out.println(term.toString());
        }
        System.out.println(RangeBinarySearch.firstIndexOf(testUnS, new Term("groda", 15), Term.byLexicographicOrder()));

        Term[] matches= ac.allMatches("gr");
        for (Term term : matches) {
            System.out.println(term.toString());
        }
        

    }
}
