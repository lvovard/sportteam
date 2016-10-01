package com.lvovard.sportteam;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.lvovard.sportteam.MyBroadcastReceiver.getConvocationDateNotifMain;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author lavovard
 * Main activity - launcher
 *
 */
public class MainActivity extends Activity {
	
	
	/**
	 * elements of main activity
	 *
	 */
	protected Spinner spinner_sport ;
	protected Spinner spinner_dep ;
	protected Spinner spinner_club ;
	protected Spinner spinner_cat ;
	protected Button button_cnx;
	protected Button button_club;
	protected EditText edittext_pwd;
	
	
	
  String sport;
  String departement;
  String club;
  String cat;
  String password;
  //used for sql result (club, club id and pwd)
  JSONArray jArrayClub = null;
  List<String> catlisttorecord = null;
  private  ProgressDialog progressBar;
  
  //state machine - START is the default one
	static String state = "START";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	  
		super.onCreate(savedInstanceState);
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);

		//create background timer to poll new convoc,result or info and trigger notification
		/////
		boolean start_alarm=true;
		int cnt = 0;
		Global.db=openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
		try 
    {
      FileInputStream objFile = openFileInput("alarm.txt");
      InputStreamReader objReader = new InputStreamReader(objFile);
      BufferedReader objBufferReader = new BufferedReader(objReader);
      String strLine;
      Log.i("myApp", "Reading alarm.txt ..." );
      while ((strLine = objBufferReader.readLine()) != null) 
      {
        if (strLine.contains("ALARM=STARTED"))
        {
          start_alarm=false;
          break;
        }
      }
      objFile.close();
    }
    catch (FileNotFoundException objError) {
      start_alarm = true;
    }
    catch (IOException objError) {
      start_alarm = true;
    }
		if (start_alarm)
		{
	    OutputStreamWriter out;
	    try
	    {
	      out = new OutputStreamWriter(openFileOutput("alarm.txt",Context.MODE_PRIVATE));
	      String line = "ALARM=STARTED\n\r";
	      out.write(line);
	      out.close();
	    } catch (FileNotFoundException e)
	    {
	      e.printStackTrace();
	    } catch (IOException e)
	    {
	      e.printStackTrace();
	    }
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    Intent intent1 = new Intent(this,MyBroadcastReceiver.class); 
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent1, 0);
	    //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
	    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,GregorianCalendar.getInstance().getTimeInMillis()+5000, Global.TIME_MS_FOR_NOTIF_CHECK, pendingIntent);
	    Toast.makeText(this, "ALARM demarre maintenant", Toast.LENGTH_LONG).show();
		}
		else
		{
		  Toast.makeText(this, "ALARM deja demarre", Toast.LENGTH_LONG).show();
		}
		Cursor c;
    try
    {
      c=Global.db.rawQuery("SELECT * FROM connexion", null);
      cnt = c.getCount();
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        Log.i("myApp","table connexion does not exist");  
      }
      else
      {
        Log.i("myApp","error when opening connexion table : "+e.toString());
      } 
      
    }
    
    //info are coming with notif to update password
    Intent i=getIntent();
    
    if (! TextUtils.isEmpty(i.getStringExtra("updatepwd_club"))) {
      club = i.getStringExtra("updatepwd_club");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("updatepwd_dep"))) {
      departement = i.getStringExtra("updatepwd_dep");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("updatepwd_sport"))) {
      sport = i.getStringExtra("updatepwd_sport");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("updatepwd_cat"))) {
      cat = i.getStringExtra("updatepwd_cat");
    }
    if (! TextUtils.isEmpty(i.getStringExtra("updatepwd_state"))) {
      state = i.getStringExtra("updatepwd_state");
    }


    //

		if (state.contentEquals("START"))
		{
  		  
  		//check if user has already recorded some entries during the previous connections
		  //connexion contains line with useful info

	    try
	    {
	      //Cursor c=Global.db.rawQuery("SELECT * FROM connexion", null);
	      if(cnt==0)
	      {
	        Log.i("myApp", "get all DB connexion records -> nothing");
	      }
	      else
	      {
          Intent intent = new Intent(MainActivity.this, ClubActivity.class);
          startActivity(intent); 
	      }
	    }
	    catch(SQLException e)
	    {
	      if (e.toString().contains("no such table"))
	      {
	        Log.i("myApp","table connexion does not exist");  
	      }
	      else
	      {
	        Log.i("myApp","error when opening connexion table : "+e.toString());
	      } 
	      
	    }
  	}
		
		setContentView(R.layout.activity_main); 
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setBackgroundResource(R.drawable.bluebkg);
		
		spinner_sport = (Spinner) findViewById(R.id.sport_spinner);
		spinner_dep = (Spinner) findViewById(R.id.departement_spinner);
		spinner_club = (Spinner) findViewById(R.id.club_spinner);
		spinner_cat = (Spinner) findViewById(R.id.cat_spinner);
		button_cnx = (Button) findViewById(R.id.btnConnexion);
		button_club = (Button) findViewById(R.id.btnClub);
		edittext_pwd = (EditText) findViewById(R.id.txtPassword);
    if(cnt==0)
    {
      button_club.setVisibility(View.INVISIBLE);
    }
    else
    {
      button_club.setVisibility(View.VISIBLE);
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
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, emptylist);
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
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, emptylist);
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
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, emptylist);
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
	}
	
	
	public void addListenerOnButton() 
	{
	  button_cnx.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			  password = edittext_pwd.getText().toString();
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
			  //check password validity
			  if (password.contentEquals(getPassword(club)))
			  {
			    //for each cat to record
			    //put it in the connection file with some checks...
			    for (String categorie:catlisttorecord)
			    {
			      Toast.makeText(MainActivity.this,sport+"-"+departement+"-"+club+"-"+categorie, Toast.LENGTH_SHORT).show();
			      Global.db.execSQL("CREATE TABLE IF NOT EXISTS connexion(sport VARCHAR,dep VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,password VARCHAR,UNIQUE(clubid, cat) ON CONFLICT FAIL);");
			      try
			      {
			        Global.db.execSQL("INSERT INTO connexion VALUES('"+sport+"','"+departement+"','"+club+"','"+getIdClub(club)+"','"+categorie+"','"+password+"');");
			      }
			      catch(SQLiteConstraintException e)
			      {
			        Toast.makeText(MainActivity.this, club+" - "+categorie+" est deja enregistré",Toast.LENGTH_LONG).show();    
			        return;
			      }

            Global.db.execSQL("CREATE TABLE IF NOT EXISTS dateinformation(sport VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,date VARCHAR);");
            GetLastInfoDateMain taskinfo = new GetLastInfoDateMain(sport,club,getIdClub(club),categorie);
            taskinfo.execute(getIdClub(club),categorie,"nodate");
            Global.db.execSQL("CREATE TABLE IF NOT EXISTS dateresultat(sport VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,date VARCHAR);");
            GetLastResultatDateMain taskresultat = new GetLastResultatDateMain(sport,club,getIdClub(club),categorie);
            taskresultat.execute(getIdClub(club),categorie,"nodate");
            Global.db.execSQL("CREATE TABLE IF NOT EXISTS dateconvocation(sport VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,date VARCHAR);");
            GetLastConvocationDateMain taskconvocation = new GetLastConvocationDateMain(sport,club,getIdClub(club),categorie);
            taskconvocation.execute(getIdClub(club),categorie,"nodate");
			    }
          GetLastInfoDateMain taskinfo = new GetLastInfoDateMain(sport,club,getIdClub(club),"all-");
          taskinfo.execute(getIdClub(club),"all-","nodate");
          Intent intent = new Intent(MainActivity.this, ClubActivity.class);
          startActivity(intent);
			  }
			  else
			  {
				  Toast.makeText(MainActivity.this,"mauvais mot de passe", Toast.LENGTH_SHORT).show();
			  }
			}
		});
	  
	   button_club.setOnClickListener(new OnClickListener() 
	    {
	      @Override
	      public void onClick(View v) 
	      {
          Intent intent = new Intent(MainActivity.this, ClubActivity.class);
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
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, sportlist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_sport.setAdapter(adapter);
       if (state.contentEquals("UPDATE_PWD"))
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
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, deplist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_dep.setAdapter(adapter);
       if (state.contentEquals("UPDATE_PWD"))
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
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, clublist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_club.setAdapter(adapter);
       if (state.contentEquals("UPDATE_PWD"))
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
     protected void onPostExecute(List<String> catlist) 
     {
       progressBar.dismiss();
       //doinbkg has sent the catlist result - put it in cat spinner
       //if more than one cat exists for the club, propose to the user to record all cat
       if (catlist.size() > 1)
       {
         catlist.add(0,"Toutes les catégories...");
       }
       if (state.contentEquals("UPDATE_PWD"))
       {
         catlist.clear();
         catlist.add(0, cat);
       }
       catlisttorecord = catlist;
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, catlist);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner_cat.setAdapter(adapter);
     }
   }
   
   private class GetLastInfoDateMain extends GetLastInfoDate
   {
     private String sport;
     private String club;
     private String clubid;
     private String cat;
     public GetLastInfoDateMain (String sport,String club,String clubid,String cat){
       this.sport = sport;
       this.club = club;
       this.clubid = clubid;
       this.cat = cat;
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
         Log.i("myApp", "ecriture de la date dans BD dateinformation "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";"+listinfo.get(0).date_record+"\n\r");
         Global.db.execSQL("INSERT INTO dateinformation VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+listinfo.get(0).date_record+"');");
       }
       else
       {
         Log.i("myApp", "ecriture de la date dans BD dateinformation "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";nodate\n\r");
         Global.db.execSQL("INSERT INTO dateinformation VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+"nodate"+"');");
       }
     }
   }
   
   private class GetLastResultatDateMain extends GetLastResultatDate
   {
     private String sport;
     private String club;
     private String clubid;
     private String cat;
     public GetLastResultatDateMain (String sport,String club,String clubid,String cat){
       this.sport = sport;
       this.club = club;
       this.clubid = clubid;
       this.cat = cat;
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
         Log.i("myApp", "ecriture de la date dans BD dateresultat "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";"+listresult.get(0).date_record+"\n\r");
         Global.db.execSQL("INSERT INTO dateresultat VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+listresult.get(0).date_record+"');");
       }
       else
       {
         Log.i("myApp", "ecriture de la date dans BD dateresultat "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";nodate\n\r");
         Global.db.execSQL("INSERT INTO dateresultat VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+"nodate"+"');");
       }
     }
   }
   
   private class GetLastConvocationDateMain extends GetLastConvocationDate
   {
     private String sport;
     private String club;
     private String clubid;
     private String cat;
     public GetLastConvocationDateMain (String sport,String club,String clubid,String cat){
       this.sport = sport;
       this.club = club;
       this.clubid = clubid;
       this.cat = cat;
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
	    	 Log.i("myApp", "ecriture de la date dans BD dateconvocation "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";"+listconv.get(0).date_record+"\n\r");
	    	 Global.db.execSQL("INSERT INTO dateconvocation VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+listconv.get(0).date_record+"');");
       }
       else
       {
	    	 Log.i("myApp", "ecriture de la date dans BD dateconvocation "+this.sport+";"+this.club+";"+this.clubid+";"+this.cat+";nodate\n\r");
	    	 Global.db.execSQL("INSERT INTO dateconvocation VALUES('"+sport+"','"+club+"','"+clubid+"','"+cat+"','"+"nodate"+"');");
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
   
   private String getPassword(String club)
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
 
}
