package com.apps.freeroadingdriver.utils;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;


import com.apps.freeroadingdriver.FreeRoadingApp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by atiqulalam on 27/11/15.
 */
public class LocationUtil {
    public static int PICK_UP_LOACTION=1;
    public final static String TAG = LocationUtil.class.getSimpleName();

    public static String placeTask(String place){
        String data = "";

        // Obtain browser key from https://code.google.com/apis/console
        String key = "AIzaSyA2cOn3RIvxtI_KclGBHdH7KrAR3oCM41I";
        //String key = com.apps.freeroadingdriverantUtils.LOCATION_KEY;

        String input="";

        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // place type to be searched
        String types = "types=geocode";

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = input+"&"+types+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

        try{
            // Fetching the data from web service in background
            data = downloadUrl(url);
        }catch(Exception e){
            Log.d("Background Task", e.toString());
        }
        return data;
    }

 /*   public static List<HashMap<String, String>> parserTask(String jsonData){
        List<HashMap<String, String>> places = null;

        PlaceJSONParser placeJsonParser = new PlaceJSONParser();

        try{
            JSONObject jObject = new JSONObject(jsonData);

            // Getting the parsed data as a List construct
            places = placeJsonParser.parse(jObject);
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        return places;
    }*/

    /** A method to download json data from url */
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuilder sb  = new StringBuilder();

            String line;
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception download url",e+"");
        }finally{
            assert iStream != null;
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public static String getAddressFromLatLong(double latitude, double longitude) {

        String fullAddress=null;
        Geocoder geocoder = new Geocoder(FreeRoadingApp.getInstance(), Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;
        ArrayList<String> addressFragments;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e(TAG, "error", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e(TAG, "address not found for . " +
                    "Latitude = " + latitude +
                    ", Longitude = " + longitude, illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            Log.e(TAG, "address not found");
        } else {
            Address address = addresses.get(0);
            addressFragments = new ArrayList<>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            String locality=address.getLocality() ;//("Mountain View", for example)
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            fullAddress= address.getFeatureName()+", "+addressFragments.get(0);
        }
        return fullAddress;
    }

    /**
     * method used to get distance in miles
     * @param srcLat contain begin lat
     * @param srcLng contain begin long
     * @param desLat contain drop lat
     * @param desLng contain drop long
     * @return distance in miles
     */
    public static double distanceBetweenTwoPoint(double srcLat, double srcLng, double desLat, double desLng) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(desLat - srcLat);
        double dLng = Math.toRadians(desLng - srcLng);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(srcLat))
                * Math.cos(Math.toRadians(desLat)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
//        double meterConversion = 1609;

//        return (int) (dist * meterConversion);
    }

    /**
     * method used to get distance in kilometers
     *
     * @param distance contain distance
     * @return distance in kilometer
     */
    public static double getDistanceInKilometers(double distance) {
        return distance * 1.60934;
    }

    /**
     * method used to get distance in meters
     * @param distance contain distance
     * @return distance in meter
     */
    public static double getDistanceInMeters(double distance) {
        double meters=distance * 1609.34;
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(meters));
    }

    public static double getDistanceInMiles(double distance) {
        double meters=distance * 1609.34;
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(meters * 0.00062137));
    }

    public static double getMilesFrmMeter(double meter)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(meter * 0.00062137));
    }

    public static int getAvgTravalTime(double distanceInMiles)
    {
        int time=(int)distanceInMiles;
        if(time<1) {
            return 3;
        } else if(time > 1) {
            return time * 3;
        }
        return 5;
    }


}
