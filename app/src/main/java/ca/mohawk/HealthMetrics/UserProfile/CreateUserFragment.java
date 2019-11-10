package ca.mohawk.HealthMetrics.UserProfile;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The CreateUserFragment class is an extension of the Fragment class.
 * Allows the user to create the user profile when the user opens the app for the first time.
 */
public class CreateUserFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    // Initialize the HealthMetricsDbHelper healthMetricsDbHelper
    private HealthMetricsDbHelper healthMetricsDbHelper;

    //The layout elements that are accessed throughout the fragment.
    private EditText dateOfBirthEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup genderRadioGroup;

    public CreateUserFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Hide the action bar to prevent the user from navigating to a different section of the application.
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        //Set the title of the activity to change the window text.
        getActivity().setTitle("Create User Profile");

        //Set the rootView.
        View rootView = inflater.inflate(R.layout.fragment_create_user, container, false);

        // Instantiate the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        //Instantiate the layout elements from the fragment_create_user layout.
        dateOfBirthEditText = rootView.findViewById(R.id.editTextDateOfBirthCreateUser);
        firstNameEditText = rootView.findViewById(R.id.editTextFirstNameCreateUser);
        lastNameEditText = rootView.findViewById(R.id.editTextLastNameCreateUser);
        genderRadioGroup = rootView.findViewById(R.id.radioGroupGenderCreateUser);

        //Instantiate the createUserButton.
        Button createUserButton = rootView.findViewById(R.id.buttonCreateUser);

        //Set the create user button and date selection clickListeners.
        createUserButton.setOnClickListener(this);
        dateOfBirthEditText.setOnClickListener(this);

        //Return the rootView
        return rootView;
    }

    /**
     * Adds the user to the database.
     */
    private void createUser() {

        //If the call to validateUserInput returns true then proceed to create the user.
        if (validateUserInput()) {

            //Get the user inputs.
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String dateOfBirth = dateOfBirthEditText.getText().toString();
            String gender;

            //Get the user's selected gender.
            if (genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            //Use the healthMetricsDbHelper to add the user to the database and verify it was successful.
            if (healthMetricsDbHelper.addUser(new User(firstName, lastName, gender, dateOfBirth))) {

                //Send message to user.
                Toast.makeText(getActivity(), "User added.", Toast.LENGTH_SHORT).show();

                //Call storeLoggedInState.
                storeLoggedInState();

                //Seed the database.
                healthMetricsDbHelper.seedDatabase();

                //Create metricsListFragment.
                MetricsListFragment metricsListFragment = new MetricsListFragment();

                //Insert metricsListFragment into activity.
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, metricsListFragment)
                        .addToBackStack(null)
                        .commit();
                //Else then inform the user.
            } else {
                Toast.makeText(getActivity(), "Unable to add user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates the user input.
     *
     * @return Returns a boolean based on whether the user input is valid.
     */
    public boolean validateUserInput() {

        //If any of the fields are empty, inform the user and return false.
        if (firstNameEditText.getText().toString().trim().equals("") ||
                lastNameEditText.getText().toString().trim().equals("") ||
                dateOfBirthEditText.getText().toString().trim().equals("")) {

            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();

            return false;

            //Else if the first name or last name contain digits, inform the user  and return false
        } else if (firstNameEditText.getText().toString().matches(".*\\d+.*") ||
                lastNameEditText.getText().toString().matches(".*\\d+.*")) {

            Toast.makeText(getActivity(), "Please remove digits from first and last name",
                    Toast.LENGTH_SHORT).show();

            return false;

            //Else the input is valid and true is returned.
        } else {
            return true;
        }
    }

    /**
     * Writes true to a SharedPreference that is used to
     * verify if the user has created an account.
     */
    private void storeLoggedInState() {

        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = userInfo.edit();

        //Insert true to validate that the user profile has been created.
        editor.putBoolean("loggedIn", true);
        editor.commit();
    }


    /**
     * The onDateSet method is ran when a date is selected from the datePicker dialog.
     * The date of birth field is set to the date that was selected.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }

    /**
     * Runs when the Create user button or the Date of Entry EditText
     * onClickListener is activated.
     *
     * @param v Represents the view.
     */
    public void onClick(View v) {
        //If the create user button was pressed then call createUser.
        if (v.getId() == R.id.buttonCreateUser) {
            createUser();
            //Else if the Date of birth edit text was pressed then build and show the DatePickerFragment.
        } else if (v.getId() == R.id.editTextDateOfBirthCreateUser) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(Objects.requireNonNull(getFragmentManager()), "datePicker");
        }
    }
}