package com.hpe.devops.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hpe.devops.entity.EndPoint;
import com.hpe.devops.entity.ProjectDetails;
import com.hpe.devops.entity.ProjectStatus;
import com.hpe.devops.R;
import com.hpe.devops.utility.NetworkHelper;
import com.hpe.devops.utility.ViewAnimationHelper;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import az.plainpie.PieView;

public class ProjectListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mProjectName;
    PieView mCodeCoverage;
    TextView mBuildStatus;
    TextView mVersionNumber;
    TextView mBuildDate;
    TextView mToggleText;
    TextView mTriggerNewBuild;
    View mExpandedLayout;
    View mCurrentBuildProgressContainer;
    StateProgressBar mCurrentBuildProgress;
    TextView mProjectHealthMessage;
    PieView mBuildHealthPercentage;
    TextView mBuildTriggeredBy;
    View mAuthorNameContainer;
    TextView mAuthorName;
    View mAuthorEmailContainer;
    TextView mAuthorEmail;
    View mCommitCommentContainer;
    TextView mCommitComment;
    View mCommitDateContainer;
    TextView mCommitDate;
    View mDatasetChangesContainer;
    TextView mDatasetChanges;
    TextView mProjectUrl;
    View mBuildLogCTA;
    String buildLog;
    View mProdBuildContainer;
    TextView mProdBuildNumber;
    String prodBuildLog;
    View mSuccessfulBuildContainer;
    TextView mSuccessfulBuildNumber;
    String successfulBuildLog;
    View mFailedBuildContainer;
    TextView mFailedBuildNumber;
    String failedBuildLog;

    Context context;
    HomeActivity homeActivity;

    public ProjectListViewHolder(View itemView) {
        super(itemView);
        mProjectName = (TextView) itemView.findViewById(R.id.project_name);
        mCodeCoverage = (PieView) itemView.findViewById(R.id.pv_code_coverage);
        mBuildStatus = (TextView) itemView.findViewById(R.id.build_status);
        mVersionNumber = (TextView) itemView.findViewById(R.id.version_number);
        mBuildDate = (TextView) itemView.findViewById(R.id.build_date);
        mToggleText = (TextView) itemView.findViewById(R.id.toggle_text);
        mToggleText.setOnClickListener(this);
        mTriggerNewBuild = (TextView) itemView.findViewById(R.id.trigger_new_build);
        mTriggerNewBuild.setOnClickListener(this);
        mExpandedLayout = itemView.findViewById(R.id.expanded_layout);
        mCurrentBuildProgressContainer = itemView.findViewById(R.id.current_build_progress_container);
        mCurrentBuildProgress = (StateProgressBar) itemView.findViewById(R.id.current_build_progress);
        String[] buildDescription = {ProjectStatus.BUILD_PROGRESS_REPO, ProjectStatus.BUILD_PROGRESS_BUILDING, ProjectStatus.BUILD_PROGRESS_UNIT_TEST_CASE_CODE_COVERAGE, ProjectStatus.BUILD_PROGRESS_STATUS};
        mCurrentBuildProgress.setStateDescriptionData(buildDescription);
        mProjectHealthMessage = (TextView) itemView.findViewById(R.id.project_health_message);
        mBuildHealthPercentage = (PieView) itemView.findViewById(R.id.pv_project_health);
        mBuildTriggeredBy = (TextView) itemView.findViewById(R.id.build_triggered_by);
        mAuthorNameContainer = itemView.findViewById(R.id.author_name_container);
        mAuthorName = (TextView) itemView.findViewById(R.id.author_name);
        mAuthorEmailContainer = itemView.findViewById(R.id.author_email_container);
        mAuthorEmail = (TextView) itemView.findViewById(R.id.author_email);
        mCommitCommentContainer = itemView.findViewById(R.id.commit_comment_container);
        mCommitComment = (TextView) itemView.findViewById(R.id.commit_comment);
        mCommitDateContainer = itemView.findViewById(R.id.commit_date_container);
        mCommitDate = (TextView) itemView.findViewById(R.id.commit_date);
        mDatasetChangesContainer = itemView.findViewById(R.id.dataset_changes_container);
        mDatasetChanges = (TextView) itemView.findViewById(R.id.dataset_changes);
        mBuildLogCTA = itemView.findViewById(R.id.build_log_cta);
        mBuildLogCTA.setOnClickListener(this);
        mProjectUrl = (TextView) itemView.findViewById(R.id.project_url);
        mProdBuildContainer = itemView.findViewById(R.id.prod_build_container);
        mProdBuildContainer.setOnClickListener(this);
        mProdBuildNumber = (TextView) itemView.findViewById(R.id.prod_build_number);
        mSuccessfulBuildContainer = itemView.findViewById(R.id.successful_build_container);
        mSuccessfulBuildContainer.setOnClickListener(this);
        mSuccessfulBuildNumber = (TextView) itemView.findViewById(R.id.successful_build_number);
        mFailedBuildContainer = itemView.findViewById(R.id.failed_build_container);
        mFailedBuildContainer.setOnClickListener(this);
        mFailedBuildNumber = (TextView) itemView.findViewById(R.id.failed_build_number);
    }

    public void bindView(final ProjectDetails projectDetail, final Context context, final HomeActivity homeActivity) {
        this.context = context;
        this.homeActivity = homeActivity;
        setProjectDetails(projectDetail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do Nothing
            }
        });
    }

    private void setProjectDetails(ProjectDetails projectDetail) {
        // Project header
        mProjectName.setText(projectDetail.projectName);

        // Code coverage details
        if (projectDetail.codeCoverage != null) {
            float codeCoveragePercentage = Float.parseFloat(projectDetail.codeCoverage);
            mCodeCoverage.setInnerText(projectDetail.codeCoverage);
            mCodeCoverage.setPercentage(codeCoveragePercentage);
            mCodeCoverage.setPercentageBackgroundColor(getPercentageColor(codeCoveragePercentage));
            mCodeCoverage.setMainBackgroundColor(context.getResources().getColor(android.R.color.white));
            mCodeCoverage.setTextColor(context.getResources().getColor(android.R.color.black));
            mCodeCoverage.setInnerBackgroundColor(context.getResources().getColor(R.color.codeCoverageColor));
        } else {
            mCodeCoverage.setVisibility(View.GONE);
        }

        // Project details
        // Build status
        if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.BLUE)) {
            mBuildStatus.setText(ProjectStatus.BUILD_SUCCESS);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            mCurrentBuildProgress.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
            mCurrentBuildProgress.checkStateCompleted(true);
            mCurrentBuildProgress.setAllStatesCompleted(true);
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.RED)) {
            mBuildStatus.setText(ProjectStatus.BUILD_FAILED);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            mCurrentBuildProgress.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
            mCurrentBuildProgress.checkStateCompleted(true);
            mCurrentBuildProgress.setCurrentStateDescriptionColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.ABORTED_ANIME) || projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.BLUE_ANIME) || projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.RED_ANIME)) {
            mBuildStatus.setText(ProjectStatus.BUILD_BUILDING);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            mCurrentBuildProgress.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            mCurrentBuildProgress.checkStateCompleted(true);
            mTriggerNewBuild.setVisibility(View.GONE);
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.ABORTED)) {
            mBuildStatus.setText(ProjectStatus.BUILD_ABORTED);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            mCurrentBuildProgress.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
            mCurrentBuildProgress.checkStateCompleted(true);
            mCurrentBuildProgress.setCurrentStateDescriptionColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        // Other details
        mVersionNumber.setText(projectDetail.projectVersion);
        mBuildDate.setText(formatDate(projectDetail.projectBuildDate));
        mProjectHealthMessage.setText(projectDetail.healthReportDescription);

        // Build Health Percentage
        if (projectDetail.healthReportScore != null) {
            float buildHealthPercentage = Float.parseFloat(projectDetail.healthReportScore);
            mBuildHealthPercentage.setInnerText(projectDetail.codeCoverage);
            mBuildHealthPercentage.setPercentage(buildHealthPercentage);
            mBuildHealthPercentage.setPercentageBackgroundColor(getPercentageColor(buildHealthPercentage));
            mBuildHealthPercentage.setMainBackgroundColor(context.getResources().getColor(android.R.color.white));
            mBuildHealthPercentage.setTextColor(context.getResources().getColor(android.R.color.black));
            mBuildHealthPercentage.setInnerBackgroundColor(context.getResources().getColor(R.color.codeCoverageColor));
        } else {
            mBuildHealthPercentage.setVisibility(View.GONE);
        }

        mBuildTriggeredBy.setText(projectDetail.buildTriggeredBy);
        if (projectDetail.changeSetAuthorName == null || projectDetail.changeSetAuthorName.length() == 0) {
            mAuthorNameContainer.setVisibility(View.GONE);
        } else {
            mAuthorName.setText(projectDetail.changeSetAuthorName);
        }
        if (projectDetail.changeSetAuthorEmailID == null || projectDetail.changeSetAuthorEmailID.length() == 0) {
            mAuthorEmailContainer.setVisibility(View.GONE);
        } else {
            mAuthorEmail.setText(projectDetail.changeSetAuthorEmailID);
        }
        if (projectDetail.commitComment == null || projectDetail.commitComment.length() == 0) {
            mCommitCommentContainer.setVisibility(View.GONE);
        } else {
            mCommitComment.setText(projectDetail.commitComment);
        }
        if (projectDetail.commitDate == null || projectDetail.commitDate.length() == 0) {
            mCommitDateContainer.setVisibility(View.GONE);
        } else {
            mCommitDate.setText(projectDetail.commitDate);
        }
        String dataSet = "";
        if (projectDetail.changeSet != null) {
            for (String str : projectDetail.changeSet) {
                dataSet += str + "\n";
            }
        }
        if (dataSet.trim().equals("") || dataSet.trim().length() == 0) {
            mDatasetChangesContainer.setVisibility(View.GONE);
        } else {
            mDatasetChanges.setText(dataSet.trim());
        }
        mProjectUrl.setText(projectDetail.projectURL);
        buildLog = projectDetail.lastBuildURL;
        if (projectDetail.lastProductionBuild != null && projectDetail.lastProductionBuild.trim().length() > 0) {
            mProdBuildNumber.setText("#" + projectDetail.lastProductionBuild);
            prodBuildLog = projectDetail.lastProductionBuildURL;
        } else {
            mProdBuildNumber.setText("\u2014");
        }
        if (projectDetail.lastSuccessfulBuild != null && projectDetail.lastSuccessfulBuild.trim().length() > 0) {
            mSuccessfulBuildNumber.setText("#" + projectDetail.lastSuccessfulBuild);
            successfulBuildLog = projectDetail.lastSuccessfulBuildURL;
        } else {
            mSuccessfulBuildNumber.setText("\u2014");
        }
        if (projectDetail.lastFailedBuild != null && projectDetail.lastFailedBuild.trim().length() > 0) {
            mFailedBuildNumber.setText("#" + projectDetail.lastFailedBuild);
            failedBuildLog = projectDetail.lastFailedBuild;
        } else {
            mFailedBuildNumber.setText("\u2014");
        }
    }

    private int getPercentageColor(float percentage) {
        int colorCode = context.getResources().getColor(android.R.color.holo_red_light);
        if (percentage > 90) {
            colorCode = context.getResources().getColor(android.R.color.holo_green_dark);
        } else if (percentage > 70 && percentage < 90) {
            colorCode = context.getResources().getColor(android.R.color.holo_green_light);
        } else if (percentage > 50 && percentage < 70) {
            colorCode = context.getResources().getColor(android.R.color.holo_orange_dark);
        } else if (percentage > 30 && percentage < 50) {
            colorCode = context.getResources().getColor(android.R.color.holo_orange_light);
        }
        return colorCode;
    }

    private String formatDate(String projectBuildDate) {
        String formattedDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.US);
            SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yyyy, HH:mm", Locale.US);
            Date dateTime = format.parse(projectBuildDate);
            formattedDate = output.format(dateTime);
        } catch (Exception e) {
            formattedDate = "\u2014";
        }
        return formattedDate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trigger_new_build:
                triggerNewBuild();
                break;
            case R.id.toggle_text:
                if (mToggleText.getText().equals(context.getString(R.string.toggle_expand))) {
                    mToggleText.setText(context.getString(R.string.toggle_collapse));
                    mToggleText.setTextColor(Color.parseColor("#ffff8800"));
                    ViewAnimationHelper.expand(mExpandedLayout);
                } else {
                    mToggleText.setText(context.getString(R.string.toggle_expand));
                    mToggleText.setTextColor(Color.parseColor("#ff669900"));
                    ViewAnimationHelper.collapse(mExpandedLayout);
                }
                break;
            case R.id.build_log_cta:
                showConsoleOutputActivity(buildLog, ProjectStatus.BUILD_TYPE_LATEST);
                break;
            case R.id.prod_build_container:
                showConsoleOutputActivity(prodBuildLog, ProjectStatus.BUILD_TYPE_PROD);
                break;
            case R.id.successful_build_container:
                showConsoleOutputActivity(successfulBuildLog, ProjectStatus.BUILD_TYPE_SUCCESS);
                break;
            case R.id.failed_build_container:
                showConsoleOutputActivity(failedBuildLog, ProjectStatus.BUILD_TYPE_FAILED);
                break;
        }
    }

    private void triggerNewBuild() {
        mTriggerNewBuild.setVisibility(View.GONE);
        Toast.makeText(context.getApplicationContext(), context.getString(R.string.trigger_new_build_message), Toast.LENGTH_SHORT).show();
        new TriggerNewBuild().execute();

    }

    private void showConsoleOutputActivity(String url, String buildType) {
        if (url != null && url.trim().length() > 1) {
            Intent intent = new Intent(context.getApplicationContext(), ConsoleActivity.class);
            intent.putExtra(ProjectStatus.BUILD_CONSOLE_URL, url);
            intent.putExtra(ProjectStatus.BUILD_NAME, buildType);
            context.startActivity(intent);
        } else {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.network_not_available_message), Toast.LENGTH_SHORT).show();
        }
    }

    private class TriggerNewBuild extends AsyncTask<Void, Void, Void> {
        boolean networkAvailable = true;

        @Override
        protected Void doInBackground(Void... params) {
            if (NetworkHelper.isNetworkAvailable(context.getApplicationContext())) {
                startBuild();
            } else {
                networkAvailable = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aLong) {
            super.onPostExecute(aLong);
            if (!networkAvailable) {
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.network_not_available_message), Toast.LENGTH_SHORT).show();
            } else {
                homeActivity.refreshActivity();
            }
        }
    }

    private void startBuild() {
        NetworkHelper.getResponseData(EndPoint.getTriggerNewBuildURL(mProjectName.getText().toString()));
    }
}