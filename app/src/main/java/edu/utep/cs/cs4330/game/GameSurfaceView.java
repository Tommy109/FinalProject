package edu.utep.cs.cs4330.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MainGameThread thread;

    private Player player;
    private Boss boss;
    private Point playerPosition;
    private Bitmap background;

    private long gameOverTime;

    private boolean playerMoving = false;
    private boolean gameOver = false;

    private MediaPlayer mediaPlayer;


    /**
     * Manager for terrains
     **/
    private TerrainManager terrainManager;


    public GameSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        Utilities.CURRENT_CONTEXT = context;

        BitmapFactory bf = new BitmapFactory();
        background = bf.decodeResource(Utilities.CURRENT_CONTEXT.getResources(), R.drawable.city);

        thread = new MainGameThread(getHolder(), this);

        playerCreation();
        bossCreation();
        terrainManager = new TerrainManager(175, 350, 75);

        mediaPlayer = MediaPlayer.create(Utilities.CURRENT_CONTEXT, R.raw.fight);
        mediaPlayer.start();


        mediaPlayer.setLooping(true);


        setFocusable(true);
    }


    private void playerCreation() {
        player = new Player(new Rect(100, 100, 200, 200));
        playerPosition = new Point(Utilities.SCREEN_WIDTH / 2, 3 * Utilities.SCREEN_HEIGHT / 4);
        player.update(playerPosition);
        playerMoving = false;

    }

    private void bossCreation() {
        boss = new Boss(new Rect(0, 0, 250, 250));
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainGameThread(getHolder(), this);

        Utilities.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            retry = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();


        switch (event.getAction()) {


            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains(x, y))
                    playerMoving = true;
                if (gameOver && (System.currentTimeMillis() - gameOverTime) >= 2000) {
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (playerMoving && !gameOver)
                    playerPosition.set(x, y);
                break;
            case MotionEvent.ACTION_UP:
                playerMoving = false;
                break;
        }


        return true;
    }

    public void update() {

        if (!gameOver) {
            player.update(playerPosition);

            if (!gameOver) {

                if (!terrainManager.getIsDone()) {
                    terrainManager.updateAll();
                    //boss.update();
                    //boss.updateMove();
                    if (terrainManager.playerCollide(player)) {
                        mediaPlayer.stop();

                        MediaPlayer crashSound = MediaPlayer.create(Utilities.CURRENT_CONTEXT, R.raw.crash);
                        crashSound.start();
                        gameOver = true;
                        gameOverTime = System.currentTimeMillis();
                    }
                }
                else{
                    boss.update();
                    boss.updateMove();

                }
            }


        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        canvas.drawBitmap(background, null, canvas.getClipBounds(), null);

        player.draw(canvas);
        boss.draw(canvas);
        terrainManager.drawAll(canvas);

        if (gameOver) {

            Paint paint = new Paint();
            paint.setTextSize(75);
            paint.setTextScaleX(1.5f);
            paint.setColor(Color.WHITE);
            drawText(canvas, paint, "Game Over!");
        }
    }


    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void reset() {
        playerCreation();
        terrainManager = new TerrainManager(300, 400, 100);
        mediaPlayer = MediaPlayer.create(Utilities.CURRENT_CONTEXT, R.raw.fight);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }

    private void drawText(Canvas canvas, Paint paint, String text) {
        Rect rect = new Rect();

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(rect);
        int h = rect.height();
        int w = rect.width();

        paint.getTextBounds(text, 0, text.length(), rect);
        float x = w / 2f - rect.width() / 2f - rect.left;
        float y = h / 2f + rect.height() / 2f - rect.bottom;

        canvas.drawText(text, x, y, paint);
    }

}
