package com.lvovard.sportteam;


public class Infoclub {

	  String message;
	  String date_message;
	  
	  public Infoclub(String msg,String date_msg)
	  {
	    this.message  = msg;
	    this.date_message = date_msg;
	  }
	  
	  public String getMessage()
	  {
		  return this.message;
	  }
	  
	  public String getDateMessage()
	  {
		  return this.date_message;
	  }
	
	
}
