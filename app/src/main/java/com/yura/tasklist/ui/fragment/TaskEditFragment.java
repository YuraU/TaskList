package com.yura.tasklist.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yura.tasklist.R;
import com.yura.tasklist.mvp.model.Task;
import com.yura.tasklist.databinding.FragmentTaskBinding;
import com.yura.tasklist.mvp.presenters.TaskEditPresenter;
import com.yura.tasklist.mvp.views.TaskEditView;

import java.util.Calendar;

public class TaskEditFragment extends MvpAppCompatFragment implements TaskEditView{

    public interface TaskEditFragmentListener {
        void onSaveClick();
    }

    @InjectPresenter
    TaskEditPresenter taskEditPresenter;

    private FragmentTaskBinding binder;
    private TaskEditFragmentListener listener;
    private GoogleMap map;

    private Marker marker;

    private Task task;

    public static TaskEditFragment getInstance(Task task, TaskEditFragmentListener listener) {
        TaskEditFragment fragment = new TaskEditFragment();
        fragment.task = task;
        fragment.listener = listener;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task, container, false);

        binder.mapView.onCreate(savedInstanceState);
        binder.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.setMyLocationEnabled(true);
                    }



                    MapsInitializer.initialize(getActivity());


                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if(marker == null) {
                                marker = map.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("Напоминание")
                                        .snippet("Напомнить тут")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            }else {
                                marker.setPosition(latLng);
                            }
                        }
                    });

                    if(task.getLat() != null && task.getLng() != null) {
                        marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(task.getLat(), task.getLng()))
                                .title("Напоминание")
                                .snippet("Напомнить тут")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(task.getLat(), task.getLng()), 10);
                                map.animateCamera(cameraUpdate);
                    }


                    }
            });



        binder.timePicker.setIs24HourView(true);

        binder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binder.editText.getText().toString().equals("")){
                    Toast toast = Toast.makeText(TaskEditFragment.this.getContext(),
                            "Укажите что нужно напомнить", Toast.LENGTH_SHORT);
                    toast.show();

                    return;
                }

                if(binder.mapRadioButton.isChecked() && marker == null){
                    Toast toast = Toast.makeText(TaskEditFragment.this.getContext(),
                            "Укажите место где нужно напомнить", Toast.LENGTH_SHORT);
                    toast.show();

                    return;
                }
                taskEditPresenter.saveTask(getTask(), getContext());
            }
        });

        binder.timeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.mapRadioButton.setChecked(false);
                taskEditPresenter.onTimeRadioBtnClick();
            }
        });

        binder.mapRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binder.timeRadioButton.setChecked(false);
                taskEditPresenter.onMapRadioBtnClick();
            }
        });

        if(task.getMsg() != null){
            binder.editText.setText(task.getMsg());
        }

        if(task.getDate() != null){
            binder.timePicker.setCurrentHour(task.getDate().get(Calendar.HOUR));
            binder.timePicker.setCurrentMinute(task.getDate().get(Calendar.MINUTE));
        }else {
            binder.mapRadioButton.setChecked(true);
            binder.timeRadioButton.setChecked(false);
            showMap();
        }

        View view = binder.getRoot();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        binder.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binder.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder.mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binder.mapView.onLowMemory();
    }

    private Task getTask(){
        task.setMsg(binder.editText.getText().toString());
        if(binder.timeRadioButton.isChecked()){
            Calendar сalendar = Calendar.getInstance();
            сalendar.set(Calendar.HOUR, binder.timePicker.getCurrentHour());
            сalendar.set(Calendar.MINUTE, binder.timePicker.getCurrentMinute());

            if(Calendar.getInstance().getTimeInMillis() > сalendar.getTimeInMillis()){
                сalendar.setTimeInMillis(сalendar.getTimeInMillis() + 86400000);
            }

            task.setDate(сalendar);
        }else {
            task.setLocation(marker.getPosition());
        }
        return task;
    }

    private void showTime(){
        binder.timePicker.setVisibility(View.VISIBLE);
        binder.mapView.setVisibility(View.GONE);
    }

    private void showMap(){
        binder.mapView.setVisibility(View.VISIBLE);
        binder.timePicker.setVisibility(View.GONE);
    }

    @Override
    public void closeTask() {
        if(listener != null)
            listener.onSaveClick();
    }

    @Override
    public void showTaskTime() {
        showTime();
    }

    @Override
    public void showTaskPosition() {
        showMap();
    }

}
