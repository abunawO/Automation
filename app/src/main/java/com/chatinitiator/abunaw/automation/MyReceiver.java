package com.chatinitiator.abunaw.automation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver
{
	String msg = "";
	String phn = "";
	long time = 0;
	String sendText = "";
	SQLiteDatabase db;
	int anId = 0;
	long frequencyMs = 0;
	int count = 0;


	@Override
	public void onReceive(Context context, Intent intent)
	{

		//open db
		db = context.openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);

		try{


			time = Long.parseLong(intent.getStringExtra("time"));
			sendText = intent.getStringExtra("send text");
			String id = intent.getStringExtra("id");

			Log.i("My receiver id: ", id);

			Toast.makeText(context, "MyReceiver class: ", Toast.LENGTH_SHORT).show();
			//get the data here
			getData(id);

			Log.i("My receiver Send text: ", sendText);


			//send only events that have not been executed yet
			if (sendText.equalsIgnoreCase("true")) {
				sendSMS(msg, phn);
				count++;
				Toast.makeText(context, "Chat initiated",
						Toast.LENGTH_LONG).show();

				//update db using time and set isActive field here

				String updateQuery = "UPDATE event SET is_active='true'," + "counter=" + count + " WHERE event_id='" + id + "';";
				db.execSQL(updateQuery);
				Log.i("My receiver query: ", updateQuery);

			} else {
				Toast.makeText(context, "Chat not initiated",
						Toast.LENGTH_LONG).show();


				anId = Integer.parseInt(intent.getStringExtra("pendIntentId"));
				//frequencyMs = Long.parseLong(intent.getStringExtra("frequency"));
				//update pending intent, intent // reset it back to true
				//use pending intent id from DBR
				AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				Intent myIntent = intent;
				myIntent.putExtra("send text", "true");
				// Extras aren't used to find the PendingIntent
				PendingIntent pendingIntent = PendingIntent.getBroadcast(context, anId, myIntent,
						PendingIntent.FLAG_UPDATE_CURRENT); // find the old PendingIntent
				//am.setRepeating(AlarmManager.RTC_WAKEUP, time, frequencyMs, pendingIntent);

				Log.i("New intent send text: ", intent.getStringExtra("send text"));


			}
		}catch (NumberFormatException nfe)
		{
			System.out.println("exception caught************");
		}



	}
	
	
	private void sendSMS(String msg2, String phn2) {

		Log.i("sending msg 3 ", msg + " " + phn);

		try{
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phn2, null, msg2, null, null);
		}catch (IllegalArgumentException InvaliddestinationAddress)
		{
			System.out.print("Invalid destinationAddress Exception");
		}

	}

	public void getData(String evtStr)
	{
		//Start editing process here
		Cursor c = db.rawQuery("SELECT * FROM event where event_id='" + evtStr + "'", null);
		Log.i("Query : ", "SELECT * FROM event where event_id='" + evtStr + "'");

		if (c.getCount() > 0) {

			while (c.moveToNext()) {

				msg = c.getString(1);
				phn = c.getString(2);
				count = c.getInt(19);
				Log.i("message : ", msg + " phone: " + phn + " COUNTER: " + count);


			}


		}
	}

}
