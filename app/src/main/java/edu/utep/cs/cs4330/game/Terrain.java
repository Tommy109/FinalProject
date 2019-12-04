package edu.utep.cs.cs4330.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;


public class Terrain extends View implements Renderable {

    /** to rectangles with a gap in between; change to other types of
     * terrain later, and manage them using the TerrainManager
     */

    private Drawable leftSide,rightSide;
    private int x,y;

    @Override
    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public boolean inScreen(){
        if(leftSide.getBounds().top >= Utilities.SCREEN_HEIGHT) {
            //Log.d("[TRUE] HEIGHT",Utilities.SCREEN_HEIGHT+" TOP = " + leftSide.getBounds().top);

            return false;
        }

        //Log.d("[FALSE] HEIGHT",Utilities.SCREEN_HEIGHT +" TOP = " + leftSide.getBounds().top);
        return true;
    }


    public void incrementY(float y){

        //(left,top) (right,bottom)

        Rect rect = new Rect(leftSide.getBounds().left,
                (int)(leftSide.getBounds().top + y),
                leftSide.getBounds().right,
                (int)(leftSide.getBounds().bottom+y));


        leftSide.setBounds(rect);

        Rect rect2 = new Rect(rightSide.getBounds().left,
                (int)(rightSide.getBounds().top + y),
                rightSide.getBounds().right,
                (int)(rightSide.getBounds().bottom+y));


        rightSide.setBounds(rect2);

        this.y += y;
    }


    public Terrain(Context context, int x, int y, int width, int height, int gap) {
        //left, top, right, bottom

        super(context);

        this.x = x;
        this.y = y;

        Rect boundsLeft = new Rect(x,y,width,height);
        Rect boundsRight = new Rect(x+width+gap,y,Utilities.SCREEN_WIDTH,height);

        leftSide = context.getResources().getDrawable(R.drawable.wall);
        rightSide = context.getResources().getDrawable(R.drawable.wall);

        leftSide.setBounds(boundsLeft);
        rightSide.setBounds(boundsRight);

    }

    /** starting x,y position, transpose for horizontal
    public Terrain(){

    }**/

    public boolean collision(Player player){
        /** Change this, it is ugly **/
        if(leftSide.getBounds().intersect(player.getRectangle()) ||
        rightSide.getBounds().intersect(player.getRectangle())){

            Log.d("Collision", "Collided");
            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        leftSide.draw(canvas);
        rightSide.draw(canvas);

    }

    @Override
    public void update() {

    }

}

