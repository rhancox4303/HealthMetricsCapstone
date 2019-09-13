package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMetricFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, Spinner.OnItemSelectedListener  {

    private HealthMetricsDbHelper healthMetricsDbHelper;
    private TextView nameTextView;
    private TextView unitCategoryDisplayTextView;
    private Spinner unitCategorySpinner;
    private RadioGroup metricTypeRadioGroup;
    private EditText metricNameEditText;
    private int unitCategoryId;

    public CreateMetricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_metric, container, false);

       // Button createMetricButton = rootView.findViewById(R.id.buttonCreateMetric);
        //createMetricButton.setOnClickListener(this);
        //metricNameEditText = rootView.findViewById(R.id.editTextMetricNameCreateMetric);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        List<UnitCategory> unitCategoriesList = healthMetricsDbHelper.getAllUnitCategories();

        CreateMetricInputFragment addMetricFragment= new CreateMetricInputFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.CreateMetricFragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();

        //nameTextView = rootView.findViewById(R.id.textViewDisplayMetricNameCreateMetric);
       // unitCategoryDisplayTextView = rootView.findViewById(R.id.textViewDisplayUnitCategoryCreateMetric);
        //unitCategorySpinner = rootView.findViewById(R.id.spinnerUnitCategoryCreateMetric);

        ArrayAdapter<UnitCategory> unitCategoryAdapater = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, unitCategoriesList);
//        unitCategorySpinner.setAdapter(unitCategoryAdapater);
      //  unitCategorySpinner.setOnItemSelectedListener(this);

        metricTypeRadioGroup = rootView.findViewById(R.id.radioGroupMetricTypeCreateMetric);
        metricTypeRadioGroup.setOnCheckedChangeListener(this);
        return rootView;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch(checkedId){
            case R.id.radioButtonQuantitativeCreateMetric:
                CreateMetricInputFragment addMetricFragment= new CreateMetricInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.radioButtonGalleryCreateMetric:
                CreatePhotoGalleryInputFragment addGalleryFragment= new CreatePhotoGalleryInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, addGalleryFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.radioButtonNoteCreateMetric:
                CreateNoteInputFragment noteInputFragment= new CreateNoteInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, noteInputFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }

    public Metric createNewMetric(){
        String metricName = metricNameEditText.getText().toString();
        Metric newMetric = new Metric(0,metricName,unitCategoryId,0);
        return  newMetric;
    }
    public PhotoGallery createNewGallery(){
        String galleryName = metricNameEditText.getText().toString();
        PhotoGallery photoGallery = new PhotoGallery(galleryName,0);
        return  photoGallery;
    }

    @Override
    public void onClick(View v) {
        if(metricTypeRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonQuantitativeCreateMetric && !metricNameEditText.getText().toString().equals("") ){
        Metric newMetric = createNewMetric();
        healthMetricsDbHelper.addMetric(newMetric);

        AddMetricFragment addMetricFragment= new AddMetricFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();

        }else if(!metricNameEditText.getText().toString().equals("")) {
            PhotoGallery photoGallery = createNewGallery();
            healthMetricsDbHelper.addPhotoGallery(photoGallery);

            AddMetricFragment addMetricFragment= new AddMetricFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, addMetricFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitCategoryId = ((UnitCategory)parent.getSelectedItem()).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
