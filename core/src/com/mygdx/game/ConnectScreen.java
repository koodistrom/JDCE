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

    private Label connectionInfo;
    private ArrayList<String> devices;
    private Table table;
    public ConnectScreen(JDCEGame g) {
        super(g);
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        //table.setBounds(textButtonX,(getStageHeight() / 5)*2f, getStageWidth() / 2f, getStageHeight() / 2f );
        final TextButton connectButton = new TextButton("scan devices", getUiSkin());
        devices = new ArrayList<String>();
        connectionInfo = new Label("start scan", getUiSkin());


        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(JDCEGame.m_platformResolver.scan()){
                    connectionInfo.setText("scanning");
                }else{
                    connectionInfo.setText("please allow use of location data from settings");
                }
            }
        });


        table.add(connectButton).width(getStageWidth() / 5f).height(getStageHeight() / 5f );

        table.row();
        table.add(connectionInfo).pad(getStageHeight() / 50f);
        table.row();

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

        if(getGame().m_platformResolver.isDeviceAdded()){
            devices.add(getGame().m_platformResolver.getNewDeviceName());
            Label foundDevice = new Label(getGame().m_platformResolver.getNewDeviceName(), getUiSkin());
            foundDevice.addListener(new ClickListener() {
                int index = devices.size()-1;
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    getGame().m_platformResolver.connectTo(index);
                }
            });
            table.add(foundDevice).pad(getStageHeight() / 50f).width(getStageWidth() / 6f).height(getStageHeight() / 5f);
            table.row();
        }

        getSpriteBatch().begin();

        getSpriteBatch().end();

        getGameStage().draw();
    }



    public void dispose() {
        super.dispose();
    }
}
