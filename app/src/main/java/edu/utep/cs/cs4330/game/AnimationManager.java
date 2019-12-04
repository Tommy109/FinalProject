package edu.utep.cs.cs4330.game;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationManager {

    private Animation[] animationList;
    private int index = 0;

    public AnimationManager(Animation[] animationList){
        this.animationList = animationList;
    }

    public void runAll(int index){
        for(int i = 0; i < animationList.length; i++){
            if(i == index) {
                if(!animationList[index].isRunning())
                    animationList[i].run();
            }
            else
                animationList[i].stop();
        }

        this.index = index;
    }

    public void draw(Canvas canvas, Rect rect){
        if(animationList[index].isRunning())
            animationList[index].draw(canvas,rect);
    }

    public void update(){
        if(animationList[index].isRunning())
            animationList[index].update();
    }
}
