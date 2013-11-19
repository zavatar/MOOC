import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private final SAP mSap;
    private final ArrayList<String> mSynsets;
    private final ArrayList<String> mGlosses;
    private final HashMap<String, ArrayList<Integer>> mNounTable;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        //long start, end; 
        //start = System.currentTimeMillis();
        
        mSynsets = new ArrayList<String>();
        mGlosses = new ArrayList<String>();
        mNounTable = new HashMap<String, ArrayList<Integer>>();
        
        In synsetsin = new In(synsets);
        while (!synsetsin.isEmpty()) {
            String[] line = synsetsin.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            mSynsets.add(line[1]);
            mGlosses.add(line[2]);
            for (String w : line[1].split(" ")) {
                if (mNounTable.get(w) == null)
                    mNounTable.put(w, new ArrayList<Integer>());
                mNounTable.get(w).add(id);
            }
        }
        
        //end = System.currentTimeMillis();
        //System.out.println("Time: "+(end-start)+"ms");
        //start = System.currentTimeMillis();
        
        Digraph g = new Digraph(mSynsets.size());
        
        In hypernymsin = new In(hypernyms);
        while (!hypernymsin.isEmpty()) {
            String[] line = hypernymsin.readLine().split(",");
            int u = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++)
                g.addEdge(u, Integer.parseInt(line[i]));
        }
        
        mSap = new SAP(g);
        
        //end = System.currentTimeMillis();
        //System.out.println("Time: "+(end-start)+"ms");
        //start = System.currentTimeMillis();
        
        DirectedCycle finder = new DirectedCycle(g);
        if (finder.hasCycle())
            throw new IllegalArgumentException();

        int numRoots = 0;
        for (int i = 0; i < g.V(); i++)
            if (!g.adj(i).iterator().hasNext())
                numRoots++;
        if (numRoots != 1)
            throw new IllegalArgumentException();
    }

    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return mNounTable.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return mNounTable.containsKey(word);
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