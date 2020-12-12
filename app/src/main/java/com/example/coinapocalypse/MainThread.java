package com.example.coinapocalypse;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {
    private CoinApocalypseView coinApocalypseView;
    private SurfaceHolder surfaceHolder;

    private Canvas canvas;

    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, CoinApocalypseView coinApocalypseView){
        super();
        this.surfaceHolder = surfaceHolder;
        this.coinApocalypseView = coinApocalypseView;
    }

    @Override
    public void run() {
        while (running){
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder) {
                    this.coinApocalypseView.update();
                    this.coinApocalypseView.draw(canvas);
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
        }
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }
}
