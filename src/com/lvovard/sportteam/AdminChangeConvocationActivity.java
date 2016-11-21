package com.lvovard.sportteam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AdminChangeConvocationActivity extends Activity
{
  String mode;
  protected Spinner spinner_cat;
  protected Spinner spinner_equipe;
  protected TextView textconvoc;
  protected Spinner spinner_convoc;
  protected Button btnDateMatch;
  protected Button btnHeureMatch;
  protected Button btnHeureRdv;
  protected TextView textadversaire;
  protected EditText txt_adversaire;
  protected TextView textmatch;
  protected Spinner spinner_match;
  protected TextView textcompet;
  protected Spinner spinner_compet;
  
  protected TextView textlieumatch;
  protected TextView textlieurdv;
  protected EditText txt_lieumatch;
  protected EditText txt_lieurdv;

  
  protected Button btnJoueur;
  protected Button btnDirigeant;
  
  List<Categorie> listcat;
  List<Convocation> listconv;
  
  List<Person> listjoueurforcat;
  List<Person> listdirigeantforcat;
  
  List<String> stringlistjoueurforcat;
  List<String> stringlistdirigeantforcat;
  
  
  boolean[] joueurChecked;
  boolean[] dirigeantChecked;
  boolean[] savedirigeantChecked;
  boolean[] savejoueurChecked;
  
  List<Person> listpersonclub;
  
  ArrayList<String> id_convoc;
  

  

  

  protected Button btnValider;
  protected Button btnAnnuler;
  
  private  ProgressDialog progressBar;
  private Calendar cal;
  private int day;
  private int month;
  private int year;
  
  ArrayAdapter<String> adaptercomp;
  ArrayAdapter<String> adaptermatch;
  
  Club adminclub;
  
  Context ctx = AdminChangeConvocationActivity.this;

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
    setContentView(R.layout.activity_admin_change_convocation);
    adminclub = Global.getCurrentAdminClub(ctx);

//    List<Categorie> listcat = new ArrayList<Categorie>();
//    List<Convocation> listconv = new ArrayList<Convocation>();
//    
//    List<Person> listjoueurforcat = new ArrayList<Person>();
//    List<Person> listdirigeantforcat = new ArrayList<Person>();
//    
//    List<String> stringlistjoueurforcat = new ArrayList<String>();
//    List<String> stringlistdirigeantforcat = new ArrayList<String>();
//    
//    List<Person> listpersonclub;
    
    this.spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
    this.spinner_equipe = (Spinner) findViewById(R.id.spinner_equipe);
    this.textconvoc = (TextView) findViewById(R.id.textconvoc);
    this.spinner_convoc = (Spinner) findViewById(R.id.spinner_convoc);
    this.btnDateMatch = (Button) findViewById(R.id.btnDateMatch);
    this.textadversaire = (TextView) findViewById(R.id.textadversaire);
    this.txt_adversaire = (EditText) findViewById(R.id.txt_adversaire);
    this.textmatch = (TextView) findViewById(R.id.textmatch);
    this.spinner_match = (Spinner) findViewById(R.id.spinner_match);
    this.textcompet = (TextView) findViewById(R.id.textcompet);
    this.spinner_compet = (Spinner) findViewById(R.id.spinner_compet);
    
    this.btnJoueur = (Button) findViewById(R.id.btnJoueur);
    this.btnDirigeant = (Button) findViewById(R.id.btnDirigeant);

    
    this.btnHeureMatch = (Button) findViewById(R.id.btnHeureMatch);
    this.btnHeureRdv = (Button) findViewById(R.id.btnHeureRdv);

    this.btnValider = (Button) findViewById(R.id.btnValider);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    
    this.textlieumatch = (TextView) findViewById(R.id.textlieumatch);
    this.textlieurdv = (TextView) findViewById(R.id.textlieurdv);
    this.txt_lieumatch = (EditText) findViewById(R.id.txt_lieumatch);
    this.txt_lieurdv = (EditText) findViewById(R.id.txt_lieurdv);
    
    
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_event_white_24dp);
    actionBar.setTitle("Gestion des convocations");
    
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

    txt_lieumatch.setText(adminclub.nom);
    
    adaptermatch = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, stringlist2);
    adaptermatch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_match.setAdapter(adaptermatch);
    
    if (mode.contentEquals("add"))
    {
      actionBar.setSubtitle("Ajout");
      textconvoc.setVisibility(View.GONE);
      spinner_convoc.setVisibility(View.GONE);
    }
    if (mode.contentEquals("modify"))
    {
      actionBar.setSubtitle("Modification");
    }
    if (mode.contentEquals("remove"))
    {
      actionBar.setSubtitle("Suppression");
      btnDateMatch.setVisibility(View.GONE);
      btnHeureMatch.setVisibility(View.GONE);
      btnHeureRdv.setVisibility(View.GONE);
      textadversaire.setVisibility(View.GONE);
      txt_adversaire.setVisibility(View.GONE);
      textcompet.setVisibility(View.GONE);
      spinner_compet.setVisibility(View.GONE);
      textmatch.setVisibility(View.GONE);
      spinner_match.setVisibility(View.GONE);
      btnJoueur.setVisibility(View.GONE);
      btnDirigeant.setVisibility(View.GONE);
      textlieumatch.setVisibility(View.GONE);
      textlieurdv.setVisibility(View.GONE);
      txt_lieumatch.setVisibility(View.GONE);
      txt_lieurdv.setVisibility(View.GONE);
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
    
    if (! mode.contentEquals("remove"))
    {
      progressBar.setMessage("recherche les joueurs du club ...");
      progressBar.show();
      new GetJoueurMain().execute(adminclub.id_club);  
    }
    
    
    progressBar.setMessage("recherche les catégories du club ...");
    progressBar.show();
    new GetCatMain().execute(adminclub.id_club);

    
    //Called when a new item is selected in the cat Spinner
    spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();

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
        //doinbkg has sent the cat result - put it in cat spinner
        ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
        adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_equipe.setAdapter(adapterteam);
        if (!mode.contentEquals("remove"))
        {
          listjoueurforcat = getJoueurList(getIdCat(cat,listcat),listpersonclub);
          listdirigeantforcat = getDirigeantList(getIdCat(cat,listcat),listpersonclub);
          joueurChecked = new boolean[listjoueurforcat.size()];
          dirigeantChecked = new boolean[listdirigeantforcat.size()];
          stringlistjoueurforcat = new ArrayList<String>();
          stringlistdirigeantforcat = new ArrayList<String>();
          for(Person p:listjoueurforcat)
          {
            stringlistjoueurforcat.add(p.nom + "," + p.prenom);
          }
          for(Person p:listdirigeantforcat)
          {
            stringlistdirigeantforcat.add(p.nom + "," + p.prenom);
          }
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_equipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
        if (equipe.contentEquals("aucune équipe trouvée"))
        { 
          return;
        }
        if (mode.contentEquals("modify") || mode.contentEquals("remove"))
        {
          progressBar.setMessage("recherche les convocations pour "+cat+" "+equipe+" ...");
          progressBar.show();
          new GetConvocMain().execute(cat,cat+" "+equipe,adminclub.id_club);
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
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
        String conv = (String) spinner_convoc.getItemAtPosition(spinner_convoc.getSelectedItemPosition());
        if (conv.contentEquals("aucune convocation trouvée"))
        {
          return;
        }
        if (mode.contentEquals("modify"))
        {
          for (int i=0;i<joueurChecked.length;i++)
          {
            joueurChecked[i] = false;
          }
          for (int i=0;i<dirigeantChecked.length;i++)
          {
            dirigeantChecked[i] = false;
          }
          Convocation c = returnConvoc(conv.split(";")[0],conv.split(";")[1],conv.split(";")[2],cat+"-"+equipe);
          btnDateMatch.setText("date match : "+c.date);
          txt_adversaire.setText(c.adversaire);
          txt_lieumatch.setText(c.lieu_match);
          txt_lieurdv.setText(c.lieu_rdv);
          spinner_match.setSelection(adaptermatch.getPosition(c.lieu));
          spinner_compet.setSelection(adaptercomp.getPosition(c.competition));
          
          btnHeureMatch.setText("heure du match : "+c.heure_match);
          btnHeureRdv.setText("heure du rdv : "+c.heure_rdv);
          for (String j:c.listejoueurs)
          {
            int idx = getIdx(j,stringlistjoueurforcat);
            if (idx != -1)
            {
              joueurChecked[idx] = true;
            }
            else
            {
              Toast.makeText(getBaseContext(), "le joueur "+j+" n'a pas été trouvé pour la catégorie "+cat, Toast.LENGTH_LONG).show();
            }
          }
          btnJoueur.setText(String.valueOf(c.listejoueurs.size()) + " joueurs sélectionnés");
          for (String d:c.listedirigeants)
          {
            int idx = getIdx(d,stringlistdirigeantforcat);
            if (idx != -1)
            {
              dirigeantChecked[idx] = true;
            }
            else
            {
              Toast.makeText(getBaseContext(), "le dirigeant "+d+" n'a pas été trouvé pour la catégorie "+cat, Toast.LENGTH_LONG).show();
            }
          }
          btnDirigeant.setText(String.valueOf(c.listedirigeants.size()) + " dirigeants sélectionnés");
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_match.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        String lieu = (String) spinner_match.getItemAtPosition(spinner_match.getSelectedItemPosition());
        if (lieu.contentEquals("domicile"))
        {
          txt_lieumatch.setText(adminclub.nom);
        }
        else
        {
          txt_lieumatch.setText(txt_adversaire.getText().toString().toUpperCase());
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    TextWatcher watcher= new TextWatcher() 
    {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Do something or nothing.                
      }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
          //Do something or nothing
      }
      @Override
      public void afterTextChanged(Editable s)
      {
        String lieu = (String) spinner_match.getItemAtPosition(spinner_match.getSelectedItemPosition());
        if (lieu.contentEquals("exterieur"))
        {
          txt_lieumatch.setText(s.toString().toUpperCase());
        }
        
        
      }
  };

  txt_adversaire.addTextChangedListener(watcher);
    
    
    //date match management
    btnDateMatch.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        DateDialog();
      }
    });
    
    btnHeureMatch.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
              
              btnHeureMatch.setText(String.format("heure du match : %02d:%02d", selectedHour, selectedMinute));
            }
        }, 0, 0,true);
        mTimePicker.setTitle("Choisissez l'heure du match");
        mTimePicker.show();
      }
    });
    
    btnHeureRdv.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
              
              btnHeureRdv.setText(String.format("heure du rdv : %02d:%02d", selectedHour, selectedMinute));
            }
        }, 0, 0,true);
        mTimePicker.setTitle("Choisissez l'heure de rendez-vous de l'équipe");
        mTimePicker.show();
      }
    });
    
    
    btnJoueur.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          savejoueurChecked = new boolean[joueurChecked.length];
          System.arraycopy(joueurChecked, 0, savejoueurChecked, 0, joueurChecked.length);
          openJoueurDialog();
          
        }
    });
    
    btnDirigeant.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        savedirigeantChecked = new boolean[dirigeantChecked.length];
        System.arraycopy(dirigeantChecked, 0, savedirigeantChecked, 0, dirigeantChecked.length);
        openDirigeantDialog();
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
        String heure_match = null;
        String heure_rdv = null;
        String adversaire = null;
        String strjoueurs = null;
        String strdirigeants = null;
        String competition = (String) spinner_compet.getItemAtPosition(spinner_compet.getSelectedItemPosition());
        String lieu = (String) spinner_match.getItemAtPosition(spinner_match.getSelectedItemPosition());
        String id_equipe = cat+"-"+equipe;
        String lieumatch = null;
        String lieurdv = null;
        if (cat.contentEquals("aucune catégorie trouvée"))
        {
          Toast.makeText(ctx,"aucune catégorie trouvée. "+adminclub.nom+" ne contient aucune catégorie ou un problème réseau a été rencontré", Toast.LENGTH_LONG).show();
          ((TextView)spinner_cat.getChildAt(0)).setError("aucune catégorie disponible");
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
          String conv = (String) spinner_convoc.getItemAtPosition(spinner_convoc.getSelectedItemPosition());
          if (conv.contentEquals("aucune convocation trouvée"))
          {
            Toast.makeText(ctx,"aucune convocation n'a été trouvée pour "+id_equipe, Toast.LENGTH_LONG).show();
            ((TextView)spinner_convoc.getChildAt(0)).setError("aucune convocation trouvée");
            return;
          }
          else
          {
            ((TextView)spinner_convoc.getChildAt(0)).setError(null);
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
          if (btnHeureMatch.getText().toString().contains("heure du match :"))
          {
            heure_match = btnHeureMatch.getText().toString().split(" : ")[1];
            btnHeureMatch.setError(null);
          }
          else
          {
            btnHeureMatch.setError("selectionner l'heure du match");
            Toast.makeText(getBaseContext(), "le champ heure du match n'est pas correct", Toast.LENGTH_LONG).show();  
            return;
          }
          if (btnHeureRdv.getText().toString().contains("heure du rdv :"))
          {
            heure_rdv = btnHeureRdv.getText().toString().split(" : ")[1];
            btnHeureRdv.setError(null);
          }
          else
          {
            btnHeureRdv.setError("selectionner l'heure du rdv");
            Toast.makeText(getBaseContext(), "le champ heure du rdv n'est pas correct", Toast.LENGTH_LONG).show(); 
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
          if(! TextUtils.isEmpty(txt_lieumatch.getText())) 
          {
            lieumatch = txt_lieumatch.getText().toString();
            txt_lieumatch.setError(null);
          }
          else
          {
            txt_lieumatch.setError("Entrez le lieu du match");
            return;    
          }
          if(! TextUtils.isEmpty(txt_lieurdv.getText())) 
          {
            lieurdv = txt_lieurdv.getText().toString();
            txt_lieurdv.setError(null);
          }
          else
          {
            txt_lieurdv.setError("Entrez le lieu du rdv");
            return;    
          }
          if (! btnJoueur.getText().toString().contains("sélectionnés"))
          {
            btnJoueur.setError("sélectionnez les joueurs");
            Toast.makeText(getBaseContext(), "aucun joueur sélectionné", Toast.LENGTH_LONG).show();
            return;
          }
          else
          {
            btnJoueur.setError(null);
            strjoueurs = "";
            for (int i=0;i<joueurChecked.length;i++)
            {
              if (joueurChecked[i])
              {
                strjoueurs = strjoueurs + stringlistjoueurforcat.get(i) + ";" ;
              }
            }
          }
          if (! btnDirigeant.getText().toString().contains("sélectionnés"))
          {
            btnDirigeant.setError("sélectionnez les dirigeants");
            Toast.makeText(getBaseContext(), "aucun dirigeant sélectionné", Toast.LENGTH_LONG).show();
            return;
          }
          else
          {
            btnDirigeant.setError(null);
            strdirigeants = "";
            for (int i=0;i<dirigeantChecked.length;i++)
            {
              if (dirigeantChecked[i])
              {
                strdirigeants = strdirigeants + stringlistdirigeantforcat.get(i) + ";" ;
              }
            }
          }
//          Log.i("myApp", "WILL CREATE CONVOC WITH:");
//          Log.i("myApp", "date_match:"+date_match);
//          Log.i("myApp", "heure_match:"+heure_match);
//          Log.i("myApp", "heure_rdv:"+heure_rdv);
//          Log.i("myApp", "adversaire:"+adversaire);
//          Log.i("myApp", "strjoueurs:"+strjoueurs);
//          Log.i("myApp", "strdirigeants:"+strdirigeants);
//          Log.i("myApp", "competition:"+competition);
//          Log.i("myApp", "lieu:"+lieu);
//          Log.i("myApp", "id_equipe:"+id_equipe);
//          Log.i("myApp", "lieu match:"+lieumatch);
//          Log.i("myApp", "lieu rdv:"+lieurdv);
          
          Convocation c = new Convocation(adversaire,date_match,heure_rdv,heure_match,lieurdv,lieumatch,Arrays.asList(strjoueurs.split(";")),Arrays.asList(strdirigeants.split(";")),"",id_equipe,"new",lieu,competition,"",adminclub.id_club);
          
          if (mode.contentEquals("modify"))
          {
            c.id_convoc = id_convoc.get(spinner_convoc.getSelectedItemPosition()); 
            c.state = "modified";
            String infoload = "modification de la convocation pour "+id_equipe+" ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new updateConvocMain().execute(c);
          }
          if (mode.contentEquals("add"))
          {
            String infoload = "creation de la convocation pour "+id_equipe+" ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new insertConvocMain().execute(c);  
          }
  
        }
        if (mode.contentEquals("remove"))
        { 
          String id_c = id_convoc.get(spinner_convoc.getSelectedItemPosition());
          String conv = (String) spinner_convoc.getItemAtPosition(spinner_convoc.getSelectedItemPosition());
          dialogBoxRemoveConvoc(id_c,id_equipe,conv.split(";")[1],conv.split(";")[0]);  
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
    getMenuInflater().inflate(R.menu.admin_change_convocation, menu);
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
        Intent intenthome = new Intent(ctx, AdminConvocationActivity.class);
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
  
  public void DateDialog(){

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
  
  private class insertConvocMain extends insertConvoc
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
        Toast.makeText(ctx, "la convocation a été ajoutée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création de la convocation\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class updateConvocMain extends updateConvoc
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
        Toast.makeText(ctx, "la convocation a été modifiée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la modification de la convocation\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class removeConvocMain extends removeConvoc
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
        Toast.makeText(ctx, "la convocation a été supprimée avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la suppression de la convocation\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
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
      id_convoc = new  ArrayList<String>();
      for (Convocation c:catconvresult)
      {
        convstringlist.add(c.date+";"+c.adversaire+";"+c.lieu);
        id_convoc.add(c.id_convoc);
      }
      if (convstringlist.size() == 0)
      {
        convstringlist.add("aucune convocation trouvée");
        Toast.makeText(getBaseContext(), "aucune convocation trouvée", Toast.LENGTH_LONG).show();
      }
      //doinbkg has sent the cat result - put it in cat spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, convstringlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_convoc.setAdapter(adaptercat);
    }
  }
  
  
  private class GetJoueurMain extends getPerson
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Person> joueurlistresult) 
    {
      progressBar.dismiss();
      listpersonclub = joueurlistresult;
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
  
  private Convocation returnConvoc(String date,String adv,String lieu,String eq)
  {
    for(Convocation c:listconv)
    {
      if (c.date.contentEquals(date) && c.adversaire.contentEquals(adv) && c.lieu.contentEquals(lieu))
      {
        return c;
      }
    }
    return null;
  }
  

  void openJoueurDialog()
  {
    final CharSequence[] joueuritems = stringlistjoueurforcat.toArray(new CharSequence[stringlistjoueurforcat.size()]);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    //.setIcon(R.drawable.icon)
    alertDialogBuilder.setTitle("Liste des joueurs");
    alertDialogBuilder.setPositiveButton("valider", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        int selected = getNbJoueurSelected();
//        for (int i = 0; i < stringlistjoueurforcat.size(); i++) 
//        {
//          if (joueurChecked[i]) 
//          {
//              Toast.makeText(getBaseContext(), stringlistjoueurforcat.get(i) + " checked!", Toast.LENGTH_LONG).show();
//          }

//        }
        Toast.makeText(getBaseContext(), String.valueOf(selected) + " joueurs sélectionnés", Toast.LENGTH_LONG).show();
        if (selected == 0)
        {
          btnJoueur.setText("Clicker pour selectionner les joueurs");
        }
        else
        {
          btnJoueur.setText(String.valueOf(selected) + " joueurs sélectionnés");
        }
      }
    });
    alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        Toast.makeText(getBaseContext(), "Annulation", Toast.LENGTH_LONG).show();
        
        System.arraycopy(savejoueurChecked, 0, joueurChecked, 0, joueurChecked.length);
        int selected = getNbJoueurSelected();
        if (selected == 0)
        {
          btnJoueur.setText("Clicker pour selectionner les joueurs");
        }
        else
        {
          btnJoueur.setText(String.valueOf(selected) + " joueurs sélectionnés");
        }
      }
    });
    alertDialogBuilder.setMultiChoiceItems(joueuritems, joueurChecked, new DialogInterface.OnMultiChoiceClickListener() 
    {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) 
        {
          //Toast.makeText(getBaseContext(), joueuritems[which] + (isChecked ? "checked!" : "unchecked!"), Toast.LENGTH_SHORT).show();
          if (isChecked)
          {
            joueurChecked[which] = true;  
          }
          else
          {
            joueurChecked[which] = false;  
          }
          
        }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
  
  void openDirigeantDialog()
  {
    final CharSequence[] dirigeantitems = stringlistdirigeantforcat.toArray(new CharSequence[stringlistdirigeantforcat.size()]);
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    //.setIcon(R.drawable.icon)
    alertDialogBuilder.setTitle("Liste des dirigeants");
    alertDialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
//        for (int i = 0; i < stringlistdirigeantforcat.size(); i++) 
//        {
//          if (dirigeantChecked[i]) 
//          {
//              Toast.makeText(getBaseContext(), stringlistdirigeantforcat.get(i) + " checked!", Toast.LENGTH_LONG).show();
//          }
//        }
        int selected = getNbDirigeantSelected();
        Toast.makeText(getBaseContext(), String.valueOf(selected) + " dirigeants sélectionnés", Toast.LENGTH_LONG).show();
        if (selected == 0)
        {
          btnDirigeant.setText("Clicker pour selectionner les dirigeants");
        }
        else
        {
          btnDirigeant.setText(String.valueOf(selected) + " dirigeants sélectionnés");
        }
      }
    });
    alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() 
    {

      @Override
      public void onClick(DialogInterface dialog, int which) 
      {
        Toast.makeText(getBaseContext(), "Annulation", Toast.LENGTH_LONG).show();
        
        System.arraycopy(savedirigeantChecked, 0, dirigeantChecked, 0, dirigeantChecked.length);
        int selected = getNbDirigeantSelected();
        if (selected == 0)
        {
          btnDirigeant.setText("Clicker pour selectionner les dirigeants");
        }
        else
        {
          btnDirigeant.setText(String.valueOf(selected) + " dirigeants sélectionnés");
        }
      }
    });
    alertDialogBuilder.setMultiChoiceItems(dirigeantitems, dirigeantChecked, new DialogInterface.OnMultiChoiceClickListener() 
    {

        @Override
        public void onClick(DialogInterface dialog, int which, boolean isChecked) 
        {
          //Toast.makeText(getBaseContext(), dirigeantitems[which] + (isChecked ? "checked!" : "unchecked!"), Toast.LENGTH_SHORT).show();
          if (isChecked)
          {
            dirigeantChecked[which] = true;  
          }
          else
          {
            dirigeantChecked[which] = false;  
          }
        }
    });
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
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
  
  private int getNbJoueurSelected()
  {
    int nbj = 0;
    for(int i=0;i<joueurChecked.length;i++)
    {
      if (joueurChecked[i])nbj++;
    }
    return nbj;
  }
  
  private int getNbDirigeantSelected()
  {
    int nbd = 0;
    for(int i=0;i<dirigeantChecked.length;i++)
    {
      if (dirigeantChecked[i])nbd++;
    }
    return nbd;
  }
  
  private void setActDefaultValue()
  {
    txt_adversaire.setText("");
    spinner_compet.setSelection(0);
    spinner_match.setSelection(0);
    btnDateMatch.setText("Clicker pour choisir la date du match");
    btnHeureMatch.setText("Clicker pour choisir l'heure du match");
    btnHeureRdv.setText("Clicker pour choisir l'heure du rdv");
    btnJoueur.setText("Clicker pour selectionner les joueurs");
    btnDirigeant.setText("Clicker pour selectionner les dirigeants");
    txt_lieurdv.setText("");
  }
  
  private int getIdx(String nomprenom,List<String> strlist)
  {
    int idx = 0;
    for(String str:strlist)
    {
      if (nomprenom.contentEquals(str))
      {
        return idx;
      }
      idx++;
    }
    return -1;
  }
  
  public void dialogBoxRemoveConvoc(String idcat, String id_eq,String adv, String date) 
  {
    final String id_cat = idcat;
    final String id_equipe = id_eq;
    final String adversaire = adv;
    final String datematch = date;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sur de vouloir supprimer la convocation du "+datematch+" des "+id_equipe+" contre "+adversaire+"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("suppression de la convocation pour "+id_equipe+" ...");
        progressBar.show();   
        new removeConvocMain().execute(id_cat);
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
