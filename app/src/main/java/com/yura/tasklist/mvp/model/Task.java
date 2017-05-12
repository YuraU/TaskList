package com.yura.tasklist.mvp.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.orm.dsl.Table;

import java.util.Calendar;

@Table
public class Task {
    private Long id;
    private String msg;
    private Calendar date;
    private Double lat;
    private Double lng;
    private boolean come;

    public Task(){

    }

    private Task(String msg){
        this.msg = msg;
    }

    public Task(String msg, Calendar date){
        this(msg);
        this.date = date;
    }

    public Task(String msg, LatLng latLng){
        this(msg);
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setLocation(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public String getMsg() {
        return msg;
    }

    public Calendar getDate() {
        return date;
    }

    public boolean isCome() {
        return come;
    }

    public void setCome(boolean come) {
        this.come = come;
    }

    public Long getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
