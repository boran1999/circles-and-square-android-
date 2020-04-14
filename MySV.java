package com.example.wariki;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySV extends SurfaceView implements SurfaceHolder.Callback {
    int r = 80;
    float w , h, xs, ys;
    boolean vflag = false;
    float bottom , left, top, right;
    int n=2, i, j;
    int colors[]={Color.GREEN, Color.BLUE, Color.BLACK, Color.MAGENTA, Color.LTGRAY, Color.CYAN, Color.RED, Color.DKGRAY, Color.YELLOW};
    float masx[] = new float[2];
    float masy[] = new float[2];
    class DrawThread extends Thread{
        boolean flag=true;
        Paint p = new Paint();
        Paint pp = new Paint();
        Paint pp1 = new Paint();
        public DrawThread(SurfaceHolder holder) {
            this.holder=holder;
        }
        SurfaceHolder holder;
        @Override
        public void run() {
            boolean xf=true, yf=true, xf1= true, yf1 = true;
            float x = masx[0], y =masy[0], x1=masx[1], y1=masy[1];
            super.run();
            p.setColor(Color.YELLOW);
            while(flag){
                Canvas c = holder.lockCanvas();
                w = c.getWidth();
                h = c.getHeight();
                pp.setColor(colors[i]);
                pp1.setColor(colors[j]);
                if(i==j){
                    flag=false;
                }
                if(c!=null){
                    c.drawColor(Color.WHITE);
                    c.drawCircle(x, y , r, pp);
                    c.drawCircle(x1, y1 , r, pp1);
                    c.drawRect(left,top,right,bottom,p);
                    if(xf==true)
                        x+=25;
                    else
                        x-=25;
                    if(x+r>=w)
                        xf=false;
                    if(x-r<=0)
                        xf=true;
                    if(xf1==true)
                        x1+=25;
                    else
                        x1-=25;
                    if(x1+r>=w)
                        xf1=false;
                    if(x1-r<=0)
                        xf1=true;
                    if(yf==true)
                        y+=25;
                    else
                        y-=25;
                    if(y+r>=h)
                        yf=false;
                    if(y-r<=0)
                        yf=true;
                    if(yf1==true)
                        y1+=25;
                    else
                        y1-=25;
                    if(y1+r>=h)
                        yf1=false;
                    if(y1-r<=0)
                        yf1=true;
                    if(Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y))<=r+r){
                        boolean temp1, temp2;
                        temp1=xf;
                        temp2=yf;
                        xf=xf1;
                        yf=yf1;
                        xf1=temp1;
                        yf1=temp2;
                    }
                    if(Math.sqrt((xs-x)*(xs-x)+(ys-y)*(ys-y))<=60+r){
                        xf=!xf;
                        yf=!yf;
                        int il = (int)(Math.random()*9);
                        if(il!=i)
                            i=il;
                        else
                            while (il==i)
                               i = (int)(Math.random()*9);
                    }
                    if(Math.sqrt((xs-x1)*(xs-x1)+(ys-y1)*(ys-y1))<=60+r){
                        xf1=!xf1;
                        yf1=!yf1;
                        int jl = (int)(Math.random()*9);
                        if(jl!=j)
                            j=jl;
                        else
                            while (jl==j)
                                j = (int)(Math.random()*9);
                    }
                    holder.unlockCanvasAndPost(c);
                    try{
                    Thread.sleep(20);
                    }
                    catch (InterruptedException e){};
                }
            }
            if(!flag){
                Canvas c = holder.lockCanvas();
                try{
                    Thread.sleep(500);
                }
                catch (InterruptedException e){};
                c.drawColor(Color.WHITE);
                Paint vp = new Paint();
                vp.setColor(Color.BLACK);
                vp.setTextSize(80);
                c.drawText("Victory!",w/2-150,h/2,vp);
                holder.unlockCanvasAndPost(c);
            }
        }
    }
    DrawThread thread;

    public MySV(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new DrawThread(holder);
        w = getWidth();
        h = getHeight();
        for (int i = 0; i < n; i++){
            if (i == 0) {
                masx[i]=(float)(r + Math.random()*(w/2-r));
                masy[i]=(float)(r + Math.random()*(h/2-r));
            }
            else {
                masx[i] = (float) (r + Math.random() * (w - r));
                masy[i] = (float) (h/2+r + Math.random() * (h - r));
            }
        }
        top = h/2+60;
        bottom = h/2-60;
        left = w/2-60;
        right = w/2+60;
        xs = w/2;
        ys = h/2;
        i = (int)(Math.random()*9);
        j = (int)(Math.random()*9);
        while(j==i){
            j = (int)(Math.random()*9);
        }
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.flag=false;
        thread.flag=true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.flag=false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(event.getX()<right && event.getX()>left && event.getY()<top && event.getY()>bottom){
                vflag = true;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(vflag){
                bottom = event.getY()-60;
                top = event.getY()+60;
                right = event.getX()+60;
                left = event.getX()-60;
                xs = event.getX();
                ys = event.getY();
            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            vflag = false;
        }
        return true;
    }
}
