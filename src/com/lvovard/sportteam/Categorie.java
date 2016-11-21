package com.lvovard.sportteam;

import java.util.Comparator;

public class Categorie
{
  String nom;
  String nb_equipe;
  String id_cat;
  String date_record;
  
  public Categorie(String nom, String nb_equipe, String id_cat,String date_record)
  {
    super();
    this.nom = nom;
    this.nb_equipe = nb_equipe;
    this.id_cat = id_cat;
    this.date_record = date_record;
  } 
  
  public String toString() 
  { 
    return this.nom;
  }
  
  public static Comparator<Categorie> CategorieNameComparator = new Comparator<Categorie>() 
  {

    public int compare(Categorie c1, Categorie c2) 
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
