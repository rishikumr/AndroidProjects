package com.dynasty.myapplication.entity;

import java.util.ArrayList;

public class People {
    private String guest_name, guest_number;
    private int guest_acceptance;  // 0 : Not responded , 1 : Accepted , 2: Declined

    public People(String guest_name, String guest_mobileNumber, int guest_acceptance) {
        this.guest_name = guest_name;
        this.guest_number = guest_mobileNumber;
        this.guest_acceptance = guest_acceptance;
    }
    public People() {
    }

    public String getguest_name() {
        return guest_name;
    }
    public String getguest_number() {
        return guest_number;
    }
    public int getguest_acceptance() {
        return guest_acceptance;
    }

    public String getGuest_acceptance_String() {
        return (guest_acceptance == 0 )  ? "No Response" : (guest_acceptance == 1) ? "Accepted" : "Declined";
    }

    public void setGuest_acceptance(int guest_acceptance) {
        this.guest_acceptance = guest_acceptance;
    }

    public ArrayList<People> getDummyPeopleList(){
        ArrayList<People> dummyList = new ArrayList<>();
        dummyList.add(new People("Ram","9900990001", 1));
        dummyList.add(new People("Ram","9900990001", 1));
        dummyList.add(new People("Sam","9900990002", 1));
        dummyList.add(new People("Tam","9900990003", 2));
        dummyList.add(new People("Cam","9900990004", 0));
        dummyList.add(new People("Ram","9900990001", 1));
        dummyList.add(new People("Sam","9900990002", 1));
        dummyList.add(new People("Tam","9900990003", 2));
        dummyList.add(new People("Cam","9900990004", 0));
        dummyList.add(new People("Ram","9900990001", 1));
        dummyList.add(new People("Sam","9900990002", 1));
        dummyList.add(new People("Tam","9900990003", 2));
        dummyList.add(new People("Cam","9900990004", 0));
        dummyList.add(new People("Sam","9900990002", 1));
        dummyList.add(new People("Tam","9900990003", 2));
        dummyList.add(new People("Cam","9900990004", 0));
        return dummyList;
    }
}
