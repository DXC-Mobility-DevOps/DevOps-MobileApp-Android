package com.hpe.devops.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hpe.devops.entity.ProjectDetails;
import com.hpe.devops.R;
import com.hpe.devops.utility.NetworkHelper;
import com.hpe.devops.utility.ResponseHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView projectList;
    private TextView mainScreenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUI();
        refreshActivity();
    }

    protected void refreshActivity() {
        new LoadProjectDetails().execute();
    }

    private void setupUI() {
        projectList = (RecyclerView) findViewById(R.id.project_list);
        mainScreenText = (TextView) findViewById(R.id.main_screen_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoadProjectDetails extends AsyncTask<Void, Void, Void> {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        boolean networkAvailable = true;

        @Override
        protected Void doInBackground(Void... params) {
            if (NetworkHelper.isNetworkAvailable(getApplicationContext())) {
                projectDetailsList = ResponseHelper.getProjectData(); // ProjectDetails.getDummyData();
            } else {
                networkAvailable = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            projectList.setVisibility(View.GONE);
            mainScreenText.setVisibility(View.VISIBLE);
            mainScreenText.setText(getString(R.string.please_wait_message));
        }

        @Override
        protected void onPostExecute(Void aLong) {
            super.onPostExecute(aLong);
            if (!networkAvailable) {
                projectList.setVisibility(View.GONE);
                mainScreenText.setVisibility(View.VISIBLE);
                mainScreenText.setText(getString(R.string.network_not_available_message));
            } else {
                if (projectDetailsList != null && projectDetailsList.size() > 0) {
                    projectList.setVisibility(View.VISIBLE);
                    mainScreenText.setVisibility(View.GONE);
                    projectList.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false));
                    ProjectListAdapter projectListAdapter = new ProjectListAdapter(projectDetailsList, HomeActivity.this);
                    projectList.setAdapter(projectListAdapter);
                } else {
                    projectList.setVisibility(View.GONE);
                    mainScreenText.setVisibility(View.VISIBLE);
                    mainScreenText.setText(getString(R.string.no_project_message));
                }
            }
        }
    }

}
