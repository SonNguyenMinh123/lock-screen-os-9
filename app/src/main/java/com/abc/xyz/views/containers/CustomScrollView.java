package com.abc.xyz.views.containers;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.abc.xyz.adapters.LockerViewPagerAdapter;

import java.util.ArrayList;


/**
 * Created by DucNguyen on 8/5/2015.
 */
public class CustomScrollView extends HorizontalScrollView {
    private final ArrayList<ItemInfo> mItems = new ArrayList<ItemInfo>();
    private final ItemInfo mTempItem = new ItemInfo();
    private boolean mEnabled = true;
    private LockerViewPagerAdapter mAdapter;
    private OnScrollChangeListener mOnScrollChangeListener;
    /**
     * Position of the last motion event.
     */
    private float mLastMotionX;
    private float mLastMotionY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mActivePointerId = -1;
    private int mPageMargin;
    private int mTouchSlop;
    private int currentPage;
    private float positionOffset = 0;
    private LinearLayout mLinearLayout;
    private Context mContext;
    private long lastScrollUpdate = -1;
    private boolean onTouch = false;
    private boolean isAutoTouch = false;
    private int count = 0;
    private CountDownTimer countDownTimer;
    /*   @Override
       public void fling(int velocityY) {
           int topVelocityY = (int) ((Math.min(Math.abs(velocityY), MAX_SCROLL_SPEED)) * Math.signum(velocityY));
           super.fling(topVelocityY);
       }*/
    private int widthSize;

