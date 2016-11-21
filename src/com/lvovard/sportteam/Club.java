package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Club
{
  String sport;
  String dep;
  String nom;
  String id_club;
  String password;
  String password_admin;
  String mode;
  int ui_index;
  List<Categorie> catlist = new ArrayList<Categorie>();
  String dateconvocation;
  String dateresultat;
  String dateinformation;
  String datecategorie;
  
  public Club(String sport, String dep, String nom, String id_club,String password,String password_admin,String mode,List<Categorie> catlist,String dateconv,String dateres,String dateinfo,String datecat)
  {
    super();
    this.sport = sport;
    this.dep = dep;
    this.nom = nom;
    this.id_club = id_club;
    this.password = password;
    this.password_admin = password_admin;
    this.mode = mode;
    this.catlist = catlist;
    this.dateconvocation = dateconv;
    this.dateresultat = dateres;
    this.dateinformation = dateinfo;
    this.datecategorie = datecat;
    Collections.sort(catlist, Categorie.CategorieNameComparator);
  }
  
  public void AddCat(Categorie cat)
  {
    catlist.add(cat);
    Collections.sort(catlist, Categorie.CategorieNameComparator);
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
