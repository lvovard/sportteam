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

public class AdminPwdActivity extends Activity
{

  protected Button btnChangeAdminPwd;
  protected Button btnChangeUserPwd;
  Context ctx = AdminPwdActivity.this;
  Club adminclub;
  
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
    adminclub = Global.getCurrentAdminClub(ctx);
    setContentView(R.layout.activity_admin_pwd);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_lock_white_24dp);
    actionBar.setTitle("Gestion des mots de passe");
    actionBar.setSubtitle("Choix");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    this.btnChangeAdminPwd = (Button) findViewById(R.id.btnChangeAdminPwd);
    this.btnChangeUserPwd = (Button) findViewById(R.id.btnChangeUserPwd);
    
    //////////
    Drawable drawableadmin = null;
    Drawable drawableuser = null;

    drawableadmin = getResources().getDrawable(R.drawable.ic_lockadmin_black_24dp);
    drawableuser = getResources().getDrawable(R.drawable.ic_lockuser_black_24dp);
    
    drawableadmin.setBounds(0, 0, (int)(drawableadmin.getIntrinsicWidth()*1.5),(int)(drawableadmin.getIntrinsicHeight()*1.5));
    ScaleDrawable sdadmin = new ScaleDrawable(drawableadmin, 0, 1, 1);
    drawableuser.setBounds(0, 0, (int)(drawableuser.getIntrinsicWidth()*1.5),(int)(drawableuser.getIntrinsicHeight()*1.5));
    ScaleDrawable sduser = new ScaleDrawable(drawableuser, 0, 1, 1);
    btnChangeAdminPwd.setCompoundDrawablesWithIntrinsicBounds(sdadmin.getDrawable(), null, null, null); 
    btnChangeUserPwd.setCompoundDrawablesWithIntrinsicBounds(sduser.getDrawable(), null, null, null); 
    btnChangeAdminPwd.setText(Html.fromHtml("<big><b><i>MODIFICATION ADMIN</i></b></big><br/><small>Modofiez le mot de passe admin pour le club de "+adminclub.nom+"</small>"));
    btnChangeUserPwd.setText(Html.fromHtml("<big><b><i>MODIFICATION UTILISATEUR</i></b></big><br/><small>Modofiez le mot de passe utilisateur pour le club de "+adminclub.nom+"</small>"));
    
    
    //////////
    
    btnChangeAdminPwd.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangePwdActivity.class);
        intent.putExtra("change_mode","admin");
        startActivity(intent);
      }
    });
    
    btnChangeUserPwd.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangePwdActivity.class);
        intent.putExtra("change_mode","utilisateur");
        startActivity(intent);
      }
    });
  }
    

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_pwd, menu);
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
        Intent intenthome = new Intent(ctx, AdminClubActivity.class);
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
