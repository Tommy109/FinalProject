package edu.utep.cs.cs4330.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    static Context context;
    private GameSurfaceView gameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        utilSetUp();


        //setContentView(R.layout.activity_main);
        gameSurfaceView = new GameSurfaceView(this);
        setContentView(gameSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameSurfaceView.pause();

    }

    private void utilSetUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Utilities.SCREEN_WIDTH = dm.widthPixels;
        Utilities.SCREEN_HEIGHT = dm.heightPixels;
    }
}
