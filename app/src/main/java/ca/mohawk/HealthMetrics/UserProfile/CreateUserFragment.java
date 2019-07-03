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
 * A simple {@link Fragment} subclass.
 */
public class CreateUserFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    public CreateUserFragment() {
        // Required empty public constructor
    }

    HealthMetricsDbHelper healthMetricsDbHelper;
    DatePickerFragment datePickerFragment = new DatePickerFragment();

    EditText dateOfBirthEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    RadioGroup genderRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().hide();
        View rootView =  inflater.inflate(R.layout.fragment_create_user, container, false);
        getActivity().setTitle("Create User Profile");

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

    public void createUser() {

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
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }else{

            healthMetricsDbHelper.addUser(new User(firstName,lastName,gender,dateOfBirth));

            Toast.makeText(getActivity(), "User added.", Toast.LENGTH_SHORT).show();

            SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor  editor = userInfo.edit();

            editor.putBoolean("loggedIn", true);
            editor.commit();

            MetricsViewFragment metricsViewFragment = new MetricsViewFragment();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer,metricsViewFragment )
                    .addToBackStack(null)
                    .commit();
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
