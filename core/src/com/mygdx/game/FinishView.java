package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class FinishView extends NewScreen {
    Preferences highscores;
    private Texture background;

    public FinishView(JDCEGame g) {
        super(g);
        highscores = Gdx.app.getPreferences("highscores");
        background = new Texture(Gdx.files.internal("bluebackground.png"));

    }

    @Override
    public void render(float delta) {
        getSpriteBatch().setProjectionMatrix(getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(background, 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        //getGameStage().draw();
    }

    public void addHighScore(float f) {
        highscores.putFloat("highscore", f);
        highscores.flush();
    }
}
