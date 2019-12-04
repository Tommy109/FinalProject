package edu.utep.cs.cs4330.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;


public class Player implements Renderable {

    /** rectangle for now; animated sprite later **/
    private Rect rectangle;
    private AnimationManager manager;

    private Animation idle,goingLeft, goingRight;


    /** change this later, it is ugly **/
    public Rect getRectangle(){
        return rectangle;
    }

    public Player(Rect rectangle){
        this.rectangle = rectangle;

        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.alienpink);
        Bitmap walk1 =  bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.alienpink_walk1);
        Bitmap walk2 =  bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.alienpink_walk2);

        idle = new Animation(new Bitmap[]{idleImg},2);
        goingRight = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        /** Reflect image **/
        Matrix m = new Matrix();
        m.preScale(-1, 1);

        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);

        goingLeft = new Animation(new Bitmap[]{walk1,walk2}, 0.5f);

        manager = new AnimationManager(new Animation[]{idle,goingRight,goingLeft});
    }

    @Override
    public void draw(Canvas canvas) {
        manager.draw(canvas,rectangle);
    }

    @Override
    public void update() {
        manager.update();
    }

    public void update(Point point){
        //l,t,r,b

        float previousLeft = rectangle.left;

        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2,
                point.x+rectangle.width()/2, point.y + rectangle.height()/2);

        int currState;

        if(rectangle.left - previousLeft > 5)
            currState = 1;
        else if(rectangle.left - previousLeft < -5)
            currState = 2;
        else
            currState = 0;

        manager.runAll(currState);
        manager.update();
    }
}
