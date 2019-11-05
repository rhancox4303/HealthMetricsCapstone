package ca.mohawk.HealthMetrics;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import ca.mohawk.HealthMetrics.DataEntry.DeleteDataEntryDialog;
import ca.mohawk.HealthMetrics.DataEntry.DataEntryListFragment;
import ca.mohawk.HealthMetrics.MetricManagement.DeleteMetricDialog;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.MetricManagement.RemoveMetricDialog;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Note.DeleteNoteDialog;
import ca.mohawk.HealthMetrics.Notification.DeleteNotificationDialog;
import ca.mohawk.HealthMetrics.Notification.NotificationListFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.DeleteGalleryDialog;
import ca.mohawk.HealthMetrics.PhotoGallery.DeletePhotoEntryDialog;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoGalleryFragment;
import ca.mohawk.HealthMetrics.Prescription.DeletePrescriptionDialog;
import ca.mohawk.HealthMetrics.Prescription.PrescriptionListFragment;
import ca.mohawk.HealthMetrics.UserProfile.CreateUserFragment;
import ca.mohawk.HealthMetrics.UserProfile.ViewProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DeleteMetricDialog.DeleteMetricDialogListener, DeleteDataEntryDialog.DeleteDataEntryDialogListener,
        DeletePrescriptionDialog.DeletePrescriptionDialogListener, RemoveMetricDialog.RemoveMetricDialogListener,
        DeleteNoteDialog.DeleteNoteDialogListener, DeletePhotoEntryDialog.DeletePhotoEntryDialogListener,
        DeleteGalleryDialog.DeleteGalleryDialogListener, DeleteNotificationDialog.DeleteNotificationDialogListener {

    private static final int CAMERA_REQUEST_CODE = 2000;
    private static final int CAMERA_PERMISSION_CODE = 100;

    HealthMetricsDbHelper healthMetricsDbHelper;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);

        SharedPreferences userInfo = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = userInfo.edit();
        boolean loggedIn = userInfo.getBoolean("loggedIn", false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (loggedIn) {
            MetricsListFragment metricsListFragment = new MetricsListFragment();
            fragmentTransaction.add(R.id.fragmentContainer, metricsListFragment);
        } else {

            //seed the DB
            healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);
            healthMetricsDbHelper.seedDatabase();

            CreateUserFragment createUserFragment = new CreateUserFragment();
            fragmentTransaction.add(R.id.fragmentContainer, createUserFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            ViewProfileFragment profileFragment = new ViewProfileFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, profileFragment);

        } else if (id == R.id.nav_metrics) {
            MetricsListFragment metricsListFragment = new MetricsListFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, metricsListFragment);

        } else if (id == R.id.nav_notifications) {
            NotificationListFragment notificationListFragment = new NotificationListFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, notificationListFragment);

        } else if (id == R.id.nav_prescriptions) {

            PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, prescriptionListFragment);
        }

        fragmentTransaction.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchContent(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDeleteMetricDialogPositiveClick(DeleteMetricDialog dialog) {
        boolean deleteSuccessful = false;

        MetricsListFragment metricsListFragment = new MetricsListFragment();
        deleteSuccessful = healthMetricsDbHelper.deleteMetric(dialog.getMetricId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, metricsListFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteMetricDialogNegativeClick(DeleteMetricDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog dialog) {
        boolean deleteSuccessful = false;

        DataEntryListFragment dataEntryListFragment = new DataEntryListFragment();
        deleteSuccessful = healthMetricsDbHelper.deleteDataEntry(dialog.getDataEntryId());

        Bundle metricBundle = new Bundle();
        metricBundle.putInt("selected_item_key", dialog.getMetricId());

        dataEntryListFragment.setArguments(metricBundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, dataEntryListFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog dialog) {

        boolean deleteSuccessful = false;

        PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();
        deleteSuccessful = healthMetricsDbHelper.deletePrescription(dialog.getPrescriptionId());


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, prescriptionListFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onRemoveMetricDialogPositiveClick(RemoveMetricDialog dialog) {

        boolean removeSuccessful = false;

        boolean deleteSuccessful = healthMetricsDbHelper.deleteDataEntryByMetricId(dialog.getMetricId());

        if (!deleteSuccessful) {
            MetricsListFragment metricsListFragment = new MetricsListFragment();

            removeSuccessful = healthMetricsDbHelper.removeMetricFromProfile(dialog.getMetricId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, metricsListFragment)
                    .addToBackStack(null)
                    .commit();
        }

        if (removeSuccessful) {
            Toast.makeText(this, "Remove was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Remove was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveMetricDialogNegativeClick(RemoveMetricDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeleteNoteDialogPositiveClick(DeleteNoteDialog dialog) {
        boolean deleteSuccessful = false;

        MetricsListFragment metricsListFragment = new MetricsListFragment();
        deleteSuccessful = healthMetricsDbHelper.deleteNoteById(dialog.getNoteId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, metricsListFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteNoteDialogNegativeClick(DeleteNoteDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog dialog) {

        boolean deleteSuccessful = false;

        ViewPhotoGalleryFragment viewPhotoGalleryFragment = new ViewPhotoGalleryFragment();

        deleteSuccessful = healthMetricsDbHelper.deletePhotoEntryById(dialog.getPhotoEntryId());

        Bundle photoEntryBundle = new Bundle();
        photoEntryBundle.putInt("selected_item_key", dialog.getGalleryId());

        viewPhotoGalleryFragment.setArguments(photoEntryBundle);

        if (deleteSuccessful && dialog.getIsFromGallery() == 0) {
            File photoEntryFile = new File(dialog.getPhotoEntryPath());
            photoEntryFile.delete();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, viewPhotoGalleryFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog dialog) {
        boolean deleteSuccessful = false;

        MetricsListFragment metricsListFragment = new MetricsListFragment();

        deleteSuccessful = healthMetricsDbHelper.deleteGallery(dialog.getGalleryId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, metricsListFragment)
                .addToBackStack(null)
                .commit();

        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog dialog) {
        int id = dialog.getNotificationId();
        NotificationListFragment notificationListFragment = new NotificationListFragment();
        Notification notification = healthMetricsDbHelper.getNotificationById(id);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, 0);
        alarmManager.cancel(pendingIntent);

        boolean deleteSuccessful = healthMetricsDbHelper.deleteNotification(dialog.getNotificationId());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, notificationListFragment)
                .addToBackStack(null)
                .commit();
        if (deleteSuccessful) {
            Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion was not successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog dialog) {
        dialog.dismiss();
    }
}