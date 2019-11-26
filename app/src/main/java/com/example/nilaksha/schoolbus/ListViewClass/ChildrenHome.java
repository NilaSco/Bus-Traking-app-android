package com.example.nilaksha.schoolbus.ListViewClass;

/**
 * Created by Nilaksha on 11/4/2019.
 */

public class ChildrenHome {

    private String name;
    private String school;
    private String id;
    private String lat;
    private String lng;

    public ChildrenHome(String name, String school, String id, String lat, String lng) {
        this.name = name;
        this.school = school;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
