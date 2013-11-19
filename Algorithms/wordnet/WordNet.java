import java.util.*;

public class WordNet {
    
    private final ArrayList<String> Glosses;
    private final ArrayList<String> Nouns;
    private final Hashtable<String, Integer> HTable;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        Glosses = new ArrayList<String>();
        Nouns = new ArrayList<String>();
        HTable = new Hashtable<String, Integer>();
        
        In synsetsin = new In(synsets);
        while (synsetsin.hasNextLine()) {
            Scanner s = new Scanner(synsetsin.readLine());
            s.useDelimiter(",");
            int id = s.nextInt();
            String synset = s.next();
            String gloss = s.next();
            String[] tokens = synset.split(" ");
            Glosses.add(gloss);
            for (String w : tokens) {
                Nouns.add(w);
                HTable.put(w, new Integer(id));
            }
        }
        
        Digraph G = new Digraph(Glosses.size());
        
        In hypernymsin = new In(hypernyms);
        while (hypernymsin.hasNextLine()) {
            Scanner s = new Scanner(hypernymsin.readLine());
            s.useDelimiter(",");
            int u = s.nextInt();
            while (s.hasNext()) {
                int v = s.nextInt();
                G.addEdge(u, v);
            }
        }
        
        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
            throw new IllegalArgumentException();
        }
        else {
            StdOut.println("No cycle");
        }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return Nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return HTable.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        return "";
    }

    // for unit testing of this class
    public static void main(String[] args) {
        //WordNet WN = new WordNet(args[0], args[1]);
        WordNet WN = new WordNet("synsets.txt", "hypernyms.txt");
    }
}