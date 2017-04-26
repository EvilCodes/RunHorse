package com.yujie.hero.tasks.examorexercise;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.yujie.hero.Injection;
import com.yujie.hero.R;
import com.yujie.hero.data.source.local.TasksLocalDataSource;
import com.yujie.hero.data.source.remote.TasksRemoteDataSource;
import com.yujie.hero.utils.AddFragmentToActivity;

/**
 * Created by Administrator on 2017/4/24.
 */

public class EorEActivity extends AppCompatActivity {
    private FragmentManager manager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        manager = getSupportFragmentManager();
        EorEFragment fragmentGame = (EorEFragment) manager.findFragmentById(R.id.fmGame);
        if (fragmentGame == null) {
            fragmentGame = new EorEFragment();
        }
        new EorEPresenter(Injection.provideTasksRepository(this), TasksRemoteDataSource.getInstance(), fragmentGame);
        AddFragmentToActivity.addFragmentToActivity(manager,R.id.fmGame,fragmentGame);

    }
}
