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

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The EditUserFragment class is an extension of the Fragment class.
 * It is used to edit the user's profile.
 */
public class EditUserFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //The HealthMetricsDbHelper healthMetricsDbHelper is used to access the SQLite database.
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

        //Instantiate healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        //Instantiate editUserButton.
        Button editUserButton = rootView.findViewById(R.id.buttonEditProfile);

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
     * The editUser method gets the user inputted values and uses the healthMetricsDbHelper
     * object to update the user profile already in the database.
     */
    private void editUser() {

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

                //Send message to user.
                Toast.makeText(getActivity(), "User updated.", Toast.LENGTH_SHORT).show();

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
     * The validateUserInput methods validates the user input.
     *
     * @return A boolean value is returned based on whether the user input is valid.
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
     * The onClick method runs when the a view's onClickListener is activated.
     * It runs the editProfile method or opens the DatePickerFragment depending on what was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditProfile) {
            editUser();
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
        //Add a 0 to the day if it less than 10 and then insert the date in dateOfBirthEditText.
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1).append("-0")
                    .append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfBirthEditText.setText(new StringBuilder().append(month + 1).append("-")
                    .append(dayOfMonth).append("-").append(year).toString());
        }
    }
}