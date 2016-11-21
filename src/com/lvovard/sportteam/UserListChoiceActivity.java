package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class UserListChoiceActivity extends Activity
{
  
  static List<String> listechoice = new ArrayList<String>();
  Context ctx = UserListChoiceActivity.this;
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
  
  @SuppressWarnings("deprecation")
  @Override
  protected void onResume() 
  {
    super.onResume();
    userclub = Global.getCurrentUserClub(ctx);
    setContentView(R.layout.activity_choice);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx));
    actionBar.setSubtitle("Choix");
    actionBar.setIcon(R.drawable.ic_visibility_white_24dp);
    
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));
    
    listechoice.clear();
    listechoice.add("Convocations");
    listechoice.add("Resultats");
    listechoice.add("Informations / Evenements");
    listechoice.add("Statistiques");
    
    for(String eq:listechoice)
    {
      Button btn = new Button(this);
      //btn.setText(eq);
      btn.setId(listechoice.indexOf(eq));
      layout.addView(btn);
      ///////////
      String text = null;
      int icon = 0;
      if (eq.contentEquals("Convocations"))
      {
        text = "<big><b><i>CONVOCATIONS</i></b></big><br/><small>Consultez les convocations pour tous les matchs des "+Global.getCurrentEquipe(ctx)+"</small>";
        icon = R.drawable.ic_event_black_24dp;
      }
      if (eq.contentEquals("Resultats"))
      {
        text = "<big><b><i>RESULTATS</i></b></big><br/><small>Consultez les resultats pour tous les matchs des "+Global.getCurrentEquipe(ctx)+"</small>";
        icon = R.drawable.ic_looks_two_black_24dp;
      }
      if (eq.contentEquals("Informations / Evenements"))
      {
        text = "<big><b><i>INFOS / EVENEMENTS</i></b></big><br/><small>Consultez toutes les informations et evenements des "+Global.getCurrentEquipe(ctx)+"</small>";
        icon = R.drawable.ic_info_black_24dp;
      }
      if (eq.contentEquals("Statistiques"))
      {
        text = "<big><b><i>STATISTIQUES</i></b></big><br/><small>Consultez toutes les statistiques des "+Global.getCurrentEquipe(ctx)+"</small>";
        icon = R.drawable.ic_pie_chart_black_24dp;
        btn.setVisibility(View.GONE);
      }
      
      Drawable drawableadd = null;
      if (Build.VERSION.SDK_INT >= 22)
      {
        drawableadd = getResources().getDrawable(icon,getApplicationContext().getTheme());
      }
      else
      {
        drawableadd = getResources().getDrawable(icon);
      }
      
      drawableadd.setBounds(0, 0, (int)(drawableadd.getIntrinsicWidth()*1.5),(int)(drawableadd.getIntrinsicHeight()*1.5));
      ScaleDrawable sdadd = new ScaleDrawable(drawableadd, 0, 1, 1);
      btn.setCompoundDrawables(sdadd.getDrawable(), null, null, null);  
      btn.setText(Html.fromHtml(text));
      /////////
      btn.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Global.setCurrentChoice(ctx,listechoice.get(v.getId()));
          Intent intent = null;
          if (Global.getCurrentChoice(ctx).contentEquals("Convocations"))
          {
            intent = new Intent(ctx, UserListConvocationActivity.class);
          }
          else
          {
            if (Global.getCurrentChoice(ctx).contentEquals("Resultats"))
            {
              intent = new Intent(ctx, UserListResultatActivity.class);
            }
            else
            {
              if (Global.getCurrentChoice(ctx).contentEquals("Informations / Evenements"))
              {
                intent = new Intent(ctx, UserListInfoActivity.class);
              }
              else
              {
                if (Global.getCurrentChoice(ctx).contentEquals("Statistiques"))
                {
                  intent = new Intent(ctx, StatisticListActivity.class);
                }
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
      case R.id.action_home:
        
          Intent intent = new Intent(ctx, MainActivity.class);
          startActivity(intent);  
          return true;
        

      default:
        return super.onOptionsItemSelected(item);
    }    
  }
}
