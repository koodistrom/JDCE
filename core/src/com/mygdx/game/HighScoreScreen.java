package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class HighScoreScreen extends NewScreen {
    public HighScoreScreen(JDCEGame g) {
        super(g);
    }

    @Override
    public void render(float delta) {
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);
        Gdx.gl.glClearColor(0.2f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}

