package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
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
    private boolean scanStarted;
    public ConnectScreen(JDCEGame g) {
        super(g);
        table = new Table();
        connectionInfo = new Label("",getGame().getUiSkin());
        //table.setDebug(true);
        table.setFillParent(true);
        //table.setBounds(textButtonX,(getStageHeight() / 5)*2f, getStageWidth() / 2f, getStageHeight() / 2f );
        final TextButton connectButton = new TextButton(getGame().getBundle().get("scanDevices"), getGame().getUiSkin());
        final TextButton skipButton = new TextButton(getGame().getBundle().get("skip"), getGame().getUiSkin());
        devices = new ArrayList<String>();
        //connectionInfo = new Label(getGame().getBundle().get("startScan"), getGame().getUiSkin());
        scanStarted = false;

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JDCEGame.m_platformResolver.scan();

                if(JDCEGame.m_platformResolver.isScanning()){
                    connectionInfo.setText(getGame().getBundle().get("scanning"));
                }else{
                    connectionInfo.setText(getGame().getBundle().get("allowLocationData"));
                    scanStarted = true;
                }
            }
        });

        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().skipConnect=true;
                getGame().setScreen(new MainMenuScreen(getGame()));
            }
        });

        table.defaults().pad(getStageHeight() / 50f);
        table.add(connectionInfo);
        table.row();
        table.add(connectButton).size(getTextButtonWidth(), getTextButtonHeight()).spaceBottom(50);//width(getStageWidth() / 5f).height(getStageHeight() / 5f );
        table.row();
        table.add(skipButton).size(getTextButtonWidth(), getTextButtonHeight()).spaceBottom(50);//width(getStageWidth() / 5f).height(getStageHeight() / 5f );



        //getGameStage().addActor(connectButton);
        getGameStage().addActor(table);
        Gdx.input.setInputProcessor(getGameStage());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        /*getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/


        if(getGame().getPlatformResolver().isConnected()){

            getGame().setScreen(new MainMenuScreen(getGame()));
            dispose();
        }

        if(!JDCEGame.m_platformResolver.isScanning()&&scanStarted){
            connectionInfo.setText(getGame().getBundle().get("scanEnd"));
        }

        if(getGame().m_platformResolver.isDeviceAdded()&&JDCEGame.m_platformResolver.isScanning()){
            devices.add(getGame().m_platformResolver.getNewDeviceName());
            Label foundDevice = new Label(getGame().m_platformResolver.getNewDeviceName(), getGame().getUiSkin());
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
