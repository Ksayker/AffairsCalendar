package ksayker.affairscalendar.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import ksayker.affairscalendar.R;
import ksayker.affairscalendar.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi(savedInstanceState);
    }

    private void initUi(Bundle savedInstanceState){
        if (savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main_rl_fragment_holder,
                    MainFragment.newInstance(), MainFragment.FRAGMENT_TAG);
            transaction.commit();
        }

        View viewActionBar = getLayoutInflater().inflate(
                R.layout.application_title, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER);
            actionBar.setCustomView(viewActionBar, params);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
