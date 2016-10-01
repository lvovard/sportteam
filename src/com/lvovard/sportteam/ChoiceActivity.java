package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChoiceActivity extends Activity
{
  
  static List<String> listechoice = new ArrayList<String>();
	
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choice);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+Global.current_equipe);
    actionBar.setSubtitle("Choix");
    
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    listechoice.clear();
    listechoice.add("Convocations");
    listechoice.add("Resultats");
    listechoice.add("Informations / Evenements");
    
    for(String eq:listechoice)
    {
      Button btn = new Button(this);
      btn.setText(eq);
      btn.setId(listechoice.indexOf(eq));
      layout.addView(btn);
      btn.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Global.current_choice = listechoice.get(v.getId());
          Intent intent = null;
          if (Global.current_choice.contentEquals("Convocations"))
          {
            intent = new Intent(ChoiceActivity.this, ConvocationActivity.class);
          }
          else
          {
            if (Global.current_choice.contentEquals("Resultats"))
            {
              intent = new Intent(ChoiceActivity.this, ResultatActivity.class);
            }
            else
            {
              if (Global.current_choice.contentEquals("Informations / Evenements"))
              {
                intent = new Intent(ChoiceActivity.this, InfoActivity.class);
              }
            }
          }
          startActivity(intent);
        }
      });
    }   
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.choice, menu);
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
}
