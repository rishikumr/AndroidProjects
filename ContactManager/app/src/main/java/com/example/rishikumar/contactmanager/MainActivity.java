package com.example.rishikumar.contactmanager;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    LinearLayout ll;
    ContactAdapter adapter;
    SearchView searchview;
    ArrayList<contactObject> contacts = new ArrayList<contactObject>();
    String _id;
    private ActionMode mActionMode;

    public static final int PERMISSION_REQUEST_CODE = 11;

    String[] permissionslist = new String[]{
            Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private boolean DeleteMode = false;
    private boolean sortByname = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RunPermisiion();
            while (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                setContentView(R.layout.need_permission);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ll = (LinearLayout) findViewById(R.id.linearlayout1);

        listView = (ListView) findViewById(R.id.contactlist);
        adapter = new ContactAdapter(MainActivity.this, R.layout.contact_info, contacts);

        listView.setAdapter(adapter);
        LoadContactRefresh();


        // listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


      /*  listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {


        });*/
        listView.setTextFilterEnabled(true);


        searchview = (SearchView) findViewById(R.id.search);
        searchview.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // TODO Auto-generated method stub
                adapter.getFilter().filter(query);
                return false;
            }
        });

        registerForContextMenu(listView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContact();
            }
        });


    }

    private void LoadContactRefresh() {
        LoadContactsAyscn lca = new LoadContactsAyscn();
        lca.execute();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an Action");
        menu.add(0, v.getId(), 0, "Details");
        menu.add(0, v.getId(), 0, "Delete");


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // int index = info.position;
        TextView tv = info.targetView.findViewById(R.id.name);
        final String namee = tv.getText().toString();


        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(namee.toString().trim()));
        Cursor mapContact = this.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
        if (mapContact.moveToNext()) {
            _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));


        }


        if (item.getTitle() == "Details") {
            showDetailsContact(_id);


            Log.d("Log", "You pressed Details for :" + namee + " with id " + _id);

        } else if (item.getTitle() == "Delete") {

            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setMessage("Do you  want to delete selected record ?");

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO  Auto-generated method stub

                }
            });
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteContact(getApplicationContext(), _id.toString());

                    Log.d("Log", "You pressed Delete for :" + namee + " with id " + _id);
                }

            });
            AlertDialog alert = builder.create();
            alert.setIcon(R.drawable.addcontac);// dialog  Icon
            alert.setTitle("Confirmation"); // dialog  Title
            alert.show();
            return true;
        }

        return true;
    }


    private void showDetailsContact(String id) {

    }

    private void RunPermisiion() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionslist, PERMISSION_REQUEST_CODE);
            }
        }
    }

    class ActionBarCallBack implements ActionMode.Callback {


        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:

                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            MainActivity.this);
                    builder.setMessage("Do you  want to delete selected record(s)?");

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO  Auto-generated method stub

                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO  Auto-generated method stub
                            SparseBooleanArray selected = adapter
                                    .getSelectedIds();
                            // Captures all selected ids with a loop
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    contactObject selecteditem = adapter
                                            .getItem(selected.keyAt(i));

                                    String nammee = selecteditem.getName();
                                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(nammee.trim()));
                                    Cursor mapContact = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
                                    if (mapContact.moveToNext()) {
                                        _id = mapContact.getString(mapContact.getColumnIndex(ContactsContract.Contacts._ID));


                                    }
                                    deleteContact(MainActivity.this, _id);
                                    // Remove selected items following the ids
                                    adapter.remove(selecteditem);
                                }
                            }
                            // Close CAB
                            mode.finish();
                            adapter.notifyDataSetChanged();


                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.setIcon(R.drawable.addcontac);// dialog  Icon
                    alert.setTitle("Confirmation"); // dialog  Title
                    alert.show();
                    return true;

                case R.id.selectAll:
                    //
                    final int checkedCount = contacts.size();

                    Log.d("Log", "total pre - checked :" + adapter.getSelectedCount());
                    adapter.removeSelection();
                    for (int i = 0; i < checkedCount ; i++) {
                        Log.d("Log", "value of i= :" + i);
                        listView.getChildAt(i).setActivated(true);
                        adapter.selectView(i, true);

                    }
                    mode.setTitle(adapter.getSelectedCount() + "  Selected");
                    Log.d("Log", "total post - checked :" + adapter.getSelectedCount());
                    Log.d("Log", "tInteresting  : list " + adapter.getSelectedCount());
                    return true;

                case R.id.DeselectAll:
                    Log.d("Log", "total pre - checked :" + adapter.getSelectedCount());
                    final int UncheckedCount = contacts.size();
                    adapter.removeSelection();
                    for (int i = 0; i < UncheckedCount; i++) {
                        adapter.selectView(i, false);
                        listView.getChildAt(i).setActivated(false);
                    }
                    mode.setTitle(adapter.getSelectedCount() + "  Selected");
                    Log.d("Log", "total post - checked :" + adapter.getSelectedCount());
                    return true;


                default:
                    return false;
            }
        }

        @Override
        public boolean onCreateActionMode(final ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.activity_main, menu);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    adapter.toggleSelection(i);
                    if (listView.getChildAt(i).isActivated() == false) {

                        listView.getChildAt(i).setActivated(true);
                    } else {
                        listView.getChildAt(i).setActivated(false);
                    }
                    mode.setTitle(adapter.getSelectedCount() + "  Selected");

                    Log.d("Log", "total post - checked :" + adapter.getSelectedCount());
                }
            });
            return true;
        }


        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // TODO Auto-generated method stub

            final int UncheckedCount = contacts.size();

            for (int i = 0; i < UncheckedCount-1; i++) {
                adapter.selectView(i, false);
                listView.getChildAt(i).setActivated(false);
            }
            listView.setOnItemClickListener(null);

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<contactObject>> {


        @Override
        protected ArrayList<contactObject> doInBackground(Void... params) {
            // TODO Auto-generated method stub


            contacts.clear();
            ContentResolver cr = getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phNumber="";
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        System.out.println("name : " + name + ", ID : " + id);

                        // get the phone number
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                             phNumber = pCur.getString(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            System.out.println("phone" + phNumber);
                        }
                        pCur.close();
                    }
                    contacts.add(new contactObject(name,phNumber));
                }
                }


            /*Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,
                    null, null, null);

           while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.Contacts.NUMBER));
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    if (phNumber.equals((String) listView.getAdapter().getItem(i).toString())) {

                    } else {

                    }

                }


            }    c.close();*/
            cur.close();

            return contacts;
        }


        @Override
        protected void onPostExecute(ArrayList<contactObject> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);
            sortDynamically();
            adapter.getSelectedCount();

        }

    }

    private void sortDynamically() {
        if (sortByname == true) {

            Collections.sort(contacts);
        } else {
            Collections.sort(contacts, new Comparator<contactObject>() {
                @Override
                public int compare(contactObject data1, contactObject data2) {
                    if (data1.getNumber().toCharArray()[0] < data2.getNumber().toCharArray()[0])
                        return 1;
                    else
                        return 0;
                }


            });
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.deleteAll:
                mActionMode = MainActivity.this.startActionMode(new ActionBarCallBack());
                Log.d("Log", "You pressed deleteAll");
                return true;

            case R.id.addName:
                addContact();
                Log.d("Log", "You pressed addName");
                return true;
            case R.id.byname:
                Log.d("Log", "You pressed byname");
                sortByname = true;
                LoadContactRefresh();
                return true;
            case R.id.bynumber:

                sortByname = false;
                LoadContactRefresh();

                Log.d("Log", "You pressed bynumber");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {
                    setContentView(R.layout.need_permission);
                    Log.d("Log", "You Denied permissions");
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


    public void addContact() {

        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        /*contactIntent
                .putExtra(ContactsContract.Intents.Insert.NAME, "Contact Name")
                .putExtra(ContactsContract.Intents.Insert.PHONE, "5555555555");*/

        startActivityForResult(contactIntent, 1);


        //
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Added Contact ", Toast.LENGTH_SHORT).show();
                Log.d("Log", "Activity result OK");
                LoadContactRefresh();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Contact adding cancelled", Toast.LENGTH_SHORT).show();
                Log.d("Log", "Activity result Not Ok");

                LoadContactRefresh();
            }
        }
    }


    public boolean deleteContact(final Context context, final String contactId) {


        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactId);
        int deleted = context.getContentResolver().delete(uri, null, null);
        if (deleted > 0) LoadContactRefresh();
        return true;
    }
}
