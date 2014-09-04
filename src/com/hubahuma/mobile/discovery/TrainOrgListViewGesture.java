package com.hubahuma.mobile.discovery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hubahuma.mobile.R;
import com.hubahuma.mobile.utils.DisplayUtil;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class TrainOrgListViewGesture implements View.OnTouchListener {

	private final static int BUTTON_FOLLOW = 111;
	private final static int BUTTON_IMAGE = 222;
	private final static int BUTTON_LOCATION = 333;
	private final static int BUTTON_CONSULT = 444;

	Activity activity;

	// Cached ViewConfiguration and system-wide constant values
	private int mSlop;
	private int mMinFlingVelocity;
	private int mMaxFlingVelocity;
	private long mAnimationTime;

	// Fixed properties
	private ListView mListView;

	// private DismissCallbacks mCallbacks;
	private int mViewWidth = 1; // 1 and not 0 to prevent dividing by zero
	private int smallWidth = 1;
	private int largewidth = 1;
	private int textwidth = 1;
	private int textwidth2 = 1;
	private int textheight = 1;

	// Transient properties
	// private List<PendingDismissData> mPendingDismisses = new
	// ArrayList<PendingDismissData>();
	// private int mDismissAnimationRefCount = 0;
	private float mDownX;
	private boolean mSwiping;
	private VelocityTracker mVelocityTracker;
	private int mDownPosition;
	private int temp_position, opened_position, stagged_position;
	private ViewGroup mDownView, old_mDownView;
	private ViewGroup mDownView_parent;
	private TextView mDownView_parent_txt1, mDownView_parent_txt2,
			mDownView_parent_txt3, mDownView_parent_txt4;
	private boolean mPaused;
	public boolean moptionsDisplay = false;
	static TouchCallbacks tcallbacks;

	// Intermediate Usages
	String TextColor = "#FFFFFF";
	String normalColor = "#222E3E"; // deep blue
	// String redColor = "#EB2D3F"; // red
	// String singleColor = "#EB2D3F";

	public float textSizeInSp = 14;
	public float itemHeightInDp = 14;

	// Functional Usages
	public String rightFirstText;
	public String rightSecondText;
	public String rightThirdText;
	public String rightForthText;
	public Drawable rightFirstDrawable;
	public Drawable rightSecondDrawable;
	public Drawable rightThirdDrawable;
	public Drawable rightForthDrawable;

	// Swipe Types
	public int itemNum;

	public TrainOrgListViewGesture(ListView listView, TouchCallbacks Callbacks,
			Activity context, int itemNum) {
		ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
		mSlop = vc.getScaledTouchSlop();
		mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
		mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
		mListView = listView;
		activity = context;
		tcallbacks = Callbacks;
		this.itemNum = itemNum;
		GetResourcesValues();

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) { // Invokes OnClick Functionality
				if (!moptionsDisplay) {
					tcallbacks.OnClickListView(temp_position);
				}
			}
		});

	}

	public interface TouchCallbacks { // Callback functions
		void OnClickRename(int position);

		void OnClickListView(int position);

		void LoadDataForScroll(int count);

		void onDeleteItem(ListView listView, int[] reverseSortedPositions);
	}

	private void GetResourcesValues() {
		mAnimationTime = 100;
		rightFirstText = activity.getResources().getString(R.string.follow);
		rightSecondText = activity.getResources().getString(
				R.string.environment);
		rightThirdText = activity.getResources().getString(R.string.location);
		rightForthText = activity.getResources().getString(R.string.consult);
		rightFirstDrawable = activity.getResources().getDrawable(
				R.drawable.icon_follow);
		rightSecondDrawable = activity.getResources().getDrawable(
				R.drawable.icon_environment);
		rightThirdDrawable = activity.getResources().getDrawable(
				R.drawable.icon_locate);
		rightForthDrawable = activity.getResources().getDrawable(
				R.drawable.icon_consult);
	}

	public void setEnabled(boolean enabled) {
		mPaused = !enabled;
	}

	public GestureScroll makeScrollListener() {
		return new GestureScroll();
	}

	class GestureScroll implements AbsListView.OnScrollListener {
		// Scroll Usages
		private int visibleThreshold = 4;
		private int currentPage = 0;
		private int previousTotal = 0;
		private boolean loading = true;
		private int previousFirstVisibleItem = 0;
		private long previousEventTime = 0, currTime, timeToScrollOneElement;
		private double speed = 0;

		@Override
		public void onScrollStateChanged(AbsListView absListView,
				int scrollState) {
			setEnabled(scrollState != AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (loading) {
				if (totalItemCount > previousTotal) {
					loading = false;
					previousTotal = totalItemCount;
					currentPage++;
				}
			}

			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				tcallbacks.LoadDataForScroll(totalItemCount);
				loading = true;
			}

			if (previousFirstVisibleItem != firstVisibleItem) {
				currTime = System.currentTimeMillis();
				timeToScrollOneElement = currTime - previousEventTime;
				speed = ((double) 1 / timeToScrollOneElement) * 1000;

				previousFirstVisibleItem = firstVisibleItem;
				previousEventTime = currTime;

			}

		}

		public double getSpeed() {
			return speed;
		}
	}

	@SuppressLint({ "ResourceAsColor", "ClickableViewAccessibility" })
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public boolean onTouch(final View view, MotionEvent event) {
		if (mViewWidth < 2) {
			mViewWidth = mListView.getWidth();
			smallWidth = mViewWidth / 7;
			textwidth2 = mViewWidth / 5;
			textwidth = textwidth2;
			largewidth = textwidth + textwidth2;
			// largewidth = textwidth * itemNum;
		}

		int tempwidth = 0;
		if (itemNum == 1)
			tempwidth = smallWidth;
		else
			tempwidth = textwidth2;

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN: {
			if (mPaused) {
				return false;
			}
			Rect rect = new Rect();
			int childCount = mListView.getChildCount();
			int[] listViewCoords = new int[2];
			mListView.getLocationOnScreen(listViewCoords);
			int x = (int) event.getRawX() - listViewCoords[0];
			int y = (int) event.getRawY() - listViewCoords[1];
			ViewGroup child;
			for (int i = 0; i < childCount; i++) {
				child = (ViewGroup) mListView.getChildAt(i);
				child.getHitRect(rect);
				if (rect.contains(x, y)) {
					mDownView_parent = child;
					mDownView = (ViewGroup) child
							.findViewById(R.id.list_item_view_container);
					if (mDownView_parent.getChildCount() == 1) {
						textheight = mDownView_parent.getHeight();
						SetBackGroundforList();
					}

					if (old_mDownView != null && mDownView != old_mDownView) {
						// ResetListItem(old_mDownView);
						old_mDownView = null;
						return false;
					}
					break;
				}
			}

			if (mDownView != null) {
				mDownX = event.getRawX();
				mDownPosition = mListView.getPositionForView(mDownView);
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			} else {
				mDownView = null;
			}

			// mSwipeDetected = false;
			temp_position = mListView.pointToPosition((int) event.getX(),
					(int) event.getY());
			view.onTouchEvent(event);
			return true;
		}

		case MotionEvent.ACTION_UP: {
			if (mVelocityTracker == null) {
				break;
			}
			float deltaX = event.getRawX() - mDownX;
			mVelocityTracker.addMovement(event);
			mVelocityTracker.computeCurrentVelocity(500); // 1000 by defaut but
			float velocityX = mVelocityTracker.getXVelocity(); // it was too
																// much
			float absVelocityX = Math.abs(velocityX);
			float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
			boolean swipe = false;
			boolean swipeRight = false;

			if (Math.abs(deltaX) > tempwidth) {
				swipe = true;
				swipeRight = deltaX > 0;

			} else if (mMinFlingVelocity <= absVelocityX
					&& absVelocityX <= mMaxFlingVelocity
					&& absVelocityY < absVelocityX) {
				// dismiss only if flinging in the same direction as dragging
				swipe = (velocityX < 0) == (deltaX < 0);
				swipeRight = mVelocityTracker.getXVelocity() > 0;
			}

			if (deltaX < 0 && swipe) {
				mListView.setDrawSelectorOnTop(false);

				if (swipe && !swipeRight && deltaX <= -tempwidth) {
					FullSwipeTrigger();
				} else if (deltaX >= -textwidth && itemNum == 2) {
					ResetListItem(mDownView);
				} else {
					ResetListItem(mDownView);
				}
			} else {
				ResetListItem(mDownView);
			}

			mVelocityTracker.recycle();
			mVelocityTracker = null;
			mDownX = 0;
			mDownView = null;
			mDownPosition = ListView.INVALID_POSITION;
			mSwiping = false;
			break;
		}

		case MotionEvent.ACTION_MOVE: {

			float deltaX = event.getRawX() - mDownX;
			if (mVelocityTracker == null || mPaused || deltaX > 0) {
				break;
			}

			mVelocityTracker.addMovement(event);

			if (Math.abs(deltaX) > mSlop) {
				mSwiping = true;
				mListView.requestDisallowInterceptTouchEvent(true);

				// Cancel ListView's touch (un-highlighting the item)
				MotionEvent cancelEvent = MotionEvent.obtain(event);
				cancelEvent
						.setAction(MotionEvent.ACTION_CANCEL
								| (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
				mListView.onTouchEvent(cancelEvent);
				cancelEvent.recycle();
			}
			if (mSwiping && deltaX < 0) {

				int width;
				if (itemNum == 1) {
					width = textwidth;
				} else {
					width = largewidth;
				}

				if (-deltaX < width) {
					mDownView.setTranslationX(deltaX);
					return false;
				}
				return false;
			} else if (mSwiping) {
				ResetListItem(mDownView);
			}
			break;
		}
		}
		return false;
	}

	private void SetBackGroundforList() {
		mDownView_parent_txt1 = new TextView(activity.getApplicationContext());
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mDownView_parent_txt1.setId(BUTTON_FOLLOW);
		mDownView_parent_txt1.setLayoutParams(lp1);
		mDownView_parent_txt1.setGravity(Gravity.CENTER_HORIZONTAL);
		mDownView_parent_txt1.setText(rightFirstText);
		mDownView_parent_txt1.setWidth(textwidth2);
		mDownView_parent_txt1.setTextSize(textSizeInSp);
		mDownView_parent_txt1.setPadding(0, textheight / 5, 0, 0);
		mDownView_parent_txt1.setHeight(textheight);
		mDownView_parent_txt1.setBackgroundColor(Color.parseColor(normalColor));
		mDownView_parent_txt1.setTextColor(Color.parseColor(TextColor));
		mDownView_parent_txt1.setCompoundDrawablesWithIntrinsicBounds(null,
				rightFirstDrawable, null, null);
		mDownView_parent.addView(mDownView_parent_txt1, 0);

		mDownView_parent_txt2 = new TextView(activity.getApplicationContext());
		mDownView_parent_txt2.setId(BUTTON_IMAGE);
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.LEFT_OF, mDownView_parent_txt1.getId());
		mDownView_parent_txt2.setLayoutParams(lp2);
		mDownView_parent_txt2.setGravity(Gravity.CENTER_HORIZONTAL);
		mDownView_parent_txt2.setText(rightSecondText);
		mDownView_parent_txt2.setWidth(textwidth2);
		mDownView_parent_txt2.setTextSize(textSizeInSp);
		mDownView_parent_txt2.setPadding(0, textheight / 5, 0, 0);
		mDownView_parent_txt2.setHeight(textheight);
		mDownView_parent_txt2.setBackgroundColor(Color.parseColor(normalColor));
		mDownView_parent_txt2.setTextColor(Color.parseColor(TextColor));
		mDownView_parent_txt2.setCompoundDrawablesWithIntrinsicBounds(null,
				rightSecondDrawable, null, null);
		mDownView_parent.addView(mDownView_parent_txt2, 0);

		mDownView_parent_txt3 = new TextView(activity.getApplicationContext());
		mDownView_parent_txt3.setId(BUTTON_IMAGE);
		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp3.addRule(RelativeLayout.LEFT_OF, mDownView_parent_txt2.getId());
		mDownView_parent_txt3.setLayoutParams(lp3);
		mDownView_parent_txt3.setGravity(Gravity.CENTER_HORIZONTAL);
		mDownView_parent_txt3.setText(rightThirdText);
		mDownView_parent_txt3.setWidth(textwidth2);
		mDownView_parent_txt3.setTextSize(textSizeInSp);
		mDownView_parent_txt3.setPadding(0, textheight / 5, 0, 0);
		mDownView_parent_txt3.setHeight(textheight);
		mDownView_parent_txt3.setBackgroundColor(Color.parseColor(normalColor));
		mDownView_parent_txt3.setTextColor(Color.parseColor(TextColor));
		mDownView_parent_txt3.setCompoundDrawablesWithIntrinsicBounds(null,
				rightThirdDrawable, null, null);
		mDownView_parent.addView(mDownView_parent_txt3, 0);

		mDownView_parent_txt4 = new TextView(activity.getApplicationContext());
		mDownView_parent_txt4.setId(BUTTON_IMAGE);
		RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp4.addRule(RelativeLayout.LEFT_OF, mDownView_parent_txt3.getId());
		mDownView_parent_txt4.setLayoutParams(lp4);
		mDownView_parent_txt4.setGravity(Gravity.CENTER_HORIZONTAL);
		mDownView_parent_txt4.setText(rightForthText);
		mDownView_parent_txt4.setWidth(textwidth2);
		mDownView_parent_txt4.setTextSize(textSizeInSp);
		mDownView_parent_txt4.setPadding(0, textheight / 5, 0, 0);
		mDownView_parent_txt4.setHeight(textheight);
		mDownView_parent_txt4.setBackgroundColor(Color.parseColor(normalColor));
		mDownView_parent_txt4.setTextColor(Color.parseColor(TextColor));
		mDownView_parent_txt4.setCompoundDrawablesWithIntrinsicBounds(null,
				rightForthDrawable, null, null);
		mDownView_parent.addView(mDownView_parent_txt4, 0);
	}

	private void ResetListItem(View tempView) {
		Log.d("Shortlist reset call", "Works");
		tempView.animate().translationX(0).alpha(1f)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						int count = mDownView_parent.getChildCount() - 1;
						for (int i = 0; i < count; i++) {
							View V = mDownView_parent.getChildAt(i);
							Log.d("removing child class", "" + V.getClass()
									+ "," + V.getId());
							mDownView_parent.removeViewAt(0);
						}
						moptionsDisplay = false;

					}
				});
		stagged_position = -1;
		opened_position = -1;

	}

	private void FullSwipeTrigger() {
		// Log.d("FUll Swipe trigger call", "Works**********************"
		// + mDismissAnimationRefCount);
		old_mDownView = mDownView;
		int width;
		if (itemNum == 1) {
			width = textwidth;
			// ++mDismissAnimationRefCount;
		} else {
			// ++mDismissAnimationRefCount;
			width = largewidth;
		}
		mDownView.animate().translationX(-width).setDuration(300)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						moptionsDisplay = true;
						stagged_position = temp_position;
						mDownView_parent_txt1
								.setOnTouchListener(new touchClass());
						mDownView_parent_txt2
								.setOnTouchListener(new touchClass());
						mDownView_parent_txt3
								.setOnTouchListener(new touchClass());
						mDownView_parent_txt4
								.setOnTouchListener(new touchClass());

					}

					@Override
					public void onAnimationStart(Animator animation) {
						super.onAnimationStart(animation);
					}
				});
	}

	class PendingDismissData implements Comparable<PendingDismissData> {
		public int position;
		public View view;

		public PendingDismissData(int position, View view) {
			this.position = position;
			this.view = view;
		}

		@Override
		public int compareTo(PendingDismissData other) {
			// Sort by descending position
			return other.position - position;
		}
	}

	class touchClass implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			opened_position = mListView
					.getPositionForView((View) v.getParent());
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN: {
				if (opened_position == stagged_position && moptionsDisplay) {
					switch (v.getId()) {
					case BUTTON_FOLLOW:
						moptionsDisplay = false;
						return true;
					case BUTTON_IMAGE:
						tcallbacks.OnClickRename(temp_position);
						return true;
					}
				}
			}
				return false;
			}

			return false;
		}

	}

}
