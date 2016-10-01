package com.lvovard.sportteam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;


abstract class getPassword extends AsyncTask<String, Void, String> 
{
  @Override
  final protected String doInBackground(String... param) 
  { 
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getpwd.php?id_club="+param[0]; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("club");
        try 
        {
          JSONObject oneObject = jArray.getJSONObject(0);
          // Pulling items from the array
          return oneObject.getString("password");
        }catch (JSONException e) 
        {
          // Oops
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return "nopasswordfound";
  }
}

abstract class GetSport extends AsyncTask<Void, Void, List<String>> {
  @Override
  final protected List<String> doInBackground(Void... progress) 
  { 
    List<String> sportlist = new ArrayList<String>();
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getsport.php"; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (!result.contains("No sport found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("sport");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            sportlist.add(oneObject.getString("sport_name"));
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) 
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(sportlist);
    return sportlist;
  }
}

abstract class GetDep extends AsyncTask<String, Void, List<String>> 
{
  @Override
  final protected List<String> doInBackground(String... sport) 
  { 
    List<String> deplist = new ArrayList<String>();
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getdep.php?sport="+sport[0]; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", "res1="+result);
    } catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("departement");
        for (int i=0; i < jArray.length(); i++)
        {
          try {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            deplist.add(oneObject.getString("dep"));
          } catch (JSONException e) {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(deplist);
    return deplist;
  }
}

abstract class GetClub extends AsyncTask<String, Void, JSONArray> 
{
  @Override
  final protected JSONArray doInBackground(String... param) 
  { 
    JSONArray jArray = null;
    //List<String> clublist = new ArrayList<String>();
    //http://www.sportteam.890m.com/getclub.php?sport="+sport+"&dep="+dep
    //List<String> deplist = new ArrayList<String>();
    String dep = param[1].split(" : ")[0];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getclub.php?sport="+param[0]+"&dep="+dep; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        jArray = jObject.getJSONArray("club");
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return jArray;
  }
}

abstract class GetCat extends AsyncTask<String, Void, List<String>> 
{
  @Override
  final protected List<String> doInBackground(String... param) 
  { 
    List<String> catlist = new ArrayList<String>();
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getcat.php?club="+param[0]; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("categorie");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            catlist.add(oneObject.getString("nom"));
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(catlist);
    return catlist;
  }
}

abstract class GetNbTeam extends AsyncTask<String, Void, Integer> 
{
  @Override
  final protected Integer doInBackground(String... param) 
  { 
    String club_id = param[0];
    String cat_name = param[1];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getcat.php?club="+club_id; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("categorie");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            if (oneObject.getString("nom").contentEquals(cat_name))
            {
              return Integer.parseInt(oneObject.getString("nb_equipe"));
            }
          } 
          catch (JSONException e) 
          {
            
          }
        }
      } 
      catch (JSONException e) 
      {
        e.printStackTrace();
      }
    }
    return 0;
  }
}


