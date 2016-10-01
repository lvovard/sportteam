package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MyBroadcastReceiver extends BroadcastReceiver {
  SQLiteDatabase db;
  @Override
  public void onReceive(Context context, Intent intent) 
  {
    /////
    Log.i("myApp", "fonction appelee par timer - check DB...");
    Log.i("myApp", "get all DB dateconvocation records");
    //open DB
    db=context.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    //check for new/updated convocation
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM dateconvocation", null);
      if(c.getCount()==0)
      {
        Log.i("myApp", "get all DB dateconvocation records -> nothing");
      }
      else
      {
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
          buffer.append("sport  : "+c.getString(0)+"\n");
          buffer.append("club   : "+c.getString(1)+"\n");
          buffer.append("club_id: "+c.getString(2)+"\n\n");
          buffer.append("cat    : "+c.getString(3)+"\n");
          buffer.append("date   : "+c.getString(4)+"\n\n");
          int bkg = R.drawable.bluebkg;
          if (c.getString(0).equals("basketball"))
          {
            bkg = R.drawable.basketballbkg;
          }
          if (c.getString(0).equals("football"))
          {
            bkg = R.drawable.footballtest;
          }
          getConvocationDateNotifMain taskconvoc = new getConvocationDateNotifMain(context,c.getString(0),c.getString(1),c.getString(2),c.getString(3),bkg);
          taskconvoc.execute(c.getString(2),c.getString(3),c.getString(4));
        }
        Log.i("myApp","dateconvocation "+buffer.toString());
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        Log.i("myApp","table dateconvocation does not exist");  
      }
      else
      {
        Log.i("myApp","error when opening dateconvocation table : "+e.toString());
      }
    }
    //check for new/updated resultat
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM dateresultat", null);
      if(c.getCount()==0)
      {
        Log.i("myApp", "get all DB dateresultat records -> nothing");
      }
      else
      {
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
          buffer.append("sport  : "+c.getString(0)+"\n");
          buffer.append("club   : "+c.getString(1)+"\n");
          buffer.append("club_id: "+c.getString(2)+"\n\n");
          buffer.append("cat    : "+c.getString(3)+"\n");
          buffer.append("date   : "+c.getString(4)+"\n\n");
          int bkg = R.drawable.bluebkg;
          if (c.getString(0).equals("basketball"))
          {
            bkg = R.drawable.basketballbkg;
          }
          if (c.getString(0).equals("football"))
          {
            bkg = R.drawable.footballtest;
          }
          getResultatDateNotifMain taskresult = new getResultatDateNotifMain(context,c.getString(0),c.getString(1),c.getString(2),c.getString(3),bkg);
          taskresult.execute(c.getString(2),c.getString(3),c.getString(4));
        }
        Log.i("myApp","dateresultat "+buffer.toString());
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        Log.i("myApp","table dateresultat does not exist");  
      }
      else
      {
        Log.i("myApp","error when opening dateresultat table : "+e.toString());
      }
    }
    //check for new/updated resultat
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM dateinformation", null);
      if(c.getCount()==0)
      {
        Log.i("myApp", "get all DB dateinformation records -> nothing");
      }
      else
      {
        StringBuffer buffer=new StringBuffer();
        while(c.moveToNext())
        {
          buffer.append("sport  : "+c.getString(0)+"\n");
          buffer.append("club   : "+c.getString(1)+"\n");
          buffer.append("club_id: "+c.getString(2)+"\n\n");
          buffer.append("cat    : "+c.getString(3)+"\n");
          buffer.append("date   : "+c.getString(4)+"\n\n");
          int bkg = R.drawable.bluebkg;
          if (c.getString(0).equals("basketball"))
          {
            bkg = R.drawable.basketballbkg;
          }
          if (c.getString(0).equals("football"))
          {
            bkg = R.drawable.footballtest;
          }
          getInformationDateNotifMain taskinfo = new getInformationDateNotifMain(context,c.getString(0),c.getString(1),c.getString(2),c.getString(3),bkg);
          taskinfo.execute(c.getString(2),c.getString(3),c.getString(4));
        }
        Log.i("myApp","dateresultat "+buffer.toString());
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        Log.i("myApp","table dateinformation does not exist");  
      }
      else
      {
        Log.i("myApp","error when opening dateinformation table : "+e.toString());
      }
    }
    //check password
    try
    {
      Cursor c=db.rawQuery("SELECT DISTINCT clubid,club,password,sport,dep FROM connexion", null);
      if(c.getCount()==0)
      {
        Log.i("myApp", "get all DB connexion records -> nothing");
      }
      else
      {
        while(c.moveToNext())
        {
          Log.i("myApp","connexion clubid     : "+c.getString(0));
          Log.i("myApp","connexion club       : "+c.getString(1));
          Log.i("myApp","connexion password   : "+c.getString(2));
          Log.i("myApp","connexion sport      : "+c.getString(3));
          Log.i("myApp","connexion dep        : "+c.getString(4));
          getPasswordMain taskpwd = new getPasswordMain(context,c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4));
          taskpwd.execute(c.getString(0));
        }
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
  
  

  
  private class getConvocationDateNotifMain extends GetLastConvocationDate
  {
    private Context mContext;
    private String sport;
    private String club;
    private String clubid;
    private String cat;
    private String bkg;
    public getConvocationDateNotifMain (Context context,String sport,String club,String clubid,String cat,int bkg)
    {
      this.mContext = context;
      this.sport = sport;
      this.club = club;
      this.clubid = clubid;
      this.cat = cat;
      this.bkg = String.valueOf(bkg);
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
          Log.i("myApp","update dateconvocation with date="+listconv.get(0).date_record); 
          Log.i("myApp","update dateconvocation with clubid"+this.clubid); 
          Log.i("myApp","update dateconvocation with cat "+this.cat); 
          db.execSQL("UPDATE dateconvocation SET date='"+listconv.get(0).date_record+"' WHERE clubid='"+this.clubid+"' AND cat='"+this.cat+"' ");
          try
          {
            Cursor c=db.rawQuery("SELECT * FROM dateconvocation", null);
            if(c.getCount()==0)
            {
              Log.i("myApp", "get all DB dateconvocation records -> nothing");
            }
            else
            {
              StringBuffer buffer=new StringBuffer();
              while(c.moveToNext())
              {
                buffer.append("sport  : "+c.getString(0)+"\n");
                buffer.append("club   : "+c.getString(1)+"\n");
                buffer.append("club_id: "+c.getString(2)+"\n\n");
                buffer.append("cat    : "+c.getString(3)+"\n");
                buffer.append("date   : "+c.getString(4)+"\n\n");
              }
              Log.i("myApp","dateconvocation "+buffer.toString());
            }
          }
          catch(SQLException e)
          {
            if (e.toString().contains("no such table"))
            {
              Log.i("myApp","table dateconvocation does not exist");  
            }
            else
            {
              Log.i("myApp","error when opening dateconvocation table : "+e.toString());
            }
            
          }

        Collections.sort(listconv, Convocation.ConvocationDateComparator);
        for(Convocation c:listconv)
        {
            String longconvoctext = "la convoc est dispo pour le match contre "+c.adversaire;
            String shortconvoctext = "convoc contre "+c.adversaire+" ajoutee";
            if (c.state.contentEquals("modified"))
            {
              longconvoctext = "la convoc pour le match contre "+c.adversaire+" a ete modifiee";
              shortconvoctext = "convoc contre "+c.adversaire+" modifiee";
            }
            Intent notificationIntent = new Intent(mContext,ConvocationActivity.class);
            notificationIntent.putExtra("club",this.club);
            notificationIntent.putExtra("Convocations","Convocations");
            notificationIntent.putExtra("cat",this.cat);
            notificationIntent.putExtra("equipe",c.equipe.replace("-", " "));
            notificationIntent.putExtra("sport",this.sport);
            notificationIntent.putExtra("clubid",this.clubid);
            notificationIntent.putExtra("convid",c.id_convoc);
            notificationIntent.putExtra("bkg",this.bkg);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(club+"-"+c.equipe)
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
  
  private class getResultatDateNotifMain extends GetLastResultatDate
  {
    private Context mContext;
    private String sport;
    private String club;
    private String clubid;
    private String cat;
    private String bkg;
    public getResultatDateNotifMain (Context context,String sport,String club,String clubid,String cat,int bkg)
    {
      this.mContext = context;
      this.sport = sport;
      this.club = club;
      this.clubid = clubid;
      this.cat = cat;
      this.bkg = String.valueOf(bkg);
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
          Log.i("myApp","update dateresultat with date="+listresult.get(0).date_record); 
          Log.i("myApp","update dateresultat with clubid"+this.clubid); 
          Log.i("myApp","update dateresultat with cat "+this.cat); 
          db.execSQL("UPDATE dateresultat SET date='"+listresult.get(0).date_record+"' WHERE clubid='"+this.clubid+"' AND cat='"+this.cat+"' ");
          try
          {
            Cursor c=db.rawQuery("SELECT * FROM dateresultat", null);
            if(c.getCount()==0)
            {
              Log.i("myApp", "get all DB dateresultat records -> nothing");
            }
            else
            {
              StringBuffer buffer=new StringBuffer();
              while(c.moveToNext())
              {
                buffer.append("sport  : "+c.getString(0)+"\n");
                buffer.append("club   : "+c.getString(1)+"\n");
                buffer.append("club_id: "+c.getString(2)+"\n\n");
                buffer.append("cat    : "+c.getString(3)+"\n");
                buffer.append("date   : "+c.getString(4)+"\n\n");
              }
              Log.i("myApp","dateresultat "+buffer.toString());
            }
          }
          catch(SQLException e)
          {
            if (e.toString().contains("no such table"))
            {
              Log.i("myApp","table dateresultat does not exist");  
            }
            else
            {
              Log.i("myApp","error when opening dateresultat table : "+e.toString());
            }
            
          }

        Collections.sort(listresult, Resultat.ResultatDateComparator);
        for(Resultat r:listresult)
        {
            Intent notificationIntent = new Intent(mContext,ResultatActivity.class);
            notificationIntent.putExtra("club",this.club);
            notificationIntent.putExtra("Resultats","Resultats");
            notificationIntent.putExtra("cat",this.cat);
            notificationIntent.putExtra("equipe",r.id_equipe.replace("-", " "));
            notificationIntent.putExtra("sport",this.sport);
            notificationIntent.putExtra("clubid",this.clubid);
            notificationIntent.putExtra("bkg",this.bkg);
            notificationIntent.putExtra("resultid",r.id_resultat);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(club+"-"+r.id_equipe)
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
  
  private class getInformationDateNotifMain extends GetLastInfoDate
  {
    private Context mContext;
    private String sport;
    private String club;
    private String clubid;
    private String cat;
    private String bkg;
    public getInformationDateNotifMain (Context context,String sport,String club,String clubid,String cat,int bkg)
    {
      this.mContext = context;
      this.sport = sport;
      this.club = club;
      this.clubid = clubid;
      this.cat = cat;
      this.bkg = String.valueOf(bkg);
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
          Log.i("myApp","update dateinformation with date="+listinfo.get(0).date_record); 
          Log.i("myApp","update dateinformation with clubid"+this.clubid); 
          Log.i("myApp","update dateinformation with cat "+this.cat); 
          db.execSQL("UPDATE dateinformation SET date='"+listinfo.get(0).date_record+"' WHERE clubid='"+this.clubid+"' AND cat='"+this.cat+"' ");
          try
          {
            Cursor c=db.rawQuery("SELECT * FROM dateinformation", null);
            if(c.getCount()==0)
            {
              Log.i("myApp", "get all DB dateinformation records -> nothing");
            }
            else
            {
              StringBuffer buffer=new StringBuffer();
              while(c.moveToNext())
              {
                buffer.append("sport  : "+c.getString(0)+"\n");
                buffer.append("club   : "+c.getString(1)+"\n");
                buffer.append("club_id: "+c.getString(2)+"\n\n");
                buffer.append("cat    : "+c.getString(3)+"\n");
                buffer.append("date   : "+c.getString(4)+"\n\n");
              }
              Log.i("myApp","dateinformation "+buffer.toString());
            }
          }
          catch(SQLException e)
          {
            if (e.toString().contains("no such table"))
            {
              Log.i("myApp","table dateinformation does not exist");  
            }
            else
            {
              Log.i("myApp","error when opening dateinformation table : "+e.toString());
            }
            
          }

        Collections.sort(listinfo, Info.InfoDateComparator);
        for(Info inf:listinfo)
        {
            String equipe = inf.id_equipe.replace("-", " ");
            String shorttext = "message pour les "+inf.id_equipe;
            String longtext = "nouveau message disponible pour les "+inf.id_equipe+": "+inf.objet;
            String title = club+"-"+inf.id_equipe;
            if (inf.id_equipe.contains("all-all"))
            {
              equipe = "info club";
              shorttext = "message pour tout le club";
              longtext = "nouveau message disponible pour tout le club: "+inf.objet;
              title = club+"-toutes les categories"; 
            }
            else
            {
              if (inf.id_equipe.contains("-all"))
              {
                equipe = "info "+inf.id_equipe.split("-")[0];
                shorttext = "message pour tous les "+inf.id_equipe.split("-")[0]+"s";
                longtext = "nouveau message disponible pour tous les "+inf.id_equipe.split("-")[0]+"s: "+inf.objet;
                title = club+"-tous les "+inf.id_equipe.split("-")[0];
              }
            }
          
            Intent notificationIntent = new Intent(mContext,InfoActivity.class);
            notificationIntent.putExtra("club",this.club);
            notificationIntent.putExtra("Information","Information");
            notificationIntent.putExtra("cat",this.cat);
            notificationIntent.putExtra("equipe",equipe);
            notificationIntent.putExtra("sport",this.sport);
            notificationIntent.putExtra("clubid",this.clubid);
            notificationIntent.putExtra("bkg",this.bkg);
            notificationIntent.putExtra("infoid",inf.id_info);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
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
  
  private class getPasswordMain extends getPassword
  {
    private Context mContext;
    private String current_password;
    private String current_club;
    private String current_clubid;
    private String current_sport;
    private String current_dep;

    public getPasswordMain (Context ctx,String current_clubid,String current_club,String current_pwd,String current_sport,String current_dep)
    {
      this.mContext = ctx;
      this.current_password = current_pwd;
      this.current_club = current_club;
      this.current_clubid = current_clubid;
      this.current_sport = current_sport;
      this.current_dep = current_dep;
    }
    @Override
    protected void onPreExecute()
    { 
    
    }

    @Override
    protected void onPostExecute(String password_db) 
    {
      if ( (! current_password.contentEquals(password_db)) && (! password_db.contentEquals("nopasswordfound")) )
      {
        String catbuffer="";
        try
        {
          Cursor c=db.rawQuery("SELECT cat FROM connexion WHERE clubid="+this.current_clubid, null);
          if(c.getCount()==0)
          {
            Log.i("myApp", "get all DB connexion records -> nothing");
          }
          else
          {
            
            while(c.moveToNext())
            {
              catbuffer = catbuffer+c.getString(0)+";";
            }
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
        Log.i("myApp", "current pwd ="+current_password);
        Log.i("myApp", "password_db ="+password_db);
        Log.i("myApp", "PASSWORD HAS CHANGED");
        Log.i("myApp", "le mot de passe pour "+this.current_club+" a changé!!!!!");
        Global.db.execSQL("DELETE FROM connexion WHERE clubid='"+this.current_clubid+"' ");
        Global.db.execSQL("DELETE FROM dateresultat WHERE clubid='"+this.current_clubid+"' ");
        Global.db.execSQL("DELETE FROM dateconvocation WHERE clubid='"+this.current_clubid+"' ");
        Global.db.execSQL("DELETE FROM dateinformation WHERE clubid='"+this.current_clubid+"' ");
        Intent notificationIntent = new Intent(mContext,MainActivity.class);
        notificationIntent.putExtra("updatepwd_clubid",this.current_clubid);
        notificationIntent.putExtra("updatepwd_club",this.current_club);
        notificationIntent.putExtra("updatepwd_dep",this.current_dep);
        notificationIntent.putExtra("updatepwd_sport",this.current_sport);
        notificationIntent.putExtra("updatepwd_cat",catbuffer);
        notificationIntent.putExtra("updatepwd_state","UPDATE_PWD");
//        notificationIntent.putExtra("Information","Information");
//        notificationIntent.putExtra("cat",this.cat);
//        notificationIntent.putExtra("equipe",equipe);
//        notificationIntent.putExtra("sport",this.sport);
//        notificationIntent.putExtra("clubid",this.clubid);
//        notificationIntent.putExtra("bkg",this.bkg);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,(int) (Math.random() * 100), notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(mContext)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(this.current_club+" - connexion")
            .setContentText("le mot de passe a changé")
        .setStyle(new NotificationCompat.BigTextStyle().bigText("Le mot de passe a changé - Demandez le nouveau mot de passe à l'admin de votre club et cliquez ici pour le mettre à jour et continuez de suivre les convocations, résultats et informations pour le club "+this.current_club));
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        mBuilder.setLights(Color.GREEN, 1, 1); // will blink
        NotificationManager mNotificationManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
      }
    }
  }

} 