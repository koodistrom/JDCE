package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class ConnectScreen extends NewScreen {
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private Label connectionInfo;
    private ArrayList<String> devices;
    private Table table;
    public ConnectScreen(JDCEGame g) {
        super(g);
        table = new Table();
        table.setDebug(true);
        table.setBounds(textButtonX,(getStageHeight() / 5)*2f, getStageWidth() / 2f, getStageHeight() / 2f );
        final TextButton connectButton = new TextButton("connect", getUiSkin());
        devices = new ArrayList<String>();
        connectionInfo = new Label("connecting", getUiSkin());
        /*connectionInfo.setWidth(getTextButtonWidth());
        connectionInfo.setHeight(getTextButtonHeight());
        connectionInfo.setPosition(textButtonX, textButtonY1-1);

        connectButton.setWidth(getTextButtonWidth());
        connectButton.setHeight(getTextButtonHeight());
        connectButton.setPosition(textButtonX, textButtonY1);*/

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            JDCEGame.m_platformResolver.connect();
            }
        });


        table.add(connectButton);

        table.row();
        table.add(connectionInfo);

        //getGameStage().addActor(connectButton);
        getGameStage().addActor(table);
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

        //if(getGame().m_platformResolver.)

        getSpriteBatch().begin();

        getSpriteBatch().end();

        getGameStage().draw();
    }



    public void dispose() {
        super.dispose();
    }
}
