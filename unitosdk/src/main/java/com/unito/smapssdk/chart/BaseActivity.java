package com.unito.smapssdk.chart;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

abstract public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected final String TAG = this.getClass().getSimpleName();

    /**
     * Returns the resource id of the layout which will be used for setContentView() for the Activity
     *
     * @return resource id of the xml layout
     */
    public abstract int defineLayoutResource();

    /**
     * Initialize the components on activity create
     */
    protected abstract void initializeComponents();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(defineLayoutResource());
        if (savedInstanceState == null)
            initializeComponents();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

}
