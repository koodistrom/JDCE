package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asset extends GameObject {
    public Asset(GameScreen game) {
        super(game);
    }

    public void setLocationInLevel(Float xPercentage, LevelCreator2 levelCreator, Texture texture) {
        for (int i=0; i<levelCreator.allVertices.size(); i++){
            if(levelCreator.allVertices.get(i).x>xPercentage*(levelCreator.lastX/100)){


                Vector2 oneBefore = levelCreator.allVertices.get(i-1);

                Vector2 oneAfter = levelCreator.allVertices.get(i);

                Vector2 positiontoground = new Vector2(0,1);
                positiontoground.setAngle(rotation+90);
                setLocation(levelCreator.allVertices.get(i).x + positiontoground.x, levelCreator.allVertices.get(i).y+ positiontoground.y);

                System.out.println(positiontoground);
                break;
            }
        }


        body.setTransform(getX() + getWidth()/2,getY() + getHeight()/2, MathUtils.degreesToRadians*rotation);
    }
}
