package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminChangePeopleActivity extends Activity
{
  String mode;
  protected TextView textcat;
  protected Spinner spinner_cat;
  protected TextView textrole;
  protected Spinner spinner_role;
  protected TextView textnom;
  protected EditText txt_nom;
  protected TextView textprenom;
  protected EditText txt_prenom;
  protected TextView textpeople;
  protected Spinner spinner_people;
  protected Button btnValider;
  protected Button btnAnnuler;
  private  ProgressDialog progressBar;
  List<Categorie> listcat;
  List<Person> listjoueur;
  List<Person> listdirigeant;
  List<Person> listjoueurcat;
  List<Person> listdirigeantcat;
  Context ctx = AdminChangePeopleActivity.this;
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
    adminclub = Global.getCurrentAdminClub(ctx);
    setContentView(R.layout.activity_admin_change_people);
    this.textcat = (TextView) findViewById(R.id.textcat);
    this.spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
    this.textrole = (TextView) findViewById(R.id.textrole);
    this.spinner_role = (Spinner) findViewById(R.id.spinner_role);
    this.textnom = (TextView) findViewById(R.id.textnom);
    this.txt_nom = (EditText) findViewById(R.id.txt_nom);
    this.textprenom = (TextView) findViewById(R.id.textprenom);
    this.txt_prenom = (EditText) findViewById(R.id.txt_prenom);
    this.textpeople = (TextView) findViewById(R.id.textpeople);
    this.spinner_people = (Spinner) findViewById(R.id.spinner_people);
    this.btnValider = (Button) findViewById(R.id.btnValider);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    
    listcat = new ArrayList<Categorie>();
    listjoueur = new ArrayList<Person>();
    listdirigeant = new ArrayList<Person>();
    listjoueurcat = new ArrayList<Person>();
    listdirigeantcat = new ArrayList<Person>();
    
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_person_white_24dp);
    actionBar.setTitle("Gestion des joueurs / dirigeants");
    
    if (mode.contentEquals("add"))
    {
      actionBar.setSubtitle("Ajout");
      textpeople.setVisibility(View.GONE);
      spinner_people.setVisibility(View.GONE);
      List<String> rolelist = new  ArrayList<String>();
      rolelist.add("joueur");
      rolelist.add("dirigeant");
      //doinbkg has sent the cat result - put it in cat spinner
      ArrayAdapter<String> adapterrole = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, rolelist);
      adapterrole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_role.setAdapter(adapterrole);

    }
    if (mode.contentEquals("remove"))
    {
      actionBar.setSubtitle("Suppression");
      textnom.setVisibility(View.GONE);
      txt_nom.setVisibility(View.GONE);
      textprenom.setVisibility(View.GONE);
      txt_prenom.setVisibility(View.GONE);
      new getPersonMain().execute(adminclub.id_club);
    }
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    
    progressBar.setMessage("recherche les catégories du club ...");
    progressBar.show();
    new GetCatMain().execute(adminclub.id_club);
    
    //Called when a new item is selected in the cat Spinner
    spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        if (mode.contentEquals("remove"))
        {
          List<String> emptylist = new ArrayList<String>();
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, emptylist);
          adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner_people.setAdapter(adapter);
          
          String cat = (String) spinner_cat.getItemAtPosition(pos);
          if (cat.contentEquals("aucune catégorie trouvée"))
          {
            return;
          }
          listjoueurcat = getJoueurList(getIdCat(cat, listcat),listjoueur);
          listdirigeantcat = getDirigeantList(getIdCat(cat, listcat), listdirigeant);
          List<String> rolelist = new  ArrayList<String>();
          if (listjoueurcat.size()>0)
          {
            rolelist.add("joueur");
          }
          if (listdirigeantcat.size()>0)
          {
            rolelist.add("dirigeant");
          }
          if (rolelist.size() == 0)
          {
            rolelist.add("aucun role trouvé");
          }
          //doinbkg has sent the cat result - put it in cat spinner
          ArrayAdapter<String> adapterrole = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, rolelist);
          adapterrole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner_role.setAdapter(adapterrole);
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        if (mode.contentEquals("remove"))
        {
          String role = (String) spinner_role.getItemAtPosition(pos);
          List<String> nomprenomlist = new  ArrayList<String>();
          if (role.contentEquals("joueur"))
          {
            for (Person p:listjoueurcat)
            {
              nomprenomlist.add(p.nom + " " + p.prenom);
            }
          }
          if (role.contentEquals("dirigeant"))
          {
            for (Person p:listdirigeantcat)
            {
              nomprenomlist.add(p.nom + " " + p.prenom);
            }
          } 
          if (nomprenomlist.size() == 0)
          {
            nomprenomlist.add("aucun joueur/dirigeant trouvé");
          }
          Collections.sort(nomprenomlist);
          //doinbkg has sent the cat result - put it in cat spinner
          ArrayAdapter<String> adapternomprenom = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, nomprenomlist);
          adapternomprenom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner_people.setAdapter(adapternomprenom);
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
        if (cat.contentEquals("aucune catégorie trouvée"))
        {
          Toast.makeText(ctx, "Aucune catégorie trouvée", Toast.LENGTH_SHORT).show();
          ((TextView)spinner_cat.getChildAt(0)).setError("Aucune catégorie trouvée");
          return;
        }
        else
        {
          ((TextView)spinner_cat.getChildAt(0)).setError(null);
        }
        String role = (String) spinner_role.getItemAtPosition(spinner_role.getSelectedItemPosition());
        if (mode.contentEquals("add"))
        {    
          String nom = txt_nom.getText().toString().toUpperCase();
          String prenom =capitalizeFirstname(txt_prenom.getText().toString());
          if (nom.isEmpty())
          {
            txt_nom.setError("Entrez un nom");
            Toast.makeText(getBaseContext(), "le champ nom n'est pas correct", Toast.LENGTH_LONG).show();
            return;
          }
          else
          {
            txt_nom.setError(null);
          }
          if (prenom.isEmpty())
          {
            txt_prenom.setError("Entrez un prénom");
            Toast.makeText(getBaseContext(), "le champ prénom n'est pas correct", Toast.LENGTH_LONG).show();
            return;
          }
          else
          {
            txt_prenom.setError(null);
          }
          progressBar.setMessage("ajout du "+role+" "+prenom+" "+nom+" dans la catégorie "+cat+" ...");
          progressBar.show();
          new insertPersonMain().execute(nom,prenom,role,getIdCat(cat, listcat),adminclub.id_club);       
        }
        if (mode.contentEquals("remove"))
        { 
          String nomprenom = (String) spinner_people.getItemAtPosition(spinner_people.getSelectedItemPosition());
          if (nomprenom.contentEquals("aucun joueur/dirigeant trouvé"))
          {
            Toast.makeText(ctx, "Aucun joueur/dirigeant à supprimer", Toast.LENGTH_SHORT).show();
            ((TextView)spinner_people.getChildAt(0)).setError("Aucun joueur/dirigeant à supprimer");
            return;
          }
          else
          {
            ((TextView)spinner_people.getChildAt(0)).setError(null);
          }
          String id_to_delete = null;
          if (role.contentEquals("dirigeant"))
          {
            id_to_delete = getIdPerson(nomprenom, listdirigeantcat);
          }
          if (role.contentEquals("joueur"))
          {
            id_to_delete = getIdPerson(nomprenom, listjoueurcat);
          }
          dialogBoxRemovePerson(id_to_delete, role,nomprenom,cat);
        }
 
      }
    });
    
    btnAnnuler.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Toast.makeText(ctx, "Annulation", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ctx, AdminPeopleActivity.class);
        startActivity(intent);
      }
    });
  }
    

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_change_people, menu);
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
        Intent intenthome = new Intent(ctx, AdminPeopleActivity.class);
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
      listcat = catlistresult;
      List<String> catlist = new  ArrayList<String>();
      for (Categorie c:catlistresult)
      {
        catlist.add(c.nom);
      }
      if (catlist.size() == 0)
      {
        catlist.add("aucune catégorie trouvée");
      }
      //doinbkg has sent the cat result - put it in cat spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, catlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_cat.setAdapter(adaptercat);
      
    }
  }
  

  
  private class insertPersonMain extends insertPerson
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
        Toast.makeText(ctx, "le joueur / dirigeant a été ajouté avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de l'ajout du joueur / dirigeant\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
      
    }
  }
  
  private class removePersonMain extends removePerson
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
        Toast.makeText(ctx, "le joueur / dirigeant a été supprimé avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la suppression du joueur / dirigeant\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
      
    }
  }
  
  
  private class getPersonMain extends getPerson
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Person> listpeople) 
    {
      progressBar.dismiss();
      listjoueur.clear();
      listdirigeant.clear();
      for (Person p:listpeople)
      {
        if (p.role.contentEquals("joueur"))
        {
          listjoueur.add(p);
        }
        if (p.role.contentEquals("dirigeant"))
        {
          listdirigeant.add(p);
        }
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
  
  private String getIdPerson(String nomprenom,List<Person> listp)
  {
    String id = null;
    for (Person p:listp)
    {
      if ( nomprenom.contentEquals(p.nom + " " + p.prenom))
      {
        return p.id_joueur; 
      }
    }
    return id;
  }
  
  private List<Person> getJoueurList(String id_cat,List<Person> listp)
  {
    List<Person> listjoueurbycat = new ArrayList<Person>();
    for (Person p:listp)
    {
      if ( p.id_cat.contentEquals(id_cat) && p.role.contentEquals("joueur"))
      {
        listjoueurbycat.add(p); 
      }
    }
    Collections.sort(listjoueurbycat, Person.PersonNameComparator);
    return listjoueurbycat;
  }
  
  private List<Person> getDirigeantList(String id_cat,List<Person> listp)
  {
    List<Person> listdirigeantbycat = new ArrayList<Person>();
    for (Person p:listp)
    {
      if ( p.id_cat.contentEquals(id_cat) && p.role.contentEquals("dirigeant"))
      {
        listdirigeantbycat.add(p); 
      }
    }
    Collections.sort(listdirigeantbycat, Person.PersonNameComparator);
    return listdirigeantbycat;
  }
  
  private String capitalizeFirstname(String string) 
  {
    char[] chars = string.toLowerCase().toCharArray();
    for (int i = 0; i < chars.length; i++) 
    {
      if (i==0)
      {
        chars[0] = Character.toUpperCase(chars[0]);
      }
      else
      {
        if (Character.isWhitespace(chars[i-1]) || chars[i-1]=='.' || chars[i-1]=='-')
        {
          chars[i] = Character.toUpperCase(chars[i]);
        }
      }
    }
    return String.valueOf(chars);
  }
  
  public void dialogBoxRemovePerson(String id, String role,String nomprenom,String cat) 
  {
    final String id_person = id;
    final String role_person = role;
    final String name = nomprenom;
    final String ctg = cat;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sûr de vouloir supprimer le "+role_person+" "+name+" de la catégorie "+cat+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("suppression du "+role_person+" "+name+" dans la catégorie "+ctg+" ...");
        progressBar.show();
        new removePersonMain().execute(id_person); 
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
