package com.example.addressbookapp;

public class Contact {

    long id;
    String name;
    String phone;
    String email;
    String street;
    String city_st_zip;

    public Contact() { super(); }

    public Contact(long ID, String n, String p, String e, String s, String c) {
        id = ID;
        name = n;
        phone = p;
        email = e;
        street = s;
        city_st_zip = c;
    }

    public long getID() {
        return id;
    }

    public void setID(long ID) {
        id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String p) {
        phone = p;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        email = e;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String s) {
        street = s;
    }

    public String getCity_st_zip() {
        return city_st_zip;
    }

    public void setCity_st_zip(String c) {
        city_st_zip = c;
    }

}
