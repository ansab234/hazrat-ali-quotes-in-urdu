package activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ansab.hazrataliquotesinurdu.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import activities.ads.AdsCallBack;
import activities.ads.AdsManager;


public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backtoast;
    private final String TAG = MainActivity.class.getSimpleName();
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdsManager.loadInterstitial(this);



        firebaseAnalytics=FirebaseAnalytics.getInstance(this);
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_container));


    }


    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backtoast.cancel();
            super.onBackPressed();

            return;
        } else {

            backtoast = Toast.makeText(this, "press back again to exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }

    public void click(View view) {

        Intent i = new Intent(MainActivity.this, imagesActivity.class);
//        startActivity(i);
        AdsManager.showInterstitialAd(this, new AdsCallBack() {
            @Override
            public void onClosed() {
                startActivity(i);
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("Read_Quotes","Quotes Read Start Button");
        firebaseAnalytics.logEvent("start_button", bundle);

    }


    public void more_btn(View view) {


        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=Ansab+Iqbal")));

        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=Ansab+Iqbal")));

    }

    public void share_btn(View view) {
        Intent myintent = new Intent(Intent.ACTION_SEND);
        myintent.setType("text/plan");

        String shereBoday = "Your Boday Here";

        String shereSub = "Check out Hazrat Ali Quotes App on Google Play \n\"http://play.google.com/store/apps/details?id=" + getPackageName();
        myintent.putExtra(Intent.EXTRA_SUBJECT, shereBoday);
        myintent.putExtra(Intent.EXTRA_TEXT, shereSub);
        startActivity(Intent.createChooser(myintent, "Share Using"));

    }

    public void exit_btn(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit!")

                .setMessage(" Do you really want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
