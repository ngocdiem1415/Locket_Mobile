package com.hucmuaf.locket_mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private Context context;

    public SwipeGestureListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY < 0) { // Vuốt lên
                Log.d("Gesture", "Vuốt lên được phát hiện!");

                Intent intent = new Intent(context, ReactActivity.class);
                context.startActivity(intent);
                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
                }
                return true;
            }
        }
        return false;
    }
}
