package com.example.lom;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.example.lom.model.vo.Track;
import com.example.lom.player.IPlayer;

import java.io.IOException;
import java.util.List;

public class MPlayer implements IPlayer {
    private static MPlayer mPlayer;
    private final MediaPlayer player;
    private boolean prepared, started;
    private List<Track> playList;
    private int currentTrackNumber;
    private TrackStateListener stateListener;

    private MPlayer() {
        player = new android.media.MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder()
//                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED) // із за сього ни мігим звук регулювати
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

//        player.setOnCompletionListener(mp -> completed = true);
        player.setOnPreparedListener(this::onPrepared);
    }

    @Override
    public void playTrackByPosition(int position) {
        System.out.println("play");
        if (position == currentTrackNumber) {
            if (player.isPlaying()) pause();
            else continuePlay();
        }

        if (position > playList.size() - 1) position = 0;
        else if (position < 0) position = playList.size() - 1;
        currentTrackNumber = position;
        play(playList.get(position));
//        currentTrackNumber = position;
    }

    private void play(Track track) {
        stateListener.setTrackName(String.format("%s - %s", track.getSinger(), track.getTitle()));

        reset();
        System.out.println("position = X, track name = " + track.getTitle() + ", track url = " + track.getUrlToLoading());

        try {
            player.setDataSource(track.getUrlToLoading());
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopTrack() {
        player.pause();
    }

    @Override
    public void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    public void continuePlay() {
        player.start();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    private void reset() {
        if (started) {
            player.stop();
        }
        started = false;
        player.reset();
        prepared = false;
    }

    public static MPlayer getInstance() {
        if (mPlayer == null) mPlayer = new MPlayer();

        return mPlayer;
    }

    @Override
    public void rewind(int newProgress) {
        if (started)
            player.seekTo(newProgress);
    }

    @Override
    public void setList(List<Track> tracks) {
        playList = tracks;
    }

    @Override
    public Track getCurrentTrack() {
        return playList.get(currentTrackNumber);
    }

    @Override
    public void setTrackStateListener(TrackStateListener trackStateListener) {
        this.stateListener = trackStateListener;
    }

    private void onPrepared(MediaPlayer mp) {
        prepared = true;
        mp.start();
        started = true;
        if (stateListener != null) {
            stateListener.setDuration(player.getDuration());
            stateListener.setProgress(0);
            new SeekBarRunTask(player, stateListener, currentTrackNumber, player.getDuration()).execute();
        } else System.out.println("MPlayer stateListener is not found");
    }

    /**
     * --------------
     */
    static class SeekBarRunTask extends AsyncTask<Integer, Void, Boolean> {
        private final MediaPlayer player;
        private final TrackStateListener stateListener;
        private final int currentTrackNumber, duration;

        public SeekBarRunTask(MediaPlayer player, TrackStateListener stateListener, int currentTrackNumber, int duration) {
            this.player = player;
            this.stateListener = stateListener;
            this.currentTrackNumber = currentTrackNumber;
            this.duration = duration;
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
            int currentPosition = 0;
            while (player.isPlaying()) {
                currentPosition = player.getCurrentPosition();
                try {
                    Thread.sleep(100);
                    setProgress();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("duration = " + duration + ", and current = " + currentPosition);
            return duration == currentPosition || duration < (currentPosition + 512);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            System.out.println(bool);
//            player.reset(); сучка єбав мозги
            if (bool) stateListener.trackIsOver();
        }

        public void setProgress() {
            stateListener.setProgress(player.getCurrentPosition());
        }
    }
}
