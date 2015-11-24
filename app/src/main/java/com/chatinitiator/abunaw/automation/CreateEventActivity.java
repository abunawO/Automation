package com.chatinitiator.abunaw.automation;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
//import android.content.Intent;
//import android.view.Menu;


public class CreateEventActivity extends AppCompatActivity implements
        OnClickListener{

    private Spinner spinner1;
    long frequencyMs;
    // Widget GUI
    Button btnCalendar, btnTimePicker,doneBtn,contact;
    EditText txtDate, txtTime,titleTxt,msgTxt,phoneTxt,txtname;
    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String month,day,aYear;
    private String hour,startMinute;
    private String mAM_PM;
    private PendingIntent pendingIntent;
    private Intent myIntent;
    private Calendar calendar;
    private int pendingIntent_id;
    SQLiteDatabase db;
    int AM_PM;
    String time;
    String eventId;
    private static final int CONTACT_PICKER_RESULT = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        ActionBar ab = getSupportActionBar();
        ab.setIcon(R.drawable.ci_actionbar_icon);
        ab.setTitle("Create Event");
        ab.setDisplayShowHomeEnabled(true);

        //Custom view with image to add to action bar
        ab.setDisplayOptions(ab.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(ab.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.helpimg);///setting create event button
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        ab.setCustomView(imageView);

        //make image clickable
        //help image is clicked start how to use video activity.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateEventActivity.this, HowToActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "How to use",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        frequencyMs = 0;
        time = "";
        eventId = "";
        calendar = Calendar.getInstance();

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        doneBtn = (Button) findViewById(R.id.doneBtn);
        contact = (Button) findViewById(R.id.contacts);

        txtDate = (EditText) findViewById(R.id.dateTxt);
        txtDate.setEnabled(false);
        txtTime = (EditText) findViewById(R.id.timeTxt);
        txtTime.setEnabled(false);
        titleTxt = (EditText) findViewById(R.id.txtTitle);
        msgTxt = (EditText) findViewById(R.id.txtMsg);
        phoneTxt = (EditText) findViewById(R.id.txtPhone);
        txtname = (EditText) findViewById(R.id.txtname);
        txtname.setEnabled(true);

        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        contact.setOnClickListener(this);

        spinner1 = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.frequency_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter);

        //Set up listener for spinner
        spinner1.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener());

        db=openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        //String sql = "INSERT or replace INTO event (event_title, message, phone_number, " +
        //"start_date,start_time,frequency) VALUES('Text mom','Hi mom','4047869557','26-8-2015','18:58 PM','Never')" ;

        //String sql = "delete from event";
        //db.execSQL(sql);


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }*/

    private class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
           /* Here you only have your inner switch statement */

            switch (position) {
                //case 0: frequencyMs = 0;break;//never
                case 0: frequencyMs = 0;break;//never
                case 1: frequencyMs = (long)8.64e+7; break;//daily
                case 2: frequencyMs = (long) 1.452e+10; break;//weekly
                case 3: frequencyMs = (long) 1.21e+9; break; //every 2 weeks
                case 4: frequencyMs = (long) 2.628e+9; break;//monthly
                case 5: frequencyMs = (long) 3.154e+10; break;//yearly
                case 6: frequencyMs = (long) 600000; break;//yearly
            }
            //Toast.makeText(getBaseContext(),
                    //"Selected frequency : " + parent.getItemAtPosition(position).toString() + " " + position,
                    //Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnCalendar) {

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            month = "" + monthOfYear;
                            day = "" + dayOfMonth;
                            aYear = "" + year;

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker) {

            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);


            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if(hourOfDay < 12) {
                                mAM_PM = "AM";
                            } else {
                                mAM_PM = "PM";
                            }
                            // Display Selected time in textbox
                            txtTime.setText(convertHour("" + hourOfDay) + ":" + convertMinute("" + minute) + " " + mAM_PM);
                            hour = "" + hourOfDay;
                            startMinute = "" + minute;
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }

        if (v == doneBtn) {

            //validate for presence
            validatePresence();
        }

       if (v == contact) {


           Intent intent = new Intent(Intent.ACTION_PICK);
           intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
           startActivityForResult(intent, CONTACT_PICKER_RESULT);
           Toast.makeText(getApplicationContext(), "contacts",Toast.LENGTH_LONG).show();
            /*db.execSQL("ALTER TABLE event ADD COLUMN pending_intent_id VARCHAR");
            String sql = "delete from event";
            db.execSQL(sql);

            cancelService();*/
        }
    }


    /*
      handles selected contacts
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    handleContactSelection(data);
                    break;
            }
        }
    }

    private void handleContactSelection(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Cursor cursor = null;
                Cursor nameCursor = null;
                try {
                    cursor = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    CommonDataKinds.Phone.CONTACT_ID} ,
                            null, null, null);

                    String phoneNumber = null;
                    String contactId = null;
                    if (cursor != null && cursor.moveToFirst()) {
                        contactId = cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.CONTACT_ID));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }

                    String givenName = null;///first name.
                    //String familyName = null;//last name.

                    String projection[] = new String[]{ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME};
                    String whereName = ContactsContract.Data.MIMETYPE + " = ? AND " +
                            ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
                    String[] whereNameParams = new String[] { ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contactId};

                    nameCursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            projection, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);

                    if(nameCursor != null && nameCursor.moveToNext()) {
                        givenName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                        //familyName = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
                    }

                    phoneTxt.setText(phoneNumber);
                   // if(givenName.equalsIgnoreCase(""))
                   // {
                        txtname.setText(givenName);
                   // }

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                    if(nameCursor != null){
                        nameCursor.close();
                    }
                }
            }
        }
    }

    /*
    restart main activity when back button is clicked
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /*
    sets up chat
     */

    public void setChat(String msg,String phn, String time)
    {


        Log.i("set chat", msg + " " + phn + " time: " + time);

        Intent myIntent = new Intent(CreateEventActivity.this, MyReceiver.class);
        //myIntent.putExtra("message", msg);
       // myIntent.putExtra("phone", phn);
        myIntent.putExtra("time",time);
        myIntent.putExtra("send text","true");
        myIntent.putExtra("id",eventId);

        Log.v(" create event id : ", "" + eventId);

        //You need to use different Broadcast id's to create different pending intents
	   /*
	    * By setting different private request codes as an argument for the getBroadcast function,
	    * you can schedule multiple alarms.
	    */
        pendingIntent_id = (int) System.currentTimeMillis();

        Log.v("Pending id : ","" + pendingIntent_id);

        pendingIntent = PendingIntent.getBroadcast(CreateEventActivity.this, pendingIntent_id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //convert execution time to milli seconds
        //This sends a notification every 10 seconds on the set date  and time above
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequencyMs, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


    }


/*
sets up users input date
 */
    public void setUpCalendar()
    {
        try
        {

            //set notification for date --> 8th January 2015 at 9:06:00 PM
            calendar.set(Calendar.MONTH, Integer.parseInt(month));//6
            calendar.set(Calendar.YEAR, Integer.parseInt(aYear));//2015
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));//21

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));//3
            calendar.set(Calendar.MINUTE, Integer.parseInt(startMinute));//45
            calendar.set(Calendar.SECOND, 0);//0

            AM_PM = calendar.get(Calendar.AM_PM);
            calendar.set(Calendar.AM_PM, AM_PM);

            time = "" + calendar.getTimeInMillis();

            Log.i("AM_PM", ""+AM_PM + " time: " + time);

        }catch (NumberFormatException nfe)
        {
            Log.i("NumberFormatException", nfe.toString());
        }

    }

    /*
    inserts data to database
     */
    public void collectDataInsertToDB()
    {
        String tempTitle = titleTxt.getText().toString();
        tempTitle.replace("'", "''");
        String tempMsg = msgTxt.getText().toString();
        tempMsg.replace("'", "''");
        String tempPhn = phoneTxt.getText().toString();
        String plusOneToMonth = "" + (Integer.parseInt(month) + 1);
        String tempStartDate = plusOneToMonth + "-"+day+"-"+aYear;
        String tempStartTime = convertHour(hour) + ":" + convertMinute("" + startMinute) + " " +mAM_PM;
        String tempFrequency = "" + frequencyMs;
        String tempPendingIntentId = "" + pendingIntent_id;
        String name = txtname.getText().toString().replace("'", "''");
        String isnull = "null";


        //There is a problem here
        String insertQuery = "INSERT INTO event(event_title,message,phone_number," +
                "start_date,start_time,frequency,pending_intent_id," +
                "month,year,day,hour,minute,second,am_pm,time,is_active,event_id,name,counter) " +
                "VALUES('" + tempTitle.replace("'", "''") + "','" + tempMsg.replace("'", "''") +
                "','" + tempPhn+ "','" + tempStartDate + "','" + tempStartTime + "','" + tempFrequency + "','" + tempPendingIntentId
                + "','" + month + "','" + aYear + "','" + day
                + "','" + hour + "','" + startMinute + "','" + 0
                + "','" +  AM_PM + "','" +  time + "','" +  isnull + "','" +  eventId + "','" +  name + "'," +  0 + ");";

        Log.i("Query", insertQuery);

        db.execSQL(insertQuery);

    }

    /*
    clear fields
     */
    public void clearText() {
        txtDate.setText("");
        txtTime.setText("");
        titleTxt.setText("");
        msgTxt.setText("");
        phoneTxt.setText("");
        txtname.setText("");
    }

    /*
    validates presence of data
     */
    public void validatePresence()
    {

        //check to see if text fields have data
        if(titleTxt.getText().toString().equalsIgnoreCase("") || msgTxt.getText().toString().equalsIgnoreCase("")||
                phoneTxt.getText().toString().equalsIgnoreCase("") || txtname.getText().toString().equalsIgnoreCase(""))
        {

            //display warning message for empty fields
            displayWarning();

        }else
        {
            CharSequence cs = phoneTxt.getText().toString();
            Log.i("valid phone number: " , "" + isValidPhoneNumber(cs));
            //Check date to make sure its correct
            if(isValidPhoneNumber(cs) == true)
            {
                starService();
            }else // Phone number must be formatted wrong
            {
                //Display invalid date warning
                displayInvalidPhoneWarning();
            }

        }



    }

    /*
    warning for empty fields
     */
    public void displayWarning()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Missing data in one or more fields.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        /*builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    /*
    starts service (creating an event)
     */
    private void starService()
    {
        //unique id
        eventId = "" + System.currentTimeMillis();
        //set calendar
        setUpCalendar();///////////crash
        //set chat
        setChat(msgTxt.getText().toString(), phoneTxt.getText().toString(), time);
        //save data in db
        collectDataInsertToDB();
        //Log.v("Message", stringToDate(date));
        //titleTxt.setText(month + "-"+day+"-"+aYear  + " " + hour + ":" + startMinute);
        //titleTxt.setText("Chat set");
        clearText();

        Toast.makeText(getApplicationContext(), "Chat setup successfully!!! :)",Toast.LENGTH_LONG).show();
    }

    /*public void cancelService()
    {
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, pendingIntent_id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(sender);

        Toast.makeText(getApplicationContext(), "Chat cancelled!!! :)",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /*
    another back button clicked handler top
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();

        }
        return true;
    }


    /*
    Convert military hours to 12 hours
     */
    public String convertHour(String hour)
    {
        Log.i("hour:", hour);
        String anHour = "";
        if(Integer.parseInt(hour) > 12 || Integer.parseInt(hour) == 0)
        {
            if(hour.equalsIgnoreCase("13"))
            {
                anHour = "1";
            }else if(hour.equalsIgnoreCase("14"))
            {
                anHour = "2";
            }else if(hour.equalsIgnoreCase("15"))
            {
                anHour = "3";
            }else if(hour.equalsIgnoreCase("16"))
            {
                anHour = "4";
            }else if(hour.equalsIgnoreCase("17"))
            {
                anHour = "5";
            }else if(hour.equalsIgnoreCase("18"))
            {
                anHour = "6";
            }else if(hour.equalsIgnoreCase("19"))
            {
                anHour = "7";
            }else if(hour.equalsIgnoreCase("20"))
            {
                anHour = "8";
            }else if(hour.equalsIgnoreCase("21"))
            {
                anHour = "9";
            }else if(hour.equalsIgnoreCase("22"))
            {
                anHour = "10";
            }else if(hour.equalsIgnoreCase("23"))
            {
                anHour = "11";
            }else
            {
                anHour = "12";
            }
        }else
        {
            anHour = hour;
        }

        Log.i("anHour:", anHour);
        return anHour;
    }


    /*
    Convert minutes to 00 format
     */
    public String convertMinute(String min)
    {
        Log.i("Min:", min);
        String aMinunte = "";
        if(Integer.parseInt(min) < 10)
        {
            if(min.equalsIgnoreCase("9"))
            {
                aMinunte = "09";
            }else if(min.equalsIgnoreCase("8"))
            {
                aMinunte = "08";
            }else if(min.equalsIgnoreCase("7"))
            {
                aMinunte = "07";
            }else if(min.equalsIgnoreCase("6"))
            {
                aMinunte = "06";
            }else if(min.equalsIgnoreCase("5"))
            {
                aMinunte = "05";
            }else if(min.equalsIgnoreCase("4"))
            {
                aMinunte = "04";
            }else if(min.equalsIgnoreCase("3"))
            {
                aMinunte = "03";
            }else if(min.equalsIgnoreCase("2"))
            {
                aMinunte = "02";
            }else if(min.equalsIgnoreCase("1"))
            {
                aMinunte = "01";
            }else if(min.equalsIgnoreCase("0"))
            {
                aMinunte = "00";
            }
        }else
        {
            aMinunte = min;
        }

        Log.i("aMinunte:", aMinunte);
        return aMinunte;
    }
    /*
    display message to user for invalid Phone
     */

    public void displayInvalidPhoneWarning()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Invalid Phone Number. Please correct Phone Number field");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    /*
    validates dat. not used
     */
    public boolean isThisDateValid(String dateToValidate, String dateFormat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Validation of Phone Number
     */
    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            if (target.length() < 10) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();  // Always call the superclass method first

    }

}
