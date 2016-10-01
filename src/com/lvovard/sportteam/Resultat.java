package com.lvovard.sportteam;

import java.util.Comparator;

public class Resultat {

    String id_equipe;
    String date_match;
    String adversaire;
    String score_equipe;
    String score_adversaire;
    String state;
    String competition;
    String lieu;
    String date_record;
    String id_club;
    String id_resultat;
    String detail;

    
    public Resultat(String id_equipe,String date_match,String adversaire,String score_equipe,String score_adversaire,String state,String competition,String lieu,String date_record,String id_club,String id_resultat,String detail)
    {
      this.id_equipe = id_equipe;
      this.date_match = date_match;
      this.adversaire = adversaire;
      this.score_equipe = score_equipe;
      this.score_adversaire = score_adversaire;
      this.state = state;
      this.competition = competition;
      this.lieu = lieu;
      this.date_record = date_record;
      this.id_club = id_club;
      this.id_resultat = id_resultat;
      this.detail = detail;
    }
    
    public static Comparator<Resultat> ResultatDateComparator = new Comparator<Resultat>() 
    {

      public int compare(Resultat r1, Resultat r2) 
      {
        String[] datesplit = r1.date_match.split(" ");
        String r1annee = datesplit[3];
        String r1jour = datesplit[1];
        //recherche du mois
        String[] tabMois={"","janvier","février","mars","avril","mai","juin","juillet","août","septembre","octobre","novembre","décembre"};
        String r1mois="";
        Boolean trouver=false;
        for(int i=0;i<tabMois.length&&!trouver;i++){
           if(datesplit[2].equalsIgnoreCase(tabMois[i])){
             
             r1mois=String.format("%02d", i);
              break;
           }
        }
        datesplit = r2.date_match.split(" ");
        String r2annee = datesplit[3];
        String r2jour = datesplit[1];
      //recherche du mois
        String r2mois="";
        for(int i=0;i<tabMois.length&&!trouver;i++){
           if(datesplit[2].equalsIgnoreCase(tabMois[i])){
             r2mois=String.format("%02d", i);
              break;
           }
        }
        
        String r1date = r1annee+r1mois+r1jour;
        String r2date = r2annee+r2mois+r2jour;
//        Log.i("myApp", "date 1 : "+c1.date);
//        Log.i("myApp", "date 1 : "+c1annee+" "+c1mois+" "+c1jour);
//        Log.i("myApp", "date 1 : "+c1date);
//        Log.i("myApp", "date 2 : "+c2.date);
//        Log.i("myApp", "date 2 : "+c2annee+" "+c2mois+" "+c2jour);
//        Log.i("myApp", "date 2 : "+c2date);

        //ascending order
        return r2date.compareTo(r1date);

        // descending order
        //return r2name.compareTo(r1name);
      }

    };
  
  
}
