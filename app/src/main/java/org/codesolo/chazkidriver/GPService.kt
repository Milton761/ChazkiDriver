package org.codesolo.chazkidriver


import android.*
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.Log

//import com.firebase.geofire.GeoFire
//import com.firebase.geofire.GeoLocation
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.server.converter.StringToIntConverter
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
//import com.google.firebase.database.FirebaseDatabase

import java.io.IOException
import java.text.DateFormat
import java.util.Date
import java.util.Locale

//import codesolo.trend.core.AppSession

/**
 * Created by Milton Condori Fernandez on 13/11/2016.
 * All rights reserved
 * CodeSolo Company
 */

class GPService : Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private var mGoogleClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLastUpdateTime: String? = null
//    private val userID = AppSession.getUser().getId() //id de la session actual
//    private var geoReference = AppSession.getGeoReference()
    private var path: String? = null

    override fun onCreate() {
        super.onCreate()

        buildGoogleApiClient()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppSession.hideLocation()
        mGoogleClient!!.disconnect()
        this.stopSelf()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        super.onTaskRemoved(rootIntent)

        Log.i(TAG, "onTaskRemoved called")
        Log.i(TAG, "path : " + path!!)

//        geoReference = GeoFire(FirebaseDatabase.getInstance().getReference(path))
//        geoReference.removeLocation(userID)
        this.stopSelf()


    }

    @Synchronized
    protected fun buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient")
        mGoogleClient = GoogleApiClient.Builder(this.applicationContext)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        createLocationRequest()
        mGoogleClient!!.connect()
    }

    protected fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS

        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    override fun onConnected(bundle: Bundle?) {
        startLocationUpdates()
    }

    override fun onConnectionSuspended(i: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }

    override fun onLocationChanged(location: Location) {


        //try to improve this.

        mLastUpdateTime = DateFormat.getTimeInstance().format(Date())


        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            path = "locations/" + addresses[0].countryName + "/" + addresses[0].locality

            Log.i(TAG, "location updated at : $mLastUpdateTime [$path]")

//            AppSession.latitude = location.latitude
//            AppSession.longitude = location.longitude

//            update location in FireStore
            AppSession.updateLocation(location.latitude, location.longitude)


        } catch (e: IOException) {
            Log.e(TAG, "Error georeference")
            e.printStackTrace()
        }

    }

    @SuppressLint("MissingPermission")
    protected fun startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this)
    }

    companion object {

        private val TAG = GPService::class.java.name

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000

        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }
}