abstract class GetConvoc extends AsyncTask<String, Void, List<Convocation>> 
{
  @Override
  final protected List<Convocation> doInBackground(String... param) 
  { 
    Log.i("myApp", "cat "+param[0]);
    Log.i("myApp", "equipe "+param[1]);
    Log.i("myApp", "id_club "+param[2]);
    String id_equipe = param[1].replace(" ", "-");
    
    List<Convocation> listeconvoc = new ArrayList<Convocation>();
    List<String> listejoueurs = new ArrayList<String>();
    List<String> listedirigeants = new ArrayList<String>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getconvoc.php?id_equipe="+id_equipe+"&id_club="+param[2]; //+ data
      Log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("convocation");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            String lj= oneObject.getString("liste_joueurs");
            listejoueurs = new ArrayList<String>(Arrays.asList(lj.split(";")));
            String ld= oneObject.getString("liste_dirigeants");
            listedirigeants = new ArrayList<String>(Arrays.asList(ld.split(";")));
            Convocation c1 = new Convocation(
          		  oneObject.getString("adversaire"),
          		  oneObject.getString("date_match"),
          		  oneObject.getString("heure_rdv"),
          		  oneObject.getString("heure_match"),
          		  oneObject.getString("lieu_rdv"),
          		  oneObject.getString("lieu_match"),
          		  listejoueurs,
          		  listedirigeants,
          		  "",
          		  "",
          		  oneObject.getString("state"),
          		  oneObject.getString("lieu"),
          		  oneObject.getString("competition"),
          		  oneObject.getString("id_convoc")
          		  );
            listeconvoc.add(c1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(listeconvoc, Convocation.ConvocationDateComparator);
    return listeconvoc;
  }
}

abstract class GetResultat extends AsyncTask<String, Void, List<Resultat>> 
{
  @Override
  final protected List<Resultat> doInBackground(String... param) 
  { 
    Log.i("myApp", "cat "+param[0]);
    Log.i("myApp", "equipe "+param[1]);
    Log.i("myApp", "id_club "+param[2]);
    String id_equipe = param[1].replace(" ", "-");
    
    List<Resultat> listresult = new ArrayList<Resultat>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getresult.php?id_equipe="+id_equipe+"&id_club="+param[2]; //+ data
      Log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (!result.contains("No resultat found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("resultat");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            //Log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe"));
            //Resultat(String id_equipe,String date_match,String adversaire,String score_equipe,String score_adversaire,String state,String competition,String lieu,String date_record,String id_club,String id_resultat)
            Resultat r1 = new Resultat(oneObject.getString("id_equipe") ,oneObject.getString("date_match") ,oneObject.getString("adversaire") ,oneObject.getString("score_equipe")  ,oneObject.getString("score_adversaire")  ,oneObject.getString("state")  ,oneObject.getString("competition"),oneObject.getString("lieu"),oneObject.getString("date_record"),oneObject.getString("id_club"),oneObject.getString("id_resultat"),oneObject.getString("detail") );
           
          listresult.add(r1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(listresult, Resultat.ResultatDateComparator);
    return listresult;
  }
}

abstract class GetInfo extends AsyncTask<String, Void, List<Info>> 
{
  @Override
  final protected List<Info> doInBackground(String... param) 
  { 
    Log.i("myApp", "cat "+param[0]);
    Log.i("myApp", "equipe "+param[1]);
    Log.i("myApp", "id_club "+param[2]);
    //String id_equipe = param[1].replace(" ", "-");
    
    List<Info> listinfo = new ArrayList<Info>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getinfo.php?id_equipe="+param[0]+"&id_club="+param[2]; //+ data
      Log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (!result.contains("No info found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("infoclub");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            Info i1 = new Info(oneObject.getString("id_equipe") ,oneObject.getString("objet") ,oneObject.getString("message"),oneObject.getString("state"),oneObject.getString("date_record"),oneObject.getString("id_club"),oneObject.getString("id_info"),oneObject.getString("date_info"),oneObject.getString("heure_info"));
            listinfo.add(i1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    Collections.sort(listinfo, Info.InfoDateComparator);
    return listinfo;
  }
}

abstract class GetLastInfoDate extends AsyncTask<String, Void, List<Info>> 
{
  @Override
  final protected List<Info> doInBackground(String... param) 
  { 
    List<Info> listinfo = new ArrayList<Info>();
    String club_id = param[0];
    String cat     = param[1];
    String date    = param[2];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getlastinfodate.php?date_record="+date+"&id_club="+club_id+"&id_equipe="+cat;
      //Log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      Log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (! result.contains("No info found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("infoclub");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            Info i1 = new Info(oneObject.getString("id_equipe") ,oneObject.getString("objet") ,oneObject.getString("message"),oneObject.getString("state"),oneObject.getString("date_record"),oneObject.getString("id_club"),oneObject.getString("id_info"),oneObject.getString("date_info"),oneObject.getString("heure_info"));
            listinfo.add(i1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return listinfo;
  }
}

abstract class GetLastResultatDate extends AsyncTask<String, Void, List<Resultat>> 
{
  @Override
  final protected List<Resultat> doInBackground(String... param) 
  { 
    List<Resultat> listresult = new ArrayList<Resultat>();
    String club_id = param[0];
    String cat     = param[1];
    String date    = param[2];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getlastresultdate.php?date_record="+date+"&id_club="+club_id+"&id_equipe="+cat;
      //Log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      Log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (! result.contains("No resultat found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("resultat");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            //Log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe"));
            //Resultat(String id_equipe,String date_match,String adversaire,String score_equipe,String score_adversaire,String state,String competition,String lieu,String date_record,String id_club,String id_resultat)
            Resultat r1 = new Resultat(oneObject.getString("id_equipe") ,oneObject.getString("date_match") ,oneObject.getString("adversaire") ,oneObject.getString("score_equipe")  ,oneObject.getString("score_adversaire")  ,oneObject.getString("state")  ,oneObject.getString("competition"),oneObject.getString("lieu"),oneObject.getString("date_record"),oneObject.getString("id_club"),oneObject.getString("id_resultat"),oneObject.getString("detail") );
           
          listresult.add(r1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return listresult;
  }
}

abstract class GetLastConvocationDate extends AsyncTask<String, Void, List<Convocation>> 
{
  @Override
  final protected List<Convocation> doInBackground(String... param) 
  { 
	  List<Convocation> listconvoc = new ArrayList<Convocation>();
    String club_id = param[0];
    String cat     = param[1];
    String date    = param[2];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = "http://www.sportteam.890m.com/getlastconvocdate.php?date_record="+date+"&id_club="+club_id+"&id_equipe="+cat;
      //Log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      Log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      Log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (! result.contains("No convocation found")) )
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("convocation");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            Log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe")+oneObject.getString("state"));
            Convocation c1 = new Convocation
                (
                  oneObject.getString("adversaire"),
                  oneObject.getString("date_match"),
                  oneObject.getString("heure_rdv"),
                  oneObject.getString("heure_match"),
                  oneObject.getString("lieu_rdv"),
                  oneObject.getString("lieu_match"),
                  new ArrayList<String>(Arrays.asList(oneObject.getString("liste_joueurs").split(";"))),
                  new ArrayList<String>(Arrays.asList(oneObject.getString("liste_dirigeants").split(";"))),
                  oneObject.getString("date_record"),
                  oneObject.getString("id_equipe"),
                  oneObject.getString("state"),
                  oneObject.getString("lieu"),
                  oneObject.getString("competition"),
                  oneObject.getString("id_convoc")
                );
    		  listconvoc.add(c1);
          } catch (JSONException e) 
          {
            // Oops
          }
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return listconvoc;
  }
}

abstract class getInfoDateNotif extends AsyncTask<RecordNotification, Void, JSONArray> 
{
  @Override
  final protected JSONArray doInBackground(RecordNotification... param) 
  { 
    JSONArray jArray = new JSONArray();
    //List<String> clublist = new ArrayList<String>();
    //http://www.sportteam.890m.com/getclub.php?sport="+sport+"&dep="+dep
    //List<String> deplist = new ArrayList<String>();
    //RecordNotification rn = param[0];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getclub.php?"; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        jArray = jObject.getJSONArray("information");
  //      for (int i=0; i < jArray.length(); i++)
  //      {
  //        try {
  //          //JSONObject oneObject = jArray.getJSONObject(i);
  //          // Pulling items from the array
  //          //clublist.add(oneObject.getString("club"));
  //        } catch (JSONException e) {
  //          // Oops
  //        }
  //      }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return jArray;
  }
}

abstract class getResultatDateNotif extends AsyncTask<RecordNotification, Void, JSONArray> 
{
  @Override
  final protected JSONArray doInBackground(RecordNotification... param) 
  { 
    JSONArray jArray = new JSONArray();
    //List<String> clublist = new ArrayList<String>();
    //http://www.sportteam.890m.com/getclub.php?sport="+sport+"&dep="+dep
    //List<String> deplist = new ArrayList<String>();
    //RecordNotification rn = param[0];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getclub.php?"; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        jArray = jObject.getJSONArray("information");
  //      for (int i=0; i < jArray.length(); i++)
  //      {
  //        try {
  //          //JSONObject oneObject = jArray.getJSONObject(i);
  //          // Pulling items from the array
  //          //clublist.add(oneObject.getString("club"));
  //        } catch (JSONException e) {
  //          // Oops
  //        }
  //      }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return jArray;
  }
}

abstract class getConvocationDateNotif extends AsyncTask<RecordNotification, Void, JSONArray> 
{
  @Override
  final protected JSONArray doInBackground(RecordNotification... param) 
  { 
    JSONArray jArray = new JSONArray();
    //List<String> clublist = new ArrayList<String>();
    //http://www.sportteam.890m.com/getclub.php?sport="+sport+"&dep="+dep
    //List<String> deplist = new ArrayList<String>();
    //RecordNotification rn = param[0];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      //data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
      //data += "&username=" + URLEncoder.encode(userName, "UTF-8");
      //data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
      //data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
      //data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");
      link = "http://www.sportteam.890m.com/getclub.php?"; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      Log.i("myApp", result);
    } catch (Exception ex) {
      Log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        jArray = jObject.getJSONArray("information");
  //      for (int i=0; i < jArray.length(); i++)
  //      {
  //        try {
  //          //JSONObject oneObject = jArray.getJSONObject(i);
  //          // Pulling items from the array
  //          //clublist.add(oneObject.getString("club"));
  //        } catch (JSONException e) {
  //          // Oops
  //        }
  //      }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return jArray;
  }
}




