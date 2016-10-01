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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultdetailsActivity extends Activity 
{
  
  LinearLayout layout;
  TextView tv;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultdetails);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    Resultat result = ResultatActivity.resultlist.get(ResultatActivity.resultselected);
    setTitle(Global.current_club+"/"+Global.current_equipe+"\r\n"+Global.current_choice);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+Global.current_equipe);
    actionBar.setSubtitle(Global.current_choice + " details");
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);

    tv = new TextView(ResultdetailsActivity.this);
    //
    String detail = 
        "<big>"+result.date_match+"</big><br/><br/>"+
        "<big>"+result.lieu+"</big><br/><br/>"+
        "<big>"+result.competition+"</big><br/><br/>"+
        "<big>"+Global.current_equipe+"</big><br/><br/>"+
        "<big>contre "+result.adversaire+"</big><br/><br/>";
    
    String score = 
        "<big>"+Global.current_club+" - "+result.adversaire+"</big><br/>"+
        "<big>"+result.score_equipe+" - "+result.score_adversaire+"</big><br/><br/><br/>";
    
    if (result.lieu.contains("exterieur"))
    {
      score = 
          "<big>"+result.adversaire+" - "+Global.current_club+"</big><br/>"+
          "<big>"+result.score_adversaire+" - "+result.score_equipe+"</big><br/><br/><br/>";
    }
    String detailmatch = "<big>Details du match:<br/>"+result.detail.replace("\n","<br/>" )+"</big>";

    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    tv.setText(Html.fromHtml(detail+score+detailmatch));
    tv.setMovementMethod(new ScrollingMovementMethod());
    tv.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
    layout.addView(tv);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.resultdetails, menu);
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
