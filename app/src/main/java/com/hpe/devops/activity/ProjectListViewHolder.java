package com.hpe.devops.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hpe.devops.entity.EndPoint;
import com.hpe.devops.entity.ProjectDetails;
import com.hpe.devops.entity.ProjectStatus;
import com.hpe.devops.R;
import com.hpe.devops.utility.ViewAnimationHelper;

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
    View mToggleView;
    TextView mToggleText;
    View mExpandedLayout;
    TextView mProjectHealthMessage;
    PieView mBuildHealthPercentage;
    TextView mBuildTriggeredBy;
    TextView mAuthorName;
    TextView mAuthorEmail;
    TextView mCommitComment;
    TextView mCommitDate;
    TextView mDatasetChanges;
    TextView mBuildUrl;
    TextView mBuildLogCTA;

    String consoleOutputURL;

    Context context;

    public ProjectListViewHolder(View itemView) {
        super(itemView);
        mProjectName = (TextView) itemView.findViewById(R.id.project_name);
        mCodeCoverage = (PieView) itemView.findViewById(R.id.pv_code_coverage);
        mBuildStatus = (TextView) itemView.findViewById(R.id.build_status);
        mVersionNumber = (TextView) itemView.findViewById(R.id.version_number);
        mBuildDate = (TextView) itemView.findViewById(R.id.build_date);
        mToggleView = itemView.findViewById(R.id.toggle_view);
        mToggleView.setOnClickListener(this);
        mToggleText = (TextView) itemView.findViewById(R.id.toggle_text);
        mExpandedLayout = itemView.findViewById(R.id.expanded_layout);
        mProjectHealthMessage = (TextView) itemView.findViewById(R.id.project_health_message);
        mBuildHealthPercentage = (PieView) itemView.findViewById(R.id.pv_project_health);
        mBuildTriggeredBy = (TextView) itemView.findViewById(R.id.build_triggered_by);
        mAuthorName = (TextView) itemView.findViewById(R.id.author_name);
        mAuthorEmail = (TextView) itemView.findViewById(R.id.author_email);
        mCommitComment = (TextView) itemView.findViewById(R.id.commit_comment);
        mCommitDate = (TextView) itemView.findViewById(R.id.commit_date);
        mDatasetChanges = (TextView) itemView.findViewById(R.id.dataset_changes);
        mBuildUrl = (TextView) itemView.findViewById(R.id.build_url);
        mBuildLogCTA = (TextView) itemView.findViewById(R.id.build_log_cta);
        mBuildLogCTA.setOnClickListener(this);
    }

    public void bindView(final ProjectDetails projectDetail, final Context context) {
        this.context = context;
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
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.RED)) {
            mBuildStatus.setText(ProjectStatus.BUILD_FAILED);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.ABORTED_ANIME) || projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.BLUE_ANIME)) {
            mBuildStatus.setText(ProjectStatus.BUILD_BUILDING);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (projectDetail.projectStatus.equalsIgnoreCase(ProjectStatus.ABORTED)) {
            mBuildStatus.setText(ProjectStatus.BUILD_ABORTED);
            mBuildStatus.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
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
        mAuthorName.setText(projectDetail.changeSetAuthorName);
        mAuthorEmail.setText(projectDetail.changeSetAuthorEmailID);
        mCommitComment.setText(projectDetail.commitComment);
        mCommitDate.setText(projectDetail.commitDate);
        String dataSet = "";
        if (projectDetail.changeSet != null) {
            for (String str : projectDetail.changeSet) {
                dataSet += str + "\n";
            }
        } else {
            dataSet = "\u2014";
        }
        mDatasetChanges.setText(dataSet);
        mBuildUrl.setText(projectDetail.lastBuildURL);
        consoleOutputURL = EndPoint.getBuildConsoleLogURL(projectDetail.lastBuildURL);
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
            case R.id.toggle_view:
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
                showConsoleOutputActivity(consoleOutputURL);
                break;
        }
    }

    private void showConsoleOutputActivity(String consoleOutputURL) {
        Intent intent = new Intent(context.getApplicationContext(), ConsoleActivity.class);
        intent.putExtra(ProjectStatus.BUILD_CONSOLE_URL, consoleOutputURL);
        context.startActivity(intent);
    }
}