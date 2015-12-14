package com.example.dominik.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

    private static final float TOLERANCE = 5;
    private static final float STROKE_WIDTH = 10;
    private Path mPathLines,mPathDots;
    private Paint mPaintLines,mPaintDots;
    private float mX, mY;

    public PaintView(Context c) {
        super(c);
        mPathLines = new Path();
        mPathDots=new Path();

        mPaintLines = new Paint();
        mPaintLines.setAntiAlias(true);
        mPaintLines.setTextAlign(Paint.Align.CENTER);
        mPaintLines.setColor(Color.BLACK);
        mPaintLines.setStyle(Paint.Style.STROKE);
        mPaintLines.setStrokeJoin(Paint.Join.ROUND);
        mPaintLines.setStrokeWidth(STROKE_WIDTH);
        mPaintLines.setTextSize(500f * getResources().getDisplayMetrics().density);

        mPaintDots = new Paint();
        mPaintDots.setAntiAlias(true);
        mPaintDots.setColor(Color.BLACK);
        mPaintDots.setStyle(Paint.Style.FILL);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaintLines.descent() + mPaintLines.ascent()) / 2));
        canvas.drawText("爨", xPos, yPos, mPaintLines);
        canvas.drawPath(mPathLines, mPaintLines);
        canvas.drawPath(mPathDots, mPaintDots);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPathLines.moveTo(x, y);
        mPathDots.addCircle(x, y, STROKE_WIDTH/2, Path.Direction.CCW);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPathLines.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPathLines.reset();
        mPathDots.reset();
        invalidate();
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPathLines.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }
}