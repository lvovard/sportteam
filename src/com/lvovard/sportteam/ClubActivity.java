package com.lvovard.sportteam;

import android.app.ActionBar;

//import com.lvovard.sportteam.MyBroadcastReceiver.getConvocationDateNotifMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ClubActivity extends Activity
{
  

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_club);
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    //layout.setBackgroundResource(R.drawable.bluebkg);
    getWindow().setBackgroundDrawableResource(R.drawable.bluebkg);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle("SportTeam");
    actionBar.setSubtitle("Clubs");
    try
    {
      Cursor c=Global.db.rawQuery("SELECT * FROM connexion", null);
      if(c.getCount()==0)
      {
        Log.i("myApp", "get all DB connexion records -> nothing");
      }
      else
      {
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
          buffer.append("sport  : "+c.getString(0)+"\n");
          buffer.append("dep    : "+c.getString(1)+"\n");
          buffer.append("club   : "+c.getString(2)+"\n");
          buffer.append("club_id: "+c.getString(3)+"\n\n");
          buffer.append("cat    : "+c.getString(4)+"\n");
          Record rec = new Record(c.getString(0), c.getString(1), c.getString(2), c.getString(5), c.getString(4), c.getString(3));
        }
        Log.i("myApp","connexion "+buffer.toString());
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        Log.i("myApp","table connexion does not exist");  
      }
      else
      {
        Log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    
    //if user has recorder at least one club
    if (Record.recordlist.size() > 0)
    {
      for(Record r:Record.recordlist)
      {
        Button btn = new Button(this);
        btn.setText(r.getClub());
        int sport_picture = 0;
        if (r.getSport().equals("football"))
        {
          sport_picture = R.drawable.football;
        }
        if (r.getSport().equals("handball"))
        {
          sport_picture = R.drawable.handball;
        }
        if (r.getSport().equals("basketball"))
        {
          sport_picture = R.drawable.basketball;
        }
        if (r.getSport().equals("volleyball"))
        {
          sport_picture = R.drawable.volleyball;
        }
        if (r.getSport().equals("rugby"))
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
            Record r = Record.recordlist.get(v.getId());
            Global.current_club = r.getClub();
            Global.current_sport = r.getSport();
            Global.current_club_id = r.getIdClub();
            Record.current_record = r;
            Intent intent = new Intent(ClubActivity.this, CategoryActivity.class);
            startActivity(intent);
          }
        });
      }
    }
    else
    {
      Intent intent = new Intent(ClubActivity.this, MainActivity.class);
      startActivity(intent);
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
      case android.R.id.home:
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) 
        {
          // This activity is NOT part of this app's task, so create a new task
          // when navigating up, with a synthesized back stack.
          TaskStackBuilder.create(this)
          // Add all of this activity's parents to the back stack
          .addNextIntentWithParentStack(upIntent)
          // Navigate up to the closest parent
          .startActivities();
        } 
        else 
        {
          // This activity is part of this app's task, so simply
          // navigate up to the logical parent activity.
          NavUtils.navigateUpTo(this, upIntent);
        }
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }    

  }
  
  public void dialogBoxRemoveAll() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner de tous ces clubs?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
          //if user confirm the deletion, delete the 4 tables used for notification and connection
          Global.db.execSQL("DROP TABLE IF EXISTS connexion");
          Global.db.execSQL("DROP TABLE IF EXISTS dateresultat");
          Global.db.execSQL("DROP TABLE IF EXISTS dateconvocation");
          Global.db.execSQL("DROP TABLE IF EXISTS dateinformation");
          Record.recordlist.clear();
          finish();
          startActivity(getIntent());
      }
    });

    alertDialogBuilder.setNegativeButton("NON",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
      }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
