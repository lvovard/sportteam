package com.lvovard.sportteam;



import java.util.ArrayList;
import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UserListCategoryActivity extends Activity
{
  
  List<Categorie> listcatasked;
  List<Categorie> listcatclub;
  List<String> catlist;
  boolean[] catChecked;
  boolean[] saveCatChecked;
  private  ProgressDialog progressBar;
  Context ctx = UserListCategoryActivity.this;
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
    setContentView(R.layout.activity_category);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    //define the bkg picture to use
    if (userclub.sport.equals("basketball"))
    {
      Global.setCurrentBkgPicture(ctx,R.drawable.terrainbasket);
      Global.setSportPicture(ctx,R.drawable.ballonbasket);
    }
    if (userclub.sport.equals("football"))
    {
      Global.setCurrentBkgPicture(ctx,R.drawable.terrainfootball);
      Global.setSportPicture(ctx,R.drawable.ballonfootball);;
    }
    if (userclub.sport.equals("handball"))
    {
      Global.setCurrentBkgPicture(ctx,R.drawable.terrainhandball);
      Global.setSportPicture(ctx,R.drawable.ballonhandball);
    }
    if (userclub.sport.equals("rugby"))
    {
      Global.setCurrentBkgPicture(ctx,R.drawable.terrainrugby);
      Global.setSportPicture(ctx,R.drawable.ballonrugby);
    }
    if (userclub.sport.equals("volleyball"))
    {
      Global.setCurrentBkgPicture(ctx,R.drawable.terrainvolley);
      Global.setSportPicture(ctx,R.drawable.ballonvolley);
    }
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));
    
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    //enable back action
    
    //get data coming from notif to add new cat

    Intent i=getIntent();
    
    if (! TextUtils.isEmpty(i.getStringExtra("state"))) 
    {
      if (i.getStringExtra("state").contentEquals("ADD_CAT"))
      {
        if ( Global.ConnexionAddUserCat(ctx, i.getStringExtra("clubid"), i.getStringExtra("cat") ) )
        {
          Toast.makeText(this, "catégorie "+i.getStringExtra("cat")+" a été ajoutée avec succès ", Toast.LENGTH_LONG).show();
        }
        else
        {
          Toast.makeText(this, "problème lors de l'ajout de la catégorie "+i.getStringExtra("cat"), Toast.LENGTH_LONG).show();
        }
      }
    }
    
    
    listcatasked = Global.ConnexionGetCat(ctx, userclub.id_club);
    for (Categorie c:listcatasked)
    {
      Button btn = new Button(this);
      //btn.setText(c.nom);
      ///////////////
      Drawable drawablesport = null;

      if (Build.VERSION.SDK_INT >= 22)
      {
        drawablesport = getResources().getDrawable(R.drawable.ic_group_black_24dp,getApplicationContext().getTheme());
      }
      else
      {
        drawablesport = getResources().getDrawable(R.drawable.ic_group_black_24dp);
      }

      drawablesport.setBounds(0, 0, (int)(drawablesport.getIntrinsicWidth()*1.5),(int)(drawablesport.getIntrinsicHeight()*1.5));
      ScaleDrawable sdsport = new ScaleDrawable(drawablesport, 0, 1, 1);
      btn.setCompoundDrawables(sdsport.getDrawable(), null, null, null); 

      btn.setText(Html.fromHtml("<big><b><i>"+c.nom+"</i></b></big><br/><small>Accedez aux convocations, résultats, informations et evenements de la catégorie "+c.nom+"</small>"));
      //////////////
      
      btn.setId(listcatasked.indexOf(c));
      layout.addView(btn);
      btn.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Global.setCurrentCat(ctx,listcatasked.get(v.getId()).nom);
          Intent intent = new Intent(ctx, UserListEquipeActivity.class);
          startActivity(intent);
        }
      });
    }
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_group_white_24dp);
    actionBar.setTitle(userclub.nom);
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
      
      case R.id.action_change:
        progressBar.setMessage("recherche les catégories du club ...");
        progressBar.show();
        new GetCatMain().execute(userclub.id_club);

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
      
      case R.id.action_home:
        Intent intent = new Intent(ctx, MainActivity.class);
        startActivity(intent);  
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
    alertDialogBuilder.setMessage("Etes vous sur de vouloir vous desabonner du club "+userclub.nom+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        Global.ConnexionRemoveUserClub(ctx,userclub.id_club);
        finish();
        Intent intent = new Intent(ctx, BothModeListClubActivity.class);
        intent.putExtra("mode", "user");
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
  
  private class GetCatMain extends GetCat
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Categorie> catlistresult) 
    {
      progressBar.dismiss();
      listcatclub = catlistresult;
      catlist = new  ArrayList<String>();
      for (Categorie c:catlistresult)
      {
        catlist.add(c.nom);
      }
      if (catlist.size() == 0)
      {
        Toast.makeText(ctx,"aucune catégorie n'a été trouvée. "+userclub.nom+" ne contient aucune catégorie ou un problème réseau a été rencontré", Toast.LENGTH_LONG).show();
        return;
      }
      else
      {
        catChecked = new boolean[listcatclub.size()];
      }
      for (Categorie c:listcatasked)
      {
        int idx = getIdx(c.nom,listcatclub);
        if (idx != -1)
        {
          catChecked[idx] = true;
        }
        else
        {
          //cat does not exist anymore - make some cleanup
          Toast.makeText(getBaseContext(), "la catégorie "+c.nom+" n'existe plus et a été supprimée de votre liste", Toast.LENGTH_LONG).show();
          Global.ConnexionRemoveUserCat(ctx, userclub.id_club, c.nom);
        }
      }
      saveCatChecked = new boolean[catChecked.length];
      System.arraycopy(catChecked, 0, saveCatChecked, 0, catChecked.length);
      openCatDialog();
    }
  }
  
  void openCatDialog()
  {
    final CharSequence[] catitems = catlist.toArray(new CharSequence[catlist.size()]);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    //.setIcon(R.drawable.icon)
    alertDialogBuilder.setTitle("Liste des catégories");
    alertDialogBuilder.setPositiveButton("valider", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        Toast.makeText(getBaseContext(), "VALIDER", Toast.LENGTH_LONG).show();
        Global.ConnexionPrintDataBase(ctx);
        //gerer l'inscription ou desincription des cat dans DB
        for(int i=0;i<catChecked.length;i++)
        {
          if (saveCatChecked[i] != catChecked[i])
          {
            if (catChecked[i])
            {
              Global.ConnexionAddUserCat(ctx, userclub.id_club, listcatclub.get(i).nom);
            }
            else
            {
              Global.ConnexionRemoveUserCat(ctx, userclub.id_club, listcatclub.get(i).nom);
            }
          }
        }
        Global.ConnexionPrintDataBase(ctx);
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
        System.arraycopy(saveCatChecked, 0, catChecked, 0, catChecked.length);
        finish();
        startActivity(getIntent());
      }
    });
    alertDialogBuilder.setMultiChoiceItems(catitems, catChecked, new DialogInterface.OnMultiChoiceClickListener() 
    {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) 
        {
          //Toast.makeText(getBaseContext(), joueuritems[which] + (isChecked ? "checked!" : "unchecked!"), Toast.LENGTH_SHORT).show();
          if (isChecked)
          {
            catChecked[which] = true;  
          }
          else
          {
            catChecked[which] = false;  
          }
        }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
  
  
  private int getIdx(String nom,List<Categorie> list)
  {
    int idx = 0;
    for(Categorie c:list)
    {
      if (nom.contentEquals(c.nom))
      {
        return idx;
      }
      idx++;
    }
    return -1;
  }
}
