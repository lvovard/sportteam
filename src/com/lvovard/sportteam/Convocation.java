package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

public class Convocation
{
  String adversaire;
  String date;
  String heure_rdv;
  String heure_match;
  String lieu;
  List<String> listejoueurs = new ArrayList<String>();
  List<String> listedirigeants = new ArrayList<String>();
  String message;
  
  public Convocation(String advers,String date, String hrdv, String hmatch, String lieu, List<String> ljoueurs,List<String> ldirigeants,String msg)
  {
    this.adversaire = advers;
    this.date = date;
    this.heure_rdv = hrdv;
    this.heure_match = hmatch;
    this.lieu = lieu;
    this.listejoueurs = ljoueurs;
    this.listedirigeants = ldirigeants;
    this.message = msg;
  }
  
  public String getAdversaire()
  {
    return adversaire;
  }
  
  public String getDate()
  {
    return date;
  }
  
  public String getHeureRdv()
  {
    return heure_rdv;
  }
  
  public String getHeureMatch()
  {
    return heure_match;
  }
  
  public String getLieu()
  {
    return lieu;
  }
  
  public List<String> getListeJoueurs()
  {
    return listejoueurs;
  }
  
  public List<String> getListeDirigeants()
  {
    return listedirigeants;
  }
  
  public String getMessage()
  {
    return message;
  }
  
  
}
