package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class Convocation
{

  String adversaire;
  String date;
  String heure_rdv;
  String heure_match;
  String lieu_rdv;
  String lieu_match;
  List<String> listejoueurs = new ArrayList<String>();
  List<String> listedirigeants = new ArrayList<String>();
  String date_record;
  String equipe;
  String state;
  String lieu;
  String competition;
  String id_convoc;

  
  public Convocation(String advers,String date, String hrdv, String hmatch, String lrdv, String lmatch, List<String> ljoueurs,List<String> ldirigeants,String date_record,String equipe,String state,String lieu,String competition,String id_convoc)
  { 
    this.adversaire = advers;
    this.date = date;
    this.heure_rdv = hrdv;
    this.heure_match = hmatch;
    this.lieu_match = lmatch;
    this.lieu_rdv = lrdv;
    this.listejoueurs = ljoueurs;
    this.listedirigeants = ldirigeants;
    this.date_record = date_record;
    this.equipe = equipe;
    this.state = state;
    this.lieu = lieu;
    this.competition = competition;
    this.id_convoc = id_convoc;
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
  
  public String getLieuMatch()
  {
    return lieu_match;
  }
  
  public String getLieuRDV()
  {
    return lieu_rdv;
  }
  
  public List<String> getListeJoueurs()
  {
    return listejoueurs;
  }
  
  public List<String> getListeDirigeants()
  {
    return listedirigeants;
  }
  
  public static Comparator<Convocation> ConvocationDateComparator = new Comparator<Convocation>() 
  {

    public int compare(Convocation c1, Convocation c2) 
    {
      String[] datesplit = c1.date.split(" ");
      String c1annee = datesplit[3];
      String c1jour = datesplit[1];
    //recherche du mois
      String[] tabMois={"","janvier","février","mars","avril","mai","juin","juillet","août","septembre","octobre","novembre","décembre"};
      String c1mois="";
      Boolean trouver=false;
      for(int i=0;i<tabMois.length&&!trouver;i++){
         if(datesplit[2].equalsIgnoreCase(tabMois[i])){
        	 
        	 c1mois=String.format("%02d", i);
            break;
         }
      }
      datesplit = c2.date.split(" ");
      String c2annee = datesplit[3];
      String c2jour = datesplit[1];
    //recherche du mois
      String c2mois="";
      for(int i=0;i<tabMois.length&&!trouver;i++){
         if(datesplit[2].equalsIgnoreCase(tabMois[i])){
        	 c2mois=String.format("%02d", i);
            break;
         }
      }
      
      String c1date = c1annee+c1mois+c1jour;
      String c2date = c2annee+c2mois+c2jour;
//      Log.i("myApp", "date 1 : "+c1.date);
//      Log.i("myApp", "date 1 : "+c1annee+" "+c1mois+" "+c1jour);
//      Log.i("myApp", "date 1 : "+c1date);
//      Log.i("myApp", "date 2 : "+c2.date);
//      Log.i("myApp", "date 2 : "+c2annee+" "+c2mois+" "+c2jour);
//      Log.i("myApp", "date 2 : "+c2date);

      //ascending order
      return c2date.compareTo(c1date);

      // descending order
      //return r2name.compareTo(r1name);
    }

  };
  
  
}
