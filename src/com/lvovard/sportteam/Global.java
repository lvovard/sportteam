package com.lvovard.sportteam;

import android.database.sqlite.SQLiteDatabase;

public class Global
{
  static String current_club;
  static String current_club_id;
  static String current_cat;
  static String current_equipe;
  static String current_sport;
  static String current_choice;
  static int current_bkg_picture; 
  static SQLiteDatabase db;
  static final int TIME_MS_FOR_NOTIF_CHECK = 300000;
  static final int NOTIF_OFFSET_CONVOC = 0;
  static final int NOTIF_OFFSET_RESULT = 30000000;
  static final int NOTIF_OFFSET_INFO   = 60000000;
}
