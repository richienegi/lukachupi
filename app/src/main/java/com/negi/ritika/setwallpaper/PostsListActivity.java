package com.negi.ritika.setwallpaper;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.negi.ritika.setwallpaper.classData.All_Images;

import java.util.ArrayList;
import java.util.List;

public class PostsListActivity extends AppCompatActivity {

    ProgressDialog pd;

    RecyclerView mRecylerview;

    DatabaseReference mFirebaseDatabase;

    private GridLayoutManager layoutManager;

    String cate;

    List<All_Images> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        mRecylerview = findViewById(R.id.recyclerView);

        data = new ArrayList<>();

        cate = getIntent().getStringExtra("categ");
        //mRecylerview.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        //set Layout as Linear layout
        layoutManager = new GridLayoutManager(this, 2);

        mRecylerview.setLayoutManager(staggeredGridLayoutManager);
        //send Query to firebase
        Log.d("one", "running");

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("uploads").child(cate);
//        Toast.makeText(this, ""+mFirebaseDatabase.getKey(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();

        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();

                Toast.makeText(PostsListActivity.this, ""+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();

                for(DataSnapshot ps : dataSnapshot.getChildren())
                {
//                    Toast.makeText(PostsListActivity.this, "", Toast.LENGTH_SHORT).show();
                    All_Images image = ps.getValue(All_Images.class);
                    data.add(image);
                }

                Toast.makeText(PostsListActivity.this, ""+data.size(), Toast.LENGTH_SHORT).show();
//                PostListAdapter ad = new PostListAdapter(PostsListActivity.this, data);
//                mRecylerview.setAdapter(ad);

                if(pd.isShowing())
                {
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }
}
