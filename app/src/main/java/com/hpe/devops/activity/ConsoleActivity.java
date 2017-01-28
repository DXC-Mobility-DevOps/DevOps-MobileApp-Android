package com.hpe.devops.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.hpe.devops.R;
import com.hpe.devops.entity.EndPoint;
import com.hpe.devops.entity.ProjectStatus;

public class ConsoleActivity extends AppCompatActivity {

    WebView mConsoleOutputWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        setupToolBar();
        setupWebView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupToolBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            String titleString = getIntent().getStringExtra(ProjectStatus.BUILD_NAME);
            if (titleString != null && titleString.length() > 0) {
                supportActionBar.setTitle(titleString);
            }
        }
    }

    private void setupWebView() {
        mConsoleOutputWebView = (WebView) findViewById(R.id.console_output_web_view);
        String consoleOutputURL = getIntent().getStringExtra(ProjectStatus.BUILD_CONSOLE_URL);
        mConsoleOutputWebView.loadUrl(EndPoint.getBuildConsoleLogURL(consoleOutputURL));
        WebSettings settings = mConsoleOutputWebView.getSettings();
        settings.setTextZoom(settings.getTextZoom() - 50);
    }
}
