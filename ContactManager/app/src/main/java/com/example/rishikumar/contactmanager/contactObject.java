package com.example.rishikumar.contactmanager;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

/**
 * Created by rishi.kumar on 12/4/2017.
 */

public class contactObject  implements Comparable
{

    private long id;
    private String name;
    private String number;

    public contactObject( String name, String number) {

        this.name = name;
        this.number = number;

    }

    public String getName() {
        return name;
    }

    public String getNumber() {    return number;    }

    public void SetName(String name) {
        this.name= name;
    }

    public void SetNumber(String number) {
        this.number= number;
    }

    @Override
    public int compareTo(@NonNull Object o ) {
        String comp = ((contactObject) o).getName();
        return this.getName().compareTo(comp);
    }


}
