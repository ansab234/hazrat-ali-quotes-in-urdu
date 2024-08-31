package activities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ansab.hazrataliquotesinurdu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.imageAdapter;
import adapter.imageClass;


public class imagesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private imageAdapter mAdapter;
    private final String TAG = imagesActivity.class.getSimpleName();

    private DatabaseReference mDatabaseref;
    private List<imageClass> mImageClass;
    private ProgressBar mProgressCircle;

    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);


        mProgressCircle = findViewById(R.id.progress_circle);
        checkConnection();
        mRecyclerview = findViewById(R.id.recycler_view);
        mRecyclerview.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);


        mRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mRecyclerview.setHasFixedSize(true);
        mImageClass = new ArrayList<>();

        mDatabaseref = FirebaseDatabase.getInstance().getReference("images");
        mDatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    imageClass mclass = postSnapshot.getValue(imageClass.class);
                    mImageClass.add(mclass);
                }
                mAdapter = new imageAdapter(imagesActivity.this, mImageClass);


                mRecyclerview.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(imagesActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                mProgressCircle.setVisibility(View.GONE);

            }
        });

    }

    public void checkConnection() {

        ConnectivityManager manager = (ConnectivityManager) imagesActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {


            }


        } else {

            Toast.makeText(imagesActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            mProgressCircle.setVisibility(View.GONE);


        }
    }

}