package edu.utep.cs.cs4330.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainGameThread extends Thread {

    public static final int MAX_FPS = 30;
    public static double averageFps;

    private SurfaceHolder surfaceHolder;
    private GameSurfaceView gameSurfaceView;
    private boolean running;
    public static Canvas canvas;


    public void setRunning(boolean running){ this.running = running;}


    public MainGameThread(SurfaceHolder surfaceHolder, GameSurfaceView gameSurfaceView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameSurfaceView = gameSurfaceView;
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running){

            startTime = System.nanoTime();
            canvas = null;

            /** Main actions happen here: **/
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    /** update -> changes values of objects **/
                    this.gameSurfaceView.update();

                    /** draw **/
                    this.gameSurfaceView.draw(canvas);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;

            /** Capping frame rate **/
            try{

                if(waitTime > 0){
                    this.sleep(waitTime);
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if(frameCount == MAX_FPS){
                averageFps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;

            }
        }

    }
}
