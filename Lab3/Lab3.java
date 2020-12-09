import java.util.stream.Stream;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.*;

// The main plagiarism detection program.
// You only need to change buildIndex() and findSimilarity().
public class Lab3 {
    public static void main(String[] args) {
        try {
            String directory;
            if (args.length == 0) {
                System.out.print("Name of directory to scan: ");
                System.out.flush();
                directory = new Scanner(System.in).nextLine();
            } else directory = args[0];
            Path[] paths = Files.list(Paths.get(directory)).toArray(Path[]::new);
            Arrays.sort(paths);

            // Stopwatches time how long each phase of the program
            // takes to execute.
            Stopwatch stopwatch = new Stopwatch();
            Stopwatch stopwatch2 = new Stopwatch();

            // Read all input files
            ScapegoatTree<Path, Ngram[]> files = readPaths(paths);
            stopwatch.finished("Reading all input files");

            // Build index of n-grams (not implemented yet)
            ScapegoatTree<Ngram, ArrayList<Path>> index = buildIndex(files);
            stopwatch.finished("Building n-gram index");

            // Compute similarity of all file pairs
            ScapegoatTree<PathPair, Integer> similarity = findSimilarity(files, index);
            stopwatch.finished("Computing similarity scores");

            // Find most similar file pairs, arranged in
            // decreasing order of similarity
            ArrayList<PathPair> mostSimilar = findMostSimilar(similarity);
            stopwatch.finished("Finding the most similar files");
            stopwatch2.finished("In total the program");

            // Print out some statistics
            System.out.println("\nBalance statistics:");
            System.out.println("  files: " + files.statistics());
            System.out.println("  index: " + index.statistics());
            System.out.println("  similarity: " + similarity.statistics());
            System.out.println("");

            // Print out the plagiarism report!
            System.out.println("Plagiarism report:");
            mostSimilar.stream().limit(50).forEach((PathPair pair) -> {
                System.out.printf("%5d similarity: %s\n", similarity.get(pair), pair);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phase 1: Read in each file and chop it into n-grams.
    static ScapegoatTree<Path, Ngram[]> readPaths(Path[] paths) throws IOException {
        ScapegoatTree<Path, Ngram[]> files = new ScapegoatTree<>();
        for (Path path: paths) {
            String contents = new String(Files.readAllBytes(path));
            Ngram[] ngrams = Ngram.ngrams(contents, 5);
            // Remove duplicates from the ngrams list
            // Uses the Java 8 streams API - very handy Java feature
            // which we don't cover in the course. If you want to
            // learn about it, see e.g.
            // https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#package.description
            // or https://stackify.com/streams-guide-java-8/
            ngrams = Arrays.stream(ngrams).distinct().toArray(Ngram[]::new);
            files.put(path, ngrams);
        }

        return files;
    }

    // Phase 2: build index of n-grams (not implemented yet)
    static ScapegoatTree<Ngram, ArrayList<Path>> buildIndex(ScapegoatTree<Path, Ngram[]> files) {                                // D * K * log N = N log N
        ScapegoatTree<Ngram, ArrayList<Path>> index = new ScapegoatTree<>();
        // TO DO: build index of n-grams
        //K*D*3logN --> O(N*logN)

        for(Path key : files.keys()){          //D
            Ngram[] ngrams = files.get(key);   //1
            for(Ngram nG: ngrams){             //K
                ArrayList<Path> l = null;      //1
                if (!index.contains(nG)){      //log N
                    l = new ArrayList<>();     //1
                } else {
                    l = index.get(nG);          //log N
                }
                l.add(key);                     //1
                index.put(nG,l);                //log N
            }
        }
        return index;
    }

    // Phase 3: Count how many n-grams each pair of files has in common.
    static ScapegoatTree<PathPair, Integer> findSimilarity(ScapegoatTree<Path, Ngram[]> files, ScapegoatTree<Ngram, ArrayList<Path>> index) {
        // TO DO: use index to make this loop much more efficient
        // N.B. Path is Java's class for representing filenames
        // PathPair represents a pair of Paths (see PathPair.java)
        // D*D*K*K = N^2 smallset = 20,000 m = 200,000, h = 4,000,000 N^2 = 400,000,000 ; 40,000,000,000;
        //D*D*N*2*logD^2*logN --> O(D^2*N*logD*logN) paths (array) mostly contains "few" elements
        //most of the times the complexity will be O(Constant*N*logN) --> O(N*logN)
        ScapegoatTree<PathPair, Integer> similarity = new ScapegoatTree<>();

        for(Ngram key : index.keys()){                              //N
            ArrayList<Path> paths = index.get(key);                 //logN
            for(Path p1: paths){                                    //D
                for(Path p2: paths){                                //D
                        PathPair pair = new PathPair(p1, p2);       //1

                        if (!similarity.contains(pair)){            //1
                            similarity.put(pair, 0);            //log D*D
                        }

                        similarity.put(pair, similarity.get(pair)+1); //Log D*D
                }
            }
        }

/*
        for (Path path1: files.keys()) {                                            // D
            for (Path path2: files.keys()) {                                        // D
                if (path1.equals(path2)) continue;                                  // 1
                for (Ngram ngram1: files.get(path1)) {                              // K
                    for (Ngram ngram2: files.get(path2)) {                          // K
                        if (ngram1.equals(ngram2)) {                                // 1
                            PathPair pair = new PathPair(path1, path2);             // 1

                            if (!similarity.contains(pair))                         // log (small constant) = constant
                                similarity.put(pair, 0);                            // also const

                            similarity.put(pair, similarity.get(pair)+1);           // also const
                        }
                    }
                }
            }
        }

 */

        return similarity;
    }

    // Phase 4: find all pairs of files with more than 30 n-grams
    // in common, sorted in descending order of similarity.
    static ArrayList<PathPair> findMostSimilar(ScapegoatTree<PathPair, Integer> similarity) {
        // Find all pairs of files with more than 100 n-grams in common.
        ArrayList<PathPair> mostSimilar = new ArrayList<>();
        for (PathPair pair: similarity.keys()) {
            if (similarity.get(pair) < 30) continue;
            // Only consider each pair of files once - (a, b) and not
            // (b,a) - and also skip pairs consisting of the same file twice
            if (pair.path1.compareTo(pair.path2) <= 0) continue;

            mostSimilar.add(pair);
        }

        // Sort to have the most similar pairs first.
        Collections.sort(mostSimilar, Comparator.comparing((PathPair pair) -> similarity.get(pair)));
        Collections.reverse(mostSimilar);
        return mostSimilar;
    }
}
