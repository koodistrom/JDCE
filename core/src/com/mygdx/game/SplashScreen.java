package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class SplashScreen extends NewScreen {
    private float startTime = 0;

    public SplashScreen(JDCEGame g) {
        super(g);
        setBackground(new Texture(Gdx.files.internal("splash_screen_ph.png")));
    }

    @Override
    public void render(float delta) {
        startTime += delta;
        if(startTime < 1.75f) {
            getMeterViewport().apply();
            getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

            getSpriteBatch().begin();
            getSpriteBatch().draw(getBackground(), 0, 0, getScreenWidth(), getScreenHeight());
            getSpriteBatch().end();
        } else {
            endSplashScreen();
        }
    }

    public void endSplashScreen() {
        getGame().setScreen(new MainMenuScreen(getGame()));
    }
}
