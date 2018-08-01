package com.abc.xyz.os10.controllers;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.developer.QuickActionBar.ActionItem;
import android.developer.QuickActionBar.QuickAction;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.entities.SongEntity;
import com.abc.xyz.os10.views.MyTextView;
import com.abc.xyz.os10.views.MyTextViewBold;
import com.abc.xyz.services.lockscreenios9.MusicPlayerService;
import com.abc.xyz.tasks.LoadSongSdcardTask;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.TimeUtil;
import com.abc.xyz.utils.logs.LogUtil;

import java.util.ArrayList;

public class PagerControlMusic {
	private View mView;
	private Context mContext;
	private ImageView ivPagerMusicIcon;
	private MyTextViewBold tvPagerMusicTitle;
	private MyTextView tvPagerMusicAuthor;
	private SeekBar skbPagerMusicPlay;
	private TextView tvPagerMusicStart;
	private TextView tvPagerMusicStop;
	private ImageView ivPagerMusicRepeat;
	private ImageView ivPagerMusicPrev;
	private ImageView ivPagerMusicPlayPause;
	private ImageView ivPagerMusicNext;
	private ImageView ivPagerMusicListMusic;
	private SeekBar skbPagerMusicVolume;
	private String TAG = "PagerControlMusic";
	private MusicPlayerService mMusicPlayerService;
	private AudioManager audioManager;
	private Handler durationHandler = new Handler();
	private ArrayList<SongEntity> mSongEntityArrayList = new ArrayList<>();
	int count = 0;
	public static boolean  firstrun = true;
	private long totalDuration;
	private long currentDuration;
	public ServiceConnection musicConnection = new ServiceConnection() {
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

	public void openLayout() {
		if (SharedPreferencesUtil.isRepeatMusic(mContext)) {
			ivPagerMusicRepeat.setSelected(true);
		}
		updateMusicList();

		LockScreenController.getInstance(mContext).runMusicService(musicConnection);
		audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		skbPagerMusicVolume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		skbPagerMusicVolume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		// setup music service
	}


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
					tvPagerMusicStop.setText("" + TimeUtil.milliSecondsToTimer(totalDuration));
					tvPagerMusicStart.setText("" + TimeUtil.milliSecondsToTimer(currentDuration));

					int progress = TimeUtil.getProgressPercentage(currentDuration, totalDuration);

					skbPagerMusicPlay.setProgress(progress);
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

	private void updateTitleAndArtistSong() {
		if (mMusicPlayerService != null) {
			tvPagerMusicTitle.setText(mMusicPlayerService.getSongTitle());
			tvPagerMusicAuthor.setText(mMusicPlayerService.getArtistName());
			if (mMusicPlayerService.isPng()) {
				ivPagerMusicPlayPause.setSelected(true);
			} else {
				ivPagerMusicPlayPause.setSelected(false);
			}
		}

	}

	public PagerControlMusic(View view, Context context) {
		mView = view;
		mContext = context;
		findViews();
	}

	private void openListMusic() {
		QuickAction quickAction = new QuickAction(mContext, QuickAction.VERTICAL_STYLE3);
		quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				firstrun = false;
				if(mMusicPlayerService!=null) {
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
			if (titleSong!=null && titleSong.length() > 25) {
				titleSong = titleSong.substring(0, 20) + "...";
			}
			if (artistName!=null && artistName.length() > 15) {
				artistName = artistName.substring(0, 13) + "...";
			}

			String title = titleSong + " - " + artistName;
			ActionItem Item = new ActionItem(1, title, "#363636", mContext.getResources().getDrawable(R.drawable.ic_note_music));
			quickAction.addActionItem(Item);
		}
		quickAction.show(ivPagerMusicListMusic);
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



	private void updateProgressBar() {
		durationHandler.postDelayed(updateTimeTask, 100);
	}
	//connect to the service



	private void findViews() {
		ivPagerMusicIcon = (ImageView) mView.findViewById(R.id.iv_pager_music_icon);
		tvPagerMusicTitle = (MyTextViewBold) mView.findViewById(R.id.tv_pager_music_title);
		tvPagerMusicAuthor = (MyTextView) mView.findViewById(R.id.tv_pager_music_author);
		skbPagerMusicPlay = (SeekBar) mView.findViewById(R.id.skb_pager_music_play);
		tvPagerMusicStart = (TextView) mView.findViewById(R.id.tv_pager_music_start);
		tvPagerMusicStop = (TextView) mView.findViewById(R.id.tv_pager_music_stop);
		ivPagerMusicRepeat = (ImageView) mView.findViewById(R.id.iv_pager_music_repeat);
		ivPagerMusicPrev = (ImageView) mView.findViewById(R.id.iv_pager_music_prev);
		ivPagerMusicPlayPause = (ImageView) mView.findViewById(R.id.iv_pager_music_play_pause);
		ivPagerMusicNext = (ImageView) mView.findViewById(R.id.iv_pager_music_next);
		ivPagerMusicListMusic = (ImageView) mView.findViewById(R.id.iv_pager_music_list_music);
		skbPagerMusicVolume = (SeekBar) mView.findViewById(R.id.skb_pager_music_volume);
		skbPagerMusicVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

		skbPagerMusicPlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

		ivPagerMusicIcon.setOnClickListener(onMusicClickedListener);
		ivPagerMusicRepeat.setOnClickListener(onMusicClickedListener);
		ivPagerMusicPrev.setOnClickListener(onMusicClickedListener);
		ivPagerMusicPlayPause.setOnClickListener(onMusicClickedListener);
		ivPagerMusicNext.setOnClickListener(onMusicClickedListener);
		ivPagerMusicListMusic.setOnClickListener(onMusicClickedListener);

	}

	private boolean isStop = false;
	public View.OnClickListener onMusicClickedListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mSongEntityArrayList == null || mSongEntityArrayList.size() == 0) {
				MyToast.showToast(mContext, "No songs found");
				ivPagerMusicPlayPause.setSelected(false);
				return;
			}
			if (v == ivPagerMusicPrev) {
				if (mMusicPlayerService != null)
					mMusicPlayerService.playPrev();
				isStop = false;
				ivPagerMusicPlayPause.setSelected(true);
			} else if (v == ivPagerMusicNext) {
					if (mMusicPlayerService != null)
						mMusicPlayerService.playNext();
				ivPagerMusicPlayPause.setSelected(true);
				isStop = false;
			} else if (v == ivPagerMusicPlayPause) {
				if (mMusicPlayerService != null) {
					if (ivPagerMusicPlayPause.isSelected()) {
						ivPagerMusicPlayPause.setSelected(false);
						mMusicPlayerService.pausePlayer();
						isStop = true;
					} else {
						ivPagerMusicPlayPause.setSelected(true);
						if (firstrun) {
							mMusicPlayerService.playSong();
						} else {
							mMusicPlayerService.go();
						}
						isStop = false;
					}
				}
			} else if (ivPagerMusicRepeat == v) {
				if (ivPagerMusicRepeat.isSelected()) {
					ivPagerMusicRepeat.setSelected(false);
					SharedPreferencesUtil.setRepeatMusic(mContext, false);
				} else {
					ivPagerMusicRepeat.setSelected(true);
					SharedPreferencesUtil.setRepeatMusic(mContext, true);
				}
				return;
			} else if (ivPagerMusicListMusic == v) {
				openListMusic();
				return;
			}
			firstrun = false;
		}


	};

	public String getTime(int time) {
		String t = "";
		time /= 1000;
		if (time / 3600 > 0) {
			t += (String.format("%02d", time / 3600) + ":");
			time = time % 3600;
		}
		t += (String.format("%02d", time / 60) + ":");
		time = time % 60;
		t += String.format("%02d", time);
		return t;
	}

	public void stopMusic() {
		firstrun = true;
		if (mMusicPlayerService != null) {
			ivPagerMusicPlayPause.setSelected(false);
			mMusicPlayerService.pausePlayer();
		}
		mMusicPlayerService = null;
		LockScreenController.getInstance(mContext).stopMusicService(musicConnection);
	}
}
