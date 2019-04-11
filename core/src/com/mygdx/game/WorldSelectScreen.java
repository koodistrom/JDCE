package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorldSelectScreen extends NewScreen {
    private TextButton world1Button;
    private TextButton world2Button;
    private TextButton world3Button;
    private TextButton world4Button;

    private float textButtonX;
    private float continueButtonY;
    private float retryButtonY;
    private float mainMenuButtonY;

    private String world1Text;
    private String world2Text;
    private String world3Text;
    private String world4Text;
    private String worldSelectText;

    private Table worldSelectTable;

    public WorldSelectScreen(JDCEGame g) {
        super(g);

        setupButtonBounds();

        setBackground(new Texture(Gdx.files.internal("bluebackground.png")));

        worldSelectTable = new Table();

        world1Button = new TextButton(world1Text, getUiSkin());
        world2Button = new TextButton(world2Text, getUiSkin());
        world3Button = new TextButton(world3Text, getUiSkin());
        world4Button = new TextButton(world4Text, getUiSkin());

        updateTexts();
        setupButtons();

        setUpWorldSelectTable();

        getGameStage().addActor(worldSelectTable);
        getGameStage().addActor(getMuteMusicButton());
        getGameStage().addActor(getMuteSoundFxButton());
        getGameStage().addActor(getBackButton());

        Gdx.input.setInputProcessor(getGameStage());

        clickListeners();
    }

    public void setUpWorldSelectTable() {
        updateTables();
        worldSelectTable.defaults().grow().space(20);
        worldSelectTable.add(world1Button);
        worldSelectTable.add(world2Button);
        worldSelectTable.row();
        worldSelectTable.add(world3Button);
        worldSelectTable.add(world4Button);
    }
    @Override
    public void updateTables() {
        worldSelectTable.setSize(getStageWidth() / 4, getStageHeight() / 3.5f);
        worldSelectTable.setPosition(getGameStage().getWidth() / 2 - (worldSelectTable.getWidth() / 2),
                getGameStage().getHeight() / 2 - (worldSelectTable.getHeight() / 2));
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

        getLayout48().setText(getFont48(), worldSelectText);

        getSpriteBatch().begin();
        getFont48().draw(getSpriteBatch(), worldSelectText, getStageWidthTenth() * 5 - getLayout48().width / 2,
                getStageHeightTenth() * 9 - getLayout48().height / 2);
        getSpriteBatch().end();
    }

/*    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        setupButtons();
        updateTables();
    }*/


    /*@Override
    public void setupButtonBounds() {
        updateTenths();

        setTextButtonHeight(getGameStage().getHeight() / 6);
        setTextButtonWidth(getGameStage().getWidth() / 3);
        setImageButtonWidth(getTextButtonHeight());
        setImageButtonHeight(getTextButtonHeight());

        setBackButtonX(getStageWidthTenth() - (getImageButtonWidth() / 2));
        setBackButtonY(getStageHeightTenth() * 1 - (getImageButtonHeight() / 2));
        setMuteMusicY(getStageHeightTenth() * 9 - (getImageButtonHeight() / 2));
        setMuteSoundEffectsY(getStageHeightTenth() * 6.333f - (getImageButtonHeight() / 2));
        setMuteMusicX(getStageWidthTenth() * 9 - (getImageButtonWidth() / 2));
        setMuteSoundEffectsX(getMuteMusicX());
    }*/

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

        world1Button.setText(world1Text);
        world2Button.setText(world2Text);
        world3Button.setText(world3Text);
        world4Button.setText(world4Text);

        getGameStage().setDebugAll(true);
    }
    @Override
    public void clickListeners() {
        super.clickListeners();

        world1Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 1));
            }
        });

        world2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 2));
            }
        });

        world3Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 3));
            }
        });

        world4Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new LevelSelectScreen(getGame(), 4));
            }
        });

        getBackButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(new MainMenuScreen(getGame()));
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
