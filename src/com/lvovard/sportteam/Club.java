package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Club
{
  String sport;
  String dep;
  String nom;
  String id_club;
  String password;
  int ui_index;
  List<Categorie> catlist = new ArrayList<Categorie>();
  
  public Club(String sport, String dep, String nom, String id_club,String password)
  {
    super();
    this.sport = sport;
    this.dep = dep;
    this.nom = nom;
    this.id_club = id_club;
  }
  
  public void AddCat(Categorie cat)
  {
    catlist.add(cat);
  }
  
  public static Comparator<Club> ClubNameComparator = new Comparator<Club>() 
  {

    public int compare(Club c1, Club c2) 
    {
      String c1name = c1.nom;
      String c2name = c2.nom;

      //ascending order
      return c1name.compareTo(c2name);

      // descending order
      //return r2name.compareTo(r1name);
    }

  };
  
}
