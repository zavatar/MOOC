import java.util.ArrayList;
import java.util.Arrays;

public class SAP {

    private final Digraph mG;

    // constructor takes a digraph (not necessarily a DAmG)
    public SAP(Digraph mG) {
        this.mG = mG;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] ret = new int[2];
        ArrayList<Integer> vl = new ArrayList<Integer>(1);
        vl.add(v);
        ArrayList<Integer> wl = new ArrayList<Integer>(1);
        wl.add(w);
        sap(vl, wl, ret);
        return ret[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] ret = new int[2];
        ArrayList<Integer> vl = new ArrayList<Integer>(1);
        vl.add(v);
        ArrayList<Integer> wl = new ArrayList<Integer>(1);
        wl.add(w);
        sap(vl, wl, ret);
        return ret[0];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] ret = new int[2];
        sap(v, w, ret);
        return ret[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] ret = new int[2];
        sap(v, w, ret);
        return ret[0];
    }

    private void rangeCheck(int v) {
        if (v < 0 || v >= mG.V())
            throw new IndexOutOfBoundsException();
    }

    private void sap(Iterable<Integer> v, Iterable<Integer> w, int[] ret) {
        boolean[] color = new boolean[mG.V()];
        int[] marked = new int[mG.V()];
        Arrays.fill(marked, -1);
        ret[0] = -1;
        ret[1] = Integer.MAX_VALUE;

        Queue<Integer> qv = new Queue<Integer>();
        for (int i : v) {
            rangeCheck(i);
            color[i] = true;
            marked[i] = 0;
            qv.enqueue(i);
        }
        Queue<Integer> qw = new Queue<Integer>();
        for (int j : w) {
            rangeCheck(j);
            color[j] = false;
            if (marked[j] != -1) {
                ret[0] = j;
                ret[1] = 0;
                return;
            }
            marked[j] = 0;
            qw.enqueue(j);
        }

        int level = 0;
        while (!(qv.isEmpty() && qw.isEmpty())) {
            while (!qv.isEmpty() && marked[qv.peek()] == level) {
                int i = qv.dequeue();
                for (int j : mG.adj(i)) {
                    if (marked[j] == -1) {
                        color[j] = true;
                        marked[j] = level+1;
                        qv.enqueue(j);
                    } else if (!color[j] && ret[1] > marked[j] + level + 1) {
                        ret[0] = j;
                        ret[1] = marked[j] + level + 1;
                    }
                }
            }
            while (!qw.isEmpty() && marked[qw.peek()] == level) {
                int i = qw.dequeue();
                for (int j : mG.adj(i)) {
                    if (marked[j] == -1) {
                        color[j] = false;
                        marked[j] = level+1;
                        qw.enqueue(j);
                    } else if (color[j] && ret[1] > marked[j] + level + 1) {
                        ret[0] = j;
                        ret[1] = marked[j] + level + 1;
                    }
                }
            }
            level++;
        }
        if (ret[0] == -1) ret[1] = -1;
    }

    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}