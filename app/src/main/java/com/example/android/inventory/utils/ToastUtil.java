package com.example.android.inventory.utils;

import android.content.Context;
import android.widget.Toast;

public final class ToastUtil {

    private ToastUtil() {

    }

    private static Toast INSTANCE = null;

    public static void show(Context context, String message) {
        if (INSTANCE == null) {
            INSTANCE = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            INSTANCE.setText(message);
            INSTANCE.setDuration(Toast.LENGTH_SHORT);
        }
        INSTANCE.show();
    }

    public static void show(Context context, int stringResId) {
        show(context, context.getString(stringResId));
    }
}