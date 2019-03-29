package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConnectScreen extends NewScreen {
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    private Label connectionInfo;
    public ConnectScreen(JDCEGame g) {
        super(g);
        final TextButton connectButton = new TextButton("connect", getUiSkin());

        //connectionInfo = new Label();
        connectButton.setWidth(getTextButtonWidth());
        connectButton.setHeight(getTextButtonHeight());
        connectButton.setPosition(textButtonX, textButtonY1);

        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            JDCEGame.m_platformResolver.connect();
            }
        });
    }
}
