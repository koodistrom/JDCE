package fi.tamk.jdce;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class WorldSelectScreen extends NewScreen {
    private Button world1Button;
    private Button world2Button;
    private Button world3Button;
    private Button world4Button;

    private float buttonCol1x;
    private float buttonCol2x;
    private float buttonRow1y;
    private float buttonRow2y;

    private float worldButtonWidth;
    private float worldButtonHeight;

    private String worldSelectText;

    public WorldSelectScreen(JDCEGame g) {
        super(g);

        setupButtonBounds();

        world1Button = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake1.png"))));
        world2Button = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake2.png"))));
        world3Button = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake3.png"))));
        world4Button = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("kuvake4.png"))));

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

        getGame().getLayout48().setText(getGame().getFont48(), worldSelectText);

        getSpriteBatch().begin();
        getGame().getFont48().draw(getSpriteBatch(), worldSelectText, getStageWidthTenth() * 5 - getGame().getLayout48().width / 2,
                getStageHeightTenth() * 9 - getGame().getLayout48().height / 2);
        getSpriteBatch().end();
    }

    @Override
    public void setupButtonBounds() {
        super.setupButtonBounds();

        worldButtonHeight = getStageHeight() / 5;
        worldButtonWidth = worldButtonHeight * 2;

        buttonCol1x = getStageWidthTenth() * 3.5f - worldButtonWidth / 2;
        buttonRow1y = getMuteSoundEffectsY();

        buttonCol2x = getStageWidthTenth() * 6.5f - worldButtonWidth / 2;
        buttonRow2y = getStageHeightTenth() * 3.666f - (getImageButtonHeight() / 2);
    }

    @Override
    public void setupButtons() {
        super.setupButtons();

        world1Button.setSize(worldButtonWidth, worldButtonHeight);
        world2Button.setSize(worldButtonWidth, worldButtonHeight);
        world3Button.setSize(worldButtonWidth, worldButtonHeight);
        world4Button.setSize(worldButtonWidth, worldButtonHeight);

        world1Button.setPosition(buttonCol1x, buttonRow1y);
        world2Button.setPosition(buttonCol2x, buttonRow1y);
        world3Button.setPosition(buttonCol1x, buttonRow2y);
        world4Button.setPosition(buttonCol2x, buttonRow2y);

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

    }
    @Override
    public void updateTexts() {
        worldSelectText = getGame().getBundle().get("worldSelect");
    }
}
