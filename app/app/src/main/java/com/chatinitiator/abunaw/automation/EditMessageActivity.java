package com.chatinitiator.abunaw.automation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.content.Intent;
//import android.view.Menu;


public class EditMessageActivity extends AppCompatActivity implements
        OnClickListener{


    SQLiteDatabase db;
    Button done;
    EditText txtTitle, txtMsg,txtName;
    String editId;
    String msg;
    String editTitle;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        ActionBar ab = getSupportActionBar();
        ab.setIcon(R.drawable.ci_actionbar_icon);
        ab.setTitle("Edit Message");
        ab.setDisplayShowHomeEnabled(true);

        //setting components
        txtName = (EditText) findViewById(R.id.name);
        txtTitle = (EditText) findViewById(R.id.editTle);
        txtMsg = (EditText) findViewById(R.id.editMsg);
        done = (Button) findViewById(R.id.editBtn);

        //setting onclick listener
        done.setOnClickListener(this);


        db=openOrCreateDatabase("EventDB", Context.MODE_PRIVATE, null);

        Intent intent = getIntent();
        editId = intent.getStringExtra("edit id");
        msg = intent.getStringExtra("message");
        editTitle = intent.getStringExtra("title");
        name = intent.getStringExtra("name");

        //check to ensure intent really has extras
        //if so display the contents on screen
        if(!editTitle.equalsIgnoreCase("") && !msg.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
            txtTitle.setText(editTitle);
            txtMsg.setText(msg);
            txtName.setText(name);
        }


        //String sql = "INSERT or replace INTO event (event_title, message, phone_number, " +
        //"start_date,start_time,frequency) VALUES('Text mom','Hi mom','4047869557','26-8-2015','18:58 PM','Never')" ;

        //String sql = "delete from event";
        //db.execSQL(sql);


    }

    /*
      onclick listener handler
     */
    @Override
    public void onClick(View v) {


        //Toast.makeText(this, "Done editing : " + editId,
                //Toast.LENGTH_LONG).show()

        if(!txtTitle.getText().toString().equalsIgnoreCase("") &&
        !txtMsg.getText().toString().equalsIgnoreCase("") && !txtName.getText().toString().equalsIgnoreCase("")) {
            //Update db here to ad an id for each event
            String updateQuery = "UPDATE event SET message='" + txtMsg.getText().toString().replace("'", "''") + "', event_title='" + txtTitle.getText().toString().replace("'", "''")
                    + "'" + ", name='" + txtName.getText().toString().replace("'", "''")+ "' WHERE edit_id='" + editId + "';";
            db.execSQL(updateQuery);

            Log.i("Query : ", updateQuery);

            //go back to main activity
            onBackPressed();
        }else
        {
            //inform user of missing data
            displayWarning();
        }


    }

    /*
    restart main activity when back button is clicked
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        db.close();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                db.close();
                finish();

        }
        return true;
    }

    /*
     warning to be displayed when user tries to submit empty message/title
     */
    public void displayWarning()
    {


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Empty field(s)");
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
