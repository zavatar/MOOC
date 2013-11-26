import java.util.HashMap;

public class BaseballElimination {

    private final int N;
    private final String[] Teams;
    private final HashMap<String, Integer> Ids;
    private final int[] W;
    private final int[] L;
    private final int[] R;
    private final int[][] G;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();

        Teams = new String[N];
        Ids = new HashMap<String, Integer>();
        W = new int[N];
        L = new int[N];
        R = new int[N];
        G = new int[N][N];

        for (int l = 0; l < N; l++) {
            Teams[l] = in.readString();
            Ids.put(Teams[l], l);
            W[l] = in.readInt();
            L[l] = in.readInt();
            R[l] = in.readInt();
            for (int i = 0; i < N; i++)
                G[l][i] = in.readInt();
        }
    }

    // number of teams
    public int numberOfTeams() {
        return N;
    }

    // all teams
    public Iterable<String> teams() {
        return Ids.keySet(); // Teams
    }

    private void checkTeam(String team) {
        if (Ids.get(team) == null)
            throw new java.lang.IllegalArgumentException();
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        return W[Ids.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        return L[Ids.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);
        return R[Ids.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return G[Ids.get(team1)][Ids.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        checkTeam(team);
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        return null;
    }

    public static void main(String[] args) {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
        if (division.isEliminated(team)) {
            StdOut.print(team + " is eliminated by the subset R = { ");
            for (String t : division.certificateOfElimination(team))
                StdOut.print(t + " ");
            StdOut.println("}");
        }
        else {
            StdOut.println(team + " is not eliminated");
        }
    }
}

}