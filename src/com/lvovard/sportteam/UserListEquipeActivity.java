package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class UserListEquipeActivity extends Activity
{
  
  static List<String> listeequipe = new ArrayList<String>();
  private LinearLayout layout;
  private  ProgressDialog progressBar;
  Context ctx = UserListEquipeActivity.this;
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
    userclub = Global.getCurrentUserClub(ctx);
    setContentView(R.layout.activity_equipe);
    
    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_group_white_24dp);
    actionBar.setTitle(userclub.nom+"/"+Global.getCurrentCat(ctx));
    actionBar.setSubtitle("Equipes");
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setMessage("recherche des equipes ...");
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.show();
    new GetNbTeamMain().execute(userclub.id_club,Global.getCurrentCat(ctx));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.equipe, menu);
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
        } else 
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
  
  private class GetNbTeamMain extends GetNbTeam
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(Integer nbteam) 
    {
      progressBar.dismiss();
      listeequipe.clear();
      for(int i = 1; i <= nbteam; i++)
      {
        listeequipe.add(Global.getCurrentCat(ctx)+" "+i);
      }
      Collections.sort(listeequipe);
      for(String eq:listeequipe)
      {
          Button btn = new Button(ctx);
          //btn.setText(eq);
          /////////////////
          Drawable drawablesport = null;

          drawablesport = getResources().getDrawable(Global.getSportPicture(ctx));

          drawablesport.setBounds(0, 0, (int)(drawablesport.getIntrinsicWidth()*1.5),(int)(drawablesport.getIntrinsicHeight()*1.5));
          ScaleDrawable sdsport = new ScaleDrawable(drawablesport, 0, 1, 1);
          btn.setCompoundDrawablesWithIntrinsicBounds(sdsport.getDrawable(), null, null, null); 
          btn.setText(Html.fromHtml("<big><b><i>"+eq+"</i></b></big><br/><small>Accedez aux convocations, résultats, informations et evenements des "+eq+"</small>"));
          /////////////////
          btn.setId(listeequipe.indexOf(eq));
          layout.addView(btn);
          btn.setOnClickListener(new OnClickListener() 
          {
            @Override
            public void onClick(View v) 
            {
              Global.setCurrentEquipe(ctx,listeequipe.get(v.getId()));
              Intent intent = new Intent(ctx, UserListChoiceActivity.class);
              startActivity(intent);
            }
          });
      }
    }
  }
  
  public void dialogBoxRemoveCat() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner de la catégorie "+Global.getCurrentCat(ctx)+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {

          //log.i("myApp", "Remove cat from connexion DB for "+userclub.nom+" "+Global.getCurrentCat(ctx));
          Global.ConnexionRemoveUserCat(ctx, userclub.id_club, Global.getCurrentCat(ctx));
          Global.ConnexionPrintDataBase(ctx);
          finish();
          List<Categorie> listcat = Global.ConnexionGetCat(ctx,userclub.id_club);
          if (listcat.size() == 0)
          {
            BothModeAddClubActivity.state = "START";
            Intent intent = new Intent(ctx, BothModeListClubActivity.class);
            intent.putExtra("mode", "user");
            startActivity(intent);
          }
          else
          {
            Intent intent = new Intent(ctx, UserListCategoryActivity.class);
            startActivity(intent);
          }
      }
    });

    alertDialogBuilder.setNegativeButton("NON",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) {

      }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
