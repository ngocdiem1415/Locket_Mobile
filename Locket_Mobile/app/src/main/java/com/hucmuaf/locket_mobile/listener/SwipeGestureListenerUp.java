package com.hucmuaf.locket_mobile.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.activity.PageComponentActivity;
//import com.hucmuaf.locket_mobile.activity.ReactActivity;

public class SwipeGestureListenerUp extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private Context context;

    public SwipeGestureListenerUp(Context context) {
        this.context = context;
    }

    private void startActivityWithAnimation(Context context, Class<?> cls, int animEnter) {
        if (context == null) return;

        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(animEnter, R.anim.no_animation);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        if (e1 == null || e2 == null) return false;
        if (context == null) return false;
        float diffY = e2.getY() - e1.getY();
        if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffY < 0) { // Vuốt lên
                Log.d("Gesture", "Vuốt lên được phát hiện!");
                startActivityWithAnimation(context, PageComponentActivity.class, R.anim.slide_up);
                return true;
            }
        }
        return false;
    }
}
