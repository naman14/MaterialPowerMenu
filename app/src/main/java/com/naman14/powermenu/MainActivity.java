package com.naman14.powermenu;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import eu.chainfire.libsuperuser.Shell;


public class MainActivity extends ActionBarActivity implements DialogInterface.OnDismissListener{


    private CircularRevealView revealView;
    private View selectedView;
    private int backgroundColor;
    RelativeLayout layout;
    LinearLayout source,rate,share,about,shortcut;
    private ImageView button;
    int maxX,maxY;
    android.os.Handler handler;
    private static final int BG_PRIO = android.os.Process.THREAD_PRIORITY_BACKGROUND;
    protected TextView mRootStatusSummary;

    String Urlgithub="https://github.com/naman14/MaterialPowerMenu";
    String Urlrate="https://play.google.com/store/apps/details?id=com.naman14.powermenu";
    String Urlapps="https://play.google.com/store/apps/developer?id=Naman14";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Material Power Menu");


        revealView=(CircularRevealView) findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#11303030");
         button =(ImageView) findViewById(R.id.button);
        mRootStatusSummary=(TextView) findViewById(R.id.rootstatus);
        layout=(RelativeLayout) findViewById(R.id.layout);

        source=(LinearLayout) findViewById(R.id.source);
        rate=(LinearLayout) findViewById(R.id.rate);
        share=(LinearLayout) findViewById(R.id.share);
        about=(LinearLayout) findViewById(R.id.about);
        shortcut=(LinearLayout) findViewById(R.id.shortcut);

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



           }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                setThreadPrio(BG_PRIO);

                if (Shell.SU.available()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (mRootStatusSummary != null) {
                                mRootStatusSummary.setText("Available");
                            }
                        }
                    });
                }
            }
        }).start();

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlgithub));
                startActivity(i);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlrate));
                startActivity(i);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlapps));
                startActivity(i);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Material Power Menu");
                String sAux = "\nCheck out this beautiful app to add rebooting functionality to your rooted android device\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.naman14.powermenu \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            }
        });
        shortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShortcut();
            }
        });
    }
    private static void setThreadPrio(int prio) {
        android.os.Process.setThreadPriority(prio);
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
        View v=button;
        final Point p=getLocationInView(revealView, v);

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

                layout.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                XposedMainActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "PowerMenuShortcut");
        addIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        addIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.ic_reboot));
        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
        Toast.makeText(getApplicationContext(),"Added Home Screen Shortcurt",Toast.LENGTH_SHORT).show();

    }
}
