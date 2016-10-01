package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.util.Log;
import android.widget.Button;

public class Record
{

  String sport = null;
  String departement = null;
  String club = null;
  String password = null;
  String id_club = null;
  List<String> category = new ArrayList<String>();
  boolean addedInList = false;
  Button btn ;
  int id;
  
  static int id_appli = 1;
  
  static Record current_record = null;
  
  static List<Record> recordlist = new ArrayList<Record>();

  public Record(String sp, String dep, String clb, String pwd, String cat, String idclub)
  {
    sport = sp;
    departement = dep;
    club = clb;
    password = pwd;
    id_club = idclub;
    if (clubExists(clb) == false)
    {
      Log.i("myApp", clb+" does not exist: create new entry");
      category.add(cat);
      this.id = id_appli;
      id_appli++;
      recordlist.add(this);
      Collections.sort(recordlist, Record.RecordNameComparator);
      addedInList = true;
    }
    else
    {
      Log.i("myApp", clb+" exists");
      if (categoryExists(cat) == false)
      {
        Log.i("myApp", cat+" does not exist: update existing entry");
        Collections.sort(recordlist, Record.RecordNameComparator);
        addedInList = true;
      }
      else
      {
        Log.i("myApp", cat+" for "+clb+" already exists - do nothing");
      }
    }
    Log.i("myApp", "display list starts");
    String sep = " - ";
    for(Record rcd:recordlist)
    {
      Log.i("myApp", rcd.getSport()+sep+rcd.getClub()+sep+rcd.getDepartement()+sep+rcd.getCategory());
    }
    Log.i("myApp", "display list ends");
  }
  
  
  private boolean clubExists(String clb)
  {
    for(Record rcd:recordlist)
    {
      if ( this.sport.equals(rcd.getSport()) )
      {
        if ( this.departement.equals(rcd.getDepartement()) )
        {
          if ( this.club.equals(rcd.getClub()) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private boolean categoryExists(String cat)
  {
    for(Record rcd:recordlist)
    {
      if ( this.sport.equals(rcd.getSport()) )
      {
        if ( this.departement.equals(rcd.getDepartement()) )
        {
          if ( this.club.equals(rcd.getClub()) )
          {
            if ( rcd.getCategory().contains(cat))
            {
              return true;
            }
            else
            {
              rcd.category.add(cat);
              Collections.sort(rcd.category);
            }
          }
        }
      }
    }
    return false;
  }
  
  static boolean RemoveRecord(String clubid)
  {
    for(Record rcd:recordlist)
    {
      if ( rcd.id_club.contentEquals(clubid))
      {
        recordlist.remove(rcd);
        return true;
      }
      
    }
    return false;
  }
  
  static boolean RemoveRecord(String clubid,String cat)
  {
    for(Record rcd:recordlist)
    {
      if ( rcd.id_club.contentEquals(clubid) && rcd.getCategory().contains(cat) )
      {
        recordlist.remove(rcd);
        current_record.category.remove(cat);
        return true;
      }
      
    }
    return false;
  }
  
  public boolean hasBeenAddedToTheList()
  {
    return addedInList;
  }
  
  public String getSport()
  {
    return sport;
  }
  
  public String getIdClub()
  {
    return id_club;
  }
  
  public String getDepartement()
  {
    return departement;
  }
  
  public String getClub()
  {
    return club;
  }
  
  public String getPassword()
  {
    return password;
  }
  
  public List<String> getCategory()
  {
    return category;
  }
  
  public void setButton(Button bt)
  {
    this.btn = bt;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public static Comparator<Record> RecordNameComparator = new Comparator<Record>() 
  {

    public int compare(Record r1, Record r2) 
    {
      String r1name = r1.getSport()+r1.getDepartement()+r1.getClub()+r1.getCategory();
      String r2name = r2.getSport()+r2.getDepartement()+r2.getClub()+r2.getCategory();

      //ascending order
      return r1name.compareTo(r2name);

      // descending order
      //return r2name.compareTo(r1name);
    }

  };
 
}
