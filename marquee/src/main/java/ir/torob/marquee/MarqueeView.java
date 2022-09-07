package ir.torob.marquee;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

public class MarqueeView extends HorizontalScrollView {

	View child;

	public MarqueeView(Context context) {
		this(context,null);
	}

	public MarqueeView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setSmoothScrollingEnabled(true);
	}

	//    TimerTask timerTask = new TimerTask() {
	//    };
	//    Timer timer = new Timer();
	private int totalFrame=0;
	private Runnable little_scroll = new Runnable() {
		@Override
		public void run() {

			if (totalFrame%3==0) {
				if (isRtl()) {
					MarqueeView.this.smoothScrollBy(-1, 0);
				}else {
					MarqueeView.this.smoothScrollBy(1, 0);
				}
			}
			totalFrame++;

		}
	};
	private Runnable full_scroll = new Runnable() {
		@Override
		public void run() {
			if (isRtl()){
				MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}else {
				MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_LEFT);
			}

		}
	};
	private  Handler handler = new Handler();
	private  Runnable runnable = new Runnable() {
		int wait = 0;
		int repeat = 0;
		int initial_wait = 0;
		final int MAX_REPEAT = Integer.MAX_VALUE;
		final int INITIAL_WAIT_TICKS = 50;
		final int FINAL_WAIT_TICKS = 30;

		@Override
		public void run() {

			if (repeat >= MAX_REPEAT) {
				//				post(new Runnable() {
				//					@Override
				//					public void run() r{
				if (isRtl()) {
					MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}else {
					MarqueeView.this.fullScroll(HorizontalScrollView.FOCUS_LEFT);
				}
				//					}
				//				});
				//				cancel();
				return;
			}
			if (isRtl()){
				if (MarqueeView.this.getScrollX() != 0) {
					if (initial_wait > INITIAL_WAIT_TICKS) {
						post(little_scroll);
					}
					initial_wait++;
				} else {
					wait++;
					if (wait > FINAL_WAIT_TICKS) {
						post(full_scroll);
						wait = 0;
						repeat++;
						initial_wait = 0;
					}
				}
				handler.postDelayed(runnable, 30);

			}else {
				if (MarqueeView.this.getScrollX() != (child.getWidth()-getWidth())) {
					if (initial_wait > INITIAL_WAIT_TICKS) {
						post(little_scroll);
					}
					initial_wait++;
				} else {
					wait++;
					if (wait > FINAL_WAIT_TICKS) {
						post(full_scroll);
						wait = 0;
						repeat++;
						initial_wait = 0;
					}
				}
				handler.postDelayed(runnable, 30);


			}
		}
	};
	public  boolean onLayoutExecuted = false;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!onLayoutExecuted) {
			if (isRtl()) {
				this.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			}else {
				this.fullScroll(HorizontalScrollView.FOCUS_LEFT);
			}
			child = this.getChildAt(0);
			//            Log.e("Marquee: ", "canScroll? " + canScroll());
			if (!canScroll()) {
				FrameLayout.LayoutParams params = (LayoutParams) child.getLayoutParams();
//                params.gravity = Gravity.RIGHT;
				child.setLayoutParams(params);
			} else {
				//                new Timer().schedule(timerTask, 500, 30);
				handler.postDelayed(runnable, 500);
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

	private boolean isRtl(){
		return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
	}

}