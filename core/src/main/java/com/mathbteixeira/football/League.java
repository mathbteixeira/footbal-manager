package com.mathbteixeira.football;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class League {
    public List<Club> allClubs = new ArrayList<>();
    public Club myClub;

    // Season Tracking
    public List<MatchWeek> schedule = new ArrayList<>();
    public int currentWeekIndex = 0;

    private Random random = new Random();

    public League() {}

    // --- 1. DATA DRIVEN INITIALIZATION ---
    public void initializeLeague() {
        Json json = new Json();
        FileHandle file = Gdx.files.internal("data.json");

        // Load the list of clubs directly from the JSON file
        ArrayList<Club> loadedClubs = json.fromJson(ArrayList.class, Club.class, file.readString());
        allClubs.addAll(loadedClubs);

        // Hydrate the transient 'currentClub' variables
        for (Club club : allClubs) {
            for (Player player : club.squad) {
                player.currentClub = club;
            }
            // Identify the player's club
            if (club.name.equals("My Custom FC")) {
                myClub = club;
            }
        }

        generateFixtures();
    }

    // --- 2. ROUND-ROBIN SCHEDULING ---
    private void generateFixtures() {
        int numTeams = allClubs.size();
        List<Club> rotatableTeams = new ArrayList<>(allClubs);

        // Create a double round-robin (Home and Away)
        for (int round = 0; round < (numTeams - 1) * 2; round++) {
            MatchWeek week = new MatchWeek();
            for (int i = 0; i < numTeams / 2; i++) {
                Club home = rotatableTeams.get(i);
                Club away = rotatableTeams.get(numTeams - 1 - i);

                // Swap home and away for the second half of the season
                if (round >= numTeams - 1) {
                    week.fixtures.add(new Fixture(away, home));
                } else {
                    week.fixtures.add(new Fixture(home, away));
                }
            }
            schedule.add(week);

            // Rotate teams to generate next week's matches (keep index 0 fixed)
            Club last = rotatableTeams.remove(rotatableTeams.size() - 1);
            rotatableTeams.add(1, last);
        }
    }

    // --- 3. SIMULATION ENGINE ---
    public String simulateMatchday() {
        if (currentWeekIndex >= schedule.size()) {
            return "SEASON OVER! Check the final table.";
        }

        MatchWeek week = schedule.get(currentWeekIndex);
        String myMatchResult = "";

        // Simulate EVERY match in this MatchWeek
        for (Fixture f : week.fixtures) {
            // Home advantage: +3 to base rating
            f.homeScore = calculateGoals(f.homeTeam.getTeamRating() + 3);
            f.awayScore = calculateGoals(f.awayTeam.getTeamRating());
            f.played = true;

            updateClubStats(f.homeTeam, f.awayTeam, f.homeScore, f.awayScore);

            // Save the player's specific result to display on the UI
            if (f.homeTeam == myClub || f.awayTeam == myClub) {
                myMatchResult = f.homeTeam.name + " " + f.homeScore + " - " + f.awayScore + " " + f.awayTeam.name;
            }
        }

        currentWeekIndex++;
        sortStandings();
        return "Matchweek " + currentWeekIndex + " Result:\n" + myMatchResult;
    }

    private void updateClubStats(Club home, Club away, int hGoals, int aGoals) {
        home.goalsFor += hGoals;
        home.goalsAgainst += aGoals;
        away.goalsFor += aGoals;
        away.goalsAgainst += hGoals;

        if (hGoals > aGoals) {
            home.wins++; home.points += 3;
            away.losses++;
            if (home == myClub) myClub.balance += 2000000;
        } else if (hGoals == aGoals) {
            home.draws++; home.points += 1;
            away.draws++; away.points += 1;
            if (home == myClub || away == myClub) myClub.balance += 500000;
        } else {
            away.wins++; away.points += 3;
            home.losses++;
            if (away == myClub) myClub.balance += 2000000;
        }
    }

    private int calculateGoals(int teamRating) {
        int baseChance = (teamRating - 50) / 10;
        return Math.max(0, baseChance + random.nextInt(4) - 1);
    }

    // --- 4. TRANSFERS AND STANDINGS ---
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

    public void sortStandings() {
        allClubs.sort((c1, c2) -> {
            if (c1.points != c2.points) return Integer.compare(c2.points, c1.points);
            return Integer.compare(c2.getGoalDifference(), c1.getGoalDifference());
        });
    }

    // --- 5. SAVING AND LOADING ---
    public void saveGame() {
        Json json = new Json();
        FileHandle file = Gdx.files.local("savegame.json");
        file.writeString(json.toJson(this), false);
    }

    public static League loadGame() {
        FileHandle file = Gdx.files.local("savegame.json");
        if (file.exists()) {
            Json json = new Json();
            League loadedLeague = json.fromJson(League.class, file.readString());
            for (Club club : loadedLeague.allClubs) {
                for (Player player : club.squad) {
                    player.currentClub = club;
                }
                if (club.name.equals(loadedLeague.myClub.name)) {
                    loadedLeague.myClub = club;
                }
            }
            // Re-link teams in the schedule
            for (MatchWeek mw : loadedLeague.schedule) {
                for (Fixture f : mw.fixtures) {
                    f.homeTeam = loadedLeague.allClubs.stream().filter(c -> c.name.equals(f.homeTeam.name)).findFirst().orElse(null);
                    f.awayTeam = loadedLeague.allClubs.stream().filter(c -> c.name.equals(f.awayTeam.name)).findFirst().orElse(null);
                }
            }
            return loadedLeague;
        }
        return null;
    }
}
