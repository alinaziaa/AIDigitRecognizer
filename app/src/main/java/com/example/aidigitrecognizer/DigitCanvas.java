
package com.example.aidigitrecognizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DigitCanvas extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

    private boolean drawingExists = false;

    public DigitCanvas(Context context) {
        super(context);
        init();
    }

    public DigitCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DigitCanvas(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.BLACK);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(24f);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                drawingExists = true;
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                path.lineTo(x, y);
                invalidate();
                performClick();
                return true;

            default:
                return false;
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public boolean hasDrawing() {
        return drawingExists;
    }

    public void clearCanvas() {
        path.reset();
        drawingExists = false;
        invalidate();
    }

    // Convert the drawing into a Bitmap image
    public Bitmap getDrawingBitmap() {

        if (getWidth() <= 0 || getHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                getWidth(),
                getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas bitmapCanvas = new Canvas(bitmap);

        draw(bitmapCanvas);

        return bitmap;
    }
}
