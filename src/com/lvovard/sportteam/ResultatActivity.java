package com.lvovard.sportteam;

import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultatActivity extends Activity 
{
  
  static int resultselected;
  public static List<Resultat> resultlist;
  LinearLayout layout;
  private  ProgressDialog progressBar;
  String notifresid = "nothing";

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_resultat);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ///
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("club"))) {
      Global.current_club = i.getStringExtra("club");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("cat"))) {
      Global.current_cat = i.getStringExtra("cat");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("equipe"))) {
      Global.current_equipe = i.getStringExtra("equipe");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("choice"))) {
      Global.current_choice = i.getStringExtra("choice");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("sport"))) {
      Global.current_sport = i.getStringExtra("sport");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("clubid"))) {
      Global.current_club_id = i.getStringExtra("clubid");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("bkg"))) {
      Global.current_bkg_picture = Integer.parseInt(i.getStringExtra("bkg"));
    }
    if (! TextUtils.isEmpty(i.getStringExtra("resultid"))) {
      notifresid = i.getStringExtra("resultid");
    }
    ///
    
    setTitle(Global.current_club+"/"+Global.current_equipe+"\r\n"+Global.current_choice);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+Global.current_equipe);
    actionBar.setSubtitle(Global.current_choice);
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des resultats ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    new GetResultatMain().execute(Global.current_cat,Global.current_equipe,Global.current_club_id);       
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.resultat, menu);
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
  
  private class GetResultatMain extends GetResultat
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Resultat> resultlist) 
    {
      progressBar.dismiss();
      ResultatActivity.resultlist = resultlist;
      String currentmonth = "";
      for(Resultat res:resultlist)
      {
        if (! currentmonth.contains(res.date_match.split(" ")[2]))
        {
          TextView txtmonth = new TextView(ResultatActivity.this);
          String monthstr = res.date_match.split(" ")[2].toUpperCase()+" "+res.date_match.split(" ")[3];
          txtmonth.setText(Html.fromHtml("<blink><big>"+monthstr+"</big></blink>"));
          currentmonth = res.date_match.split(" ")[2];
          txtmonth.setTypeface(null, Typeface.BOLD);
          txtmonth.setGravity(Gravity.CENTER);
          layout.addView(txtmonth);
        }
        LinearLayout layout2 = new LinearLayout(ResultatActivity.this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(layout2);
        
        Button btn1 = new Button(ResultatActivity.this);
        btn1.setText(Html.fromHtml("<small>"+res.date_match.split(" ")[0]+"</small><br/><big>"+res.date_match.split(" ")[1]+"</big><br/><small>"+res.date_match.split(" ")[2]+"</small><br/><small>"+res.date_match.split(" ")[3]+"</small>"));
        
        Button btn2 = new Button(ResultatActivity.this);
        
        
        String info = "<small>"+res.competition +" - "+res.lieu+"</small><br/>";
        String match = "<big>"+Global.current_club+"<br/>"+res.adversaire+"</big><br/>";
        String score = "<big>"+res.score_equipe+"   -   "+res.score_adversaire+"</big>";
        if (res.lieu.contains("exterieur"))
        {
          match = "<big>"+res.adversaire+"<br/>"+Global.current_club+"</big><br/>";
          score = "<big>"+res.score_adversaire+"   -   "+res.score_equipe+"</big>";
        }
        btn2.setText(Html.fromHtml(info+match+score));
        //Button btn = new Button(ResultatActivity.this);
        //btn.setText(res.date_match+"\n\r" + match+"\n\r" +score );
        if (Integer.parseInt(res.score_adversaire) > Integer.parseInt(res.score_equipe))
        {
          btn2.setTextColor(Color.RED);
        }
        if (Integer.parseInt(res.score_adversaire) < Integer.parseInt(res.score_equipe))
        {
          btn2.setTextColor(Color.GREEN);
        } 
        if (Integer.parseInt(res.score_adversaire) == Integer.parseInt(res.score_equipe))
        {
          btn2.setTextColor(Color.BLUE);
        }
        
        layout2.addView(btn1);
        layout2.addView(btn2);
        LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) btn1.getLayoutParams();
        LinearLayout.LayoutParams lParams2 = (LinearLayout.LayoutParams) btn2.getLayoutParams();
        lParams1.height = LayoutParams.MATCH_PARENT;
        lParams1.rightMargin = -20;
        lParams2.height = LayoutParams.MATCH_PARENT;
        lParams1.weight = 0.3f;
        lParams1.width = 0;
        lParams2.weight = 0.7f;
        lParams2.width = 0;
        layout2.setWeightSum(1f);
        btn1.setLayoutParams(lParams1);
        btn2.setLayoutParams(lParams2);
        layout2.refreshDrawableState();
        btn1.setId(resultlist.indexOf(res));
        btn2.setId(resultlist.indexOf(res));
        if (notifresid.contentEquals(res.id_resultat))
        {
          final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
          animation.setDuration(500); // duration - half a second
          animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
          animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
          animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
          layout2.startAnimation(animation);
        }
        
//        btn.setId(resultlist.indexOf(res));
//        layout.addView(btn);
//        btn.setOnClickListener(new OnClickListener() 
//        {
//          @Override
//          public void onClick(View v) 
//          {
//            resultselected = v.getId();
//            Intent intent = new Intent(ResultatActivity.this, ResultdetailsActivity.class);
//            startActivity(intent);
//          }
//        });
        btn1.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            resultselected = v.getId();
            Intent intent = new Intent(ResultatActivity.this, ResultdetailsActivity.class);
            startActivity(intent);
          }
        });
        btn2.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            resultselected = v.getId();
            Intent intent = new Intent(ResultatActivity.this, ResultdetailsActivity.class);
            startActivity(intent);
          }
        });
      }
    }
  }
}
