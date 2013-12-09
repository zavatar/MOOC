import java.util.Arrays;

public class MoveToFront {

    private static char[] initMtf() {
        char[] mtf = new char[256];
        for (char c = 0; c < 256; c++)
            mtf[c] = c;
        return mtf;
    }

    private static char[] read() {
        String s = BinaryStdIn.readString();
        return s.toCharArray();
    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] in = read();
        char[] mtf = initMtf();
        for (int i = 0; i < in.length; i++) {
            char c = 0;
            for (char k = 0; k < 256; k++) {
                char tmp = mtf[k];
                mtf[k] = c; c = tmp;
                if (c == in[i]) {
                    mtf[0] = c;
                    StdOut.print(k);
                    break;
                }
            }
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] in = read();
        char[] mtf = initMtf();
        for (int i = 0; i < in.length; i++) {
            char c = 0;
            for (char k = 0; k < 256; k++) {
                char tmp = mtf[k];
                mtf[k] = c; c = tmp;
                if (k == in[i]) {
                    mtf[0] = c;
                    StdOut.print(c);
                    break;
                }
            }
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}