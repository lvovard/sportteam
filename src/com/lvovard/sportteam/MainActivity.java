package com.lvovard.sportteam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

	protected Button btnSuivre;
	protected Button btnGerer;
	protected TextView textwelcome;
	
	LinearLayout layout;
	Context ctx = MainActivity.this;
	String version;
	
	
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
    String versionName;
    try
    {
      versionName = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
      int versionCode = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
      Log.i("myApp",versionName);
      Log.i("myApp",""+versionCode);
      version = versionName+"."+String.valueOf(versionCode);
    } catch (NameNotFoundException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    setContentView(R.layout.activity_main);
    saveLogcatToFile(ctx);
    
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
//    textwelcome = (TextView) findViewById(R.id.textwelcome);
//    textwelcome.setText(Html.fromHtml("<br/><br/><big><b><i>SportTeam "+version+"</i></b></big><br/><br/>"));
//    textwelcome.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
    //create background timer to poll new convoc,result or info and trigger notification
    boolean start_alarm=true;

    try 
    {
      FileInputStream objFile = openFileInput("alarm.txt");
      InputStreamReader objReader = new InputStreamReader(objFile);
      BufferedReader objBufferReader = new BufferedReader(objReader);
      String strLine;
      //log.i("myApp", "Reading alarm.txt ..." );
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
      Intent intent1 = new Intent(this,NotifMyBroadcastReceiver.class); 
      PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent1, 0);
      //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent);
      alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,GregorianCalendar.getInstance().getTimeInMillis()+5000, Global.TIME_MS_FOR_NOTIF_CHECK, pendingIntent);
      //Toast.makeText(this, "ALARM demarre maintenant", Toast.LENGTH_LONG).show();
    }
    else
    {
      //Toast.makeText(this, "ALARM deja demarrée", Toast.LENGTH_LONG).show();
    }
   
    btnSuivre = (Button) findViewById(R.id.btnSuivre);
    btnGerer = (Button) findViewById(R.id.btnGerer);
    Drawable drawablebtn1 = null;
    Drawable drawablebtn2 = null;
    //if (Build.VERSION.SDK_INT >= 22)
    //{
      //drawablebtn1 = getResources().getDrawable(R.drawable.ic_visibility_black_24dp,getApplicationContext().getTheme());
      //drawablebtn1 = getResources().getDrawable(R.drawable.ic_settings_black_24dp,getApplicationContext().getTheme());
    //}
    //else
    //{
      drawablebtn1 = getResources().getDrawable(R.drawable.ic_visibility_black_24dp);
      drawablebtn2 = getResources().getDrawable(R.drawable.ic_settings_black_24dp);
    //}
    
    drawablebtn1.setBounds(0, 0, (int)(drawablebtn1.getIntrinsicWidth()*1.5),(int)(drawablebtn1.getIntrinsicHeight()*1.5));
    ScaleDrawable sd1 = new ScaleDrawable(drawablebtn1, 0, 1, 1);
    btnSuivre.setCompoundDrawablesWithIntrinsicBounds(sd1.getDrawable(), null, null, null); //set drawableLeft for example
    drawablebtn2.setBounds(0, 0, (int)(drawablebtn2.getIntrinsicWidth()*1.5),(int)(drawablebtn2.getIntrinsicHeight()*1.5));
    ScaleDrawable sd2 = new ScaleDrawable(drawablebtn2, 0, 1, 1);
    btnGerer.setCompoundDrawablesWithIntrinsicBounds(sd2.getDrawable(), null, null, null); //set drawableLeft for example
    //btnSuivre.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_visibility_black_24dp, 0, 0, 0);
    btnSuivre.setText(Html.fromHtml("<big><b><i>SUIVRE UN CLUB</i></b></big><br/><small>Vous pourrez choisir des catégories afin de recevoir les convocation, résultats, informations et evenements</small>"));
    btnGerer.setText(Html.fromHtml("<big><b><i>GERER UN CLUB</i></b></big><br/><small>Vous pourrez envoyer, modifier, supprimer les catégories, joueurs, convocation, résultats, informations et evenements</small>"));
    addListenerOnButton();

    
  }
	
	public void addListenerOnButton() 
	{
	  btnSuivre.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			  //if no club -> create activity else go to club list
			  Intent intent = new Intent(ctx, BothModeAddClubActivity.class);
			  if (Global.ConnexionHasUserClub(ctx))
			  {
			    intent = new Intent(ctx, BothModeListClubActivity.class);
			  }
			  intent.putExtra("mode","user");
			  intent.putExtra("caller","MainActivity");
			  //intent = new Intent(ctx, TestActivity.class);
			  //intent = new Intent(ctx, StatisticListActivity.class);
			  
        startActivity(intent);
			}
		});
	  
	   btnGerer.setOnClickListener(new OnClickListener() 
	    {
	      @Override
	      public void onClick(View v) 
	      {
	        //if no club -> create activity else go to club list
	        Intent intent = new Intent(ctx, AdminWayToAddClubActivity.class);
	        if (Global.ConnexionHasAdminClub(ctx))
	        {
	          intent = new Intent(ctx, BothModeListClubActivity.class);
	          intent.putExtra("mode","admin");
	          intent.putExtra("caller","MainActivity");
	        }
	        startActivity(intent);
	      }
	    });
	} 
	
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
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

      case R.id.action_help:
        dialoghelp();
      return true;
    

    default:
    return super.onOptionsItemSelected(item);
    }
  }
  
  public void dialoghelp() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//    TextView myMsg = new TextView(this);
