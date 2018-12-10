package org.codesolo.chazkidriver

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
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
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_maps_permission.*


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    GoogleMap.OnMapLoadedCallback {




    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var myMap: GoogleMap


    private  var tag = this.javaClass.name.toString()


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        FirebaseMessaging.getInstance().isAutoInitEnabled = true;
        validatePermission()




    }



    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {

        myMap = googleMap
        Log.i(tag, "onMapReady")
        setMarkerLocation(myMap)

    }

    @SuppressLint("MissingPermission")
    fun setMarkerLocation(googleMap: GoogleMap){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                val currentLocation  = LatLng(location!!.latitude, location!!.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLocation).title("You r Here"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16.0f))

            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.mainActionSetting -> {

            startActivity(Intent(this, ConfigurationActivity::class.java))

            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun validatePermission() {
        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {

                    Toast.makeText(this@MapsActivity, "Permission Granted", Toast.LENGTH_LONG).show()

                    setContentView(R.layout.activity_maps)

                    val mapFragment = supportFragmentManager
                        .findFragmentById(R.id.map) as SupportMapFragment
                    mapFragment.getMapAsync(this@MapsActivity)
                    setSupportActionBar(toolbar)
                    toolbar.title = AppSession.userName

//        startService(Intent(this, GPService::class.java))

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    AlertDialog.Builder(this@MapsActivity)
                        .setTitle("Permissions")
                        .setMessage("This app need use gps to get your current location")
                        .setCancelable(false)
                        .setNegativeButton(android.R.string.cancel) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(android.R.string.ok) { dialogInterface, i ->
                            dialogInterface.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@MapsActivity, "Denied Permisionn", Toast.LENGTH_LONG).show()
//                    var dialog = Dialog(this@MapsActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

                    this@MapsActivity.setContentView(R.layout.activity_maps_permission)

                    button.setOnClickListener {
                        validatePermission()
                    }

//                    dialog.show()
                }
            }
            ).check()
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onMapLoaded() {


    }

}
