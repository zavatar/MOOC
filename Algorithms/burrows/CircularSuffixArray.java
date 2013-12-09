public class CircularSuffixArray {

    private final int[] index;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        SuffixArrayX sa = new SuffixArrayX(s);
        //SuffixArray sa = new SuffixArray(s);
        index = new int[s.length()];
        for (int i = 0; i < s.length(); i++)
            index[i] = sa.index(i);
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            StdOut.println(csa.index(i));
    }
}