//    myMsg.setText(Html.fromHtml("<br/><big><b><i>Bienvenue et merci d'avoir installé l'application SportTeam<br/><br/>Si vous êtes joueurs, cette application va vous permettre de suivre et recevoir les convocations, résultats, informations et évenements de votre club<br/><br/>Si vous êtes dirigeants, cette application va vous permettre de gérer vos équipes, convocations, résultats, informations et évenements pour tout le club</i></b></big><br/>"));
//    myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
//    alertDialogBuilder.setView(myMsg);


    alertDialogBuilder.setMessage(Html.fromHtml(""
        + "<br/><i>SportTeam "+version+"<br/><br/>"
            + "Bienvenue et merci d'avoir installé l'application SportTeam.<br/><br/>"
            + "Si vous êtes joueurs, cette application va vous permettre de suivre et recevoir les convocations, résultats, informations et évenements de votre club. Demandez vite le mot de passe utilisateur à votre dirigeant.Ne perdez pas de temps, cliquez sur suivre un club et laissez vous guider.<br/><br/>Si vous êtes dirigeants, cette application va vous permettre de gérer vos équipes, joueurs, convocations, résultats, informations et évenements pour tout le club.Ne perdez pas de temps, cliquez sur gérer un club et laissez vous guider.</i><br/>"));
    
    alertDialogBuilder.setTitle("Aide SportTeam");
    alertDialogBuilder.setIcon(R.drawable.ic_launcher);  
    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {

      }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
    TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
    messageView.setGravity(Gravity.CENTER);
  }
  
  public void saveLogcatToFile(Context context) 
  {   
    //log only one file for one launch of SportTeam
    if ( (Global.logfilecreated == false) && (ctx.getExternalCacheDir() != null))
    {
      String res = Global.checkLogError(ctx);
      if (res != null)
      {
        new insertCrashMain().execute(res);
      }
      //log.i("myApp", "File size is "+Global.getLogErrorSize(ctx)+"Ko" );
      //delete previous file before creating a new one.
      Global.deleteLogError(ctx);

      String fileName = "logcat_"+System.currentTimeMillis()+".txt";
      File outputFile = new File(context.getExternalCacheDir(),fileName);
      //log.i("myApp", "File is --> "+outputFile.getAbsolutePath() );
      try
      {
        @SuppressWarnings("unused")
        Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      Global.logfilecreated = true;
    }
  }
  
  private class insertCrashMain extends insertCrash
  {
    
    
    @Override
    protected void onPreExecute()
    { 
    }

    @Override
    protected void onPostExecute(Boolean result) 
    {
      if (result)
      {
        //Toast.makeText(ctx, "log envoyé à la DB crashinfo", Toast.LENGTH_LONG).show();
      }
    }
  }

}
