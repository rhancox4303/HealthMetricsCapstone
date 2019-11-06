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

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The CreateUserFragment class is an extension of the Fragment class.
 * It is used to create the user's profile when the user opens the app for the first time.
 */
public class CreateUserFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    //The HealthMetricsDbHelper healthMetricsDbHelper is used to access the SQLite database.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    //The layout elements that are accessed throughout the fragment.
    private EditText dateOfBirthEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup genderRadioGroup;

    public CreateUserFragment() {
        //Required empty public constructor
    }

    /**
     * The onCreateView method initializes the view variables
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Hide the action bar to prevent the user from navigating to a different section of the application.
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

        //Set the title of the activity to change the window text.
        getActivity().setTitle("Create User Profile");

        //Set the rootView.
        View rootView = inflater.inflate(R.layout.fragment_create_user, container, false);

        //Instantiate the healthMetricsDbHelper
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
     * The createUser method gets the user inputted values, creates the user and uses the
     * healthMetricsDbHelper object to add the created user to the database.
     */
    public void createUser() {

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
     * The validateUserInput methods validates the user input.
     *
     * @return A boolean value is returned based on whether the user input is valid.
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
     * The storeLoggedInState method writes true to a SharedPreference that is used to
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
     * The onDateSet method is ran when a date is selected from the datepicker dialog.
     * The date of birth field is set to the date that was selected.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Add a 0 to the day if it less than 10 and then insert the date in dateOfBirthEditText.
        if (dayOfMonth < 10) {

            dateOfBirthEditText.setText(new StringBuilder().append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1)
                    .append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }

    /**
     * The onClick method runs when the view's onClickListener is activated.
     * It runs the createUser method or opens the DatePickerFragment depending on what was clicked.
     */
    @Override
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
