package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class LevelSelectScreen extends NewScreen {
    private float heightUnit = getStageHeight() / 3;
    private float widthUnit = getStageWidth()/4;
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private float textButtonY2 = getStageHeight() / 2.5f - (getTextButtonHeight() / 2);
    private ArrayList<ArrayList<TextButton>> buttonGrid;
    private int levelnumber;

    public LevelSelectScreen(JDCEGame g) {
        super(g);
        levelnumber = 1;
        buttonGrid = new ArrayList<ArrayList<TextButton>>();
        for(int i = 0; i<2;i++){
            buttonGrid.add(new ArrayList<TextButton>());
            for(int n=0; n<3;n++){
                buttonGrid.get(i).add( new TextButton("Level "+levelnumber, getUiSkin()));

                buttonGrid.get(i).get(n).setWidth(getTextButtonWidth());
                buttonGrid.get(i).get(n).setHeight(getTextButtonHeight());
                buttonGrid.get(i).get(n).setPosition(widthUnit*n, (heightUnit*i)+1);

                getGameStage().addActor(buttonGrid.get(i).get(n));


                buttonGrid.get(i).get(n).addListener(new ClickListener() {
                    int level = levelnumber;
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        GameScreen gs = new GameScreen(getGame(),level);
                        getGame().setScreen(gs);
                    }
                });

                levelnumber++;

            }
        }

        setBackground(new Texture(Gdx.files.internal("levelselect_ph.png")));


        Gdx.input.setInputProcessor(getGameStage());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
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
