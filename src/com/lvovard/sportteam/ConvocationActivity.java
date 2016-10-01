package com.lvovard.sportteam;

import java.util.List;


import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ConvocationActivity extends Activity {
  
  static int convocselected;
  public static List<Convocation> convoclist;
  LinearLayout layout;
  ScrollView sv;
  private  ProgressDialog progressBar;
  String notifconvid = "nothing";
  

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_convocation);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    //sv = (ScrollView) findViewById(R.id.scrollview);
    
    ////
    
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
    if (! TextUtils.isEmpty(i.getStringExtra("convid"))) {
      notifconvid = i.getStringExtra("convid");
    }
    ////
    
    setTitle(Global.current_club+"/"+Global.current_equipe+"\r\n"+Global.current_choice);
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Global.current_club+"/"+ " "+Global.current_equipe);
    actionBar.setSubtitle(Global.current_choice);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    //sv.setBackgroundResource(Global.current_bkg_picture);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    //setProgressBarVisibility(true);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des convocations ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();

    new GetConcovMain().execute(Global.current_cat,Global.current_equipe,Global.current_club_id);   
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.convocation, menu);
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
        //Intent upIntent = NavUtils.getParentActivityIntent(this);
        Intent upIntent = new Intent(ConvocationActivity.this, ChoiceActivity.class);
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

      default:
        return super.onOptionsItemSelected(item);
    }    

  }
  
  private class GetConcovMain extends GetConvoc
  {
    @Override
    protected void onPreExecute()
    { 
      //getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
      //getWindow().requestFeature(Window.PROGRESS_VISIBILITY_ON);
      //setProgressBarIndeterminate(false);
      //setProgressBarVisibility(true);
    }

    @Override
    protected void onPostExecute(List<Convocation> convoclist) 
    {
      progressBar.dismiss();
      //getWindow().requestFeature(Window.PROGRESS_VISIBILITY_OFF);
      //setProgressBarVisibility(false);
      //setProgressBarVisibility(false);
      ConvocationActivity.convoclist = convoclist;
      String currentmonth = "";
      for(Convocation conv:convoclist)
      {
        if (! currentmonth.contains(conv.date.split(" ")[2]))
        {
          TextView txtmonth = new TextView(ConvocationActivity.this);
          String monthstr = conv.date.split(" ")[2].toUpperCase()+" "+conv.date.split(" ")[3];
          txtmonth.setText(Html.fromHtml("<blink><big>"+monthstr+"</big></blink>"));
          currentmonth = conv.date.split(" ")[2];
          txtmonth.setTypeface(null, Typeface.BOLD);
          txtmonth.setGravity(Gravity.CENTER);
          layout.addView(txtmonth);
//          final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
//          animation.setDuration(1000); // duration - half a second
//          animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
//          animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
//          animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
//          txtmonth.startAnimation(animation);
        }
        //
        LinearLayout layout2 = new LinearLayout(ConvocationActivity.this);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        layout.addView(layout2);
        
        Button btn1 = new Button(ConvocationActivity.this);
        btn1.setText(Html.fromHtml("<small>"+conv.date.split(" ")[0]+"</small><br/><big>"+conv.date.split(" ")[1]+"</big><br/><small>"+conv.date.split(" ")[2]+"</small><br/><big>"+conv.heure_match+"</big>"));
        Button btn2 = new Button(ConvocationActivity.this);
        btn2.setText(Html.fromHtml("<small>"+conv.competition+"</small><br/><small>"+conv.lieu+"</small><br/><small>contre  </small><br/><big>"+conv.adversaire+"</big>"));

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
        btn1.setId(convoclist.indexOf(conv));
        btn2.setId(convoclist.indexOf(conv));
        if (notifconvid.contentEquals(conv.id_convoc))
        {
          final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
          animation.setDuration(500); // duration - half a second
          animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
          animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
          animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
          layout2.startAnimation(animation);
        }

        //
//        Button btn = new Button(ConvocationActivity.this);
//        btn.setText(conv.date +"\n\r" + conv.lieu + "\n\r" + conv.getAdversaire()  + "\n\r" +conv.competition  );
//        btn.setId(convoclist.indexOf(conv));
        //layout.addView(btn);

//        btn.setOnClickListener(new OnClickListener() 
//        {
//          @Override
//          public void onClick(View v) 
//          {
//            convocselected = v.getId();
//            //choiceselected = v.getId();
//            Intent intent = new Intent(ConvocationActivity.this, ConvocdetailsActivity.class);
//            startActivity(intent);
//          }
//        });
        btn1.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            convocselected = v.getId();
            //choiceselected = v.getId();
            Intent intent = new Intent(ConvocationActivity.this, ConvocdetailsActivity.class);
            startActivity(intent);
          }
        });
        btn2.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            convocselected = v.getId();
            //choiceselected = v.getId();
            Intent intent = new Intent(ConvocationActivity.this, ConvocdetailsActivity.class);
            startActivity(intent);
          }
        });
      }
    }
  }
}
