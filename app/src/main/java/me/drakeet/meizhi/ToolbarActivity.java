package me.drakeet.meizhi;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

public abstract class ToolbarActivity extends AppCompatActivity
{
	abstract protected int getLayoutResource();
	
	protected AppBarLayout mAppBar;
	protected Toolbar mToolbar;
	protected TabLayout mTabLayout;
	protected boolean isHidden = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResource());
		
		mAppBar = (AppBarLayout) findViewById(R.id.appbar);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		
		if (mToolbar == null || mAppBar == null) {
			throw new IllegalStateException("no toolbar");
		}
		
		setSupportActionBar(mToolbar);
		
		if (Build.VERSION.SDK_INT >= 21) {
			mAppBar.setElevation(10.6f);
		}
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void setAppBarAlpha(float alpha) {
		mAppBar.setAlpha(alpha);
	}
	
	protected void hideOrShowToolbar() {
		mAppBar.animate()
			.translationY(isHidden ? 0 : -mAppBar.getHeight())
			.setInterpolator(new DecelerateInterpolator(2))
			.start();
		
		isHidden = !isHidden;
	}
}
