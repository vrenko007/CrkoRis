package fri.crkoris.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

    private static final float TOLERANCE = 5;
    private static final float STROKE_WIDTH = 30f;
    public int mHits, mTries;
    public Bitmap mBitmap;
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;
    private String mCharacter;
    private int mTextColor;
    private float w, h;
    private int xPos, yPos;
    private int black_pixel_count;
    private int left,top,right,bottom;

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
        mTextColor = Color.argb(255, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setTextSize(200f * getResources().getDisplayMetrics().density);

        mCharacter = "A";
        mHits = 0;
        mTries = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        setUpBitmap();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void setUpBitmap() {
        int width = (int) w, height = (int) h;
        this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        xPos = (canvas.getWidth() / 2);
        yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect();
        mPaint.getTextBounds(mCharacter, 0, 1, rect);
        int dy = canvas.getHeight() / 2 + rect.height() / 2;
        left = canvas.getWidth() / 2 - rect.width() / 2;
        right = canvas.getWidth() / 2 + rect.width() / 2;
        top = canvas.getHeight() / 2 - rect.height() / 2 + (yPos - dy);
        bottom = canvas.getHeight() / 2 + rect.height() / 2 + (yPos - dy);
        if(mCharacter.equals("j") || mCharacter.equals("g")){
            top+=mPaint.descent();
            bottom+=mPaint.descent();
        }
        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                if (mBitmap.getPixel(x, y) == Color.BLACK)
                    black_pixel_count++;
            }
        }
    }

    public float finalizeBitmap() {
        Canvas canvas = new Canvas(mBitmap);
        mPaint.setColor(Color.RED);
        canvas.drawPath(mPath, mPaint);
        int black_count = 0;
        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                if (mBitmap.getPixel(x, y) == Color.BLACK)
                    black_count++;
            }
        }
        float result = (float) black_count / black_pixel_count;
        return 1 - result;
    }


    private boolean isInside(float x, float y) {
        int pixel = mBitmap.getPixel((int) x, (int) y);
        return pixel == Color.BLACK;
    }


    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mPath, mPaint);
        mPaint.setStrokeWidth(1);
        canvas.drawRect(left,top,right,bottom,mPaint);
        mPaint.setStrokeWidth(STROKE_WIDTH);

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

    public void setCharacter(String character, int known) {
        this.mCharacter = character;
        int alpha = 10 * (10 - known);
        mTextColor = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
        invalidate();
    }
}