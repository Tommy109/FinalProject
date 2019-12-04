package edu.utep.cs.cs4330.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class TerrainManager {

    private ArrayList<Terrain> terrainList;
    private int playerGap;
    private int terrainGap;
    private int terrainHeight;
    private int score = 0;

    private long startTime;
    private long creationTime;
    private boolean isDone = false;

    public int getScore() {
        return score;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public TerrainManager(int playerGap, int terrainGap, int terrainHeight) {
        this.playerGap = playerGap; //Gap width between terrain for player to pass through
        this.terrainGap = terrainGap; //Gap between each terrain
        this.terrainHeight = terrainHeight;


        startTime = creationTime = System.currentTimeMillis();
        terrainList = new ArrayList<>();

        generateTerrains();
    }

    private void generateTerrains() {

        int y = -(terrainGap * 10);

        while (y < 0) {

            int width = (int) (Math.random() * (Utilities.SCREEN_WIDTH - playerGap));

            Terrain terrain = new Terrain(MainActivity.context, 0, y, width, y + terrainHeight, playerGap);

            terrainList.add(terrain);

            y += terrainGap;
        }

    }

    public void updateAll() {

        if (startTime < Utilities.INIT_TIME)
            startTime = Utilities.INIT_TIME;

        int elapsedTime = (int) (System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float difficulty = (float) (2 + Math.sqrt(startTime - creationTime) / 1000.0);

        /** scaling speed **/
        float speed = difficulty * Utilities.SCREEN_HEIGHT / 10000.0f;

        for (Terrain terrain : terrainList) {
            terrain.incrementY(speed * elapsedTime);
        }

        Terrain last = terrainList.get(terrainList.size() - 1);
        Terrain first = terrainList.get(0);


        if (!last.inScreen()) {

            if (score < 5) {
                int width = (int) (Math.random() * (Utilities.SCREEN_WIDTH - 300));
                int y = (int) first.getY();

                Terrain terrain = new Terrain(MainActivity.context, 0, y - terrainGap, width, y - terrainGap + terrainHeight, playerGap);
                terrainList.add(0, terrain);


            }

            if (!terrainList.isEmpty()) {
                terrainList.remove(last);
                score++;
            }

            if (terrainList.isEmpty()) {
                isDone = true;
            }
        }
    }


    public boolean playerCollide(Player player) {
        for (Terrain terrain : terrainList) {
            if (terrain.collision(player)) {
                return true;
            }
        }

        return false;
    }

    public void drawAll(Canvas canvas) {
        for (Terrain terrain : terrainList) {
            terrain.draw(canvas);
        }

        Paint paint = new Paint();
        paint.setTextSize(75);
        paint.setColor(Color.RED);
        canvas.drawText(Integer.toString(score), 20, 80, paint);
    }


}
