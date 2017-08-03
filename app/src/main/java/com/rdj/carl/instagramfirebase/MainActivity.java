package com.rdj.carl.instagramfirebase;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {
    /**************************************************/
    private FirebaseRemoteConfig firebaseRemoteConfig;
    /**************************************************/
    private Button bSync;
    private LinearLayout llMainActivity;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llMainActivity = (LinearLayout) findViewById(R.id.llMainActivity);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        bSync = (Button) findViewById(R.id.bSync);

        /**************************************************/
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();

        firebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        setConfigurationView();

        /**************************************************/

    }

    private void setConfigurationView() {
        llMainActivity.setBackgroundColor(Color.parseColor(firebaseRemoteConfig.getString("color_background")));

        if (firebaseRemoteConfig.getString("image_background").equals("logo")){
            ivImage.setImageResource(R.drawable.logo);
        }else if (firebaseRemoteConfig.getString("image_background").equals("custom_web_application_development")){
            ivImage.setImageResource(R.drawable.custom_web_application_development);
        }else if (firebaseRemoteConfig.getString("image_background").equals("website_development")){
            ivImage.setImageResource(R.drawable.website_development);
        }
    }

    public void syncData(View view){
        long cacheExpiration = 3600;
        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sync terminado", Toast.LENGTH_SHORT).show();
                    firebaseRemoteConfig.activateFetched();
                }else {
                    Toast.makeText(MainActivity.this, "Sync error", Toast.LENGTH_SHORT).show();
                }
                setConfigurationView();
            }
        });
    }
}
