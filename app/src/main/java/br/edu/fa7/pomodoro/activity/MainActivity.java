package br.edu.fa7.pomodoro.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private final MainFragment mMainFragment;

    public MainActivity() {
        this.mMainFragment = new MainFragment();
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content, mMainFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        switch (id){
            case android.R.id.home:
                fragmentTransaction.replace(R.id.main_content, mMainFragment);
                break;
        }

        fragmentTransaction.commit();
        return true;
    }

}
