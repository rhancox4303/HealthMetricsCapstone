package ca.mohawk.HealthMetrics.DataEntry;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.R;


/**
 * The EmailShareFragment class is an extension of the Fragment class.
 * Allows the user to share data via email.
 */
public class EmailShareFragment extends Fragment implements View.OnClickListener {

    // Instantiate the EditText variables.
    private EditText recipientEditText;
    private EditText subjectEditText;
    private EditText messageEditText;

    public EmailShareFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_email_share, container,
                false);

        // Get the views.
        recipientEditText = rootView.findViewById(R.id.editTextRecipientEmailShare);
        subjectEditText = rootView.findViewById(R.id.editTextSubjectLineEmailShare);
        messageEditText = rootView.findViewById(R.id.editTextMessageEmailShare);

        Button sendButton = rootView.findViewById(R.id.buttonSendEmailShare);

        // Set the sendButton OnClickListener.
        sendButton.setOnClickListener(this);

        // Instantiate the HealthMetricsDbHelper variable.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        int metricId = -1;

        // Get the metric id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("metric_id_key", -1);
        }

        // Get the list of data entries from the database.
        List<DataEntryDisplayObject> dataEntries = healthMetricsDbHelper.getDataEntriesByMetricId(metricId);

        // Get the metric from the database.
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);

        // Validate the metric is not null.
        if (metric != null) {
            // Call the composeMessage method with metric and dataEntries passed in.
            composeMessage(metric, dataEntries);
        } else {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load metric from database.",
                    Toast.LENGTH_SHORT).show();

            navigateToMetricsListFragment();
        }
        return rootView;
    }


    /**
     * Start an email intent to with the user's inputted data passed in.
     *
     * @param address Represents the recipient's email address.
     * @param subject Represents the subject of the email.
     * @param message Represents the message of the email.
     */
    private void sendEmail(String address, String subject, String message) {

        // Create the emailIntent
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        // Pass the email data as extras.
        emailIntent.setData(Uri.parse("mailto: " + address));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        // Validate the activity and start the intent if is valid.
        if (emailIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
            startActivity(emailIntent);
        }
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
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    public boolean validateUserInput() {

        // If any field is empty, inform the user and return false.
        if (recipientEditText.getText().toString().trim().equals("") ||
                messageEditText.getText().toString().trim().equals("") ||
                subjectEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please fill in all empty fields.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the recipient's email is not a valid email, inform the user and return false.
        if (!Patterns.EMAIL_ADDRESS.matcher(recipientEditText.getText().toString().trim()).matches()) {
            Toast.makeText(getActivity(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        // Validate the user input.
        if (validateUserInput()) {

            // Get the message, address and subject from the fields.
            String message = messageEditText.getText().toString();
            String address = recipientEditText.getText().toString();
            String subject = subjectEditText.getText().toString();

            // Call the sendEmail with the address, subject and email passed in.
            sendEmail(address, subject, message);
        }
    }

    /**
     * Composes the email message and display it to the user.
     *
     * @param metric      Represents the metric the email is about.
     * @param dataEntries Represents the data entries of the metric.
     */
    private void composeMessage(Metric metric, List<DataEntryDisplayObject> dataEntries) {

        // Create a new String Builder.
        StringBuilder builder = new StringBuilder();

        // Add the metric name to the top of the message.
        builder.append(String.format("%s Data\n", metric.Name));

        // Add each data latestDataEntry to the message.
        for (DataEntryDisplayObject dataEntry : dataEntries) {
            builder.append(String.format("%s : %s %s\n", dataEntry.dateOfEntry, dataEntry.data, dataEntry.unitAbbreviation));
        }

        // Display the message.
        messageEditText.setText(builder);
    }
}