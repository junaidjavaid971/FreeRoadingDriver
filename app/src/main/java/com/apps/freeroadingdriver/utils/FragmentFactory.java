package com.apps.freeroadingdriver.utils;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apps.freeroadingdriver.R;

public class FragmentFactory {
    private static String TAG = FragmentFactory.class.getName();

    public static void replaceFragment(Fragment fragment, int id, Context context) {
        Log.d(TAG, "on replaceFragment without tag method");
        String backStateName = fragment.getClass().getName();
        Log.e("cur", backStateName);
        if (!backStateName.equals(getCurrentFragment(context))) {
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(id, fragment).commit();
        }
    }

    public static void replaceFragmentWithAnim(Fragment fragment, int id, Context context) {
        Log.d(TAG, "on replaceFragmentWithAnim without tag method");
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(id, fragment).commit();
    }

    public static void addFragment(Fragment fragment, int id, Context context, String TAG) {
        Log.d(TAG, "on addFragment method");
        Fragment fragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag == null) {
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(id, fragment).addToBackStack(TAG).commit();
        }
    }

    public static void replaceFragment(Fragment fragment, int id, Context context, String TAG) {
        Log.d(TAG, "on replaceFragment method");
        Fragment fragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag == null) {
            String backStateName = fragment.getClass().getName();
            Log.e("cur", backStateName);
            if (!backStateName.equals(getCurrentFragment(context))) {
                FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(id, fragment).addToBackStack(TAG).commit();
            }
        }
    }

    //This method will only replace the fragment if and only if the fragment which you are trying to replace with is not available
    public static void replaceFragmentWithAnim(Fragment fragment, int id, Context context, String TAG) {
        Log.d(TAG, "on replaceFragmentWithAnim method");
        Fragment fragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag == null) {
            String backStateName = fragment.getClass().getName();
            Log.e("cur", backStateName);
            if (!backStateName.equals(getCurrentFragment(context))) {
                FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(id, fragment).addToBackStack(TAG).commit();
            }
        }
    }

    //This method will only add the fragment if and only if the fragment which you are trying to replace with is not available
    public static void addFragmentWithAnim(Fragment fragment, int id, Context context, String TAG) {
        Log.d(TAG, "on replaceFragmentWithAnim method");
        Fragment fragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag == null) {
            String backStateName = fragment.getClass().getName();
            Log.e("cur", backStateName);
            if (!backStateName.equals(getCurrentFragment(context))) {
                FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.add(id, fragment).addToBackStack(TAG).commit();
            }
        }
    }

    public static void addFragment(Fragment fragment, int id, Context context) {
        Log.d(TAG, "on addFragment without tag method");
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(id, fragment).commit();
    }

    public static void replaceFragmentBackStack(Fragment fragment, int id, Context context, String TAG) {
        Log.d(TAG, "on replaceFragmentBackStack with tag method");
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(id, fragment).addToBackStack(TAG).commit();
    }

    public static void back(Context context) {
        Log.d(TAG, "on back method");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();

        } else {
            ((FragmentActivity) context).finish();
        }
    }

    public static boolean removedBack(Context context) {
        Log.d(TAG, "on reomve back method");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    public static boolean isFragmentStackIsEmpty(Context context) {
        Log.d(TAG, "on isFragmentStackIsEmpty method");
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() >= 1) {
            return false;
        }
        return true;
    }

    public static String getCurrentFragment(Context context) {
        String str = "";
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            FragmentManager.BackStackEntry backEntry = fm.getBackStackEntryAt(count - 1);
            if (backEntry != null) {
                str = backEntry.getName();
            }
        }
        return str;
    }

   /* public static boolean isFragmentVisible(Context context,int id,String tag){
        boolean flag = false;
        android.support.v4.app.FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(id);
        if (currentFragment!=null) {
            if (currentFragment.getTag().equals(tag)) {
                flag = true;
            } else flag = false;
        }
        return flag;
    }*/
}