package com.mathbteixeira.football;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MatchScreen extends ScreenAdapter {
    private FootballManagerGame game;
    private Stage stage;
    private Skin skin;

    public MatchScreen(FootballManagerGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("MATCHDAY", skin);
        titleLabel.setFontScale(2f);

        Label teamAvgLabel = new Label("Your Squad OVR: " + game.league.myClub.getTeamRating(), skin);

        Label resultLabel = new Label("Awaiting Kickoff...", skin);
        resultLabel.setColor(Color.YELLOW);

        TextButton simButton = new TextButton("Simulate Match", skin);
        TextButton backButton = new TextButton("<- Back to Market", skin);

        simButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                String matchReport = game.league.simulateMatchday();
                resultLabel.setText(matchReport);
                // Update OVR just in case a new signing changed it
                teamAvgLabel.setText("Your Squad OVR: " + game.league.myClub.getTeamRating());
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MarketScreen(game));
            }
        });

        table.add(titleLabel).padBottom(40).row();
        table.add(teamAvgLabel).padBottom(20).row();
        table.add(resultLabel).padBottom(40).row();
        table.add(simButton).width(200).height(60).padBottom(20).row();
        table.add(backButton).width(200).height(50);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.4f, 0.2f, 1); // Pitch green
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
