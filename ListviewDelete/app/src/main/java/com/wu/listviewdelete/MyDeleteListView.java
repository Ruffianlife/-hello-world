package com.wu.listviewdelete;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
public class MyDeleteListView extends ListView {

	private LayoutInflater inflater;

	public MyDeleteListView(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		ViewConfiguration vc = ViewConfiguration.get(context);
		//滑动事件设置类
		mSlop = vc.getScaledTouchSlop();
	}

	public MyDeleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyDeleteListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			handleActionDown(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			return handleActionMove(ev);
		case MotionEvent.ACTION_UP:
			handleActionUp(ev);
			break;
		}
		return super.onTouchEvent(ev);
	}

	private float mDownX;
	private float mDownY;

	/**
	 * 认为是用户滑动的�?��距离
	 */
	private int mSlop;

	private int mDownPosition;

	private OnDeleteCallback onDeleteCallback;

	/**
	 * 用来标记用户是否正在滑动�?
	 */
	private boolean mSwiping;

	/**
	 * 按下的item对应的View
	 */
	private View mDownView;
	/**
	 * 滑动速度�?���?
	 */
	private VelocityTracker mVelocityTracker;
	/**
	 * item的宽�?
	 */
	private int mViewWidth;
	/**
	 * item的高�?
	 */
	private int mViewHeight;

	/**
	 * 按下事件处理
	 * 
	 * @param ev
	 * @return
	 */
	private void handleActionDown(MotionEvent ev) {
		mDownX = ev.getX();
		mDownY = ev.getY();

		mDownPosition = pointToPosition((int) mDownX, (int) mDownY);

		if (mDownPosition == AdapterView.INVALID_POSITION) {
			return;
		}
		mDownView = getChildAt(mDownPosition - getFirstVisiblePosition());
		if (mDownView != null) {
			mViewWidth = mDownView.getWidth();
			mViewHeight = mDownView.getHeight();
		}

		mVelocityTracker = VelocityTracker.obtain();
		mVelocityTracker.addMovement(ev);
	}

	private PopupWindow popupWindow;

	/**
	 * 手指抬起的事件处�?
	 * 
	 * @param ev
	 */
	private void handleActionUp(MotionEvent ev) {
		if (mVelocityTracker == null || mDownView == null || !mSwiping) {
			return;
		}
		if (ev.getX() < mDownX
				&& Math.abs(ev.getX() - mDownX) > Math.abs(ev.getY() - mDownY)) {// x轴左方向滑动
			showDeletePopup();
		}
		mSwiping = false;
	}

	private void showDeletePopup() {
		View deleteView = inflater.inflate(R.layout.layout_popupwindow, null);
		deleteView.findViewById(R.id.popupwindow_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
						if (onDeleteCallback != null) {
							onDeleteCallback.onDelete(MyDeleteListView.this,
									mDownPosition);
						}
					}
				});

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		deleteView.measure(w, h);
		int height = deleteView.getMeasuredHeight();
		int width = deleteView.getMeasuredWidth();

		popupWindow = new PopupWindow(deleteView, LayoutParams.WRAP_CONTENT,
				android.view.WindowManager.LayoutParams.WRAP_CONTENT, true);


		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLUE));// 设置背景颜色



		popupWindow.setAnimationStyle(R.style.AnimationPreview);// 动画
		popupWindow.showAsDropDown(mDownView, mViewWidth - width, -(mViewHeight - (mViewHeight - height) / 2));
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
	}

	/**
	 * 处理手指滑动的方�?
	 * 
	 * @param ev
	 * @return
	 */
	private boolean handleActionMove(MotionEvent ev) {
		if (mVelocityTracker == null || mDownView == null) {
			return super.onTouchEvent(ev);
		}

		float deltaX = ev.getX() - mDownX;
		float deltaY = ev.getY() - mDownY;

		// X方向滑动的距离大于mSlop并且Y方向滑动的距离小于mSlop，表示可以滑�?
		if (Math.abs(deltaX) > mSlop && Math.abs(deltaY) < mSlop) {
			mSwiping = true;

			// 当手指滑动item,取消item的点击事件，不然我们滑动Item也伴随着item点击事件的发�?
			MotionEvent cancelEvent = MotionEvent.obtain(ev);
			cancelEvent
					.setAction(MotionEvent.ACTION_CANCEL
							| (ev.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
			onTouchEvent(cancelEvent);
		}

		if (mSwiping) {

			return true;
		}

		return super.onTouchEvent(ev);

	}

	public void setOnItemDeleteCallback(OnDeleteCallback onDeleteCallback) {
		this.onDeleteCallback = onDeleteCallback;
	}

	/**
	 * 删除条目的回调
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface OnDeleteCallback {
		public void onDelete(AdapterView<?> parent, int deletePosition);
	}
}
