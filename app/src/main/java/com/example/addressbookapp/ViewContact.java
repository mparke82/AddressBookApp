package com.example.addressbookapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class ViewContact extends AppCompatActivity {
    TextView UIname, UIphone, UIemail, UIstreet, UIcsz;
    private static final int MENU_ITEM_1 = 1;
    private static final int MENU_ITEM_2 = 2;

    long ContactID = -1;
    String[] Contact;
    private ContactDBAdaptor contactDBAdaptor;

    private boolean isStop;
    int count = 0;
    private MyAsyncTask myTask;
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        private int counter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            counter = 0;
            Toast.makeText(getApplicationContext(),"Thread ID is: "+Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            isStop = true;
            count = integer;

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(),"Thread ID is: "+Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("onProgressUpdate: ", "Thread ID is: "+Thread.currentThread().getId());
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            counter = integers[0];
            Log.i("doInbackground: ", "Thread ID is: "+Thread.currentThread().getId());

            contactDBAdaptor.delete(ContactID);

            counter++;
            return counter;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(),"Thread ID is: "+Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        UIname = (TextView)findViewById(R.id.showName);
        UIphone = (TextView)findViewById(R.id.showPhone);
        UIemail = (TextView)findViewById(R.id.showEmail);
        UIstreet = (TextView)findViewById(R.id.showStreet);
        UIcsz = (TextView)findViewById(R.id.showCSZ);
        contactDBAdaptor = ContactDBAdaptor.getContactDBAdapterInstance(this);
        isStop = false;

        ContactID = getIntent().getLongExtra("id", ContactID);
        Contact = new String[5];
        Contact = getIntent().getStringArrayExtra("Contact");

        if (Contact.length > 0 && ContactID != -1) {

            Contact = getIntent().getStringArrayExtra("Contact");
            UIname.setText(Contact[0]);
            UIphone.setText(Contact[1]);
            UIemail.setText(Contact[2]);
            UIstreet.setText(Contact[3]);
            UIcsz.setText(Contact[4]);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_1, Menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, MENU_ITEM_2, Menu.NONE, "Delete Contact");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_ITEM_1:
                Intent editContact = new Intent(getApplicationContext(), EditContact.class);
                editContact.putExtra("Contact", Contact);
                editContact.putExtra("id", ContactID);
                startActivity(editContact);
                return true;
            case MENU_ITEM_2:
                AlertDialog.Builder adb = new AlertDialog.Builder(ViewContact.this);
                adb.setTitle("Are you sure?");
                adb.setMessage("This will permanently delete the contact");
                adb.setCancelable(true);
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myTask = new MyAsyncTask();
                        myTask.execute(count);
                    }
                });
                AlertDialog confirmD = adb.create();
                confirmD.show();
                return true;
            default:
                return false;
        }
    }
}
