import java.io.IOException;

class Test {

    public static void main(String[] args) throws IOException {
        WordLadder ladder = new WordLadder(args[0]);
        System.out.println(ladder.toString());
        System.out.println(ladder.guessCost("mamma", "pappa"));


        /*
        int iterations = 0;
        while (ladder.dictionary.iterator().hasNext()) {
            iterations++;

            System.out.println(ladder.dictionary.iterator().next());

            if (iterations > 10000){
                break;
            }
        }*/


    }
}