package com.example.lom;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lom.model.SearchType;
import com.example.lom.model.SiteFactory;
import com.example.lom.player.IPlayer;
import com.example.lom.player.PlayerService;
import com.example.lom.util.Chooser;
import com.example.lom.view.GenresAdapter;
import com.example.lom.view.PlayListAdapter;
import com.example.lom.view.TrackAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements FragmentHelper {
    private DrawerLayout drawer;
//    private IPlayer player;
//    public static IUpdate iUpdate;
    private TrackAdapter trackAdapter;
    private PlayListAdapter playListAdapter;
    private AppBarConfiguration mAppBarConfiguration;
    private SiteFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (isOnline() && factory == null) {
            createFactory(getTrackAdapter());
            ContextCompat.startForegroundService(this, new Intent(this, PlayerService.class));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_search)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

//        ((RadioGroup) findViewById(R.id.radio_group)).setOnCheckedChangeListener(this);
//        edit = findViewById(R.id.search_edit);

    }

    @Override
    public IPlayer getPlayer() {
        return MPlayer.getInstance();
//        return (IPlayer) getSupportFragmentManager().findFragmentById(R.id.fragment_player);
    }

    public boolean checkPermWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на ЗАПИСЬ В ХРАНИЛИЩЕ
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
            } else {
                Log.d(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isOnline() {
        return ((ConnectivityManager) Objects.requireNonNull(getSystemService(CONNECTIVITY_SERVICE))).getActiveNetwork() != null;
    }

    /*public boolean checkPermInternet() {
        if (Build.VERSION.SDK_INT >= 23) {
            //динамическое получение прав на INTERNET
            if (checkSelfPermission(android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission is granted");
            } else {
                Log.d(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
                return false;
            }
        }
        return true;
    }*/

    @Override
    public void closeDrawer() {
        drawer.closeDrawers();
    }

    @Override
    public void startChooseDir() {
        Chooser.choosePath(this);
    }

    @Override
    public void search() {
        factory.search();
    }

    @Override
    public void search(SearchType type, String searchStr) {
        factory.search(type, searchStr);
    }

    public void createFactory(IUpdate i) {
        factory = SiteFactory.init(i);
//        iUpdate = i;
        RecyclerView genres = findViewById(R.id.genres_box);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        layoutManager.setAlignItems(AlignItems.CENTER);

        genres.setLayoutManager(layoutManager);
        genres.setAdapter(new GenresAdapter(this, factory, this));

        RecyclerView playlist = findViewById(R.id.playlist_box);
        FlexboxLayoutManager layoutManager2 = new FlexboxLayoutManager(this);
        layoutManager2.setFlexWrap(FlexWrap.WRAP);
        layoutManager2.setFlexDirection(FlexDirection.ROW);
        layoutManager2.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager2.setAlignItems(AlignItems.FLEX_START);

        playlist.setLayoutManager(layoutManager2);
        playListAdapter = new PlayListAdapter(this, factory, this);
        playlist.setAdapter(playListAdapter);

//        return factory;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            System.out.println("requestCode = " + requestCode);
            if (requestCode == 9999) {
                Log.i("Test", "Result URI " + Objects.requireNonNull(data.getData()).getPath());
                Chooser.setDirToSave(data.getData().getPath());
//                Chooser.setDownloadPath((TextView) findViewById(R.id.download_path));
            }
        }
    }

    /*
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_DOWN)
        {
            AudioManager audio = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    // Volume up key detected
                    // Do something
                    setVolume(audio, ++volume, true);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    // Volume down key detected
                    // Do something
                    setVolume(audio, --volume, true);
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    public void setVolume(AudioManager audio, int val, boolean showScroll){
        if (showScroll) {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, val, 1); // устанавливаем громкость на С показом ползунка
        } else {
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, val, 0); // устанавливаем громкость на БЕЗ показа ползунка
        }
    }*/

    @Override
    public TrackAdapter getTrackAdapter() {
        if (trackAdapter == null)
            trackAdapter = new TrackAdapter(this, this);
        return trackAdapter;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnline() && factory == null) {
            createFactory(getTrackAdapter());
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("onBack");
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getGroupId() == R.id.action_settings) {
        } else if (item.getGroupId() == R.id.ru_music) {
            System.out.println("ru");
            SiteFactory.setCurrentSite(SiteFactory.Type.RU_MUSIC);
            playListAdapter.setList(SiteFactory.getCurrentSite());
        } else if(item.getGroupId() == R.id.sefone) {
            System.out.println("sef");
            SiteFactory.setCurrentSite(SiteFactory.Type.SEFON);
            playListAdapter.setList(SiteFactory.getCurrentSite());
        } else {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }
        return false;
        /*
        if (item.getItemId() == R.id.action_settings)
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);*/
    }
}