package com.chatinitiator.abunaw.automation;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    // Declare Variables
    ListView list;
    ListViewAdapter listviewadapter;
    List<Event> eventlist = new ArrayList<Event>();
    ArrayList<String> title;
    ArrayList<String> time;
    ArrayList<String> starttime;
    ArrayList<String> frequency;
    ArrayList<String> pendingIntentIds;
    ArrayList<String> eventIds;
    ArrayList<String> isActive;
    SQLiteDatabase db;
    String freqStr;
    int[] flag;
    //private Button sendMmsEtP;
    String msg;
    String controlId;
    String editTitle;
    String is_active;
    String event_id;
    String name;
    int flagControl;
    private SwipeRefreshLayout swipeContainer;
    private int counter;
    DBEvent dbEvent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.listview_main);


        msg = "";
        controlId = "";
        editTitle = "";
        is_active = "";
        freqStr = "";
        event_id = "";
        name = "";
        counter = 0;


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                //refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                refresh();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        ActionBar ab = getSupportActionBar();
        ab.setIcon(R.drawable.ci_actionbar_icon);
        ab.setTitle("Chat Initiator");
        ab.setDisplayShowHomeEnabled(true);


        //Custom view with image to add to action bar
        ab.setDisplayOptions(ab.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(ab.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.neweventimg);///setting create event button
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

                //String sql = "delete from event;";
                //db.execSQL(sql);
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loadActivity();


    }



    /*
       Loads all components in main activity
     */

    private void loadActivity() {
        // Do all of your work here

        title = new ArrayList<String>();
        time = new ArrayList<String>();
        frequency = new ArrayList<String>();
        pendingIntentIds = new ArrayList<String>();
        starttime = new ArrayList<String>();
        eventIds = new ArrayList<String>();
        isActive = new ArrayList<String>();


        db=openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS event(event_title VARCHAR,message VARCHAR,phone_number VARCHAR," +
        "start_date VARCHAR,start_time VARCHAR,frequency VARCHAR,pending_intent_id VARCHAR,month VARCHAR,year VARCHAR,day VARCHAR," +
                "hour VARCHAR,minute VARCHAR,second VARCHAR,am_pm VARCHAR,time VARCHAR,is_active VARCHAR,edit_id VARCHAR,event_id VARCHAR,name VARCHAR,counter int);");

        //String sql = "INSERT or replace INTO event (event_title, message, phone_number, " +
        //"start_date,start_time,frequency) VALUES('Text mom','Hi mom','4047869557','26-8-2015','18:58 PM','Never')" ;
        //String sql = "delete from event";
        //db.execSQL(sql);

        Cursor c = db.rawQuery("SELECT * FROM event ", null);
        if(c.getCount()>0)
        {
            while(c.moveToNext())
            {
                title.add(c.getString(0));
                time.add(c.getString(3) + " " + c.getString(4));
                //Convert frequency to human readable days
                if(c.getString(5).equalsIgnoreCase("0"))
                {
                    freqStr = "Never";
                }else if(c.getString(5).equalsIgnoreCase("86400000"))
                {
                    freqStr = "Daily";
                }else if(c.getString(5).equalsIgnoreCase("14520000000"))
                {
                    freqStr = "Weekly";
                }else if(c.getString(5).equalsIgnoreCase("1210000000"))
                {
                    freqStr = "Every 2 weeks";
                }else if(c.getString(5).equalsIgnoreCase("2628000000"))
                {
                    freqStr = "Monthly";
                }else if(c.getString(5).equalsIgnoreCase("31540000000"))
                {
                    freqStr = "Yearly";
                }else {
                    freqStr = "Every 10 minutes";
                }

                frequency.add(freqStr);
                pendingIntentIds.add(c.getString(6));
                starttime.add(c.getString(14));
                isActive.add(c.getString(15));
                eventIds.add(c.getString(17));

            }
        }

        // Generate sample data into string arrays
        //title = new String[] { "Text mom", "Text wifey", "Text child 1"};

        //time = new String[] { "9:15 am", "3:15 pm", "11:30 am","10:30 pm"};

        //frequency = new String[] { "Never", "Everyday","Every week"};

		//flag = new int[] { R.drawable.pendimg,R.drawable.active};


        //add events to custom list
        //update db with an id for each event
        for (int i = 0; i < title.size(); i++)
        {
            /*if(isActive.get(i).equalsIgnoreCase("false") || isActive.get(i).equalsIgnoreCase("null"))
            {
                flagControl = 0;
            } else {
                flagControl = 1;
            }*/

            Event listOfEvents = new Event(title.get(i), time.get(i), frequency.get(i),eventIds.get(i),isActive.get(i));
            eventlist.add(listOfEvents);

            //Update db here to ad an id for each event
            String updateQuery = "UPDATE event SET edit_id='" + i + "'" + " WHERE event_id='" + eventIds.get(i) + "';";
            db.execSQL(updateQuery);
        }



         //Locate the ListView in listview_main.xml
           list = (ListView) findViewById(R.id.listview);
           //list.setBackgroundColor(Color.WHITE);
           ColorDrawable cd = new ColorDrawable(Color.parseColor("#A9A9A9"));
           //list.setDivider(cd);
           //list.setDividerHeight(50);
           //list.setBackgroundColor(Color.WHITE);
           //list.setPadding(50,0,50,0);




        // Pass results to ListViewAdapter Class
        //Has special method to interact with Event object
        listviewadapter = new ListViewAdapter(this, R.layout.listview_item,
                eventlist);

        // Binds the Adapter to the ListView
        list.setAdapter(listviewadapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        //When list item is clicked
        /*
        EDITING HERE!!!!!!!!!!!!!!!!!!!!!
         */
        list.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {


                //Assign control id here
                displayWarning(arg2);
                //final View selectedView = arg1 ; // Save selected view in final variable**
                //Toast.makeText(getApplicationContext(), "i have been clicked : " + arg2,Toast.LENGTH_LONG).show();

            }
        });

        // Capture ListView item click
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = list.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                listviewadapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = listviewadapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Event selecteditem = listviewadapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                listviewadapter.remove(selecteditem);
                                //Delete pending intent
                                cancelService(Integer.parseInt(pendingIntentIds.get(i)));
                                //Delete data from database
                                deleteFromDB(selecteditem.getId());
                                /*****/
                                refresh();
                                //refresh data in list adapter
                                listviewadapter.clear();
                                listviewadapter.notifyDataSetChanged();
                                reloadActivity();


                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }


            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                listviewadapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                //MenuItem settingsItem = menu.findItem(R.id.);
                //settingsItem.setVisible(false);
                return false;
            }
        });


    }



    /*
      Method to refresh list view when pulled down
     */

    public void refresh() {

        listviewadapter.clear();
        listviewadapter.notifyDataSetChanged();
        reloadActivity();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //reschedule all events once activity comes to views
    }

    @Override
    protected void onPause() {
        super.onPause();
        refresh();

    }

    @Override
    protected void onStop() {

        super.onStop();  // Always call the superclass method first
        refresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();  // Always call the superclass method first
        //reschedule();
    }

    /*public void sendBroadcast()
    {

        Intent intent = new Intent();
        intent.setAction("reschedule");
        sendBroadcast(intent);
    }*/

    /*
       Cancels a pending event
     */
    public void cancelService(int pendingIntentId)
    {
        Intent intent = new Intent(this, MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent.getBroadcast(this, pendingIntentId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        sender.cancel();
        alarmManager.cancel(sender);

        Toast.makeText(getApplicationContext(), "Chat cancelled!!! :)",Toast.LENGTH_LONG).show();
    }

    /*
      Delete data from database
     */
    public void deleteFromDB(String eventId)
    {
        String sql = "delete from event " +
                "where event_id = " + eventId;
        db.execSQL(sql);
    }
    /*
    display message to user for validation or editing
     */

    public void displayWarning(final int anId)
    {
        //get select event using id to prep for edit
        edit(anId);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("To: " + name + "\nChat Initiated: " + is_active +  "\nNumber of Times: " + counter + "\n\nText Message: " + "\n" + msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("All Good",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("Edit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Start editing activity
                        Intent intent = new Intent(MainActivity.this, EditMessageActivity.class);
                        intent.putExtra("edit id", controlId);
                        intent.putExtra("message", msg);
                        intent.putExtra("title", editTitle);
                        intent.putExtra("name", name);

                        startActivity(intent);
                        finish();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    //edit event
    public void edit(int anId)
    {
        //Start editing process here
        Cursor c = db.rawQuery("SELECT * FROM event where edit_id='" + anId + "'", null);
        Log.i("Query : ", "SELECT * FROM event where edit_id='" + anId + "'");

        if (c.getCount() > 0) {

            while (c.moveToNext()) {

                editTitle = c.getString(0);
                msg = c.getString(1);
                is_active = c.getString(15);
                controlId = c.getString(16);
                name = c.getString(18);
                counter = c.getInt(19);
                //For user convenience instead of true/false it should be yes/no
                if(is_active.equalsIgnoreCase("false") || is_active.equalsIgnoreCase("null"))
                {
                    is_active = "No";
                }else
                {
                    is_active = "Yes";
                }
                Log.i("message : ", msg);


            }


        }
        //Toast.makeText(getApplicationContext(), "Message : " + msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menu.findItem(R.id.help).setVisible(false);
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    action bar controls
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                //Do stuff
                Intent intent = new Intent(MainActivity.this, HowToActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "How to use", Toast.LENGTH_LONG).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
      Reloads the activity
     */
    private void reloadActivity() {
        // Do all of your work here

        title = new ArrayList<String>();
        time = new ArrayList<String>();
        frequency = new ArrayList<String>();
        pendingIntentIds = new ArrayList<String>();
        starttime = new ArrayList<String>();
        eventIds = new ArrayList<String>();
        isActive = new ArrayList<String>();


        Cursor c = db.rawQuery("SELECT * FROM event ", null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                title.add(c.getString(0));
                time.add(c.getString(3) + " " + c.getString(4));
                //Convert frequency to human readable days
                if (c.getString(5).equalsIgnoreCase("0")) {
                    freqStr = "Never";
                } else if (c.getString(5).equalsIgnoreCase("86400000")) {
                    freqStr = "Daily";
                } else if (c.getString(5).equalsIgnoreCase("14520000000")) {
                    freqStr = "Weekly";
                } else if (c.getString(5).equalsIgnoreCase("1210000000")) {
                    freqStr = "Every 2 weeks";
                } else if (c.getString(5).equalsIgnoreCase("2628000000")) {
                    freqStr = "Monthly";
                } else if (c.getString(5).equalsIgnoreCase("31540000000")) {
                    freqStr = "Yearly";
                } else {
                    freqStr = "Every 5 minutes";
                }

                frequency.add(freqStr);
                pendingIntentIds.add(c.getString(6));
                starttime.add(c.getString(14));
                isActive.add(c.getString(15));
                eventIds.add(c.getString(17));

            }
        }

        //flag = new int[]{R.drawable.pendimg, R.drawable.active};


        //add events to custom list
        //update db with an id for each event
        for (int i = 0; i < title.size(); i++) {
            /*if (isActive.get(i).equalsIgnoreCase("false") || isActive.get(i).equalsIgnoreCase("null")) {
                flagControl = 0;
            } else {
                flagControl = 1;
            }*/
            Event listOfEvents = new Event(title.get(i), time.get(i), frequency.get(i),eventIds.get(i),isActive.get(i));
            eventlist.add(listOfEvents);

            //Update db here to ad an id for each event
            String updateQuery = "UPDATE event SET edit_id='" + i + "'" + " WHERE event_id='" + eventIds.get(i) + "';";
            db.execSQL(updateQuery);
        }


        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);


        // Pass results to ListViewAdapter Class
        //Has special method to interact with Event object
        listviewadapter = new ListViewAdapter(this, R.layout.listview_item,
                eventlist);

        // Binds the Adapter to the ListView
        list.setAdapter(listviewadapter);
    }

}
