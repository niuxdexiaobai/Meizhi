package me.drakeet.meizhi.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import me.drakeet.meizhi.R;

/*
 * A hacky Behavior class for FloatingActionButton to collapse
 * With the AppBarLayout
 * Only works with a collapsing toolbar (AppBarLayout)
 *
 */
public class FABAutoHideBehavior extends CoordinatorLayout.Behavior
{
	public FABAutoHideBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public boolean onInterceptTouchEvent(final CoordinatorLayout parent, final View child, MotionEvent ev) {
		if (!(child instanceof FloatingActionButton)) throw new IllegalArgumentException("Cannot work for non-FAB views");
		
		CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
		View anchor = parent.findViewById(lp.getAnchorId());

		if (anchor == null) {
			throw new IllegalStateException("must be anchored");
		}
		
		Object tag = anchor.getTag(R.id.appbar);
		
		if (tag == null) tag = false;
		
		if (!Boolean.parseBoolean(tag.toString())) {
			final View appbar = parent.findViewById(R.id.appbar);
			final View toolbar = parent.findViewById(R.id.toolbar);
			
			anchor.getViewTreeObserver().addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
				@Override
				public void onDraw() {
					int childHeight = child.getMeasuredHeight() + (parent.getMeasuredHeight() - child.getBottom());
					int toolbarHeight = toolbar.getMeasuredHeight();
					float translationY = appbar.getTranslationY();
					child.setTranslationY(- translationY / (float) toolbarHeight * childHeight);
				}
			});
			
			anchor.setTag(R.id.appbar, true);
		}
		
		return super.onInterceptTouchEvent(parent, child, ev);
	}
	
}
