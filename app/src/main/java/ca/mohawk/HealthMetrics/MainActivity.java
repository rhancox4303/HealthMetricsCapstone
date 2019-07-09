package ca.mohawk.HealthMetrics;

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

import ca.mohawk.HealthMetrics.MetricManagement.MetricsViewFragment;
import ca.mohawk.HealthMetrics.Notification.NotificationListFragment;
import ca.mohawk.HealthMetrics.Prescription.PrescriptionListFragment;
import ca.mohawk.HealthMetrics.UserProfile.CreateUserFragment;
import ca.mohawk.HealthMetrics.UserProfile.ViewProfileFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    HealthMetricsDbHelper healthMetricsDbHelper;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            MetricsViewFragment metricsViewFragment = new MetricsViewFragment();
            fragmentTransaction.add(R.id.fragmentContainer, metricsViewFragment);
        } else {

            //Create and seed the DB
            healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(this);
            healthMetricsDbHelper.seedUnitCategories();
            healthMetricsDbHelper.seedUnits();
            healthMetricsDbHelper.seedMetrics();

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
            MetricsViewFragment metricsViewFragment = new MetricsViewFragment();
            fragmentTransaction.replace(R.id.fragmentContainer, metricsViewFragment);

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
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.commit();
    }
}
