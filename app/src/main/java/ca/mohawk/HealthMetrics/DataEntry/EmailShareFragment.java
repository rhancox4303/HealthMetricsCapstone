package ca.mohawk.HealthMetrics.DataEntry;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.StringJoiner;

import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryRecyclerViewObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.MetricDataEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailShareFragment extends Fragment implements View.OnClickListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;
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
        View rootView = inflater.inflate(R.layout.fragment_email_share, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        recipientEditText = rootView.findViewById(R.id.editTextRecipientEmailShare);
        subjectEditText = rootView.findViewById(R.id.editTextSubjectLineEmailShare);
        messageEditText = rootView.findViewById(R.id.editTextMessageEmailShare);



        Button button = rootView.findViewById(R.id.buttonSendEmailShare);
        button.setOnClickListener(this);

        int MetricId = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id_key", -1);
        }

        composeMessage(MetricId);
        return rootView;
    }

    public void sendEmail(String address, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: " + address)); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        if (emailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }
    public boolean validateUserInput(){
       if(recipientEditText.getText().toString().trim().equals("") ||
               messageEditText.getText().toString().trim().equals("") ||
               subjectEditText.getText().toString().trim().equals("")){
           Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
           return false;
       }
       return true;
    }
    @Override
    public void onClick(View v) {
        if(validateUserInput()){
            String message = messageEditText.getText().toString();
            String address = recipientEditText.getText().toString();
            String subject = subjectEditText.getText().toString();
            sendEmail(address,subject,message);
        }
    }
    public void composeMessage(int metricId){
        List<DataEntryRecyclerViewObject> dataEntries = healthMetricsDbHelper.getDataEntriesByMetricId(metricId);
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s Data\n",metric.Name));
        for(DataEntryRecyclerViewObject dataEntry : dataEntries){
            builder.append(String.format("%s : %s %s\n", dataEntry.DateOfEntry,dataEntry.DataEntry,dataEntry.UnitAbbreviation));
        }
        messageEditText.setText(builder);
    }
}
