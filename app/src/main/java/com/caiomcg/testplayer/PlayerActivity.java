package com.caiomcg.testplayer;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import com.caiomcg.testplayer.Player.VideoPlayer;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{

    private static final String TAG = "PlayerActivity";

    private VideoPlayer player;

    private ProgressDialog progressDialog;

    private WifiHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.i(TAG, "Requesting fullscreen window");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        holder = new WifiHolder(getApplicationContext(), WifiHolder.LockType.Default);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        holder.unlock();
        player = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Creating VideoPlayer");
        player = VideoPlayer.getInstance(this);
        player.setOnPreparedListener(this);

        Log.i(TAG, "Creating Synchronizer");

        Log.i(TAG, "Adding surface callback");
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);

        Log.i(TAG, "Preparing ProgressDialog");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Preparing...");
        progressDialog.show();

        holder.lock();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "Stopping dialog");
        progressDialog.dismiss();
        Log.i(TAG, "Starting player");
        player.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            Log.i(TAG, "Player is initiating");
            player.init(surfaceHolder, getIntent().getExtras().getString("url"));
        } catch (IOException exc) {
            Log.e(TAG, "Bad file path"); //CLEAN AND RETURN TO AID SCREEN
            exc.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i(TAG, "Surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "Surface destroyed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
