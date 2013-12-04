import java.util.Arrays;
import java.util.ArrayList;

public class BoggleSolver
{
    private static int[] scores = {0, 0, 0, 1, 1, 2, 3, 5, 11};

    private final TST<Integer> dicts;
    private BoggleBoard board;
    private int[][] colors;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dicts = new TST<Integer>();
        for (String word : dictionary)
            dicts.put(word, 0);
    }

    private void fillColors(int color) {
        for (int i = 0; i < colors.length; i++)
            Arrays.fill(colors[i], color);
    }

    private boolean isInBoard(int r, int c) {
        return (r >= 0 && r < board.rows()) && (c >= 0 && c < board.cols());
    }

    private boolean DFS(int r, int c, String w, int d) {
        if (board.getLetter(r, c) == w.charAt(d)) {
            if (d == w.length() - 1) {
                if (w.length() < 3 || w.charAt(d) == 'Q') return false;
                dicts.put(w, scoreOf(w));
                return true;
            }
            if (w.charAt(d) == 'Q') {
                d++;
                if (w.charAt(d) != 'U')
                    return false;
            }
        } else
            return false;

        colors[r][c] = 1;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                int m = r + y;
                int n = c + x;
                if (isInBoard(m, n))
                    if (colors[m][n] == 0 && DFS(m, n, w, d+1))
                        return true;
            }
        }
        colors[r][c] = 0;
        return false;
    }

    private boolean search(String w) {
        fillColors(0);
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                if (DFS(i, j, w, 0))
                    return true;
            }
        }
        return false;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        this.board = board;
        colors = new int[board.rows()][board.cols()];
        ArrayList<String> ret = new ArrayList<String>();
        for (String word : dicts.keys()) {
            if (search(word))
                ret.add(word);
        }
        return ret;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        Integer score = dicts.get(word);
        if (score == null) return 0;
        return scores[Math.min(scores.length-1, word.length())];
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
