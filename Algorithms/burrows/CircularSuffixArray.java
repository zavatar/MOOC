public class CircularSuffixArray {
    // cutoff to insertion sort (any value between 0 and 12)
    private static final int CUTOFF =  5;
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
        sort(0, N-1, 0);
    }

    private char circularText(int i, int d) {
        return text[ (i + d) % N ];
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) { 
        if (d == N) return;

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        char v = circularText(index[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = circularText(index[i], d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(lo, lt-1, d);
        sort(lt, gt, d+1);
        sort(gt+1, hi, d);
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
        for (int k = d; k < N; k++) {
            if (circularText(i, k) < circularText(j, k)) return true;
            if (circularText(i, k) > circularText(j, k)) return false;
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