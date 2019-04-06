package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class ConnectScreen extends NewScreen {
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private Label connectionInfo;
    private ArrayList<String> devices;
    public ConnectScreen(JDCEGame g) {
        super(g);
        final TextButton connectButton = new TextButton("connect", getUiSkin());
        devices = new ArrayList<String>();
        connectionInfo = new Label("connecting", getUiSkin());
        connectionInfo.setWidth(getTextButtonWidth());
        connectionInfo.setHeight(getTextButtonHeight());
        connectionInfo.setPosition(textButtonX, textButtonY1-1);

        connectButton.setWidth(getTextButtonWidth());
        connectButton.setHeight(getTextButtonHeight());
        connectButton.setPosition(textButtonX, textButtonY1);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            JDCEGame.m_platformResolver.connect();
            }
        });

        getGameStage().addActor(connectButton);
        Gdx.input.setInputProcessor(getGameStage());
    }

    @Override
    public void render(float delta) {
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(getGame().getPlatformResolver().isConnected()){
            MainMenuScreen gs = new MainMenuScreen(getGame());
            getGame().setScreen(gs);
        }
        /*if(play.isClicked(getCamera())) {
            getGame().setScreen(new GameScreen(getGame()));
        }

        if(highScores.isClicked(getCamera())) {
            getGame().setScreen(new HighScoreScreen(getGame()));
        }*/

        getSpriteBatch().begin();

        getSpriteBatch().end();

        getGameStage().draw();
    }



    public void dispose() {
        super.dispose();
    }
}
