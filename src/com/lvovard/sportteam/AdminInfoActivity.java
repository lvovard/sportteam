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

public class AdminInfoActivity extends Activity
{
  protected Button btnAddInfo;
  protected Button btnModifyInfo;
  protected Button btnRemoveInfo;
  Context ctx = AdminInfoActivity.this;
  Club adminclub;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_info);
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
    setContentView(R.layout.activity_admin_info);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_info_white_24dp);
    actionBar.setTitle("Gestion des informations/evenements");
    actionBar.setSubtitle("Choix");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    this.btnAddInfo = (Button) findViewById(R.id.btnAddInfo);
    this.btnModifyInfo = (Button) findViewById(R.id.btnModifyInfo);
    this.btnRemoveInfo = (Button) findViewById(R.id.btnRemoveInfo);
    
    
    Drawable drawableadd = null;
    Drawable drawablemodify = null;
    Drawable drawableremove = null;

    if (Build.VERSION.SDK_INT >= 22)
    {
      drawableadd = getResources().getDrawable(R.drawable.ic_add_circle_black_24dp,getApplicationContext().getTheme());
      drawablemodify = getResources().getDrawable(R.drawable.ic_create_black_24dp,getApplicationContext().getTheme());
      drawableremove = getResources().getDrawable(R.drawable.ic_remove_circle_black_24dp,getApplicationContext().getTheme());
    }
    else
    {
      drawableadd = getResources().getDrawable(R.drawable.ic_add_circle_black_24dp);
      drawablemodify = getResources().getDrawable(R.drawable.ic_create_black_24dp);
      drawableremove = getResources().getDrawable(R.drawable.ic_remove_circle_black_24dp);
    }
    
    drawableadd.setBounds(0, 0, (int)(drawableadd.getIntrinsicWidth()*1.5),(int)(drawableadd.getIntrinsicHeight()*1.5));
    ScaleDrawable sdadd = new ScaleDrawable(drawableadd, 0, 1, 1);
    drawablemodify.setBounds(0, 0, (int)(drawablemodify.getIntrinsicWidth()*1.5),(int)(drawablemodify.getIntrinsicHeight()*1.5));
    ScaleDrawable sdmodify = new ScaleDrawable(drawablemodify, 0, 1, 1);
    drawableremove.setBounds(0, 0, (int)(drawableremove.getIntrinsicWidth()*1.5),(int)(drawableremove.getIntrinsicHeight()*1.5));
    ScaleDrawable sdremove = new ScaleDrawable(drawableremove, 0, 1, 1);
    btnAddInfo.setCompoundDrawables(sdadd.getDrawable(), null, null, null); 
    btnModifyInfo.setCompoundDrawables(sdmodify.getDrawable(), null, null, null); 
    btnRemoveInfo.setCompoundDrawables(sdremove.getDrawable(), null, null, null); 
    btnAddInfo.setText(Html.fromHtml("<big><b><i>AJOUTER</i></b></big><br/><small>Ajouter une information, un evenement pour le club de "+adminclub.nom+"</small>"));
    btnModifyInfo.setText(Html.fromHtml("<big><b><i>MODIFIER</i></b></big><br/><small>Modifier une information, un evenement existant (licence, soirée, repas, entrainement...)</small>"));
    btnRemoveInfo.setText(Html.fromHtml("<big><b><i>SUPPRIMER</i></b></big><br/><small>Supprimer ne information, un evenement du club de "+adminclub.nom+"</small>"));
    
    btnAddInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeInfoActivity.class);
        intent.putExtra("mode","add");
        startActivity(intent);
      }
    });
    
    btnModifyInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeInfoActivity.class);
        intent.putExtra("mode","modify");
        startActivity(intent);
      }
    });
    
    btnRemoveInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeInfoActivity.class);
        intent.putExtra("mode","remove");
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_convocation, menu);
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
