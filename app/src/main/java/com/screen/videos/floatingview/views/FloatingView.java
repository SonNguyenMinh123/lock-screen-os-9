package com.screen.videos.floatingview.views;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

import com.screen.videos.floatingview.controler.FloatingViewManager;

import java.lang.ref.WeakReference;

public class FloatingView extends LinearLayout implements ViewTreeObserver.OnPreDrawListener {
    private static final float MOVE_THRESHOLD_DP = 8.0f;
    private static final float SCALE_PRESSED = 0.9f;
    private static final float SCALE_NORMAL = 1.0f;
    private static final long MOVE_TO_EDGE_DURATION = 450L;
    private static final float MOVE_TO_EDGE_OVERSHOOT_TENSION = 1.25f;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_INTERSECTING = 1;
    public static final int STATE_FINISHING = 2;
    public static final int LONG_PRESS_TIMEOUT = (int) (1.5f * ViewConfiguration.getLongPressTimeout());
    public static final int DEFAULT_X = Integer.MIN_VALUE;

    /**
     * デフォルトのY座標を表す値
     */
    public static final int DEFAULT_Y = Integer.MIN_VALUE;

    /**
     * WindowManager
     */
    private final WindowManager mWindowManager;

    /**
     * LayoutParams
     */
    private final WindowManager.LayoutParams mParams;

    /**
     * DisplayMetrics
     */
    private final DisplayMetrics mMetrics;

    /**
     * 押下処理を通過しているかチェックするための時間
     */
    private long mTouchDownTime;

    /**
     * スクリーン押下X座標(移動量判定用)
     */
    private float mScreenTouchDownX;
    /**
     * スクリーン押下Y座標(移動量判定用)
     */
    private float mScreenTouchDownY;
    /**
     * 一度移動を始めたフラグ
     */
    private boolean mIsMoveAccept;

    /**
     * スクリーンのタッチX座標
     */
    private float mScreenTouchX;
    /**
     * スクリーンのタッチY座標
     */
    private float mScreenTouchY;
    /**
     * ローカルのタッチX座標
     */
    private float mLocalTouchX;
    /**
     * ローカルのタッチY座標
     */
    private float mLocalTouchY;
    /**
     * 初期表示のX座標
     */
    private int mInitX;
    /**
     * 初期表示のY座標
     */
    private int mInitY;

    /**
     * 初期表示時にアニメーションするフラグ
     */
    private boolean mAnimateInitialMove;

    /**
     * ステータスバーの高さ
     */
    private final int mStatusBarHeight;

    /**
     * 左・右端に寄せるアニメーション
     */
    private ValueAnimator mMoveEdgeAnimator;

    /**
     * Interpolator
     */
    private final TimeInterpolator mMoveEdgeInterpolator;

    /**
     * 移動限界を表すRect
     */
    private final Rect mMoveLimitRect;

    /**
     * 表示位置（画面端）の限界を表すRect
     */
    private final Rect mPositionLimitRect;

    /**
     * ドラッグ可能フラグ
     */
    private boolean mIsDraggable;

    /**
     * 形を表す係数
     */
    private float mShape;

    /**
     * FloatingViewのアニメーションを行うハンドラ
     */
    private final FloatingAnimationHandler mAnimationHandler;

    /**
     * 長押しを判定するためのハンドラ
     */
    private final LongPressHandler mLongPressHandler;

    /**
     * 画面端をオーバーするマージン
     */
    private int mOverMargin;

    /**
     * OnTouchListener
     */
    private OnTouchListener mOnTouchListener;

    /**
     * 長押し状態の場合
     */
    private boolean mIsLongPressed;

    /**
     * 移動方向
     */
    private int mMoveDirection;

    /**
     * コンストラクタ
     *
     * @param context {@link Context}
     */
    public FloatingView(final Context context) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        mParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        mParams.format = PixelFormat.TRANSLUCENT;
        // 左下の座標を0とする
        mParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mAnimationHandler = new FloatingAnimationHandler(this);
        mLongPressHandler = new LongPressHandler(this);
        mMoveEdgeInterpolator = new OvershootInterpolator(MOVE_TO_EDGE_OVERSHOOT_TENSION);
        mMoveDirection = FloatingViewManager.MOVE_DIRECTION_DEFAULT;

        mMoveLimitRect = new Rect();
        mPositionLimitRect = new Rect();

