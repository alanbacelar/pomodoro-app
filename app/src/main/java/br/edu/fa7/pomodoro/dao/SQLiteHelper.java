package br.edu.fa7.pomodoro.dao;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.edu.fa7.pomodoro.R;

/**
 * Created by bruno on 30/08/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pomodoro.db";
    private Context mContext;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            Resources resources = mContext.getResources();
            InputStream is = resources.openRawResource(R.raw.create_database);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = reader.readLine()) != null) {
                db.execSQL(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {

            Resources resources = mContext.getResources();
            InputStream is = resources.openRawResource(R.raw.delete_database);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line = null;

            while ((line = reader.readLine()) != null) {
                db.execSQL(line);
            }

            onCreate(db);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
