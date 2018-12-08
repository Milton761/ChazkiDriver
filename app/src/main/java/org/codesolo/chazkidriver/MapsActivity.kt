package org.codesolo.chazkidriver

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.location.Location
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMapLoadedCallback {

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onMapLoaded() {


    }


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var lat:Double? = null
    var lon:Double? = null

    private  var TAG = this.javaClass.name.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val intent = Intent(this, GPService::class.java)
        startService(intent)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        lat = AppSession.latitude
//        lon = AppSession.longitude

        Log.i(TAG, "onCreate")

//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = GoogleApiClient.Builder(applicationContext)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(API)
//                .build()
//            Log.i("MAP ACTIVITY", "GoogleApiClient")
//        }



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        Log.i(TAG, "onMapReady")

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                val currentLocation  = LatLng(location!!.latitude, location!!.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLocation).title("You r Here"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f))
            }

//        val currentLocation  = LatLng(AppSession.latitude, AppSession.longitude)
//        googleMap.addMarker(MarkerOptions().position(currentLocation).title("You r Here"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
    }
}
