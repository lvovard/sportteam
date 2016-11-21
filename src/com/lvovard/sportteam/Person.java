package com.lvovard.sportteam;

import java.util.Comparator;

public class Person
{
  String nom;
  String prenom;
  String role;
  String id_cat;
  String id_joueur;
  
  public Person(String nom,String prenom, String role, String id_cat, String id_joueur)
  { 
    this.nom = nom;
    this.prenom = prenom;
    this.role = role;
    this.id_cat = id_cat;
    this.id_joueur = id_joueur;
  }
  
  public static Comparator<Person> PersonNameComparator = new Comparator<Person>() 
  {

    public int compare(Person p1, Person p2) 
    {
      String p1name = p1.nom + " " + p1.prenom;
      String p2name = p2.nom + " " + p2.prenom;

      //ascending order
      return p1name.compareTo(p2name);

      // descending order
      //return r2name.compareTo(r1name);
    }

  };
  
}
