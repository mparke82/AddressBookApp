package com.example.addressbookapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    EditText Cname, Cphone, Cemail, Cstreet, Caddress;
    Button save;
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
            count = integer;

            Intent backToMain = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(backToMain);

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
            //while (!isStop) {

                AddContactToDB();

                counter++;
                /*try {
                    Thread.sleep(1000);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.i("AsyncTask: ", e.getMessage());
                }*/

                //if(isCancelled())
                    //break;
            //}
            return counter;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getApplicationContext(),"Thread ID is: "+Thread.currentThread().getId(), Toast.LENGTH_SHORT).show();
        }
    }

    public void AddContactToDB() {
        contactDBAdaptor.insert(Cname.getText().toString(), Cphone.getText().toString(),
                Cemail.getText().toString(), Cstreet.getText().toString(),
                Caddress.getText().toString());
        isStop = true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contactDBAdaptor = ContactDBAdaptor.getContactDBAdapterInstance(this);
        Cname = (EditText)findViewById(R.id.nameInput);
        Cphone = (EditText)findViewById(R.id.phoneInput);
        Cemail = (EditText)findViewById(R.id.emailInput);
        Cstreet = (EditText)findViewById(R.id.streetInput);
        Caddress = (EditText)findViewById(R.id.address2Input);
        save = (Button)findViewById(R.id.saveBtn);
        isStop = false;

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTask = new MyAsyncTask();
                myTask.execute(count);
            }
        });
    }
}
