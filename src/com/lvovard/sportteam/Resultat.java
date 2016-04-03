package com.lvovard.sportteam;


public class Resultat {

	  String equipe_domicile;
	  String equipe_exterieure;
	  int score_domicile;
	  int score_exterieur;
	  String date_match;
	  
	  public Resultat(String equipe_d,String equipe_e, int score_d, int score_e, String date)
	  {
	    this.equipe_domicile  = equipe_d;
	    this.equipe_exterieure = equipe_e;
	    this.score_domicile   = score_d;
	    this.score_exterieur  = score_e;
	    this.date_match = date;
	  }
	  
	  public String getEquipeDomicile()
	  {
		  return this.equipe_domicile;
	  }
	  
	  public String getEquipeExterieure()
	  {
		  return this.equipe_exterieure;
	  }
	  
	  public int getScoreDomicile()
	  {
		  return this.score_domicile;
	  }
	  
	  public int getScoreExterieur()
	  {
		  return this.score_exterieur;
	  }
	  
	  public String getDateMatch()
	  {
		  return this.date_match;
	  }
	
	
}
