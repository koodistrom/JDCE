package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen extends NewScreen {
    private float startTime = 0;

    public SplashScreen(JDCEGame g) {
        super(g);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        startTime += delta;
        if(startTime < 1.75f) {
            getMeterViewport().apply();
            getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

            getSpriteBatch().begin();
            getSpriteBatch().draw(getGame().getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
            getSpriteBatch().end();
        } else {
            endSplashScreen();
        }
    }

    public void endSplashScreen() {
        getGame().setScreen(new MainMenuScreen(getGame()));
    }
}
