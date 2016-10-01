package com.lvovard.sportteam;


public class RecordNotification
{
//
//  String sport = null;
//  String club = null;
//  String id_club = null;
//  String lastdate = null;
//  List<String> category = new ArrayList<String>();
//  boolean addedInList = false;
//  
//  static RecordNotification current_record = null;
//  
//  //static List<RecordNotification> recordlist = new ArrayList<RecordNotification>();
//
//  public RecordNotification(String sp, String clb, String idclub, String cat ,String lastdate)
//  {
//    this.sport = sp;
//    this.club = clb;
//    this.id_club = idclub;
//    this.lastdate = lastdate;
//    if (clubExists() == false)
//    {
//      Log.i("myApp", clb+" does not exist: create new entry");
//      category.add(cat);
//      recordlist.add(this);
//      Collections.sort(recordlist, RecordNotification.RecordNameComparator);
//      addedInList = true;
//    }
//    else
//    {
//      Log.i("myApp", clb+" exists");
//      if (categoryExists(cat) == false)
//      {
//        Log.i("myApp", cat+" does not exist: update existing entry");
//        Collections.sort(recordlist, RecordNotification.RecordNameComparator);
//        addedInList = true;
//      }
//      else
//      {
//        Log.i("myApp", cat+" for "+clb+" already exists - do nothing");
//      }
//    }
//    Log.i("myApp", "display list starts");
//    String sep = " - ";
//    for(RecordNotification rcd:recordlist)
//    {
//      Log.i("myApp", rcd.sport+sep+rcd.club+sep+rcd.getCategory());
//    }
//    Log.i("myApp", "display list ends");
//  }
//  
//  
//  private boolean clubExists()
//  {
//    for(RecordNotification rcd:recordlist)
//    {
//      if ( this.id_club.equals(rcd.id_club) )
//      {
//        return true;
//      }
//    }
//    return false;
//  }
//  
//  private boolean categoryExists(String cat)
//  {
//    for(RecordNotification rcd:recordlist)
//    {
//      if ( this.id_club.equals(rcd.id_club))
//      {
//        if ( rcd.getCategory().contains(cat))
//        {
//          return true;
//        }
//        else
//        {
//          rcd.category.add(cat);
//          Collections.sort(rcd.category);
//        }
//      }
//    }
//    return false;
//  }
//  
//  public List<String> getCategory()
//  {
//    return category;
//  }
//  
//
//  public static Comparator<RecordNotification> RecordNameComparator = new Comparator<RecordNotification>() 
//  {
//
//    public int compare(RecordNotification r1, RecordNotification r2) 
//    {
//      String r1name = r1.sport+r1.club+r1.getCategory();
//      String r2name = r2.sport+r2.club+r2.getCategory();
//
//      //ascending order
//      return r1name.compareTo(r2name);
//
//      // descending order
//      //return r2name.compareTo(r1name);
//    }
//
//  };
// 
}
