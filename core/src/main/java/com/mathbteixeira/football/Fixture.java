package com.mathbteixeira.football;

public class Fixture {
    public Club homeTeam;
    public Club awayTeam;
    public int homeScore;
    public int awayScore;
    public boolean played = false;

    public Fixture() {} // Required for saving

    public Fixture(Club homeTeam, Club awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }
}
