package com.lvovard.sportteam;



import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfodetailsActivity extends Activity {

  LinearLayout layout;
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_infodetails);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    Info info = InfoActivity.infolist.get(InfoActivity.infoselected);
    setTitle(Global.current_club+"/"+Global.current_equipe+"\r\n"+Global.current_choice);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+Global.current_equipe);
    actionBar.setSubtitle(Global.current_choice + " details");
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    tv = new TextView(InfodetailsActivity.this);
    //
    String dest = "";
    String date = "";
    String heure = "";
    if (info.id_equipe.contains(Global.current_cat+"-all"))
    {
      dest = "<big>informations pour tous les "+Global.current_cat+"s</big><br/><br/>";
    }
    if (info.id_equipe.contains("all-all"))
    {
      dest = "<big>informations pour tout le club</big><br/><br/>";
    }
    if (info.id_equipe.contains(Global.current_equipe.replace(" ", "-")))
    {
      dest = "<big>informations pour tous les "+Global.current_equipe.split(" ")[0]+"s "+Global.current_equipe.split(" ")[1]+"</big><br/><br/>";
    }
    
    if (! info.date_info.contains("auto"))
    {
      date = "<big>"+info.date_info+"</big><br/><br/>";
    }
    
    if (! info.heure_info.contains("empty"))
    {
      heure = "<big>"+info.heure_info+"</big><br/><br/>";
    }
    
    String objet = "<big>"+info.objet+"</big><br/><br/>";
    

    String message = "<big>"+info.message.replace("\n","<br/>" )+"</big><br/><br/>";

    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    tv.setText(Html.fromHtml(dest+date+heure+objet+message));
    tv.setMovementMethod(new ScrollingMovementMethod());
    tv.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
    layout.addView(tv);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.infodetails, menu);
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
}
