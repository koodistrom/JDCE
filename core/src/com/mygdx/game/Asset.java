package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asset extends GameObject {
    public Asset(GameScreen game,Float xPercentage, Texture texture)  {
        super(game);
        setTexture(texture);

        setWidth(getWidth()*0.5f);
        setHeight(getHeight()*0.5f);
        setLocationInLevel(xPercentage, game.getLevelCreator());
    }

    public void setLocationInLevel(Float xPercentage, LevelCreator2 levelCreator) {

        for (int i=0; i<levelCreator.allVertices.size(); i++){
            float absolutePos = xPercentage*(levelCreator.lastX/100);
            if(levelCreator.allVertices.get(i).x>absolutePos){


                Vector2 oneBefore = levelCreator.allVertices.get(i-1);

                Vector2 oneAfter = levelCreator.allVertices.get(i);
                float xFactor = (absolutePos-oneBefore.x)/(oneAfter.x-oneBefore.x);
                float y = oneBefore.y+(oneAfter.y-oneBefore.y)*xFactor;

                setLocation(absolutePos-(getWidth()/2), y-0.1f);


                break;
            }
        }
    }
}
