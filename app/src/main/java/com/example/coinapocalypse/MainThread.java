package com.example.coinapocalypse;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private CoinApocalypseView coinApocalypseView;
    private SurfaceHolder surfaceHolder;
    private Object pauseLock;
    private boolean paused;

    private Canvas canvas;

    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, CoinApocalypseView coinApocalypseView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.coinApocalypseView = coinApocalypseView;
        pauseLock = new Object();
        paused = false;
    }

    @Override
    public void run() {
        while (running){
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.coinApocalypseView.update();
                    //this.coinApocalypseView.draw(canvas);
                }
            } catch (Exception e) {} finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized (pauseLock) {
                while (paused) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    /**
     * Call this on pause.
     */
    public void onPause() {
        synchronized (pauseLock) {
            paused = true;
        }
    }

    /**
     * Call this on resume.
     */
    public void onResume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
