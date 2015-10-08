package ir.torob.marquee;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import java.util.Timer;
import java.util.TimerTask;

public class MarqueeView extends HorizontalScrollView {


    View child;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setSmoothScrollingEnabled(true);
    }

    TimerTask timerTask = new TimerTask() {
        int wait = 0;
        int repeat = 0;
        int initial_wait = 0;
        final int MAX_REPEAT = 2;
        final int INITIAL_WAIT_TICKS = 50;
        final int FINAL_WAIT_TICKS = 30;

        @Override
        public void run() {
            if (repeat >= MAX_REPEAT) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    }
                });
                cancel();
                return;
            }
            if (MarqueeView.this.getScrollX() != 0) {
                if (initial_wait > INITIAL_WAIT_TICKS) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            MarqueeView.this.smoothScrollBy(-2, 0);

                        }
                    });
                }
                initial_wait++;
            } else {
                wait++;
                if (wait > FINAL_WAIT_TICKS) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    });
                    wait = 0;
                    repeat++;
                    initial_wait = 0;
                }
            }
        }
    };
    Timer timer = new Timer();
    boolean onLayoutExecuted = false;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!onLayoutExecuted) {
            this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            child = this.getChildAt(0);
//            Log.e("Marquee: ", "canScroll? " + canScroll());
            if (!canScroll()) {
                FrameLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
                params.gravity = Gravity.RIGHT;
                child.setLayoutParams(params);
            } else {
                timer.schedule(timerTask, 500, 30);
            }
            onLayoutExecuted = true;
        }
    }

    private boolean canScroll() {
        if (child != null) {
//            Log.e("marquee: ","child of scroll: "+child.toString());
            int childWidth = child.getWidth();
            return getWidth() < childWidth + this.getPaddingRight() + this.getPaddingLeft();
        }
        return false;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}