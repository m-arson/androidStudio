package com.example.bluecontroller;

public class Devices {
    private String cName;
    private String address;

    public Devices(String name, String address) {
        this.cName = name;
        this.address = address;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
