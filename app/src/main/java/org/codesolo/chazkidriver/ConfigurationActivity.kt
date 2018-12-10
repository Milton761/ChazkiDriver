package org.codesolo.chazkidriver

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_configuration.*
import android.widget.CompoundButton
import android.content.Intent
import android.graphics.Color
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener




class ConfigurationActivity : AppCompatActivity() {

    private var tag = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        val prefs =this.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val isSharing  = prefs.getBoolean("share",true)

        switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.i(tag, isChecked.toString())
                startService(Intent(this, GPService::class.java))
                circleStatus.setColorFilter(Color.GREEN)
                prefs.edit().putBoolean("share", true).commit()

            }else{
                stopService(Intent(this, GPService::class.java))
                AppSession.hideLocation()
                Log.i(tag, isChecked.toString())
                circleStatus.setColorFilter(Color.DKGRAY)
                prefs.edit().putBoolean("share", false).commit()


            }

        }

        switch1.isChecked = isSharing
    }
}
