package com.anandkparmar.githubtrend.utils;

public class NetworkConstants {

    private NetworkConstants() {}

    // Configurations
    public static final String BASE_URL =  "https://github-trending-api.now.sh";
    public static final boolean IS_LOGGING_ENABLE = true;
    public static final int CONNECTION_TIMEOUT_IN_MINS = 25;
    public static final int READ_TIMEOUT_IN_MINS = 25;
    public static final int WRITE_TIMEOUT_IN_MINS = 25;

    // End-Points
    public static final String GET_REPOSITORIES_END_POINT =  "/repositories";


}
