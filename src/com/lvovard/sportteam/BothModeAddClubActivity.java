package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author lavovard
 * Main activity - launcher
 *
 */
public class BothModeAddClubActivity extends Activity {
	
	
	/**
	 * elements of main activity
	 *
	 */
	protected Spinner spinner_sport ;
	protected Spinner spinner_dep ;
	protected Spinner spinner_club ;
	protected Spinner spinner_cat ;
	protected Button btnConnexion;
	protected Button btnAnnuler;
	protected EditText edittext_pwd;
	protected TextView textpwd;
	protected CheckBox checkBox1;
	protected TextView textcat;
	
	String caller = "nocaller";
	
  String sport;
  String departement;
  String club;
  String cat;
  String password;
  
  String mode; //admin or user
  //used for sql result (club, club id and pwd)
  JSONArray jArrayClub = null;
  List<String> catlisttorecord = null;
  private  ProgressDialog progressBar;
  
  //state machine - START is the default one
	static String state = "START";
	
	Context ctx = BothModeAddClubActivity.this;
	
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
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("SportTeam");
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);

    
    //info are coming with notif to update password
    Intent i=getIntent();
    
    if (! TextUtils.isEmpty(i.getStringExtra("club"))) {
      club = i.getStringExtra("club");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("dep"))) {
      departement = i.getStringExtra("dep");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("sport"))) {
      sport = i.getStringExtra("sport");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("cat"))) {
      cat = i.getStringExtra("cat");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("state"))) {
      state = i.getStringExtra("state");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("password"))) {
      password = i.getStringExtra("password");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("caller"))) {
      caller = i.getStringExtra("caller");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("mode"))) {
      mode = i.getStringExtra("mode");
    }


    
    setContentView(R.layout.activity_user_add_club); 
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setBackgroundResource(R.drawable.gazon);
    
    spinner_sport = (Spinner) findViewById(R.id.sport_spinner);
    spinner_dep = (Spinner) findViewById(R.id.departement_spinner);
    spinner_club = (Spinner) findViewById(R.id.club_spinner);
    spinner_cat = (Spinner) findViewById(R.id.cat_spinner);
    btnConnexion = (Button) findViewById(R.id.btnConnexion);
    btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    edittext_pwd = (EditText) findViewById(R.id.txtPassword);
    textpwd = (TextView) findViewById(R.id.textpwd);
    checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
    textcat = (TextView) findViewById(R.id.textcat);
    if (mode.contentEquals("admin"))
    {
      spinner_cat.setVisibility(View.GONE);
      textcat.setVisibility(View.GONE);
      textpwd.setText("Saisissez le mot de passe admin de votre club");
      actionBar.setSubtitle("Gerer un club");
      actionBar.setIcon(R.drawable.ic_settings_white_24dp);
    }
    else
    {
      textpwd.setText("Saisissez le mot de passe utilisateur de votre club");
      actionBar.setSubtitle("Suivre un club");
      actionBar.setIcon(R.drawable.ic_visibility_white_24dp);
    }

    addListenerOnButton();
    

    
    
    
    //sql request to get all the available sports
    //doinbkg is mandatory for such request
      progressBar.setMessage("recherche des sports ...");
      progressBar.show();
      new GetSportMain().execute();

    //Called when a new item is selected in the sport Spinner
    spinner_sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        sport = (String) spinner_sport.getItemAtPosition(pos);
        //clean all spinners
        List<String> emptylist = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, emptylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_dep.setAdapter(adapter);
        spinner_club.setAdapter(adapter);
        spinner_cat.setAdapter(adapter);
        //sql request to get all the available dep according to the selected sport
        progressBar.setMessage("recherche des departements ...");
        progressBar.show();
        new GetDepMain().execute(sport);
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    }); 
    
    //Called when a new item is selected in the dep Spinner
    spinner_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        departement = (String) spinner_dep.getItemAtPosition(pos);
        List<String> emptylist = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, emptylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //clean other spinners
        spinner_club.setAdapter(adapter);
        spinner_cat.setAdapter(adapter);
        //sql request to get all the available clubs according to the selected sport and dep
        progressBar.setMessage("recherche des clubs ...");
        progressBar.show();
        new GetClubMain().execute(sport,departement);
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    }); 
    
    //Called when a new item is selected in the club Spinner
    spinner_club.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        club = (String) spinner_club.getItemAtPosition(pos);
        List<String> emptylist = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, emptylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //clean other spinners
        spinner_cat.setAdapter(adapter);
        //sql request to get all the available cat according to the selected club
        progressBar.setMessage("recherche des categories ...");
        progressBar.show();
        new GetCatMain().execute(getIdClub(club));
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    }); 
    
    //Called when a new item is selected in the cat Spinner
    spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
    {
      public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) 
      {
        cat = (String) spinner_cat.getItemAtPosition(pos);
      }

      public void onNothingSelected(AdapterView<?> parent) 
      {
        // Do nothing, just another required interface callback
      }
    });
    
    checkBox1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) 
      {
        if (((CheckBox) v).isChecked()) 
        {
          edittext_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else
        {
          edittext_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

      }
    });
    
    
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.user_add_club, menu);
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
	
	public void addListenerOnButton() 
	{
	  btnConnexion.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			  password = edittext_pwd.getText().toString();
			  //check password validity
			  if (mode.contentEquals("user"))
			  {
	         //if user wants to record all cat - just remove unused proposal from the cat list
          if (cat == null)
          {
            return;
          }
          if (cat.contains("Toutes les catégories..."))
          {
            catlisttorecord.remove(0);
          }
          //if user wants to record only one cat
          else
          {
            catlisttorecord.clear();
            catlisttorecord.add(cat);
          }
          if (state.contentEquals("UPDATE_PWD"))
          {
              catlisttorecord.clear();
              catlisttorecord = new ArrayList<String>(Arrays.asList(cat.split(";")));;
          }
  			  if (password.contentEquals(getUserPassword(club)))
  			  {
  			    //add club in connection db
  			    Global.ConnexionAddUserClub(ctx, sport, departement, club, getIdClub(club), "", password);
            GetLastInfoDateMain taskinfo = new GetLastInfoDateMain(sport,club,getIdClub(club));
            taskinfo.execute(getIdClub(club),"nodate");
            GetLastResultatDateMain taskresultat = new GetLastResultatDateMain(sport,club,getIdClub(club));
            taskresultat.execute(getIdClub(club),"nodate");
            GetLastConvocationDateMain taskconvocation = new GetLastConvocationDateMain(sport,club,getIdClub(club));
            taskconvocation.execute(getIdClub(club),"nodate");
            GetLastCatDateMain taskcat = new GetLastCatDateMain(departement,sport,club,getIdClub(club));
            taskcat.execute(getIdClub(club),"nodate");
  			    
  			    //for each cat to record
  			    //put it in the connection file with some checks...
  			    for (String categorie:catlisttorecord)
  			    {
  			      if (Global.ConnexionUserCatExist(ctx, getIdClub(club), categorie))
  			      {
  			        Toast.makeText(ctx,sport+"-"+departement+"-"+club+"-"+categorie+" existe déjà", Toast.LENGTH_SHORT).show();  
  			        continue;
  			      }
  			      else
  			      {
  			        Global.ConnexionAddUserCatWithPwd(ctx, getIdClub(club), categorie,password);
  			        Toast.makeText(ctx,sport+"-"+departement+"-"+club+"-"+categorie+" a été ajoutée", Toast.LENGTH_SHORT).show();
  			      }
  			    }
  			    Global.ConnexionPrintDataBase(ctx);
            Intent intent = new Intent(ctx, BothModeListClubActivity.class);
            intent.putExtra("mode", "user");
            startActivity(intent);
  			  }
  			  else
  			  {
  				  Toast.makeText(ctx,"mauvais mot de passe utilisateur", Toast.LENGTH_SHORT).show();
  			  }
			  }
	      if (mode.contentEquals("admin"))
	      {
	        if (password.contentEquals(getAdminPassword(club)))
	        {
	          //add club in connection db
	          Global.ConnexionAddAdminClub(ctx, sport, departement, club, getIdClub(club), password);
	          Global.ConnexionPrintDataBase(ctx);
	          Intent intent = new Intent(ctx, BothModeListClubActivity.class);
	          intent.putExtra("mode", "admin");
	          startActivity(intent);
	        }
	        else
	        {
	          Toast.makeText(ctx,"mauvais mot de passe admin", Toast.LENGTH_SHORT).show();
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
	      Intent intent = new Intent(ctx, BothModeListClubActivity.class);
	      intent.putExtra("mode", "user");
	      if (caller.contentEquals("Main"))
	      {
	        intent = new Intent(ctx, MainActivity.class);
	      }
	      startActivity(intent);
	    }
	  });
	}

	
	 private class GetSportMain extends GetSport 
	 {
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<String> sportlist) 
     {
       progressBar.dismiss();
       //doinbkg has sent the sportlist result - put it in sport spinner
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, sportlist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_sport.setAdapter(adapter);
       if ( state.contentEquals("UPDATE_PWD") || state.contentEquals("ADD_CAT") )
       {
         spinner_sport.setSelection(adapter.getPosition(sport));
       }
     }
   }
	 
   private class GetDepMain extends GetDep
   {
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<String> deplist) 
     {
       progressBar.dismiss();
       //doinbkg has sent the deplist result - put it in dep spinner
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, deplist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_dep.setAdapter(adapter);
       if (state.contentEquals("UPDATE_PWD") || state.contentEquals("ADD_CAT") )
       {
         spinner_dep.setSelection(adapter.getPosition(departement));
       }
     }
   }
   
   private class GetClubMain extends GetClub
   {
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(JSONArray jArray) 
     {
       progressBar.dismiss();
       //doinbkg has sent the JSONArray result - put it in club spinner
       jArrayClub = jArray;
       List<String> clublist = new ArrayList<String>();
       for (int i=0; i < jArray.length(); i++)
       {
         try 
         {
           JSONObject oneObject = jArray.getJSONObject(i);
           // Pulling club item from the array
           clublist.add(oneObject.getString("club"));
		     } catch (JSONException e) 
         {
		         // Oops
		     }
       }
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, clublist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_club.setAdapter(adapter);
       if (state.contentEquals("UPDATE_PWD") || state.contentEquals("ADD_CAT") )
       {
         spinner_club.setSelection(adapter.getPosition(club));
       }
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
       List<String> catlist = new  ArrayList<String>();
       for (Categorie c:catlistresult)
       {
         catlist.add(c.nom);
       }
       //doinbkg has sent the catlist result - put it in cat spinner
       //if more than one cat exists for the club, propose to the user to record all cat
       if (catlist.size() > 1)
       {
         catlist.add(0,"Toutes les catégories...");
       }
       if (state.contentEquals("UPDATE_PWD") || state.contentEquals("ADD_CAT"))
       {
         catlist.clear();
         catlist.add(0, cat);
       }
       catlisttorecord = catlist;
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, catlist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_cat.setAdapter(adapter);
       if (state.contentEquals("ADD_CAT"))
       {
         edittext_pwd.setText(password);
       }
     }
   }
   
   private class GetLastInfoDateMain extends GetLastInfoDate
   {
     //private String sport;
     //private String club;
     private String clubid;
     public GetLastInfoDateMain (String sport,String club,String clubid){
       //this.sport = sport;
       //this.club = club;
       this.clubid = clubid;
     }
     
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<Info> listinfo) 
     {
       if (listinfo.size() > 0)
       {
         Global.ConnexionUserUpdateDateInformation(ctx, clubid, listinfo.get(0).date_record);
       }
       else
       {
         Global.ConnexionUserUpdateDateInformation(ctx, clubid, "nodate");
       }
     }
   }
   
   private class GetLastResultatDateMain extends GetLastResultatDate
   {
     //private String sport;
     //private String club;
     private String clubid;
     public GetLastResultatDateMain (String sport,String club,String clubid){
       //this.sport = sport;
       //this.club = club;
       this.clubid = clubid;
     }
     
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<Resultat> listresult) 
     {
       if (listresult.size() > 0)
       {
         Global.ConnexionUserUpdateDateResultat(ctx, clubid, listresult.get(0).date_record);
       }
       else
       {
         Global.ConnexionUserUpdateDateResultat(ctx, clubid, "nodate");
       }
     }
   }
   
   private class GetLastConvocationDateMain extends GetLastConvocationDate
   {
     //private String sport;
     //private String club;
     private String clubid;
     public GetLastConvocationDateMain (String sport,String club,String clubid){
       //this.sport = sport;
       //this.club = club;
       this.clubid = clubid;
     }
     
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<Convocation> listconv) 
     {
       if (listconv.size() > 0)
       {
         Global.ConnexionUserUpdateDateConvocation(ctx, clubid, listconv.get(0).date_record);
       }
       else
       {
         Global.ConnexionUserUpdateDateConvocation(ctx, clubid, "nodate");
       }
     }
   }
   
   private class GetLastCatDateMain extends GetLastCatDate
   {
     //private String dep;
     //private String sport;
     //private String club;
     private String clubid;
     public GetLastCatDateMain (String dep,String sport,String club,String clubid){
//       this.dep = dep;
//       this.sport = sport;
//       this.club = club;
       this.clubid = clubid;
     }
     
     @Override
     protected void onPreExecute()
     { 
     
     }

     @Override
     protected void onPostExecute(List<Categorie> listcat) 
     {
       if (listcat.size() > 0)
       {
         Global.ConnexionUserUpdateDateCategorie(ctx, clubid, listcat.get(0).date_record);
       }
       else
       {
         Global.ConnexionUserUpdateDateCategorie(ctx, clubid, "nodate");
       }
       
     }
   }
   
   private String getIdClub(String club)
   {
	   for (int i=0; i < jArrayClub.length(); i++)
	   {
	     try 
	     {
	       JSONObject oneObject = jArrayClub.getJSONObject(i);
	       if (oneObject.getString("club") == club)
	       {
	         return oneObject.getString("id_club");
	       }
		   }catch (JSONException e) 
	     {
		     // Oops
		   }
		 }
	   return null;   
   }
   
   private String getUserPassword(String club)
   {
	   for (int i=0; i < jArrayClub.length(); i++)
	   {
	     try 
	     {
	       JSONObject oneObject = jArrayClub.getJSONObject(i);
	       if (oneObject.getString("club") == club)
	       {
	         return oneObject.getString("password"); 
	       }
	     }catch (JSONException e) 
	     {
	       // Oops
		   }
		 }
	   return null;   
   }
   
   private String getAdminPassword(String club)
   {
     for (int i=0; i < jArrayClub.length(); i++)
     {
       try 
       {
         JSONObject oneObject = jArrayClub.getJSONObject(i);
         if (oneObject.getString("club") == club)
         {
           return oneObject.getString("password_admin"); 
         }
       }catch (JSONException e) 
       {
         // Oops
       }
     }
     return null;   
   }
 
}
