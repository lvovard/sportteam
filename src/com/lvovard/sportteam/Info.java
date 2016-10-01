package com.lvovard.sportteam;

import java.util.Comparator;

public class Info {

  String id_equipe;
  String objet;
  String message;
  String state;
  String date_record;
  String id_club;
  String id_info;
  String date_info;
  String heure_info;


  public Info(String id_equipe,String objet,String message,String state,String date_record,String id_club,String id_info,String date_info,String heure_info)
  {
    this.id_equipe = id_equipe;
    this.objet = objet;
    this.message = message;
    this.state = state;
    this.date_record = date_record;
    this.id_club = id_club;
    this.id_info = id_info;
    this.date_info = date_info;
    this.heure_info = heure_info;
  }

  
  public static Comparator<Info> InfoDateComparator = new Comparator<Info>() 
  {

    public int compare(Info i1, Info i2) 
    {
      String[] datesplit = i1.date_info.split(" ");
      String i1annee = datesplit[3];
      String i1jour = datesplit[1];
      //recherche du mois
      String[] tabMois={"","janvier","février","mars","avril","mai","juin","juillet","août","septembre","octobre","novembre","décembre"};
      String i1mois="";
      Boolean trouver=false;
      for(int i=0;i<tabMois.length&&!trouver;i++){
         if(datesplit[2].equalsIgnoreCase(tabMois[i])){
           
           i1mois=String.format("%02d", i);
            break;
         }
      }
      datesplit = i2.date_info.split(" ");
      String i2annee = datesplit[3];
      String i2jour = datesplit[1];
    //recherche du mois
      String i2mois="";
      for(int i=0;i<tabMois.length&&!trouver;i++){
         if(datesplit[2].equalsIgnoreCase(tabMois[i])){
           i2mois=String.format("%02d", i);
            break;
         }
      }
      
      String i1date = i1annee+i1mois+i1jour;
      String i2date = i2annee+i2mois+i2jour;
//      Log.i("myApp", "date 1 : "+c1.date);
//      Log.i("myApp", "date 1 : "+c1annee+" "+c1mois+" "+c1jour);
//      Log.i("myApp", "date 1 : "+c1date);
//      Log.i("myApp", "date 2 : "+c2.date);
//      Log.i("myApp", "date 2 : "+c2annee+" "+c2mois+" "+c2jour);
//      Log.i("myApp", "date 2 : "+c2date);

      //ascending order
      return i2date.compareTo(i1date);

      // descending order
      //return r2name.compareTo(r1name);
    }

  };
  
}