        // ステータスバーの高さを取得
        final Resources resources = context.getResources();
        final int statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (statusBarHeightId > 0) {
            mStatusBarHeight = resources.getDimensionPixelSize(statusBarHeightId);
        } else {
            mStatusBarHeight = 0;
        }

        getViewTreeObserver().addOnPreDrawListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("test", "onTouchEvent");
        return super.onTouchEvent(event);
    }

    /**
     * 表示位置を決定します。
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateViewLayout();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateViewLayout();
    }


    @Override
    public boolean onPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        if (mInitX == DEFAULT_X) {
            mInitX = 0;
        }
        if (mInitY == DEFAULT_Y) {
            mInitY = mMetrics.heightPixels - mStatusBarHeight - getMeasuredHeight();
        }
        mParams.x = mInitX;
        mParams.y = mInitY;
        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_NONE) {
            moveTo(mInitX, mInitY, mInitX, mInitY, false);
        } else {
            // 初期位置から画面端に移動
            moveToEdge(mInitX, mInitY, mAnimateInitialMove);
        }
        mIsDraggable = true;
        mWindowManager.updateViewLayout(this, mParams);
        return true;
    }

    private void updateViewLayout() {
        cancelAnimation();

        // 前の画面座標を保存
        final int oldScreenHeight = mMetrics.heightPixels;
        final int oldScreenWidth = mMetrics.widthPixels;
        final int oldPositionLimitWidth = mPositionLimitRect.width();
        final int oldPositionLimitHeight = mPositionLimitRect.height();

        // 新しい座標情報に切替
        mWindowManager.getDefaultDisplay().getMetrics(mMetrics);
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int newScreenWidth = mMetrics.widthPixels;
        final int newScreenHeight = mMetrics.heightPixels;

        // 移動範囲の設定
        mMoveLimitRect.set(-width, -height * 2, newScreenWidth + width, newScreenHeight + height);
        mPositionLimitRect.set(-mOverMargin, 0, newScreenWidth - width + mOverMargin, newScreenHeight - mStatusBarHeight - height);

        // 縦横切替の場合
        if (oldScreenWidth != newScreenWidth || oldScreenHeight != newScreenHeight) {
            // 画面端に移動する場合は現在の位置から左右端を設定
            if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_DEFAULT) {
                // 右半分にある場合
                if (mParams.x > (newScreenWidth - width) / 2) {
                    mParams.x = mPositionLimitRect.right;
                }
                // 左半分にある場合
                else {
                    mParams.x = mPositionLimitRect.left;
                }
            }
            // 左端に移動
            else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_LEFT) {
                mParams.x = mPositionLimitRect.left;
            }
            // 右端に移動
            else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_RIGHT) {
                mParams.x = mPositionLimitRect.right;
            }
            // 画面端に移動しない場合は画面座標の比率から計算
            else {
                final int newX = (int) (mParams.x * mPositionLimitRect.width() / (float) oldPositionLimitWidth + 0.5f);
                mParams.x = Math.min(Math.max(mPositionLimitRect.left, newX), mPositionLimitRect.right);
            }

            // スクリーン位置の比率からY座標を設定(四捨五入)
            final int newY = (int) (mParams.y * mPositionLimitRect.height() / (float) oldPositionLimitHeight + 0.5f);
            mParams.y = Math.min(Math.max(mPositionLimitRect.top, newY), mPositionLimitRect.bottom);
            mWindowManager.updateViewLayout(this, mParams);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDetachedFromWindow() {
        if (mMoveEdgeAnimator != null) {
            mMoveEdgeAnimator.removeAllUpdateListeners();
        }
        super.onDetachedFromWindow();
    }

    private GestureDetector gestureDetector;

    /*onToouch cua Floatingview*/
