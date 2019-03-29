package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ConnectScreen extends NewScreen {
    private float textButtonX = getStageWidth() / 2 - (getTextButtonWidth() / 2);
    private float textButtonY1 = getStageHeight() / 2 - (getTextButtonHeight() / 2);
    public ConnectScreen(JDCEGame g) {
        super(g);
        final TextButton connectButton = new TextButton("connect", getUiSkin());


        connectButton.setWidth(getTextButtonWidth());
        connectButton.setHeight(getTextButtonHeight());
        connectButton.setPosition(textButtonX, textButtonY1);
    }
}
