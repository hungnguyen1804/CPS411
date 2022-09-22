package com.example.cps_lab411.Category;

public class CategoryMode {
    private String flightMode;
    private int flightModeID;

    public CategoryMode(String flightMode, int flightModeID) {
        this.flightMode = flightMode;
        this.flightModeID = flightModeID;
    }

    public String getName() {
        return flightMode;
    }
    public int getFlightModeID() { return flightModeID; }

    public void setName(String flightMode) {
        this.flightMode = flightMode;
    }
}
