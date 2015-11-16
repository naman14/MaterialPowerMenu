package com.naman14.powermenu;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;


public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {


    private CircularRevealView revealView;
    private View selectedView;
    private int backgroundColor;
    int maxX, maxY;
    android.os.Handler handler;
    private FloatingActionButton fabPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getString("pref_theme", "light").equals("dark"))
            setTheme(R.style.ThemeBaseDark);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Material Power Menu");

        fabPower = (FloatingActionButton) findViewById(R.id.fab);
        revealView = (CircularRevealView) findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#11303030");

        if (preferences.getBoolean("pref_autolaunch", true)) {
            View v = fabPower;
            launchPowerMenu(v);
        }

        android.preference.PreferenceFragment fragment = new PreferenceFragment();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.pref_container, fragment).commit();

        fabPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPowerMenu(v);
            }
        });

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
        maxY = mdispSize.y;


    }

    private Point getLocationInView(View src, View target) {
        final int[] l0 = new int[2];
        src.getLocationOnScreen(l0);

        final int[] l1 = new int[2];
        target.getLocationOnScreen(l1);

        l1[0] = l1[0] - l0[0] + target.getWidth() / 2;
        l1[1] = l1[1] - l0[1] + target.getHeight() / 2;

        return new Point(l1[0], l1[1]);
    }


    public void revealFromTop() {
        final int color = getBackgroundColor();

        final Point p = new Point(maxX / 2, maxY / 2);

        revealView.reveal(p.x, p.y, color, fabPower.getHeight() / 2, 440, null);

    }

    private void launchPowerMenu(View v) {
        final int color = getPrimaryColor();
        final Point p = getLocationInView(revealView, v);

        revealView.reveal(p.x, p.y, color, v.getHeight() / 2, 370, null);
        selectedView = v;

        FabAnimationUtils.scaleOut(fabPower, new FabAnimationUtils.ScaleCallback() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {
                fabPower.setImageResource(R.drawable.close);
                FabAnimationUtils.scaleIn(fabPower);
            }
        });

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showPowerDialog();
            }
        }, 160);
    }

    private void showPowerDialog() {
        FragmentManager fm = getFragmentManager();
        PowerDialog powerDialog = new PowerDialog();
        powerDialog.show(fm, "fragment_power");

    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        View v = fabPower;
        final Point p = getLocationInView(revealView, v);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                FabAnimationUtils.scaleOut(fabPower, new FabAnimationUtils.ScaleCallback() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        fabPower.setImageResource(R.drawable.power);
                        FabAnimationUtils.scaleIn(fabPower);
                    }
                });
            }
        }, 300);

    }

    private int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }

    private int getBackgroundColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = obtainStyledAttributes(typedValue.data, new int[]{R.attr.windowBackground});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }

}
