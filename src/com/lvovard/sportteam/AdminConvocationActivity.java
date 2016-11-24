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

public class AdminConvocationActivity extends Activity
{
  protected Button btnAddConv;
  protected Button btnModifyConv;
  protected Button btnRemoveConv;
  Context ctx = AdminConvocationActivity.this;
  Club adminclub;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_convocation);
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
    setContentView(R.layout.activity_admin_convocation);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_event_white_24dp);
    //actionBar.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    actionBar.setTitle("Gestion des convocations");
    actionBar.setSubtitle("Choix");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    this.btnAddConv = (Button) findViewById(R.id.btnAddConv);
    this.btnModifyConv = (Button) findViewById(R.id.btnModifyConv);
    this.btnRemoveConv = (Button) findViewById(R.id.btnRemoveConv);
    
    Drawable drawableadd = null;
    Drawable drawablemodify = null;
    Drawable drawableremove = null;

    drawableadd = getResources().getDrawable(R.drawable.ic_add_circle_black_24dp);
    drawablemodify = getResources().getDrawable(R.drawable.ic_create_black_24dp);
    drawableremove = getResources().getDrawable(R.drawable.ic_remove_circle_black_24dp);
    
    drawableadd.setBounds(0, 0, (int)(drawableadd.getIntrinsicWidth()*1.5),(int)(drawableadd.getIntrinsicHeight()*1.5));
    ScaleDrawable sdadd = new ScaleDrawable(drawableadd, 0, 1, 1);
    drawablemodify.setBounds(0, 0, (int)(drawablemodify.getIntrinsicWidth()*1.5),(int)(drawablemodify.getIntrinsicHeight()*1.5));
    ScaleDrawable sdmodify = new ScaleDrawable(drawablemodify, 0, 1, 1);
    drawableremove.setBounds(0, 0, (int)(drawableremove.getIntrinsicWidth()*1.5),(int)(drawableremove.getIntrinsicHeight()*1.5));
    ScaleDrawable sdremove = new ScaleDrawable(drawableremove, 0, 1, 1);
    btnAddConv.setCompoundDrawablesWithIntrinsicBounds(sdadd.getDrawable(), null, null, null); 
    btnModifyConv.setCompoundDrawablesWithIntrinsicBounds(sdmodify.getDrawable(), null, null, null); 
    btnRemoveConv.setCompoundDrawablesWithIntrinsicBounds(sdremove.getDrawable(), null, null, null); 
    btnAddConv.setText(Html.fromHtml("<big><b><i>AJOUTER</i></b></big><br/><small>Créer une convocation pour le club de "+adminclub.nom+"</small>"));
    btnModifyConv.setText(Html.fromHtml("<big><b><i>MODIFIER</i></b></big><br/><small>Modifier une convocation existante (joueurs, heure, lieu,...)</small>"));
    btnRemoveConv.setText(Html.fromHtml("<big><b><i>SUPPRIMER</i></b></big><br/><small>Supprimer une convocation du club de "+adminclub.nom+"</small>"));
    
    btnAddConv.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeConvocationActivity.class);
        intent.putExtra("mode","add");
        startActivity(intent);
      }
    });
    
    btnModifyConv.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeConvocationActivity.class);
        intent.putExtra("mode","modify");
        startActivity(intent);
      }
    });
    
    btnRemoveConv.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, AdminChangeConvocationActivity.class);
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
