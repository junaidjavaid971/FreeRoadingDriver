package com.apps.freeroadingdriver.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import android.util.Log
import com.apps.freeroadingdriver.FreeRoadingApp
import com.apps.freeroadingdriver.R
import com.apps.freeroadingdriver.constants.AppConstant
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.lang.Double
import java.math.BigDecimal
import java.util.*

class MapUtils {

    companion object {
        fun getMapsApiDirectionsUrl(dropLat: String, dropLng: String, pickupLat: String, pickupLng:String, googleMap : GoogleMap): String {
            val latLng = LatLng(Double.parseDouble(pickupLat), Double.parseDouble(pickupLng))
            val latLng1 = LatLng(Double.parseDouble(dropLat), Double.parseDouble(dropLng))
            googleMap!!.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ci_location_pin)))
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, AppConstant.MAP_ZOOM));
            val key = "&key=" + FreeRoadingApp.getInstance().getString(R.string.map_key)

            val waypoints = ("origin=" + pickupLat + "," + pickupLng + "&" + "destination="
                    + dropLat + "," + dropLng)

            val sensor = "sensor=false"
            val params = "$waypoints&$sensor"
            val output = "json"
            return "https://maps.googleapis.com/maps/api/directions/$output?$params$key"

        }


        public fun initMap(activity : Context, lattitude: kotlin.Double, longitude: kotlin.Double, googleMap: GoogleMap?) {

            val position = LatLng(lattitude, longitude)
            //MarkerOptions options = new MarkerOptions();
            //options.position(position);
            // Setting position for the MarkerOptions

            // Gets to GoogleMap from the MapView and does initialization stuff
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            googleMap?.setMapType(GoogleMap.MAP_TYPE_TERRAIN)
            googleMap?.getUiSettings()?.isCompassEnabled = false
            googleMap?.getUiSettings()?.isMyLocationButtonEnabled = false
            googleMap?.setMyLocationEnabled(true)
            // Needs to call MapsInitializer before doing any CameraUpdateFactory
            // calls
            MapsInitializer.initialize(activity)
            // Zoom in, animating the camera.
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, AppConstant.MAP_ZOOM))

            googleMap?.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener { arg0 ->
                // google_map.clear();
                val perth = googleMap?.addMarker(MarkerOptions().position(arg0.target).draggable(true))
                perth?.isVisible = false
            })
        }

        fun getAddressFromLatLng(context: Context,lattitude: kotlin.Double,longitude: kotlin.Double) : String{
            var strAdd = ""
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(lattitude, longitude, 1)
                if (addresses != null) {
                    val returnedAddress = addresses[0]
                    val strReturnedAddress = StringBuilder("")

                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i))
                    }
                    strAdd = strReturnedAddress.toString()
                    Log.w("My Current loction", strReturnedAddress.toString())
                } else {
                    Log.w("My Current loction", "No Address returned!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w("My Current loction", "Canont get Address!")
            }

            return strAdd
        }

        @Throws(IOException::class)
        fun getCityNameByCoordinates(context: Context,lat: kotlin.Double, lon: kotlin.Double, isCity : Boolean): String? {
            val mGeocoder = Geocoder(context, Locale.getDefault())
            val addresses = mGeocoder.getFromLocation(lat, lon, 1)
            Log.d("Addresscity",addresses.toString())
            return if (addresses != null && addresses!!.size > 0) {
                if (isCity){
                    addresses!!.get(0).getLocality()
                }
                else {
                    "" +addresses!!.get(0).postalCode
                }
            } else null
        }
    }











}