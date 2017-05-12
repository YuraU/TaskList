package com.yura.tasklist.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.yura.tasklist.R;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.databinding.ActivityMainBinding;
import com.yura.tasklist.service.LocationService;
import com.yura.tasklist.ui.fragment.TaskEditFragment;
import com.yura.tasklist.ui.fragment.TaskListFragment;


public class MainActivity extends MvpAppCompatActivity implements TaskEditFragment.TaskEditFragmentListener, TaskListFragment.TaskListFragmentListener{
    public static final int REQUEST_LOCATION = 1;

    private ActivityMainBinding binder;
    private TaskListFragment taskListFragment;
    private TaskEditFragment taskEditFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binder = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binder.toolbar);

        taskListFragment = TaskListFragment.getInstance(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(binder.contentMain.getId(), taskListFragment).commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();

        requestLocationPermission();


    }

    private void requestLocationPermission(){
        if(PackageManager.PERMISSION_GRANTED== ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)){
            startService(new Intent(this, LocationService.class));
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_LOCATION);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showTaskFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(binder.contentMain.getId(), taskEditFragment).addToBackStack(null).commitAllowingStateLoss();

    }

    @Override
    public void onAddTaskClick() {
        taskEditFragment = TaskEditFragment.getInstance(new Task(), this);

        showTaskFragment();
    }

    @Override
    public void onTaskClick(final Task task) {
        taskEditFragment = TaskEditFragment.getInstance(task, this);

        showTaskFragment();
    }


    @Override
    public void onSaveClick() {
        onBackPressed();

        taskListFragment.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if(requestCode == REQUEST_LOCATION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService(new Intent(this, LocationService.class));
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

}
