package com.lvovard.sportteam;

import java.util.Collections;
import java.util.List;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class NotifMyBroadcastReceiver extends BroadcastReceiver {
  SQLiteDatabase db;
  @Override
  public void onReceive(Context context, Intent intent) 
  {
    /////
    //log.i("myApp", "fonction appelee par timer - check DB...");
    //log.i("myApp", "get all DB connexion records");
    
    List<Club> clublist = Global.ConnexionGetClub(context);
    
    for (Club c:clublist)
    {
      if (c.mode.contentEquals("all") || c.mode.contentEquals("user"))
      {
        //checking convocation for the club
        getConvocationDateNotifMain taskconvoc = new getConvocationDateNotifMain(context,c);
        taskconvoc.execute(c.id_club,c.dateconvocation);
        //checking resultat for the club
        getResultatDateNotifMain taskresult = new getResultatDateNotifMain(context,c);
        taskresult.execute(c.id_club,c.dateresultat);
        //checking info for the club
        getInformationDateNotifMain taskinfo = new getInformationDateNotifMain(context,c);
        taskinfo.execute(c.id_club,c.dateinformation);
        getCategorieDateNotifMain taskcat = new getCategorieDateNotifMain(context,c);
        taskcat.execute(c.id_club,c.datecategorie);
        getPasswordMain taskpwd = new getPasswordMain(context,c);
        taskpwd.execute(c.id_club);
      }

    }
    //check error log file
    //if error caught, send to db and clearfile
    if (context.getExternalCacheDir() != null)
    {
      String res = Global.checkLogError(context);
      if (res != null)
      {
        Global.clearLogError(context);
        insertCrashMain taskcrash = new insertCrashMain(context);
        taskcrash.execute(res);
      }
      else
      {
        //if file is greater than 500 KB clear it
        if (Global.getLogErrorSize(context) > 500)
        {
          Global.clearLogError(context);
        }
      }
    }
  }
  
  private class getCategorieDateNotifMain extends GetLastCatDate
  {
    private Club club;
    private Context mcontext;
    public getCategorieDateNotifMain (Context mcontext,Club club)
    {
      this.mcontext = mcontext;
      this.club = club;
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
        //update db with new date
        Global.ConnexionUserUpdateDateCategorie(mcontext, club.id_club, listcat.get(0).date_record);
        for(Categorie c:listcat)
        {
          Intent notificationIntent = new Intent(mcontext,UserListCategoryActivity.class);
          notificationIntent.putExtra("clubid",this.club.id_club);
          //notificationIntent.putExtra("club",this.club.nom);
          //notificationIntent.putExtra("dep",this.dep);
          //notificationIntent.putExtra("sport",this.sport);
          notificationIntent.putExtra("cat",c.nom);
          notificationIntent.putExtra("state","ADD_CAT");
          //notificationIntent.putExtra("password",pwd);
          PendingIntent contentIntent = PendingIntent.getActivity(mcontext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
          NotificationCompat.Builder mBuilder =
              new NotificationCompat.Builder(mcontext)
              .setTicker(this.club.nom+" - nouvelle catégorie "+c.nom)
              .setSmallIcon(R.drawable.ic_launcher)
              .setContentTitle(this.club.nom+" - nouvelle catégorie "+c.nom)
              .setContentText("suivez la nouvelle catégorie "+c.nom)
          .setStyle(new NotificationCompat.BigTextStyle().bigText("Une nouvelle catégorie "+c.nom+" a été créée - cliquez ici pour l'ajouter à vos préférences ou faites glisser pour ignorer"));
  
          mBuilder.setContentIntent(contentIntent);
          mBuilder.setDefaults(Notification.DEFAULT_SOUND);
          mBuilder.setAutoCancel(true);
          mBuilder.setLights(Color.GREEN, 1, 1); // will blink
          NotificationManager mNotificationManager =(NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
          mNotificationManager.notify(Global.NOTIF_OFFSET_CAT+Integer.parseInt(c.id_cat), mBuilder.build());
        }
    }
      
    }
  }
  
  

  
  private class getConvocationDateNotifMain extends GetLastConvocationDate
  {
    private Context mContext;
    private Club club;

    public getConvocationDateNotifMain (Context context,Club club)
    {
      this.mContext = context;
      this.club = club;
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
        //update db with new date
        Global.ConnexionUserUpdateDateConvocation(mContext, club.id_club, listconv.get(0).date_record);
        //sort convoc list by dates
        Collections.sort(listconv, Convocation.ConvocationDateComparator);
        for(Convocation c:listconv)
        {
          //check if user is following the cat of the new convocation
          if (Global.ConnexionUserCatExist(mContext, club.id_club, c.equipe.split("-")[0]))
          {
            String longconvoctext = "la convoc est dispo pour le match contre "+c.adversaire;
            String shortconvoctext = "convoc contre "+c.adversaire+" ajoutee";
            String tickertext = club.nom+"-"+c.equipe +" nouvelle convocation contre "+c.adversaire;
            if (c.state.contentEquals("modified"))
            {
              longconvoctext = "la convoc pour le match contre "+c.adversaire+" a ete modifiee";
              shortconvoctext = "convoc contre "+c.adversaire+" modifiee";
              tickertext = club.nom+"-"+c.equipe +" convocation modifiée contre "+c.adversaire;
            }
            Intent notificationIntent = new Intent(mContext,UserListConvocationActivity.class);
            notificationIntent.putExtra("notif",true);
            notificationIntent.putExtra("club",this.club.nom);
            notificationIntent.putExtra("choice","Convocations");
            notificationIntent.putExtra("cat",c.equipe.split("-")[0]);
            notificationIntent.putExtra("equipe",c.equipe.replace("-", " "));
            notificationIntent.putExtra("sport",this.club.sport);
            notificationIntent.putExtra("clubid",this.club.id_club);
            notificationIntent.putExtra("convid",c.id_convoc);
            notificationIntent.putExtra("bkg",Global.getbkgpicture(this.club.sport));
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setTicker(tickertext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(club.nom+"-"+c.equipe)
                .setContentText(shortconvoctext)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(longconvoctext));
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(Color.GREEN, 1, 1); // will blink
            NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Global.NOTIF_OFFSET_CONVOC+Integer.parseInt(c.id_convoc), mBuilder.build());
          }
        }
      }
    }

  }
  
  private class getResultatDateNotifMain extends GetLastResultatDate
  {
    private Context mContext;
    private Club club;

    public getResultatDateNotifMain (Context context,Club club)
    {
      this.mContext = context;
      this.club = club;
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
          //update db with new date
          Global.ConnexionUserUpdateDateResultat(mContext, club.id_club, listresult.get(0).date_record);
          //sort result list by dates
          Collections.sort(listresult, Resultat.ResultatDateComparator);


        for(Resultat r:listresult)
        {
        //check if user is following the cat of the new convocation
          if (Global.ConnexionUserCatExist(mContext, club.id_club, r.id_equipe.split("-")[0]))
          {
            Intent notificationIntent = new Intent(mContext,UserListResultatActivity.class);
            notificationIntent.putExtra("notif",true);
            notificationIntent.putExtra("club",this.club.nom);
            notificationIntent.putExtra("choice","Resultats");
            notificationIntent.putExtra("cat",r.id_equipe.split("-")[0]);
            notificationIntent.putExtra("equipe",r.id_equipe.replace("-", " "));
            notificationIntent.putExtra("sport",this.club.sport);
            notificationIntent.putExtra("clubid",this.club.id_club);
            notificationIntent.putExtra("bkg",Global.getbkgpicture(this.club.sport));
            notificationIntent.putExtra("resultid",r.id_resultat);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setTicker(club.nom+"-"+r.id_equipe+" resultat contre "+r.adversaire)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(club.nom+"-"+r.id_equipe)
                .setContentText("resultat contre "+r.adversaire)
            .setStyle(new NotificationCompat.BigTextStyle().bigText("le resultat est dispo pour le match contre "+r.adversaire));
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(Color.GREEN, 1, 1); // will blink
            NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Global.NOTIF_OFFSET_RESULT+Integer.parseInt(r.id_resultat), mBuilder.build());
          }
        }
      }
    }

  }
  
  private class getInformationDateNotifMain extends GetLastInfoDate
  {
    private Context mContext;
    private Club club;

    public getInformationDateNotifMain (Context context,Club club)
    {
      this.mContext = context;
      this.club = club;
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
        //update db with new date
        Global.ConnexionUserUpdateDateInformation(mContext, club.id_club, listinfo.get(0).date_record);
        //sort result list by dates
        Collections.sort(listinfo, Info.InfoDateComparator);
          
        for(Info inf:listinfo)
        {
          if ( inf.id_equipe.contentEquals("all-all") || Global.ConnexionUserCatExist(mContext, club.id_club, inf.id_equipe.split("-")[0]))
          {
            String equipe = inf.id_equipe.replace("-", " ");
            String shorttext = "message pour les "+inf.id_equipe;
            String longtext = "nouveau message disponible pour les "+inf.id_equipe+": "+inf.objet;
            String title = club.nom+"-"+inf.id_equipe;
            String tickertxt = club.nom+" message pour "+inf.id_equipe;
            if (inf.id_equipe.contains("all-all"))
            {
              equipe = "info club";
              shorttext = "message pour tout le club";
              longtext = "nouveau message disponible pour tout le club: "+inf.objet;
              title = club.nom+"-toutes les categories"; 
              tickertxt = club.nom+" message pour toutes les categories";
            }
            else
            {
              if (inf.id_equipe.contains("-all"))
              {
                equipe = "info "+inf.id_equipe.split("-")[0];
                shorttext = "message pour tous les "+inf.id_equipe.split("-")[0]+"s";
                longtext = "nouveau message disponible pour tous les "+inf.id_equipe.split("-")[0]+"s: "+inf.objet;
                title = club.nom+"-tous les "+inf.id_equipe.split("-")[0];
                tickertxt = club.nom+" message pour tous les "+inf.id_equipe.split("-")[0];
              }
            }
          
            Intent notificationIntent = new Intent(mContext,UserListInfoActivity.class);
            notificationIntent.putExtra("notif",true);
            notificationIntent.putExtra("club",this.club.nom);
            notificationIntent.putExtra("choice","Information");
            notificationIntent.putExtra("cat",inf.id_equipe.split("-")[0]);
            notificationIntent.putExtra("equipe",equipe);
            notificationIntent.putExtra("sport",this.club.sport);
            notificationIntent.putExtra("clubid",this.club.id_club);
            notificationIntent.putExtra("bkg",Global.getbkgpicture(this.club.sport));
            notificationIntent.putExtra("infoid",inf.id_info);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setTicker(tickertxt)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(shorttext)
            .setStyle(new NotificationCompat.BigTextStyle().bigText(longtext));
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            mBuilder.setLights(Color.GREEN, 1, 1); // will blink
            NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(Global.NOTIF_OFFSET_INFO+Integer.parseInt(inf.id_info), mBuilder.build());
          }
        }
      }
    }

  }
  
  private class getPasswordMain extends getPassword
  {
    private Context mContext;
    private Club club;

    public getPasswordMain (Context ctx,Club club)
    {
      this.mContext = ctx;
      this.club = club;
    }
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(String password_db) 
    {
      String password_user = "";
      String password_admin = "";
      boolean password_user_changed = false;
      boolean password_admin_changed = false;
      //if we properly catch the passwords
      if (! password_db.contentEquals("nopasswordfound"))
      {
        password_user = password_db.split(";;;;;")[0];
        password_admin = password_db.split(";;;;;")[1];
        if ( club.mode.contentEquals("user") || club.mode.contentEquals("all") )
        {
          if (!Global.ConnexionGetUserPwd(mContext, club.id_club).contentEquals(password_user))
          {
            password_user_changed = true;
          }
        }
        if ( club.mode.contentEquals("admin") || club.mode.contentEquals("all") )
        {
          if (!Global.ConnexionGetAdminPwd(mContext, club.id_club).contentEquals(password_admin))
          {
            password_admin_changed = true;
          } 
        }
        
        //remove data from db table
        if (password_user_changed)
        {
          //save cat before remove them
          List<Categorie> listcat = Global.ConnexionGetCat(mContext,club.id_club);
          //remove cat from db
          Global.ConnexionRemoveUserClub(mContext, club.id_club);
          //build string with cat name
          String catbuffer = "";
          for(Categorie cat:listcat)
          {
            catbuffer = catbuffer + cat.nom + ";" ;
          }
          Intent notificationIntent = new Intent(mContext,BothModeAddClubActivity.class);
          notificationIntent.putExtra("clubid",this.club.id_club);
          notificationIntent.putExtra("club",this.club.nom);
          notificationIntent.putExtra("dep",this.club.dep);
          notificationIntent.putExtra("sport",this.club.sport);
          notificationIntent.putExtra("cat",catbuffer);
          notificationIntent.putExtra("state","UPDATE_PWD");
          PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
          NotificationCompat.Builder mBuilder =
              new NotificationCompat.Builder(mContext)
              .setSmallIcon(R.drawable.ic_launcher)
              .setContentTitle(this.club.nom+" - connexion")
              .setContentText("le mot de passe utilisateur a changé")
          .setStyle(new NotificationCompat.BigTextStyle().bigText("Le mot de passe utilisateur a changé - Demandez le nouveau mot de passe à l'admin de votre club et cliquez ici pour le mettre à jour et continuez de suivre les convocations, résultats et informations pour le club "+this.club.nom));
          //
          //.addAction (0,"ignorer", piDismiss)
          //.addAction (0,"mettre à jour", contentIntent);
          //
          mBuilder.setContentIntent(contentIntent);
          mBuilder.setDefaults(Notification.DEFAULT_SOUND);
          mBuilder.setAutoCancel(true);
          mBuilder.setLights(Color.GREEN, 1, 1); // will blink
          NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
          mNotificationManager.notify(Global.NOTIF_OFFSET_CLUB_PWD_USER+Integer.parseInt(this.club.id_club), mBuilder.build());
        }
        if (password_admin_changed)
        {
          Global.ConnexionRemoveAdminClub(mContext, club.id_club);
          Intent notificationIntent = new Intent(mContext,BothModeAddClubActivity.class);
          notificationIntent.putExtra("clubid",this.club.id_club);
          notificationIntent.putExtra("club",this.club.nom);
          notificationIntent.putExtra("dep",this.club.dep);
          notificationIntent.putExtra("sport",this.club.sport);
          notificationIntent.putExtra("state","UPDATE_PWD");
          PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
          NotificationCompat.Builder mBuilder =
              new NotificationCompat.Builder(mContext)
              .setSmallIcon(R.drawable.ic_launcher)
              .setContentTitle(this.club.nom+" - connexion")
              .setContentText("le mot de passe admin a changé")
          .setStyle(new NotificationCompat.BigTextStyle().bigText("Le mot de passe admin a changé - Demandez le nouveau mot de passe à l'admin de votre club et cliquez ici pour le mettre à jour et continuez de gérer les convocations, résultats et informations pour le club "+this.club.nom));
          //
          //.addAction (0,"ignorer", piDismiss)
          //.addAction (0,"mettre à jour", contentIntent);
          //
          mBuilder.setContentIntent(contentIntent);
          mBuilder.setDefaults(Notification.DEFAULT_SOUND);
          mBuilder.setAutoCancel(true);
          mBuilder.setLights(Color.GREEN, 1, 1); // will blink
          NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
          mNotificationManager.notify(Global.NOTIF_OFFSET_CLUB_PWD_ADMIN+Integer.parseInt(this.club.id_club), mBuilder.build());
        }
        
        //send notification to the user
        
        
      }
   

//        Intent notificationIntent = new Intent(mContext,MainActivity.class);
//        notificationIntent.putExtra("clubid",this.current_clubid);
//        notificationIntent.putExtra("club",this.current_club);
//        notificationIntent.putExtra("dep",this.current_dep);
//        notificationIntent.putExtra("sport",this.current_sport);
//        notificationIntent.putExtra("cat",catbuffer);
//        notificationIntent.putExtra("state","UPDATE_PWD");
        //notificationIntent.setAction("mettre à jour");
        
        //
//        Intent dismissIntent = new Intent(mContext, MainActivity.class);
//        dismissIntent.setAction("ignorer");
//        PendingIntent piDismiss = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Intent snoozeIntent = new Intent(mContext, MainActivity.class);
//        snoozeIntent.setAction("consulter");
//        PendingIntent piSnooze = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), snoozeIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //
        

      }
  }
  
  private class insertCrashMain extends insertCrash
  {
    private Context mContext;

    public insertCrashMain (Context ctx)
    {
      this.mContext = ctx;
    }
    
    @Override
    protected void onPreExecute()
    { 
    }

    @Override
    protected void onPostExecute(Boolean result) 
    {
      if (result)
      {
        Toast.makeText(mContext, "log envoyé à la DB crashinfo", Toast.LENGTH_LONG).show();
      }
    }
  }

} 