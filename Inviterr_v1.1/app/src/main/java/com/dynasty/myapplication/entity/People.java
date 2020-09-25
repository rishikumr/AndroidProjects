package com.dynasty.myapplication.entity;

import java.util.ArrayList;

public class People {
    private String invitee_name, invitee_number;
    private boolean invitee_acceptance;

    public People(String invitee_name, String invitee_mobileNumber, boolean invitee_acceptance) {
        this.invitee_name = invitee_name;
        this.invitee_number = invitee_mobileNumber;
        this.invitee_acceptance = invitee_acceptance;
    }
    public People() {
    }

    public String getInvitee_name() {
        return invitee_name;
    }
    public String getInvitee_number() {
        return invitee_number;
    }
    public boolean getInvitee_acceptance() {
        return invitee_acceptance;
    }

    public void setInvitee_acceptance(boolean invitee_acceptance) {
        this.invitee_acceptance = invitee_acceptance;
    }

    public ArrayList<People> getDummyPeopleList(){
        ArrayList<People> dummyList = new ArrayList<>();
        dummyList.add(new People("Ram","9900990001", true));
        dummyList.add(new People("Sam","9900990002", false));
        dummyList.add(new People("Tam","9900990003", false));
        dummyList.add(new People("Cam","9900990004", false));
        return dummyList;
    }
}
