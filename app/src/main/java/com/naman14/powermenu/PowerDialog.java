package com.naman14.powermenu;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import eu.chainfire.libsuperuser.Shell;

public class PowerDialog extends DialogFragment {

    public PowerDialog() {

    }

    LinearLayout power, reboot, soft_reboot, recovery, bootloader, safemode;
    FrameLayout frame, frame2;
    private CircularRevealView revealView;
    private View selectedView;
    private int backgroundColor;
    ProgressBar progress;
    TextView status, status_detail;

    private static final String SHUTDOWN_BROADCAST
            = "am broadcast android.intent.action.ACTION_SHUTDOWN";
    private static final String SHUTDOWN = "reboot -p";
    private static final String REBOOT_CMD = "reboot";
    private static final String REBOOT_SOFT_REBOOT_CMD = "setprop ctl.restart zygote";
    private static final String REBOOT_RECOVERY_CMD = "reboot recovery";
    private static final String REBOOT_BOOTLOADER_CMD = "reboot bootloader";
    private static final String[] REBOOT_SAFE_MODE
            = new String[]{"setprop persist.sys.safemode 1", REBOOT_SOFT_REBOOT_CMD};

    private static final int BG_PRIO = android.os.Process.THREAD_PRIORITY_BACKGROUND;
    private static final int RUNNABLE_DELAY_MS = 1000;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power, container, false);

        revealView = (CircularRevealView) view.findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#ffffff");
        power = (LinearLayout) view.findViewById(R.id.power);
        reboot = (LinearLayout) view.findViewById(R.id.reboot);
        soft_reboot = (LinearLayout) view.findViewById(R.id.soft_reboot);
        recovery = (LinearLayout) view.findViewById(R.id.recovery);
        bootloader = (LinearLayout) view.findViewById(R.id.bootloader);
        safemode = (LinearLayout) view.findViewById(R.id.safemode);

        frame = (FrameLayout) view.findViewById(R.id.frame);
        frame2 = (FrameLayout) view.findViewById(R.id.frame2);

        status = (TextView) view.findViewById(R.id.status);
        status_detail = (TextView) view.findViewById(R.id.status_detail);

        progress = (ProgressBar) view.findViewById(R.id.progress);


        progress.getIndeterminateDrawable().setColorFilter(
                Color.parseColor("#ffffff"),
                android.graphics.PorterDuff.Mode.SRC_IN);

        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#3f51b5");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Reboot");
                status_detail.setText("Rebooting...");

                new BackgroundThread(REBOOT_CMD).start();
            }
        });
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#d32f2f");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Power Off");
                status_detail.setText("Shutting down...");

                new BackgroundThread(SHUTDOWN).start();


            }
        });
        soft_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#e91e63");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Soft Reboot");
                status_detail.setText("Rebooting...");

                new BackgroundThread(REBOOT_SOFT_REBOOT_CMD).start();


            }
        });
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#8bc34a");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Reboot Recovery");
                status_detail.setText("Rebooting...");

                new BackgroundThread(REBOOT_RECOVERY_CMD).start();


            }
        });
        bootloader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#277b71");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Reboot Bootloader");
                status_detail.setText("Rebooting...");

                new BackgroundThread(REBOOT_BOOTLOADER_CMD).start();


            }
        });
        safemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.parseColor("#009688");
                final Point p = getLocationInView(revealView, v);

                if (selectedView == v) {
                    revealView.hide(p.x, p.y, backgroundColor, 0, 330, null);
                    selectedView = null;
                } else {
                    revealView.reveal(p.x / 2, p.y / 2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity) getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);

                status.setText("Safe Mode");
                status_detail.setText("Rebooting...");

                new BackgroundThread(REBOOT_SAFE_MODE).start();


            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound("P", Color.parseColor("#d32f2f"));
        ((ImageView) view.findViewById(R.id.ipower)).setImageDrawable(drawable1);

        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound("S", Color.parseColor("#009688"));
        ((ImageView) view.findViewById(R.id.isafe)).setImageDrawable(drawable2);

        TextDrawable drawable3 = TextDrawable.builder()
                .buildRound("B", Color.parseColor("#009688"));
        ((ImageView) view.findViewById(R.id.ibootloader)).setImageDrawable(drawable3);

        TextDrawable drawable4 = TextDrawable.builder()
                .buildRound("R", Color.parseColor("#009688"));
        ((ImageView) view.findViewById(R.id.irecovery)).setImageDrawable(drawable4);

        TextDrawable drawable5 = TextDrawable.builder()
                .buildRound("S", Color.parseColor("#e91e63"));
        ((ImageView) view.findViewById(R.id.isoftreboot)).setImageDrawable(drawable5);

        TextDrawable drawable6 = TextDrawable.builder()
                .buildRound("R", Color.parseColor("#3f51b5"));
        ((ImageView) view.findViewById(R.id.ireboot)).setImageDrawable(drawable6);


        return view;

    }

    private static void setThreadPrio(int prio) {
        android.os.Process.setThreadPriority(prio);
    }

    private static class BackgroundThread extends Thread {
        private Object sCmd;

        private BackgroundThread(Object cmd) {
            this.sCmd = cmd;
        }

        @Override
        public void run() {
            super.run();
            setThreadPrio(BG_PRIO);

            if (sCmd == null)
                return;

            /**
             * Sending a system broadcast to notify apps and the system that we're going down
             * so that they write any outstanding data that might need to be flushed
             */
            Shell.SU.run(SHUTDOWN_BROADCAST);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sCmd instanceof String)
                        Shell.SU.run((String) sCmd);
                    else if (sCmd instanceof String[])
                        Shell.SU.run((String[]) sCmd);
                }
            }, RUNNABLE_DELAY_MS);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.0f;

        window.setAttributes(windowParams);
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
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
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }


}
