package com.swapnil.mvvmstarterkit;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.swapnil.mvvmstarterkit.ui.dashboard.DashboardFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dashboardActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_left_option)
    TextView tvLeftOption;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_right_option)
    TextView tvRightOption;
    @BindView(R.id.pb_scanning)
    ProgressBar pbScanning;
    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        ButterKnife.bind(this);
        tvToolbarTitle.setText("Dashboard");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance())
                    .commitNow();
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
    }
}
