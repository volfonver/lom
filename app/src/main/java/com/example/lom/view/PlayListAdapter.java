package com.example.lom.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lom.FragmentHelper;
import com.example.lom.R;
import com.example.lom.model.SearchType;
import com.example.lom.model.SiteFactory;
import com.example.lom.sites.MusicSite;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private static List<String> list;
    private LayoutInflater inflater;
    private ViewHolder currentHolder;
    private SiteFactory factory;
    private FragmentHelper helper;

    public PlayListAdapter(Context context, SiteFactory factory, FragmentHelper helper) {
        list = new ArrayList<>(SiteFactory.getCurrentSite().getListOfAlbum());
        inflater = LayoutInflater.from(context);
        this.factory = factory;
        this.helper = helper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_genres, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull final PlayListAdapter.ViewHolder holder, int position) {
        holder.text.setText(list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text_in_flex);
            cardView = itemView.findViewById(R.id.card);
        }
    }

    public void selectItem(ViewHolder holder) {
        if (helper.isOnline()) {
            if (currentHolder != null)
                currentHolder.cardView.setCardBackgroundColor(inflater.getContext().getResources().getColor(R.color.white, null));

            factory.search(SearchType.ALBUM, holder.text.getText().toString());
            currentHolder = holder;
            helper.closeDrawer();
            holder.cardView.setCardBackgroundColor(inflater.getContext().getResources().getColor(R.color.dark_green_tea, null));
        } else Toast.makeText(inflater.getContext(), "Нет доступа в интернет", Toast.LENGTH_LONG).show();
    }

    public void setList(MusicSite site) {
        list = site.getListOfAlbum();
        notifyDataSetChanged();
    }
}
