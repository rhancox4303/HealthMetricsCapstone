package ca.mohawk.HealthMetrics.UserProfile;


import android.app.DatePickerDialog;
import android.os.Bundle;
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
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The EditUserFragment class is an extension of the Fragment class.
 * It is used to edit the user's profile.
 */
public class EditUserFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    // Initialize the HealthMetricsDbHelper healthMetricsDbHelper
    private HealthMetricsDbHelper healthMetricsDbHelper;

    //The layout elements that are accessed throughout the fragment.
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private RadioGroup radioGroupGender;
    private EditText dateOfBirthEditText;

    public EditUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_edit_user, container, false);

        //Get healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        //Instantiate editUserButton.
        Button editUserButton = rootView.findViewById(R.id.buttonEditUser);

        //Set the editUserButton onClickListener
        editUserButton.setOnClickListener(this);

        //Instantiate the form elements.
        firstNameEditText = rootView.findViewById(R.id.editTextFirstNameEditUser);
        lastNameEditText = rootView.findViewById(R.id.editTextLastNameEditUser);
        radioGroupGender = rootView.findViewById(R.id.radioGroupGenderEditProfile);
        dateOfBirthEditText = rootView.findViewById(R.id.editTextDateOfBirthEditUser);

        //Set the dateOfBirthEditText onClickListener.
        dateOfBirthEditText.setOnClickListener(this);

        //Get the user fom the database.
        User user = healthMetricsDbHelper.getUser();

        // If the returned user is null then display a message to the user.
        if (user == null) {
            Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        } else {
            //Populate the fields with the user's information.
            firstNameEditText.setText(user.firstName);
            lastNameEditText.setText(user.lastName);
            dateOfBirthEditText.setText(user.dateOfBirth);

            if (user.gender.equals("Female")) {
                radioGroupGender.check(R.id.radioButtonFemaleEditUser);
            } else {
                radioGroupGender.check(R.id.radioButtonMaleEditUser);
            }
        }

        //Return rootView.
        return rootView;
    }

    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }
    /**
     * Updates the user profile in the database.
     */
    private void updateUser() {

        //If the call to validateUserInput returns true then proceed to create the user.
        if (validateUserInput()) {

            //Get the user inputs.
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String dateOfBirth = dateOfBirthEditText.getText().toString();
            String gender;

            //Get the user's selected gender.
            if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            //Use the healthMetricsDbHelper to update the user in the database and verify it was successful.
            if (healthMetricsDbHelper.updateUser(new User(firstName, lastName, gender, dateOfBirth))) {

                //Create viewUserFragment.
                ViewUserFragment viewUserFragment = new ViewUserFragment();

                //Insert viewUserFragment into activity.
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewUserFragment)
                        .addToBackStack(null)
                        .commit();

                //Else then inform the user.
            } else {
                Toast.makeText(getActivity(), "Unable to update user.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates the user input.
     *
     * @return Returns a boolean value based on whether the user input is valid.
     */
    private boolean validateUserInput() {

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
     * Runs when the edit user or date of birth edit text is pressed.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditUser) {
            updateUser();
        } else if (v.getId() == R.id.editTextDateOfBirthEditUser) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "datePicker");
        }
    }

    /**
     * The onDateSet method is ran when a date is selected from the date picker dialog.
     * The date of birth field is set to the date that was selected.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1).append("-0")
                    .append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1).append("-")
                    .append(dayOfMonth).append("-").append(year).toString());
        }
    }
}