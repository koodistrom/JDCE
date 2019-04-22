package fi.tamk.jdce;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * MainMenuScreen is the main menu view for the game.
 *
 * It extends the NewScreen-class and has controls for the languages and
 * mute-buttons for the game's music and sound effects.
 *
 * From the MainMenuScreen you can also press the playButton,
 * which sets the screen to WorldSelectScreen or the quitButton
 * which sets the quitConfirmTable visible.
 *
 * @author Jaakko Mäntylä
 * @author Miika Minkkinen
 * @version 2019.0421
 */
public class MainMenuScreen extends NewScreen {
    /**
     * The location of the TextButtons on the x-axis/width-axis.
     */
    private float textButtonX;

    /**
     * The location of the playButton on the y-axis/height-axis.
     */
    private float playButtonY;

    /**
     * The location of the quitButton on the y-axis/height-axis.
     */
    private float quitButtonY;

    /**
     * The location of the languageEN on the x-axis/width-axis.
     */
    private float ENbuttonX;

    /**
     * The location of the languageFI on the x-axis/width-axis.
     */
    private float FIbuttonX;

    /**
     * The location of languageEN and languageFI on the y-axis/height-axis.
     */
    private float languageButtonY;

    /**
     * The Button that moves you to WorldSelectScreen.
     */
    private TextButton playButton;

    /**
     * The Button that sets quitConfirmTable visible.
     */
    private TextButton quitButton;

    /**
     * The Button on the quitConfirmTable, that confirms that you want to quit the game.
     */
    private TextButton confirmAffirmative;

    /**
     * The Button on the quitConfirmTable, that confirms that you don't want to quit the game.
     */
    private TextButton confirmNegative;

    /**
     * The Button that sets the game's language to Finnish.
     */
    private Button languageFI;

    /**
     * The Button that sets the game's language to English.
     */
    private Button languageEN;

    /**
     * The height of languageFI and languageEN.
     */
    private float languageButtonHeight;

    /**
     * The width of languageFI and languageEN.
     */
    private float languageButtonWidth;

    /**
     * The string that includes the text displayed on playButton.
     */
    private String playButtonText;

    /**
     * The string that includes the text displayed on quitButton.
     */
    private String quitButtonText;

    /**
     * The string that includes the text displayed on the title of quitConfirmTable.
     */
    private String quitConfirmText;

    /**
     * The string that includes the texts displayed on confirmAffirmative.
     */
    private String confirmAffirmativeText;

    /**
     * The string that includes the texts displayed on confirmNegative.
     */
    private String confirmNegativeText;

    /**
     * The Label displayed on the top of quitConfirmTable.
     */
    private Label title;

    /**
     * The Table that confirms if the user wants to quit the game.
     */
    private Table quitConfirmTable;

    /**
     * A boolean that checks if quitConfirmTable is visible (true = visible).
     */
    private boolean isQuitConfirmOn = false;


