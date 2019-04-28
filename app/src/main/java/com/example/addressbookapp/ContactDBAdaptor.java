package com.example.addressbookapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

public class ContactDBAdaptor {

    private static final String TAG=ContactDBAdaptor.class.getSimpleName();

    private static final String DB_NAME="Contacts.db";
    private static final int DB_VERSION=1;

    private static final String TABLE_CONTACTS ="table_contacts";
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_PHONE ="phone";
    private static final String COLUMN_EMAIL ="email";
    private static final String COLUMN_STREET ="street";
    private static final String COLUMN_CITYSTZIP ="city_st_zip";


    //create table table_todo(task_id integer primary key, todo text not null);
    private static String CREATE_TABLE_CONTACTS ="CREATE TABLE "+ TABLE_CONTACTS +
            "("+ COLUMN_ID +" INTEGER PRIMARY KEY, "+ COLUMN_NAME +
            " TEXT NOT NULL, " + COLUMN_PHONE +" TEXT NOT NULL, " +
            COLUMN_EMAIL + " TEXT NOT NULL, " + COLUMN_STREET +
            " TEXT NOT NULL, " + COLUMN_CITYSTZIP + " TEXT NOT NULL)";

    private Context context;
    private SQLiteDatabase sqLliteDatabase;
    private static ContactDBAdaptor ContactDBAdapterInstance;



    private ContactDBAdaptor(Context context){
        this.context=context;
        sqLliteDatabase=new ContactDBHelper(this.context,DB_NAME,null,DB_VERSION).getWritableDatabase();
    }

    public  static ContactDBAdaptor getContactDBAdapterInstance(Context context){
        if(ContactDBAdapterInstance ==null){
            ContactDBAdapterInstance =new ContactDBAdaptor(context);
        }
        return ContactDBAdapterInstance;
    }




    //insert,delete,modify,query methods

    public boolean insert(String name, String phone, String email, String street, String a){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_STREET, street);
        contentValues.put(COLUMN_CITYSTZIP, a);

        return sqLliteDatabase.insert(TABLE_CONTACTS,null,contentValues)>0;
    }

    public boolean delete(long id){
        return sqLliteDatabase.delete(TABLE_CONTACTS, COLUMN_ID +" = "+id,null)>0;
    }

    public boolean modify(long id, String name, String phone, String email, String street, String c){
        ContentValues contentValues=new ContentValues();
        contentValues.put(COLUMN_NAME,name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_STREET, street);
        contentValues.put(COLUMN_CITYSTZIP, c);

        return sqLliteDatabase.update(TABLE_CONTACTS,contentValues, COLUMN_ID +" = "+id,null)>0;
    }


    public List<Contact> getAllContacts(){
        List<Contact> ContactList=new ArrayList<Contact>();

        Cursor cursor=sqLliteDatabase.query(TABLE_CONTACTS,new String[]{COLUMN_ID, COLUMN_NAME,
                COLUMN_PHONE, COLUMN_EMAIL, COLUMN_STREET, COLUMN_CITYSTZIP},null,
                null,null,null,null,null);

        if(cursor!=null &cursor.getCount()>0){
            while(cursor.moveToNext()){
                Contact c=new Contact(cursor.getLong(0),cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        cursor.getString(4), cursor.getString(5));
                ContactList.add(c);

            }
        }
        cursor.close();
        return ContactList;
    }

    private static class ContactDBHelper extends SQLiteOpenHelper {

        public ContactDBHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int dbVersion){
            super(context,databaseName,factory,dbVersion);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_CONTACTS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                              int oldVersion, int newVersion) {
            //Not implemented now
        }
    }


}