    public CustomScrollView(Context context) {
        super(context);
    }


    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mLinearLayout = new LinearLayout(mContext);
    }


    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setPagingEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        if (!isAutoTouch) {
        if (lastScrollUpdate == -1) {
            postDelayed(new ScrollStateHandler(), 20);
        }
        lastScrollUpdate = System.currentTimeMillis();
//        }
        onScrollStart();
    }

    private void onScrollStart() {
        if (this.getScrollX() == 0) {
            currentPage = 0;
        } else if (this.getScrollX() == (widthSize)) {
            currentPage = 1;
        }
        positionOffset = (float) this.getScrollX() / (widthSize);
        if (positionOffset < 0) positionOffset = 0;
        if (positionOffset > 1) positionOffset = 1;

        mOnScrollChangeListener.onPageScrolled(currentPage, positionOffset, this.getScrollX());
    }

    private void onScrollEnd() {
        // do something
        if (CustomScrollView.this.getScrollX() > widthSize / 2) {
            setCurrentSmoothItem(1);
        } else {
            setCurrentSmoothItem(0);
        }
    }


    /**
     * @return Info about the page at the current scroll position.
     * This can be synthetic for a missing middle page; the 'object' field can be null.
     */
    private ItemInfo infoForCurrentScrollPosition() {
        final int width = getClientWidth();
        final float scrollOffset = width > 0 ? (float) getScrollX() / width : 0;
        final float marginOffset = width > 0 ? (float) mPageMargin / width : 0;
        int lastPos = -1;
        float lastOffset = 0.f;
        float lastWidth = 0.f;
        boolean first = true;

        ItemInfo lastItem = null;
        for (int i = 0; i < mItems.size(); i++) {
            ItemInfo ii = mItems.get(i);
            float offset;
            if (!first && ii.position != lastPos + 1) {
                // Create a synthetic item for a missing page.
                ii = mTempItem;
                ii.offset = lastOffset + lastWidth + marginOffset;
                ii.position = lastPos + 1;
                ii.widthFactor = mAdapter.getPageWidth(ii.position);
                i--;
            }
            offset = ii.offset;

            final float leftBound = offset;
            final float rightBound = offset + ii.widthFactor + marginOffset;
            if (first || scrollOffset >= leftBound) {
                if (scrollOffset < rightBound || i == mItems.size() - 1) {
                    return ii;
                }
            } else {
                return lastItem;
            }
            first = false;
            lastPos = ii.position;
            lastOffset = offset;
            lastWidth = ii.widthFactor;
            lastItem = ii;
        }

        return lastItem;
    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException("widthMode.UNSPECIFIED");
        }
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            throw new IllegalArgumentException("heightMode.UNSPECIFIED");
        }
        if (getChildCount() > 0) {

            widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            final View child = getChildAt(0);
//            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            ((LinearLayout) child).setOrientation(LinearLayout.HORIZONTAL);
            child.measure(MeasureSpec.makeMeasureSpec(widthSize * 2, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
            //Set the current view size
            for (int i = 0; i < 2; i++) {
                View childOfChild = ((LinearLayout) child).getChildAt(i);
                childOfChild.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
            }
            setMeasuredDimension((int) (widthSize), (int) (heightSize));

        }
    /*    if (getChildCount() > 0) {
            final View child = getChildAt(0);
            int width = getMeasuredWidth();
            if (child.getMeasuredWidth() < width) {
                final FrameLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, mPaddingTop
                        + mPaddingBottom, lp.height);
                width -= mPaddingLeft;
                width -= mPaddingRight;
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }*/
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mEnabled) {
            if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                onTouch = true;
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                onTouch = false;
            }
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void setCurrentSmoothItem(int value) {
        if (value <= 0) {
            currentPage = 0;
  /*          ValueAnimator realSmoothScrollAnimation =
                    ValueAnimator.ofInt(this.getScrollX(), 0);
            realSmoothScrollAnimation.setDuration(200);
            realSmoothScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int scrollTo = (Integer) animation.getAnimatedValue();
                    if (scrollTo == 0) isAutoTouch = false;
                    else {
                        isAutoTouch = true;
                        CustomScrollView.this.scrollTo(scrollTo, 0);
                    }
                }
            });
            realSmoothScrollAnimation.start();
*/
            CustomScrollView.this.smoothScrollTo(0, 0);
        } else if (value >= 1) {
            currentPage = 1;
     /*       ValueAnimator realSmoothScrollAnimation =
                    ValueAnimator.ofInt(this.getScrollX(), CustomScrollView.this.(widthSize ));
            realSmoothScrollAnimation.setDuration(200);
            realSmoothScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int scrollTo = (Integer) animation.getAnimatedValue();
                    if (scrollTo == CustomScrollView.this.(widthSize )) isAutoTouch = false;
                    else {
                        isAutoTouch = true;
                    }
                    CustomScrollView.this.scrollTo(scrollTo, 0);

                }
            });
            realSmoothScrollAnimation.start();*/
            CustomScrollView.this.smoothScrollTo((widthSize), 0);
        }
    }

    public void setCurrentItem(int value) {
        if (value <= 0) {
            currentPage = 0;
            this.post(new Runnable() {
                @Override
                public void run() {
                    CustomScrollView.this.scrollTo(0, 0);
                }
            });
        } else if (value >= 1) {
            currentPage = 1;
            this.post(new Runnable() {
                @Override
                public void run() {
                    CustomScrollView.this.scrollTo((widthSize), 0);
                }
            });
        }
    }

    public void setAdapter(LockerViewPagerAdapter adapter) {
        mAdapter = adapter;
//        mAdapter.startUpdate(this);
        float widthPage = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.position = i;
            itemInfo.object = mAdapter.instantiateItem(mLinearLayout, i);
            itemInfo.widthFactor = mAdapter.getPageWidth(i);
            widthPage += itemInfo.widthFactor;
            mItems.add(itemInfo);
//            mLinearLayout.addView((RelativeLayout) itemInfo.object);
//            mAdapter.destroyItem(mLinearLayout, itemInfo.position, itemInfo.object);
        }
        this.addView(mLinearLayout);
    }

    public void setOnPageChangeListener(OnScrollChangeListener onPageChangeListener) {
        this.mOnScrollChangeListener = onPageChangeListener;
    }

    public interface OnScrollChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    static class ItemInfo {
        Object object;
        int position;
        boolean scrolling;
        float widthFactor;
        float offset;
    }

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 20 && !onTouch) {
                lastScrollUpdate = -1;
                if (!(getScrollX() <= 0) && !(getScrollX() >= (widthSize)))
                    onScrollEnd();
            } else {
                postDelayed(this, 20);
            }
        }
    }
}


