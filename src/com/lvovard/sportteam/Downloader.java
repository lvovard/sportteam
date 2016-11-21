package com.lvovard.sportteam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.TextUtils;
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
      link = Global.URL_SQL_DATABASE+"/getpwd.php?id_club="+param[0]; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } catch (Exception ex) {
      ////log.i("myApp", "error="+ex);
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
          return oneObject.getString("password")+";;;;;"+oneObject.getString("password_admin");
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
      link = Global.URL_SQL_DATABASE+"/getsport.php"; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } catch (Exception ex) 
    {
      ////log.i("myApp", "error="+ex);
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


abstract class GetCatBySport extends AsyncTask<String, Void, List<String>> {
  @Override
  final protected List<String> doInBackground(String... sport) 
  { 
    List<String> catlist = new ArrayList<String>();
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getcatbysport.php?sport="+URLEncoder.encode(sport[0], "UTF-8"); //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } catch (Exception ex) 
    {
      ////log.i("myApp", "error="+ex);
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
            catlist = Arrays.asList(oneObject.getString("cat").split(";"));
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
    Collections.sort(catlist);
    return catlist;
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
      link = Global.URL_SQL_DATABASE+"/getdep.php?sport="+URLEncoder.encode(sport[0], "UTF-8"); //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", "res1="+result);
    } catch (Exception ex) 
    {
      ////log.i("myApp", "error="+ex);
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
      link = Global.URL_SQL_DATABASE+"/getclub.php?sport="+URLEncoder.encode(param[0], "UTF-8")+"&dep="+URLEncoder.encode(dep, "UTF-8"); //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } catch (Exception ex) {
      ////log.i("myApp", "error="+ex);
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

abstract class GetCat extends AsyncTask<String, Void, List<Categorie>> 
{
  @Override
  final protected List<Categorie> doInBackground(String... param) 
  { 
    List<Categorie> catlist = new ArrayList<Categorie>();
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
      link = Global.URL_SQL_DATABASE+"/getcat.php?club="+param[0]; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } catch (Exception ex) {
      ////log.i("myApp", "error="+ex);
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
            Categorie c = new Categorie(oneObject.getString("nom"), oneObject.getString("nb_equipe"), oneObject.getString("id_cat"), "");
            catlist.add(c);
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
    Collections.sort(catlist, Categorie.CategorieNameComparator);
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
      link = Global.URL_SQL_DATABASE+"/getcat.php?club="+club_id; //+ data
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      ////log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      ////log.i("myApp", "error="+ex);
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
    //log.i("myApp", "cat "+param[0]);
    //log.i("myApp", "equipe "+param[1]);
    //log.i("myApp", "id_club "+param[2]);
    String id_equipe = param[1].replace(" ", "-");
    
    List<Convocation> listeconvoc = new ArrayList<Convocation>();
    List<String> listejoueurs = new ArrayList<String>();
    List<String> listedirigeants = new ArrayList<String>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getconvoc.php?id_equipe="+id_equipe+"&id_club="+param[2]; //+ data
      //log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
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
          		  oneObject.getString("id_convoc"),
          		  oneObject.getString("id_club")
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

abstract class insertConvoc extends AsyncTask<Convocation, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Convocation... param) 
  { 
    Convocation c = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertconvoc.php?id_equipe="+c.equipe+"&date_match="+URLEncoder.encode(c.date, "UTF-8")+"&lieu_match="+URLEncoder.encode(c.lieu_match, "UTF-8")+"&heure_match="+c.heure_match+"&lieu_rdv="+URLEncoder.encode(c.lieu_rdv, "UTF-8")+"&heure_rdv="+c.heure_rdv+"&adversaire="+URLEncoder.encode(c.adversaire, "UTF-8")+"&liste_joueurs="+URLEncoder.encode(TextUtils.join(";", c.listejoueurs), "UTF-8")+"&liste_dirigeants="+URLEncoder.encode(TextUtils.join(";", c.listedirigeants), "UTF-8")+"&state="+c.state+"&id_club="+c.id_club+"&lieu="+URLEncoder.encode(c.lieu, "UTF-8")+"&competition="+URLEncoder.encode(c.competition, "UTF-8"); 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("INSERT INTO convocation") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class updateConvoc extends AsyncTask<Convocation, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Convocation... param) 
  { 
    Convocation c = param[0];

    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/updateconvoc.php?id_equipe="+c.equipe+"&date_match="+URLEncoder.encode(c.date, "UTF-8")+"&lieu_match="+URLEncoder.encode(c.lieu_match, "UTF-8")+"&heure_match="+c.heure_match+"&lieu_rdv="+URLEncoder.encode(c.lieu_rdv, "UTF-8")+"&heure_rdv="+c.heure_rdv+"&adversaire="+URLEncoder.encode(c.adversaire, "UTF-8")+"&liste_joueurs="+URLEncoder.encode(TextUtils.join(";", c.listejoueurs), "UTF-8")+"&liste_dirigeants="+URLEncoder.encode(TextUtils.join(";", c.listedirigeants), "UTF-8")+"&state="+c.state+"&id_club="+c.id_club+"&lieu="+URLEncoder.encode(c.lieu, "UTF-8")+"&competition="+URLEncoder.encode(c.competition, "UTF-8")+"&id_convoc="+c.id_convoc; 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("UPDATE convocation") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class removeConvoc extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_conv = param[0];

    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/deleteconvoc.php?id_convoc="+id_conv; 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("delete passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class insertResultat extends AsyncTask<Resultat, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Resultat... param) 
  { 
    Resultat r = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertresult.php?date_match="+URLEncoder.encode(r.date_match, "UTF-8")+"&adversaire="+URLEncoder.encode(r.adversaire, "UTF-8")+"&id_equipe="+r.id_equipe+"&lieu="+URLEncoder.encode(r.lieu, "UTF-8")+"&score_equipe="+r.score_equipe+"&score_adversaire="+r.score_adversaire+"&state="+r.state+"&competition="+URLEncoder.encode(r.competition, "UTF-8")+"&id_club="+r.id_club+"&detail="+URLEncoder.encode(r.detail.replace("\n", "<br/>"), "UTF-8"); 
      link = link.replace(" ", "%20");
      //log.i("myApp", link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("INSERT INTO resultat") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class updateResultat extends AsyncTask<Resultat, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Resultat... param) 
  { 
    Resultat r = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/updateresult.php?date_match="+URLEncoder.encode(r.date_match, "UTF-8")+"&adversaire="+URLEncoder.encode(r.adversaire, "UTF-8")+"&id_equipe="+r.id_equipe+"&lieu="+URLEncoder.encode(r.lieu, "UTF-8")+"&score_equipe="+r.score_equipe+"&score_adversaire="+r.score_adversaire+"&state="+r.state+"&competition="+URLEncoder.encode(r.competition, "UTF-8")+"&id_club="+r.id_club+"&id_resultat="+r.id_resultat+"&detail="+URLEncoder.encode(r.detail.replace("\n", "<br/>"), "UTF-8"); 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("UPDATE resultat SET") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class removeResultat extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_result = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/deleteresult.php?id_resultat="+id_result; 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("delete passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class insertInfo extends AsyncTask<Info, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Info... param) 
  { 
    Info info = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertinfo.php?id_equipe="+info.id_equipe+"&objet="+URLEncoder.encode(info.objet, "UTF-8")+"&message="+URLEncoder.encode(info.message.replace("\n","<br/>"), "UTF-8")+"&state="+info.state+"&id_club="+info.id_club+"&date_info="+URLEncoder.encode(info.date_info, "UTF-8")+"&heure_info="+info.heure_info; 
      link = link.replace(" ", "%20");
      //log.i("myApp", link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("INSERT INTO infoclub") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class updateInfo extends AsyncTask<Info, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(Info... param) 
  { 
    Info info = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/updateinfo.php?id_equipe="+info.id_equipe+"&objet="+URLEncoder.encode(info.objet, "UTF-8")+"&message="+URLEncoder.encode(info.message.replace("\n","<br/>"), "UTF-8")+"&state="+info.state+"&id_club="+info.id_club+"&id_info="+info.id_info+"&date_info="+URLEncoder.encode(info.date_info, "UTF-8")+"&heure_info="+info.heure_info; 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("UPDATE infoclub SET") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class removeInfo extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_info = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/deleteinfo.php?id_info="+id_info; 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("delete passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class getPerson extends AsyncTask<String, Void, List<Person>> 
{
  @Override
  final protected List<Person> doInBackground(String... param) 
  { 
    String id_club = param[0];
    
    List<Person> listeperson = new ArrayList<Person>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getjoueur.php?id_club="+id_club;
      //log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      try 
      {
        JSONObject jObject = new JSONObject(result);
        JSONArray jArray = jObject.getJSONArray("joueur");
        for (int i=0; i < jArray.length(); i++)
        {
          try 
          {
            JSONObject oneObject = jArray.getJSONObject(i);
            // Pulling items from the array
            Person p = new Person(oneObject.getString("nom"),oneObject.getString("prenom"),oneObject.getString("role"),oneObject.getString("id_cat"),oneObject.getString("id_joueur"));
            listeperson.add(p);
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
    Collections.sort(listeperson, Person.PersonNameComparator);
    return listeperson;
  }
}

abstract class GetResultat extends AsyncTask<String, Void, List<Resultat>> 
{
  @Override
  final protected List<Resultat> doInBackground(String... param) 
  { 
    //log.i("myApp", "cat "+param[0]);
    //log.i("myApp", "equipe "+param[1]);
    //log.i("myApp", "id_club "+param[2]);
    String id_equipe = param[1].replace(" ", "-");
    
    List<Resultat> listresult = new ArrayList<Resultat>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getresult.php?id_equipe="+id_equipe+"&id_club="+param[2]; //+ data
      //log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
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
            ////log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe"));
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
    //log.i("myApp", "cat "+param[0]);
    //log.i("myApp", "equipe "+param[1]);
    //log.i("myApp", "id_club "+param[2]);
    //String id_equipe = param[1].replace(" ", "-");
    
    List<Info> listinfo = new ArrayList<Info>();
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getinfo.php?id_equipe="+param[0]+"&id_club="+param[2]; //+ data
      //log.i("myApp", "link "+link);
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
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
    String date    = param[1];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getlastinfodate.php?date_record="+date+"&id_club="+club_id;
      ////log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      //log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      //log.i("myApp", "error="+ex);
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
    String date    = param[1];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getlastresultdate.php?date_record="+date+"&id_club="+club_id;
      ////log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      //log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      //log.i("myApp", "error="+ex);
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
            ////log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe"));
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
    String date    = param[1];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getlastconvocdate.php?date_record="+date+"&id_club="+club_id;
      ////log.i("myApp", "link="+link);
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      //log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      //log.i("myApp", "error="+ex);
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
            //log.i("myApp", "enregistre dans liste "+oneObject.getString("adversaire")+oneObject.getString("date_match")+oneObject.getString("date_record")+oneObject.getString("id_equipe")+oneObject.getString("state"));
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
                  oneObject.getString("id_convoc"),
                  oneObject.getString("id_club")
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

abstract class GetLastCatDate extends AsyncTask<String, Void, List<Categorie>> 
{
  @Override
  final protected List<Categorie> doInBackground(String... param) 
  { 
    List<Categorie> listcat = new ArrayList<Categorie>();
    String club_id = param[0];
    String date    = param[1];
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/getlastcatdate.php?date_record="+date+"&id_club="+club_id;
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      //log.i("myApp", "link="+link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } 
    catch (Exception ex) 
    {
      //log.i("myApp", "error="+ex);
    }
    if ( (result != null) && (! result.contains("No categorie found")) )
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
            //log.i("myApp", "enregistre dans liste "+oneObject.getString("nom")+oneObject.getString("date_record"));
            Categorie c1 = new Categorie(oneObject.getString("nom"), "", oneObject.getString("id_cat"), oneObject.getString("date_record"));

            listcat.add(c1);
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
    return listcat;
  }
}



abstract class createClub extends AsyncTask<String, Void, Integer> 
{
  @Override
  final protected Integer doInBackground(String... param) 
  { 
    String sport = param[0];
    String dep = param[1];
    String club = param[2];
    String email = param[3];
    String pwd_user = param[4];
    String pwd_admin = param[5];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertclub.php?sport="+URLEncoder.encode(sport, "UTF-8")+"&dep="+URLEncoder.encode(dep, "UTF-8")+"&club="+URLEncoder.encode(club, "UTF-8")+"&email="+URLEncoder.encode(email, "UTF-8")+"&pwd="+URLEncoder.encode(pwd_user, "UTF-8")+"&pwd_admin="+URLEncoder.encode(pwd_admin, "UTF-8"); //+ data
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed;id_club=") )
      {
        return Integer.parseInt(result.split(";")[1].split("=")[1]);
      }
    }
    return 0;
  }
}

abstract class insertCat extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_club = param[0];
    String cat = param[1];
    String nb_equipe = param[2];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertcat.php?id_club="+id_club+"&nom="+URLEncoder.encode(cat, "UTF-8")+"&nb_equipe="+nb_equipe; //+ data
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class insertPerson extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String nom = param[0];
    String prenom = param[1];
    String role = param[2];
    String id_cat = param[3];
    String id_club = param[4];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertjoueur.php?nom="+URLEncoder.encode(nom, "UTF-8")+"&prenom="+URLEncoder.encode(prenom, "UTF-8")+"&role="+URLEncoder.encode(role, "UTF-8")+"&id_cat="+id_cat+"&id_club="+id_club;
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class removePerson extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_joueur = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/deletejoueur.php?id_joueur="+id_joueur;
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}


abstract class removeCat extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_cat = param[0];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/deletecat.php?id_cat="+id_cat;
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class modifyCat extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String id_cat = param[0];
    String nb_equipe = param[1];
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/updatecat.php?id_cat="+id_cat+"&nb_equipe="+nb_equipe;
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}



abstract class updatePwd extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    String idclub = param[0];
    String newpwd = param[1];
    String mode = param[2];
    
    String link = "";
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      if (mode.contentEquals("utilisateur"))
      {
        link = Global.URL_SQL_DATABASE+"/updateuserpwd.php?id_club="+idclub+"&password="+URLEncoder.encode(newpwd, "UTF-8");
      }
      else
      {
        if (mode.contentEquals("admin"))
        {
          link = Global.URL_SQL_DATABASE+"/updateadminpwd.php?id_club="+idclub+"&password_admin="+URLEncoder.encode(newpwd, "UTF-8");
        }
      }
      
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("update passed") )
      {
        return true;
      }
    }
    return false;
  }
}

abstract class insertCrash extends AsyncTask<String, Void, Boolean> 
{
  @Override
  final protected Boolean doInBackground(String... param) 
  { 
    
    String link;
    BufferedReader bufferedReader;
    String result = null;
    try 
    {
      link = Global.URL_SQL_DATABASE+"/insertcrash.php?content="+URLEncoder.encode(param[0], "UTF-8"); 
      link = link.replace(" ", "%20");
      URL url = new URL(link);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();

      bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      result = bufferedReader.readLine();
      //log.i("myApp", result);
    } catch (Exception ex) {
      //log.i("myApp", "error="+ex);
    }
    if (result != null)
    {
      if ( result.contains("insert passed") )
      {
        return true;
      }
    }
    return false;
  }
}





