package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LevelSelectScreen extends NewScreen {
    private Texture background;
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private float textButtonY2 = getStageHeight() / 2.5f - (getTextButtonHeight() / 2);

    public LevelSelectScreen(JDCEGame g) {
        super(g);

        background = new Texture(Gdx.files.internal("levelselect_ph.png"));

        final TextButton levelButton1 = new TextButton("Level 1", getUiSkin());

        levelButton1.setWidth(getTextButtonWidth());
        levelButton1.setHeight(getTextButtonHeight());
        levelButton1.setPosition(textButtonX, textButtonY1);

        getGameStage().addActor(levelButton1);
        Gdx.input.setInputProcessor(getGameStage());

        levelButton1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen gs = new GameScreen(getGame());
                getGame().setScreen(gs);
            }
        });
    }

    @Override
    public void render(float delta) {
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(background, 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();
    }

    public float getStageWidth() {
        return getGameStage().getWidth();
    }

    public float getStageHeight() {
        return getGameStage().getHeight();
    }
}
