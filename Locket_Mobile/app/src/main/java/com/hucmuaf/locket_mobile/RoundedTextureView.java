package com.hucmuaf.locket_mobile;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.TextureView;

import androidx.annotation.NonNull;

public class RoundedTextureView extends TextureView {
    private Path path;
    private float cornerRadius = 50f; // bo góc 50px, bạn có thể đổi sang dp

    public RoundedTextureView(Context context) {
        super(context);
        init();
    }

    public RoundedTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.reset();
        RectF rect = new RectF(0, 0, w, h);
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW);
        path.close();
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.clipPath(path);
        super.draw(canvas);
        canvas.restore();
    }

//    @Override
//    public void draw(Canvas canvas) {
//        canvas.save();
//        canvas.clipPath(path);
//        super.draw(canvas);
//        canvas.restore();
//    }
}
