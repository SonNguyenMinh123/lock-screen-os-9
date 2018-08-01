package android.developer.QuickActionBar;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;

/**
 * Action item, displayed as menu with icon and text.
 * 
 * @author Lorensius. W. L. T <lorenz@londatiga.net>
 * 
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 * 
 */
public class ActionItem {
	private Drawable icon;
	private Bitmap thumb;
	private String title;
	private int actionId = -1;
	private boolean selected;
	private boolean sticky;
	
	private Drawable scroller;
	private Drawable arrow_up;
	private Drawable arrow_down;
	
	public Drawable getScroller() {
		return scroller;
	}

	public void setScroller(Drawable scroller) {
		this.scroller = scroller;
	}

	public Drawable getArrow_up() {
		return arrow_up;
	}

	public void setArrow_up(Drawable arrow_up) {
		this.arrow_up = arrow_up;
	}

	public Drawable getArrow_down() {
		return arrow_down;
	}

	public void setArrow_down(Drawable arrow_down) {
		this.arrow_down = arrow_down;
	}

	public String getColorTitle() {
		return ColorTitle;
	}

	public void setColorTitle(String colorTitle) {
		ColorTitle = colorTitle;
	}

	private String ColorTitle;

	public ActionItem(int actionId, String title,String colorTitle, Drawable icon) {
		this.title = title;
		this.ColorTitle =colorTitle;
		this.icon = icon;
		this.actionId = actionId;
	}

	public ActionItem() {
		this(-1, null,null, null);
	}

	public ActionItem(int actionId, String title) {
		this(actionId, title,null, null);
	}

	public ActionItem(Drawable icon) {
		this(-1, null,null, icon);
	}

	public ActionItem(int actionId, Drawable icon) {
		this(actionId, null,null,icon);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public Drawable getIcon() {
		return this.icon;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public boolean isSticky() {
		return sticky;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setThumb(Bitmap thumb) {
		this.thumb = thumb;
	}

	public Bitmap getThumb() {
		return this.thumb;
	}
}