package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminChangeCatActivity extends Activity
{

  String mode;
  protected TextView textcat;
  protected Spinner spinner_cat;
  protected TextView textnbteam;
  protected Spinner spinner_equipe;
  protected Button btnValider;
  protected Button btnAnnuler;
  
  protected List<Categorie> catlisttoremove;
  protected List<Categorie> catlisttomodify;
  
  protected List<String> catlistsport;
  protected List<String> catlistexisting;
  protected List<String> catlisttoadd;
  
  private  ProgressDialog progressBar;
  Context ctx = AdminChangeCatActivity.this;
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
  
  @Override
  protected void onResume() 
  {
    super.onResume();
    setContentView(R.layout.activity_admin_add_cat);
    
    adminclub = Global.getCurrentAdminClub(ctx);
    
    this.textcat = (TextView) findViewById(R.id.textcat);
    this.spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
    this.textnbteam = (TextView) findViewById(R.id.textnbteam);
    this.spinner_equipe = (Spinner) findViewById(R.id.spinner_equipe);
    this.btnValider = (Button) findViewById(R.id.btnValider);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_group_white_24dp);
    actionBar.setTitle("Gestion des catégories / équipes");
    
    actionBar.setSubtitle("Ajout");
    textcat.setText("Choisissez la catégorie à ajouter");
    if (mode.contentEquals("modify"))
    {
      actionBar.setSubtitle("Modification");  
      textcat.setText("Choisissez la catégorie à modifier");
    }
    if (mode.contentEquals("remove"))
    {
      actionBar.setSubtitle("Suppression");  
      textcat.setText("Choisissez la catégorie à supprimer");
      textnbteam.setVisibility(View.GONE);
      spinner_equipe.setVisibility(View.GONE);
    }
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    

    if (mode.contentEquals("add"))
    {
      progressBar.setMessage("recherche des catégories disponibles pour "+adminclub.sport+"...");
      progressBar.show();
      new GetCatBySportMain().execute(adminclub.sport);
    }
    if (mode.contentEquals("remove") || mode.contentEquals("modify"))
    {
      progressBar.setMessage("recherche des catégories existantes du club "+adminclub.nom+"...");
      progressBar.show();
      new GetCatMain().execute(adminclub.id_club);
    }

    
    
    //Called when a new item is selected in the sport Spinner
    spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        String cat = (String) spinner_cat.getItemAtPosition(pos);
        //clean all spinners
        List<String> teamlist = new ArrayList<String>();
        for (int i=0;i<11;i++)
        {
          teamlist.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
        adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_equipe.setAdapter(adapterteam);
        spinner_equipe.setSelection(0);

        //if modify, set the current nb team value
        if (mode.contentEquals("modify"))
        {
          String nb_current_team = getNbTeam(cat,catlisttomodify);
          int spinnerPosition = adapterteam.getPosition(nb_current_team);
          spinner_equipe.setSelection(spinnerPosition);
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    btnValider.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        if(cat.contains("aucune catégorie disponible"))
        {
          if (mode.contentEquals("add"))
          {
            Toast.makeText(ctx,"aucune catégorie ne peut être ajoutée. "+adminclub.nom+" contient déjà toutes les catégories disponibles pour le sport "+adminclub.sport, Toast.LENGTH_LONG).show();
          }
          else
          {
            Toast.makeText(ctx,"aucune catégorie trouvée pour "+adminclub.nom, Toast.LENGTH_LONG).show();
          }
          ((TextView)spinner_cat.getChildAt(0)).setError("aucune catégorie disponible");
          return;
        }
        else
        {
          ((TextView)spinner_cat.getChildAt(0)).setError(null);
        }
        if (mode.contentEquals("add"))
        {
          String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
          progressBar.setMessage("création de "+equipe+" équipes "+cat+" ...");
          progressBar.show();
          new insertCatMain().execute(adminclub.id_club,cat,equipe);       
        }
        if (mode.contentEquals("remove"))
        {
          dialogBoxRemoveCat(getIdCat(cat,catlisttoremove), cat);   
        }
        if (mode.contentEquals("modify"))
        {
          String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
          if (equipe.contentEquals(getNbTeam(cat,catlisttomodify)))
          {
            Toast.makeText(ctx,"le nombre d'équipe n'a pas changé", Toast.LENGTH_SHORT).show();
            ((TextView)spinner_equipe.getChildAt(0)).setError("le nombre d'équipe n'a pas changé");
          }
          else
          {
            ((TextView)spinner_equipe.getChildAt(0)).setError(null);
            progressBar.setMessage("modification de la catégorie "+cat+" avec "+equipe+" équipes ...");
            progressBar.show();
            new modifyCatMain().execute(getIdCat(cat,catlisttomodify),equipe);  
          }
        }
      }
    });
    
    btnAnnuler.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Toast.makeText(ctx, "Annulation", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ctx, AdminCatActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_add_cat, menu);
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
        Intent intenthome = new Intent(ctx, AdminCatActivity.class);
        startActivity(intenthome);  
      return true;
      
      case R.id.action_home:
        Intent intentaction_home = new Intent(ctx, MainActivity.class);
        startActivity(intentaction_home);  
      return true;
      

      default:
      return super.onOptionsItemSelected(item);
    }
  }
  
  private class GetCatBySportMain extends GetCatBySport 
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<String> catlist) 
    {
      progressBar.dismiss();
      
      //catch the available cat for this sport
      catlistsport = new ArrayList<String>(catlist);
      
      progressBar.setMessage("recherche des catégories existantes du club "+adminclub.nom+"...");
      progressBar.show();
      new GetCatMain().execute(adminclub.id_club);
      //doinbkg has sent the cat result - put it in sport spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, catlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_cat.setAdapter(adaptercat);
      
//      List<String> teamlist = new ArrayList<String>();
//      for (int i=0;i<11;i++)
//      {
//        teamlist.add(String.valueOf(i));
//      }
//      ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
//      adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//      spinner_equipe.setAdapter(adapterteam);
//      spinner_equipe.setSelection(0);
    }
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
      if (mode.contentEquals("remove"))
      {
        catlisttoremove = catlistresult;
      }
      if (mode.contentEquals("modify"))
      {
        catlisttomodify = catlistresult;
      }
      List<String> catlist = new  ArrayList<String>();
      for (Categorie c:catlistresult)
      {
        if (mode.contentEquals("add"))
        {
          if (catlistsport.contains(c.nom))
          {
            catlistsport.remove(catlistsport.indexOf(c.nom));
          }
        }
        else
        {
          catlist.add(c.nom);
        }
      }
      if (mode.contentEquals("add"))
      {
        catlist = catlistsport;
      }
      if (catlist.size() == 0)
      { 
        catlist.add("aucune catégorie disponible");
        textnbteam.setVisibility(View.GONE);
        spinner_equipe.setVisibility(View.GONE);
        if (mode.contentEquals("add"))
        {
          Toast.makeText(ctx,"aucune catégorie ne peut être ajoutée. "+adminclub.nom+" contient déjà toutes les catégories disponibles pour le sport "+adminclub.sport, Toast.LENGTH_LONG).show();
        }
        if (mode.contentEquals("remove"))
        {
          Toast.makeText(ctx,"aucune catégorie ne peut être supprimée. "+adminclub.nom+" ne contient aucune catégorie", Toast.LENGTH_LONG).show();  
        }
        if (mode.contentEquals("modify"))
        {
          Toast.makeText(ctx,"aucune catégorie ne peut être modifiée. "+adminclub.nom+" ne contient aucune catégorie", Toast.LENGTH_LONG).show();
        }
      }
      else
      {
        if (! mode.contentEquals("remove"))
        {
          textnbteam.setVisibility(View.VISIBLE);
          spinner_equipe.setVisibility(View.VISIBLE);
        }
      }
      //doinbkg has sent the cat result - put it in cat spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, catlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_cat.setAdapter(adaptercat);
      
    }
  }
  
  
  
  private class insertCatMain extends insertCat 
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(Boolean result) 
    {
      progressBar.dismiss();
      if (result)
      {
        Toast.makeText(ctx, "la catégorie a été créée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création de la catégorie\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }

    }
  }
  
  private class removeCatMain extends removeCat 
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(Boolean result) 
    {
      progressBar.dismiss();
      if (result)
      {
        Toast.makeText(ctx, "la catégorie a été supprimée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création de lma catégorie\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }

    }
  }
  
  private class modifyCatMain extends modifyCat 
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(Boolean result) 
    {
      progressBar.dismiss();
      if (result)
      {
        Toast.makeText(ctx, "la catégorie a été modifiée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création de lma catégorie\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }

    }
  }
  
  private String getIdCat(String catname,List<Categorie> listcat)
  {
    String id = null;
    for (Categorie c:listcat)
    {
      if ( c.nom.contentEquals(catname))
      {
        return c.id_cat; 
      }
    }
    return id;
  }
  
  private String getNbTeam(String catname,List<Categorie> listcat)
  {
    String nbteam = null;
    for (Categorie c:listcat)
    {
      if ( c.nom.contentEquals(catname))
      {
        return c.nb_equipe; 
      }
    }
    return nbteam;
  }
  
  public void dialogBoxRemoveCat(String id, String cat) 
  {
    final String id_cat = id;
    final String catname = cat;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sûr de vouloir supprimer la catégorie "+catname+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("suppression de la catégorie "+catname+" ...");
        progressBar.show();
        new removeCatMain().execute(id_cat);
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
