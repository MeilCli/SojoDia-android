package com.numero.sojodia;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.numero.sojodia.Adapter.ReciprocatingFragmentPagerAdapter;
import com.numero.sojodia.Utils.ApplicationPreferences;

public class MainActivity extends AppCompatActivity {

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ReciprocatingFragmentPagerAdapter fragmentPagerAdapter = new ReciprocatingFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if(ApplicationPreferences.isFirstBoot(this)) {
            showDescription();
            ApplicationPreferences.setFirstBoot(this, false);
        }
    }

    private void showDescription(){
        View view = getLayoutInflater().inflate(R.layout.description_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText("ようこそ");
        TextView message = (TextView)view.findViewById(R.id.message);
        message.setText("駅名をタップすると時刻表を見ることができます");
        Button positiveButton = (Button)view.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

        dialog = builder.create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}