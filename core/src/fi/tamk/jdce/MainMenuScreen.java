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

public class MainMenuScreen extends NewScreen {
    //private Table mainMenuTable;

    private float textButtonX;
    private float playButtonY;
    //private float highScoreButtonY;
    private float quitButtonY;
    private float ENbuttonX;
    private float FIbuttonX;
    private float languageButtonY;

    private TextButton playButton;
    //private TextButton highScoreButton;
    private TextButton quitButton;
    private TextButton confirmAffirmative;
    private TextButton confirmNegative;

    private Button languageFI;
    private Button languageEN;

    private float languageButtonHeight;
    private float languageButtonWidth;

    private String playButtonText;
    //private String highScoreButtonText;
    private String quitButtonText;
    private String quitConfirmText;
    private String confirmAffirmativeText;
    private String confirmNegativeText;

    private Label title;
    private Table quitConfirmTable;
    private boolean isQuitConfirmOn = false;


    public MainMenuScreen(JDCEGame g) {
        super(g);
        System.out.println("korkeus "+getScreenHeight());
        System.out.println("leveys "+getScreenWidth());

        setupButtonBounds();



        /*mainMenuTable = new Table();
        mainMenuTable.setDebug(true);*/
        quitConfirmTable = new Table();
//        quitConfirmTable.setDebug(true);

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
        //highScoreButton = new TextButton(highScoreButtonText, getGame().getUiSkin());
        quitButton = new TextButton(quitButtonText, getGame().getUiSkin());
        confirmAffirmative = new TextButton(confirmAffirmativeText, getGame().getUiSkin());
        confirmNegative = new TextButton(confirmNegativeText, getGame().getUiSkin());

        quitConfirmTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("valikko_popup.png"))));





        updateTexts();
        setupButtons();
        setUpQuitConfirmTable();
        //setUpMainMenuTable();


        getGameStage().addActor(playButton);
        //getGameStage().addActor(highScoreButton);
        getGameStage().addActor(quitButton);
        getGameStage().addActor(languageEN);
        getGameStage().addActor(languageFI);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(quitConfirmTable);
        //getGameStage().addActor(mainMenuTable);

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();

    }

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

   /* public void setUpMainMenuTable() {
        updateTables();
        mainMenuTable.top().center();
        mainMenuTable.add(playButton).width(getTextButtonWidth()).height(getTextButtonHeight()).colspan(2).spaceBottom(50).spaceLeft(100);
        mainMenuTable.add(getMuteMusicButton()).spaceBottom(50).spaceRight(100);
        mainMenuTable.row();
        mainMenuTable.add(quitButton).width(getTextButtonWidth()).height(getTextButtonHeight()).colspan(2).spaceBottom(50);
        mainMenuTable.add(getMuteSoundFxButton()).spaceBottom(50).spaceRight(100);
        mainMenuTable.row();
        mainMenuTable.add(getButtonFI()).spaceBottom(50);
        mainMenuTable.add(getButtonEN()).spaceBottom(50);
    }*/

   @Override
    public void updateTables() {
        title.setText(quitConfirmText);
        quitConfirmTable.setSize(getStageWidth() / 3, getStageHeight() / 3);
        quitConfirmTable.setPosition(getGameStage().getWidth() / 2 - (quitConfirmTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (quitConfirmTable.getHeight() / 2));
        //mainMenuTable.setSize(getStageWidth(), getStageHeight());
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(isQuitConfirmOn) {
            quitConfirmTable.setVisible(true);
        } else {
            quitConfirmTable.setVisible(false);
        }

        /*Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();*/
    }

    /*@Override
    public void resize(int width, int height) {
        getPixelViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
        updateTables();
    }*/

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();
        /*updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());*/

        languageButtonHeight = getImageButtonHeight();
        languageButtonWidth = languageButtonHeight * 1.741f;

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        //playButtonY = getStageHeightTenth() * 9 - (getTextButtonHeight() / 2);
        playButtonY = getStageHeight() - getTextButtonHeight() * 2;
        //highScoreButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        quitButtonY = getTextButtonHeight() * 2;
        //quitButtonY = getStageHeightTenth() * 3.666f  - (getTextButtonHeight() / 2);

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

        /*highScoreButton.setWidth(getTextButtonWidth());
        highScoreButton.setHeight(getTextButtonHeight());
        highScoreButton.setPosition(textButtonX, highScoreButtonY);*/

        quitButton.setWidth(getTextButtonWidth());
        quitButton.setHeight(getTextButtonHeight());
        quitButton.setPosition(textButtonX, quitButtonY);

        languageEN.setWidth(languageButtonWidth);
        languageEN.setHeight(languageButtonHeight);
        languageEN.setPosition(ENbuttonX, languageButtonY);

        languageFI.setWidth(languageButtonWidth);
        languageFI.setHeight(languageButtonHeight);
        languageFI.setPosition(FIbuttonX, languageButtonY);

        /*getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());*/

        playButton.setText(playButtonText);
        //highScoreButton.setText(highScoreButtonText);
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

        /*highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isQuitConfirmOn == false) {
                    dispose();
                    getGame().setScreen(new HighScoreScreen(getGame(), 2));

                }
            }
        });*/

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = true;
                playButton.setDisabled(true);
                //highScoreButton.setDisabled(true);
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
                //highScoreButton.setDisabled(false);
                quitButton.setDisabled(false);
                playButtonSound();
            }
        });

    }

    @Override
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        //highScoreButtonText = getGame().getBundle().get("highscores");
        quitButtonText = getGame().getBundle().get("quit");
        confirmAffirmativeText = getGame().getBundle().get("affirmative");
        confirmNegativeText = getGame().getBundle().get("negative");
        quitConfirmText = getGame().getBundle().get("quitConfirmText");
    }
}
