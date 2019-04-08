package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Locale;

import static com.badlogic.gdx.graphics.Color.BLACK;

public class MainMenuScreen extends NewScreen {
    private float textButtonX;
    private float playButtonY;
    private float highScoreButtonY;
    private float quitButtonY;
    private float ENbuttonX;
    private float FIbuttonX;
    private float languageButtonY;

    private TextButton playButton;
    private TextButton highScoreButton;
    private TextButton quitButton;
    private TextButton confirmAffirmative;
    private TextButton confirmNegative;


    private String playButtonText;
    private String highScoreButtonText;
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

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));

        quitConfirmTable = new Table();
//        quitConfirmTable.setDebug(true);

        playButton = new TextButton(playButtonText, getUiSkin());
        highScoreButton = new TextButton(highScoreButtonText, getUiSkin());
        quitButton = new TextButton(quitButtonText, getUiSkin());
        confirmAffirmative = new TextButton(confirmAffirmativeText, getUiSkin());
        confirmNegative = new TextButton(confirmNegativeText, getUiSkin());

        quitConfirmTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("orangebackground.png"))));

        updateTexts();
        setupButtons();
        setUpTable();

        getGameStage().addActor(playButton);
        getGameStage().addActor(highScoreButton);
        getGameStage().addActor(quitButton);
        getGameStage().addActor(getButtonEN());
        getGameStage().addActor(getButtonFI());
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(quitConfirmTable);

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();

    }

    public void setUpTable() {
        title = new Label(quitConfirmText, getUiSkin());
        updateTable();
        quitConfirmTable.add(title).height(25).spaceBottom(30);
        quitConfirmTable.center().top();
        quitConfirmTable.row();
        quitConfirmTable.add(confirmAffirmative).height(50).width(100).spaceBottom(30);
        quitConfirmTable.row();
        quitConfirmTable.add(confirmNegative).height(50).width(100).spaceBottom(30);

    }

    public void updateTable() {
        title.setText(quitConfirmText);
        quitConfirmTable.setSize(getStageWidth() / 4, getStageHeight() / 3.5f);
        quitConfirmTable.setPosition(getGameStage().getWidth() / 2 - (quitConfirmTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (quitConfirmTable.getHeight() / 2));
    }

    @Override
    public void render(float delta) {
        if(isQuitConfirmOn) {
            quitConfirmTable.setVisible(true);
        } else {
            quitConfirmTable.setVisible(false);
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();
    }

    @Override
    public void resize(int width, int height) {
        getGameViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
        updateTable();
    }

    public void dispose() {
        super.dispose();
    }

    @Override
    public void setupButtonBounds() {
        updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        playButtonY = getStageHeightTenth() * 9 - (getTextButtonHeight() / 2);
        highScoreButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        quitButtonY = getStageHeightTenth() * 3.666f  - (getTextButtonHeight() / 2);

        ENbuttonX = getStageWidthTenth() - (getImageButtonWidth() / 2);
        FIbuttonX = getStageWidthTenth() * 2.5f - (getImageButtonWidth() / 2);
        languageButtonY = getStageHeightTenth() * 1 - (getImageButtonHeight() / 2);

        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
    }

    public void setupButtons() {
        playButton.setWidth(getTextButtonWidth());
        playButton.setHeight(getTextButtonHeight());
        playButton.setPosition(textButtonX, playButtonY);

        highScoreButton.setWidth(getTextButtonWidth());
        highScoreButton.setHeight(getTextButtonHeight());
        highScoreButton.setPosition(textButtonX, highScoreButtonY);

        quitButton.setWidth(getTextButtonWidth());
        quitButton.setHeight(getTextButtonHeight());
        quitButton.setPosition(textButtonX, quitButtonY);

        getButtonEN().setWidth(getImageButtonWidth());
        getButtonEN().setHeight(getImageButtonHeight());
        getButtonEN().setPosition(ENbuttonX, languageButtonY);

        getButtonFI().setWidth(getImageButtonWidth());
        getButtonFI().setHeight(getImageButtonHeight());
        getButtonFI().setPosition(FIbuttonX, languageButtonY);

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());

        playButton.setText(playButtonText);
        highScoreButton.setText(highScoreButtonText);
        quitButton.setText(quitButtonText);
        confirmAffirmative.setText(confirmAffirmativeText);
        confirmNegative.setText(confirmNegativeText);

        getGameStage().setDebugAll(true);
    }

    public void clickListeners() {
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isQuitConfirmOn == false) {
                    getGame().setScreen(new LevelSelectScreen(getGame()));
                }
            }
        });

        highScoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isQuitConfirmOn == false) {
                    getGame().setScreen(new HighScoreScreen(getGame()));
                }
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = true;
            }
        });

        confirmAffirmative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
                System.exit(-1);
            }
        });

        confirmNegative.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isQuitConfirmOn = false;
            }
        });

        getMuteMusicButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        getMuteSoundFxButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        getButtonFI().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().updateLanguage(new Locale("fi", "FI"));
                updateTexts();
                updateTable();
                setupButtons();
            }
        });

        getButtonEN().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().updateLanguage(new Locale("en", "UK"));
                updateTexts();
                updateTable();
                setupButtons();
            }
        });
    }
    public void updateTexts() {
        playButtonText = getGame().getBundle().get("play");
        highScoreButtonText = getGame().getBundle().get("highscores");
        quitButtonText = getGame().getBundle().get("quit");
        confirmAffirmativeText = getGame().getBundle().get("affirmative");
        confirmNegativeText = getGame().getBundle().get("negative");
        quitConfirmText = getGame().getBundle().get("quitConfirmText");
    }
}

