package com.example.addressbookapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditContact extends AppCompatActivity {
    EditText Cname, Cphone, Cemail, Cstreet, Caddress;
    Button save;

    long ContactID = -1;
    String[] Contact;

    private ContactDBAdaptor contactDBAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        Cname = (EditText)findViewById(R.id.nameInput);
        Cphone = (EditText)findViewById(R.id.phoneInput);
        Cemail = (EditText)findViewById(R.id.emailInput);
        Cstreet = (EditText)findViewById(R.id.streetInput);
        Caddress = (EditText)findViewById(R.id.address2Input);
        save = (Button)findViewById(R.id.saveBtn);
        contactDBAdaptor = ContactDBAdaptor.getContactDBAdapterInstance(this);

        Contact = new String[5];
        Contact = getIntent().getStringArrayExtra("Contact");
        ContactID = getIntent().getLongExtra("id", ContactID);

        if (Contact.length > 0 && ContactID != -1) {
            Cname.setText(Contact[0]);
            Cphone.setText(Contact[1]);
            Cemail.setText(Contact[2]);
            Cstreet.setText(Contact[3]);
            Caddress.setText(Contact[4]);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Cname.getText().toString().isEmpty()) {
                    String newName = Cname.getText().toString();
                    String newPhone = Cphone.getText().toString();
                    String newEmail = Cemail.getText().toString();
                    String newStreet = Cstreet.getText().toString();
                    String newAddress = Caddress.getText().toString();

                    contactDBAdaptor.modify(ContactID, newName, newPhone, newEmail, newStreet,
                            newAddress);
                    Intent sendBack = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(sendBack);
                }
                //else {
                    // Display message to ask user to enter a name
                //}
            }
        });
    }
}
