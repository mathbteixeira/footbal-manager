package com.mathbteixeira.football;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StandingsScreen extends ScreenAdapter {
    private FootballManagerGame game;
    private Stage stage;
    private Skin skin;

    public StandingsScreen(FootballManagerGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table mainContainer = new Table();
        mainContainer.setFillParent(true);
        stage.addActor(mainContainer);

        // Title
        Label title = new Label("LEAGUE TABLE", skin);
        title.setFontScale(1.5f);
        mainContainer.add(title).pad(20).row();

        // Ensure the league is sorted before we display it
        game.league.sortStandings();

        // Build the Table Data
        Table tableUI = new Table();

        // Headers
        tableUI.add(new Label("POS", skin)).pad(10);
        tableUI.add(new Label("CLUB", skin)).pad(10).width(150).left();
        tableUI.add(new Label("PL", skin)).pad(10); // Played
        tableUI.add(new Label("W", skin)).pad(10);
        tableUI.add(new Label("D", skin)).pad(10);
        tableUI.add(new Label("L", skin)).pad(10);
        tableUI.add(new Label("GD", skin)).pad(10);
        tableUI.add(new Label("PTS", skin)).pad(10).row();

        // Loop through sorted clubs and populate rows
        int position = 1;
        for (Club club : game.league.allClubs) {
            int played = club.wins + club.draws + club.losses;

            Label posLbl = new Label(String.valueOf(position), skin);
            Label nameLbl = new Label(club.name, skin);
            Label plLbl = new Label(String.valueOf(played), skin);
            Label wLbl = new Label(String.valueOf(club.wins), skin);
            Label dLbl = new Label(String.valueOf(club.draws), skin);
            Label lLbl = new Label(String.valueOf(club.losses), skin);
            Label gdLbl = new Label(String.valueOf(club.getGoalDifference()), skin);
            Label ptsLbl = new Label(String.valueOf(club.points), skin);

            // Highlight the player's club in GOLD
            if (club == game.league.myClub) {
                Color highlight = Color.GOLD;
                posLbl.setColor(highlight);
                nameLbl.setColor(highlight);
                plLbl.setColor(highlight);
                wLbl.setColor(highlight);
                dLbl.setColor(highlight);
                lLbl.setColor(highlight);
                gdLbl.setColor(highlight);
                ptsLbl.setColor(highlight);
            }

            tableUI.add(posLbl).pad(5);
            tableUI.add(nameLbl).pad(5).width(150).left();
            tableUI.add(plLbl).pad(5);
            tableUI.add(wLbl).pad(5);
            tableUI.add(dLbl).pad(5);
            tableUI.add(lLbl).pad(5);
            tableUI.add(gdLbl).pad(5);
            tableUI.add(ptsLbl).pad(5).row();

            position++;
        }

        // Put the table in a ScrollPane (useful if you add 20+ teams later!)
        ScrollPane scrollPane = new ScrollPane(tableUI, skin);
        scrollPane.setFadeScrollBars(false);
        mainContainer.add(scrollPane).expand().fill().pad(20).row();

        // Back Button
        TextButton backBtn = new TextButton("<- Back to Matchday", skin);
        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MatchScreen(game));
            }
        });
        mainContainer.add(backBtn).width(250).height(50).pad(20);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
