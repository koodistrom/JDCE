package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * ConnectScreen is displayed when the game is launched on an Android device
 * and no Bluetooth connection to the cycling motion censor is found.
 *
 * It extends the NewScreen-class, and gives the user the option to either try to connect to the censor,
 * or to skip the connection process.
 *
 * If the connection is successful or the user chooses to skip the process,
 * the screen is set to MainMenuScreen.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class ConnectScreen extends NewScreen {
    /**
     * The Label displayed on the top of quitConfirmTable.
     */
    private Label connectionInfo;

    /**
     * ArrayList for the found devices.
     */
    private ArrayList<String> devices;

    /**
     * Table for controlling the UI elements and layout.
     */
    private Table table;

    /**
     * Checks is the use of location data is allowed on the user's device.
     */
    private boolean locationDataAllowed;

    /**
     * The default constructor for ConnectScreen.
     *
     * Creates two TextButtons:
     *    - connectButton looks for the the cycling motion censor
     *      via Bluetooth connection
     *    - skipButton skips the connection process and sets the screen to MainMenuScreen.
     *
     * @param g the JDCEGame-class. It allows HighScoreScreen and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     */
    public ConnectScreen(JDCEGame g) {
        super(g);
        table = new Table();
        connectionInfo = new Label("",getGame().getUiSkin());
        table.setFillParent(true);
        final TextButton connectButton = new TextButton(getGame().getBundle().get("scanDevices"), getGame().getUiSkin());
        final TextButton skipButton = new TextButton(getGame().getBundle().get("skip"), getGame().getUiSkin());
        devices = new ArrayList<String>();


        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JDCEGame.m_platformResolver.scan();

                if(JDCEGame.m_platformResolver.isScanning()){
                    connectionInfo.setText(getGame().getBundle().get("scanning"));
                    locationDataAllowed = true;
                }else{
                    connectionInfo.setText(getGame().getBundle().get("allowLocationData"));
                    locationDataAllowed = false;
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

        getGameStage().addActor(table);
        Gdx.input.setInputProcessor(getGameStage());
        getGameStage().setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(getGame().getPlatformResolver().isConnected()){
            getGame().setScreen(new MainMenuScreen(getGame()));
            dispose();
        }

        if(!JDCEGame.m_platformResolver.isScanning()&& locationDataAllowed){
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
}