    /**
     * The default constructor for MainMenuScreen.
     *
     * @param g the JDCEGame-class. It allows MainMenuScreen and NewScreen access to the: batch, myBundle,
     *          the game's settings, textures, uiSkin and font48.
     */
    public MainMenuScreen(JDCEGame g) {
        super(g);

        setupButtonBounds();

        quitConfirmTable = new Table();

        languageFI = new Button(getGame().finOffTextRegDrawable, getGame().finTextRegDrawable, getGame().finTextRegDrawable);
        languageEN = new Button(getGame().engOffTextRegDrawable, getGame().engTextRegDrawable, getGame().engTextRegDrawable);

        if(JDCEGame.isEnglish) {
            languageEN.setChecked(true);
            languageEN.setDisabled(true);
        } else {
            languageFI.setChecked(true);
            languageFI.setDisabled(true);
        }

        playButton = new TextButton(playButtonText, getGame().getUiSkin());
        quitButton = new TextButton(quitButtonText, getGame().getUiSkin());
        confirmAffirmative = new TextButton(confirmAffirmativeText, getGame().getUiSkin());
        confirmNegative = new TextButton(confirmNegativeText, getGame().getUiSkin());

        quitConfirmTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("valikko_popup.png"))));

        updateTexts();
        setupButtons();
        setUpQuitConfirmTable();

        getGameStage().addActor(playButton);
        getGameStage().addActor(quitButton);
        getGameStage().addActor(languageEN);
        getGameStage().addActor(languageFI);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(quitConfirmTable);

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();

    }

    /**
     * Sets up the layout and actors to quitConfirmTable.
     */
    public void setUpQuitConfirmTable() {
        title = new Label(quitConfirmText, getGame().getUiSkin());
        updateTables();
        quitConfirmTable.defaults();
        quitConfirmTable.row();
        quitConfirmTable.add(title).height(getGame().getFontParameter().size).padTop(50);
        quitConfirmTable.center().top();
        quitConfirmTable.row();
        quitConfirmTable.add(confirmAffirmative).height(50).width(100).spaceBottom(30).pad(10);
        quitConfirmTable.row();
        quitConfirmTable.add(confirmNegative).height(50).width(100).spaceBottom(30);

    }

    /**
     * Updates title with quitConfirmText and the size and position of quitConfirmTable.
     */
   @Override
    public void updateTables() {
        title.setText(quitConfirmText);
        quitConfirmTable.setSize(getStageWidth() / 3, getStageHeight() / 3);
        quitConfirmTable.setPosition(getGameStage().getWidth() / 2 - (quitConfirmTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (quitConfirmTable.getHeight() / 2));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(isQuitConfirmOn) {
            quitConfirmTable.setVisible(true);
        } else {
            quitConfirmTable.setVisible(false);
        }
    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        languageButtonHeight = getImageButtonHeight();
        languageButtonWidth = languageButtonHeight * 1.741f;

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        playButtonY = getStageHeight() - getTextButtonHeight() * 2;
        quitButtonY = getTextButtonHeight() * 2;

        ENbuttonX = getStageWidth() / 2 - languageButtonWidth * 1.5f;
        FIbuttonX = getStageWidth() / 2 + languageButtonWidth * 0.5f;
        languageButtonY = getStageHeightTenth() * 1 - (getImageButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        playButton.setWidth(getTextButtonWidth());
        playButton.setHeight(getTextButtonHeight());
        playButton.setPosition(textButtonX, playButtonY);

        quitButton.setWidth(getTextButtonWidth());
        quitButton.setHeight(getTextButtonHeight());
        quitButton.setPosition(textButtonX, quitButtonY);

        languageEN.setWidth(languageButtonWidth);
        languageEN.setHeight(languageButtonHeight);
        languageEN.setPosition(ENbuttonX, languageButtonY);

        languageFI.setWidth(languageButtonWidth);
        languageFI.setHeight(languageButtonHeight);
        languageFI.setPosition(FIbuttonX, languageButtonY);

        playButton.setText(playButtonText);
        quitButton.setText(quitButtonText);
        confirmAffirmative.setText(confirmAffirmativeText);
        confirmNegative.setText(confirmNegativeText);
    }

    @Override
    public void clickListeners() {
        super.clickListeners();

        languageFI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                languageEN.setDisabled(false);
                JDCEGame.settings.putBoolean("Language", false);
                languageEN.setChecked(false);
                JDCEGame.updateLanguage(JDCEGame.fi);
                JDCEGame.settings.flush();
                languageFI.setDisabled(true);
                updateTexts();
                updateTables();
                setupButtons();
                playButtonSound();
            }
        });

        languageEN.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                languageFI.setDisabled(false);
                JDCEGame.settings.putBoolean("Language", true);
                languageFI.setChecked(false);
                JDCEGame.updateLanguage(JDCEGame.en);
                JDCEGame.settings.flush();
                languageEN.setDisabled(true);
                updateTexts();
                updateTables();
                setupButtons();
                playButtonSound();
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new WorldSelectScreen(getGame()));
                playButtonSound();
                dispose();

            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = true;
                playButton.setDisabled(true);
                quitButton.setDisabled(true);
                playButtonSound();
            }
        });

        confirmAffirmative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(-1);
                dispose();
                playButtonSound();
            }
        });

        confirmNegative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = false;
                playButton.setDisabled(false);
                quitButton.setDisabled(false);
                playButtonSound();
            }
        });

    }

    @Override
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        quitButtonText = getGame().getBundle().get("quit");
        confirmAffirmativeText = getGame().getBundle().get("affirmative");
        confirmNegativeText = getGame().getBundle().get("negative");
        quitConfirmText = getGame().getBundle().get("quitConfirmText");
    }
}

