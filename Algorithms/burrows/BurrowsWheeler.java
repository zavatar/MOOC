import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        char[] out = new char[s.length()];
        int id = 0;
        for (int i = 0; i < s.length(); i++) {
            int k = csa.index(i);
            if (k == 0) {
                id = i;
                out[i] = s.charAt(s.length()-1);
            } else {
                out[i] = s.charAt(k-1);
            }
        }
        BinaryStdOut.write(id);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(out[i]);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int id = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] col1 = s.toCharArray();
        int[] next = new int[n];

        // counting, O(n)
        int[] counts = new int[256];
        Arrays.fill(counts, 0);
        for (int i = 0; i < n; i++)
            counts[s.charAt(i)]++;
        // scan, O(256)
        for (int i = 1; i < 256; i++)
            counts[i] += counts[i-1];
        // sort col1 and generate next, O(n)
        for (int i = n-1; i >= 0; i--) {
            char c = s.charAt(i);
            int j = --counts[c];
            next[j] = i;
            col1[j] = c;
        }
        
        // construct O(n)
        for (int i = 0; i < n; id = next[id], i++)
            BinaryStdOut.write(col1[id]);
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
    	if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}