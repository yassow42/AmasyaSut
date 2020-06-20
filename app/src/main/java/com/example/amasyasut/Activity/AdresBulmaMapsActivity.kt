package com.example.amasyasut.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.amasyasut.BottomNavigationViewHelper
import com.example.amasyasut.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_adres_bulma_maps.*

class AdresBulmaMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var musteriAdi: String? = null
    private val ACTIVITY_NO = 3
    var ref = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adres_bulma_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        setupNavigationView()
        mapFragment.getMapAsync(this)

        musteriAdi = intent.getStringExtra("musteriAdi")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val amasya = LatLng(40.6565, 35.8373)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amasya, 12.7f))

        mMap.isMyLocationEnabled = true
        konumKaydi()
    }

    private fun konumKaydi() {
        /*
        var hand = Handler()
        mRunnable = Runnable {

            hand.postDelayed(mRunnable, 1000)
        }

        hand.postDelayed(mRunnable, 5000)
*/
        tvKaydet.setOnClickListener {
            //  hand.removeCallbacks(mRunnable)
            val latLng: LatLng = mMap.getCameraPosition().target
            ref.child("Musteriler").child(musteriAdi.toString()).child("musteri_zkonum").setValue(true)
            ref.child("Musteriler").child(musteriAdi.toString()).child("musteri_zlat").setValue(latLng.latitude)
            ref.child("Musteriler").child(musteriAdi.toString()).child("musteri_zlong").setValue(latLng.longitude)
            var intent = Intent(this, MusterilerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            Handler().postDelayed({ startActivity(intent) }, 250)
        }

    }

    fun setupNavigationView() {
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNav.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }


}