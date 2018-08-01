package com.abc.xyz.views.partials;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.developer.QuickActionBar.ActionItem;
import android.developer.QuickActionBar.QuickAction;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.adapters.MessagerAdapter;
import com.abc.xyz.callbacks.UnLockLayoutCallBack;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.entities.MessageEntity;
import com.abc.xyz.entities.SongEntity;
import com.abc.xyz.events.SwipeDismissListEvent;
import com.abc.xyz.screens.ActionControlLockActivity;
import com.abc.xyz.screens.LockerActivity;
import com.abc.xyz.screens.MenuSettingActivity;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.services.lockscreenios9.MusicPlayerService;
import com.abc.xyz.tasks.LoadSongSdcardTask;
import com.abc.xyz.utils.DpiUtil;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.ScreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.SystemUtil;
import com.abc.xyz.utils.TimeUtil;
import com.abc.xyz.utils.logs.LogUtil;
import com.abc.xyz.views.widgets.TextviewIPBold;
import com.abc.xyz.views.widgets.shimmertextview.Shimmer;
import com.abc.xyz.views.widgets.shimmertextview.ShimmerTextView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by DucNguyen on 6/24/2015.
 */
public class UnlockLayout extends RelativeLayout implements View.OnClickListener {
    public static Camera camera;
    public static boolean firstrun = true;
    private static AudioManager audioManager;
    private static Context mContext;
    private static ViewGroup mContainer;
    private static LinearLayout lnlLayoutUnlockWeather;
    private static UnlockLayout layout;
    private static Weatherlayout test;
    private static ImageButton imbPartialSlideupPlayOrPase;
    public final String TAG = "UnlockLayout";
    public ImageView imgPartialUnlockCamera;
    public SlidingUpPanelLayout sluLayoutUnlock;
    public ImageView imvPartialSlideupTimmer;
    public ImageView imvPartialSlideupCalculator;
    public ImageView imvPartialSlideupCamera;
    public MessagerAdapter mMessagerAdapter;
    public boolean isRegistered;
    int count = 0;
    private TextView txvLayoutUnlockTime;
    private TextView txvLayoutUnlockDay;
    private ShimmerTextView stvLayoutUnlock;
    private TextView txvLayoutUnlockAm;
    private Typeface mClock1;
    private Typeface mClock2;
    private Shimmer shimmer;
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;
    private String str7;
    private RelativeLayout rllLayoutUnlockRootview;
    private MusicPlayerService mMusicPlayerService;
    private SeekBar skbPartialSlideupVolumn;
    private ImageView imvPartialSlideupFlash;
    private ImageView imgLayoutUnlockSlideup;
    private FrameLayout frlPartialSlideupCameraheight;
    //slide up
    private ImageButton imbPartialSlideupWifi;
    private ImageButton imbPartialSlideupBluetooth;
    private ImageButton imbPartialSlideupMute;
    private ImageButton imbPartialSlideupOrientation;
    private SeekBar skbPartialSlideupBrightness;
    private ImageButton imbPartialSlideupRepeat;
    private ImageButton imbPartialSlideupMenuMusic;
    private ImageButton imbPartialSlideupLock;
    private ImageView imgPartialSlideupArrow;
    private UnLockLayoutCallBack mUnLockLayoutCallBack;
    private ListView slvLayoutUnlock;
    private TextView txvLayoutMusicNameSong;
    private TextView txvLayoutMusicNameAuthor;
    private SeekBar nowPlayingSeekBar;
    private ImageView btnPartialSlideupPrev;
    private ImageView btnPartialSlideupNext;
    private Handler durationHandler = new Handler();
    private List<MessageEntity> mListMessage;
    SwipeDismissListEvent.OnDismissCallback callback = new SwipeDismissListEvent.OnDismissCallback() {
        // Gets called whenever the user deletes an item.
        public SwipeDismissListEvent.Undoable onDismiss(AbsListView listView, final int position) {

            try {
                // Get your item from the adapter (mAdapter being an adapter for MyItem objects)
                final MessageEntity deletedItem = mListMessage.get(position);
                // Delete item from adapter
                mListMessage.remove(position);
                mMessagerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // adatperFavoriteMeme.notifyDataSetChanged();
            // Return an Undoable implementing every method
            return new SwipeDismissListEvent.Undoable() {

                // Method is called when user undoes this deletion
                public void undo() {
                    // Reinsert item to list
                    //adatperFavoriteMeme.insert(deletedItem, position)

                }

                // Return an undo message for that item
                public String getTitle() {
                    return "";
                }

                // Called when user cannot undo the action anymore
                public void discard() {
                    // Use this place to e.g. delete the item from database
                    //finallyDeleteFromSomeStorage(deletedItem);


                }
            };
        }
    };
    private FrameLayout frlLayoutUnlockCenterView;
    private ArrayList<SongEntity> mSongEntityArrayList = new ArrayList<>();
    private PendingIntent mClientIntent;
    private int newPosition;
    private int currentPosition;
    private int previousPosition;
    private int currentTime;
    private long totalDuration;
    private long currentDuration;
    private TextviewIPBold txvPartialSlideupTimePlay;
    private TextviewIPBold txvPartialSlideupTimeDuration;
    private boolean isStop = false;
    public OnClickListener onMusicClickedListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSongEntityArrayList == null || mSongEntityArrayList.size() == 0) {
                MyToast.showToast(mContext, "No songs found");
                imbPartialSlideupPlayOrPase.setSelected(false);
                return;
            }
            if (v == btnPartialSlideupPrev) {
                if (mMusicPlayerService != null)
                    mMusicPlayerService.playPrev();
                isStop = false;
                imbPartialSlideupPlayOrPase.setSelected(true);
            } else if (v == btnPartialSlideupNext) {
                if (mMusicPlayerService != null)
                    mMusicPlayerService.playNext();
                imbPartialSlideupPlayOrPase.setSelected(true);
                isStop = false;
            } else if (v == imbPartialSlideupPlayOrPase) {
                if (mMusicPlayerService != null) {
                    if (imbPartialSlideupPlayOrPase.isSelected()) {
                        imbPartialSlideupPlayOrPase.setSelected(false);
                        mMusicPlayerService.pausePlayer();
                        isStop = true;
                    } else {
                        imbPartialSlideupPlayOrPase.setSelected(true);
                        if (firstrun) {
                            mMusicPlayerService.playSong();
                        } else {
                            mMusicPlayerService.go();
                        }
                        isStop = false;
                    }
                }
            } else if (imbPartialSlideupRepeat == v) {
                if (imbPartialSlideupRepeat.isSelected()) {
                    imbPartialSlideupRepeat.setSelected(false);
                    SharedPreferencesUtil.setRepeatMusic(mContext, false);
                } else {
                    imbPartialSlideupRepeat.setSelected(true);
                    SharedPreferencesUtil.setRepeatMusic(mContext, true);
                }
                return;
            } else if (imbPartialSlideupMenuMusic == v) {
                openListMusic();
                return;
            }
            firstrun = false;
        }


    };
    private Handler mHandler = new Handler();
    private boolean touch = false;
    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            try {
                if (!firstrun) {
                    //                if (isStop) {
                    //                    totalDuration = 0;
                    //                    currentDuration = 0;
                    //                } else {
                    totalDuration = mMusicPlayerService.getDur();
                    currentDuration = mMusicPlayerService.getPosn();
                    //                }
                    txvPartialSlideupTimeDuration.setText("" + TimeUtil.milliSecondsToTimer(totalDuration));
                    txvPartialSlideupTimePlay.setText("" + TimeUtil.milliSecondsToTimer(currentDuration));

                    int progress = TimeUtil.getProgressPercentage(currentDuration, totalDuration);

                    nowPlayingSeekBar.setProgress(progress);
                    if (count >= 5) {
                        updateTitleAndArtistSong();
                        count = 0;
                    }
                    count++;
                }
                durationHandler.postDelayed(this, 100);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    };
    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            new LoadSongSdcardTask(mContext, new LoadSongSdcardTask.CommunicatorLoadLocation() {
                @Override
                public void onPostExecute(ArrayList<SongEntity> songEntityArrayList) {
                    mSongEntityArrayList = songEntityArrayList;
                    MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
                    //get service
                    mMusicPlayerService = binder.getService();
                    //pass list
                    mMusicPlayerService.setList(mSongEntityArrayList);
                    LogUtil.getLogger().d(TAG, "onServiceConnected");
//            musicBound = true;
                    updateProgressBar();
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            musicBound = false;
        }
    };
    private BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        public void onReceive(Context paramContext, Intent paramIntent) {
            if (paramIntent.getAction().equals("android.intent.action.TIME_TICK")) {
                new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                LockScreenController.getInstance(mContext).runLockscreeniOS9ViewService();
            }
        }
    };
    private boolean check = false;
    /**
     * comming soon events
     */
    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == imvPartialSlideupFlash) {
                PackageManager pm = mContext.getPackageManager();
                if (SystemUtil.isFlashSupported(pm)) {
                    if (check) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imvPartialSlideupFlash.setImageResource(R.drawable.ic_slide_flash_normal);
                                    }
                                }).run();
                            }
                        });
                        startFlashLight(false);
                        check = false;
                    } else {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imvPartialSlideupFlash.setImageResource(R.drawable.ic_slide_flash_pressed);
                                    }
                                }).run();
                            }
                        });
                        startFlashLight(true);
                        check = true;
                    }
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme)).create();
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alertDialog.setTitle("No Camera Flash");
                    alertDialog.setMessage("The device's camera doesn't support flash.");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int which) {
                            Log.e("err", "The device's camera doesn't support flash.");
                        }
                    });
                    alertDialog.show();
                }
            } else if (v == imvPartialSlideupCalculator) {
                imgPartialUnlockCamera.setClickable(false);
                imvPartialSlideupCalculator.setClickable(false);
                imvPartialSlideupTimmer.setClickable(false);
                imvPartialSlideupCamera.setClickable(false);
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent = new Intent(mContext, ActionControlLockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key_start", "calculator");
                mContext.startActivity(intent);
            } else if (v == imvPartialSlideupTimmer) {
                imgPartialUnlockCamera.setClickable(false);
                imvPartialSlideupCalculator.setClickable(false);
                imvPartialSlideupTimmer.setClickable(false);
                imvPartialSlideupCamera.setClickable(false);
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent = new Intent(mContext, ActionControlLockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key_start", "alarm");
                mContext.startActivity(intent);
            } else if (v == imvPartialSlideupCamera) {
                imgPartialUnlockCamera.setClickable(false);
                imvPartialSlideupCalculator.setClickable(false);
                imvPartialSlideupTimmer.setClickable(false);
                imvPartialSlideupCamera.setClickable(false);
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent = new Intent(mContext, ActionControlLockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key_start", "camera");
                mContext.startActivity(intent);
            } else if (v == imgPartialUnlockCamera) {
                imgPartialUnlockCamera.setClickable(false);
                imvPartialSlideupCalculator.setClickable(false);
                imvPartialSlideupTimmer.setClickable(false);
                imvPartialSlideupCamera.setClickable(false);

                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent = new Intent(mContext, ActionControlLockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key_start", "camera");
                mContext.startActivity(intent);
                imvPartialSlideupCamera.setClickable(true);
            }
        }
    };

    public UnlockLayout(Context context) {
        super(context);
    }

    public UnlockLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnlockLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static UnlockLayout fromXml(Context context, ViewGroup viewGroup) {
        mContext = context;
        mContainer = viewGroup;
        layout = (UnlockLayout) LayoutInflater.from(context)
                .inflate(R.layout.partial_unlock, viewGroup, false);
        return layout;
    }

    public static UnlockLayout getInstance() {
        return layout;
    }

    public static void updateVisiableWeather() {
        if (SharedPreferencesUtil.isShowWeather(mContext)) {
            if (test != null) {
                test.closeLayout();
            }
            test = Weatherlayout.fromXml(mContext, lnlLayoutUnlockWeather);
            test.openLayout();
        } else {
            if (test != null) {
                test.closeLayout();
                test = null;
            }
        }
    }

    public void updateSlideText() {
        stvLayoutUnlock.setText(SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, "> Slide to Unlock"));
    }

    public void updateCamera() {
        if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_CAMERA, false)) {
            imgPartialUnlockCamera.setVisibility(View.VISIBLE);
        } else {
            imgPartialUnlockCamera.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rllLayoutUnlockRootview = (RelativeLayout) findViewById(R.id.rll_layout_unlock__rootview);
        lnlLayoutUnlockWeather = (LinearLayout) findViewById(R.id.lnl_layout_unlock_weather);
        txvLayoutUnlockTime = (TextView) findViewById(R.id.txv_layout_unlock_time);
        txvLayoutUnlockDay = (TextView) findViewById(R.id.txv_layout_unlock_day);
        stvLayoutUnlock = (ShimmerTextView) findViewById(R.id.stv_layout_unlock);
        txvLayoutUnlockAm = (TextView) findViewById(R.id.txv_layout_unlock_am);
        imgPartialUnlockCamera = (ImageView) findViewById(R.id.img_partial_unlock_camera);
        sluLayoutUnlock = (SlidingUpPanelLayout) findViewById(R.id.slu_layout_unlock);
        imbPartialSlideupWifi = (ImageButton) findViewById(R.id.imb_partial_slideup_wifi);
        imbPartialSlideupBluetooth = (ImageButton) findViewById(R.id.imb_partial_slideup_bluetooth);
        imbPartialSlideupMute = (ImageButton) findViewById(R.id.imb_partial_slideup_mute);
        imbPartialSlideupOrientation = (ImageButton) findViewById(R.id.imb_partial_slideup_orientation);
        skbPartialSlideupBrightness = (SeekBar) findViewById(R.id.skb_partial_slideup_brightness);
        imgPartialSlideupArrow = (ImageView) findViewById(R.id.img_partial_slideup_arrow);
        slvLayoutUnlock = (ListView) findViewById(R.id.slv_layout_unlock);
        imvPartialSlideupFlash = (ImageView) findViewById(R.id.imv_partial_slideup_flash);
        imgLayoutUnlockSlideup = (ImageView) findViewById(R.id.img_layout_unlock_slideup);
        imvPartialSlideupTimmer = (ImageView) findViewById(R.id.imv_partial_slideup_timmer);
        imvPartialSlideupCalculator = (ImageView) findViewById(R.id.imv_partial_slideup_calculator);
        imvPartialSlideupCamera = (ImageView) findViewById(R.id.imv_partial_slideup_camera);
        frlPartialSlideupCameraheight = (FrameLayout) findViewById(R.id.frl_partial_slideup_cameraheight);
        skbPartialSlideupVolumn = (SeekBar) findViewById(R.id.skb_partial_slideup_volumn);
        frlLayoutUnlockCenterView = (FrameLayout) findViewById(R.id.frl_layout_unlock__center_view);
        imbPartialSlideupRepeat = (ImageButton) findViewById(R.id.imb_partial_slideup__repeat);
        imbPartialSlideupMenuMusic = (ImageButton) findViewById(R.id.imb_partial_slideup__menu_music);
        nowPlayingSeekBar = (SeekBar) findViewById(R.id.nowPlayingSeekBar);
        txvLayoutMusicNameSong = (TextView) findViewById(R.id.txv_layout_music__name_song);
        txvLayoutMusicNameAuthor = (TextView) findViewById(R.id.txv_layout_music__name_author);
        txvPartialSlideupTimePlay = (TextviewIPBold) findViewById(R.id.txv_partial_slideup__time_play);
        txvPartialSlideupTimeDuration = (TextviewIPBold) findViewById(R.id.txv_partial_slideup__time_duration);
        btnPartialSlideupPrev = (ImageView) findViewById(R.id.imb_partial_slideup__prev);
        imbPartialSlideupPlayOrPase = (ImageButton) findViewById(R.id.imb_partial_slideup__play_or_pase);
        btnPartialSlideupNext = (ImageView) findViewById(R.id.imb_partial_slideup__next);
        imbPartialSlideupLock = (ImageButton) findViewById(R.id.imb_partial_slideup__lock);
        btnPartialSlideupPrev.setOnClickListener(onMusicClickedListener);
        imbPartialSlideupPlayOrPase.setOnClickListener(onMusicClickedListener);
        btnPartialSlideupNext.setOnClickListener(onMusicClickedListener);
        imbPartialSlideupRepeat.setOnClickListener(onMusicClickedListener);
        imbPartialSlideupMenuMusic.setOnClickListener(onMusicClickedListener);
        imvPartialSlideupFlash.setOnClickListener(onClickListener);
        imvPartialSlideupTimmer.setOnClickListener(onClickListener);
        imvPartialSlideupCalculator.setOnClickListener(onClickListener);
        imvPartialSlideupCamera.setOnClickListener(onClickListener);
        imgPartialUnlockCamera.setOnClickListener(onClickListener);


        imbPartialSlideupWifi.setOnClickListener(this);
        imbPartialSlideupBluetooth.setOnClickListener(this);
        imbPartialSlideupMute.setOnClickListener(this);
        imbPartialSlideupOrientation.setOnClickListener(this);
        imbPartialSlideupLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imbPartialSlideupLock.isSelected()) {
                    SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, true);
                    if (MenuSettingActivity.getInstance() != null) {
                        MenuSettingActivity.getInstance().swcActivitySettingScreenlock.setOn(true);
                    }
                    imbPartialSlideupLock.setSelected(true);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme)).create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.setMessage(getResources().getString(R.string.turn_of_lockscreen));
                    dialog.setButton2(getResources().getString(R.string.w_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            imbPartialSlideupLock.setSelected(true);
                            if (MenuSettingActivity.getInstance() != null) {
                                MenuSettingActivity.getInstance().swcActivitySettingScreenlock.setOn(true);
                            }
                        }
                    });
                    dialog.setButton(getResources().getString(R.string.w_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, false);
                            imbPartialSlideupLock.setSelected(false);
                            if (MenuSettingActivity.getInstance() != null) {
                                MenuSettingActivity.getInstance().swcActivitySettingScreenlock.setOn(false);
                            }

                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            imbPartialSlideupLock.setSelected(true);
                        }
                    });

              /*      dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            imbPartialSlideupLock.setChecked(true);
                        }
                    });*/

                    dialog.show();

                }
            }
        });
        nowPlayingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtil.getLogger().d(TAG, "onStopTrackingTouch");

                int totalDuration = mMusicPlayerService.getDur();
                int currentPosition = TimeUtil.progressToTimer(seekBar.getProgress(), totalDuration);

                mMusicPlayerService.seek(currentPosition);

            }
        });
        skbPartialSlideupBrightness.setMax(255);
        skbPartialSlideupVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        skbPartialSlideupBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 10) {
                    if (LockerActivity.getInstance() != null) {
                        SystemUtil.setBrightness(LockerActivity.getInstance(), progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rllLayoutUnlockRootview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (touch) {
                    sluLayoutUnlock.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    return true;
                }
                return false;
            }
        });
        sluLayoutUnlock.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                imgPartialSlideupArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow_scroll));
                imgLayoutUnlockSlideup.setVisibility(View.GONE);
                imgPartialUnlockCamera.setVisibility(View.GONE);
                if (slideOffset > 0.5)
                    stvLayoutUnlock.setVisibility(View.GONE);
                if (slideOffset < 0.5)
                    stvLayoutUnlock.setVisibility(View.VISIBLE);
                mUnLockLayoutCallBack.valueBlur(slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
                touch = true;
                imgPartialSlideupArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_arrow));
            }

            @Override
            public void onPanelCollapsed(View panel) {
                touch = false;
                if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_CAMERA, false))
                    imgPartialUnlockCamera.setVisibility(View.VISIBLE);
                imgLayoutUnlockSlideup.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });
        if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_CAMERA, false)) {
            imgPartialUnlockCamera.setVisibility(View.VISIBLE);
        } else {
            imgPartialUnlockCamera.setVisibility(View.GONE);
        }

    }

    public void updateSeekbarVolumn() {
        skbPartialSlideupVolumn.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    private void updateProgressBar() {
        durationHandler.postDelayed(updateTimeTask, 100);
    }

    private void showTime(boolean show) {
        if (show) {
            frlLayoutUnlockCenterView.setVisibility(View.VISIBLE);
            txvLayoutUnlockTime.setVisibility(View.VISIBLE);
            txvLayoutUnlockAm.setVisibility(View.VISIBLE);
            txvLayoutUnlockDay.setVisibility(View.VISIBLE);
        } else {
            frlLayoutUnlockCenterView.setVisibility(View.GONE);
            txvLayoutUnlockTime.setVisibility(View.GONE);
            txvLayoutUnlockAm.setVisibility(View.GONE);
            txvLayoutUnlockDay.setVisibility(View.GONE);
        }
    }

    private void openListMusic() {
        QuickAction quickAction = new QuickAction(mContext, QuickAction.VERTICAL_STYLE3);
        quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                firstrun = false;
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.setSong(pos);
                    mMusicPlayerService.playSong();
                }
            }
        });
        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        for (SongEntity songEntity : mSongEntityArrayList) {
            String titleSong = songEntity.getTitle();
            String artistName = songEntity.getArtist();
            if (titleSong != null && titleSong.length() > 25) {
                titleSong = titleSong.substring(0, 20) + "...";
            }
            if (artistName != null && artistName.length() > 15) {
                artistName = artistName.substring(0, 13) + "...";
            }

            String title = titleSong + " - " + artistName;
            ActionItem Item = new ActionItem(1, title, "#363636", mContext.getResources().getDrawable(R.drawable.ic_note_music));
            quickAction.addActionItem(Item);
        }
        quickAction.show(imbPartialSlideupMenuMusic);
    }

    private void updateTitleAndArtistSong() {
        if (mMusicPlayerService != null) {
            txvLayoutMusicNameSong.setText(mMusicPlayerService.getSongTitle());
            txvLayoutMusicNameAuthor.setText(mMusicPlayerService.getArtistName());
            if (mMusicPlayerService.isPng()) {
                imbPartialSlideupPlayOrPase.setSelected(true);
            } else {
                imbPartialSlideupPlayOrPase.setSelected(false);
            }
        }

    }

    public void updateTimeFormat() {
        new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * VolumnChangingReceiver -> this method
     */
    public void updateVolumnSeekbar() {
        skbPartialSlideupVolumn.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    public void updateMusicList() {
        new LoadSongSdcardTask(mContext, new LoadSongSdcardTask.CommunicatorLoadLocation() {
            @Override
            public void onPostExecute(ArrayList<SongEntity> songEntityArrayList) {
                mSongEntityArrayList = songEntityArrayList;
                if (mMusicPlayerService != null) {
                    mMusicPlayerService.setList(mSongEntityArrayList);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void openLayout() {
        // setup music service
        if (SharedPreferencesUtil.isRepeatMusic(mContext)) {
            imbPartialSlideupRepeat.setSelected(true);
        }
        updateMusicList();
        LockScreenController.getInstance(mContext).runMusicService(musicConnection);
        if (ScreenUtil.getSoftKeyHeight(mContext, LockScreen9ViewService.getInstance().mWindowManager) > 0) {

            LayoutParams layoutParams = (LayoutParams) imgLayoutUnlockSlideup.getLayoutParams();
            layoutParams.bottomMargin = (int) DpiUtil.dipToPx(mContext, 33);
            imgLayoutUnlockSlideup.setLayoutParams(layoutParams);

            layoutParams = (LayoutParams) stvLayoutUnlock.getLayoutParams();
            layoutParams.bottomMargin = (int) DpiUtil.dipToPx(mContext, 75);
            stvLayoutUnlock.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams layoutParamsFr = (LinearLayout.LayoutParams) frlPartialSlideupCameraheight.getLayoutParams();
            layoutParamsFr.height = (int) DpiUtil.dipToPx(mContext, 88);
            frlPartialSlideupCameraheight.setLayoutParams(layoutParamsFr);

            LinearLayout.LayoutParams layoutParamsln = (LinearLayout.LayoutParams) imvPartialSlideupFlash.getLayoutParams();
            layoutParamsln.bottomMargin = (int) DpiUtil.dipToPx(mContext, 33);
            imvPartialSlideupFlash.setLayoutParams(layoutParamsln);

            layoutParamsln = (LinearLayout.LayoutParams) imvPartialSlideupTimmer.getLayoutParams();
            layoutParamsln.bottomMargin = (int) DpiUtil.dipToPx(mContext, 33);
            imvPartialSlideupTimmer.setLayoutParams(layoutParamsln);

            layoutParamsln = (LinearLayout.LayoutParams) imvPartialSlideupCamera.getLayoutParams();
            layoutParamsln.bottomMargin = (int) DpiUtil.dipToPx(mContext, 33);
            imvPartialSlideupCamera.setLayoutParams(layoutParamsln);

            layoutParamsln = (LinearLayout.LayoutParams) imvPartialSlideupCalculator.getLayoutParams();
            layoutParamsln.bottomMargin = (int) DpiUtil.dipToPx(mContext, 33);
            imvPartialSlideupCalculator.setLayoutParams(layoutParamsln);

            sluLayoutUnlock.post(new Runnable() {
                @Override
                public void run() {
                    if (sluLayoutUnlock != null) {
                        sluLayoutUnlock.setPanelHeight((int) DpiUtil.dipToPx(mContext, 88));
                    }
                }
            });
        }
        mContainer.addView(this);

        new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        try {
            if (isRegistered == false) {

            }
            mContext.registerReceiver(timeReceiver, new IntentFilter("android.intent.action.TIME_TICK"));
            isRegistered = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (SystemUtil.isWifiEnble(mContext)) {
            imbPartialSlideupWifi.setSelected(true);
        }
        if (SystemUtil.isBluetoothEnble()) {
            imbPartialSlideupBluetooth.setSelected(true);
        }

        if (SystemUtil.isAutoOrientaitonEnable(mContext)) {
            imbPartialSlideupOrientation.setSelected(true);
        }
        if (SystemUtil.isSilentEnable(mContext)) {
            imbPartialSlideupMute.setSelected(true);
        }
        if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, true)) {
            imbPartialSlideupLock.setSelected(true);
        }
 /*       if (SystemUtil.isPlaneEnable(ActionControlLockActivity.getInstance())) {
            imbPartialSlideupPlane.setChecked(true);
        }*/
        skbPartialSlideupBrightness.setProgress(SystemUtil.getBrightness(mContext));
        requestFocus();
        requestLayout();

        updateVisiableWeather();
        setupMessage();
        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        skbPartialSlideupVolumn.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        skbPartialSlideupVolumn.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }

    public void updateMessage(List<MessageEntity> listMessage) {
        if (mListMessage == null) {
            mListMessage = new ArrayList<MessageEntity>();
            mMessagerAdapter = new MessagerAdapter(mContext, mListMessage);
            slvLayoutUnlock.setAdapter(mMessagerAdapter);
        }
        mListMessage.addAll(listMessage);
        mMessagerAdapter.notifyDataSetChanged();
    }

    public void updateMessage() {
        mListMessage = new ArrayList<MessageEntity>();
        mMessagerAdapter = new MessagerAdapter(mContext, mListMessage);
        slvLayoutUnlock.setAdapter(mMessagerAdapter);
    }

    public void updateMessage(MessageEntity messageEntity) {
        if (mListMessage == null) {
            mListMessage = new ArrayList<MessageEntity>();
            mMessagerAdapter = new MessagerAdapter(mContext, mListMessage);
            slvLayoutUnlock.setAdapter(mMessagerAdapter);
        }
        mListMessage.add(messageEntity);
        mMessagerAdapter.notifyDataSetChanged();
    }

    private void setupMessage() {
        mListMessage = new ArrayList<MessageEntity>();
        mMessagerAdapter = new MessagerAdapter(mContext, mListMessage);
        slvLayoutUnlock.setAdapter(mMessagerAdapter);
        SwipeDismissListEvent swipeList = new SwipeDismissListEvent(slvLayoutUnlock, callback,
                SwipeDismissListEvent.UndoMode.COLLAPSED_UNDO);
        slvLayoutUnlock.setOnTouchListener(swipeList);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
    }

    public void closeLayout() {
//            slideLeft(this);
        try {
            mContext.unregisterReceiver(timeReceiver);
            isRegistered = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContainer.removeView(this);
        clearFocus();
    }

    /**
     * start flash light
     */
    public void startFlashLight(boolean show) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters p = camera.getParameters();
            boolean on = show;
            if (on) {
                Log.i("info", "torch is turn on!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
                try {
                    camera.setPreviewTexture(mPreviewTexture);
                } catch (IOException ex) {
                    // Ignore
                }
                camera.startPreview();
            } else {
                Log.i("info", "torch is turn off!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onClick(View buttonView) {
        if (buttonView.isSelected()) {
            buttonView.setSelected(false);
        } else {
            buttonView.setSelected(true);
        }
        if (buttonView == imbPartialSlideupWifi) {
            SystemUtil.toggleWiFi(mContext, buttonView.isSelected());

        } else if (buttonView == imbPartialSlideupBluetooth) {
            SystemUtil.setBluetooth(buttonView.isSelected());

        } else if (buttonView == imbPartialSlideupOrientation) {
            SystemUtil.setAutoOrientationEnabled(mContext, buttonView.isSelected());
        } else if (buttonView == imbPartialSlideupMute) {
            SystemUtil.setSilentEnable(mContext, buttonView.isSelected());
        }
    }

    public void stopMusic() {
        firstrun = true;
        if (mMusicPlayerService != null) {
            imbPartialSlideupPlayOrPase.setSelected(false);
            mMusicPlayerService.pausePlayer();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(timeReceiver);
        isRegistered = false;
        stopMusic();
        LockScreenController.getInstance(mContext).stopMusicService(musicConnection);
        mMusicPlayerService = null;
        layout = null;
        if (test != null) {
            test.closeLayout();
            test = null;
        }
        if (check) {
            startFlashLight(false);
        }
    }

    public void setUnLocklayoutCallBackListener(UnLockLayoutCallBack unLockLayoutCallBack) {
        mUnLockLayoutCallBack = unLockLayoutCallBack;
    }

    private class InitDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            shimmer = new Shimmer();
            mClock1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal-font.ttf");
            mClock2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold-font.ttf");
            Calendar localCalendar = Calendar.getInstance();
            int i = localCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            int j = localCalendar.get(Calendar.HOUR);
            localCalendar.get(Calendar.DAY_OF_WEEK);
            str1 = String.valueOf(localCalendar.get(Calendar.DAY_OF_MONTH));
            str2 = String.valueOf(i);
            str3 = String.valueOf(j);
            if (str3.length() < 2) {
                new StringBuilder("0").append(str3).toString();
            }
            if (str2.length() < 2)
                new StringBuilder("0").append(str2).toString();
            Date localDate1 = new Date();
            SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("h:mm");
            str4 = localSimpleDateFormat1.format(localDate1);
            str5 = new SimpleDateFormat("a").format(localDate1);
            if (SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12").equalsIgnoreCase("12")) {
                Date localDate2 = new Date();
                SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("h:mm");
                str4 = localSimpleDateFormat2.format(localDate2);
                str5 = new SimpleDateFormat("a").format(localDate2);
            }
            if (SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12").equalsIgnoreCase("24")) {
                Date localDate3 = new Date();
                SimpleDateFormat localSimpleDateFormat3 = new SimpleDateFormat("k:mm");
                str4 = localSimpleDateFormat3.format(localDate3);
                str5 = "";
            }
            SimpleDateFormat localSimpleDateFormat4 = new SimpleDateFormat("MMM");
            SimpleDateFormat localSimpleDateFormat5 = new SimpleDateFormat("EEEE");
            boolean isLang = Locale.getDefault().getLanguage().equals("vi");
            if (isLang) {
                localSimpleDateFormat4 = new SimpleDateFormat("MMMM");
            }

            str6 = localSimpleDateFormat4.format(localCalendar.getTime());

            str7 = localSimpleDateFormat5.format(new Date());
//            addStateSound(mContext, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            stvLayoutUnlock.setText(SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, "> Slide to Unlock"));
                            shimmer.setDuration(2000);
                            shimmer.start(stvLayoutUnlock);

                            txvLayoutUnlockTime.setTypeface(mClock1);
                            boolean isLang = Locale.getDefault().getLanguage().equals("vi");

                            txvLayoutUnlockTime.setText(str4);
                            txvLayoutUnlockAm.setText(String.valueOf(str5));
                            if (!isLang) {
                                txvLayoutUnlockDay.setText(str7 + ", " + str6 + " " + str1);
                            } else {
                                txvLayoutUnlockDay.setText(str7 + ", " + str1 + " " + str6);
                            }
                            txvLayoutUnlockDay.setTypeface(mClock2);
                            stvLayoutUnlock.setTypeface(mClock2);
                        }
                    }).run();
                }
            });
        }


     /*   public void addStateSound(Context context, boolean b) {
            SharedPreferences pref = context.getSharedPreferences("dovui", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("onsound", b);
            editor.commit();
        }
*/
    }


}
