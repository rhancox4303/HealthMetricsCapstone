package ca.mohawk.HealthMetrics.UserProfile;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_view_user, container, false);

        // Instantiate the HealthMetricsDbHelper object.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Instantiate the edit User button.
        Button editUserButton = rootView.findViewById(R.id.buttonEditUser);

        // Set the onClickListener for the editUserButton.
        editUserButton.setOnClickListener(this);

        // Instantiate the layout elements.
        TextView firstNameView = rootView.findViewById(R.id.textViewFirstNameDisplayViewUser);
        TextView lastNameView = rootView.findViewById(R.id.textViewLastNameDisplayViewUser);
        TextView genderView = rootView.findViewById(R.id.textViewGenderDisplayViewUser);
        TextView dateOfBirthView = rootView.findViewById(R.id.textViewBirthDateDisplayViewUser);

        // Get the user from the database.
        User user = healthMetricsDbHelper.getUser();

        // If the returned user is null then display a message to the user.
        if (user == null) {
            Toast.makeText(getActivity(), "User not found.", Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();

            // Else then display the user's information.
        } else {
            // Fill the fields with the user data.
            firstNameView.setText(user.firstName);
            lastNameView.setText(user.lastName);
            genderView.setText(user.gender);
            dateOfBirthView.setText(user.dateOfBirth);
        }

        //Return the rootView.
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
     * Runs when the edit user button is pressed.
     */
    @Override
    public void onClick(View v) {
        EditUserFragment editUserFragment = new EditUserFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, editUserFragment)
                .addToBackStack(null)
                .commit();
    }
}