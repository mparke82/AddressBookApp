package com.example.addressbookapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MENU_ITEM_1 = 1;

    private boolean isStop;
    int count = 0;
    private MyAsyncTask myTask;

    private List<Contact> contacts;
    private ContactDBAdaptor contactDBAdaptor;
    LinearLayout LL;


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

            writeContactsToUI();

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

            loadContacts();

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
        setContentView(R.layout.activity_main);

        LL = (LinearLayout)findViewById(R.id.LL);

        contactDBAdaptor = ContactDBAdaptor.getContactDBAdapterInstance(this);
        contacts = contactDBAdaptor.getAllContacts();

        isStop = false;
        myTask = new MyAsyncTask();
        myTask.execute(count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_1, Menu.NONE, "Add Contacts");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_ITEM_1:
                Intent addContact = new Intent(getApplicationContext(), AddContact.class);
                startActivity(addContact);
                return true;
            default:
                return false;
        }
    }

    public void loadContacts() {
        contacts = contactDBAdaptor.getAllContacts();
    }

    public void writeContactsToUI() {
         if (contacts != null && contacts.size() > 0) {
             for (final Contact c : contacts) {
                 final String name = c.name;
                 final long cID = c.id;

                 Button b = new Button(this);
                 b.setText(name);
                 b.setTextSize(24);
                 b.setTextColor(Color.BLACK);
                 b.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent viewContact = new Intent(getApplicationContext(), ViewContact.class);

                         String[] cDetails = new String[5];
                         cDetails[0] = name;
                         cDetails[1] = c.phone;
                         cDetails[2] = c.email;
                         cDetails[3] = c.street;
                         cDetails[4] = c.city_st_zip;

                         viewContact.putExtra("Contact", cDetails);
                         viewContact.putExtra("id", cID);
                         startActivity(viewContact);
                     }
                 });
                 LL.addView(b);
             }
         }
    }


}
