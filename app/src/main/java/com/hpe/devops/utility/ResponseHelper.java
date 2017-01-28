package com.hpe.devops.utility;

import android.util.Log;

import com.hpe.devops.entity.EndPoint;
import com.hpe.devops.entity.ProjectDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public abstract class ResponseHelper {

    public static List<ProjectDetails> getProjectData() {

        // Get project list (name, color, url)
        String projectList = NetworkHelper.getResponseData(EndPoint.PROJECT_LIST);
        JSONArray jsonArrayProjectList;
        try {
            JSONObject projectListJSONObject = new JSONObject(projectList);
            jsonArrayProjectList = projectListJSONObject.getJSONArray("jobs");
        } catch (JSONException je) {
            jsonArrayProjectList = null;
            je.printStackTrace();
        }

        // Get project additional details (date, version, coverage)
        String projectAdditionalDetails = NetworkHelper.getResponseData(EndPoint.PROJECT_ADDITIONAL_DETAILS);
        JSONArray jsonArrayProjectAdditionalDetails;
        try {
            jsonArrayProjectAdditionalDetails = new JSONArray(projectAdditionalDetails);
        } catch (JSONException je) {
            jsonArrayProjectAdditionalDetails = null;
            je.printStackTrace();
        }

        return parseData(jsonArrayProjectList, jsonArrayProjectAdditionalDetails);
    }

    private static List<ProjectDetails> parseData(JSONArray jsonArrayProjectList, JSONArray jsonArrayProjectAdditionalDetails) {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();
        ProjectDetails projectDetails;
        JSONObject jsonObjectProjectList;
        JSONObject jsonObjectProjectAdditionalDetails = null;
        try {
            if (jsonArrayProjectList != null && jsonArrayProjectList.length() > 0 && jsonArrayProjectAdditionalDetails != null && jsonArrayProjectAdditionalDetails.length() > 0
                    && (jsonArrayProjectList.length() == jsonArrayProjectAdditionalDetails.length())) {

                for (int i = 0; i < jsonArrayProjectList.length(); i++) {
                    jsonObjectProjectList = (JSONObject) jsonArrayProjectList.get(i);
                    for (int j = 0; j < jsonArrayProjectAdditionalDetails.length(); j++) {
                        jsonObjectProjectAdditionalDetails = (JSONObject) jsonArrayProjectAdditionalDetails.get(j);
                        if (jsonObjectProjectList.getString("name").equalsIgnoreCase(jsonObjectProjectAdditionalDetails.getString("name"))) {
                            break;
                        }
                    }

                    projectDetails = new ProjectDetails();
                    projectDetails.projectName = jsonObjectProjectList.getString("name");
                    projectDetails.projectURL = jsonObjectProjectList.getString("url");
                    projectDetails.projectStatus = jsonObjectProjectList.getString("color");
                    if (jsonObjectProjectAdditionalDetails != null) {
                        projectDetails.projectVersion = jsonObjectProjectAdditionalDetails.getString("version");
                        projectDetails.projectBuildDate = jsonObjectProjectAdditionalDetails.getString("date");
                        JSONArray msr = jsonObjectProjectAdditionalDetails.getJSONArray("msr");
                        JSONObject coverage = msr.getJSONObject(0);
                        projectDetails.codeCoverage = coverage.getString("val");
                    }

                    // Get project last build details
                    String projectLastBuildDetails = NetworkHelper.getResponseData(EndPoint.getProjectLastBuildDetailsURL(projectDetails.projectName));
                    if (projectLastBuildDetails != null) {
                        JSONObject jsonObjectProjectBuildDetails = new JSONObject(projectLastBuildDetails);
                        JSONArray actions = jsonObjectProjectBuildDetails.getJSONArray("actions");
                        JSONObject causes = actions.getJSONObject(0);
                        JSONArray causes2 = causes.getJSONArray("causes");
                        JSONObject causesObj = causes2.getJSONObject(0);
                        projectDetails.buildTriggeredBy = causesObj.getString("shortDescription");
                        JSONObject changeSet = jsonObjectProjectBuildDetails.getJSONObject("changeSet");
                        if (changeSet != null && changeSet.length() > 0) {
                            JSONArray items = changeSet.getJSONArray("items");
                            if (items != null && items.length() > 0) {
                                JSONObject itemObject = items.getJSONObject(0);
                                if (itemObject != null && itemObject.length() > 0) {
                                    JSONArray affectedPaths = itemObject.getJSONArray("affectedPaths");
                                    List<String> affectedPath = new ArrayList<>();
                                    if (affectedPaths != null && affectedPaths.length() > 0) {
                                        for (int k = 0; k < affectedPaths.length(); k++) {
                                            affectedPath.add(affectedPaths.get(k).toString());
                                        }
                                    }
                                    projectDetails.changeSet = affectedPath;
                                    JSONObject author = itemObject.getJSONObject("author");
                                    if (author != null && author.length() > 0) {
                                        projectDetails.changeSetAuthorName = author.getString("fullName");
                                    }
                                    projectDetails.changeSetAuthorEmailID = itemObject.getString("authorEmail");
                                    projectDetails.commitComment = itemObject.getString("msg");
                                    projectDetails.commitDate = itemObject.getString("date");
                                }
                            }
                        }
                    }

                    // Get project extended build details
                    String projectExtendedBuildDetails = NetworkHelper.getResponseData(EndPoint.getProjectExtendedBuildDetailsURL(projectDetails.projectName));
                    if (projectExtendedBuildDetails != null) {
                        JSONObject jsonObjectProjectExtendedBuildDetails = new JSONObject(projectExtendedBuildDetails);
                        JSONArray healthReport = jsonObjectProjectExtendedBuildDetails.getJSONArray("healthReport");
                        if (healthReport != null && healthReport.length() > 0) {
                            JSONObject healthReportObject = healthReport.getJSONObject(0);
                            if (healthReportObject != null && healthReportObject.length() > 0) {
                                projectDetails.healthReportDescription = healthReportObject.getString("description");
                                projectDetails.healthReportScore = healthReportObject.getString("score");
                            }
                        }
                        if (!jsonObjectProjectExtendedBuildDetails.isNull("lastBuild")) {
                            JSONObject lastBuild = jsonObjectProjectExtendedBuildDetails.getJSONObject("lastBuild");
                            if (lastBuild != null && lastBuild.length() > 0) {
                                projectDetails.lastBuild = lastBuild.getString("number");
                                projectDetails.lastBuildURL = lastBuild.getString("url");
                            }
                        }
                        if (!jsonObjectProjectExtendedBuildDetails.isNull("lastFailedBuild")) {
                            JSONObject lastFailedBuild = jsonObjectProjectExtendedBuildDetails.getJSONObject("lastFailedBuild");
                            if (lastFailedBuild != null && lastFailedBuild.length() > 0) {
                                projectDetails.lastFailedBuild = lastFailedBuild.getString("number");
                                projectDetails.lastFailedBuildURL = lastFailedBuild.getString("url");
                            }
                        }
                    }

                    projectDetailsList.add(projectDetails);
                }
            }
        } catch (JSONException je) {
            projectDetailsList = null;
            Log.i(ResponseHelper.class.getCanonicalName(), "Unable to parse response.");
            je.printStackTrace();
        }
        Log.i(ResponseHelper.class.getCanonicalName(), "Response Parsed");
        return projectDetailsList;
    }

}
