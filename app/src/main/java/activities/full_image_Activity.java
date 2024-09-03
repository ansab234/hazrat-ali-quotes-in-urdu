package activities;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ansab.hazrataliquotesinurdu.BuildConfig;
import com.ansab.hazrataliquotesinurdu.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
//import com.github.chrisbanes.photoview.BuildConfig;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.squareup.picasso.BuildConfig;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import activities.ads.AdsCallBack;
import activities.ads.AdsManager;

public class full_image_Activity extends AppCompatActivity {
    PhotoView imageView;
    Button download_button;
//    LazyLoader lazyLoader;
    Button btn_share;
    ProgressBar progressBar;
    private final String TAG = full_image_Activity.class.getSimpleName();
    FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_);
        AdsManager.loadBannerAd(this, findViewById(R.id.banner_container));


        imageView = findViewById(R.id.viewImage);
        download_button = findViewById(R.id.button_download);
//        lazyLoader = findViewById(R.id.dotsloader);
        btn_share = findViewById(R.id.button_share);
        progressBar = findViewById(R.id.progress_bar);
        firebaseAnalytics=FirebaseAnalytics.getInstance(this);

        progressBar.setVisibility(View.VISIBLE);


//        Picasso.get().load(getIntent().getStringExtra("images"))
//                //.placeholder(R.mipmap.ic_launcher)
//                .into(imageView);
//
//        }

        Picasso.get().load(getIntent().getStringExtra("images"))
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // Hide the progress bar when the image is successfully loaded
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Hide the progress bar in case of error
                        progressBar.setVisibility(View.GONE);
                        makeText(full_image_Activity.this, "Failed to load image", LENGTH_SHORT).show();
                    }
                });
    }


    public void downloadWallpaper() {

//        PermissionListener permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                //      Toast.makeText(view_full_wallpaper_Activity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                makeText(full_image_Activity.this, "Permission Denied\n" + deniedPermissions.toString(), LENGTH_SHORT).show();
//
//            }
//        };
//
//
//        TedPermission.with(this)
//                .setPermissionListener(permissionListener)
//                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .check();


        FileOutputStream fileOutputStream = null;
        File file = getDisc();
        if (!file.exists() && !file.mkdirs()) {
            makeText(this, "Can't create directory to save image", LENGTH_SHORT).show();
            return;
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmhhssmm");
        String date = simpleDateFormat.format(new Date());
        String name = "Img" + date + ".jpg";
        String file_name = file.getAbsolutePath() + "/" + name;
        File new_file = new File(file_name);
        try {
            //  btn_download.animate().rotation(imageView.getRotation() + 360).start();
            fileOutputStream = new FileOutputStream(new_file);
            Bitmap bitmap = viewToBitmap(imageView, imageView.getWidth(), imageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            makeText(this, "Image Saved in Gallery!", LENGTH_SHORT).show();

            AdsManager.showInterstitialAd(this, new AdsCallBack() {
                @Override
                public void onClosed() {

                }
            });


            Bundle bundle = new Bundle();
            bundle.putString("save_image","Save Image Button");
            firebaseAnalytics.logEvent("save_button", bundle);


            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }

    public static Bitmap viewToBitmap(View view, int width, int height) {
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        view.draw(canvas);
        return bm;
    }


    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }

    private File getDisc() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Hazrat Ali(R.A) Quotes");

    }


    public void save_btn(View view) {

        if (imageView.getDrawable() == null) {

//            LazyLoader loader = new LazyLoader(full_image_Activity.this, 30, 20,
//                    ContextCompat.getColor(full_image_Activity.this, R.color.loader_selected),
//                    ContextCompat.getColor(full_image_Activity.this, R.color.loader_selected),
//                    ContextCompat.getColor(full_image_Activity.this, R.color.loader_selected));
//            loader.setAnimDuration(500);
//            loader.setFirstDelayDuration(100);
//            loader.setSecondDelayDuration(200);
//            loader.setInterpolator(new LinearInterpolator());
//
//            lazyLoader.addView(loader);


            makeText(full_image_Activity.this, "Image is loading...", LENGTH_SHORT).show();
        } else {
            downloadWallpaper();

        }

    }

    public void share(View view) {

        if (imageView.getDrawable() == null) {

//            LazyLoader loader = new LazyLoader(full_image_Activity.this, 30, 20,
//                    ContextCompat.getColor(full_image_Activity.this, R.color.colorPrimaryDark),
//                    ContextCompat.getColor(full_image_Activity.this, R.color.colorPrimaryDark),
//                    ContextCompat.getColor(full_image_Activity.this, R.color.colorPrimaryDark));
//            loader.setAnimDuration(500);
//            loader.setFirstDelayDuration(100);
//            loader.setSecondDelayDuration(200);
//            loader.setInterpolator(new LinearInterpolator());
//
//            lazyLoader.addView(loader);


            makeText(full_image_Activity.this, "Image is loading...", LENGTH_SHORT).show();
        } else {
            shareImage();
        }

    }


//    public void shareImage() {
//        progressBar.setVisibility(View.VISIBLE);
//        //  btn_share.animate().rotation(imageView.getRotation() +360).start();
//
//
//        //     Glide.with(this).load(getIntent().getStringExtra("images")).into(imageView);
//
//        Glide.with(this)
//                .asBitmap()
//                .load(getIntent().getStringExtra("images"))
//                .into(new SimpleTarget<Bitmap>() {
//                          @Override
//                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//
//                              progressBar.setVisibility(View.GONE);
//                              Intent intent = new Intent(Intent.ACTION_SEND);
//                              intent.setType("image/*");
//                              intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
//
//                              startActivity(Intent.createChooser(intent, "Share Image"));
//
//
//                              Bundle bundle = new Bundle();
//                              bundle.putString("share_image","Share Image Button");
//                              firebaseAnalytics.logEvent("share_button", bundle);
//                          }
//                      }
//                );
//    }

    public void shareImage() {
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(this)
                .asBitmap()
                .load(getIntent().getStringExtra("images"))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        progressBar.setVisibility(View.GONE);

                        Uri bmpUri = getLocalBitmapUri(resource);
                        if (bmpUri != null) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission for the URI
                            startActivity(Intent.createChooser(intent, "Share Image"));

                            Bundle bundle = new Bundle();
                            bundle.putString("share_image", "Share Image Button");
                            firebaseAnalytics.logEvent("share_button", bundle);
                        } else {
                            makeText(full_image_Activity.this, "Failed to get image URI for sharing", LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        progressBar.setVisibility(View.GONE);
                        makeText(full_image_Activity.this, "Failed to load image for sharing", LENGTH_SHORT).show();
                    }
                });
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "hd_wallpapers_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file); // Ensure this matches the provider authority
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            makeText(this, "File not found", LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            makeText(this, "Failed to save image", LENGTH_SHORT).show();
        }
        return bmpUri;
    }

}
