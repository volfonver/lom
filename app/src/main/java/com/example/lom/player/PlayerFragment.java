package com.example.lom.player;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lom.R;
import com.example.lom.TrackStateListener;
import com.example.lom.model.vo.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment implements IPlayer/*, MediaPlayer.OnPreparedListener*/ {
    public AsyncTask<Integer, Void, Boolean> taskBar;
    private MediaPlayer player;
    private boolean prepared, started, playing;
    private int numTrack;
    private List<Track> trackList = new ArrayList<>();
    private TextView nameTrack;
    private SeekBar seekBar;
//    private int seek
    private View root;
//    private FragmentHelper helper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) return root;

        root = inflater.inflate(R.layout.fragment_player, container, false);
        nameTrack = root.findViewById(R.id.name_track);
        seekBar = root.findViewById(R.id.progress_track);

        /*root.findViewById(R.id.save_track).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loader.download(getContext(), trackList.get(numTrack));
            }
        });*/

        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder()
//                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED) // із за сього ни мігим звук регулювати
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                taskBar = new SeekBarRunTask(player){

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                        player.reset();
                        if (bool) {
                           /* try {
                                System.out.println("fragment.next");
//                                PendingIntent.getBroadcast(getContext(), 99, new Intent(PlayerService.PlayerReceiver.NEXT), 0).send(99);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }*/
                        }
                    }

                    public void setProgress() {
                        seekBar.setProgress(player.getCurrentPosition());
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mp.getDuration());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress()); // перемотка
            }
        });

        /* запрет перетаскивания
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });*/

        return root;
    }

    @Override
    public void playTrackByPosition(int position) {
        Track track = trackList.get(position);
        if (player.isPlaying()) stopTrack();
        nameTrack.setText(String.format("%s - %s", track.getSinger(), track.getTitle()));
        numTrack = position;

        reset();
//        visiblePlayer(true);
        System.out.println("position = " + position + ", track name = " + track.getTitle() + ", track url = " + track.getUrlToLoading());
//        System.out.println("started = " + started + ", prepared = " + prepared);

        try {
            player.setDataSource(track.getUrlToLoading());
            player.prepareAsync();
            seekBar.setMax(track.getDuration());
            seekBar.setProgress(0);
            PlayerService.playerService.updatePlayer(track);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play(Track track) {
        if (player.isPlaying()) stopTrack();
        nameTrack.setText(String.format("%s - %s", track.getSinger(), track.getTitle()));

        reset();
//        visiblePlayer(true);
        System.out.println("position = X, track name = " + track.getTitle() + ", track url = " + track.getUrlToLoading());
//        System.out.println("started = " + started + ", prepared = " + prepared);

        try {
            player.setDataSource(track.getUrlToLoading());
            player.prepareAsync();
            seekBar.setMax(track.getDuration());
            seekBar.setProgress(0);
            PlayerService.playerService.updatePlayer(track);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Track getCurrentTrack() {
        return null;
    }

    @Override
    public void stopTrack() {
        player.pause();
        if (!taskBar.isCancelled())
            taskBar.cancel(true);
    }
/*

    @Override
    public Track next() {
        playTrackByPosition(numTrack + 1);
        MainActivity.iUpdate.selectItem(numTrack);
        playing = true;
        return trackList.get(numTrack);
    }

    @Override
    public Track preview() {
        playTrackByPosition(numTrack - 1);
        MainActivity.iUpdate.selectItem(numTrack);
        playing = true;
        return trackList.get(numTrack);
    }
*/

    @Override
    public void pause() {
        if (player.isPlaying()){
//            MainActivity.iUpdate.selectItem(numTrack);
            player.pause();
            playing = false;
        }
    }

    @Override
    public void continuePlay() {
        playing = true;
//        MainActivity.iUpdate.selectItem(numTrack);
        player.start();
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

    @Override
    public void rewind(int newProgress) {

    }

    @Override
    public void setTrackStateListener(TrackStateListener listener) {

    }

    @Override
    public void setList(List<Track> tracks) {

    }

    abstract static class SeekBarRunTask extends AsyncTask<Integer, Void, Boolean> {
        private MediaPlayer player;

        public SeekBarRunTask(MediaPlayer player) {
            this.player = player;
        }

        @Override
        protected Boolean doInBackground(Integer... durations) {
            boolean isEnd = false; // ??? тре го вопше?
            while (player.getCurrentPosition() < durations[0] - 500) {
//                System.out.println(player.getCurrentPosition() + " --- " + durations[0]);
                try {
                    Thread.sleep(100);
                    setProgress();
                    if (!isEnd) isEnd = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return isEnd;
        }
        abstract void setProgress();
    }

    /*public void visiblePlayer(boolean isVisible) {
        root.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }*/
/*
    public void setProgress() {
        seekBar.setProgress(player.getCurrentPosition());
    }*/

   /* private void start() {
        if (prepared && !started) {
            player.start();
            started = true;
        }
    }

    private void pause() {
        if (started) {
            player.pause();
            started = false;
        }
    }*/

    private void reset() {
        if (taskBar != null) taskBar.cancel(true);
        if (started) {
            player.stop();
        }
        prepared = false;
        started = false;
        player.reset();
    }


/*
    public static void setVolume(AudioManager audio, int val, boolean showScroll) {
        if (showScroll) {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, val, 1); // устанавливаем громкость на С показом ползунка
        } else {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0); // устанавливаем громкость на БЕЗ показа ползунка
        }
    }

*/
/*
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         helper = (FragmentHelper) context;
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        if (prepared && !started) {
            player.start();
            started = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (prepared) player.release();
        System.out.println("onDestroy");
        root = null;
    }
}
