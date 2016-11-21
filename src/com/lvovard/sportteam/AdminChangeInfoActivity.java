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
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AdminChangeInfoActivity extends Activity
{

  String mode;
  protected Spinner spinner_cat;
  protected TextView textequipe;
  protected Spinner spinner_equipe;
  protected TextView textinfo;
  protected Spinner spinner_info;
  protected TextView textobjet;
  protected EditText txt_objet;
  protected TextView textdetail;
  protected EditText editTextdetail;
  protected Button btnDateInfo;
  protected Button btnHeureInfo;
  protected Button btnValider;
  protected Button btnAnnuler;
  
  ArrayList<String> id_info;
  
  private  ProgressDialog progressBar;
  private Calendar cal;
  private int day;
  private int month;
  private int year;
  
  List<Categorie> listcat;
  List<Info> listinfo;
  
  Context ctx = AdminChangeInfoActivity.this;
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
    setContentView(R.layout.activity_admin_change_info); 
    this.spinner_cat = (Spinner) findViewById(R.id.spinner_cat);
    this.textequipe = (TextView) findViewById(R.id.textequipe);
    this.spinner_equipe = (Spinner) findViewById(R.id.spinner_equipe);
    this.textinfo = (TextView) findViewById(R.id.textinfo);
    this.spinner_info = (Spinner) findViewById(R.id.spinner_info);
    this.textobjet = (TextView) findViewById(R.id.textobjet);
    this.txt_objet = (EditText) findViewById(R.id.txt_objet);
    this.textdetail = (TextView) findViewById(R.id.textdetail);
    this.editTextdetail = (EditText) findViewById(R.id.editTextdetail);
    this.btnDateInfo = (Button) findViewById(R.id.btnDateInfo);
    this.btnHeureInfo = (Button) findViewById(R.id.btnHeureInfo);
    this.btnValider = (Button) findViewById(R.id.btnValider);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    Intent i=getIntent();
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }
    
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_info_white_24dp);
    actionBar.setTitle("Gestion des informations / evenements");
    
    //remove some items according to the mode
    if (mode.contentEquals("add"))
    {
      actionBar.setSubtitle("Ajout");
      textinfo.setVisibility(View.GONE);
      spinner_info.setVisibility(View.GONE);
    }
    if (mode.contentEquals("modify"))
    {
      actionBar.setSubtitle("Modification");
    }
    if (mode.contentEquals("remove"))
    {
      actionBar.setSubtitle("Suppression");
      textobjet.setVisibility(View.GONE);
      txt_objet.setVisibility(View.GONE);
      textdetail.setVisibility(View.GONE);
      editTextdetail.setVisibility(View.GONE);
      btnDateInfo.setVisibility(View.GONE);
      btnHeureInfo.setVisibility(View.GONE);
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
          
        //populate nb teams according to the cat
        if (! cat.contentEquals("toutes les catégories"))
        {
          textequipe.setVisibility(View.VISIBLE);
          spinner_equipe.setVisibility(View.VISIBLE);
          List<String> teamlist = new  ArrayList<String>();
          teamlist.add("toutes les équipes");
          for (int i=1;i<=getNbTeam(cat);i++)
          {
            teamlist.add(String.valueOf(i));
          }
          ArrayAdapter<String> adapterteam = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, teamlist);
          adapterteam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          spinner_equipe.setAdapter(adapterteam);
        }
        else
        {
          textequipe.setVisibility(View.GONE);
          spinner_equipe.setVisibility(View.GONE);
          if(mode.contentEquals("modify") || mode.contentEquals("remove"))
          {
            progressBar.setMessage("recherche les informations / evenements pour toutes les catégories ...");
            progressBar.show();
            new GetInfoMain().execute("all-","all-all",adminclub.id_club);
          }
        }
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
        //if team changes and convoc is selected, populate the convocations according to the cat/team
        if(mode.contentEquals("modify") || mode.contentEquals("remove"))
        {
          progressBar.setMessage("recherche les informations / evenements pour "+cat+" "+equipe+" ...");
          progressBar.show();
          new GetInfoMain().execute(cat,cat+"-"+equipe,adminclub.id_club);
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //Called when a new item is selected in the cat Spinner
    spinner_info.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        setActDefaultValue();
        if (listinfo.size()>0)
        {
          Info inf = listinfo.get(pos);
          txt_objet.setText(inf.objet);
          editTextdetail.setText(inf.message.replace("<br/>","\n"));
          if (! inf.date_info.contains("auto"))
          {
            btnDateInfo.setText("date evenement : "+inf.date_info);
          }
          if (! inf.heure_info.contentEquals("empty"))
          {
            btnHeureInfo.setText("heure evenement : "+inf.heure_info);
          }
        }
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    //date match management
    btnDateInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        DateDialog();
      }
    });
    
    btnHeureInfo.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
              
              btnHeureInfo.setText(String.format("heure evenement : %02d:%02d", selectedHour, selectedMinute));
            }
        }, 0, 0,true);
        mTimePicker.setTitle("Choisissez l'heure de l'evenement");
        if (btnHeureInfo.getText().toString().contains(":"))
        {
          
          int heure = Integer.parseInt(btnHeureInfo.getText().toString().split(" : ")[1].split(":")[0]);
          int min = Integer.parseInt(btnHeureInfo.getText().toString().split(" : ")[1].split(":")[1]);
          mTimePicker.updateTime(heure,min);
        }
        
        mTimePicker.show();
      }
    });
    
    btnValider.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
        String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
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
        //check if one existing info has been selected
        if (mode.contentEquals("remove") || mode.contentEquals("modify"))
        {
          String inf = (String) spinner_info.getItemAtPosition(spinner_info.getSelectedItemPosition());
          if (inf.contentEquals("aucune information trouvée"))
          {
            Toast.makeText(ctx,"aucune information n'a été trouvée pour la catégorie "+cat, Toast.LENGTH_LONG).show();
            ((TextView)spinner_info.getChildAt(0)).setError("aucune information selectionnée");
            return;
          }
          else
          {
            ((TextView)spinner_info.getChildAt(0)).setError(null);
          }
        }

        String id_eq = null;
        if (cat.contentEquals("toutes les catégories"))
        {
          id_eq = "all-all";
        }
        else
        {
          if (equipe.contentEquals("toutes les équipes"))
          {
            id_eq = cat+"-all";
          }
          else
          {
            id_eq = cat+"-"+equipe;
          }
        }
        String obj = null;
        String msg = null;
        String date_info = null;
        String heure_info = "empty";

        if (mode.contentEquals("add") || mode.contentEquals("modify"))
        {    
          //check date info, if not provided take the today date + auto
          if (btnDateInfo.getText().toString().contains("date evenement :"))
          {
            date_info = btnDateInfo.getText().toString().split(" : ")[1];
          }
          else
          {
            date_info = getDateStringWithDay(0,0,0)+" auto";
          }
          if(! TextUtils.isEmpty(txt_objet.getText())) 
          {
            obj = txt_objet.getText().toString();
            txt_objet.setError(null);
          }
          else
          {
            txt_objet.setError("Entrez l'objet");
            txt_objet.requestFocus();
            return;    
          }
          if(! TextUtils.isEmpty(editTextdetail.getText())) 
          {
            msg = editTextdetail.getText().toString();
            editTextdetail.setError(null);
          }
          else
          {
            editTextdetail.setError("Entrez le message");
            editTextdetail.requestFocus();
            return;    
          }
          if (btnHeureInfo.getText().toString().contains("heure evenement"))
          {
            heure_info = btnHeureInfo.getText().toString().split(" : ")[1];
          }
          //user has set hourbut no date
          if (! heure_info.contentEquals("empty") && date_info.contains("auto"))
          {
            btnDateInfo.setError("Un horaire a été trouvé, vous devez entrer une date");
            btnDateInfo.requestFocus();  
            return;
          }
          else
          {
            btnDateInfo.setError(null);
          }

//          Log.i("myApp", "WILL CREATE INFO WITH:");
//          Log.i("myApp", "date_info:"+date_info);
//          Log.i("myApp", "heure_info:"+heure_info);
//          Log.i("myApp", "objet:"+obj);
//          Log.i("myApp", "msg:"+msg);
//          Log.i("myApp", "id_equipe:"+id_eq);
          Info inf = new Info(id_eq, obj, msg, "new", "", adminclub.id_club, "", date_info, heure_info);
          
          
          if (mode.contentEquals("modify"))
          {
            inf.id_info = id_info.get(spinner_info.getSelectedItemPosition()); 
            //Log.i("myApp", "id_info to modify:"+inf.id_info);
            inf.state = "modified";
            String infoload = "modification de l'information / evenement ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new updateInfoMain().execute(inf);
          }
          if (mode.contentEquals("add"))
          {
            String infoload = "creation de l'information / evenement ...";
            progressBar.setMessage(infoload);
            progressBar.show();   
            new insertInfoMain().execute(inf);  
          }
  
        }
        if (mode.contentEquals("remove"))
        { 
            String id_info_to_delete = id_info.get(spinner_info.getSelectedItemPosition());
            String selectedinfo = (String) spinner_info.getItemAtPosition(spinner_info.getSelectedItemPosition());
            dialogBoxRemoveInfo(id_info_to_delete, selectedinfo.split(";")[1]); 
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
    getMenuInflater().inflate(R.menu.admin_change_info, menu);
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
        Intent intenthome = new Intent(ctx, AdminInfoActivity.class);
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
      if (catlist.size() > 0)
      {
        catlist.add("toutes les catégories");  
      }
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
  
  private class GetInfoMain extends GetInfo
  {
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(List<Info> listinforesult) 
    {
      progressBar.dismiss();
      String cat = (String) spinner_cat.getItemAtPosition(spinner_cat.getSelectedItemPosition());
      String equipe = (String) spinner_equipe.getItemAtPosition(spinner_equipe.getSelectedItemPosition());
      String dest = null;
      if (cat.contentEquals("toutes les catégories"))
      {
        dest = "all-all";
      }
      else
      {
        if (equipe.contentEquals("toutes les équipes"))
        {
          dest = cat+"-all";
        }
        else
        {
          dest = cat+"-"+equipe;
        }
      }
      listinfo = new  ArrayList<Info>();;
      List<String> infostringlist = new  ArrayList<String>();   
      id_info = new  ArrayList<String>();
      for (Info inf:listinforesult)
      {
        if (inf.id_equipe.contentEquals(dest))
        {
          listinfo.add(inf);
          infostringlist.add(inf.date_info.replace("auto", "")+";"+inf.objet);
          id_info.add(inf.id_info);
        }
      }
      if (infostringlist.size() == 0)
      {
        infostringlist.add("aucune information trouvée");
        Toast.makeText(ctx,"aucune information n'a été trouvée. "+adminclub.nom+" ne contient pas d'information avec ces critères ou un problème réseau a été rencontré", Toast.LENGTH_LONG).show();
      }
      //doinbkg has sent the convoc result - put it in convoc spinner
      ArrayAdapter<String> adaptercat = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, infostringlist);
      adaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      spinner_info.setAdapter(adaptercat);
    }
  }
  
  private class insertInfoMain extends insertInfo
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
        Toast.makeText(ctx, "l'information / evenement a été ajouté avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création de l'information / evenement\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class updateInfoMain extends updateInfo
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
        Toast.makeText(ctx, "l'information / evenement a été modifié avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la modification de l'information / evenement\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class removeInfoMain extends removeInfo
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
        Toast.makeText(ctx, "l'information / evenement a été supprimé avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);   
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la suppression de l'information / evenement \n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
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
    txt_objet.setText("");    
    editTextdetail.setText("");
    btnDateInfo.setText("Clicker pour ajouter une date");
    btnHeureInfo.setText("Clicker pour ajouter une heure");
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
          btnDateInfo.setText("date evenement : "+daystring+" "+stringDayOfMonth+" "+monthstring+" "+year);

        }};

    DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
    dpDialog.setTitle("Choisissez la date de l'evenement");
    if (btnDateInfo.getText().toString().contains(" : "))
    {
      int jour = Integer.parseInt(btnDateInfo.getText().toString().split(" : ")[1].split(" ")[1]);
      SimpleDateFormat monthint = new SimpleDateFormat("MM");
      SimpleDateFormat monthstr = new SimpleDateFormat("MMMM");
      String m = null;
      try
      {
        m = monthint.format(monthstr.parse(btnDateInfo.getText().toString().split(" : ")[1].split(" ")[2]));
      } catch (ParseException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      int mois = Integer.parseInt(m)-1;
      int annee = Integer.parseInt(btnDateInfo.getText().toString().split(" : ")[1].split(" ")[3]);
      dpDialog.updateDate(annee, mois, jour); 
    }
    dpDialog.show();
  }
  
  public String getDateStringWithDay(int day,int month,int year)
  { 
    //set to current date
    Calendar calend = Calendar.getInstance();
    //calend.set(year, month, day);
    String daystring = calend.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    String dayofmonth = String.valueOf(calend.get(Calendar.DAY_OF_MONTH));
    String monthstring = calend.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    String yearstring = String.valueOf(calend.get(Calendar.YEAR));
    
    return daystring+" "+dayofmonth+" "+monthstring+" "+yearstring;
  }
  
  public void dialogBoxRemoveInfo(String id,String obj) 
  {
    final String id_inf = id;
    final String obj_inf = obj;
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setMessage("Etes vous sûr de vouloir supprimer l'information / évenement \""+obj_inf+"\"?");
    alertDialogBuilder.setPositiveButton("OUI",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("suppression de l'information / évenement ...");
        progressBar.show();   
        new removeInfoMain().execute(id_inf); 
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
