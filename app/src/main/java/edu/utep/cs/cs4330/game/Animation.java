package edu.utep.cs.cs4330.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

    private Bitmap[] frameList;
    private int index;
    private long lastFrameTime;
    private float frameTime;

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    public void run() {
        isRunning = true;
        index = 0;
        lastFrameTime = System.currentTimeMillis();
    }

    public void stop() {
        isRunning = false;
    }


    public Animation(Bitmap[] frameList, float duration) {
        this.frameList = frameList;
        index = 0;

        frameTime = duration / frameList.length;
        lastFrameTime = System.currentTimeMillis();
    }

    public void draw(Canvas canvas, Rect destination) {
        if(isRunning){
            canvas.drawBitmap(frameList[index], null, destination, new Paint());

            scale(destination);
        }
    }

    public void update() {

        if(System.currentTimeMillis() - lastFrameTime > frameTime*1000
                && isRunning()){
            index++;
            index = index >= frameList.length ? 0 : index;

            lastFrameTime = System.currentTimeMillis();
        }
    }

    private void scale(Rect rect){
        float ratio = (float)(frameList[index].getWidth())/frameList[index].getHeight();

        if(rect.width() > rect.height())
            rect.left = rect.right - (int)(rect.height() * ratio);
        else
            rect.top = rect.bottom - (int)(rect.width() * (1/ratio));

    }
}
