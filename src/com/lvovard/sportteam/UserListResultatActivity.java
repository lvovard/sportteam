package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
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

public class UserListResultatActivity extends Activity 
{
  
  static int resultselected;
  public static List<Resultat> resultlist;
  LinearLayout layout;
  private  ProgressDialog progressBar;
  String notifresid = "nothing";
  Context ctx = UserListResultatActivity.this;
  Club userclub;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);      
  }
  
  @Override
  protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      setIntent(intent);
  }
  
  @Override
  protected void onResume() 
  {
    super.onResume();
    setContentView(R.layout.activity_resultat);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ///
    Intent i=getIntent();
    
    boolean notif = i.getBooleanExtra("notif", false);
    
    if (notif)
    {
      List<Categorie> catlist = new ArrayList<Categorie>();
      Club c = new Club(i.getStringExtra("sport"), "", i.getStringExtra("club"), i.getStringExtra("clubid"), "", "", "", catlist, "", "", "", "");
      Global.setCurrentUserClub(ctx, c);
      Global.setCurrentBkgPicture(ctx,i.getIntExtra("bkg",-1));
      notifresid = i.getStringExtra("resultid");
      Global.setCurrentCat(ctx,i.getStringExtra("cat"));
      Global.setCurrentEquipe(ctx,i.getStringExtra("equipe"));
      Global.setCurrentChoice(ctx,i.getStringExtra("choice"));
    }
    userclub = Global.getCurrentUserClub(ctx);
    
    setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx)+"\r\n"+Global.getCurrentChoice(ctx));
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_looks_icon_two_black_24dp);
    actionBar.setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx));
    actionBar.setSubtitle(Global.getCurrentChoice(ctx));
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des resultats ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    new GetResultatMain().execute(Global.getCurrentCat(ctx),Global.getCurrentEquipe(ctx),userclub.id_club); 
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
        
      case R.id.action_home:
        
          Intent intent = new Intent(ctx, MainActivity.class);
          startActivity(intent);  
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

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(List<Resultat> resultlist) 
    {
      progressBar.dismiss();
      UserListResultatActivity.resultlist = resultlist;
      String currentmonth = "";
      for(Resultat res:resultlist)
      {
        if (! currentmonth.contains(res.date_match.split(" ")[2]))
        {
          TextView txtmonth = new TextView(ctx);
          String monthstr = res.date_match.split(" ")[2].toUpperCase()+" "+res.date_match.split(" ")[3];
          txtmonth.setText(Html.fromHtml("<blink><big>"+monthstr+"</big></blink>"));
          currentmonth = res.date_match.split(" ")[2];
          txtmonth.setTypeface(null, Typeface.BOLD);
          txtmonth.setGravity(Gravity.CENTER);
          layout.addView(txtmonth);
        }
        LinearLayout layout2 = new LinearLayout(ctx);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(layout2);
        
        Button btn1 = new Button(ctx);
        btn1.setText(Html.fromHtml("<small>"+res.date_match.split(" ")[0]+"</small><br/><big>"+res.date_match.split(" ")[1]+"</big><br/><small>"+res.date_match.split(" ")[2]+"</small><br/><small>"+res.date_match.split(" ")[3]+"</small>"));
        
        Button btn2 = new Button(ctx);
        
        
        String info = "<small>"+res.competition +" - "+res.lieu+"</small><br/>";
        String match = "<big>"+userclub.nom+"<br/>"+res.adversaire+"</big><br/>";
        String score = "<big>"+res.score_equipe+"   -   "+res.score_adversaire+"</big>";
        if (res.lieu.contains("exterieur"))
        {
          match = "<big>"+res.adversaire+"<br/>"+userclub.nom+"</big><br/>";
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
            Intent intent = new Intent(ctx, UserResultDetailsActivity.class);
            startActivity(intent);
          }
        });
        btn2.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            resultselected = v.getId();
            Intent intent = new Intent(ctx, UserResultDetailsActivity.class);
            startActivity(intent);
          }
        });
      }
    }
  }
}
