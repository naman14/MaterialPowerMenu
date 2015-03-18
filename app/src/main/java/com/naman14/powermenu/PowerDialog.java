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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class PowerDialog extends DialogFragment {

    public PowerDialog(){

    }

    LinearLayout power,reboot;
    FrameLayout frame,frame2,frame3;
    private CircularRevealView revealView;
    private View selectedView;
    private int backgroundColor;
    ProgressBar progress,progress2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_power,container,false);



        revealView=(CircularRevealView) view.findViewById(R.id.reveal);
        backgroundColor = Color.parseColor("#ffffff");
        power=(LinearLayout) view.findViewById(R.id.power);
        reboot=(LinearLayout) view.findViewById(R.id.reboot);
        frame=(FrameLayout) view.findViewById(R.id.frame);
        frame2=(FrameLayout) view.findViewById(R.id.frame2);
        frame3=(FrameLayout) view.findViewById(R.id.frame3);
        progress=(ProgressBar) view.findViewById(R.id.progress);
        progress2=(ProgressBar) view.findViewById(R.id.progress2);

        progress.getIndeterminateDrawable().setColorFilter(
                Color.parseColor("#ffffff"),
                android.graphics.PorterDuff.Mode.SRC_IN);
        progress2.getIndeterminateDrawable().setColorFilter(
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
                    revealView.reveal(p.x/2, p.y/2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity)getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame3.setVisibility(View.VISIBLE);
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
                    revealView.reveal(p.x/2, p.y/2, color, v.getHeight() / 2, 440, null);
                    selectedView = v;
                }

                ((MainActivity)getActivity()).revealFromTop();
                frame.setVisibility(View.GONE);
                frame2.setVisibility(View.VISIBLE);


            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;

    }

    @Override public void onStart() {
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
