package com.mathbteixeira.football;

import com.badlogic.gdx.Game;

public class FootballManagerGame extends Game {
    public League league;

    @Override
    public void create() {
        league = new League();
        league.initializeLeague();

        this.setScreen(new MarketScreen(this));
    }
}
