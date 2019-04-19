package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorldSelectScreen extends NewScreen {
    private TextButton world1Button;
    private TextButton world2Button;
    private TextButton world3Button;
    private TextButton world4Button;

    private float buttonCol1x;
    private float buttonCol2x;
    private float buttonRow1y;
    private float buttonRow2y;

    private String world1Text;
    private String world2Text;
    private String world3Text;
    private String world4Text;
    private String worldSelectText;

    public WorldSelectScreen(JDCEGame g) {
        super(g);

        setupButtonBounds();

        world1Button = new TextButton(world1Text, getGame().getUiSkin());
        world2Button = new TextButton(world2Text, getGame().getUiSkin());
        world3Button = new TextButton(world3Text, getGame().getUiSkin());
        world4Button = new TextButton(world4Text, getGame().getUiSkin());

        updateTexts();
        setupButtons();

        getGameStage().addActor(world1Button);
        getGameStage().addActor(world2Button);
        getGameStage().addActor(world3Button);
        getGameStage().addActor(world4Button);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
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

        getGame().getLayout48().setText(getGame().getFont48(), worldSelectText);

        getSpriteBatch().begin();
        getGame().getFont48().draw(getSpriteBatch(), worldSelectText, getStageWidthTenth() * 5 - getGame().getLayout48().width / 2,
                getStageHeightTenth() * 9 - getGame().getLayout48().height / 2);
        getSpriteBatch().end();
    }

/*    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        setupButtons();
        updateTables();
    }*/


    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        buttonCol1x = getStageWidthTenth() * 3.5f - getTextButtonWidth() / 2;
        buttonRow1y = getMuteSoundEffectsY();

        buttonCol2x = getStageWidthTenth() * 6.5f - getTextButtonWidth() / 2;
        buttonRow2y = getStageHeightTenth() * 3.666f - (getImageButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();
        /*getBackButton().setWidth(getImageButtonWidth());
        getBackButton().setHeight(getImageButtonHeight());
        getBackButton().setPosition(getBackButtonX(), getBackButtonY());

        getMuteMusicButton().setWidth(getImageButtonWidth());
        getMuteMusicButton().setHeight(getImageButtonHeight());
        getMuteMusicButton().setPosition(getMuteMusicX(), getMuteMusicY());

        getMuteSoundFxButton().setWidth(getImageButtonWidth());
        getMuteSoundFxButton().setHeight(getImageButtonHeight());
        getMuteSoundFxButton().setPosition(getMuteSoundEffectsX(), getMuteSoundEffectsY());*/

        world1Button.setSize(getTextButtonWidth(), getTextButtonHeight());
        world2Button.setSize(getTextButtonWidth(), getTextButtonHeight());
        world3Button.setSize(getTextButtonWidth(), getTextButtonHeight());
        world4Button.setSize(getTextButtonWidth(), getTextButtonHeight());

        world1Button.setPosition(buttonCol1x, buttonRow1y);
        world2Button.setPosition(buttonCol1x, buttonRow2y);
        world3Button.setPosition(buttonCol2x, buttonRow1y);
        world4Button.setPosition(buttonCol2x, buttonRow2y);

        world1Button.setText(world1Text);
        world2Button.setText(world2Text);
        world3Button.setText(world3Text);
        world4Button.setText(world4Text);

    }
    @Override
    public void clickListeners() {
        super.clickListeners();

        world1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 1));
                playButtonSound();
                dispose();
            }
        });

        world2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 2));
                playButtonSound();
                dispose();
            }
        });

        world3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 3));
                playButtonSound();
                dispose();
            }
        });

        world4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 4));
                playButtonSound();
                dispose();
            }
        });

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
                playButtonSound();
                dispose();
            }
        });

/*        getMuteMusicButton().addListener(new ClickListener() {
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
        world1Text = getGame().getBundle().get("world1");
        world2Text = getGame().getBundle().get("world2");
        world3Text = getGame().getBundle().get("world3");
        world4Text = getGame().getBundle().get("world4");
        worldSelectText = getGame().getBundle().get("worldSelect");
    }
}
