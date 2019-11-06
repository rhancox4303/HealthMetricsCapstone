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

    /**
     * The onCreateView method initializes the view variables with the user profile
     * information fetched from the database using the HealthMetricsDbHelper object.
     */
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
            firstNameEditText.setText(user.FirstName);
            lastNameEditText.setText(user.LastName);
            dateOfBirthEditText.setText(user.DateOfBirth);

            if (user.Gender.equals("Female")) {
                radioGroupGender.check(R.id.radioButtonFemaleEditUser);
            } else {
                radioGroupGender.check(R.id.radioButtonMaleEditUser);
            }
        }

        //Return rootView.
        return rootView;
    }

    /**
     * The editUserProfile method gets the user inputed values, creates the new user and uses the healthMetricsDbHelper
     * object to update the user profile already in the database.
     */
    public void editUserProfile() {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String gender = "Female";

        if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMaleEditUser) {
            gender = "Male";
        }

        //Validate the user input.
        if (firstName.matches("") || lastName.matches("") | dateOfBirth.matches("")) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {
            //Update the user in the database.
            int updateStatus = healthMetricsDbHelper.updateUser(new User(firstName, lastName, gender, dateOfBirth));
            if (updateStatus != 1) {
                Toast.makeText(getActivity(), "Error updating profile.", Toast.LENGTH_SHORT).show();
            } else {
                //Launch the ViewUserFragment
                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                ViewUserFragment viewProfileFragment = new ViewUserFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewProfileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    /**
     * The onClick method runs when the a view's onClickListener is activated.
     * It runs the editProfile method or opens the DatePickerFragment depending on what was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditProfile) {
            editUserProfile();
        } else if (v.getId() == R.id.editTextDateOfBirthEditUser) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
        }
    }

    /**
     * The onDateSet method is ran when a date is selected from the datepicker dialog.
     * The date of birth field is set to the date that was selected.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfBirthEditText.setText((month + 1) + "-0" + dayOfMonth + "-" + year);
        } else {
            dateOfBirthEditText.setText((month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }
}
