package com.mathbteixeira.football;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class League {
    public List<Club> allClubs = new ArrayList<>();
    public Club myClub;
    private Random random = new Random();

    public void initializeLeague() {
        Club arsenal = new Club("Arsenal", 100000000);
        new Player("Saka", 88, 85000000, arsenal);
        new Player("Odegaard", 87, 75000000, arsenal);
        new Player("Saliba", 86, 70000000, arsenal);

        Club city = new Club("Man City", 200000000);
        new Player("Haaland", 91, 150000000, city);
        new Player("De Bruyne", 90, 100000000, city);
        new Player("Rodri", 89, 90000000, city);

        Club liverpool = new Club("Liverpool", 100000000);
        new Player("Salah", 89, 90000000, liverpool);
        new Player("Alisson", 88, 60000000, liverpool);
        new Player("Van Dijk", 88, 65000000, liverpool);

        myClub = new Club("My Custom FC", 150000000);
        new Player("Average Joe", 70, 1000000, myClub);
        new Player("Rookie Bob", 68, 500000, myClub);

        allClubs.add(arsenal);
        allClubs.add(city);
        allClubs.add(liverpool);
        allClubs.add(myClub);
    }

    public boolean executeTransfer(Player targetPlayer) {
        if (targetPlayer.currentClub == myClub) return false;

        if (myClub.balance >= targetPlayer.price) {
            myClub.balance -= targetPlayer.price;
            targetPlayer.currentClub.balance += targetPlayer.price;

            targetPlayer.currentClub.squad.remove(targetPlayer);
            targetPlayer.currentClub = myClub;
            myClub.squad.add(targetPlayer);
            return true;
        }
        return false;
    }

    public String simulateMatchday() {
        Club opponent;
        do {
            opponent = allClubs.get(random.nextInt(allClubs.size()));
        } while (opponent == myClub);

        int myScore = calculateGoals(myClub.getTeamRating());
        int oppScore = calculateGoals(opponent.getTeamRating());

        if (myScore > oppScore) {
            myClub.balance += 2000000;
            return "VICTORY! " + myScore + " - " + oppScore + " vs " + opponent.name + "\n(+$2,000,000)";
        } else if (myScore == oppScore) {
            myClub.balance += 500000;
            return "DRAW. " + myScore + " - " + oppScore + " vs " + opponent.name + "\n(+$500,000)";
        } else {
            return "DEFEAT. " + myScore + " - " + oppScore + " vs " + opponent.name;
        }
    }

    private int calculateGoals(int teamRating) {
        int baseChance = (teamRating - 50) / 10;
        return Math.max(0, baseChance + random.nextInt(4) - 1);
    }
}
