package ca.mohawk.HealthMetrics.UserProfile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;

public class UserCreationActivity extends AppCompatActivity {
    HealthMetricsDbHelper healthMetricsDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        setTitle("Create User Profile");
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);
    }

    public void createUser(View view) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        EditText firstNameEditText = findViewById(R.id.editTextFirstName);
        EditText lastNameEditText = findViewById(R.id.editTextLastName);
        RadioGroup genderRadioGroup = findViewById(R.id.radioGroupGender);
        EditText dateOfBirthEditText = findViewById(R.id.editTextDateOfBirth);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String stringDateOfBirth = dateOfBirthEditText.getText().toString();
        String gender;
        Date dateOfBirth = null;

        if(genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
            gender = "Male";
        }else{
            gender = "Female";
        }

        if(firstName.matches("")  || lastName.matches("")| stringDateOfBirth.matches("") ){
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }else{
            try {
                 dateOfBirth = simpleDateFormat.parse(stringDateOfBirth);
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }

            healthMetricsDbHelper.addUser(new User(firstName,lastName,gender,dateOfBirth));

            Toast.makeText(this, "User added.", Toast.LENGTH_SHORT).show();

            SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor  editor = userInfo.edit();

            editor.putBoolean("loggedIn", true);
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}