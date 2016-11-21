package com.lvovard.sportteam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminChangeResultatActivity extends Activity
{
  
  String mode;
  protected Spinner spinner_cat;
  protected Spinner spinner_equipe;
  protected CheckBox checkBoxconvoc;
  protected TextView textconvoc;
  protected Spinner spinner_convoc;
  protected TextView textresult;
  protected Spinner spinner_result;
  protected Button btnDateMatch;
  protected TextView textadversaire;
  protected EditText txt_adversaire;
  protected TextView textmatch;
  protected Spinner spinner_match;
  protected TextView textcompet;
  protected Spinner spinner_compet;
  protected TextView textscoreequipe;
  protected Spinner spinner_scoreequipe;
  protected TextView textscoreadversaire;
  protected Spinner spinner_scoreadversaire;
  protected TextView textdetail;
  protected EditText editTextdetail;
  protected Button btnValider;
  protected Button btnAnnuler;
  
  ArrayAdapter<String> adaptercomp;
  ArrayAdapter<String> adaptermatch;
  ArrayAdapter<String> adapterscore;
  
  ArrayList<String> id_result;
  
  private  ProgressDialog progressBar;
  private Calendar cal;
  private int day;
  private int month;
  private int year;
  
  List<Categorie> listcat;
  List<Convocation> listconv;
  List<Resultat> listresultat;
  
  Context ctx = AdminChangeResultatActivity.this;
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
    setContentView(R.layout.activity_admin_change_resultat);
    this.spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
    this.spinner_equipe = (Spinner) findViewById(R.id.spinner_equipe);
    this.checkBoxconvoc = (CheckBox) findViewById(R.id.checkBoxconvoc);
    this.textconvoc = (TextView) findViewById(R.id.textconvoc);
    this.spinner_convoc = (Spinner) findViewById(R.id.spinner_convoc);
    this.textresult = (TextView) findViewById(R.id.textresult);
    this.spinner_result = (Spinner) findViewById(R.id.spinner_result);
    this.btnDateMatch = (Button) findViewById(R.id.btnDateMatch);
    this.textadversaire = (TextView) findViewById(R.id.textadversaire);
    this.txt_adversaire = (EditText) findViewById(R.id.txt_adversaire);
    this.textmatch = (TextView) findViewById(R.id.textmatch);
    this.spinner_match = (Spinner) findViewById(R.id.spinner_match);
    this.textcompet = (TextView) findViewById(R.id.textcompet);
    this.spinner_compet = (Spinner) findViewById(R.id.spinner_compet);
    this.textscoreequipe = (TextView) findViewById(R.id.textscoreequipe);
    this.spinner_scoreequipe = (Spinner) findViewById(R.id.spinner_scoreequipe);
    this.textscoreadversaire = (TextView) findViewById(R.id.textscoreadversaire);
    this.spinner_scoreadversaire = (Spinner) findViewById(R.id.spinner_scoreadversaire);
    this.textdetail = (TextView) findViewById(R.id.textdetail);
    this.editTextdetail = (EditText) findViewById(R.id.editTextdetail);
    this.btnValider = (Button) findViewById(R.id.btnValider);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_looks_icon_two_black_24dp);
    actionBar.setTitle("Gestion des resultats");
    
    List<String> stringlist = new  ArrayList<String>();
    stringlist.add("championnat");
    stringlist.add("coupe");
    stringlist.add("amical");
    stringlist.add("tournoi");
    stringlist.add("autre");
    adaptercomp = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, stringlist);
    adaptercomp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_compet.setAdapter(adaptercomp);
    
    List<String> stringlist2 = new  ArrayList<String>();
    stringlist2.add("domicile");
    stringlist2.add("exterieur");
    adaptermatch = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, stringlist2);
    adaptermatch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_match.setAdapter(adaptermatch);
    
    List<String> listscore = new  ArrayList<String>();
    for (int sc=0;sc<31;sc++)
    {
      listscore.add(String.valueOf(sc));
    }

    adapterscore = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, listscore);
    adapterscore.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_scoreequipe.setAdapter(adapterscore);
    spinner_scoreadversaire.setAdapter(adapterscore);
    
    //remove some items according to the mode
    if (mode.contentEquals("add"))
    {
      actionBar.setSubtitle("Ajout");
      textresult.setVisibility(View.GONE);
      spinner_result.setVisibility(View.GONE);
      textconvoc.setVisibility(View.GONE);
      spinner_convoc.setVisibility(View.GONE);

    }
    if (mode.contentEquals("modify"))
    {
      actionBar.setSubtitle("Modification");
      textconvoc.setVisibility(View.GONE);
      checkBoxconvoc.setVisibility(View.GONE);
      spinner_convoc.setVisibility(View.GONE);
    }
    if (mode.contentEquals("remove"))
    {
      actionBar.setSubtitle("Suppression");
      
      textconvoc.setVisibility(View.GONE);
      checkBoxconvoc.setVisibility(View.GONE);
      spinner_convoc.setVisibility(View.GONE);
      btnDateMatch.setVisibility(View.GONE);
      textadversaire.setVisibility(View.GONE);
      txt_adversaire.setVisibility(View.GONE);
      textmatch.setVisibility(View.GONE);
      spinner_match.setVisibility(View.GONE);
      textcompet.setVisibility(View.GONE);
      spinner_compet.setVisibility(View.GONE);
      textscoreequipe.setVisibility(View.GONE);
      spinner_scoreequipe.setVisibility(View.GONE);
      textscoreadversaire.setVisibility(View.GONE);
      spinner_scoreadversaire.setVisibility(View.GONE);
      textdetail.setVisibility(View.GONE);
      editTextdetail.setVisibility(View.GONE);
      
    }
    
    cal = Calendar.getInstance();
    day = cal.get(Calendar.DAY_OF_MONTH);
    month = cal.get(Calendar.MONTH);
    year = cal.get(Calendar.YEAR);
    
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
        setActDefaultValue();
        //populate nb teams according to the cat
        String cat = (String) spinner_cat.getItemAtPosition(pos);
        if (cat.contentEquals("aucune catégorie trouvée") || getNbTeam(cat) == 0)
        {
          List<String> teamlist = new  ArrayList<String>();
          teamlist.add("aucune équipe trouvée");
          ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
          adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner_equipe.setAdapter(adapterteam);
          return;
        }
        List<String> teamlist = new  ArrayList<String>();
        for (int i=1;i<=getNbTeam(cat);i++)
        {
          teamlist.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
        adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_equipe.setAdapter(adapterteam);
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the equipe Spinner
    spinner_equipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
        if (equipe.contentEquals("aucune équipe tropuvée"))
        {
          return;
        }
        //if team changes and convoc is selected, populate the convocations according to the cat/team
        if(checkBoxconvoc.isChecked())
        {
          progressBar.setMessage("recherche les convocations pour "+cat+" "+equipe+" ...");
          progressBar.show();
          new GetConvocMain().execute(cat,cat+" "+equipe,adminclub.id_club);
        }
        if(mode.contentEquals("modify") || mode.contentEquals("remove"))
        {
          progressBar.setMessage("recherche les resultats pour "+cat+" "+equipe+" ...");
          progressBar.show();
          new GetResultatMain().execute(cat,cat+" "+equipe,adminclub.id_club);
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_convoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();
        String conv = (String) spinner_convoc.getItemAtPosition(spinner_convoc.getSelectedItemPosition());
        if (conv.contentEquals("aucune convocation trouvée"))
        {
          return;
        }
        Convocation c = listconv.get(pos);
        btnDateMatch.setText("date match : "+c.date);
        txt_adversaire.setText(c.adversaire);
        spinner_match.setSelection(adaptermatch.getPosition(c.lieu));
        spinner_compet.setSelection(adaptercomp.getPosition(c.competition));
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_result.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();
        String result = (String) spinner_result.getItemAtPosition(spinner_result.getSelectedItemPosition());
        if (result.contentEquals("aucun résultat trouvé"))
        {
          return;
        }
        Resultat r = listresultat.get(pos);
        btnDateMatch.setText("date match : "+r.date_match);
        txt_adversaire.setText(r.adversaire);
        spinner_match.setSelection(adaptermatch.getPosition(r.lieu));
        spinner_compet.setSelection(adaptercomp.getPosition(r.competition));
        spinner_scoreequipe.setSelection(adapterscore.getPosition(r.score_equipe));
        spinner_scoreadversaire.setSelection(adapterscore.getPosition(r.score_adversaire));
        editTextdetail.setText(r.detail.replace("<br/>", "\n"));
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    checkBoxconvoc.setOnCheckedChangeListener(new OnCheckedChangeListener() 
    {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
      {
        //user decide to put the result of one convocation
        //populate items according to this convoc
        if (isChecked)
        {
          String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
          String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
          textconvoc.setVisibility(View.VISIBLE);
          spinner_convoc.setVisibility(View.VISIBLE);
          progressBar.setMessage("recherche les convocations pour "+cat+" "+equipe+" ...");
          progressBar.show();
          new GetConvocMain().execute(cat,cat+" "+equipe,adminclub.id_club);
        }
        else
        {
          textconvoc.setVisibility(View.GONE);
          spinner_convoc.setVisibility(View.GONE);
        }
      }
    });
    
    //date match management
    btnDateMatch.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        DateDialog();
      }
    });
    
    btnValider.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
        String date_match = null;
        String adversaire = null;
        String competition = (String) spinner_compet.getItemAtPosition(spinner_compet.getSelectedItemPosition());
        String lieu = (String) spinner_match.getItemAtPosition(spinner_match.getSelectedItemPosition());
        String id_equipe = cat+"-"+equipe;
        String score_equipe = (String) spinner_scoreequipe.getItemAtPosition(spinner_scoreequipe.getSelectedItemPosition());
        String score_adversaire = (String) spinner_scoreadversaire.getItemAtPosition(spinner_scoreadversaire.getSelectedItemPosition());
        String detail = editTextdetail.getText().toString();
        if (cat.contentEquals("aucune catégorie trouvée"))
        {
          Toast.makeText(ctx,"aucune catégorie trouvée. "+adminclub.nom+" ne contient aucune catégorie ou un problème réseau a été rencontré", Toast.LENGTH_LONG).show();
          ((TextView)spinner_cat.getChildAt(0)).setError("aucune catégorie trouvée");
          return;
        }
        else
        {
          ((TextView)spinner_cat.getChildAt(0)).setError(null);
        }
        if (getNbTeam(cat) == 0)
        {
          Toast.makeText(ctx,"aucune équipe n'a été trouvée pour la catégorie "+cat, Toast.LENGTH_LONG).show();
          ((TextView)spinner_equipe.getChildAt(0)).setError("aucune équipe trouvée");
          return;
        }
        else
        {
          ((TextView)spinner_equipe.getChildAt(0)).setError(null);
        }
        if (mode.contentEquals("modify") || mode.contentEquals("remove"))
        {
          String res = (String) spinner_result.getItemAtPosition(spinner_result.getSelectedItemPosition());
          if (res.contentEquals("aucun résultat trouvé"))
          {
            Toast.makeText(ctx,"aucun résultat n'a été trouvé pour "+id_equipe, Toast.LENGTH_LONG).show();
            ((TextView)spinner_result.getChildAt(0)).setError("aucun résultat trouvé");
            return;
          }
          else
          {
            ((TextView)spinner_result.getChildAt(0)).setError(null);
          }
        }
        if (mode.contentEquals("add") || mode.contentEquals("modify"))
        {    
          //check all inputs - no id convoc required, it will be created later
          //check date match
          if (btnDateMatch.getText().toString().contains("date match :"))
          {
            date_match = btnDateMatch.getText().toString().split(" : ")[1];
            btnDateMatch.setError(null);
          }
          else
          {
            btnDateMatch.setError("selectionner une date");
            Toast.makeText(getBaseContext(), "le champ date du match n'est pas correct", Toast.LENGTH_LONG).show();
            return;
          }
          if(! TextUtils.isEmpty(txt_adversaire.getText())) 
          {
            adversaire = txt_adversaire.getText().toString().toUpperCase();
            txt_adversaire.setError(null);
          }
          else
          {
            txt_adversaire.setError("Entrez l'adversaire");
            return;    
          }

//          Log.i("myApp", "WILL CREATE RESULT WITH:");
//          Log.i("myApp", "date_match:"+date_match);
//          Log.i("myApp", "adversaire:"+adversaire);
//          Log.i("myApp", "score equipe:"+score_equipe);
//          Log.i("myApp", "score adversaire:"+score_adversaire);
//          Log.i("myApp", "competition:"+competition);
//          Log.i("myApp", "lieu:"+lieu);
//          Log.i("myApp", "id_equipe:"+id_equipe);
          
          Resultat r = new Resultat(id_equipe, date_match, adversaire, score_equipe, score_adversaire, "new", competition, lieu, "", adminclub.id_club, "", detail);
          
          
          if (mode.contentEquals("modify"))
          {
            r.id_resultat = id_result.get(spinner_result.getSelectedItemPosition()); 
            r.state = "modified";
            String infoload = "modification du resultat pour "+id_equipe+" ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new updateResultatMain().execute(r);
          }
          if (mode.contentEquals("add"))
          {
            String infoload = "creation du resultat pour "+id_equipe+" ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new insertResultatMain().execute(r);  
          }
  
        }
        if (mode.contentEquals("remove"))
        { 
          String id_r = id_result.get(spinner_result.getSelectedItemPosition());
          String result = (String) spinner_result.getItemAtPosition(spinner_result.getSelectedItemPosition());
          dialogBoxRemoveResult(id_r, result.split(";")[0],result.split(";")[1],result.split(";")[2],id_equipe); 
 
        }
 
      }
    });
    
    btnAnnuler.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Toast.makeText(ctx, "Annulation", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_change_resultat, menu);
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
        Intent intenthome = new Intent(ctx, AdminResultatActivity.class);
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
        Toast.makeText(ctx,"aucune catégorie n'a été trouvée. "+adminclub.nom+" ne contient aucune catégorie ou un problème réseau a été rencontré", Toast.LENGTH_LONG).show();
      }
      //doinbkg has sent the cat result - put it in cat spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, catlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_cat.setAdapter(adaptercat);
    }
  }
  
  private class GetConvocMain extends GetConvoc
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Convocation> catconvresult) 
    {
      progressBar.dismiss();
      listconv = catconvresult;
      List<String> convstringlist = new  ArrayList<String>();   
      for (Convocation c:catconvresult)
      {
        convstringlist.add(c.date+";"+c.adversaire+";"+c.lieu);
      }
      if (convstringlist.size() == 0)
      {
        convstringlist.add("aucune convocation trouvée");
      }
      //doinbkg has sent the convoc result - put it in convoc spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, convstringlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_convoc.setAdapter(adaptercat);
    }
  }
  
  private class GetResultatMain extends GetResultat
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Resultat> listresult) 
    {
      progressBar.dismiss();
      listresultat = listresult;
      List<String> resultstringlist = new  ArrayList<String>();   
      id_result = new  ArrayList<String>();
      for (Resultat r:listresult)
      {
        resultstringlist.add(r.date_match+";"+r.adversaire+";"+r.lieu);
        id_result.add(r.id_resultat);
      }
      if (resultstringlist.size() == 0)
      {
        resultstringlist.add("aucun résultat trouvé");
      }
      //doinbkg has sent the convoc result - put it in convoc spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, resultstringlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_result.setAdapter(adaptercat);
    }
  }
  
  
  private class insertResultatMain extends insertResultat
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
        Toast.makeText(ctx, "le resultat a été ajouté avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création du resultat\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class updateResultatMain extends updateResultat
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
        Toast.makeText(ctx, "le resultat a été modifié avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la modification du resultat\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class removeResultatMain extends removeResultat
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
        Toast.makeText(ctx, "le resultat a été supprimé avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la suppression du resultat\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private int getNbTeam(String cat)
  {
    for(Categorie c:listcat)
    {
      if (c.nom.contentEquals(cat))
      {
        return Integer.parseInt(c.nb_equipe);
      }
    }
    return 0;
  }
  
  private void setActDefaultValue()
  {
    txt_adversaire.setText("");
    spinner_compet.setSelection(0);
    spinner_match.setSelection(0);
    spinner_scoreequipe.setSelection(0);
    spinner_scoreadversaire.setSelection(0);
    btnDateMatch.setText("Clicker pour choisir la date du match");
    editTextdetail.setText("");
  }
  
  public void DateDialog()
  {

    OnDateSetListener listener=new OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
        {
          cal.set(year, monthOfYear, dayOfMonth);
          String daystring = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
          String stringDayOfMonth = String.valueOf(dayOfMonth) ;
          if (dayOfMonth < 10)
          {
            stringDayOfMonth = "0"+stringDayOfMonth;
          }
          SimpleDateFormat monthParse = new SimpleDateFormat("MM");
          SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
          String monthstring = null;
          try
          {
            monthstring = monthDisplay.format(monthParse.parse(String.valueOf(monthOfYear+1)));
          } catch (ParseException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          btnDateMatch.setText("date match : "+daystring+" "+stringDayOfMonth+" "+monthstring+" "+year);

        }};

    DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
    dpDialog.setTitle("Choisissez la date du match");
    dpDialog.show();
  }
  
  public void dialogBoxRemoveResult(String id, String date,String adversaire,String lieu, String id_e) 
  {
    final String id_result= id;
    final String date_match = date;
    final String adv = adversaire;
    final String domext = lieu; 
    final String id_equipe = id_e;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sûr de vouloir supprimer le résultat du "+date_match+" contre "+adv+" ("+domext+") pour les "+id_equipe+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("suppression du resultat pour "+id_equipe+" ...");
        progressBar.show();   
        new removeResultatMain().execute(id_result); 
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
