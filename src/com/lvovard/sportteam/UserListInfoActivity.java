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

public class UserListInfoActivity extends Activity 
{
  
  static int infoselected;
  public static List<Info> infolist;
  LinearLayout layout;
  private  ProgressDialog progressBar;
  String notifinfoid = "nothing";
  Context ctx = UserListInfoActivity.this;
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
    setContentView(R.layout.activity_info);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);

    ////
    
    Intent i=getIntent();
    
    boolean notif = i.getBooleanExtra("notif", false);
    
    if (notif)
    {
      List<Categorie> catlist = new ArrayList<Categorie>();
      Club c = new Club(i.getStringExtra("sport"), "", i.getStringExtra("club"), i.getStringExtra("clubid"), "", "", "", catlist, "", "", "", "");
      Global.setCurrentUserClub(ctx, c);
      Global.setCurrentBkgPicture(ctx,i.getIntExtra("bkg",-1));
      notifinfoid = i.getStringExtra("infoid");
      Global.setCurrentCat(ctx,i.getStringExtra("cat"));
      Global.setCurrentEquipe(ctx,i.getStringExtra("equipe"));
      Global.setCurrentChoice(ctx,i.getStringExtra("choice"));
    }
    userclub = Global.getCurrentUserClub(ctx);
    
    setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx)+"\r\n"+Global.getCurrentChoice(ctx));
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_info_white_24dp);
    actionBar.setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx));
    actionBar.setSubtitle(Global.getCurrentChoice(ctx));
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des informations club ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    new GetInfoMain().execute(Global.getCurrentCat(ctx),Global.getCurrentEquipe(ctx),userclub.id_club); 
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.info, menu);
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
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
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
  
  private class GetInfoMain extends GetInfo
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(List<Info> infolist) 
    {
      progressBar.dismiss();
      UserListInfoActivity.infolist = infolist;
      String currentmonth = "";
      for(Info info:infolist)
      {
        if (! currentmonth.contains(info.date_info.split(" ")[2]))
        {
          TextView txtmonth = new TextView(ctx);
          String monthstr = info.date_info.split(" ")[2].toUpperCase()+" "+info.date_info.split(" ")[3];
          txtmonth.setText(Html.fromHtml("<big>"+monthstr+"</big>"));
          currentmonth = info.date_info.split(" ")[2];
          txtmonth.setTypeface(null, Typeface.BOLD);
          txtmonth.setGravity(Gravity.CENTER);
          layout.addView(txtmonth);
        }
        
        LinearLayout layout2 = new LinearLayout(ctx);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(layout2);
        
        Button btn1 = new Button(ctx);
        String date = "";
        String heure = "";
        if (info.date_info.contains("auto"))
        {
          date = "<small>information<br/>"+info.date_info.split(" ")[0]+"</small><br/><big>"+info.date_info.split(" ")[1]+"</big><br/><small>"+info.date_info.split(" ")[2]+"</small><br/>";
        }
        else
        {
          date = "<small>evenement<br/>"+info.date_info.split(" ")[0]+"</small><br/><big>"+info.date_info.split(" ")[1]+"</big><br/><small>"+info.date_info.split(" ")[2]+"</small><br/>";
        }
        if (! info.heure_info.contains("empty"))
        {
          heure = "<big>"+info.heure_info+"</big>";
        }
        
        btn1.setText(Html.fromHtml(date+heure));


        
        if ( (info.id_equipe.contains(Global.getCurrentCat(ctx)+"-all")) || (info.id_equipe.contains("all-all")) || (info.id_equipe.contains(Global.getCurrentEquipe(ctx).replace(" ", "-"))))
        {
          String dest = "";
          if (info.id_equipe.contains(Global.getCurrentCat(ctx)+"-all"))
          {
            dest = "<small>informations pour tous les "+Global.getCurrentCat(ctx)+"s</small><br/>";
          }
          if (info.id_equipe.contains("all-all"))
          {
            dest = "<small>informations pour tout le club</small><br/>";
          }
          if (info.id_equipe.contains(Global.getCurrentEquipe(ctx).replace(" ", "-")))
          {
            dest = "<small>informations pour tous les "+Global.getCurrentEquipe(ctx).split(" ")[0]+"s "+Global.getCurrentEquipe(ctx).split(" ")[1]+"</small><br/>";
          }
          String objet = "<big>"+info.objet+"</big>";
          Button btn2 = new Button(ctx);
          btn2.setText(Html.fromHtml(dest+objet));
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
          btn1.setId(infolist.indexOf(info));
          btn2.setId(infolist.indexOf(info));
          if (notifinfoid.contentEquals(info.id_info))
          {
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            layout2.startAnimation(animation);
          }

          btn1.setOnClickListener(new OnClickListener() 
          {
            @Override
            public void onClick(View v) 
            {
              infoselected = v.getId();
              //choiceselected = v.getId();
              Intent intent = new Intent(ctx, UserInfoDetailsActivity.class);
              startActivity(intent);
            }
          });
          btn2.setOnClickListener(new OnClickListener() 
          {
            @Override
            public void onClick(View v) 
            {
              infoselected = v.getId();
              //choiceselected = v.getId();
              Intent intent = new Intent(ctx, UserInfoDetailsActivity.class);
              startActivity(intent);
            }
          });
        }
      }
    }
  }
}
