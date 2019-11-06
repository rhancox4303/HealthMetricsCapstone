package ca.mohawk.HealthMetrics.UserProfile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.R;


/**
 * The ViewUserFragment class is an extension of the Fragment class.
 * It is used to view the user's profile information.
 */
public class ViewUserFragment extends Fragment implements View.OnClickListener {

    public ViewUserFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method initializes the view variables
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);

        // Instantiate the HealthMetricsDbHelper object.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Instantiate the edit User button.
        Button editUserButton = rootView.findViewById(R.id.buttonNavigateToEditProfile);

        // Set the onClickListener for the editUserButton.
        editUserButton.setOnClickListener(this);

        // Instantiate the layout elements.
        TextView firstNameView = rootView.findViewById(R.id.textViewFirstNameDisplay);
        TextView lastNameView = rootView.findViewById(R.id.textViewLastNameDisplay);
        TextView genderView = rootView.findViewById(R.id.textViewGenderDisplay);
        TextView dateOfBirthView = rootView.findViewById(R.id.textViewBirthDateDisplay);

        // Get the user from the database.
        User user = healthMetricsDbHelper.getUser();

        // If the returned user is null then display a message to the user.
        if (user == null) {
            Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();

            // Else then display the user's information.
        } else {
            // Fill the fields with the user data.
            firstNameView.setText(user.FirstName);
            lastNameView.setText(user.LastName);
            genderView.setText(user.Gender);
            dateOfBirthView.setText(user.DateOfBirth);
        }

        //Return the rootView.
        return rootView;
    }

    /**
     * The onClick method runs when the a view's onClickListener is activated.
     * It displays the EditProfileFragment when the method is run.
     */
    @Override
    public void onClick(View v) {
        EditProfileFragment editProfileFragment = new EditProfileFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }
}
