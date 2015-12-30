package fri.crkoris.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

    private static final float TOLERANCE = 5;
    private static final float STROKE_WIDTH = 30f;
    public int mHits, mTries;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private String mCharacter;
    private float w, h;

    public PaintView(Context c) {
        super(c);
        init();
    }

    public PaintView(Context c, AttributeSet set) {
        super(c, set);
        init();
    }

    public PaintView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        init();
    }

    private void init() {
        this.setDrawingCacheEnabled(true);
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setTextSize(500f * getResources().getDisplayMetrics().density);

        mCharacter = "A";
        mHits = 0;
        mTries = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public Bitmap getUnderlyingBitmap() {
        return this.getDrawingCache();
    }

    private boolean isInside(float x, float y) {
        Bitmap b = getUnderlyingBitmap();
        int pixel = b.getPixel((int) x, (int) y);
        return pixel == Color.LTGRAY || pixel == Color.BLACK;
    }


    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mPath, mPaint);

    }

    // when ACTION_DOWN start touch according to the x,y values
    private void touchDown(float x, float y) {
        mPath.moveTo(x, y);
        mPath.addCircle(x, y, STROKE_WIDTH / 4, Path.Direction.CCW);
        mX = x;
        mY = y;
        if (isInside(x, y)) {
            mHits++;
        }
        mTries++;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            if (isInside(x, y)) {
                mHits++;
            }
            mTries++;
        }
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x < 0 || x > w || y < 0 || y > h)
            return false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public void setCharacter(String character) {
        this.mCharacter = character;
        invalidate();
    }
}