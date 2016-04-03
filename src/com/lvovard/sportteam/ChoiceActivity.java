package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChoiceActivity extends Activity
{
  
  static int choiceselected;
  static List<String> listechoice = new ArrayList<String>();
	
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_choice);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    Record r = Record.recordlist.get(ClubActivity.clubselected);
    String s = r.getCategory().get(CategoryActivity.categoryselected);
    setTitle(r.getClub()+"/"+ " "+EquipeActivity.listeequipe.get(EquipeActivity.equipeselected));
    
    listechoice.clear();
    listechoice.add("Convocations");
    listechoice.add("Resultat");
    listechoice.add("Informations Club");
    
    for(String eq:listechoice)
    {
        Button btn = new Button(this);
        btn.setText(eq);
        btn.setId(listechoice.indexOf(eq));
        layout.addView(btn);
        btn.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            Log.i("myApp", "id = "+v.getId());
            choiceselected = v.getId();
            Intent intent = null;
            if (choiceselected == listechoice.indexOf("Convocations"))
            {
            	intent = new Intent(ChoiceActivity.this, ConvocationActivity.class);
            }
            else
            {
                if (choiceselected == listechoice.indexOf("Resultat"))
                {
                	intent = new Intent(ChoiceActivity.this, ConvocationActivity.class);
                }
                else
                {
                    if (choiceselected == listechoice.indexOf("Informations Club"))
                    {
                    	intent = new Intent(ChoiceActivity.this, ConvocationActivity.class);
                    }
                }
            }
            startActivity(intent);
          }
        });
    }
    
    
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.category, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings)
    {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
