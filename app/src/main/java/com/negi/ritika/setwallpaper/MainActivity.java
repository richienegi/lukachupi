package com.negi.ritika.setwallpaper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//add two dependency recycler view and card view
public class MainActivity extends AppCompatActivity implements my_adapter.RecylerListener
{
    String category="";
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView)findViewById(R.id.rview);
        my_adapter myadpt = new my_adapter(this,nature_model.getObjectList());
        myadpt.setOnClick(this);
        rv.setAdapter(myadpt);
        LinearLayoutManager lm = new LinearLayoutManager(this);//to show card view, recycler View
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(lm);
        rv.setItemAnimator(new DefaultItemAnimator());//
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