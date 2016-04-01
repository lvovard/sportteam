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
import android.widget.TextView;

public class EquipeActivity extends Activity
{
  
  static int equipeselected;
  static List<String> listeequipe = new ArrayList<String>();
	
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equipe);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    
    Record r = Record.recordlist.get(ClubActivity.clubselected);
    String s = r.getCategory().get(CategoryActivity.categoryselected);
    setTitle(r.getClub()+"/"+s);
    
    listeequipe.clear();
    listeequipe.add(s+" 1");
    listeequipe.add(s+" 2");
    listeequipe.add(s+" 3");
    
    for(String eq:listeequipe)
    {
        Button btn = new Button(this);
        btn.setText(eq);
        btn.setId(listeequipe.indexOf(eq));
        layout.addView(btn);
        btn.setOnClickListener(new OnClickListener() 
        {
          @Override
          public void onClick(View v) 
          {
            Log.i("myApp", "id = "+v.getId());
            equipeselected = v.getId();
            Intent intent = new Intent(EquipeActivity.this, ChoiceActivity.class);
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
