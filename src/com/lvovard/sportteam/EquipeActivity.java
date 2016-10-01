package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class EquipeActivity extends Activity
{
  
  static List<String> listeequipe = new ArrayList<String>();
  private LinearLayout layout;
  private  ProgressDialog progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equipe);
    
    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+Global.current_cat);
    actionBar.setSubtitle("Equipes");
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des equipes ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    new GetNbTeamMain().execute(Global.current_club_id,Global.current_cat);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.equipe, menu);
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
    
      case R.id.action_delete:
        MainActivity.state = "START";
        dialogBoxRemoveCat(); 
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
        } else 
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
  
  private class GetNbTeamMain extends GetNbTeam
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(Integer nbteam) 
    {
      progressBar.dismiss();
      listeequipe.clear();
      for(int i = 1; i <= nbteam; i++)
      {
        listeequipe.add(Global.current_cat+" "+i);
      }
      Collections.sort(listeequipe);
      for(String eq:listeequipe)
      {
          Button btn = new Button(EquipeActivity.this);
          btn.setText(eq);
          btn.setId(listeequipe.indexOf(eq));
          layout.addView(btn);
          btn.setOnClickListener(new OnClickListener() 
          {
            @Override
            public void onClick(View v) 
            {
              Global.current_equipe = listeequipe.get(v.getId());
              Intent intent = new Intent(EquipeActivity.this, ChoiceActivity.class);
              startActivity(intent);
            }
          });
      }
    }
  }
  
  public void dialogBoxRemoveCat() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner de la catégorie "+Global.current_cat+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {

          Log.i("myApp", "Remove cat from connexion DB for "+Global.current_club_id+" "+Global.current_cat);
          Global.db.execSQL("DELETE FROM connexion WHERE clubid='"+Global.current_club_id+"' AND cat='"+Global.current_cat+"' ");
          
          Log.i("myApp", "Remove cat from dateresultat DB for "+Global.current_club_id+" "+Global.current_cat);
          Global.db.execSQL("DELETE FROM dateresultat WHERE clubid='"+Global.current_club_id+"' AND cat='"+Global.current_cat+"' ");
          
          Log.i("myApp", "Remove cat from dateconvocation DB for "+Global.current_club_id+" "+Global.current_cat);
          Global.db.execSQL("DELETE FROM dateconvocation WHERE clubid='"+Global.current_club_id+"' AND cat='"+Global.current_cat+"' ");
          
          Log.i("myApp", "Remove cat from dateinformation DB for "+Global.current_club_id+" "+Global.current_cat);
          Global.db.execSQL("DELETE FROM dateinformation WHERE clubid='"+Global.current_club_id+"' AND cat='"+Global.current_cat+"' ");
          
          Record.RemoveRecord(Global.current_club_id,Global.current_cat);
          
          finish();
          if (Record.current_record.category.size() == 1)
          {
            MainActivity.state = "START";
            Intent intent = new Intent(EquipeActivity.this, MainActivity.class);
            startActivity(intent);
          }
          else
          {
            Intent intent = new Intent(EquipeActivity.this, CategoryActivity.class);
            startActivity(intent);
          }
      }
    });

    alertDialogBuilder.setNegativeButton("NON",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) {

      }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
