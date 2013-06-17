package com.threeH.MyExhibition.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.threeH.MyExhibition.R;
import com.threeH.MyExhibition.cache.XmlDB;
import com.threeH.MyExhibition.tools.PixelDpHelper;
import com.threeH.MyExhibition.tools.TimeHelper;
import com.threeH.MyExhibition.ui.MyApplication;

public class PullToRefreshView extends ListView implements OnScrollListener,
		OnClickListener {

	private Context mCon;
	private LayoutInflater mInflater;
	private MyApplication mApp;

	public PullToRefreshView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private final int RELEASE_To_REFRESH = 0;

	/** 下拉刷新. */
	private final int PULL_To_REFRESH = 1;

	private final int REFRESHING = 2;
	/** 刷新完毕状态 */
	private final int DONE = 3;

	// 实际的padding的距离与界面上偏移距离的比例
	private final int RATIO = 1;

	/** ListView顶部的HeaderView. */
	private LinearLayout mHeadView;
	/** ListView刷新状态显示，比如:正在刷新...;放手吧; */
	private TextView mListStatus;
	/** ListView上次刷新时间显示，比如:最近更新:06-01 12:00 */
	private TextView mListUpdateTime;
	/** ListView变换箭头. */
	private ImageView mListHeadArrow;
	/** 顶部Loading的View. */
	private ImageView mListHeadLoading;
	/** 顶部AnimationDrawable. */
	private AnimationDrawable mHeaderAnim;
	/** ListView顶部高度. */
	private int mHeadHeight;

	/** ListView底部的FooterView. */
	public RelativeLayout mFootView;
	/** 底部Footer布局的Layout */
	private RelativeLayout mFootBgLayout;
	/** 底部Loading的View. */
	private ImageView mListFootLoading;
	/** 底部AnimationDrawable. */
	private AnimationDrawable mFooterAnim;
	// /** 当ListView为空时显示的View. */
	// private View emptyFootView;

	/** ListView滑动状态. */
	private int mListState;
	/** 是否显示回到顶端的标示位. */
	private boolean isShowBackToTop = false;
	private final int HIDEBACKTOTOP = 0;

	/** 目前ListView的状态(正在刷新中....;刷新完毕;) */
	private int mRefreshState;
	private OnRefreshListener mOnRefreshListener;
	/** 由松开进行刷新状态变为下拉进行刷新状态. */
	private boolean isBack;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	/** 用于保证startY的值在一个完整的touch事件中只被记录一次. */
	private boolean isRecored;
	private int startY;
	private int mFirstVisibleItem;

	private boolean isRefreshable;
	private boolean mBanRefresh = true;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDEBACKTOTOP:
				if (!isShowBackToTop) {
					if (mBackToTopView != null) {
						mBackToTopView.setVisibility(View.GONE);
					}
				}
				break;
			}
		}
	};

	private void init(Context context) {
		this.mCon = context;
		/** 防止滑动变黑. */
		setCacheColorHint(mCon.getResources().getColor(R.color.transparent));
		mInflater = LayoutInflater.from(mCon);
		mApp = (MyApplication) mCon.getApplicationContext();

		mHeadView = (LinearLayout) mInflater
				.inflate(R.layout.list_header, null);

		mListHeadArrow = (ImageView) mHeadView
				.findViewById(R.id.mListHeadArrow);
		mListHeadArrow.setMinimumHeight(50);
		mListHeadLoading = (ImageView) mHeadView
				.findViewById(R.id.mListHeadLoading);
		mHeaderAnim = (AnimationDrawable) mListHeadLoading.getDrawable();
		mListStatus = (TextView) mHeadView.findViewById(R.id.mListStatus);
		mListUpdateTime = (TextView) mHeadView
				.findViewById(R.id.mListUpdateTime);

		measureView(mHeadView);
		mHeadHeight = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -1 * mHeadHeight, 0, 0);
		mHeadView.invalidate();
		addHeaderView(mHeadView, null, false);

		/** 底部View. */
		mFootView = (RelativeLayout) mInflater.inflate(R.layout.list_footer,
				null);
		mFootView.setOnClickListener(this);
		mFootBgLayout = (RelativeLayout) mFootView
				.findViewById(R.id.mFootBgLayout);
		mListFootLoading = (ImageView) mFootView
				.findViewById(R.id.mListFootLoading);
		mFooterAnim = (AnimationDrawable) mListFootLoading.getDrawable();
		mRefreshFooterText = (TextView) mFootView
				.findViewById(R.id.refresh_tv_message);

		// emptyFootView = mInflater.inflate(R.layout.empty_foot, null);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(200);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		mRefreshState = DONE;
		isRefreshable = false;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		mListState = scrollState;
	}

	public void onScroll(AbsListView arg0, int firstVisibleItem, int arg2,
			int arg3) {
		mFirstVisibleItem = firstVisibleItem;
		switch (mListState) {
		case SCROLL_STATE_FLING:
			if (mFirstVisibleItem == 0) {
				isShowBackToTop = false;
				mHandler.sendEmptyMessageDelayed(HIDEBACKTOTOP, 1000);
			} else {
				isShowBackToTop = true;
			}
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			if (mBackToTopView != null) {
				mBackToTopView.setVisibility(View.VISIBLE);
				isShowBackToTop = true;
			}
			break;
		case SCROLL_STATE_IDLE:
			isShowBackToTop = false;
			mHandler.sendEmptyMessageDelayed(HIDEBACKTOTOP, 3000);
			break;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable && mBanRefresh) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Trace.e("action down called");
				if (mFirstVisibleItem == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				// Trace.e("action cancel called");
				if (mRefreshState != REFRESHING) {
					if (mRefreshState == RELEASE_To_REFRESH) {
						mRefreshState = REFRESHING;
						changeHeadStatus();
						onRefresh(HEADER);
					} else {
						mRefreshState = DONE;
						changeHeadStatus();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_UP:
				// Trace.e("action up called");
				if (mRefreshState != REFRESHING) {
					if (mRefreshState == RELEASE_To_REFRESH) {
						mRefreshState = REFRESHING;
						changeHeadStatus();
						onRefresh(HEADER);
					} else {
						mRefreshState = DONE;
						changeHeadStatus();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				// Trace.d("action move called");
				int tempY = (int) event.getY();
				if (!isRecored && mFirstVisibleItem == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (mRefreshState != REFRESHING && isRecored) {
					// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					if (mRefreshState == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO) < mHeadHeight)
								&& (tempY - startY) > 0) {
							/** 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步 */
							mRefreshState = PULL_To_REFRESH;
							changeHeadStatus();
						} else if (tempY - startY <= 0) {
							mRefreshState = DONE;
							changeHeadStatus();
						}
					}
					if (mRefreshState == PULL_To_REFRESH) {
						/** 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态 */
						setSelection(0);
						if ((tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO) >= mHeadHeight) {
							/** 下拉到可以进入RELEASE_TO_REFRESH的状态 */
							mRefreshState = RELEASE_To_REFRESH;
							isBack = true;
							changeHeadStatus();
						} else if (tempY - startY <= 0) {
							/** 上推到顶了 */
							mRefreshState = DONE;
							changeHeadStatus();
						}
					}
					if (mRefreshState == DONE) {
						if (tempY - startY > 0) {
							mRefreshState = PULL_To_REFRESH;
							changeHeadStatus();
						}
					}
					if (mRefreshState == PULL_To_REFRESH) {
						mHeadView.setPadding(
								0,
								-1
										* mHeadHeight
										+ (tempY - startY)
										/ PixelDpHelper.dip2px(getContext(),
												RATIO), 0, 0);
					}
					if (mRefreshState == RELEASE_To_REFRESH) {
						mHeadView.setPadding(0, (tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO)
								- mHeadHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeadStatus() {
		switch (mRefreshState) {
		case RELEASE_To_REFRESH:
			/** 当前状态，松开刷新 */
			/** 播放松开刷新的提示音 */
//			mApp.playSound(R.raw.xiala);
			mListHeadArrow.setVisibility(View.VISIBLE);
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListStatus.setVisibility(View.VISIBLE);
			mListUpdateTime.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.startAnimation(animation);
			mListStatus.setText("放手吧");
			break;
		case PULL_To_REFRESH:
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListStatus.setVisibility(View.VISIBLE);
			mListUpdateTime.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setVisibility(View.VISIBLE);
			if (isBack) {
				/** 是由RELEASE_To_REFRESH状态转变来的 */
				isBack = false;
				mListHeadArrow.clearAnimation();
				mListHeadArrow.startAnimation(reverseAnimation);
				mListStatus.setText("下拉刷新");
//				mApp.playSound(R.raw.pushup);
			} else {
				mListStatus.setText("下拉刷新");
			}
			break;
		case REFRESHING:
			/** 当前状态,正在刷新... */
			mHeadView.setPadding(0, 0, 0, 0);
			mListHeadLoading.setVisibility(View.VISIBLE);
			mHeaderAnim.start();
			mListHeadArrow.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setVisibility(View.GONE);
			mListStatus.setText("正在刷新...");
			mListUpdateTime.setVisibility(View.VISIBLE);
			break;
		case DONE:
			smoothScrollTo(mHeadView.getPaddingTop(), -1 * mHeadHeight,
					-mHeadHeight);
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListHeadArrow.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setImageResource(R.drawable.ic_refresh_droparrow);
			mListStatus.setText("下拉刷新");
			mListUpdateTime.setVisibility(View.VISIBLE);
			break;
		}
	}

	private SmoothScrollRunnable mSmoothScrollRunnable;
	private final Handler handler = new Handler();

	protected final void smoothScrollTo(int mFromY, int mToY, int mEndPos) {
		if (null != mSmoothScrollRunnable) {
			mSmoothScrollRunnable.stop();
		}

		if (this.getScrollY() != mToY) {
			this.mSmoothScrollRunnable = new SmoothScrollRunnable(handler,
					mFromY, mToY, mEndPos);
			handler.post(mSmoothScrollRunnable);
		}
	}

	final class SmoothScrollRunnable implements Runnable {

		static final int ANIMATION_DURATION_MS = 250;
		static final int ANIMATION_FPS = 1000 / 100;

		private final Interpolator interpolator;
		private final int scrollToY;
		private final int scrollFromY;
		private final int mEndPos;
		private final Handler handler;

		private boolean continueRunning = true;
		private long startTime = -1;
		private int currentY = -1;

		public SmoothScrollRunnable(Handler handler, int fromY, int toY,
				int mEndPos) {
			this.handler = handler;
			this.scrollFromY = fromY;
			this.scrollToY = toY;
			this.mEndPos = mEndPos;
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {
			/**
			 * Only set startTime if this is the first time we're starting, else
			 * actually calculate the Y delta
			 */
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {
				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - startTime))
						/ ANIMATION_DURATION_MS;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math
						.round((scrollFromY - scrollToY)
								* interpolator
										.getInterpolation(normalizedTime / 1000f));
				this.currentY = scrollFromY - deltaY;
				setHeaderScroll(currentY);
			}

			// If we're not at the target Y, keep going...
			if (continueRunning && scrollToY != currentY) {
				handler.postDelayed(this, ANIMATION_FPS);
			} else {
				mHeadView.setPadding(0, mEndPos, 0, 0);
			}
		}

		public void stop() {
			this.continueRunning = false;
			this.handler.removeCallbacks(this);
		}
	};

	protected final void setHeaderScroll(int y) {
		mHeadView.setPadding(0, y, 0, 0);
	}

	/** ListView数据刷新或加载更多时回调的注册函数. */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.mOnRefreshListener = onRefreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh(int which);
	}

	public void setRefreshAble(boolean enable) {
		mBanRefresh = enable;
	}

	/**
	 * Resets the list to a normal mRefreshState after a refresh.
	 */
	public void onRefreshComplete(int which) {
		switch (which) {
		case HEADER:
			mRefreshState = DONE;
			initRefreshTime();
			changeHeadStatus();
			invalidateViews();
			break;
		case FOOTER:
			mFootBgLayout.setBackgroundResource(R.drawable.sy01_2x);
			mListFootLoading.setVisibility(View.GONE);
			mFooterAnim.stop();
			mRefreshFooterText.setText("查看更多");
			mRefreshState = DONE;
			changeHeadStatus();
			invalidateViews();
			break;
		}
	}

	public void initRefreshTime() {
		if (this.getTag() == null) {
			mListUpdateTime.setText("最近更新:" + TimeHelper.getCurrentMinute());
			return;
		}
		if ("ChatList".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_ChatList",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue(
							"list_ChatList", TimeHelper.getCurrentMinute()));
		} else if ("Inbox".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_Inbox",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_Inbox",
							TimeHelper.getCurrentMinute()));
		} else if ("OnLine".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_OnLine",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_OnLine",
							TimeHelper.getCurrentMinute()));
		} else if ("Nearby".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_Nearby",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_Nearby",
							TimeHelper.getCurrentMinute()));
		} else if ("dynamic".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_dynamic",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_dynamic",
							TimeHelper.getCurrentMinute()));
		}
	}

	/** ListView刷新时调用的方法(可能由下拉ListView或点击"更多"触发该方法). */
	public void initRefresh(int which) {
		prepareForRefresh(which);
		onRefresh(which);
	}

	public final static int HEADER = 0;
	public final static int FOOTER = 1;

	private void prepareForRefresh(int which) {
		switch (which) {
		case HEADER:
			mRefreshState = REFRESHING;
			changeHeadStatus();
			break;
		case FOOTER:
			mFootBgLayout.setBackgroundDrawable(null);
			mListFootLoading.setVisibility(View.VISIBLE);
			mFooterAnim.start();
			mRefreshFooterText.setText("加载中...");
			mRefreshState = REFRESHING;
			break;
		}
	}

	public void onRefresh(int which) {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh(which);
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		initRefreshTime1();
		super.setAdapter(adapter);
	}

	public void initRefreshTime1() {
		if (this.getTag() == null) {
			mListUpdateTime.setText("最近更新:" + TimeHelper.getCurrentMinute());
			return;
		}
		if ("ChatList".equalsIgnoreCase(this.getTag().toString())) {
			XmlDB.getInstance(mCon).saveKey("list_ChatList",
					TimeHelper.getCurrentMinute());
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue(
							"list_ChatList", TimeHelper.getCurrentMinute()));
		} else if ("Inbox".equalsIgnoreCase(this.getTag().toString())) {
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_Inbox",
							TimeHelper.getCurrentMinute()));
		} else if ("OnLine".equalsIgnoreCase(this.getTag().toString())) {
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_OnLine",
							TimeHelper.getCurrentMinute()));
		} else if ("Nearby".equalsIgnoreCase(this.getTag().toString())) {
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_Nearby",
							TimeHelper.getCurrentMinute()));
		} else if ("dynamic".equalsIgnoreCase(this.getTag().toString())) {
			mListUpdateTime.setText("最近更新:"
					+ XmlDB.getInstance(mCon).getKeyStringValue("list_dynamic",
							TimeHelper.getCurrentMinute()));
		}

	}

	private ImageView mBackToTopView;

	public void setTopViewImage(ImageView mTopViewImage) {
		this.mBackToTopView = mTopViewImage;
		mTopViewImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelection(0);
			}
		});
	}

	private View mTempFootView = null;
	private TextView mRefreshFooterText;

	public void addFooterView() {
		// if (!hasFooter) {
		removeFooterView();
		mTempFootView = mFootView;
		addFooterView(mFootView);
		// hasFooter = true;
		// }
	}

	// public void addEmptyView(int drawableId) {
	// removeFooterView();
	// emptyFootView.findViewById(R.id.empty_imageview).setBackgroundResource(
	// drawableId);
	// mTempFootView = emptyFootView;
	// addFooterView(mTempFootView);
	// }

	public void removeFooterView() {
		if (mTempFootView != null) {
			removeFooterView(mTempFootView);
		}
	}

	@Override
	public void onClick(View arg0) {
		if (mRefreshState == DONE) {
			initRefresh(FOOTER);
		}
	}

}
