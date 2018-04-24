package com.example.piotr.bakingapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.piotr.bakingapp.R;

public class UiHelper {
    public static final String KEY_RECYCLER_STATE = "key_recycler_state";
    public static final String KEY_CAKE_LIST = "key_cake_list";
    public static final String KEY_CAKE_ITEM = "key_cake_item";
    public static final String KEY_INGREDIENT_LIST = "key_ingredient_list";
    public static final String KEY_STEPS_LIST = "key_steps_list";

    public static boolean isPhone(Context context) {
        try {
            context.getResources().getBoolean(R.bool.isTablet);
            return false;
        }catch(Resources.NotFoundException e){
            return true;
        }
    }

    public static int getColumnCount(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
