package com.hpe.devops.entity;

public class EndPoint {
    private static final String BASE_IP = "http://35.162.132.98";
    public static final String SONAR_BASE = BASE_IP + ":9000/";
    public static final String JENKINS_BASE = BASE_IP + ":8080/";

    public static final String PROJECT_LIST = JENKINS_BASE + "api/json?pretty=true";
    public static final String PROJECT_ADDITIONAL_DETAILS = SONAR_BASE + "api/resources?metrics=coverage";

    public static String getProjectLastBuildDetailsURL(String projectName) {
        if (projectName != null) {
            return JENKINS_BASE + "job/" + projectName + "/lastBuild/api/json";
        }
        return null;
    }

    public static String getProjectExtendedBuildDetailsURL(String projectName) {
        if (projectName != null) {
            return JENKINS_BASE + "job/" + projectName + "/api/json";
        }
        return null;
    }

    public static String getBuildConsoleLogURL(String url) {
        if (url != null) {
            return url + "consoleText";
        }
        return null;
    }

    public static String getTriggerNewBuildURL(String projectName) {
        if (projectName != null) {
            return JENKINS_BASE + "buildByToken/build?job=" + projectName + "&token=build";
        }
        return null;
    }
}
