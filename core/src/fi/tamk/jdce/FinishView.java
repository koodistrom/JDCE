package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * FinishView is displayed when the game ends either through succeeding or failing to finish a level.
 *
 * It extends the NewScreen-class, implements Input.TextInputListener and has
 * mute-buttons for the game's music and sound effects
 *
 * If the level is passed, user is asked to input a name and
 * the screen displays the time the level was passed in.
 *
 * From the FinishView the user can either retry the level via retryButton or
 * go to the LevelSelectScreen via menuButton.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class FinishView extends NewScreen implements Input.TextInputListener {
    /**
     * A file where the high scores are stored.
     */
    Preferences highscores;

    /**
     * Tells the time that was achieved upon finishing a level.
     */
    private String score;

    /**
     * Message displayed upon failing to finish a level.
     */
    private String loseMessage;

    private String worldHSMessage;
    private String personalHSMessage;

    /**
     * Table created upon finishing a level.
     */
    private Table winTable;

    /**
     * Table created upon failing to finish a level.
     */
    private Table loseTable;

    /**
     * The Button that moves you to LevelSelectScreen.
     */
    private TextButton menuButton;

    /**
     * The Button that lets the user retry the level.
     */
    private TextButton retryButton;

    /**
     * Stores the name user inputs.
     */
    private String name;

    /**
     * Stores the time that the level was passed in.
     */
    private float time;

    /**
     * Holds the the number of the level.
     */
    private int levelNumber;

    /**
     * Holds the the number of the world.
     */
    private int worldNumber;

    /**
     * The limit of characters the user can input for the name.
     */
    private int nameLengthLimit;

    /**
     * Screen for playing the level.
     */
    private  GameScreen gameScreen;

    private Socket socket;

    private Boolean fitsToGlobalHS;
    final JSONObject levelTime = new JSONObject();

    private Table connectingTable;

    private String connectingMessage;
    private Button skipButton;

    /**
     * The default constructor for FinishView.
     *
     * @param g the JDCEGame-class. It allows FinishView and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     * @param gameScreen last game screen seved for retrying whitout loading the screen again.
     * @param time the time that the level was passed in.
     * @param isItAWin tells FinishView if the level was successfully finished.
     * @param levelNumber the number of the level.
     * @param worldNumber the number of the world.
     */
    public FinishView(JDCEGame g, GameScreen gameScreen, float time, boolean isItAWin, int levelNumber, int worldNumber) {
        super(g);
        this.time = time;
        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        this.gameScreen = gameScreen;

        nameLengthLimit = 10;
        winTable = new Table();
        loseTable = new Table();
        connectingTable = new Table();
        score = getGame().getBundle().get("yourTime") + " " + Utilities.secondsToString(time);
        loseMessage = getGame().getBundle().get("loseMessage");
        personalHSMessage = getGame().getBundle().get("highscoreMessagePersonal");
        worldHSMessage = getGame().getBundle().get("highscoreMessageWorld");
        connectingMessage = getGame().getBundle().get("connectingHS");

        name = "";

        menuButton = new TextButton(getGame().getBundle().get("continue"), getGame().getUiSkin());
        retryButton = new TextButton(getGame().getBundle().get("retry"), getGame().getUiSkin());
        skipButton = new TextButton(getGame().getBundle().get("skip"), getGame().getUiSkin());

        fitsToGlobalHS = false;




        if(isItAWin) {

            try {
                levelTime.put("level", levelNumber);
                levelTime.put("time", time);
            } catch (JSONException e) {
                Gdx.app.log("json", "error creating level & time json");
            }

            connectSocket();
            configSocketEvents();

        } else {
            setUpLoseTable();
            getGameStage().addActor(loseTable);
        }

        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());

        clickListeners();

        Gdx.input.setInputProcessor(getGameStage());
    }

    /**
     * Sets up the winTable. Called if the level is successfully finished.
     */
    public void setUpWinTable() {
        updateTables();
        connectingTable.remove();
        if(fitsToGlobalHS){
            winTable.add(new Label(worldHSMessage, getGame().getUiSkin())).height(50).spaceBottom(30);
            winTable.center();
            winTable.row();
        }else if(fitsToHighscore(time,levelNumber)){
            winTable.add(new Label(personalHSMessage, getGame().getUiSkin())).height(50).spaceBottom(30);
            winTable.center();
            winTable.row();
        }
        winTable.add(new Label(score, getGame().getUiSkin())).height(50).spaceBottom(30);
        winTable.center();
        winTable.row();
        winTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        winTable.row();
        winTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    /**
     * Sets up the loseTable. Called if the level is not successfully finished.
     */
    public void setUpLoseTable() {
        updateTables();
        loseTable.add(new Label(loseMessage, getGame().getUiSkin())).height(50).spaceBottom(30);
        loseTable.center();
        loseTable.row();
        loseTable.add(menuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
        loseTable.row();
        loseTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);

    }

    public void setUpConnectingTable(){
        connectingTable.add(new Label(connectingMessage, getGame().getUiSkin())).height(50).spaceBottom(30);
        connectingTable.center();
        connectingTable.row();
        connectingTable.add(skipButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(30);
    }
    @Override
    public void updateTables() {
        winTable.setFillParent(true);
        loseTable.setFillParent(true);
        connectingTable.setFillParent(true);
    }

    /**
     * Checks if the scored time is fast enough to fit the high scores.
     *
     * @param score the time that the level was finished in.
     * @param levelNum the number of that level.
     * @return true if the score can be added to the level's high scores.
     */
    public boolean fitsToHighscore(float score, int levelNum){
        String level = String.valueOf(levelNum);
        String HSOfLevel = highscores.getString(level, "");
        String comparisonTime = "";
        String turnedComparisonTime="";
        boolean fits = false;
        boolean addToTime = false;
        int numOfEntries = 0;
        for(int i=HSOfLevel.length()-1;i>=0;i--){
            if(addToTime && HSOfLevel.charAt(i)!='%'){
                comparisonTime +=HSOfLevel.charAt(i);
            }
            if (HSOfLevel.charAt(i) =='#'){
                if(numOfEntries==0){
                    addToTime = true;
                }
                numOfEntries++;
            }

            if (HSOfLevel.charAt(i) =='%'){
                addToTime = false;

            }

        }

        for(int i=comparisonTime.length()-1;i>=0;i--){
            turnedComparisonTime+=comparisonTime.charAt(i);
        }

        if (!turnedComparisonTime.equals("") && Float.valueOf(turnedComparisonTime)>score){
            fits = true;
        }


        if(numOfEntries<10 ){
            fits = true;
        }
        return fits;
    }

    /**
     * Adds the score to the level.
     *
     * @param score the time that the level was finished in.
     * @param levelNum the number of that level.
     */
    public void addHighScore(float score, int levelNum) {

        String level = String.valueOf(levelNum);
        String HSOfLevel = highscores.getString(level, "");
        String comparisonTime = "";
        String valueToSave = "";
        String lastEntry = "";
        boolean entryMade = false;
        boolean addToTime = false;
        int numOfEntries = 0;
        for(int i=0;i<HSOfLevel.length();i++){
            lastEntry += HSOfLevel.charAt(i);

            if (HSOfLevel.charAt(i) != '#' && HSOfLevel.charAt(i) != '%') {
                if (addToTime) {
                    comparisonTime += HSOfLevel.charAt(i);
                }

            } else if (HSOfLevel.charAt(i) == '%') {

                comparisonTime = "";
                addToTime = true;

            } else if (HSOfLevel.charAt(i) == '#') {

                addToTime = false;
                if(Float.valueOf(comparisonTime)>score&&!entryMade){
                   valueToSave+=name+"%"+String.valueOf(score)+"#";
                   numOfEntries++;
                   entryMade=true;
                }

                if(numOfEntries<10) {
                    valueToSave += lastEntry;
                    numOfEntries++;
                }
                lastEntry="";
            }
            if(numOfEntries>=10){
                break;
            }
        }
        if(numOfEntries<10&&!entryMade){
            valueToSave+=name+"%"+String.valueOf(score)+"#";
        }

        highscores.putString(level, valueToSave);
        highscores.flush();
    }

    /**
     * Asks the user to input a name with maximum of 10 characters.
     */
    public void enterName(){
        Gdx.input.getTextInput(this, getGame().getBundle().get("nameText"), "", getGame().getBundle().get("maxChars"));



    }

    @Override
    public void clickListeners() {
        super.clickListeners();

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                playButtonSound();
                gameScreen.dispose();
                dispose();
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.reset();
                getGame().setScreen(gameScreen);
                playButtonSound();
                dispose();
            }
        });

        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                playButtonSound();
                fitsToGlobalHS = false;

                if(fitsToHighscore(time, levelNumber)){
                    enterName();
                }else {
                    setUpWinTable();
                    getGameStage().addActor(winTable);
                }


            }
        });
    }

    @Override
    public void input(String text) {
        name = "";
        for(int i=0; i<text.length();i++){
            if(text.charAt(i)!='#'&&text.charAt(i)!='%'){
                name+=text.charAt(i);
            }
        }
        if(name.length()<=nameLengthLimit){

            setUpWinTable();
            getGameStage().addActor(winTable);

            if(fitsToHighscore(time, levelNumber)) {
                addHighScore(time, levelNumber);
            }

            if(fitsToGlobalHS){
                final JSONObject nameTime = new JSONObject();
                try {
                    nameTime.put("level", levelNumber);
                    nameTime.put("name", name);
                    nameTime.put("time", time);
                } catch (JSONException e) {
                    Gdx.app.log("json", "error creating name, level & time json");
                }

                socket.emit("nameTime", nameTime);
            }




        }else{
            enterName();
        }

    }

    @Override
    public void canceled() {

    }

    public void connectSocket(){
        try{
            setUpConnectingTable();
            getGameStage().addActor(connectingTable);
            socket = IO.socket("http://192.168.2.33:6969");

            socket.connect();
            Gdx.app.log("testi","yritetään yhdistää");
        }catch (Exception e){
            System.out.println("netti ei toimi: "+e);
        }
    }

    public void configSocketEvents() {





        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
                socket.emit("testi", levelTime);
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("scoreFits", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                fitsToGlobalHS = (Boolean) args[0];

                if(fitsToHighscore(time,levelNumber)|| fitsToGlobalHS){
                    enterName();
                }

                setUpWinTable();
                getGameStage().addActor(winTable);

            }
        });
    }
}
