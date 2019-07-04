package ca.mohawk.HealthMetrics.UserProfile;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The ViewProfileFragment class is an extension of the Fragment class.
 * It is used to view the user's profile information.
 */
public class ViewProfileFragment extends Fragment implements View.OnClickListener  {
    HealthMetricsDbHelper healthMetricsDbHelper;

    public ViewProfileFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method initializes the view variables 
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_profile, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Button editButton = (Button) view.findViewById(R.id.buttonNavigateToEditProfile);

        editButton.setOnClickListener(this);

        TextView firstNameView = view.findViewById(R.id.textViewFirstNameDisplay);
        TextView lastNameView = view.findViewById(R.id.textViewLastNameDisplay);
        TextView genderView = view.findViewById(R.id.textViewGenderDisplay);
        TextView dateOfBirthView = view.findViewById(R.id.textViewBirthDateDisplay);
        
        //Get the user from the database.
        User user = healthMetricsDbHelper.getUser();
        
        //Fill the fields with the user data.
        firstNameView.setText(user.FirstName);
        lastNameView.setText(user.LastName);
        genderView.setText(user.Gender);
        dateOfBirthView.setText(user.DateOfBirth);

        return view;
    }
    /**
     * The onClick method runs when the a view's onClickListener is activated.
     * It displays the EditProfileFragment when the method is run.
     */
    @Override
    public void onClick(View v) {
        EditProfileFragment editProfileFragment= new EditProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }
}
