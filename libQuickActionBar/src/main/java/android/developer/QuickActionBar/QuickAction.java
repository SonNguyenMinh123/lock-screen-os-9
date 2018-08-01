package android.developer.QuickActionBar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import developer.pham.tien.phong.libquickactionbar.R;

public class QuickAction extends PopupWindows implements OnDismissListener {
    private View mRootView;
    private ImageView mArrowUp;
    private ImageView mArrowDown;
    private LayoutInflater mInflater;
    private ViewGroup mTrack;
    private ScrollView mScroller;
    private OnActionItemClickListener mItemClickListener;
    private OnDismissListener mDismissListener;
    private TextView text;

    private List<ActionItem> actionItems = new ArrayList<ActionItem>();

    private boolean mDidAction;

    private int mChildPos;
    private int mInsertPos;
    private int mAnimStyle;
    private int mOrientation;
    private int rootWidth = 0;

    public static final int HORIZONTAL = 10;
    public static final int HORIZONTAL_STYLE1 = 11;
    public static final int HORIZONTAL_STYLE2 = 12;

    public static final int VERTICAL = 20;
    public static final int VERTICAL_STYLE1 = 21;
    public static final int VERTICAL_STYLE2 = 22;
    public static final int VERTICAL_STYLE3 = 23;

    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_REFLECT = 4;
    public static final int ANIM_AUTO = 5;

    public QuickAction(Context context) {
        this(context, VERTICAL);
    }

    public QuickAction(Context context, int orientation) {
        super(context);

        mOrientation = orientation;

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // if (mOrientation == HORIZONTAL) {
        // setRootViewId(R.layout.popup_horizontal);
        // } else {
        // setRootViewId(R.layout.popup_vertical);
        // }
        switch (mOrientation) {
            case HORIZONTAL:
                setRootViewId(R.layout.popup_horizontal);
                break;
            case HORIZONTAL_STYLE1:
                setRootViewId(R.layout.popup_horizontal1);
                break;
            case HORIZONTAL_STYLE2:
                setRootViewId(R.layout.popup_horizontal2);
                break;
            case VERTICAL:
                setRootViewId(R.layout.popup_vertical);
                break;
            case VERTICAL_STYLE1:
                setRootViewId(R.layout.popup_vertical1);
                break;
            case VERTICAL_STYLE2:
                setRootViewId(R.layout.popup_vertical2);
                break;
            case VERTICAL_STYLE3:
                setRootViewId(R.layout.popup_vertical3);
                break;
            default:
                setRootViewId(R.layout.popup_vertical);
                break;
        }

        mAnimStyle = ANIM_AUTO;
        mChildPos = 0;
    }

    public void setRootViewId(int id, boolean isArow) {
        mRootView = (ViewGroup) mInflater.inflate(id, null);
        if (isArow) {
            mArrowDown = (ImageView) mRootView.findViewById(R.id.arrow_down);
            mArrowUp = (ImageView) mRootView.findViewById(R.id.arrow_up);
        }

        mTrack = (ViewGroup) mRootView.findViewById(R.id.tracks);


        mScroller = (ScrollView) mRootView.findViewById(R.id.scroller);
        mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setContentView(mRootView);
    }


    public ActionItem getActionItem(int index) {
        return actionItems.get(index);
    }

    public void setRootViewId(int id) {
        setRootViewId(id, true);
    }

