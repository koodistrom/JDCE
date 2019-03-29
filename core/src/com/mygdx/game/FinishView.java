package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class FinishView extends NewScreen {
    Preferences highscores;
    private Texture background;
    float finishTime;
    private FreeTypeFontGenerator generator;
    private BitmapFont font12;
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private GlyphLayout layout;
    private String score;
    private float textX;
    private float textY;
    //private OrthographicCamera pixelCamera;

    public FinishView(JDCEGame g, float time) {
        super(g);
        highscores = Gdx.app.getPreferences("JDCE_highscores");
        background = new Texture(Gdx.files.internal("bluebackground.png"));
        addHighScore(Utilities.secondsToString(time));

        /*final*/ TextButton menuButton = new TextButton("Continue", getUiSkin());
        menuButton.setWidth(getTextButtonWidth());
        menuButton.setHeight(getTextButtonHeight());
        menuButton.setPosition(textButtonX, textButtonY1);

        getGameStage().addActor(menuButton);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainMenuScreen mms = new MainMenuScreen(getGame());
                getGame().setScreen(mms);
            }
        });

        //pixelCamera.setToOrtho(false, g, getScreenHeight());

        generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 3;
        font12 = generator.generateFont(parameter);

        GlyphLayout layout = new GlyphLayout();
        score = "Your Score: " + highscores.getString("High Score");
        layout.setText(font12, score);

        textX = Gdx.graphics.getWidth()/2 - layout.width / 2;
        textY = Gdx.graphics.getHeight()/1.5f - layout.height;
    }

    @Override
    public void render(float delta) {
        getMeterViewport().apply();
        getSpriteBatch().setProjectionMatrix(getMeterViewport().getCamera().combined);

        getSpriteBatch().begin();
        getSpriteBatch().draw(background, 0, 0, getScreenWidth(), getScreenHeight());
        getSpriteBatch().end();

        getGameStage().draw();

        getGameViewport().apply();
        getSpriteBatch().setProjectionMatrix(getGameViewport().getCamera().combined);

        getSpriteBatch().begin();
        font12.draw(getSpriteBatch(), score, textX, textY);
        getSpriteBatch().end();
    }

    public void addHighScore(String score) {
        highscores.putString("High Score", score);
        highscores.flush();
    }
}
