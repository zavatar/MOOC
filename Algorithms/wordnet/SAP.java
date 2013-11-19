import java.util.*;

public class SAP {

	private Digraph G;

	private void rangeCheck(int v) {
		if (v < 0 || v >= G.V())
			throw new IndexOutOfBoundsException();
	}

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		this.G = G;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		Integer[] ret = new Integer[]{-1, -1};
		ArrayList<Integer> vl = new ArrayList<Integer>(1);
		vl.add(v);
		ArrayList<Integer> wl = new ArrayList<Integer>(1);
		wl.add(w);
		sap_core(vl, wl, ret);
		return ret[1];
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		Integer[] ret = new Integer[]{-1, -1};
		ArrayList<Integer> vl = new ArrayList<Integer>(1);
		vl.add(v);
		ArrayList<Integer> wl = new ArrayList<Integer>(1);
		wl.add(w);
		sap_core(vl, wl, ret);
		return ret[0];
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		Integer[] ret = new Integer[]{-1, -1};
		sap_core(v, w, ret);
		return ret[1];
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		Integer[] ret = new Integer[]{-1, -1};
		sap_core(v, w, ret);
		return ret[0];
	}

	public void sap_core(Iterable<Integer> v, Iterable<Integer> w, Integer[] ret) {
		boolean[] color = new boolean[G.V()];
		Integer[] marked = new Integer[G.V()];
		Arrays.fill(marked, -1);
		ret[0] = ret[1] = Integer.MAX_VALUE;

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
			marked[j] = 0;
			qw.enqueue(j);
		}
		int level = 0; boolean flag = false;
		while (!(qv.isEmpty() && qw.isEmpty())) {
			while (!qv.isEmpty() && marked[qv.peek()] == level) {
				int i = qv.dequeue();
				for (int j : G.adj(i)) {
					if (marked[j] == -1) {
						color[j] = true;
						marked[j] = level+1;
						qv.enqueue(j);
					} else if (color[j] == false && ret[1] > marked[j] + level + 1) {
						ret[0] = j;
						ret[1] = marked[j] + level + 1;
						flag = true;
					}
				}
			}
			while (!qw.isEmpty() && marked[qw.peek()] == level) {
				int i = qw.dequeue();
				for (int j : G.adj(i)) {
					if (marked[j] == -1) {
						color[j] = false;
						marked[j] = level+1;
						qw.enqueue(j);
					} else if (color[j] == true && ret[1] > marked[j] + level + 1) {
						ret[0] = j;
						ret[1] = marked[j] + level + 1;
						flag = true;
					}
				}
			}
			if (flag) return;
			level++;
		}
		ret[0] = ret[1] = -1;
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