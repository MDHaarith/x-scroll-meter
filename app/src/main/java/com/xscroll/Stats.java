package com.xscroll;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.LocalDate;
import java.util.Locale;

/** Daily totals in SharedPreferences, keyed "yyyy-MM-dd" + field. */
final class Stats {
    private static final String[] FIELDS = {"px", "posts", "reels", "ms"};

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getSharedPreferences("stats", Context.MODE_PRIVATE);
    }

    /** Adds the counter's pending totals to today, then zeroes them. */
    static void add(Context ctx, Counter c) {
        String day = LocalDate.now().toString();
        SharedPreferences p = prefs(ctx);
        long[] v = {c.px, c.posts, c.reels, c.ms};
        SharedPreferences.Editor ed = p.edit();
        for (int i = 0; i < FIELDS.length; i++) {
            String k = day + FIELDS[i];
            ed.putLong(k, p.getLong(k, 0) + v[i]);
        }
        ed.apply();
        c.px = c.posts = c.reels = c.ms = 0;
    }

    static String report(Context ctx) {
        SharedPreferences p = prefs(ctx);
        float ydpi = ctx.getResources().getDisplayMetrics().ydpi;
        StringBuilder sb = new StringBuilder();
        LocalDate d = LocalDate.now();
        for (int i = 0; i < 7; i++, d = d.minusDays(1)) {
            String k = d.toString();
            double meters = p.getLong(k + "px", 0) / ydpi * 0.0254;
            sb.append(i == 0 ? "TODAY" : k).append('\n')
              .append(String.format(Locale.US, "  %.0f m scrolled · %d min\n  %d posts · %d reels\n",
                      meters, p.getLong(k + "ms", 0) / 60_000, p.getLong(k + "posts", 0), p.getLong(k + "reels", 0)));
            if (i == 0) sb.append(String.format(Locale.US, "  = %.2f× Burj Khalifa (828 m)\n", meters / 828));
            sb.append('\n');
        }
        return sb.toString();
    }
}
