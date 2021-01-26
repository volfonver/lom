package com.example.lom.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lom.FragmentHelper;
import com.example.lom.R;
import com.example.lom.TrackStateListener;
import com.example.lom.model.SearchType;
import com.example.lom.player.PlayerReceiver;

import java.util.Objects;

public class SearchFragment extends Fragment implements TrackStateListener, TextView.OnEditorActionListener, View.OnClickListener,
        View.OnScrollChangeListener, SeekBar.OnSeekBarChangeListener {
    private int scrollingBottom = 0;
    private View root;
    private EditText edit;
    private FragmentHelper fragmentHelper;
    private SeekBar seekBar;
    private TextView trackName;
    private TrackAdapter adapter;
    private View seekBarView, searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root != null) return root;

        root = inflater.inflate(R.layout.fragment_search, container, false);
        root.findViewById(R.id.search_button).setOnClickListener(this);
        trackName = root.findViewById(R.id.name_track);

        edit = root.findViewById(R.id.search_edit);
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        edit.setOnEditorActionListener(this);

//        searchView = root.findViewById(R.id.search_view);
        seekBarView = root.findViewById(R.id.seek_bar_view);
        seekBar = root.findViewById(R.id.progress_track);
        seekBar.setOnSeekBarChangeListener(this);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_music);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = fragmentHelper.getTrackAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollChangeListener(this);

        fragmentHelper.getPlayer().setTrackStateListener(this);
        if (fragmentHelper.isOnline()) fragmentHelper.search();
        else Toast.makeText(getContext(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();

        return root;
    }

    private void search() {
        if (edit.getText().toString().length() > 2) {
            if (fragmentHelper.isOnline()) {
                ((InputMethodManager) Objects.requireNonNull(requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)))
                        .hideSoftInputFromWindow(edit.getWindowToken(), 0);
                edit.clearFocus();
                fragmentHelper.search(SearchType.ALL, edit.getText().toString());
            } else {
                Toast.makeText(getContext(), "Нет подключения к интернету", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(getContext(), "Слишком короткое название исполнителя", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentHelper = (FragmentHelper) context;
    }

    @Override
    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    @Override
    public void setDuration(int duration) {
        seekBar.setMax(duration);
    }

    @Override
    public void setTrackName(String track) {
        trackName.setText(track);
    }

    @Override
    public void trackIsOver() {
        adapter.nextTrack();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(PlayerReceiver.NEXT));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // обрабатываем нажатие кнопки поиска
            search();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_button) search();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int y = (int) (oldScrollY * 0.4);
        if (seekBarView.getHeight() * -2 < scrollingBottom + y && 0 > scrollingBottom + y) {
            scrollingBottom += y;
//            System.out.println(scrollingBottom + " -- " + y);
            seekBarView.scrollBy(0, y);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        System.out.println(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        System.out.println("onStartTrackingTouch");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        fragmentHelper.getPlayer().rewind(seekBar.getProgress()); // перемотка
    }
}
