package com.lvovard.sportteam;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author lavovard
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
	protected EditText edittext_pwd;
  String sport;
  String departement;
  String club;
  String cat;
  String password;
	/**
	 * hashtable for other activities
	 *
	 */
	static Hashtable<String, ArrayList<String>> liste = new Hashtable<String, ArrayList<String>>();
	static String state = "START";

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	  
		super.onCreate(savedInstanceState);

		if (state.contentEquals("START"))
		{
  		  
  		//check if user has already recorded some entries
  		try 
  		{
  			FileInputStream objFile = openFileInput("connexion.txt");
  			InputStreamReader objReader = new InputStreamReader(objFile);
  			BufferedReader objBufferReader = new BufferedReader(objReader);
  			String strLine;
  			Log.i("myApp", "Reading connexion.txt ..." );
  			while ((strLine = objBufferReader.readLine()) != null) 
  			{
  			  if (strLine.matches(".*;.*;.*;.*;.*"))
  			  {
    				Log.i("myApp", strLine);
    				String[] parts = strLine.split(";");
    				Log.i("myApp", "create new record");
    				Log.i("myApp", "sport       ="+parts[0]);
    				Log.i("myApp", "departement ="+parts[1]);
    				Log.i("myApp", "club        ="+parts[2]);
    				Log.i("myApp", "password    ="+parts[3]);
    				Log.i("myApp", "category    ="+parts[4]);
    				Record rd = new Record(parts[0],parts[1],parts[2],parts[3],parts[4]);
    				if (rd.hasBeenAddedToTheList() == true)
    				{
    				  Log.i("myApp", "record has been added to the record list");
    				}
    				else
    				{
    				  Log.i("myApp", "record not added - alraedy exists in the record list");
    				}
  			  }
  			}
  			objFile.close();
  			if (Record.recordlist.size() > 0)
  			{
          Intent intent = new Intent(MainActivity.this, ClubActivity.class);
          startActivity(intent); 
  			}

  		}
  		catch (FileNotFoundException objError) {
  			Toast.makeText(this, "Aucune configuration trouvée\n", Toast.LENGTH_LONG).show();
  		}
  		catch (IOException objError) {
  			Toast.makeText(this, "Erreur\n"+objError.toString(), Toast.LENGTH_LONG).show();
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
		edittext_pwd = (EditText) findViewById(R.id.txtPassword);
		addListenerOnButton();
		
		//SPORT
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.sport_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_sport.setAdapter(adapter);
		
		//DEPARTEMENT
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_dep = ArrayAdapter.createFromResource(this,
		        R.array.departement_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_dep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_dep.setAdapter(adapter_dep);
		
		//CLUB
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_club = ArrayAdapter.createFromResource(this,
		        R.array.club_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_club.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_club.setAdapter(adapter_club);
		
		//CAT
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter_cat = ArrayAdapter.createFromResource(this,
		        R.array.cat_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter_cat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner_cat.setAdapter(adapter_cat);
		
		spinner_sport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        /**
	         * Called when a new item is selected (in the Spinner)
	         */
	         public void onItemSelected(AdapterView<?> parent, View view, 
	                    int pos, long id) {
	                // An spinnerItem was selected. You can retrieve the selected item using
	                // parent.getItemAtPosition(pos)
	                //Toast.makeText(MainActivity.this, (String) spinner_sport.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
	        	 	sport = (String) spinner_sport.getItemAtPosition(pos);
	            }

	            public void onNothingSelected(AdapterView<?> parent) {
	                // Do nothing, just another required interface callback
	            }

	    }); // (optional)

		spinner_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        /**
	         * Called when a new item is selected (in the Spinner)
	         */
	         public void onItemSelected(AdapterView<?> parent, View view, 
	                    int pos, long id) {
	                // An spinnerItem was selected. You can retrieve the selected item using
	                // parent.getItemAtPosition(pos)
	                //Toast.makeText(MainActivity.this, (String) spinner_dep.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
	        	 	departement = (String) spinner_dep.getItemAtPosition(pos);
	            }

	            public void onNothingSelected(AdapterView<?> parent) {
	                // Do nothing, just another required interface callback
	            }

	    }); // (optional)
		
		spinner_club.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	        /**
	         * Called when a new item is selected (in the Spinner)
	         */
	         public void onItemSelected(AdapterView<?> parent, View view, 
	                    int pos, long id) {
	                // An spinnerItem was selected. You can retrieve the selected item using
	                // parent.getItemAtPosition(pos)
	                //Toast.makeText(MainActivity.this, (String) spinner_club.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
	        	 	club = (String) spinner_club.getItemAtPosition(pos);
	            }

	            public void onNothingSelected(AdapterView<?> parent) {
	                // Do nothing, just another required interface callback
	            }

	    }); // (optional)
		
		spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		
        /**
         * Called when a new item is selected (in the Spinner)
         */
         public void onItemSelected(AdapterView<?> parent, View view, 
                    int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                //Toast.makeText(MainActivity.this, (String) spinner_cat.getItemAtPosition(pos),Toast.LENGTH_SHORT).show();
        	 	cat = (String) spinner_cat.getItemAtPosition(pos);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

    }); // (optional)
        
	}
	
	
	public void addListenerOnButton() 
	{

		button_cnx.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			  password = edittext_pwd.getText().toString();
				Toast.makeText(MainActivity.this,sport+"-"+departement+"-"+club+"-"+cat+"-"+password, Toast.LENGTH_SHORT).show();
				Record rd = new Record(sport, departement, club, password, cat);
        if (rd.hasBeenAddedToTheList() == true)
        {
          Log.i("myApp", "record has been added to the record list");
          Toast.makeText(MainActivity.this,"new record OK", Toast.LENGTH_SHORT).show();
          OutputStreamWriter out;
          try
          {
            out = new OutputStreamWriter(openFileOutput("connexion.txt",Context.MODE_APPEND));
            String line = sport+";"+departement+";"+club+";"+password+";"+cat+"\n\r";
            out.write(line);
            out.close();
          } 
          catch (FileNotFoundException e)
          {
            e.printStackTrace();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }

        }
        else
        {
          Toast.makeText(MainActivity.this,"new record alraedy exists", Toast.LENGTH_SHORT).show();
          Log.i("myApp", "record not added - alraedy exists in the record list");
        }
				Intent intent = new Intent(MainActivity.this, ClubActivity.class);
		    startActivity(intent);
			}
		});
	}

	

	
	
  
}
