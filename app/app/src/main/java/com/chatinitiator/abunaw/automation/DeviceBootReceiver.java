package com.chatinitiator.abunaw.automation;

/**
 * Created by Abunaw on 10/29/15.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Calendar;


/**
 *
 *
 *    Broadcast receiver, starts when the device gets starts.
 *    Start your repeating alarm here.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    Context context;
    SQLiteDatabase db;
    DBEvent dbEvent;
    Calendar calendar;
    String sendText;
    String name = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        calendar = Calendar.getInstance();
        sendText = "false";

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            //Log.i("On Boot started", " CI app started: ");

            //open db
            db=context.openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
            //select an events data from db and set up chat
            getEvents();
        }
    }



    //Gets events from database
    public void getEvents()
    {
        //A db event
        dbEvent = new DBEvent();

        Cursor c = db.rawQuery("SELECT * FROM event ",null);
        if(c.getCount()>0)
        {
            while(c.moveToNext())
            {

                //Get and create  an event
                dbEvent.setMsg(c.getString(1));
                dbEvent.setPhn_num(c.getString(2));
                dbEvent.setFrq(c.getString(5));
                dbEvent.setPend_int(c.getString(6));


                //get isActive field and if it is active assign then don't send text string = true
                String time = "" + c.getString(14);
                String tempIsActive = c.getString(15);
                String tempId = c.getString(17);


                //Log.i("Is active string", tempIsActive);

                Toast.makeText(context, "On boot is active: " + tempIsActive,
                        Toast.LENGTH_LONG).show();


                if(tempIsActive.equalsIgnoreCase("true")) {
                    sendText = "false";
                }else
                {
                    sendText = "true";
                }

                //Log.i("On boot send text: ", sendText);

                //Log.i("Booting get events", "inside get event");


                //set up chat
                setChat(dbEvent.getMsg(), dbEvent.getPhn_num(),time,sendText,tempId);
            }
        }

        /*******/
        c.close();
        db.close();
    }

    //method that sets up chat
    public void setChat(String msg,String phn,String time, String sendTxt,String eventId)
    {

        //Log.i("set chat", msg + " " + phn + " time: ");

        Intent myIntent = new Intent(context, MyReceiver.class);
        //myIntent.putExtra("message", msg);
        //myIntent.putExtra("phone", phn);
        myIntent.putExtra("id", eventId);
        myIntent.putExtra("time",time);
        myIntent.putExtra("send text", sendTxt);
        myIntent.putExtra("pendIntentId",dbEvent.getPend_int());
        myIntent.putExtra("frequency",dbEvent.getFrq());

        //You need to use different Broadcast id's to create different pending intents
	   /*
	    * By setting different private request codes as an argument for the getBroadcast function,
	    * you can schedule multiple alarms.
	    */
        int pendingIntent_id = Integer.parseInt(dbEvent.getPend_int());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, pendingIntent_id, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //convert execution time to milli seconds
        //This sends a notification every 10 seconds on the set date  and time above
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Long.parseLong(time), Long.parseLong(dbEvent.getFrq()), pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        ComponentName receiver = new ComponentName(context, MyReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Toast.makeText(context, "On Boot Alarm Set", Toast.LENGTH_SHORT).show();
        //reset send text
        sendText = "";

    }


}
