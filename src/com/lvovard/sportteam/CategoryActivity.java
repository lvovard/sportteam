package com.lvovard.sportteam;

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

public class CategoryActivity extends Activity
{
  
  static int categoryselected;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);
    
    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setBackgroundResource(R.drawable.bkg);
    
    
    Record r = Record.recordlist.get(ClubActivity.clubselected);
    if (r.getSport().equals("Basketball"))
    {
      layout.setBackgroundResource(R.drawable.basketballbkg);
    }
    if (r.getSport().equals("Football"))
    {
      layout.setBackgroundResource(R.drawable.footbkg);
    }
    
    for (String s:r.category)
    {
      Button btn = new Button(this);
      btn.setText(s);
      btn.setId(r.category.indexOf(s));
      layout.addView(btn);
      btn.setOnClickListener(new OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          Log.i("myApp", "id = "+v.getId());
          categoryselected = v.getId();
          Intent intent = new Intent(CategoryActivity.this, EquipeActivity.class);
          startActivity(intent);
        }
      });
    }
    setTitle(r.getClub());

    
//    for(Record r:Record.recordlist)
//    {
//      if (r.getId() == ClubActivity.id_on_going)
//      {
//        for (String s:r.category)
//        {
//          Button btn = new Button(this);
//          btn.setText(s);
//          btn.setId(r.category.indexOf(s));
//          layout.addView(btn);
//          btn.setOnClickListener(new OnClickListener() 
//          {
//            @Override
//            public void onClick(View v) 
//            {
//              Log.i("myApp", "id = "+v.getId());
//              id_on_going = v.getId();
//              Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
//              startActivity(intent);
//            }
//          });
//        }
//        setTitle(r.getClub());
//        break;
//      }
//    }
    
//    Button btn_convoc = new Button(this);
//    btn_convoc.setText("Convocations");
//    layout.addView(btn_convoc);
//    Button btn_result = new Button(this);
//    btn_result.setText("Resultat");
//    layout.addView(btn_result);
//    Button btn_infoclub = new Button(this);
//    btn_infoclub.setText("Info Club");
//    layout.addView(btn_infoclub);
    
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
