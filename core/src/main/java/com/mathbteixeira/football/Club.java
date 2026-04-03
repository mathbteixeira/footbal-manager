package com.mathbteixeira.football;

import java.util.ArrayList;
import java.util.List;

public class Club {
    public String name;
    public int balance;
    public List<Player> squad = new ArrayList<>();

    // League Table Stats
    public int points = 0;
    public int wins = 0;
    public int draws = 0;
    public int losses = 0;
    public int goalsFor = 0;
    public int goalsAgainst = 0;

    public Club() {} // Required for JSON

    public Club(String name, int balance) {
        this.name = name;
        this.balance = balance;
    }

    public int getTeamRating() {
        if (squad.isEmpty()) return 50;
        int total = 0;
        for (Player p : squad) total += p.overall;
        return total / squad.size();
    }

    // Helper to calculate Goal Difference dynamically
    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }
}
