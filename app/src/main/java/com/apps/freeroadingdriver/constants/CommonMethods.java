package com.apps.freeroadingdriver.constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.apps.freeroadingdriver.network.URLConstant;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Admin on 6/29/2017.
 */

public class CommonMethods {

    static ProgressDialog dialog;
    Dialog listDialog;
    static SimpleDateFormat dateFormat = new SimpleDateFormat("");
    public static boolean isEmpty(View view) {
        boolean isEmpty = true;
        if (view instanceof EditText) {
            isEmpty = TextUtils.isEmpty(((EditText) view).getText().toString().trim());
        } else if (view instanceof TextView) {
            isEmpty = TextUtils.isEmpty(((TextView) view).getText().toString().trim());
        }

        return isEmpty;
    }
    public static String getText(EditText editText){
        return editText==null ? "" : editText.getText().toString().trim();
    }
    public static void changePasswordTypeDynamically(EditText editText, boolean isShow,TextView textView){
        if (isShow){
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            textView.setTag("0");
            textView.setText("Hide");
        }else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            textView.setTag("1");
            textView.setText("Show");
        }
        editText.setSelection(editText.length());
    }
    public static String returnNotnullString(String value){
        return TextUtils.isEmpty(value) ? "" : value;
    }

    public static boolean isValidPinCode(String hex) {
        Pattern pattern = Pattern.compile("^\\p{Alpha}{2,}:",
                Pattern.UNICODE_CHARACTER_CLASS );
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    public static void showLongToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    public static void showShortToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
        String formattedDate = df.format(c.getTime());

        Log.d("Current Date:- ", formattedDate);
        return formattedDate;
    }
    public static String getCurrentTime12Format() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String formattedDate = df.format(c.getTime());

        Log.d("Current Time:- ", formattedDate);
        return formattedDate;
    }
    public enum AppStatus{
                 FINISHED("1"),
                 DROPPED("2"),
                 STARTED("3"),
                 ARRIVED("4"),
                 ACCEPTED("5"),
                 PENDING("6"),
                 REJECTED("7"),
                 CANCELLED_BY_DRIVER("8"),
                 CANCELLED_BY_PASSENGER("9"),
                 NO_RESPONSE("10"),
                 NO_DRIVER_FOUND("11"),
                 REQUEST("12");

        private final String value;
        AppStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static boolean isEmailValid(String Email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = Email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean validateEmail(String inputStr) {

        boolean isValid = false;


        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    public static ProgressDialog GetDialog(Context context, String title, String Message) {


        if (dialog != null) {
            dialog = null;
        }
        dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(Message);
        dialog.show();
        return dialog;
    }


    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static void errormessageon_Edittext(String message, EditText view) {

        int ecolor = Color.parseColor("#ff0000"); // whatever color you want
        String estring = message;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
        ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
        view.setError(ssbuilder);
        view.requestFocus();
    }

    /**
     * method used to get current time
     *
     * @return date and time both
     */
    public static String getFormattedCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        String dateTime = formatter.format(date);
        return dateTime;
    }
    public  static String custTtrim(String str, String c) {

        StringBuilder sb = new StringBuilder(str);

        try {
            while(str.startsWith(c))
            {
                str = sb.deleteCharAt(0).toString();
            }
            while(str.endsWith(c))
            {
                str = sb.deleteCharAt(str.length()-1).toString();
            }
        }catch (Exception e){
            return str;
        }
        return str;

    }

    public static void showSnackBar(ViewGroup viewGroup, Context context, String message){
        Snackbar snackbar = Snackbar
                .make(viewGroup, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
    public static LinearLayout.LayoutParams getLinearLayoutParams(int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        return params;
    }

    /**
     * method used to show alert dialog
     *
     * @param string get alert message
     */
    public static void showAlert(Context context, String title, String string) {
        // TODO Auto-generated method stub
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(string);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

            }
        });
        alert.show();

    }

    /**
     * method used to validate email
     *
     * @param email get email
     * @return true or false
     */
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * method used to get age from date of birth
     *
     * @param _month month
     * @param _day   day
     * @param _year  year
     * @return age
     */
    public static int getAge(int _month, int _day, int _year) {

        GregorianCalendar cal = new GregorianCalendar();
        int year, month, date, age;

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        age = year - cal.get(Calendar.YEAR);
        if ((month < cal.get(Calendar.MONTH))
                || ((month == cal.get(Calendar.MONTH)) && (date < cal.get(Calendar.DAY_OF_MONTH)))) {
            --age;
        }
        if (age < 0) {
            throw new IllegalArgumentException("Age < 0");
        }
        return age;
    }

    /**
     * method used to change date format
     *
     * @param date date
     * @return change format date
     */
    public static String changeDateFormat(String date) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date oneWayTripDate = input.parse(date);  // parse input
            date = output.format(oneWayTripDate);    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * method used to change the font
     *
     * @param context get context
     * @return changed font
     */

    /**
     * method used to change the font
     *
     * @param context get context
     * @return changed font
     */

    /**
     * method used to change the font
     *
     * @param context get context
     * @return changed font
     */


    /**
     * method used to remove a comma from last in string
     *
     * @param str contain comma seprated string
     * @return string
     */
    public static String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        Log.d("String is" + " String", str);
        return str;
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getWidth(Context mContext) {
        int width = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        } else {
            width = display.getWidth();  // deprecated
        }
        return width;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getHeight(Context mContext) {
        int height = 0;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT > 12) {
            Point size = new Point();
            display.getSize(size);
            height = size.y;
        } else {
            height = display.getHeight();  // deprecated
        }
        return height;
    }

    public static Date getTime(String sTime) {
        try {
            return new SimpleDateFormat("hh:mm aa").parse(sTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateString() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());

        Log.d("Current Date:- ", strDate);

        return strDate;
    }
    public static String getCurrentDateOnly() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());

        Log.d("Current Date:- ", strDate);

        return strDate;
    }


    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date getMonthLaterDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public static String getDateString(Date date) {
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(date);
        return strDate;
    }

    public static String getDateAndTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Log.d("Current Date:- ", formattedDate);
        return formattedDate;
    }

    public static String getConvertedDate(String oldDate) {
        Date date = null;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat write = new SimpleDateFormat("dd-MMM-yyyy");
        write.setTimeZone(TimeZone.getDefault());
        try {
            date = parser.parse(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return write.format(date);
    }

    public static double metersToMiles(double distance) {
        return distance / 1609.34;
    }

    public static String getImageUrl(String profile_pic) {
        return URLConstant.Companion.getPASSENGER_IMAGE_URL() + profile_pic;
    }
    public static String passengerImageUrl(String profile_pic) {
        return URLConstant.Companion.getPASSENGER_IMAGE_URL() + profile_pic;
    }
    public static String getDriverImageUrl(String profile_pic) {
        return URLConstant.Companion.getDRIVER_IMAGE_URL() + profile_pic;
    }

    public static String getTimeZone() {
        return TimeZone.getDefault().getID();
    }

    public static int convertBooleanToInt(boolean isChecked) {
        return isChecked ? 1 : 0;
    }

    public static boolean convertIntToBoolean(int value) {
        return value == 1;
    }
}
