package edu.utep.cs.cs4330.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.Random;

public class Boss implements Renderable {

    private Rect rectangle;
    private AnimationManager manager;
    private Point bossPosition;

    private Animation moveInPlace;
    private boolean isFight = false;
    private Random randGen = new Random();
    private int xDir,yDir;

    public boolean isFight() {
        return isFight;
    }

    public void move(){
        int currX = bossPosition.x;
        int currY = bossPosition.y;

        int distX, distY;
        distX = distY = 10;



        if(yDir == 1){
            if(currY < Utilities.SCREEN_HEIGHT){
                yDir = 0;
            }
            bossPosition.set(currX,currY+distY);
        if(yDir == 0){

            if(currY < 0)
                yDir = 1;
        }
            bossPosition.set(currX,currY-distY);
        }




    }

    public void setIsFight(boolean isFight){
        this.isFight = isFight;
    }

    public Rect getRectangle(){
        return rectangle;
    }

    public boolean inScreen(){
        if(rectangle.left <= 0 || rectangle.top <= 0)
            return true;
        if(rectangle.right >= Utilities.SCREEN_WIDTH || rectangle.bottom >= Utilities.SCREEN_HEIGHT)
            return false;

        return true;
    }

    public Boss(Rect rectangle){
        this.xDir = randGen.nextInt(2);
        this.yDir = randGen.nextInt(2);
        this.rectangle = rectangle;
        this.bossPosition = new Point(Utilities.SCREEN_WIDTH/2,((Utilities.SCREEN_HEIGHT/4)/2)+40);
        BitmapFactory bf = new BitmapFactory();
        Bitmap idleImg = bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.minotaur_stand);
        Bitmap walk1 =  bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.minotaur_step1);
        Bitmap walk2 =  bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(),R.drawable.minotaur_step2);

        moveInPlace = new Animation(new Bitmap[]{idleImg,walk1,walk2}, 0.5f);

        manager = new AnimationManager((new Animation[]{moveInPlace}));

    }

    @Override
    public void draw(Canvas canvas) {
        manager.draw(canvas,rectangle);
    }

    @Override
    public void update() {
        manager.update();
    }



    public void updateMove(){

        move();

        rectangle.set(bossPosition.x - rectangle.width()/2, bossPosition.y - rectangle.height()/2,
                bossPosition.x+rectangle.width()/2, bossPosition.y + rectangle.height()/2);


        manager.runAll(0);
        manager.update();
    }
}
