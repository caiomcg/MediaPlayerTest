package com.caiomcg.testplayer.Player;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by caiomcg on 02/04/2017.
 */

public final class VideoPlayer extends MediaPlayer implements MediaPlayer.OnErrorListener {
    private static VideoPlayer videoPlayer = new VideoPlayer();
    private boolean syncPlayer = false;
    private long startTimeSynchd;
    private final int MAX_DRIFT = 200;
    private boolean running = false;

    private VideoPlayer() {
        setOnErrorListener(this);
    }

    public static synchronized VideoPlayer getInstance(@NonNull Context appContext) {
        return videoPlayer;
    }

    public void init(@NonNull SurfaceHolder surface, @NonNull String url) throws IOException {
        setDisplay(surface);
        setDataSource(url);
        setScreenOnWhilePlaying(true);
        prepareAsync();
    }

    public void start(int offset) {
        super.start();

        //if (holder != null) {
        //    holder.lock();
        //}

        if (offset > 0) {
            seekTo(offset + MAX_DRIFT);
        }

        running = true;
    }

    public boolean isRunning() {
        return running;
    }

    public void adjustPlayback(int ptsServer) throws InterruptedException {

        int playerCurrentTime = getCurrentPosition();

        Log.e("PLAYER", "PLAYER TIME : " + playerCurrentTime
                + " SERVER TIME: " + ptsServer);

        if (!syncPlayer) {
            long driftTime;
            if ( (ptsServer - playerCurrentTime) > MAX_DRIFT ) {
                Log.e("PLAYER", "seek()");
                driftTime = (ptsServer - playerCurrentTime) << 1;
                this.seekTo(playerCurrentTime + (int) driftTime);
            }
            else {
                if ( (playerCurrentTime - ptsServer) > MAX_DRIFT ) {
                    Log.e("PLAYER", "delay()");
                    driftTime = (playerCurrentTime - ptsServer) >> 1;
                    this.pause();
                    Thread.sleep(driftTime);
                    this.start();
                }
                else {
                    syncPlayer = true;
                    startTimeSynchd = ptsServer;
                }
            }
        }
        else if ((ptsServer - startTimeSynchd) > MAX_DRIFT /*1m*/) {
            syncPlayer = false;
        }
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        //Handle in the future
        return false;
    }
}