//    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {

        for (int i = 0; i < getChildCount(); i++) {
            Log.e("test", "child : " + getChildAt(i));
        }

        if (getVisibility() != View.VISIBLE) {
            return true;
        }
        if (!mIsDraggable) {
            return true;
        }
        mScreenTouchX = event.getRawX();
        mScreenTouchY = event.getRawY();
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            cancelAnimation();
            mScreenTouchDownX = mScreenTouchX;
            mScreenTouchDownY = mScreenTouchY;
            mLocalTouchX = event.getX();
            mLocalTouchY = event.getY();
            mIsMoveAccept = false;
            setScale(SCALE_PRESSED);
            mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mAnimationHandler.sendAnimationMessage(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            mLongPressHandler.sendEmptyMessageDelayed(LongPressHandler.LONG_PRESSED, LONG_PRESS_TIMEOUT);
            mTouchDownTime = event.getDownTime();
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (mIsMoveAccept) {
                mIsLongPressed = false;
                mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            }
            if (mTouchDownTime != event.getDownTime()) {
                return true;
            }
            final float moveThreshold = MOVE_THRESHOLD_DP * mMetrics.density;
            if (!mIsMoveAccept && Math.abs(mScreenTouchX - mScreenTouchDownX) < moveThreshold && Math.abs(mScreenTouchY - mScreenTouchDownY) < moveThreshold) {
                return true;
            }
            mIsMoveAccept = true;
            mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
        }
        // 押上、キャンセル
        else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            // 判定のため長押しの状態を一時的に保持
            final boolean tmpIsLongPressed = mIsLongPressed;
            // 長押しの解除
            mIsLongPressed = false;
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
            // 押下処理が行われていない場合は処理しない
            if (mTouchDownTime != event.getDownTime()) {
                return true;
            }
            // アニメーションの削除
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            // 拡大率をもとに戻す
            setScale(SCALE_NORMAL);

            // 動かされていれば画面端に戻す
            if (mIsMoveAccept) {
                moveToEdge(true);
            }
            // 動かされていなければ、クリックイベントを発行
            else {
                if (!tmpIsLongPressed) {
                    final int size = getChildCount();
                    for (int i = 0; i < size; i++) {
                        getChildAt(i).performClick();
                    }
                }
            }
        }

        if (mOnTouchListener != null) {
            mOnTouchListener.onTouch(this, event);
        }
