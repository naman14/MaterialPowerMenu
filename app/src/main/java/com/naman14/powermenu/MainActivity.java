package com.naman14.powermenu;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements DialogInterface.OnDismissListener{


    private CircularRevealView revealView;
    private View selectedView;
    private int backgroundColor;
    private Button button;
    int maxX,maxY;
    android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        revealView=(CircularRevealView) findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#212121");
         button =(Button) findViewById(R.id.button);

        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        maxX = mdispSize.x;
         maxY = mdispSize.y;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#00bcd4");
                final Point p = getLocationInView(revealView, v);

                    revealView.reveal(p.x, p.y, color, v.getHeight() / 2, 440, null);
                    selectedView = v;

                showPowerDialog();
                button.setVisibility(View.GONE);
            }
        });
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void revealFromTop() {
        final int color = Color.parseColor("#ffffff");


        final Point p = new Point(maxX/2,maxY/2);

            revealView.reveal(p.x, p.y, color, button.getHeight() / 2, 440, null);


    }

    private void showPowerDialog() {
        FragmentManager fm = getFragmentManager();
        PowerDialog editNameDialog = new PowerDialog();
        editNameDialog.show(fm, "fragment_power");

    }
    @Override
    public void onDismiss(final DialogInterface dialog) {
        final Point p=new Point(maxX/2,maxY/2);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
            }
        }, 300);


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               button.setVisibility(View.VISIBLE);
            }
        }, 500);

    }
}
