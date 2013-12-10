public class CircularSuffixArray {

    private final char[] text;
    private final int[] index;
    private final int N;
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        N = s.length();
        s = s + '\0';
        text = s.toCharArray();
        index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;
        insertion(0, N-1, 0);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // is text[i+d..i-1) < text[j+d..i-1) ?
    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        int _i = i;
        int _j = j;
        i = i + d;
        j = j + d;
        while (i != _i && j != _j || d == 0) {
            if (text[i] < text[j]) return true;
            if (text[i] > text[j]) return false;
            i = (i + 1) % N;
            j = (j + 1) % N;
            d = -1;
        }
        return i > j;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    // length of s
    public int length() {
        return N;
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