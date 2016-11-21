package com.lvovard.sportteam;




import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AdminCreateClubActivity extends Activity
{
  
  protected Spinner spinner_sport ;
  protected Spinner spinner_dep ;
  protected Button  btn_create ;
  protected Button  btn_cancel ;
  
  protected EditText txt_name;
  protected EditText txt_email;
  protected EditText txt_pwd_admin;
  protected EditText txt_pwd_admin_confirm;
  protected EditText txt_pwd_user;
  protected EditText txt_pwd_user_confirm;
  protected CheckBox checkBox1;
  
  String sport;
  String dep;
  String club;
  String email;
  String pwd_user;
  String pwd_admin;
  
  private  ProgressDialog progressBar;
  
  Context ctx = AdminCreateClubActivity.this;
  

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
    setContentView(R.layout.activity_create_club);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_fiber_new_white_24dp);
    actionBar.setTitle("Création de club");
    actionBar.setSubtitle("information création");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    
    spinner_sport = (Spinner) findViewById(R.id.sport_spinner);
    ArrayAdapter<CharSequence> sadapter = ArrayAdapter.createFromResource(this,R.array.sport_array, android.R.layout.simple_spinner_item);
    sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_sport.setAdapter(sadapter);
    
    spinner_dep = (Spinner) findViewById(R.id.departement_spinner);
    ArrayAdapter<CharSequence> dadapter = ArrayAdapter.createFromResource(this,R.array.departement_array, android.R.layout.simple_spinner_item);
    dadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner_dep.setAdapter(dadapter);

    
    btn_create = (Button) findViewById(R.id.btnCreate);
    btn_cancel = (Button) findViewById(R.id.btnAnnuler);
    
    txt_name = (EditText) findViewById(R.id.txt_name);
    txt_email = (EditText) findViewById(R.id.txt_email);
    txt_pwd_admin = (EditText) findViewById(R.id.txt_pwd_admin);
    txt_pwd_admin_confirm = (EditText) findViewById(R.id.txt_pwd_admin_confirm);
    txt_pwd_user = (EditText) findViewById(R.id.txt_pwd_user);
    txt_pwd_user_confirm = (EditText) findViewById(R.id.txt_pwd_user_confirm);
    checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
    
    btn_create.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        if (checkparam())
        {
          if (txt_pwd_admin.getText().toString().contentEquals(txt_pwd_admin_confirm.getText().toString()))
          {
            if (txt_pwd_user.getText().toString().contentEquals(txt_pwd_user_confirm.getText().toString()))
            {
              //TODO add here management of club creation with sql request
              //this part will move in async task
              dialogBoxConfirm();
            }
            else
            {
              Toast.makeText(ctx, "mauvaise confirmation de mot de passe utilisateur", Toast.LENGTH_LONG).show();
            }
          }
          else
          {
            Toast.makeText(ctx, "mauvaise confirmation de mot de passe admin", Toast.LENGTH_LONG).show();
          }
        }
        

      }
    });
    
    btn_cancel.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Intent intent = new Intent(ctx, MainActivity.class);
        startActivity(intent);
      }
    });
    
    checkBox1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) 
      {
        if (((CheckBox) v).isChecked()) 
        {
          txt_pwd_admin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
          txt_pwd_admin_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
          txt_pwd_user.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
          txt_pwd_user_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else
        {
          txt_pwd_admin.setTransformationMethod(PasswordTransformationMethod.getInstance());
          txt_pwd_admin_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
          txt_pwd_user.setTransformationMethod(PasswordTransformationMethod.getInstance());
          txt_pwd_user_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

      }
    });
  }
  
  

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.create_club, menu);
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
  
  public void dialogBoxConfirm() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    sport = spinner_sport.getSelectedItem().toString();
    dep = spinner_dep.getSelectedItem().toString();
    club = txt_name.getText().toString().toUpperCase();
    email = txt_email.getText().toString();
    pwd_user = txt_pwd_user.getText().toString();
    pwd_admin = txt_pwd_admin.getText().toString();
    alertDialogBuilder.setMessage("Le club "+club+" va être créé avec les valeurs suivantes:\n\r"
                                  + "- sport: "+sport+"\n\r"
                                  + "- departement: "+dep+"\n\r"
                                  + "- email: "+txt_email.getText().toString()+"\n\r"
                                  + "- mdp admin: "+pwd_admin+"\n\r"
                                  + "- mdp utilisateur: "+pwd_user+"\n\r\n\r"
                                  + "IMPORTANT: \n\r"
                                  + "- conservez le mdp admin (il doit être diffuser uniquement aux dirigeants qui pourront créer, modifier, supprimer les convocations, résultats et informations du club)\n\r"
                                  + "- conservez le mdp utilisateur (il doit être diffuser aux joueurs/dirigeants qui souhaitent uniquement consulter les convocations, résultats et informations du club)" 
                                  
                                  );
    alertDialogBuilder.setTitle("Création du club "+txt_name.getText().toString().toUpperCase());
    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);  
    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar = new ProgressDialog(ctx);
        progressBar.setCancelable(true);
        progressBar.setMessage("creation du club "+club+"...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        new createClubMain().execute(sport,dep,club,email,pwd_user,pwd_admin);
      }
    });

    alertDialogBuilder.setNegativeButton("ANNULER",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
      }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }
  
  public boolean checkparam()
  {
    if (TextUtils.isEmpty(txt_name.getText()))
    {
      Toast.makeText(ctx, "le champ nom de club est vide", Toast.LENGTH_LONG).show();
      txt_name.setSelection(0);
      return false;
    }
    if (TextUtils.isEmpty(txt_email.getText()))
    {
      Toast.makeText(ctx, "le champ email est vide", Toast.LENGTH_LONG).show();
      txt_email.setSelection(0);
      return false;
    }
    else
    {
      if (! android.util.Patterns.EMAIL_ADDRESS.matcher(txt_email.getText()).matches())
      {
        Toast.makeText(ctx, "le champ email ne contient pas une adresse email valide", Toast.LENGTH_LONG).show();
        return false;
      }
    }
    if (TextUtils.isEmpty(txt_pwd_admin.getText()))
    {
      Toast.makeText(ctx, "le mot de passe admin est vide", Toast.LENGTH_LONG).show();
      txt_pwd_admin.setSelection(0);
      return false;
    }
    if (TextUtils.isEmpty(txt_pwd_user.getText()))
    {
      Toast.makeText(ctx, "le mot de passe utilisateur est vide", Toast.LENGTH_LONG).show();
      txt_pwd_user.setSelection(0);
      return false;
    }
    return true;
  }
  
  private class createClubMain extends createClub
  {
    @Override
    protected void onPreExecute()
    { 
    }

    @Override
    protected void onPostExecute(Integer result) 
    {
      progressBar.dismiss();
      if (result > 0)
      {
        Global.ConnexionAddAdminClub(ctx, sport, dep, club, String.valueOf(result), pwd_admin);
        Toast.makeText(ctx, "le club "+club+" a été créé avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, BothModeListClubActivity.class);
        intent.putExtra("mode","admin");
        startActivity(intent);
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la création du club "+club+"\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
}
  
