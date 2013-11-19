import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

public class WordNet {

    private final SAP mSap;
    private final ArrayList<String> mSynsets;
    private final ArrayList<String> mGlosses;
    private final HashSet<String> mNouns;
    private final HashMap<String, ArrayList<Integer>> mNounTable;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        mSynsets = new ArrayList<String>();
        mGlosses = new ArrayList<String>();
        mNouns = new HashSet<String>();
        mNounTable = new HashMap<String, ArrayList<Integer>>();
        
        In synsetsin = new In(synsets);
        while (synsetsin.hasNextLine()) {
            Scanner s = new Scanner(synsetsin.readLine());
            s.useDelimiter(",");
            int id = s.nextInt();
            String synset = s.next();
            String gloss = s.next();
            String[] tokens = synset.split(" ");
            mSynsets.add(synset);
            mGlosses.add(gloss);
            for (String w : tokens) {
                if (mNouns.add(w))
                    mNounTable.put(w, new ArrayList<Integer>());
                mNounTable.get(w).add(id);
            }
        }
        
        Digraph G = new Digraph(mSynsets.size());
        mSap = new SAP(G);
        
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
            //StdOut.println("No cycle");
        }
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return mNouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return mNouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        return mSap.length(mNounTable.get(nounA), mNounTable.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!(isNoun(nounA) && isNoun(nounB)))
            throw new IllegalArgumentException();
        return mSynsets.get(mSap.ancestor(mNounTable.get(nounA), mNounTable.get(nounB)));
    }

    // for unit testing of this class
    public static void main(String[] args) {
        WordNet WN = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            int dist   = WN.distance(v, w);
            String syn = WN.sap(v, w);
            StdOut.printf("distance = %d, synset = %s\n", dist, syn);
        }
    }
}