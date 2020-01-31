package com.anandkparmar.githubtrend.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.anandkparmar.githubtrend.R;

public class SharedPrefUtil {

    // Constant Keys
    private static final String LAST_DATA_SAVED_TIME_STAMP = "LAST_DATA_SAVED_TIME_STAMP";
    private static final String TRENDING_GITHUB_REPOSITORY = "TRENDING_GITHUB_REPOSITORY";

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences getSharedPreferences(Context context) {
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }

    public static void setLastDataSavedTimeStamp(Context context, Long timestamp){
        getSharedPreferences(context).edit().putLong(LAST_DATA_SAVED_TIME_STAMP, timestamp).apply();
    }

    public static Long getLastDataSavedTimeStamp(Context context){
        return getSharedPreferences(context).getLong(LAST_DATA_SAVED_TIME_STAMP, -1L);
    }

    public static void setTrendGithubRepository(Context context, String data){
        getSharedPreferences(context).edit().putString(TRENDING_GITHUB_REPOSITORY, data).apply();
    }

    public static String getTrendGithubRepository(Context context){
        return getSharedPreferences(context).getString(TRENDING_GITHUB_REPOSITORY, "");
    }

}
