package com.lvovard.sportteam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Global
{
  
  public static final String MyPREFERENCES = "SPORTTEAM" ;
  public static final String currentcat = "currentcat";
  public static final String currentequipe = "currentequipe";
  public static final String currentchoice = "current_choice";
  public static final String currentbkgpicture = "currentbkgpicture";
  public static final String sportpicture = "sportpicture";
  
  public static final String adminclubsport = "adminclubsport";
  public static final String adminclubdep = "adminclubdep";
  public static final String adminclubname = "adminclubname";
  public static final String adminclubid = "adminclubid";
  public static final String adminclubpwduser = "adminclubpwduser";
  public static final String adminclubpwdadmin = "adminclubpwdadmin";
  public static final String adminclubmode = "adminclubmode";
  
  public static final String userclubsport = "userclubsport";
  public static final String userclubdep = "userclubdep";
  public static final String userclubname = "userclubname";
  public static final String userclubid = "userclubid";
  public static final String userclubpwduser = "userclubpwduser";
  public static final String userclubpwdadmin = "userclubpwdadmin";
  public static final String userclubmode = "userclubmode";

  
  
  static boolean logfilecreated = false;
  
  //private static Club current_admin_club;
  //private static Club current_user_club;
  
  static final String SPORTTEAM_VERSION        = "1.0";//= 1200000;
  static final int TIME_MN_FOR_NOTIF_CHECK     = 10;
  static final int TIME_MS_FOR_NOTIF_CHECK     = 1000*60*TIME_MN_FOR_NOTIF_CHECK;
  static final int NOTIF_OFFSET_CONVOC         = 0;
  static final int NOTIF_OFFSET_RESULT         = 100000000;
  static final int NOTIF_OFFSET_INFO           = 200000000;
  static final int NOTIF_OFFSET_CAT            = 300000000;
  static final int NOTIF_OFFSET_CLUB_PWD_USER  = 400000000;
  static final int NOTIF_OFFSET_CLUB_PWD_ADMIN = 500000000;
  
  static final String URL_SQL_DATABASE = "http://www.sportteam.890m.com";
  
  //add club with user permission
  static boolean ConnexionAddUserClub(Context ctx,String sport,String dep,String club,String clubid,String cat,String pwduser)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    db.execSQL("CREATE TABLE IF NOT EXISTS connexion(sport VARCHAR,dep VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,password VARCHAR,password_admin VARCHAR,mode VARCHAR,dateconvocation VARCHAR,dateresultat VARCHAR,dateinformation VARCHAR,datecategorie VARCHAR);");
    //check if club, if yes update cat and mode
    String cattoadd;
    if (cat.contentEquals(""))
    {
      cattoadd = cat;
    }
    else
    {
      cattoadd = cat+";";
    }
    try
    {
      Cursor c=db.rawQuery("SELECT mode,cat FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.execSQL("INSERT INTO connexion VALUES('"+sport+"','"+dep+"','"+club+"','"+clubid+"','"+cattoadd+"','"+pwduser+"','','user','nodate','nodate','nodate','nodate');");
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(0);
          //String currentcat = c.getString(1);
          String newmode = "user";
          //String newcat = currentcat + cattoadd;
          if (currentmode.contentEquals("admin") || currentmode.contentEquals("all"))
          {
            newmode = "all";
          }
          db.execSQL("UPDATE connexion SET mode='"+newmode+"',cat='"+cattoadd+"' WHERE clubid='"+clubid+"' "); 
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return false;
  }
  
  static void ConnexionUserUpdateDateConvocation(Context ctx,String clubid,String newdate)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET dateconvocation='"+newdate+"' WHERE clubid='"+clubid+"' "); 
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  static void ConnexionUserUpdateDateInformation(Context ctx,String clubid,String newdate)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET dateinformation='"+newdate+"' WHERE clubid='"+clubid+"' "); 
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  static void ConnexionUserUpdateDateResultat(Context ctx,String clubid,String newdate)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET dateresultat='"+newdate+"' WHERE clubid='"+clubid+"' ");
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  static void ConnexionUserUpdateDateCategorie(Context ctx,String clubid,String newdate)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET datecategorie='"+newdate+"' WHERE clubid='"+clubid+"' ");
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  static String ConnexionUserGetDateConvocation(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT dateconvocation FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return null;
      }
      else
      {
        while(c.moveToNext())
        {
          db.close();
          return c.getString(0);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return null;
  }
  
  static String ConnexionUserGetDateInformation(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT dateinformation FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return null;
      }
      else
      {
        while(c.moveToNext())
        {
          db.close();
          return c.getString(0);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return null;
  }
  
  static String ConnexionUserGetDateResultat(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT dateresultat FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return null;
      }
      else
      {
        while(c.moveToNext())
        {
          db.close();
          return c.getString(0);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return null;
  }
  
  static String ConnexionUserGetDateCategorie(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT datecategorie FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return null;
      }
      else
      {
        while(c.moveToNext())
        {
          db.close();
          return c.getString(0);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return null;
  }


  //add club with admin permission
  static boolean ConnexionAddAdminClub(Context ctx,String sport,String dep,String club,String clubid,String pwdadmin)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("CREATE TABLE IF NOT EXISTS connexion(sport VARCHAR,dep VARCHAR,club VARCHAR,clubid VARCHAR,cat VARCHAR,password VARCHAR,password_admin VARCHAR,mode VARCHAR,dateconvocation VARCHAR,dateresultat VARCHAR,dateinformation VARCHAR,datecategorie VARCHAR);");
      //check if club, if yes update mode
      Cursor c=db.rawQuery("SELECT mode FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.execSQL("INSERT INTO connexion VALUES('"+sport+"','"+dep+"','"+club+"','"+clubid+"','','','"+pwdadmin+"','admin','nodate','nodate','nodate','nodate');");
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(0);
          String newmode = "admin";
          if (currentmode.contentEquals("user"))
          {
            newmode = "all";
          }
          db.execSQL("UPDATE connexion SET mode='"+newmode+"',password_admin='"+pwdadmin+"' WHERE clubid='"+clubid+"' "); 
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return false;
  }
  
  //check if cat is already followed
  static boolean ConnexionUserCatExist(Context ctx,String clubid,String cat)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT cat FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return false;
      }
      else
      {
        while(c.moveToNext())
        {
          if ( c.getString(0).contains(cat) )
          {
            db.close();
            return true;
          }
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return false;
  }
  
  //get cat for one club
  static List<Categorie> ConnexionGetCat(Context ctx,String clubid)
  {
    List<Categorie> catlist = new ArrayList<Categorie>();
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT cat FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return catlist;
      }
      else
      {
        while(c.moveToNext())
        {
          String[] catstring = c.getString(0).split(";");
          for (int i=0; i < catstring.length; i++) 
          {
            Categorie cat = new Categorie(catstring[i], "", "", "");
            catlist.add(cat);  
          }
          db.close();
          Collections.sort(catlist, Categorie.CategorieNameComparator);
          return catlist;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return catlist;
  }
  
  //get all club
  static List<Club> ConnexionGetClub(Context ctx)
  {
    List<Club> clublist = new ArrayList<Club>();
    List<Categorie> catlist = new ArrayList<Categorie>();
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion ", null);
      if(c.getCount()==0)
      {
        db.close();
        return clublist;
      }
      else
      {
        while(c.moveToNext())
        {
          String[] catstring = c.getString(4).split(";");
          for (int i=0; i < catstring.length; i++) 
          {
            Categorie cat = new Categorie(catstring[i], "", "", "");
            catlist.add(cat);
          }
          Collections.sort(catlist, Categorie.CategorieNameComparator);
          String sport = c.getString(0);
          String dep = c.getString(1);
          String name = c.getString(2);
          String id = c.getString(3);
          String pwd_user = c.getString(5);
          String pwd_admin = c.getString(6);
          String mode = c.getString(7);
          String dateconv = c.getString(8);
          String dateres = c.getString(9);
          String dateinfo = c.getString(10);
          String datecat = c.getString(11);
          Club club = new Club(sport,dep,name,id,pwd_user,pwd_admin,mode,catlist,dateconv,dateres,dateinfo,datecat);
          clublist.add(club);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    Collections.sort(clublist, Club.ClubNameComparator);
    return clublist;
  }
  
  //get user club
  static List<Club> ConnexionGetUserClub(Context ctx)
  {
    List<Club> clublist = new ArrayList<Club>();
    List<Categorie> catlist = new ArrayList<Categorie>();
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion WHERE mode='user' OR mode='all'", null);
      if(c.getCount()==0)
      {
        db.close();
        return clublist;
      }
      else
      {
        while(c.moveToNext())
        {
          String[] catstring = c.getString(4).split(";");
          for (int i=0; i < catstring.length; i++) 
          {
            Categorie cat = new Categorie(catstring[i], "", "", "");
            catlist.add(cat);
          }
          Collections.sort(catlist, Categorie.CategorieNameComparator);
          String sport = c.getString(0);
          String dep = c.getString(1);
          String name = c.getString(2);
          String id = c.getString(3);
          String pwd_user = c.getString(5);
          String pwd_admin = c.getString(6);
          String mode = c.getString(7);
          String dateconv = c.getString(8);
          String dateres = c.getString(9);
          String dateinfo = c.getString(10);
          String datecat = c.getString(11);
          Club club = new Club(sport,dep,name,id,pwd_user,pwd_admin,mode,catlist,dateconv,dateres,dateinfo,datecat);
          clublist.add(club);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    Collections.sort(clublist, Club.ClubNameComparator);
    return clublist;
  }
  
  //get admin club
  static List<Club> ConnexionGetAdminClub(Context ctx)
  {
    List<Club> clublist = new ArrayList<Club>();
    List<Categorie> catlist = new ArrayList<Categorie>();
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion WHERE mode='admin' OR mode='all'", null);
      if(c.getCount()==0)
      {
        db.close();
        return clublist;
      }
      else
      {
        while(c.moveToNext())
        {
          String[] catstring = c.getString(4).split(";");
          for (int i=0; i < catstring.length; i++) 
          {
            Categorie cat = new Categorie(catstring[i], "", "", "");
            catlist.add(cat);
          }
          Collections.sort(catlist, Categorie.CategorieNameComparator);
          String sport = c.getString(0);
          String dep = c.getString(1);
          String name = c.getString(2);
          String id = c.getString(3);
          String pwd_user = c.getString(5);
          String pwd_admin = c.getString(6);
          String mode = c.getString(7);
          String dateconv = c.getString(8);
          String dateres = c.getString(9);
          String dateinfo = c.getString(10);
          String datecat = c.getString(11);
          Club club = new Club(sport,dep,name,id,pwd_user,pwd_admin,mode,catlist,dateconv,dateres,dateinfo,datecat);
          clublist.add(club);
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    Collections.sort(clublist, Club.ClubNameComparator);
    return clublist;
  }
  
  //update admin pwd
  static void ConnexionUpdateAdminPwd(Context ctx,String id_club,String newadminpwd)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET password_admin='"+newadminpwd+"' WHERE clubid='"+id_club+"' ");
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  //update user pwd
  static void ConnexionUpdateUserPwd(Context ctx,String id_club,String newuserpwd)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      db.execSQL("UPDATE connexion SET password='"+newuserpwd+"' WHERE clubid='"+id_club+"' ");
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  //check if there is at least one club in user mode
  static boolean ConnexionHasUserClub(Context ctx)
  {
    boolean ret = false;
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion WHERE mode='user' OR mode='all'", null);
      if(c.getCount()==0)
      {
        ret = false;
      }
      else
      {
        ret = true;
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return ret;
  }
  
  //check if there is at least one club in admin mode
  static boolean ConnexionHasAdminClub(Context ctx)
  {
    boolean ret = false;
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion WHERE mode='admin' OR mode='all'", null);
      if(c.getCount()==0)
      {
        ret = false;
      }
      else
      {
        ret = true;  
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return ret;
  }
  
  //remove club from the user connexion
  static boolean ConnexionRemoveUserClub(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      //check if club mode
      Cursor c=db.rawQuery("SELECT mode FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(0);
          String newmode = "admin";
          //if mode = user, remove from the table
          if (currentmode.contentEquals("user"))
          {
            db.execSQL("DELETE FROM connexion WHERE clubid='"+clubid+"' ");
          }
          else
          {
            //if mode = all or admin, update mode from all/admin to admin
            db.execSQL("UPDATE connexion SET mode='"+newmode+"' WHERE clubid='"+clubid+"' ");
          }
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return true;
  }
  
  //remove club from the user connexion
  static boolean ConnexionRemoveUserAllClub(Context ctx)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      //check if club mode
      Cursor c=db.rawQuery("SELECT clubid,mode FROM connexion ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(1);
          String newmode = "admin";
          //if mode = user, remove from the table
          if (currentmode.contentEquals("user"))
          {
            db.execSQL("DELETE FROM connexion WHERE clubid='"+c.getString(0)+"' ");
          }
          else
          {
            //if mode = all or admin, update mode from all/admin to admin
            db.execSQL("UPDATE connexion SET mode='"+newmode+"',cat='',password='',dateinformation='nodate',dateresultat='nodate',dateconvocation='nodate',datecategorie='nodate' WHERE clubid='"+c.getString(0)+"' ");
          }
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return true;
  }
  
  //remove all club from the admin connexion
  static boolean ConnexionRemoveAdminAllClub(Context ctx)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      //check if club mode
      Cursor c=db.rawQuery("SELECT clubid,mode FROM connexion ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(1);
          String newmode = "user";
          //if mode = admin, remove from the table
          if (currentmode.contentEquals("admin"))
          {
            db.execSQL("DELETE FROM connexion WHERE clubid='"+c.getString(0)+"' ");
          }
          else
          {
            //if mode = all or admin, update mode from all/admin to admin
            db.execSQL("UPDATE connexion SET mode='"+newmode+"',password_admin='' WHERE clubid='"+c.getString(0)+"' ");
          }
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return true;
  }
  
  //remove club from the admin connexion
  static boolean ConnexionRemoveAdminClub(Context ctx,String clubid)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      //check if club mode
      Cursor c=db.rawQuery("SELECT mode FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String currentmode = c.getString(0);
          String newmode = "user";
          //if mode = admin, remove from the table
          if (currentmode.contentEquals("admin"))
          {
            db.execSQL("DELETE FROM connexion WHERE clubid='"+clubid+"' ");
          }
          else
          {
            //if mode = user or admin, update mode from all/user to user
            db.execSQL("UPDATE connexion SET mode='"+newmode+"' WHERE clubid='"+clubid+"' ");
          }
          db.close();
          return true;
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return true;
  }  
  
  //remove cat from club 
  static boolean ConnexionRemoveUserCat(Context ctx,String clubid,String cat)
  {
    
    if (Global.ConnexionUserCatExist(ctx, clubid, cat))
    {
      SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
      Cursor c=db.rawQuery("SELECT cat FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String[] catstring = c.getString(0).split(";");
          String newcat = "";
          for (int i=0; i < catstring.length; i++) 
          {
            if (! catstring[i].contentEquals(cat))
            {
              newcat = newcat + catstring[i] + ";";
            }
          }
          if (newcat != null && newcat.length() > 0 && newcat.charAt(newcat.length()-1)==';') {
            newcat = newcat.substring(0, newcat.length()-1);
          }
          db.execSQL("UPDATE connexion SET cat='"+newcat+"' WHERE clubid='"+clubid+"' ");
          db.close();
          return true;
        }
      }
    }
    //already exist
    else
    {
      return true;
    }
    return false;
  }
  
  //add cat to club 
  static boolean ConnexionAddUserCat(Context ctx,String clubid,String cat)
  {
    
    if (Global.ConnexionUserCatExist(ctx, clubid, cat) == false)
    {
      SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
      Cursor c=db.rawQuery("SELECT cat,mode FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String newcat = c.getString(0) + ";" + cat;
          if (c.getString(0).contentEquals(""))
          {
            newcat = cat;
          }
          String newmode = "all";
          if (c.getString(1).contentEquals("user"))
          {
            newmode = "user";
          }
          db.execSQL("UPDATE connexion SET cat='"+newcat+"',mode='"+newmode+"' WHERE clubid='"+clubid+"' ");
          db.close();
          return true;
        }
      }
    }
    //already exist
    else
    {
      return true;
    }
    return false;
  }
  
  static boolean ConnexionAddUserCatWithPwd(Context ctx,String clubid,String cat,String pwd)
  {
    
    if (Global.ConnexionUserCatExist(ctx, clubid, cat) == false)
    {
      SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
      Cursor c=db.rawQuery("SELECT cat,mode FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        db.close();
        return true;
      }
      else
      {
        while(c.moveToNext())
        {
          String newcat = c.getString(0) + ";" + cat;
          if (c.getString(0).contentEquals(""))
          {
            newcat = cat;
          }
          String newmode = "all";
          if (c.getString(1).contentEquals("user"))
          {
            newmode = "user";
          }
          db.execSQL("UPDATE connexion SET cat='"+newcat+"',mode='"+newmode+"',password='"+pwd+"' WHERE clubid='"+clubid+"' ");
          db.close();
          return true;
        }
      }
    }
    //already exist
    else
    {
      return true;
    }
    return false;
  }
  
  //add cat to club 
  static void ConnexionPrintDataBase(Context ctx)
  {
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT * FROM connexion ", null);
      if(c.getCount()==0)
      {
        //log.i("myApp", "DATA BASE CONNEXION IS EMPTY");
      }
      else
      {
        while(c.moveToNext())
        {
          //log.i("myApp", "DATA BASE CONNEXION DATA\n\r");
          String data = "";
          for (int i=0;i<c.getColumnCount();i++)
          {
            data = data + " - " + c.getString(i);
          }
          //log.i("myApp", data + "\n\r");
        }
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
  }
  
  //get user pwd 
  static String ConnexionGetUserPwd(Context ctx,String clubid)
  {
    String pwd = null;
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT password FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        //log.i("myApp", "no user pwd found for club id ="+clubid);
      }
      else
      {
        while(c.moveToNext())
        {
          pwd = c.getString(0); 
        }    
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return pwd;
  }
  
  //get admin pwd 
  static String ConnexionGetAdminPwd(Context ctx,String clubid)
  {
    String pwd = null;
    SQLiteDatabase db = ctx.openOrCreateDatabase("dateDB", Context.MODE_PRIVATE, null);
    try
    {
      Cursor c=db.rawQuery("SELECT password_admin FROM connexion WHERE clubid='"+clubid+"' ", null);
      if(c.getCount()==0)
      {
        //log.i("myApp", "no admin pwd found for club id ="+clubid);
      }
      else
      {
        while(c.moveToNext())
        {
          pwd = c.getString(0); 
        }    
      }
    }
    catch(SQLException e)
    {
      if (e.toString().contains("no such table"))
      {
        //log.i("myApp","table connexion does not exist");  
      }
      else
      {
        //log.i("myApp","error when opening connexion table : "+e.toString());
      }
    }
    db.close();
    return pwd;
  }
  
  static int getbkgpicture(String sport)
  {
    int bkg = R.drawable.gazon;
    if (sport.equals("basketball"))
    {
      bkg = R.drawable.terrainbasket;
    }
    if (sport.equals("football"))
    {
      //bkg = R.drawable.terrainfootball;
    }
    if (sport.equals("handball"))
    {
      bkg = R.drawable.terrainhandball;
    }
    if (sport.equals("rugby"))
    {
      bkg = R.drawable.terrainrugby;
    }
    if (sport.equals("volleyball"))
    {
      bkg = R.drawable.terrainvolley;
    }
    return bkg;
  }
  
  static String checkLogError(Context ctx)
  {
  //check if previous launch contains error
    File[] paths = ctx.getExternalCacheDir().listFiles();
    String crash_content = "";
    if (paths.length == 1)
    {
      File file = new File(paths[0].getAbsolutePath());
      try {
          Scanner scanner = new Scanner(file);
          //now read the file line by line...
          int lineNum = 0;
          Pattern p = Pattern.compile("^E.*AndroidRuntime.*sportteam", Pattern.CASE_INSENSITIVE);
          while (scanner.hasNextLine()) 
          {
            String line = scanner.nextLine();
            lineNum++;
            Matcher m = p.matcher(line);
            if (m.find())
            { 
              //log.i("myApp", line );
              //log.i("myApp", "line num = "+lineNum );
              crash_content = crash_content+line+"\n";
            }
          }
          scanner.close();
      } catch(FileNotFoundException e) { 
          //handle this
      }
    }
    if (!crash_content.contentEquals(""))
    {
      return crash_content;
    }
    else
    {
      return null;
    }
  }
  
  static void deleteLogError(Context ctx)
  {
    if (ctx.getExternalCacheDir().isDirectory())
    {
      for (File child : ctx.getExternalCacheDir().listFiles())
      {
        child.delete();
      }
    }
  }
  
  static int getLogErrorSize(Context ctx)
  {
    File[] paths = ctx.getExternalCacheDir().listFiles();
    if (paths.length == 1)
    {
      File file = new File(paths[0].getAbsolutePath());
      if(file.exists())
      {
        double bytes = file.length();
        return (int) (bytes / 1024);
      }else
      {
        return 0;
      }
    }
    return 0;
  }
  
  static void clearLogError(Context ctx)
  {
    File[] paths = ctx.getExternalCacheDir().listFiles();
    if (paths.length == 1)
    {
      File file = new File(paths[0].getAbsolutePath());
      if(file.exists())
      {
        try
        {
          FileOutputStream fos = new FileOutputStream(file);
          fos.close( );
        } catch (FileNotFoundException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
      }
    }
  }
  
  static String getCurrentCat(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    return settings.getString(currentcat, null);
  }
  
  static void setCurrentCat(Context ctx,String cat)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(currentcat, cat);
    editor.commit();
  }
  
  static String getCurrentEquipe(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    return settings.getString(currentequipe, null);
  }
  
  static void setCurrentEquipe(Context ctx,String eq)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(currentequipe, eq);
    editor.commit();
  }
  
  static String getCurrentChoice(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    return settings.getString(currentchoice, null);
  }
  
  static void setCurrentChoice(Context ctx,String c)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(currentchoice, c);
    editor.commit();
  }
  
  static int getCurrentBkgPicture(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    return settings.getInt(currentbkgpicture, 0);
  }
  
  static void setCurrentBkgPicture(Context ctx,int p)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putInt(currentbkgpicture, p);
    editor.commit();
  }
  
  static int getSportPicture(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    return settings.getInt(sportpicture, 0);
  }
  
  static void setSportPicture(Context ctx,int p)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putInt(sportpicture, p);
    editor.commit();
  }
  
  static Club getCurrentAdminClub(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    List<Categorie> catlist = new ArrayList<Categorie>();
    return new Club(
        settings.getString(adminclubsport, null),
        settings.getString(adminclubdep, null),
        settings.getString(adminclubname, null),
        settings.getString(adminclubid, null),
        settings.getString(adminclubpwduser, null),
        settings.getString(adminclubpwdadmin, null),
        settings.getString(adminclubmode, null),
        catlist,
        null,
        null,
        null,
        null
        );
  }
  
  static void setCurrentAdminClub(Context ctx,Club club)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(adminclubsport, club.sport);
    editor.putString(adminclubdep, club.dep);
    editor.putString(adminclubname, club.nom);
    editor.putString(adminclubid, club.id_club);
    editor.putString(adminclubpwduser, club.password);
    editor.putString(adminclubpwdadmin, club.password_admin);
    editor.putString(adminclubmode, club.mode);
    editor.commit();
  }
  
  static Club getCurrentUserClub(Context ctx)
  {
    SharedPreferences settings = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    List<Categorie> catlist = new ArrayList<Categorie>();
    return new Club(
        settings.getString(userclubsport, null),
        settings.getString(userclubdep, null),
        settings.getString(userclubname, null),
        settings.getString(userclubid, null),
        settings.getString(userclubpwduser, null),
        settings.getString(userclubpwdadmin, null),
        settings.getString(userclubmode, null),
        catlist,
        null,
        null,
        null,
        null
        );
  }
  
  static void setCurrentUserClub(Context ctx,Club club)
  {
    SharedPreferences sharedpreferences = ctx.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString(userclubsport, club.sport);
    editor.putString(userclubdep, club.dep);
    editor.putString(userclubname, club.nom);
    editor.putString(userclubid, club.id_club);
    editor.putString(userclubpwduser, club.password);
    editor.putString(userclubpwdadmin, club.password_admin);
    editor.putString(userclubmode, club.mode);
    editor.commit();
  }
  
  static void raisecrash(String msg)
  {
    throw new RuntimeException(msg);
  }
  

}
