package com.droiddev.mansionbooking;

/**
 * Created by iiro on 7.6.2016.
 */
public class TabMessage {
    public static String get(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.tab_home:
                message += "recents";
                break;
            case R.id.tab_search:
                message += "favorites";
                break;
            case R.id.tab_map:
                message += "nearby";
                break;
            case R.id.tab_top:
                message += "friends";
                break;
            case R.id.tab_user:
                message += "food";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }
}
