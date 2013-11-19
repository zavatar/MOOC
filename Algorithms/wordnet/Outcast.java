public class Outcast {

    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxd = 0;
        String ret = nouns[0];
        for (String v : nouns) {
            int d = 0;
            for (String w : nouns)
                d += wordnet.distance(v, w);
            if (d > maxd) {
                maxd = d;
                ret = v;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = new In(args[t]).readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}