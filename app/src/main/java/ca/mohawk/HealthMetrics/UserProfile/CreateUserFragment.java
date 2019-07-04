package ca.mohawk.HealthMetrics.UserProfile;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsViewFragment;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;

/**
 * The CreateUserFragment class is an extension of the Fragment class.
 * It is used to create the user's profile when the user opens the app for the first time.
 */
public class CreateUserFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    public CreateUserFragment() {
        // Required empty public constructor
    }

    private HealthMetricsDbHelper healthMetricsDbHelper;
    private DatePickerFragment datePickerFragment = new DatePickerFragment();

    private EditText dateOfBirthEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup genderRadioGroup;

    /**
     * The onCreateView method initializes the view variables 
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Hide the action bar to prevent the user from navigating to a different section of the application.
        ((MainActivity)getActivity()).getSupportActionBar().hide();
        getActivity().setTitle("Create User Profile");
        
        View rootView =  inflater.inflate(R.layout.fragment_create_user, container, false);
        
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        dateOfBirthEditText = rootView.findViewById(R.id.editTextDateOfBirthUserCreation);
        firstNameEditText = rootView.findViewById(R.id.editTextFirstName);
        lastNameEditText = rootView.findViewById(R.id.editTextLastName);
        genderRadioGroup = rootView.findViewById(R.id.radioGroupGender);
        Button createUserButton = rootView.findViewById(R.id.buttonCreateUser);
      
        createUserButton .setOnClickListener(this);
        dateOfBirthEditText.setOnClickListener(this);

        return rootView;
    }
    
    /**
     * The createUser method gets the user inputed values, creates the user and uses the healthMetricsDbHelper
     * object to add the created user to the database.
     */
    public void createUser() {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String gender;

        //Get the user's selected gender.
        if(genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale){
            gender = "Male";
        }else{
            gender = "Female";
        }
        
        //Verify the user's data entry
        if(firstName.matches("")  || lastName.matches("")| dateOfBirth.matches("") ){
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }else{
            
            healthMetricsDbHelper.addUser(new User(firstName,lastName,gender,dateOfBirth));

            Toast.makeText(getActivity(), "User added.", Toast.LENGTH_SHORT).show();
            
            storeLoggedInState();
            
            MetricsViewFragment metricsViewFragment = new MetricsViewFragment();        
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,metricsViewFragment )
                    .addToBackStack(null)
                    .commit();
        }
    }
    
    /**
     * The storeLoggedInState method writes true to a SharedPreference that is used to 
     * verify if the user has created an account.
     */
    public void storeLoggedInState(){
        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = userInfo.edit();

        editor.putBoolean("loggedIn", true);
        editor.commit();
    }
    
    /**
     * The onDateSet method is ran when a date is selected from the datepicker dialog.
     * The date of birth field is set to the date that was selected.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Add a 0 to the day if it less than 10.
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText((month + 1) + "-0" + dayOfMonth + "-" + year);
        }else{
            dateOfBirthEditText.setText((month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }
    
    /**
     * The onClick method runs when the a view's onClickListener is activated.
     * It runs the createUser method or opens the DatePickerFragment depending on what was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonCreateUser) {
            createUser();
        }else if(v.getId() == R.id.editTextDateOfBirthUserCreation){
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(getFragmentManager(), "datePicker");
        }
    }
}