//        }
        return true;

    }

    /**
     * 長押しされた場合の処理です。
     */
    private void onClick() {
        Log.e("test", "onLongClick");
        mIsLongPressed = true;
        // 長押し処理
        final int size = getChildCount();
        for (int i = 0; i < size; i++) {
            getChildAt(i).performLongClick();
//            getChildAt(i).performClick();
            Log.e("test", "size : " + size);
        }
    }


    /**
     * 画面から消す際の処理を表します。
     */
    @Override
    public void setVisibility(int visibility) {
        // 画面表示時
        if (visibility != View.VISIBLE) {
            // 画面から消す時は長押しをキャンセルし、画面端に強制的に移動します。
            cancelLongPress();
            setScale(SCALE_NORMAL);
            if (mIsMoveAccept) {
                moveToEdge(false);
            }
            mAnimationHandler.removeMessages(FloatingAnimationHandler.ANIMATION_IN_TOUCH);
            mLongPressHandler.removeMessages(LongPressHandler.LONG_PRESSED);
        }
        super.setVisibility(visibility);
    }


    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        mOnTouchListener = listener;
    }


    private void moveToEdge(boolean withAnimation) {
        final int currentX = getXByTouch();
        final int currentY = getYByTouch();
        moveToEdge(currentX, currentY, withAnimation);
    }

    /**
     * 始点を指定して左右の端に移動します。
     *
     * @param startX        X座標の初期値
     * @param startY        Y座標の初期値
     * @param withAnimation アニメーションを行う場合はtrue.行わない場合はfalse
     */
    private void moveToEdge(int startX, int startY, boolean withAnimation) {
        //TODO:縦軸の速度も考慮して斜めに行くようにする
        // X・Y座標と移動方向を設定
        final int goalPositionX;
        // 画面端に移動する場合は画面端の座標を設定
        if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_DEFAULT) {
            final boolean isMoveRightEdge = startX > (mMetrics.widthPixels - getWidth()) / 2;
            goalPositionX = isMoveRightEdge ? mPositionLimitRect.right : mPositionLimitRect.left;
        }
        // 左端への移動
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_LEFT) {
            goalPositionX = mPositionLimitRect.left;
        }
        // 右端への移動
        else if (mMoveDirection == FloatingViewManager.MOVE_DIRECTION_RIGHT) {
            goalPositionX = mPositionLimitRect.right;
        }
        // 画面端に移動しない場合は、現在の座標のまま
        else {
            goalPositionX = startX;
        }
        // TODO:Y座標もアニメーションさせる
        final int goalPositionY = startY;
        // 指定座標に移動
        moveTo(startX, startY, goalPositionX, goalPositionY, withAnimation);
    }

    /**
     * 指定座標に移動します。<br/>
     * 画面端の座標を超える場合は、自動的に画面端に移動します。
     *
     * @param currentX      現在のX座標（アニメーションの始点用に使用）
     * @param currentY      現在のY座標（アニメーションの始点用に使用）
     * @param goalPositionX 移動先のX座標
     * @param goalPositionY 移動先のY座標
     * @param withAnimation アニメーションを行う場合はtrue.行わない場合はfalse
     */
    private void moveTo(int currentX, int currentY, int goalPositionX, int goalPositionY, boolean withAnimation) {
        // 画面端からはみ出さないように調整
        goalPositionX = Math.min(Math.max(mPositionLimitRect.left, goalPositionX), mPositionLimitRect.right);
        goalPositionY = Math.min(Math.max(mPositionLimitRect.top, goalPositionY), mPositionLimitRect.bottom);
        // アニメーションを行う場合
        if (withAnimation) {
            // TODO:Y座標もアニメーションさせる
            mParams.y = goalPositionY;

            mMoveEdgeAnimator = ValueAnimator.ofInt(currentX, goalPositionX);
            mMoveEdgeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mParams.x = (Integer) animation.getAnimatedValue();
                    mWindowManager.updateViewLayout(FloatingView.this, mParams);
                }
            });
            // X軸のアニメーション設定
            mMoveEdgeAnimator.setDuration(MOVE_TO_EDGE_DURATION);
            mMoveEdgeAnimator.setInterpolator(mMoveEdgeInterpolator);
            mMoveEdgeAnimator.start();
        } else {
            // 位置が変化した時のみ更新
            if (mParams.x != goalPositionX || mParams.y != goalPositionY) {
                mParams.x = goalPositionX;
                mParams.y = goalPositionY;
                mWindowManager.updateViewLayout(FloatingView.this, mParams);
            }
        }
        // タッチ座標を初期化
        mLocalTouchX = 0;
        mLocalTouchY = 0;
        mScreenTouchDownX = 0;
        mScreenTouchDownY = 0;
        mIsMoveAccept = false;
    }

    /**
     * アニメーションをキャンセルします。
     */
    private void cancelAnimation() {
        if (mMoveEdgeAnimator != null && mMoveEdgeAnimator.isStarted()) {
            mMoveEdgeAnimator.cancel();
            mMoveEdgeAnimator = null;
        }
    }

    private void setScale(float newScale) {
        // INFO:childにscaleを設定しないと拡大率が変わらない現象に対処するための修正
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View targetView = getChildAt(i);
                targetView.setScaleX(newScale);
                targetView.setScaleY(newScale);
            }
        } else {
            setScaleX(newScale);
            setScaleY(newScale);
        }
    }

    /**
     * ドラッグ可能フラグ
     *
     * @param isDraggable ドラッグ可能にする場合はtrue
     */
    public void setDraggable(boolean isDraggable) {
        mIsDraggable = isDraggable;
    }

    /**
     * Viewの形を表す定数
     *
     * @param shape SHAPE_CIRCLE or SHAPE_RECTANGLE
     */
    public void setShape(float shape) {
        mShape = shape;
    }

    /**
     * Viewの形を取得します。
     *
     * @return SHAPE_CIRCLE or SHAPE_RECTANGLE
     */
    public float getShape() {
        return mShape;
    }

    /**
     * 画面端をオーバーするマージンです。
     *
     * @param margin マージン
     */
    public void setOverMargin(int margin) {
        mOverMargin = margin;
    }

    /**
     * 移動方向を設定します。
     *
     * @param moveDirection 移動方向
     */
    public void setMoveDirection(int moveDirection) {
        mMoveDirection = moveDirection;
    }

    public void setInitCoords(int x, int y) {
        mInitX = x;
        mInitY = y;
    }

    public void setAnimateInitialMove(boolean animateInitialMove) {
        mAnimateInitialMove = animateInitialMove;
    }

    public void getWindowDrawingRect(Rect outRect) {
        final int currentX = getXByTouch();
        final int currentY = getYByTouch();
        outRect.set(currentX, currentY, currentX + getWidth(), currentY + getHeight());
    }

    public WindowManager.LayoutParams getWindowLayoutParams() {
        return mParams;
    }

    private int getXByTouch() {
        return (int) (mScreenTouchX - mLocalTouchX);
    }

    private int getYByTouch() {
        return (int) (mMetrics.heightPixels - (mScreenTouchY - mLocalTouchY + getHeight()));
    }

    public void setNormal() {
        mAnimationHandler.setState(STATE_NORMAL);
        mAnimationHandler.updateTouchPosition(getXByTouch(), getYByTouch());
    }

    /**
     * 重なった状態に変更します。
     *
     * @param centerX 対象の中心座標X
     * @param centerY 対象の中心座標Y
     */
    public void setIntersecting(int centerX, int centerY) {
        mAnimationHandler.setState(STATE_INTERSECTING);
        mAnimationHandler.updateTargetPosition(centerX, centerY);
    }

    /**
     * 終了状態に変更します。
     */
    public void setFinishing() {
        mAnimationHandler.setState(STATE_FINISHING);
        setVisibility(View.GONE);
    }

    public int getState() {
        return mAnimationHandler.getState();
    }

    /**
     * アニメーションの制御を行うハンドラです。
     */
    public static class FloatingAnimationHandler extends Handler {

        /**
         * アニメーションをリフレッシュするミリ秒
         */
        private static final long ANIMATION_REFRESH_TIME_MILLIS = 17L;

        /**
         * FloatingViewの吸着の着脱時間
         */
        private static final long CAPTURE_DURATION_MILLIS = 300L;

        /**
         * アニメーションなしの状態を表す定数
         */
        private static final int ANIMATION_NONE = 0;

        /**
         * タッチ時に発生するアニメーションの定数
         */
        private static final int ANIMATION_IN_TOUCH = 1;

        /**
         * アニメーション開始を表す定数
         */
        private static final int TYPE_FIRST = 1;
        /**
         * アニメーション更新を表す定数
         */
        private static final int TYPE_UPDATE = 2;

        /**
         * アニメーションを開始した時間
         */
        private long mStartTime;

        /**
         * アニメーションを始めた時点のTransitionX
         */
        private float mStartX;

        /**
         * アニメーションを始めた時点のTransitionY
         */
        private float mStartY;

        /**
         * 実行中のアニメーションのコード
         */
        private int mStartedCode;

        /**
         * アニメーション状態フラグ
         */
        private int mState;

        /**
         * 現在の状態
         */
        private boolean mIsChangeState;

        /**
         * 追従対象のX座標
         */
        private float mTouchPositionX;

        /**
         * 追従対象のY座標
         */
        private float mTouchPositionY;

        /**
         * 追従対象のX座標
         */
        private float mTargetPositionX;

        /**
         * 追従対象のY座標
         */
        private float mTargetPositionY;

        /**
         * FloatingView
         */
        private final WeakReference<FloatingView> mFloatingView;

        /**
         * コンストラクタ
         */
        FloatingAnimationHandler(FloatingView floatingView) {
            mFloatingView = new WeakReference<>(floatingView);
            mStartedCode = ANIMATION_NONE;
            mState = STATE_NORMAL;
        }

        /**
         * アニメーションの処理を行います。
         */
        @Override
        public void handleMessage(Message msg) {
            final FloatingView floatingView = mFloatingView.get();
            if (floatingView == null) {
                removeMessages(ANIMATION_IN_TOUCH);
                return;
            }

            final int animationCode = msg.what;
            final int animationType = msg.arg1;
            final WindowManager.LayoutParams params = floatingView.mParams;
            final WindowManager windowManager = floatingView.mWindowManager;

            // 状態変更またはアニメーションを開始した場合の初期化
            if (mIsChangeState || animationType == TYPE_FIRST) {
                // 状態変更時のみアニメーション時間を使う
                mStartTime = mIsChangeState ? SystemClock.uptimeMillis() : 0;
                mStartX = params.x;
                mStartY = params.y;
                mStartedCode = animationCode;
                mIsChangeState = false;
            }
            // 経過時間
            final float elapsedTime = SystemClock.uptimeMillis() - mStartTime;
            final float trackingTargetTimeRate = Math.min(elapsedTime / CAPTURE_DURATION_MILLIS, 1.0f);

            // 重なっていない場合のアニメーション
            if (mState == FloatingView.STATE_NORMAL) {
                final float basePosition = calcAnimationPosition(trackingTargetTimeRate);
                // 画面外へのオーバーを認める
                final Rect moveLimitRect = floatingView.mMoveLimitRect;
                // 最終的な到達点
                final float targetPositionX = Math.min(Math.max(moveLimitRect.left, (int) mTouchPositionX), moveLimitRect.right);
                final float targetPositionY = Math.min(Math.max(moveLimitRect.top, (int) mTouchPositionY), moveLimitRect.bottom);
                params.x = (int) (mStartX + (targetPositionX - mStartX) * basePosition);
                params.y = (int) (mStartY + (targetPositionY - mStartY) * basePosition);
                windowManager.updateViewLayout(floatingView, params);
                sendMessageAtTime(newMessage(animationCode, TYPE_UPDATE), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            }
            // 重なった場合のアニメーション
            else if (mState == FloatingView.STATE_INTERSECTING) {
                final float basePosition = calcAnimationPosition(trackingTargetTimeRate);
                // 最終的な到達点
                final float targetPositionX = mTargetPositionX - floatingView.getWidth() / 2;
                final float targetPositionY = mTargetPositionY - floatingView.getHeight() / 2;
                // 現在地からの移動
                params.x = (int) (mStartX + (targetPositionX - mStartX) * basePosition);
                params.y = (int) (mStartY + (targetPositionY - mStartY) * basePosition);
                windowManager.updateViewLayout(floatingView, params);
                sendMessageAtTime(newMessage(animationCode, TYPE_UPDATE), SystemClock.uptimeMillis() + ANIMATION_REFRESH_TIME_MILLIS);
            }

        }

        /**
         * アニメーション時間から求められる位置を計算します。
         *
         * @param timeRate 時間比率
         * @return ベースとなる係数(0.0から1.0＋α)
         */
        private static float calcAnimationPosition(float timeRate) {
            final float position;
            // y=0.55sin(8.0564x-π/2)+0.55
            if (timeRate <= 0.4) {
                position = (float) (0.55 * Math.sin(8.0564 * timeRate - Math.PI / 2) + 0.55);
            }
            // y=4(0.417x-0.341)^2-4(0.417-0.341)^2+1
            else {
                position = (float) (4 * Math.pow(0.417 * timeRate - 0.341, 2) - 4 * Math.pow(0.417 - 0.341, 2) + 1);
            }
            return position;
        }

        /**
         * アニメーションのメッセージを送信します。
         *
         * @param animation   ANIMATION_IN_TOUCH
         * @param delayMillis メッセージの送信時間
         */
        void sendAnimationMessageDelayed(int animation, long delayMillis) {
            sendMessageAtTime(newMessage(animation, TYPE_FIRST), SystemClock.uptimeMillis() + delayMillis);
        }

        /**
         * アニメーションのメッセージを送信します。
         *
         * @param animation ANIMATION_IN_TOUCH
         */
        void sendAnimationMessage(int animation) {
            sendMessage(newMessage(animation, TYPE_FIRST));
        }

        /**
         * 送信するメッセージを生成します。
         *
         * @param animation ANIMATION_IN_TOUCH
         * @param type      TYPE_FIRST,TYPE_UPDATE
         * @return Message
         */
        private static Message newMessage(int animation, int type) {
            final Message message = Message.obtain();
            message.what = animation;
            message.arg1 = type;
            return message;
        }

        /**
         * タッチ座標の位置を更新します。
         *
         * @param positionX タッチX座標
         * @param positionY タッチY座標
         */
        void updateTouchPosition(float positionX, float positionY) {
            mTouchPositionX = positionX;
            mTouchPositionY = positionY;
        }

        /**
         * 追従対象の位置を更新します。
         *
         * @param centerX 追従対象のX座標
         * @param centerY 追従対象のY座標
         */
        void updateTargetPosition(float centerX, float centerY) {
            mTargetPositionX = centerX;
            mTargetPositionY = centerY;
        }

        /**
         * アニメーション状態を設定します。
         *
         * @param newState STATE_NORMAL or STATE_INTERSECTING or STATE_FINISHING
         */
        void setState(int newState) {
            // 状態が異なった場合のみ状態を変更フラグを変える
            if (mState != newState) {
                mIsChangeState = true;
            }
            mState = newState;
        }

        /**
         * 現在の状態を返します。
         *
         * @return STATE_NORMAL or STATE_INTERSECTING or STATE_FINISHING
         */
        int getState() {
            return mState;
        }
    }


    static class LongPressHandler extends Handler {
        private final WeakReference<FloatingView> mFloatingView;
        private static final int LONG_PRESSED = 0;

        LongPressHandler(FloatingView view) {
            mFloatingView = new WeakReference<>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            FloatingView view = mFloatingView.get();
            if (view == null) {
                removeMessages(LONG_PRESSED);
                return;
            }

//            view.onLongClick();
            view.onClick();
        }
    }
}