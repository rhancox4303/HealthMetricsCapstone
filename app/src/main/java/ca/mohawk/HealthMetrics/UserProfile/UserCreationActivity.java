package ca.mohawk.HealthMetrics.UserProfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;

public class UserCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HealthMetricsDbHelper db =  new HealthMetricsDbHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        setTitle("Create User Profile");
    }
}