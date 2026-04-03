package com.mathbteixeira.football;

import java.util.ArrayList;
import java.util.List;

public class Club {
    public String name;
    public int balance;
    public List<Player> squad = new ArrayList<>();

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
}
