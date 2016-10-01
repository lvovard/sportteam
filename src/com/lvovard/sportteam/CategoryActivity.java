package com.lvovard.sportteam;



import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CategoryActivity extends Activity
{
  

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    //define the bkg picture to use
    if (Global.current_sport.equals("basketball"))
    {
      Global.current_bkg_picture = R.drawable.basketballbkg;
    }
    if (Global.current_sport.equals("football"))
    {
      Global.current_bkg_picture = R.drawable.footballtest;
    }
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.current_bkg_picture);
    //enable back action
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    for (String s:Record.current_record.category)
    {
      Button btn = new Button(this);
      btn.setText(s);
      btn.setId(Record.current_record.category.indexOf(s));
      layout.addView(btn);
      btn.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Global.current_cat = Record.current_record.getCategory().get(v.getId());
          Intent intent = new Intent(CategoryActivity.this, EquipeActivity.class);
          startActivity(intent);
        }
      });
    }
    ActionBar actionBar = getActionBar();
    actionBar.setTitle(Record.current_record.getClub());
    actionBar.setSubtitle("Categories");
    
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.category, menu);
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
        dialogBoxRemoveClub(); 
      return true;
      
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

      default:
      return super.onOptionsItemSelected(item);
    }    

  }
  
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);

      // Checks the orientation of the screen
      if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
      } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
          Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
      }
      switch (getResources().getDisplayMetrics().densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        Toast.makeText(this, "ldpi", Toast.LENGTH_SHORT).show();
          break;
      case DisplayMetrics.DENSITY_MEDIUM:
        Toast.makeText(this, "mdpi", Toast.LENGTH_SHORT).show();
          break;
      case DisplayMetrics.DENSITY_HIGH:
        Toast.makeText(this, "hdpi", Toast.LENGTH_SHORT).show();
          break;
      case DisplayMetrics.DENSITY_XHIGH:
        Toast.makeText(this, "xhdpi", Toast.LENGTH_SHORT).show();
          break;
      }
  }
  
  public void dialogBoxRemoveClub() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner du club "+Global.current_club+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        Global.db.execSQL("DELETE FROM connexion WHERE clubid='"+Global.current_club_id+"' ");
        Global.db.execSQL("DELETE FROM dateresultat WHERE clubid='"+Global.current_club_id+"' ");
        Global.db.execSQL("DELETE FROM dateconvocation WHERE clubid='"+Global.current_club_id+"' ");
        Global.db.execSQL("DELETE FROM dateinformation WHERE clubid='"+Global.current_club_id+"' ");
        Record.RemoveRecord(Global.current_club_id);
        finish();
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        startActivity(intent); 
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
