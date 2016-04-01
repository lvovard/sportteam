package com.lvovard.sportteam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClubActivity extends Activity
{
  
  static int clubselected;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_club);
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);

    layout.setBackgroundResource(R.drawable.bluebkg);

    
    //if user has recorder at least one club
    if (Record.recordlist.size() > 0)
    {
      for(Record r:Record.recordlist)
      {
        Button btn = new Button(this);
        btn.setText(r.getClub());
        int sport_picture = 0;
        if (r.getSport().equals("Football"))
        {
          sport_picture = R.drawable.football;
        }
        if (r.getSport().equals("Handball"))
        {
          sport_picture = R.drawable.handball;
        }
        if (r.getSport().equals("Basketball"))
        {
          sport_picture = R.drawable.basketball;
        }
        if (r.getSport().equals("Volleyball"))
        {
          sport_picture = R.drawable.volleyball;
        }
        if (r.getSport().equals("Rugby"))
        {
          sport_picture = R.drawable.rugby;
        }
        btn.setCompoundDrawablesWithIntrinsicBounds( sport_picture, 0, 0, 0);
        btn.setId(r.getId());
        btn.setId(Record.recordlist.indexOf(r));
        r.setButton(btn);
        layout.addView(btn);
        btn.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            Log.i("myApp", "id = "+v.getId());
            clubselected = v.getId();
            Intent intent = new Intent(ClubActivity.this, CategoryActivity.class);
            startActivity(intent);
          }
        });
      }
    }
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.club, menu);
      return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    
    switch (item.getItemId()) 
    {
      //if user choose to add a new club/category, go back to main page
      case R.id.action_add:
        MainActivity.state = "AJOUT";
        Intent intent = new Intent(ClubActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
      //if user choose to delete all records, first ask for a confirmation  
      case R.id.action_delete:
        MainActivity.state = "SUPPRESSION";
        dialogBoxRemoveAll();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }    

  }
  
  public void dialogBoxRemoveAll() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Effacer tous");
    alertDialogBuilder.setPositiveButton("OUI",
        new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
              
              try
              {
                //if user confirm the deletion, erase file and clear the list and then reload activity
                OutputStreamWriter out = new OutputStreamWriter(openFileOutput("connexion.txt",0));
                out.write("");
                out.close();
                Record.recordlist.clear();
                finish();
                startActivity(getIntent());
              } catch (IOException e)
              {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
        }
    });

    alertDialogBuilder.setNegativeButton("NON",
        new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {

        }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
}
}
