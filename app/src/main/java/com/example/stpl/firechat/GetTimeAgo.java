package com.example.stpl.firechat;

import android.app.Application;
import android.content.Context;

/**
 * Created by Praneeth on 9/26/2017.
 */

public class GetTimeAgo extends Application {

    private static final int SECONDS_MILLIS = 1000;
    private static final int MINUTES_MILLIS = 60 * SECONDS_MILLIS;
    private static final int HOURS_MILLIS = 60 * MINUTES_MILLIS;
    private static final int DAYS_MILLIS = 24 * HOURS_MILLIS;

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        final long diff = now - time;

        if (diff < MINUTES_MILLIS) {
            return "just now";

        } else if (diff < 2 * MINUTES_MILLIS) {
            return "a minute ago";

        } else if (diff < 50 * MINUTES_MILLIS) {
            return diff / MINUTES_MILLIS + "minutes ago";

        } else if (diff < 90 * MINUTES_MILLIS) {
            return "a hour ago";

        } else if (diff < 24 * HOURS_MILLIS) {
            return diff / HOURS_MILLIS + "hours ago";

        } else if (diff < 48 * HOURS_MILLIS) {
            return "yesterday";

        } else {
            return diff / DAYS_MILLIS + "days ago";

        }

    }
}