    public void setAnimStyle(int mAnimStyle) {
        this.mAnimStyle = mAnimStyle;
    }

    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        mItemClickListener = listener;
    }

    public void addActionItem(ActionItem action) {
        actionItems.add(action);

        String title = action.getTitle();
        String colortext = action.getColorTitle();
        Drawable icon = action.getIcon();

        View container;

        if (mOrientation == HORIZONTAL || mOrientation == HORIZONTAL_STYLE1 || mOrientation == HORIZONTAL_STYLE2) {
            container = mInflater.inflate(R.layout.action_item_horizontal, null);
        } else {
            container = mInflater.inflate(R.layout.action_item_vertical, null);
        }

        ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
        text = (TextView) container.findViewById(R.id.tv_title);
        if (colortext == null || colortext == "") {
        } else {
            try {
                text.setTextColor(Color.parseColor(colortext));
            } catch (Exception e) {
                Log.e("Quickactionbar",
                        "Parse the color string, and return the corresponding color-int. If the string cannot be parsed, throws an IllegalArgumentException exception. Supported formats are: #RRGGBB #AARRGGBB 'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta', 'yellow', 'lightgray', 'darkgray', 'grey', 'lightgrey', 'darkgrey', 'aqua', 'fuschia', 'lime', 'maroon', 'navy', 'olive', 'purple', 'silver', 'teal'");
            }

        }
        if (icon != null) {
            img.setImageDrawable(icon);
        } else {
            img.setVisibility(View.GONE);
        }

        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(View.GONE);
        }

        final int pos = mChildPos;
        final int actionId = action.getActionId();

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
                }

                if (!getActionItem(pos).isSticky()) {
                    mDidAction = true;

                    dismiss();
                }
            }
        });

        container.setFocusable(true);
        container.setClickable(true);

        if ((mOrientation == HORIZONTAL && mChildPos != 0) || (mOrientation == HORIZONTAL_STYLE1 && mChildPos != 0) ||
                (mOrientation == HORIZONTAL_STYLE2 && mChildPos != 0)) {
            View separator = mInflater.inflate(R.layout.horiz_separator, null);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.FILL_PARENT);

            separator.setLayoutParams(params);
            separator.setPadding(5, 0, 5, 0);

            mTrack.addView(separator, mInsertPos);

            mInsertPos++;
        }

        mTrack.addView(container, mInsertPos);

        mChildPos++;
        mInsertPos++;
    }

    public void show(View anchor) {
        preShow();

        int xPos, yPos, arrowPos;

        mDidAction = false;

        int[] location = new int[2];

        anchor.getLocationOnScreen(location);

        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
        mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int rootHeight = mRootView.getMeasuredHeight();

        if (rootWidth == 0) {
            rootWidth = mRootView.getMeasuredWidth();
        }

        int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if ((anchorRect.left + rootWidth) > screenWidth) {
            xPos = anchorRect.left - (rootWidth - anchor.getWidth());
            xPos = (xPos < 0) ? 0 : xPos;

            arrowPos = anchorRect.centerX() - xPos;

        } else {
            if (anchor.getWidth() > rootWidth) {
                xPos = anchorRect.centerX() - (rootWidth / 2);
            } else {
                xPos = anchorRect.left;
            }

            arrowPos = anchorRect.centerX() - xPos;
        }

        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;

        boolean onTop = (dyTop > dyBottom) ? true : false;

        if (onTop) {
            if (rootHeight > dyTop) {
                yPos = 15;
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyTop - anchor.getHeight();
            } else {
                yPos = anchorRect.top - rootHeight;
            }
        } else {
            yPos = anchorRect.bottom;

            if (rootHeight > dyBottom) {
                LayoutParams l = mScroller.getLayoutParams();
                l.height = dyBottom;
            }
        }

        showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

        mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
    }

    private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
        int arrowPos = 0;
        if (mArrowUp != null && mArrowDown != null) {
            arrowPos = requestedX - mArrowUp.getMeasuredWidth() / 2;
        }

        switch (mAnimStyle) {
            case ANIM_GROW_FROM_LEFT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                break;

            case ANIM_GROW_FROM_RIGHT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                break;

            case ANIM_GROW_FROM_CENTER:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                break;

            case ANIM_REFLECT:
                mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
                break;

            case ANIM_AUTO:
                if (mArrowUp != null && mArrowDown != null) {
                    if (arrowPos <= screenWidth / 4) {
                        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
                    } else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
                        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                    } else {
                        mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
                    }
                } else {
                    mWindow.setAnimationStyle((onTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
                }

                break;
        }
    }

    private void showArrow(int whichArrow, int requestedX) {
        if (mArrowUp != null && mArrowDown != null) {
            final View showArrow = (whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;

            final View hideArrow = (whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

            final int arrowWidth = mArrowUp.getMeasuredWidth();

            showArrow.setVisibility(View.VISIBLE);

            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow.getLayoutParams();

            param.leftMargin = requestedX - arrowWidth / 2;

            hideArrow.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Set listener for window dismissed. This listener will only be fired if the quicakction dialog is dismissed by clicking outside
     * the dialog or clicking on sticky item.
     */
    public void setOnDismissListener(QuickAction.OnDismissListener listener) {
        setOnDismissListener(this);

        mDismissListener = listener;
    }

    @Override
    public void onDismiss() {
        if (!mDidAction && mDismissListener != null) {
            mDismissListener.onDismiss();
        }
    }

    public interface OnActionItemClickListener {
        public abstract void onItemClick(QuickAction source, int pos, int actionId);
    }

    public interface OnDismissListener {
        public abstract void onDismiss();
    }
}