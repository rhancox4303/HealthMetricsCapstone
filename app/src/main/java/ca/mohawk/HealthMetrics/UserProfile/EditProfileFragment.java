package ca.mohawk.HealthMetrics.UserProfile;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    HealthMetricsDbHelper healthMetricsDbHelper;
    EditText firstNameEditText;
    EditText lastNameEditText;
    RadioGroup radioGroupGender;
    EditText dateOfBirthEditText;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Button editProfileButton = view.findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener(this);

        firstNameEditText = view.findViewById(R.id.editTextFirstNameEditProfile);
        lastNameEditText = view.findViewById(R.id.editTextLastNameEditProfile);
        radioGroupGender = view.findViewById(R.id.radioGroupGenderEditProfile);
        dateOfBirthEditText = view.findViewById(R.id.editTextDateOfBirthEditProfile);

        dateOfBirthEditText.setOnClickListener(this);

        User user = healthMetricsDbHelper.getUser();

        firstNameEditText.setText(user.FirstName);
        lastNameEditText.setText(user.LastName);
        dateOfBirthEditText.setText(user.DateOfBirth);
        if (user.Gender.equals("Female")) {
            radioGroupGender.check(R.id.radioButtonFemaleEditProfile);
        } else {
            radioGroupGender.check(R.id.radioButtonMaleEditProfile);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditProfile) {
            editUserProfile();
        } else if (v.getId() == R.id.editTextDateOfBirthEditProfile) {
            DatePickerFragment datePickerFragment = new DatePickerFragment();
            datePickerFragment.setOnDateSetListener(this);
            datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
        }
    }

    public void editUserProfile() {

        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String dateOfBirth = dateOfBirthEditText.getText().toString();
        String gender = "Female";

        if (radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMaleEditProfile) {
            gender = "Male";
        }

        if (firstName.matches("") || lastName.matches("") | dateOfBirth.matches("")) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        } else {

            int updateStatus = healthMetricsDbHelper.updateUser(new User(firstName, lastName, gender, dateOfBirth));
            if (updateStatus != 1) {
                Toast.makeText(getActivity(), "Error updating profile.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                ViewProfileFragment viewProfileFragment = new ViewProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewProfileFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
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
