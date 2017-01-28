package com.hpe.devops.entity;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetails {
    public String projectName;
    public String projectURL;
    public String projectStatus;
    public String projectBuildDate;
    public String projectVersion;
    public String codeCoverage;

    public String buildTriggeredBy;
    public List<String> changeSet;
    public String changeSetAuthorName;
    public String changeSetAuthorEmailID;
    public String commitComment;
    public String commitDate;

    public String healthReportDescription;
    public String healthReportScore;
    public String lastBuild;
    public String lastBuildURL;
    public String lastProductionBuild;
    public String lastProductionBuildURL;
    public String lastSuccessfulBuild;
    public String lastSuccessfulBuildURL;
    public String lastFailedBuild;
    public String lastFailedBuildURL;

    public static List<ProjectDetails> getDummyData() {

        // Dummy wait for 5 seconds...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        ProjectDetails projectDetail1 = new ProjectDetails();
        projectDetail1.projectName = "Aeroplan";
        projectDetail1.projectURL = "http://35.162.132.98:8080/job/Aeroplan/";
        projectDetail1.projectStatus = "blue";
        projectDetail1.projectBuildDate = "2017-01-13T03:44:00+0000";
        projectDetail1.projectVersion = "Aeroplan-4.0.3_14";
        projectDetail1.codeCoverage = "100";
        projectDetail1.buildTriggeredBy = "Started by remote host 122.172.16.7";
        projectDetail1.changeSetAuthorName = "gokul-nath.periasamy";
        projectDetail1.commitComment = "Trigger a build by SCM\n";
        projectDetail1.changeSetAuthorEmailID = "gokul-nath.periasamy@hpe.com";
        projectDetail1.commitDate = "2017-01-15 11:23:58 +0530";
        List<String> changeSetList = new ArrayList<>();
        changeSetList.add("MainActivity.java");
        changeSetList.add("HomeActivity.java");
        changeSetList.add("SpalashActivity.java");
        projectDetail1.changeSet = changeSetList;

        ProjectDetails projectDetail2 = new ProjectDetails();
        projectDetail2.projectName = "HPEIoT";
        projectDetail2.projectURL = "http://35.162.132.98:8080/job/HPEIoT/";
        projectDetail2.projectStatus = "red";
        projectDetail2.projectBuildDate = "2016-12-01T15:20:00+0000";
        projectDetail2.projectVersion = "HPEIoT-1.0.1_4";
        projectDetail2.codeCoverage = "45";
        projectDetail2.buildTriggeredBy = "Started by SCM change";
        projectDetail2.changeSetAuthorName = "gokul-nath.periasamy";
        projectDetail2.commitComment = "Trigger a build by SCM\n";
        projectDetail2.changeSetAuthorEmailID = "gokul-nath.periasamy@hpe.com";
        projectDetail2.commitDate = "2017-01-12 11:23:58 +0530";
        List<String> changeSetList2 = new ArrayList<>();
        changeSetList.add("string.xml");
        projectDetail2.changeSet = changeSetList2;

        projectDetailsList.add(projectDetail1);
        projectDetailsList.add(projectDetail2);

        return projectDetailsList;

    }

}
