package com.mathbteixeira.football;

public class Player {
    public String name;
    public int overall;
    public int price;

    // 'transient' tells libGDX's JSON serializer to ignore this field
    public transient Club currentClub;

    // Required empty constructor for JSON loading
    public Player() {}

    public Player(String name, int overall, int price, Club club) {
        this.name = name;
        this.overall = overall;
        this.price = price;
        this.currentClub = club;
        if (club != null) {
            club.squad.add(this);
        }
    }
}
