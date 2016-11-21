package com.lvovard.sportteam;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class TestActivity extends Activity
{

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test);
    getWindow().setBackgroundDrawableResource(R.drawable.gazon);
    PieProgressDrawable pieProgressDrawable = new PieProgressDrawable();
    pieProgressDrawable.setColor(Color.GREEN);
    ImageView timeProgress = (ImageView) findViewById(R.id.time_progress);
    timeProgress.setImageDrawable(pieProgressDrawable);
    pieProgressDrawable.setLevel(100);
    timeProgress.invalidate();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.test, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    return super.onOptionsItemSelected(item);
  }
}
