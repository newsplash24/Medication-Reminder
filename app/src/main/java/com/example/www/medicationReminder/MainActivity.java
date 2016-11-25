package com.example.www.medicationReminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.www.medicationReminder.data.MedicationContract;
import com.example.www.medicationReminder.data.MedicationContract.MedicationEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String mDateFormat = "dd/MM/yyyy";
    ContentValues [] expiredList;
    String[] columnNames;
    TextView hello;
    TableLayout tableLayout;
    RelativeLayout table;
    TableRow header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        expiredList = null;
        columnNames = null;
        hello = (TextView) findViewById(R.id.hello);
        tableLayout=(TableLayout)findViewById(R.id.tableLayout);
        table = (RelativeLayout) findViewById(R.id.table);
        header = (TableRow)  findViewById(R.id.header);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkExpired();
        getExpired();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id == R.id.nav_insert){
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new FragmentAdd()
                   ).addToBackStack("ADD_FRAGMENT").commit();
        }

        else if (id == R.id.nav_list) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content, new FragmentShow()
            ).addToBackStack("SHOW_FRAGMENT").commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void checkExpired() {
        Cursor cursor = getContentResolver().query(MedicationContract.MedicationEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
//            Toast.makeText(MainActivity.this, "Entered", Toast.LENGTH_SHORT).show();
            int expIndx = cursor.getColumnIndex(MedicationEntry.COLUMN_EXPIRY_DATE);
            long currTime = System.currentTimeMillis();
            while (cursor.moveToNext()) {

                String expDate = cursor.getString(expIndx);
                long expDateMillis = getDateMillis(expDate);
                if (currTime > expDateMillis) {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MedicationEntry.COLUMN_EXPIRED, 1);
                    long id = cursor.getLong(cursor.getColumnIndex(MedicationEntry._ID));
                    int i = getContentResolver().update(MedicationEntry.buildMedicationUpdateUri(), newValues, MedicationEntry._ID + "=?",
                            new String[]{id + ""}
                    );
//                    Toast.makeText(MainActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    void getExpired(){

        Cursor cursor = getContentResolver().query(MedicationEntry.CONTENT_URI,
                null, MedicationEntry.COLUMN_EXPIRED +"=? ", new String[]{"1"}, null);
//        Toast.makeText(MainActivity.this, "Size = "+cursor.getCount(), Toast.LENGTH_SHORT).show();
        columnNames = cursor.getColumnNames();
        expiredList = new ContentValues[cursor.getCount()];
        int c = 0;

//        while (cursor.moveToNext() && c < columnNames.length) {
//            expiredList[c] = new ContentValues();
//            for (String col : columnNames) {
//
//                    if (col.equals(MedicationEntry.COLUMN_EXPIRED))
//                        expiredList[c].put(col, cursor.getInt(cursor.getColumnIndex(col)));
////                        Toast.makeText(MainActivity.this, col + " = "+cursor.getInt(cursor.getColumnIndex(col)), Toast.LENGTH_SHORT).show();
//                    else {
//
//                        expiredList[c].put(col, cursor.getString(cursor.getColumnIndex(col)));
////                        Toast.makeText(MainActivity.this,col+ " = "+cursor.getString(cursor.getColumnIndex(col)), Toast.LENGTH_SHORT).show();
//                    }
//            }
//
//            c++;
//        }
//
//        String result = "";
//        for(ContentValues cv : expiredList) {
//            for (String col : columnNames) {
//                result += cv.get(col) + "   ";
//            }
//            result += System.getProperty("line.separator");
//        }
//        hello.setText("looool");
//        hello.setText(result);


        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/helveticaneueltcom_thex.ttf");
        Typeface tfc = Typeface.createFromAsset(this.getAssets(), "fonts/HelveticaNeueLT_Arabic_55_Roman.ttf");

        TextView hName = (TextView) findViewById(R.id.hName);
        TextView hPDate = (TextView) findViewById(R.id.hPDate);
        TextView hEDate = (TextView) findViewById(R.id.hEDate);

        hName.setTypeface(tf); hPDate.setTypeface(tf);  hEDate.setTypeface(tf);


        if(cursor.getCount() >0) {
            header.setVisibility(View.VISIBLE);
            hello.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item, null, false);
                TextView item_name = (TextView) tableRow.findViewById(R.id.item_name);
                TextView item_PDate = (TextView) tableRow.findViewById(R.id.item_PDate);
                TextView item_EDate = (TextView) tableRow.findViewById(R.id.item_EDate);

                item_name.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_TYPE)));
                item_PDate.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_PRODUCTION_DATE)));
                item_EDate.setText(cursor.getString(cursor.getColumnIndex(MedicationEntry.COLUMN_EXPIRY_DATE)));

                tableLayout.addView(tableRow);
            }
        }
        else
        {
            header.setVisibility(View.GONE);
            hello.setVisibility(View.VISIBLE);
        }


    }

    public long getDateMillis(String toParse) {

        SimpleDateFormat formatter = new SimpleDateFormat(mDateFormat);
        Date date = null; // You will need try/catch around this
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }
}
