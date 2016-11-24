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

public class AdminClubActivity extends Activity
{

  protected Button btnPwd;
  protected Button btnCat;
  protected Button btnJoueur;
  protected Button btnConvoc;
  protected Button btnResult;
  protected Button btnInfo;
  Context ctx = AdminClubActivity.this;
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
    setContentView(R.layout.activity_admin_club);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_settings_white_24dp);
    actionBar.setTitle("Administration du club");
    actionBar.setSubtitle("Choix");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    this.btnPwd = (Button) findViewById(R.id.btnPwd);
    this.btnCat = (Button) findViewById(R.id.btnCat);
    this.btnJoueur = (Button) findViewById(R.id.btnJoueur);
    this.btnConvoc = (Button) findViewById(R.id.btnConvoc);
    this.btnResult = (Button) findViewById(R.id.btnResult);
    this.btnInfo = (Button) findViewById(R.id.btnInfo);
    
    //
    Drawable drawablepwd = null;
    Drawable drawablecat = null;
    Drawable drawablejoueur = null;
    Drawable drawableconvoc = null;
    Drawable drawableresult = null;
    Drawable drawableinfo = null;

     
    drawablepwd = getResources().getDrawable(R.drawable.ic_lock_black_24dp);
    drawablecat = getResources().getDrawable(R.drawable.ic_group_black_24dp);
    drawablejoueur = getResources().getDrawable(R.drawable.ic_person_black_24dp);
    drawableconvoc = getResources().getDrawable(R.drawable.ic_event_black_24dp);
    drawableresult = getResources().getDrawable(R.drawable.ic_looks_two_black_24dp);
    drawableinfo = getResources().getDrawable(R.drawable.ic_info_black_24dp);
    
    
    drawablepwd.setBounds(0, 0, (int)(drawablepwd.getIntrinsicWidth()*1.5),(int)(drawablepwd.getIntrinsicHeight()*1.5));
    ScaleDrawable sdpwd = new ScaleDrawable(drawablepwd, 0, 1, 1);
    drawablecat.setBounds(0, 0, (int)(drawablecat.getIntrinsicWidth()*1.5),(int)(drawablecat.getIntrinsicHeight()*1.5));
    ScaleDrawable sdcat = new ScaleDrawable(drawablecat, 0, 1, 1);
    drawablejoueur.setBounds(0, 0, (int)(drawablejoueur.getIntrinsicWidth()*1.5),(int)(drawablejoueur.getIntrinsicHeight()*1.5));
    ScaleDrawable sdjoueur = new ScaleDrawable(drawablejoueur, 0, 1, 1);
    drawableconvoc.setBounds(0, 0, (int)(drawableconvoc.getIntrinsicWidth()*1.5),(int)(drawableconvoc.getIntrinsicHeight()*1.5));
    ScaleDrawable sdconvoc = new ScaleDrawable(drawableconvoc, 0, 1, 1);
    drawableresult.setBounds(0, 0, (int)(drawableresult.getIntrinsicWidth()*1.5),(int)(drawableresult.getIntrinsicHeight()*1.5));
    ScaleDrawable sdresult = new ScaleDrawable(drawableresult, 0, 1, 1);
    drawableinfo.setBounds(0, 0, (int)(drawableinfo.getIntrinsicWidth()*1.5),(int)(drawableinfo.getIntrinsicHeight()*1.5));
    ScaleDrawable sdinfo = new ScaleDrawable(drawableinfo, 0, 1, 1);
    btnPwd.setCompoundDrawablesWithIntrinsicBounds(sdpwd.getDrawable(), null, null, null); 
    btnCat.setCompoundDrawablesWithIntrinsicBounds(sdcat.getDrawable(), null, null, null); 
    btnJoueur.setCompoundDrawablesWithIntrinsicBounds(sdjoueur.getDrawable(), null, null, null); 
    btnConvoc.setCompoundDrawablesWithIntrinsicBounds(sdconvoc.getDrawable(), null, null, null); 
    btnResult.setCompoundDrawablesWithIntrinsicBounds(sdresult.getDrawable(), null, null, null); 
    btnInfo.setCompoundDrawablesWithIntrinsicBounds(sdinfo.getDrawable(), null, null, null); 
    btnPwd.setText(Html.fromHtml("<big><b><i>MOT DE PASSE</i></b></big><br/><small>Vous pourrez modifier le mot de passe utilisateur et admin</small>"));
    btnCat.setText(Html.fromHtml("<big><b><i>CATEGORIES / EQUIPES</i></b></big><br/><small>Vous pourrez ajouter, modifier, supprimer des catégories pour "+adminclub.nom+"</small>"));
    btnJoueur.setText(Html.fromHtml("<big><b><i>JOUEURS / DIRIGEANTS</i></b></big><br/><small>Vous pourrez ajouter ou supprimer des joueurs et dirigeants pour "+adminclub.nom+"</small>"));
    btnConvoc.setText(Html.fromHtml("<big><b><i>CONVOCATIONS</i></b></big><br/><small>Vous pourrez ajouter, modifier ou supprimer des convocations pour "+adminclub.nom+"</small>"));
    btnResult.setText(Html.fromHtml("<big><b><i>RESULTATS</i></b></big><br/><small>Vous pourrez ajouter, modifier ou supprimer des résultats pour "+adminclub.nom+"</small>"));
    btnInfo.setText(Html.fromHtml("<big><b><i>INFOS / EVENEMENTS</i></b></big><br/><small>Vous pourrez ajouter, modifier ou supprimer des informations / evenements pour "+adminclub.nom+"</small>"));
    //
    
    btnPwd.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminPwdActivity.class);
        startActivity(intent);
      }
    });
    
    btnCat.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminCatActivity.class);
        startActivity(intent);
      }
    });
    
    btnJoueur.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminPeopleActivity.class);
        startActivity(intent);
      }
    });
    
    btnConvoc.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminConvocationActivity.class);
        startActivity(intent);
      }
    });
    
    btnResult.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminResultatActivity.class);
        startActivity(intent);
      }
    });
    
    btnInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminInfoActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_club, menu);
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
        Intent intenthome = new Intent(ctx, BothModeListClubActivity.class);
        intenthome.putExtra("mode", "admin");
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
