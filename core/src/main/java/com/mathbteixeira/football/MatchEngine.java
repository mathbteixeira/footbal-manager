package com.mathbteixeira.football;

import java.util.Random;

public class MatchEngine {
    private static final Random random = new Random();

    // Returns true if your club wins, false if you lose or draw
    public static boolean simulateMatch(Club myClub, int opponentRating) {
        int myScore = myClub.getTeamRating() + random.nextInt(20);
        int oppScore = opponentRating + random.nextInt(20);
        return myScore > oppScore;
    }
}
