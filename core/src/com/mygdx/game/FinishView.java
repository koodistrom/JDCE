package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class FinishView extends NewScreen {
    Preferences highscores;
    private Texture background;
    float finishTime;
    private FreeTypeFontGenerator generator;
    private FreeTypeFont font12;

    public FinishView(JDCEGame g, float time) {
        super(g);
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        background = new Texture(Gdx.files.internal("bluebackground.png"));
        addHighScore(Utilities.secondsToString(time));

        generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        font12 = generator.generateFont(parameter);
    }

    @Override
    public void render(float delta) {
        getSpriteBatch().setProjectionMatrix(getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(background, 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        //getGameStage().draw();
    }

    public void addHighScore(String score) {
        highscores.putString("High Score", score);
        highscores.flush();
    }
}
