package ca.mohawk.HealthMetrics.UserProfile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewProfileFragment extends Fragment implements View.OnClickListener  {
    HealthMetricsDbHelper healthMetricsDbHelper;

    public ViewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Button editButton = (Button) view.findViewById(R.id.buttonEditProfile);

        editButton.setOnClickListener(this);

        TextView firstNameView = view.findViewById(R.id.textViewFirstNameDisplay);
        TextView lastNameView = view.findViewById(R.id.textViewLastNameDisplay);
        TextView genderView = view.findViewById(R.id.textViewGenderDisplay);
        TextView dateOfBirthView = view.findViewById(R.id.textViewBirthDateDisplay);

        User user = healthMetricsDbHelper.GetUser();

        firstNameView.setText(user.FirstName);
        lastNameView.setText(user.LastName);
        genderView.setText(user.Gender);
        dateOfBirthView.setText(user.DateOfBirth);

        return view;
    }

    @Override
    public void onClick(View v) {
        EditProfileFragment editProfileFragment= new EditProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }
}
