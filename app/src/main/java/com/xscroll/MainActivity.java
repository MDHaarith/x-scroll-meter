package com.xscroll;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private Button toggle;
    private TextView report;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(48, 48, 48, 48);

        toggle = new Button(this);
        toggle.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        report = new TextView(this);
        report.setTextSize(18);
        report.setTypeface(Typeface.MONOSPACE);
        report.setPadding(0, 32, 0, 0);
        col.addView(toggle);
        col.addView(report);

        ScrollView root = new ScrollView(this);
        root.setFitsSystemWindows(true);
        root.addView(col);
        setContentView(root);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String enabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        boolean on = enabled != null && enabled.contains(getPackageName() + "/");
        toggle.setText(on ? "Tracking ON · tap to change" : "Tracking OFF · tap to enable");
        report.setText(Stats.report(this));
    }
}
