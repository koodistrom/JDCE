package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PauseScreen extends NewScreen {
    private TextButton continueButton;
    private TextButton retryButton;
    private TextButton mainMenuButton;

    private float textButtonX;
    private float continueButtonY;
    private float retryButtonY;
    private float mainMenuButtonY;

    private String continueButtonText;
    private String retryButtonText;
    private String mainMenuButtonText;
    private String pausedText;

    private int levelNumber;
    private int worldNumber;
    private Label title;
    private Table pauseTable;

    public PauseScreen(JDCEGame g, int levelNumber, int worldNumber) {
        super(g);

        this.levelNumber = levelNumber;
        this.worldNumber = worldNumber;

        setupButtonBounds();

        pauseTable = new Table();

        continueButton = new TextButton(continueButtonText, getGame().getUiSkin());
        retryButton = new TextButton(retryButtonText, getGame().getUiSkin());
        mainMenuButton = new TextButton(mainMenuButtonText, getGame().getUiSkin());

        pauseTable.setBackground(new TextureRegionDrawable(new Texture(Gdx.files.internal("valikko_popup.png"))));

        updateTexts();
        setupButtons();

        setUpPauseTable();

        getGameStage().addActor(pauseTable);
        /*getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());*/

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    public void setUpPauseTable() {
        title = new Label(pausedText, getGame().getUiSkin());
        updateTables();
        pauseTable.defaults().pad(5);
        pauseTable.row();
        pauseTable.add(title).height(getGame().getFontParameter().size).spaceTop(50);
        pauseTable.row();
        pauseTable.add(continueButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceTop(50).spaceBottom(10);
        pauseTable.row();
        pauseTable.add(retryButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(10);
        pauseTable.row();
        pauseTable.add(mainMenuButton).height(getTextButtonHeight()).width(getTextButtonWidth()).spaceBottom(10);
    }

    @Override
    public void updateTables() {
        title.setText(pausedText);
        pauseTable.setSize(getStageWidth() / 2.5f, getStageHeight() - getStageHeightTenth() / 2);
        pauseTable.setPosition(getGameStage().getWidth() / 2 - (pauseTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (pauseTable.getHeight() / 2));

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        /*Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);



        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();

        getPixelViewport().apply();
        getSpriteBatch().setProjectionMatrix(getPixelViewport().getCamera().combined);*/

        getGame().getLayout48().setText(getGame().getFont48(), pausedText);

        getSpriteBatch().begin();
        /*getGame().getFont48().draw(getSpriteBatch(), pausedText, getStageWidthTenth() * 5 - getGame().getLayout48().width / 2,
                getStageHeightTenth() * 9 - getGame().getLayout48().height / 2);*/
        getSpriteBatch().end();
    }

/*    @Override
    public void resize(int width, int height) {
        getPixelViewport().update(width, height, true);
        getMeterViewport().update(width, height, true);

        setupButtonBounds();
        setupButtons();
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

        textButtonX = getStageWidthTenth() * 5 - (getTextButtonWidth() / 2);
        continueButtonY = getStageHeightTenth() * 6.333f - (getTextButtonHeight() / 2);
        retryButtonY = getStageHeightTenth() * 3.666f - (getTextButtonHeight() / 2);
        mainMenuButtonY = getStageHeightTenth() - (getTextButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        continueButton.setWidth(getTextButtonWidth());
        continueButton.setHeight(getTextButtonHeight());
        continueButton.setPosition(textButtonX, continueButtonY);

        retryButton.setWidth(getTextButtonWidth());
        retryButton.setHeight(getTextButtonHeight());
        retryButton.setPosition(textButtonX, retryButtonY);

        mainMenuButton.setWidth(getTextButtonWidth());
        mainMenuButton.setHeight(getTextButtonHeight());
        mainMenuButton.setPosition(textButtonX, mainMenuButtonY);

        /*getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());*/

        continueButton.setText(continueButtonText);
        retryButton.setText(retryButtonText);
        mainMenuButton.setText(mainMenuButtonText);

    }

    public void clickListeners() {
        super.clickListeners();

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), worldNumber));
                dispose();
            }
        });

        retryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new GameScreen(getGame(), levelNumber, worldNumber));
                dispose();
            }
        });

        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
                dispose();
            }
        });
/*

        getMuteMusicButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        getMuteSoundFxButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });*/

    }

    @Override
    public void updateTexts() {
        continueButtonText = getGame().getBundle().get("continue");
        retryButtonText = getGame().getBundle().get("retry");
        mainMenuButtonText = getGame().getBundle().get("mainmenu");
        pausedText = getGame().getBundle().get("paused");
    }
}
