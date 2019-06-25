package ca.mohawk.HealthMetrics.UserProfile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import androidx.fragment.app.DialogFragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;

public class UserCreationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    HealthMetricsDbHelper healthMetricsDbHelper;
    EditText dateOfBirthEditText;
    DatePickerFragment datePickerFragment = new DatePickerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        setTitle("Create User Profile");
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);
        healthMetricsDbHelper.seedUnits();
        dateOfBirthEditText = findViewById(R.id.editTextDateOfBirthUserCreation);
        dateOfBirthEditText.setOnClickListener(this);
    }

    public void createUser(View view) {

        EditText firstNameEditText = findViewById(R.id.editTextFirstName);
        EditText lastNameEditText = findViewById(R.id.editTextLastName);
        RadioGroup genderRadioGroup = findViewById(R.id.radioGroupGender);

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String gender;


        if(genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
            gender = "Male";
        }else{
            gender = "Female";
        }

        if(firstName.matches("")  || lastName.matches("")| dateOfBirth.matches("") ){
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }else{

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

    @Override
    public void onClick(View v) {
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText((month + 1) + "-0" + dayOfMonth + "-" + year);
        }else{
            dateOfBirthEditText.setText((month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }
}