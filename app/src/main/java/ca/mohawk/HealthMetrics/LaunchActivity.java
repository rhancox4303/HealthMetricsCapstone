package ca.mohawk.HealthMetrics;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ca.mohawk.HealthMetrics.UserProfile.UserCreationActivity;

public class LaunchActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor  editor = userInfo.edit();
        boolean loggedIn =  userInfo.getBoolean("loggedIn", false);

        if (loggedIn)
        {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else {
            editor.putBoolean("loggedIn",true);
            editor.commit();
            Intent intent = new Intent(this, UserCreationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}

