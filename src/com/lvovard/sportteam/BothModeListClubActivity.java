package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;

//import com.lvovard.sportteam.MyBroadcastReceiver.getConvocationDateNotifMain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BothModeListClubActivity extends Activity
{
  
  List<Club> clublist;
  List<String> clubliststring;
  String mode;
  String caller = null;
  boolean[] clubChecked;
  boolean[] saveClubChecked;
  Context ctx = BothModeListClubActivity.this;
  

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
    setContentView(R.layout.activity_club);
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    //layout.setBackgroundResource(R.drawable.gazon);
    //getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("SportTeam");
    actionBar.setSubtitle("Clubs");
    
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("caller"))) {
      caller = i.getStringExtra("caller");
    }
    
    if (mode.contentEquals("user"))
    {
      clublist = Global.ConnexionGetUserClub(ctx);
      actionBar.setIcon(R.drawable.ic_visibility_white_24dp);
    }
    if (mode.contentEquals("admin"))
    {
      clublist = Global.ConnexionGetAdminClub(ctx);
      actionBar.setIcon(R.drawable.ic_settings_white_24dp);
    }
    
    //if user has recorder at least one club
    if (clublist.size() > 0)
    {
      clubliststring = new ArrayList<String>();
      for(Club c:clublist)
      {
        clubliststring.add(c.nom);
        Button btn = new Button(this);
        //btn.setText(c.nom);
        int sport_picture = 0;
        if (c.sport.equals("football"))
        {
          sport_picture = R.drawable.ballonfootball;
        }
        if (c.sport.equals("handball"))
        {
          sport_picture = R.drawable.ballonhandball;
        }
        if (c.sport.equals("basketball"))
        {
          sport_picture = R.drawable.ballonbasket;
        }
        if (c.sport.equals("rugby"))
        {
          sport_picture = R.drawable.ballonrugby;
        }
        if (c.sport.equals("volleyball"))
        {
          sport_picture = R.drawable.ballonvolley;
        }
        //btn.setCompoundDrawablesWithIntrinsicBounds( sport_picture, 0, 0, 0);
        //btn.setId(c.id_club);
        //////////////
        Drawable drawablesport = null;

        drawablesport = getResources().getDrawable(sport_picture);

        drawablesport.setBounds(0, 0, (int)(drawablesport.getIntrinsicWidth()*1.5),(int)(drawablesport.getIntrinsicHeight()*1.5));
        ScaleDrawable sdsport = new ScaleDrawable(drawablesport, 0, 1, 1);
        btn.setCompoundDrawablesWithIntrinsicBounds(sdsport.getDrawable(), null, null, null); 
        if (mode.contentEquals("user"))
        {
          btn.setText(Html.fromHtml("<big><b><i>"+c.nom+"</i></b></big><br/><small>Accedez aux convocations, resultats, informations et évenements de votre club</small>"));
        }
        if (mode.contentEquals("admin"))
        {
          btn.setText(Html.fromHtml("<big><b><i>"+c.nom+"</i></b></big><br/><small>Accedez à la gestion des catégories, équipes, joueurs, convocations, résultats, informations et evenements de votre club</small>"));
        }
        
        //////////////
        btn.setId(clublist.indexOf(c));
        //r.setButton(btn);
        layout.addView(btn);
        btn.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            Club c = clublist.get(v.getId());
            Intent intent = null;
            if (mode.contentEquals("admin"))
            {
              Global.setCurrentAdminClub(ctx, c);
              intent = new Intent(ctx, AdminClubActivity.class);  
            }
            if (mode.contentEquals("user"))
            {
              Global.setCurrentUserClub(ctx, c);
              intent = new Intent(ctx, UserListCategoryActivity.class);  
            }
            startActivity(intent);
          }
        });
      }
      clubChecked = new boolean[clubliststring.size()];
    }
    else
    {
      Intent intent = new Intent(ctx, MainActivity.class);
      startActivity(intent);
    }
  }
    



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.club, menu);
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
      //if user choose to add a new club/category, go back to main page
      case R.id.action_add:
        BothModeAddClubActivity.state = "AJOUT";
        if (mode.contentEquals("user"))
        {
          Intent intent = new Intent(ctx, BothModeAddClubActivity.class);
          intent.putExtra("mode", mode);
          startActivity(intent);  
        }
        if (mode.contentEquals("admin"))
        {
          Intent intent = new Intent(ctx, AdminWayToAddClubActivity.class);
          startActivity(intent);  
        } 

        return true;
      //if user choose to delete all records, first ask for a confirmation  
        
      case R.id.action_remove:
        saveClubChecked = new boolean[clubChecked.length];
        System.arraycopy(clubChecked, 0, saveClubChecked, 0, clubChecked.length);
        openClubDialog();
        return true;
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
  
  public void dialogBoxRemoveAll() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner de tous ces clubs?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
          //if user confirm the deletion, delete the 4 tables used for notification and connection
        if (mode.contentEquals("user"))
        {
          Global.ConnexionRemoveUserAllClub(ctx);
        }
        if (mode.contentEquals("admin"))
        {
          Global.ConnexionRemoveAdminAllClub(ctx);
        }
        finish();
        startActivity(getIntent());
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
  
  void openClubDialog()
  {
    String title = "";
    if (mode.contentEquals("user"))
    {
      title = "Liste des clubs à ne plus suivre";
    }
    if (mode.contentEquals("admin"))
    {
      title = "Liste des clubs à ne plus gérer";
    }
    final CharSequence[] clubitems = clubliststring.toArray(new CharSequence[clubliststring.size()]);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    //.setIcon(R.drawable.icon)
    alertDialogBuilder.setTitle(title);
    alertDialogBuilder.setPositiveButton("valider", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        Toast.makeText(getBaseContext(), "VALIDER", Toast.LENGTH_LONG).show();
        //gerer l'inscription ou desincription des cat dans DB
        for(int i=0;i<clubChecked.length;i++)
        {
          if (saveClubChecked[i] != clubChecked[i])
          {
            if (clubChecked[i])
            {
              if (mode.contentEquals("user"))
              {
                Global.ConnexionRemoveUserClub(ctx, clublist.get(i).id_club);
              }
              if (mode.contentEquals("admin"))
              {
                Global.ConnexionRemoveAdminClub(ctx, clublist.get(i).id_club);
              }
            }
          }
        }
        finish();
        startActivity(getIntent());
      }
    });
    alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        Toast.makeText(getBaseContext(), "Annulation", Toast.LENGTH_LONG).show();      
        System.arraycopy(saveClubChecked, 0, clubChecked, 0, clubChecked.length);
        finish();
        startActivity(getIntent());
      }
    });
    alertDialogBuilder.setMultiChoiceItems(clubitems, clubChecked, new DialogInterface.OnMultiChoiceClickListener() 
    {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) 
        {
          //Toast.makeText(getBaseContext(), joueuritems[which] + (isChecked ? "checked!" : "unchecked!"), Toast.LENGTH_SHORT).show();
          if (isChecked)
          {
            clubChecked[which] = true;  
          }
          else
          {
            clubChecked[which] = false;  
          }
        }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
}
