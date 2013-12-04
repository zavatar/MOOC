import java.util.Arrays;
import java.util.ArrayList;

public class BoggleSolver
{
    private static final int[] scores = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    private static final int R = 26;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    private Node root = new Node();

    private BoggleBoard board;
    private int[][] colors;

    private boolean contains(String key) {
        return get(key) != null;
    }

    private Integer get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Integer) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char j = key.charAt(d);
        return get(x.next[j-'A'], key, d+1);
    }

    private void put(String key, Integer val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Integer val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char j = key.charAt(d);
        x.next[j-'A'] = put(x.next[j-'A'], key, val, d+1);
        return x;
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary)
            put(word, 0);
    }

    private void fillColors(int color) {
        for (int i = 0; i < colors.length; i++)
            Arrays.fill(colors[i], color);
    }

    private boolean isInBoard(int i, int j) {
        return (i >= 0 && i < board.rows()) && (j >= 0 && j < board.cols());
    }

    private void DFA(int i, int j, Node x, String key, ArrayList<String> ret, ArrayList<Node> nodes) {
        char ch = board.getLetter(i, j);
        if (ch == 'Q') {
            if (x.next['Q'-'A'] != null)
                DFS(i, j, x.next['Q'-'A'].next['U'-'A'], key + "QU", ret, nodes);
        } else
            DFS(i, j, x.next[ch-'A'], key + ch, ret, nodes);
    }

    private void DFS(int i, int j, Node x, String key, ArrayList<String> ret, ArrayList<Node> nodes) {
        if (x == null) return;
        int l = key.length();
        if (x.val == 0 && l >= 3) {
            ret.add(key);
            nodes.add(x);
            x.val = 1;
        }

        colors[i][j] = 1;
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int m = i + r;
                int n = j + c;
                if (isInBoard(m, n)) {
                    if (colors[m][n] == 0) {
                        DFA(m, n, x, key, ret, nodes);
                    }
                }
            }
        }
        colors[i][j] = 0;
    }

    private Iterable<String> parallelSearch() {
        ArrayList<String> ret = new ArrayList<String>();
        ArrayList<Node> nodes = new ArrayList<Node>();
        // parallel DFS search Board and Trie
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                DFA(i, j, root, "", ret, nodes);
            }
        }
        for (Node v : nodes) v.val = 0;
        return ret;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        colors = new int[board.rows()][board.cols()];
        fillColors(0);
        return parallelSearch();
    }

    private int score(String word) {
        return scores[Math.min(scores.length-1, word.length())];
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (contains(word))
            return score(word);
        else
            return 0;
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
