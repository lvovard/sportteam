package com.lvovard.sportteam;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ConvocationActivity extends Activity {
	
	static List<Convocation> listeconvoc = new ArrayList<Convocation>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_convocation);
	    
	    LinearLayout layout = (LinearLayout) findViewById(R.id.linlayout);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    Record r = Record.recordlist.get(ClubActivity.clubselected);
	    String s = r.getCategory().get(CategoryActivity.categoryselected);
	    String c = ChoiceActivity.listechoice.get(ChoiceActivity.choiceselected);
	    setTitle(r.getClub()+"/"+ " "+EquipeActivity.listeequipe.get(EquipeActivity.equipeselected)+"\r\n"+c);
	    ActionBar actionBar = getActionBar();
        actionBar.setTitle(r.getClub()+"/"+ " "+EquipeActivity.listeequipe.get(EquipeActivity.equipeselected));
        actionBar.setSubtitle(c);
        
        listeconvoc.clear();
        List<String> listejoueurs = new ArrayList<String>();
        List<String> listedirigeants = new ArrayList<String>();
        listejoueurs.add("Nico");
        listejoueurs.add("Jimmy");
        listejoueurs.add("Laurent");
        listejoueurs.add("Jeremy");
        listejoueurs.add("Christopher");
        listejoueurs.add("Ibou");
        listejoueurs.add("Romain");
        listejoueurs.add("Thomas");
        listejoueurs.add("Yoann");
        listedirigeants.add("Gerard");
        listedirigeants.add("Marcel");
        listeconvoc.add(new Convocation("Plestin", "20-12-2015", "12h15", "15h00", "Ploubezre", listejoueurs, listedirigeants, "la victoire !!"));
        for(Convocation conv:listeconvoc)
        {
            Button btn = new Button(this);
            btn.setText(conv.getDate() + " contre " + conv.getAdversaire() );
            btn.setId(listeconvoc.indexOf(conv));
            layout.addView(btn);
            //
            ListView listView = new ListView(this);//(ListView) findViewById(R.id.listView1);

            String[] noms = { "Paris", "Marseille", "Lille", "Bordeaux", "Lyon",
                "Toulouse" };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, noms);

            // on assigne l'adapter à notre list
            listView.setAdapter(adapter);
            layout.addView(listView);
            //
            btn.setOnClickListener(new OnClickListener() 
            {
              @Override
              public void onClick(View v) 
              {
                Log.i("myApp", "id = "+v.getId());
                //choiceselected = v.getId();
                //Intent intent = new Intent(ChoiceActivity.this, ConvocationActivity.class);
                //startActivity(intent);
              }
            });
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.convocation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
