package com.example.cps_lab411.EvenBus;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class UpdateNavigationPath {
    private List<LatLng> latLngList = new ArrayList<>();

    public void setLatLngList(List<LatLng> latLngList) { this.latLngList = latLngList; }

    public List<LatLng> getLatLngList() { return latLngList; }

}
