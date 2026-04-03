package com.mathbteixeira.football;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MarketScreen extends ScreenAdapter {
    private FootballManagerGame game;
    private Stage stage;
    private Skin skin;
    private Table scrollTable;

    public MarketScreen(FootballManagerGame game) {
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

        Label title = new Label("PREMIER LEAGUE TRANSFERS", skin);
        mainContainer.add(title).pad(20).row();

        Label balanceLabel = new Label("Balance: $" + String.format("%,d", game.league.myClub.balance), skin);
        balanceLabel.setColor(Color.GOLD);
        mainContainer.add(balanceLabel).padBottom(10).row();

        scrollTable = new Table();
        scrollTable.add(new Label("PLAYER", skin)).pad(10);
        scrollTable.add(new Label("CLUB", skin)).pad(10);
        scrollTable.add(new Label("OVR", skin)).pad(10);
        scrollTable.add(new Label("PRICE", skin)).pad(10);
        scrollTable.add(new Label("ACTION", skin)).pad(10).row();

        for (Club club : game.league.allClubs) {
            for (Player player : club.squad) {
                addPlayerRow(player, balanceLabel);
            }
        }

        ScrollPane scrollPane = new ScrollPane(scrollTable, skin);
        scrollPane.setFadeScrollBars(false);
        mainContainer.add(scrollPane).expand().fill().pad(10).row();

        TextButton toMatchBtn = new TextButton("Go to Matchday ->", skin);
        toMatchBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                game.setScreen(new MatchScreen(game));
            }
        });
        mainContainer.add(toMatchBtn).width(200).height(50).pad(20);
    }

    private void addPlayerRow(final Player player, final Label balanceLabel) {
        scrollTable.add(new Label(player.name, skin)).left().pad(10);
        scrollTable.add(new Label(player.currentClub.name, skin)).pad(10);

        Label ovrLabel = new Label(String.valueOf(player.overall), skin);
        if (player.overall >= 85) ovrLabel.setColor(Color.CYAN);
        scrollTable.add(ovrLabel).pad(10);

        scrollTable.add(new Label("$" + (player.price / 1000000) + "M", skin)).pad(10);

        final TextButton buyBtn = new TextButton("BUY", skin);
        if (player.currentClub == game.league.myClub) {
            buyBtn.setDisabled(true);
            buyBtn.setText("OWNED");
        }

        buyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (game.league.executeTransfer(player)) {
                    balanceLabel.setText("Balance: $" + String.format("%,d", game.league.myClub.balance));
                    buyBtn.setDisabled(true);
                    buyBtn.setText("OWNED");
                }
            }
        });
        scrollTable.add(buyBtn).width(80).pad(5).row();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.05f, 0.05f, 0.1f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
