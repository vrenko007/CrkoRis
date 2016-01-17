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
    private static float STROKE_WIDTH = 40f;
    public int mHits, mTries;
    public Bitmap mBitmap;
    private Path mLinePath, mPointPath;
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
        mLinePath = new Path();
        mPointPath = new Path();

        mTextColor = Color.argb(255, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setTextSize((float) (2.5 * getResources().getDisplayMetrics().xdpi));
        STROKE_WIDTH = mPaint.getTextSize() / 16;
        mPaint.setStrokeWidth(STROKE_WIDTH);

        mCharacter = "A";
        mHits = 0;
        mTries = 0;
    }

    public void setCharacter(String character, int known) {
        this.mCharacter = character;
        int alpha = 10;
        if (known == 0) alpha = 75;
        else if (known == 1) alpha = 25;
        mTextColor = Color.argb(alpha, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY));
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        setUpBitmap(w, h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void setUpBitmap(int width, int height) {
        this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        xPos = (canvas.getWidth() / 2);
        yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect();
        mPaint.getTextBounds(mCharacter, 0, 1, rect);
        left = xPos - rect.width() / 2;
        right = xPos + rect.width() / 2;
        top = yPos + rect.top;
        bottom = yPos + rect.bottom;

        left = left < 0 ? 0 : left;
        right = right > width ? width : right;
        top = top < 0 ? 0 : top;
        bottom = bottom > height ? height : bottom;


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
        canvas.drawPath(mLinePath, mPaint);
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
        //x = x<0 ? left : x;
        //x = x>right ? right : x;
        //y = y<0 ? top : y;
        //y = y>bottom ? bottom : y;

        int pixel = mBitmap.getPixel((int) x, (int) y);
        return pixel == Color.BLACK;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        canvas.drawText(mCharacter, xPos, yPos, mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mPointPath, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mLinePath, mPaint);
        mPaint.setStrokeWidth(1);
        canvas.drawRect(left,top,right,bottom,mPaint);
        mPaint.setStrokeWidth(STROKE_WIDTH);

    }

    private void touchDown(float x, float y) {
        mLinePath.moveTo(x, y);
        mPointPath.addCircle(x, y, STROKE_WIDTH / 2, Path.Direction.CCW);
        mX = x;
        mY = y;
        if (isInside(x, y)) mHits++;
        mTries++;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mLinePath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            if (isInside(x, y)) {
                mHits++;
            }
            mTries++;
        }
    }

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
}