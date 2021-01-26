package com.example.lom.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lom.MPlayer;
import com.example.lom.MainActivity;
import com.example.lom.model.vo.Track;
import com.example.lom.util.Loader;

public class PlayerReceiver extends BroadcastReceiver {
    public static final String NEXT = "player.next";
    public static final String PREVIEW = "player.preview";
    public static final String PAUSE = "player.pause";
    public static final String SAVE = "player.save";

    private PlayerService service = PlayerService.playerService;
    private Track track;

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(context.getClass().getSimpleName());
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case SAVE:
                    if (track != null)
                        Loader.download(context, track);
                    break;
                case NEXT:
//                        track = MainActivity.player.next();
//                    track = MainActivity.iUpdate.nextTrack();
//                    service.updatePlayer(track);
//                    service.isPlaying = true;
//                    break;
                case PREVIEW:
//                        track = MainActivity.player.preview();
//                    track = MainActivity.iUpdate.previewTrack();
//                    service.updatePlayer(track);
//                    service.isPlaying = true;
//                    break;
                case PAUSE:
//                    if (service.isPlaying) {
//                        MainActivity.player.pause();
//                            PlayerService.playerService.manager.cancel(1);
//                        service.isPlaying = false;
//                    } else {
//                        MainActivity.player.continuePlay();
//                        service.isPlaying = true;
//                    }
                    track = MPlayer.getInstance().getCurrentTrack();
                    service.updatePlayer(track);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + intent.getAction());
            }
        }
        System.out.println(intent.getAction());
    }
}
