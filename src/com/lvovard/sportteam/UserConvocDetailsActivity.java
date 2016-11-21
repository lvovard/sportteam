package com.lvovard.sportteam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.graphics.PorterDuff;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserConvocDetailsActivity extends Activity {
  
  LinearLayout layout;
  ScrollView scrview;
  TextView tv;
  Context ctx = UserConvocDetailsActivity.this;
  Club userclub;
  String sharetitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
  }
  
  @Override
  protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      setIntent(intent);
  }
  
  @Override
  protected void onResume() 
  {
    super.onResume();
    userclub = Global.getCurrentUserClub(ctx);
    setContentView(R.layout.activity_convocdetails);

    layout = (LinearLayout) findViewById(R.id.linlayout);
    layout.setOrientation(LinearLayout.VERTICAL);
    scrview = (ScrollView) findViewById(R.id.scrollview);

    Convocation convoc = UserListConvocationActivity.convoclist.get(UserListConvocationActivity.convocselected);
    sharetitle = convoc.date + " - Convocation "+ Global.getCurrentEquipe(ctx) + " contre "+convoc.adversaire + " ("+convoc.lieu+")";
    setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx)+"\r\n"+Global.getCurrentChoice(ctx));
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setIcon(R.drawable.ic_event_white_24dp);
    actionBar.setTitle(userclub.nom+"/"+Global.getCurrentEquipe(ctx));
    actionBar.setSubtitle(Global.getCurrentChoice(ctx) + " details");
    //layout.setBackgroundResource(Global.current_bkg_picture);
    getWindow().setBackgroundDrawableResource(Global.getCurrentBkgPicture(ctx));

    tv = new TextView(ctx);
    //
    String detail = 
        "<big>"+convoc.date+"</big><br/><br/>"+
        "<big>"+convoc.lieu+"</big><br/><br/>"+
        "<big>"+convoc.competition+"</big><br/><br/>"+
        "<big>contre "+convoc.adversaire+"</big><br/><br/>"+
        "<big>heure rdv: "+convoc.heure_rdv+"</big><br/>"+
        "<big>lieu  rdv: "+convoc.lieu_rdv+"</big><br/><br/>"+
        "<big>heure match: "+convoc.heure_match+"</big><br/>"+
        "<big>lieu  match: "+convoc.lieu_match+"</big><br/><br/><br/><br/>"+
        "<big>********joueurs convoqués ("+convoc.getListeJoueurs().size()+")***********</big><br/><br/>";
        
    //
    for(String joueur:convoc.getListeJoueurs())
    {
      detail = detail + "<big>"+joueur+"</big><br/>";
    }
    detail = detail + "<br/><br/><big>********dirigeants convoqués ("+convoc.getListeDirigeants().size()+")********</big><br/><br/>";
    for(String dirigeant:convoc.getListeDirigeants())
    {
      detail = detail + "<big>"+dirigeant+"</big><br/>";
    }
    tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
    tv.setText(Html.fromHtml(detail));
    tv.setMovementMethod(new ScrollingMovementMethod());
    tv.setTypeface(null, Typeface.BOLD|Typeface.ITALIC);
    layout.addView(tv);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.convocdetails, menu);
    return true;
  }

  @SuppressLint("CutPasteId")
  @SuppressWarnings("deprecation")
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    
    switch (item.getItemId()) 
    {
      case android.R.id.home:
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
            // This activity is NOT part of this app's task, so create a new task
            // when navigating up, with a synthesized back stack.
            TaskStackBuilder.create(this)
                    // Add all of this activity's parents to the back stack
                    .addNextIntentWithParentStack(upIntent)
                    // Navigate up to the closest parent
                    .startActivities();
        } else {
            // This activity is part of this app's task, so simply
            // navigate up to the logical parent activity.
            NavUtils.navigateUpTo(this, upIntent);
        }
        return true;
      case R.id.action_home:
          Intent intent = new Intent(ctx, MainActivity.class);
          startActivity(intent);  
          return true;
      case R.id.action_share: 
        try
        {
          //set path for bitmap
          String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +"/SportTeam/";
          File dir = new File(file_path);
          dir.mkdirs();
          File file = new File(dir, "sportteamshare.png");
          FileOutputStream fOut = new FileOutputStream(file);
          
          //create bitmap from the scrollview to have all info
          Bitmap map;
          View u = ((Activity) UserConvocDetailsActivity.this).findViewById(R.id.scrollview);
          ScrollView z = (ScrollView) ((Activity) UserConvocDetailsActivity.this).findViewById(R.id.scrollview);
          int totalHeight = z.getChildAt(0).getHeight();
          int totalWidth = z.getChildAt(0).getWidth();
          map = getBitmapFromView(u,totalHeight,totalWidth);
          map.setConfig(Bitmap.Config.ARGB_8888);
          
          //create bitmap from the bkg picture to have all info
          Drawable myDrawable = null;
          if (Build.VERSION.SDK_INT >= 22)
          {
            myDrawable = getResources().getDrawable(Global.getCurrentBkgPicture(ctx),getApplicationContext().getTheme());
          }
          else
          {
            myDrawable = getResources().getDrawable(Global.getCurrentBkgPicture(ctx));
          }
          
          Bitmap bkgbitmap      = ((BitmapDrawable) myDrawable).getBitmap();
          
          //resize scr image to match bkg one
          Bitmap resizebmp = map;
          if (bkgbitmap.getHeight() < map.getHeight())
          {
            resizebmp = getResizedBitmap(map,bkgbitmap.getWidth(), bkgbitmap.getHeight());
          }
          
          for(int x=0;x<resizebmp.getWidth();x++)
          {
            for(int y=0;y<resizebmp.getHeight();y++)
            {
              //non black pix, set it transparent 0xff323232
              //if(resizebmp.getPixel(x, y) > Color.rgb(50, 50, 50))
              if(resizebmp.getPixel(x, y) == 0xFFFFFFFF)
              {
                resizebmp.setPixel(x, y,Color.argb(0, 0, 0, 0));
              }         
              else
              {
                resizebmp.setPixel(x, y,Color.argb(255, 50, 50, 50));
              }
            }
          }

  
          Bitmap bmOverlay = Bitmap.createBitmap(bkgbitmap.getWidth(), bkgbitmap.getHeight(), Bitmap.Config.ARGB_8888);
          Canvas canvas = new Canvas(bmOverlay);
          canvas.drawBitmap(bkgbitmap, 0.0f, 0.0f, null);
          canvas.drawBitmap(resizebmp, 0.0f, 0.0f, null);

          bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
          fOut.flush();
          fOut.close();
          Intent share = new Intent(Intent.ACTION_SEND);
          share.setType("image/png");
          share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
          share.putExtra(Intent.EXTRA_TEXT, sharetitle);
          share.putExtra(Intent.EXTRA_SUBJECT, sharetitle);

          startActivity(Intent.createChooser(share, "Partage de la convocation")); 
        } catch (FileNotFoundException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }    

  }
  
  public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

    Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.   ARGB_8888);
    Canvas canvas = new Canvas(returnedBitmap);
    Drawable bgDrawable = view.getBackground();
    if (bgDrawable != null)
        bgDrawable.draw(canvas);
    else
        canvas.drawColor(Color.WHITE);
    view.draw(canvas);
    return returnedBitmap;
  }
  
  public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    Bitmap resizedBitmap = Bitmap.createBitmap(
        bm, 0, 0, width, height, matrix, false);
    bm.recycle();
    return resizedBitmap;
  }
  
  @SuppressWarnings("unused")
  private Bitmap adjustOpacity(Bitmap bitmap, int opacity)
  {
      Bitmap mutableBitmap = bitmap.isMutable()
                             ? bitmap
                             : bitmap.copy(Bitmap.Config.ARGB_8888, true);
      Canvas canvas = new Canvas(mutableBitmap);
      int colour = (opacity & 0xFF) << 24;
      canvas.drawColor(colour,  PorterDuff.Mode.DST_IN);
      return mutableBitmap;
  }
  
}
