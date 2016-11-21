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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AdminChangePwdActivity extends Activity
{
  
  protected TextView textoldpwd;
  protected TextView textnewpwd;
  protected TextView textconfirmpwd;
  protected EditText txt_old_pwd;
  protected EditText txt_new_pwd;
  protected EditText txt_confirm_pwd;
  protected Button btnChanger;
  protected Button btnAnnuler;
  protected CheckBox checkBox1;
  String change_mode;
  String oldpwd;
  String newpwd;
  String confirmpwd;
  private  ProgressDialog progressBar;
  
  String current_pwd;
  
  Context ctx = AdminChangePwdActivity.this;
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
    setContentView(R.layout.activity_admin_change_pwd);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    actionBar.setTitle("Changement du mot de passe");
    actionBar.setSubtitle("Choix");
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    this.textoldpwd = (TextView) findViewById(R.id.textoldpwd);
    this.textnewpwd = (TextView) findViewById(R.id.textnewpwd);
    this.textconfirmpwd = (TextView) findViewById(R.id.textconfirmpwd);
    this.txt_old_pwd = (EditText) findViewById(R.id.txt_old_pwd);
    this.txt_new_pwd = (EditText) findViewById(R.id.txt_new_pwd);
    this.txt_confirm_pwd = (EditText) findViewById(R.id.txt_confirm_pwd);
    this.btnChanger = (Button) findViewById(R.id.btnChanger);
    this.btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
    this.checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
    Intent i=getIntent();
    
    if (! TextUtils.isEmpty(i.getStringExtra("change_mode"))) {
      change_mode = i.getStringExtra("change_mode");
    }
    if (change_mode.contentEquals("admin"))
    {
      actionBar.setIcon(R.drawable.ic_lockadmin_white_24dp);
    }
    else
    {
      actionBar.setIcon(R.drawable.ic_lockuser_white_24dp);
    }
    
    
    actionBar.setSubtitle(change_mode);
    
    this.textoldpwd.setText("Entrer le mot de passe "+change_mode+" actuel");
    this.textnewpwd.setText("Entrer un nouveau mot de passe "+change_mode);
    this.textconfirmpwd.setText("Confirmer le nouveau mot de passe "+change_mode);
    
    progressBar = new ProgressDialog(this);
    progressBar.setCancelable(true);
    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressBar.setProgress(0);
    progressBar.setMax(100);
    progressBar.setMessage("Récupération du mot de passe");
    progressBar.show();
    new getPasswordMain().execute(adminclub.id_club);
    
    
    btnChanger.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        oldpwd = txt_old_pwd.getText().toString();
        newpwd = txt_new_pwd.getText().toString();
        confirmpwd = txt_confirm_pwd.getText().toString();
        if (checkparam())
        {
          dialogBoxConfirm();
        }
      }
    });
    
    btnAnnuler.setOnClickListener(new OnClickListener() 
    {
      @Override
      public void onClick(View v) 
      {
        Toast.makeText(ctx, "Modification mot de passe "+change_mode+" annulé", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);
      }
    });

    
    checkBox1.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) 
      {
        if (((CheckBox) v).isChecked()) 
        {
          txt_old_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
          txt_new_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
          txt_confirm_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else
        {
          txt_old_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
          txt_new_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
          txt_confirm_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin_change_pwd, menu);
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
        Intent intenthome = new Intent(ctx, AdminPwdActivity.class);
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
  
  public boolean checkparam()
  {
    if (TextUtils.isEmpty(txt_old_pwd.getText()))
    {
      Toast.makeText(ctx, "le champ mot de passe actuel est vide", Toast.LENGTH_LONG).show();
      txt_old_pwd.setError("le champ mot de passe actuel est vide");
      txt_old_pwd.setSelection(0);
      return false;
    }
    else
    {
      txt_old_pwd.setError(null);
    }
    if (TextUtils.isEmpty(txt_new_pwd.getText()))
    {
      Toast.makeText(ctx, "le champ nouveau mot de passe est vide", Toast.LENGTH_LONG).show();
      txt_new_pwd.setError("le champ nouveau mot de passe est vide");
      txt_new_pwd.setSelection(0);
      return false;
    }
    else
    {
      txt_new_pwd.setError(null);
    }
    if (! oldpwd.contentEquals(current_pwd))
    {
      Toast.makeText(ctx, "mauvais mot de passe actuel", Toast.LENGTH_LONG).show();
      txt_old_pwd.setError("mauvais mot de passe actuel");
      txt_old_pwd.setSelection(0);
      return false;
    }
    else
    {
      txt_old_pwd.setError(null);
    }
    if (newpwd.contentEquals(oldpwd))
    {
      Toast.makeText(ctx, "Le nouveau mot de passe est similaire à l'actuel - pas de changement", Toast.LENGTH_LONG).show();
      txt_new_pwd.setError("Le nouveau mot de passe est similaire à l'actuel - pas de changement");
      txt_new_pwd.setSelection(0);
      return false;
    }
    else
    {
      txt_new_pwd.setError(null);
    }
    if (! newpwd.contentEquals(confirmpwd))
    {
      Toast.makeText(ctx, "mauvaise confirmation de mot de passe", Toast.LENGTH_LONG).show();
      txt_confirm_pwd.setError("mauvaise confirmation de mot de passe");
      txt_confirm_pwd.setSelection(0);
      return false;
    }
    else
    {
      txt_confirm_pwd.setError(null);
    }
    
    
    return true;
  }
  
  public void dialogBoxConfirm() 
  {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    String important = "- conservez le mdp utilisateur (il doit être diffuser aux joueurs/dirigeants qui souhaitent uniquement consulter les convocations, résultats et informations du club)";
    if (change_mode.contentEquals("admin"))
    {
      important = "- conservez le mdp admin (il doit être diffuser uniquement aux dirigeants qui pourront créer, modifier, supprimer les convocations, résultats et informations du club)\n\r";
    }
    alertDialogBuilder.setMessage("Vous êtes sur le point de modifier le mot de passe "+change_mode+":\n\r"
                                  + "- ancien mdp: "+oldpwd+"\n\r"
                                  + "- nouveau mdp: "+newpwd+"\n\r\n\r"
                                  + "IMPORTANT: \n\r"
                                  + important
                                  );
    alertDialogBuilder.setTitle("Modification du mot de passe "+change_mode);
    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);  
    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() 
    {
      @Override
      public void onClick(DialogInterface arg0, int arg1) 
      {
        progressBar.setMessage("Modification du mot de passe "+change_mode+"...");
        progressBar.show();
        updatePwdMain taskpwd = new updatePwdMain(change_mode);
        taskpwd.execute(adminclub.id_club,newpwd,change_mode);
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
  
  private class updatePwdMain extends updatePwd
  {
    String mode;
    
    public updatePwdMain (String mode){
      this.mode = mode;
    }
    
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
        if (mode.contentEquals("admin"))
        {
          Global.ConnexionUpdateAdminPwd(ctx, adminclub.id_club, newpwd);
        }
        else
        {
          Global.ConnexionUpdateUserPwd(ctx, adminclub.id_club, newpwd);
        }
        Toast.makeText(ctx, "le mot de passe "+change_mode+" a été mofifié avec succès", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(ctx, AdminClubActivity.class);
        startActivity(intent);
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion lors de la modification du mot de passe "+change_mode+"\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
  
  private class getPasswordMain extends getPassword
  {

    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(String password_db) 
    {
      progressBar.dismiss();
      String password_user = "";
      String password_admin = "";
      //if we properly catch the passwords
      if (! password_db.contentEquals("nopasswordfound"))
      {
        password_user = password_db.split(";;;;;")[0];
        password_admin = password_db.split(";;;;;")[1];
        if (change_mode.contentEquals("admin"))
        {
          current_pwd = password_admin;
        }
        else
        {
          current_pwd = password_user;
        }
      }
      else
      {
        Toast.makeText(ctx, "problème de connexion la récupération du mot de passe.\n\rAssurez vous d'être connecté au réseau", Toast.LENGTH_LONG).show();
      }
    }
  }
}
