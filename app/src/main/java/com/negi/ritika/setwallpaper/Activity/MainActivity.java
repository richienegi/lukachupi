package com.negi.ritika.setwallpaper.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.negi.ritika.setwallpaper.R;
import com.negi.ritika.setwallpaper.Adapters.my_adapter;
import com.negi.ritika.setwallpaper.Models.nature_model;

//add two dependency recycler view and card view
public class MainActivity extends AppCompatActivity implements my_adapter.RecylerListener {

    String category="";
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView)findViewById(R.id.rview);
        my_adapter myadpt = new my_adapter(this, nature_model.getObjectList());
        myadpt.setOnClick(this);
        rv.setAdapter(myadpt);
        LinearLayoutManager lm = new LinearLayoutManager(this);//to show card view, recycler View
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Onlick(int poistion) {
        switch (poistion)
        {
            case 0:
                category="Wildlife";
                break;
            case 1:
                category="Insects";
                break;
            case 2:
               category="Flowers";
               break;
            case 3:
                category="Landscape";
                break;


        }
        Intent i=new Intent(MainActivity.this,PostsListActivity.class);
        i.putExtra("categ",category);
        startActivity(i);
    }
}