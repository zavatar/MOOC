import java.util.ArrayList;
import java.util.Arrays;

public class SAP {

    private final Digraph mG;

    // constructor takes a digraph (not necessarily a DAmG)
    public SAP(Digraph G) {
        mG = new Digraph(G); //this.mG = mG;
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
        int[] marked1 = new int[mG.V()];
        Arrays.fill(marked1, -1);
        Queue<Integer> q = new Queue<Integer>();
        for (int i : v) {
            rangeCheck(i);
            marked1[i] = 0;
            q.enqueue(i);
        }
        while (!q.isEmpty()) {
            int i = q.dequeue();
            for (int j : mG.adj(i)) {
                if (marked1[j] == -1) {
                    marked1[j] = marked1[i]+1;
                    q.enqueue(j);
                }
            }
        }

        ret[0] = -1;
        ret[1] = Integer.MAX_VALUE;

        int[] marked2 = new int[mG.V()];
        Arrays.fill(marked2, -1);
        for (int i : w) {
            rangeCheck(i);
            marked2[i] = 0;
            if (marked1[i] != -1 && marked1[i]+marked2[i] < ret[1]) {
                ret[0] = i;
                ret[1] = marked1[i]+marked2[i];
            }
            q.enqueue(i);
        }
        while (!q.isEmpty()) {
            int i = q.dequeue();
            for (int j : mG.adj(i)) {
                if (marked2[j] == -1) {
                    marked2[j] = marked2[i]+1;
                    if (marked1[j] != -1 && marked1[j]+marked2[j] < ret[1]) {
                        ret[0] = j;
                        ret[1] = marked1[j]+marked2[j];
                    }
                    q.enqueue(j);
                }
            }
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