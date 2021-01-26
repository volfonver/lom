package com.example.lom.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lom.util.Chooser;
import com.example.lom.FragmentHelper;
import com.example.lom.IUpdate;
import com.example.lom.util.Loader;
import com.example.lom.R;
import com.example.lom.model.vo.Track;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.Holder> implements IUpdate {
    protected List<Track> tracks;
    protected FragmentHelper helper;
    protected Context context;
    private int currentPosition;

    private boolean isUpdating;

    public TrackAdapter(Context context, FragmentHelper helper) {
        this.context = context;
        this.helper = helper;
        tracks = new LinkedList<>();
        currentPosition = -1;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_of_track_info, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size() > 0) holder.itemView.performClick();
        else super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        final Track info = tracks.get(position);
        holder.nameOfMusic.setText(info.getTitle());
        holder.signer.setText(info.getSinger());
        Glide.with(context).load(info.getUrlImage()).circleCrop().into(holder.imageView);
        holder.highlight(currentPosition == position);
    }

    public void initDialog(final Track track) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_view);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // Футкция окна
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView dir = dialog.findViewById(R.id.download_path);
        dir.setText(Chooser.getDirToSave());
        dir.setOnClickListener(v -> {
            helper.startChooseDir();
            if (Chooser.dirToSaveInDownloads.equals(Chooser.getDirToSave()))
                dir.setText("в папку 'Загрузки'");
            dialog.dismiss();
        });

        dialog.findViewById(R.id.loading)
                .setOnClickListener(v -> {
                    dialog.dismiss();
                    Loader.download(context, track);
                });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    protected class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView nameOfMusic;
        private final TextView signer;
        private final CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageOfMusic);
            nameOfMusic = itemView.findViewById(R.id.nameOfMusic);
            signer = itemView.findViewById(R.id.signer);
            cardView = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(v -> {
                if (helper.checkPermWriteExternalStorage())
                    initDialog(tracks.get(getAdapterPosition()));
                return true;
            });
        }

        public void highlight(boolean bool) {
            cardView.setCardBackgroundColor(context.getResources().getColor(bool ? R.color.dark_green_tea : R.color.white, null));
        }

        @Override
        public void onClick(View v) {
            if (isUpdating) {
                helper.getPlayer().setList(tracks);
                isUpdating = false;
            }

            int oldPosition = currentPosition;
            helper.getPlayer().playTrackByPosition(getAdapterPosition());
            currentPosition = getAdapterPosition();

            v.post(() -> {
                notifyItemChanged(currentPosition);
                notifyItemChanged(oldPosition);
            });
        }
    }

    @Override
    public void updateList(List<Track> list) {
        tracks = list;
        currentPosition = -1;
        notifyDataSetChanged();
        isUpdating = true;
    }

    @Override
    public Track nextTrack() {
        int numTrack = currentPosition + 1;
        if (numTrack == tracks.size()) numTrack = 0;
        notifyItemChanged(numTrack, 1);
        System.out.println("adapter.next");
        return tracks.get(numTrack);
    }

    @Override
    public Track previewTrack() {
        int numTrack = currentPosition - 1;
        if (numTrack == -1) numTrack = tracks.size() - 1;
        notifyItemChanged(numTrack, -1);
        System.out.println("adapter.preview");
        return tracks.get(numTrack);
    }
}
