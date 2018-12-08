package org.codesolo.chazkidriver

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore



object AppSession {

     private var TAG = this.javaClass.name

     val fs = FirebaseFirestore.getInstance()



     val userName = "User101"
     val userCode = "DRIVER001"

     fun updateLocation(lat:Double, lon:Double) {

          var currentLoc: HashMap<String,Any> = hashMapOf()
          currentLoc["latitude"] = lat
          currentLoc["longitude"] = lon

          fs.collection("locDriver").document(userCode).set(currentLoc).addOnCompleteListener{
               if (it.isSuccessful){
                    Log.i(TAG, "Successfully: update location in Firestore")
               }else{
                    Log.i(TAG, "Problem updating location in Firestore")
               }

          }
     }

}