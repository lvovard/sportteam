package com.lvovard.sportteam;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AdminWayToAddClubActivity extends Activity
{
  
  protected Button btnExisting;
  protected Button btnNew;
  Context ctx = AdminWayToAddClubActivity.this;

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
    setContentView(R.layout.activity_admin_way_to_add_club);
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_settings_white_24dp);
    actionBar.setTitle("SportTeam");
    actionBar.setSubtitle("Gestion de club");
    btnExisting = (Button) findViewById(R.id.btnExisting);
    btnNew = (Button) findViewById(R.id.btnNew);
    ////////////
    Drawable drawableexisting = null;
    Drawable drawablenew = null;

    if (Build.VERSION.SDK_INT >= 22)
    {
      drawableexisting = getResources().getDrawable(R.drawable.ic_build_black_24dp,getApplicationContext().getTheme());
      drawablenew = getResources().getDrawable(R.drawable.ic_fiber_new_black_24dp,getApplicationContext().getTheme());
    }
    else
    {
      drawableexisting = getResources().getDrawable(R.drawable.ic_build_black_24dp);
      drawablenew = getResources().getDrawable(R.drawable.ic_fiber_new_black_24dp);
    }

    drawableexisting.setBounds(0, 0, (int)(drawableexisting.getIntrinsicWidth()*1.5),(int)(drawableexisting.getIntrinsicHeight()*1.5));
    ScaleDrawable sdexisting = new ScaleDrawable(drawableexisting, 0, 1, 1);
    btnExisting.setCompoundDrawables(sdexisting.getDrawable(), null, null, null); 
    btnExisting.setText(Html.fromHtml("<big><b><i>GERER UN CLUB EXISTANT</i></b></big><br/><small>Le club existe déjà et vous souhaitez vous ajouter au groupe d'admin de ce club</small>"));
    drawablenew.setBounds(0, 0, (int)(drawablenew.getIntrinsicWidth()*1.5),(int)(drawablenew.getIntrinsicHeight()*1.5));
    ScaleDrawable sdnew = new ScaleDrawable(drawablenew, 0, 1, 1);
    btnNew.setCompoundDrawables(sdnew.getDrawable(), null, null, null); 
    btnNew.setText(Html.fromHtml("<big><b><i>CREER UN NOUVEAU CLUB</i></b></big><br/><small>Le club n'existe pas encore, créez-le et gérez votre club avec SportTeam </small>"));
    ////////////
    addListenerOnButton();
  }
  
  public void addListenerOnButton() 
  {
    btnExisting.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        //if no club -> create activity else go to club list
        Intent intent = new Intent(ctx, BothModeAddClubActivity.class);
        intent.putExtra("mode","admin");
        startActivity(intent);
      }
    });
    
    btnNew.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Intent intent = new Intent(ctx, AdminCreateClubActivity.class);
          startActivity(intent);
        }
      });
  } 

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_way_to_add_club, menu);
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
        Intent intenthome = new Intent(ctx, MainActivity.class);
        startActivity(intenthome);
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
