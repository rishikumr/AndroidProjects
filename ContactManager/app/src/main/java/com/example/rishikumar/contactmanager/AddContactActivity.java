package com.example.rishikumar.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends AppCompatActivity {
EditText Edname , Ednumber;
String mName , mNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        Edname = findViewById(R.id.name1);

        Ednumber = findViewById(R.id.number1);
        Button  save = findViewById(R.id.save_contact);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mName = Edname.getText().toString();
                mNumber = Ednumber.getText().toString();


                Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                contactIntent
                        .putExtra(ContactsContract.Intents.Insert.NAME, mName)
                        .putExtra(ContactsContract.Intents.Insert.PHONE, mNumber);

                startActivityForResult(contactIntent, 1);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
