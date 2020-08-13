package com.example.amasyasut.Activity

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.amasyasut.BottomNavigationViewHelper
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.LoadingDialog
import com.example.amasyasut.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.*
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.*
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tb3lt
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tb5lt
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran150
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran170
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran180
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran1lt
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran200
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran220
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbAyran290
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbBp17Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbBp500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbBp5Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbBp800
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbCerkezPeynir
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbCokelek
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbKasar1KG
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbKasar400
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbLor9Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbPideKasar2Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTereyag1800
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTereyag1Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTereyag1KgKoy
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTereyag500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTostKasar2kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbTulum250
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt10KgAzyagli
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt10KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt10KgV
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt200V
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt2750tava
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt2KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt3KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurt4KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtSuzme5Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtSuzme900
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtTam200
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtTam2Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtTam500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurtTam9Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tbYogurttam1250
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tv3ltSut
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tv5ltSut
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran150
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran170
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran180
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran1lt
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran200
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran220
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvAyran290
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvBp17Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvBp500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvBp5Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvBp800
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvCerkezPeynir
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvCokelek
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvKasar1KG
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvKasar400
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvLor9Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvPideKasar2Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTereyag1800
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTereyag1Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTereyag1KgKoy
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTereyag500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTostKasar2kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvTulum250
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt10KgAzyagli
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt10KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt10KgV
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt200V
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt2750tava
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt2KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt3KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurt4KgDm
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtSuzme5Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtSuzme900
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtTam200
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtTam2Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtTam500
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurtTam9Kg
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.tvYogurttam1250


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetDialog: BottomSheetDialog

    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

    var konum: Boolean = false
    val handler = Handler()

    val ref = FirebaseDatabase.getInstance().reference
    var loading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid

        setupNavigationView()




        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        konum = sharedPreferences.getBoolean("Konum", false)
        swKonum.isChecked = konum
      //  Toast.makeText(this, "Bazı Siparişler Adres Bulunamadığından gösterilmeyebilir. Dikkatli Ol.!!!", Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isMyLocationEnabled = false
        handler.postDelayed(Runnable { mMap.isMyLocationEnabled = konum }, 2000)

        // Add a marker in Sydney and move the camera
        val amasya = LatLng(40.6565, 35.8373)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amasya, 12.7f))

        dialogCalistir()
        setupKullaniciAdi()
        konumIzni()
        handler.postDelayed(Runnable { dialogGizle() }, 2000)


    }

    fun dialogGizle() {
        loading?.let { if (it.isShowing) it.cancel() }

    }

    fun dialogCalistir() {
        dialogGizle()
        loading = LoadingDialog.startDialog(this)
    }


    private fun veri() {

        var mahalleler = ArrayList<String>()
        var siparisList = ArrayList<SiparisData>()
        ref.child("Siparisler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    for (ds in p0.children) {
                        var mahalle = ds.key!!
                        mahalleler.add(mahalle)

                    }

                    for (siparisMahallesi in mahalleler) {
                        ref.child("Siparisler").child(siparisMahallesi).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {}
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.hasChildren()) {
                                    for (ds in p0.children) {
                                        try {
                                            var gelenData = ds.getValue(SiparisData::class.java)!!
                                            siparisList.add(gelenData)

                                            var konumVarMi = gelenData.musteri_zkonum.toString().toBoolean()
                                            if (konumVarMi == true) {
                                                try {
                                                    var lat = gelenData.musteri_zlat!!.toDouble()
                                                    var long = gelenData.musteri_zlong!!.toDouble()
                                                    val adres = LatLng(lat, long)
                                                    var myMarker = mMap.addMarker(MarkerOptions().position(adres).title(gelenData.musteri_ad_soyad))

                                                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.order_map))
                                                    myMarker.tag = gelenData.siparis_key
                                                    val mahalle = gelenData.siparis_mah.toString()
                                                    myMarker.snippet = mahalle
                                                    dialogGizle()
                                                } catch (e: Exception) {
                                                    Toast.makeText(this@MapsActivity, gelenData.musteri_ad_soyad + " adlı müşterinin konumu hatalı. Lütfen Müşterinin ev konum switch'ini kapatın", Toast.LENGTH_LONG).show()
                                                }

                                            } else {

                                                var lat = convertAddressLat(gelenData.siparis_mah + " mahallesi " + gelenData.siparis_adres + " Amasya 05000")!!.toDouble()
                                                var long = convertAddressLng(gelenData.siparis_mah + " mahallesi " + gelenData.siparis_adres + " Amasya 05000")!!.toDouble()
                                                val adres = LatLng(lat, long)
                                                var myMarker = mMap.addMarker(MarkerOptions().position(adres).title(gelenData.musteri_ad_soyad))
                                                dialogGizle()
                                                myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.order_map))
                                                myMarker.tag = gelenData.siparis_key
                                                val mahalle = gelenData.siparis_mah.toString()
                                                myMarker.snippet = mahalle
                                                Log.e("it mah", mahalle)

                                            }


                                            mMap.setOnMarkerClickListener {
                                                it.tag
                                                it.snippet

                                                Log.e("it snip", it.snippet)
                                                Log.e("it tag", it.tag.toString())

                                                var bottomSheetDialog = BottomSheetDialog(this@MapsActivity)

                                                var view = bottomSheetDialog.layoutInflater.inflate(R.layout.dialog_map_item_siparisler, null)
                                                bottomSheetDialog.setContentView(view)

                                                ref.child("Siparisler").child(it.snippet.toString()).child(it.tag.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                                                    override fun onCancelled(p0: DatabaseError) {
                                                    }

                                                    override fun onDataChange(p0: DataSnapshot) {
                                                        try {
                                                            var gelenData = p0.getValue(SiparisData::class.java)!!
                                                            view.tvSiparisVeren.text = gelenData!!.musteri_ad_soyad
                                                            view.tvSiparisAdres.text = gelenData!!.siparis_mah + " Mah. " + gelenData!!.siparis_adres + " " + gelenData!!.siparis_apartman
                                                            view.tvSiparisTel.text = gelenData.siparis_tel
                                                            view.tvNot.text = gelenData.siparis_notu

                                                            if (gelenData.zz_3litre.toString() == "0") {
                                                                view.tb3lt.visibility = View.GONE
                                                            } else {
                                                                view.tv3ltSut.text = gelenData.zz_3litre.toString()
                                                                view.tb3lt.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_5litre.toString() == "0") {
                                                                view.tb5lt.visibility = View.GONE
                                                            } else {
                                                                view.tv5ltSut.text = gelenData.zz_5litre.toString()
                                                                view.tb5lt.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Cokelek.toString() == "0") {
                                                                view.tbCokelek.visibility = View.GONE
                                                            } else {
                                                                view.tvCokelek.text = gelenData.zz_Cokelek.toString()
                                                                view.tbCokelek.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Tulum250.toString() == "0") {
                                                                view.tbTulum250.visibility = View.GONE
                                                            } else {
                                                                view.tvTulum250.text = gelenData.zz_Tulum250.toString()
                                                                view.tbTulum250.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Koy15Kg.toString() == "0") {
                                                                view.tbKoy15Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvKoy15Kg.text = gelenData.zz_Koy15Kg.toString()
                                                                view.tbKoy15Kg.visibility = View.VISIBLE
                                                            }




                                                            if (gelenData.zz_Koy5Kg.toString() == "0") {
                                                                view.tbKoy5Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvKoy5Kg.text = gelenData.zz_Koy5Kg.toString()
                                                                view.tbKoy5Kg.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Kaymak200.toString() == "0") {
                                                                view.tbKaymak200Gr.visibility = View.GONE
                                                            } else {
                                                                view.tvKaymak200Gr.text = gelenData.zz_Kaymak200.toString()
                                                                view.tbKaymak200Gr.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Bp17Kg.toString() == "0") {
                                                                view.tbBp17Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvBp17Kg.text = gelenData.zz_Bp17Kg.toString()
                                                                view.tbBp17Kg.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_CerkezPeynir.toString() == "0") {
                                                                view.tbCerkezPeynir.visibility = View.GONE
                                                            } else {
                                                                view.tvCerkezPeynir.text = gelenData.zz_CerkezPeynir.toString()
                                                                view.tbCerkezPeynir.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Bp5Kg.toString() == "0") {
                                                                view.tbBp5Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvBp5Kg.text = gelenData.zz_Bp5Kg.toString()
                                                                view.tbBp5Kg.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Bp800.toString() == "0") {
                                                                view.tbBp800.visibility = View.GONE
                                                            } else {
                                                                view.tvBp800.text = gelenData.zz_Bp800.toString()
                                                                view.tbBp800.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Bp500.toString() == "0") {
                                                                view.tbBp500.visibility = View.GONE
                                                            } else {
                                                                view.tvBp500.text = gelenData.zz_Bp500.toString()
                                                                view.tbBp500.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Tereyag1800.toString() == "0") {
                                                                view.tbTereyag1800.visibility = View.GONE
                                                            } else {
                                                                view.tvTereyag1800.text = gelenData.zz_Tereyag1800.toString()
                                                                view.tbTereyag1800.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Tereyag1Kg.toString() == "0") {
                                                                view.tbTereyag1Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvTereyag1Kg.text = gelenData.zz_Tereyag1Kg.toString()
                                                                view.tbTereyag1Kg.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Tereyag1KgKoy.toString() == "0") {
                                                                view.tbTereyag1KgKoy.visibility = View.GONE
                                                            } else {
                                                                view.tvTereyag1KgKoy.text = gelenData.zz_Tereyag1KgKoy.toString()
                                                                view.tbTereyag1KgKoy.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Tereyag500.toString() == "0") {
                                                                view.tbTereyag500.visibility = View.GONE
                                                            } else {
                                                                view.tvTereyag500.text = gelenData.zz_Tereyag500.toString()
                                                                view.tbTereyag500.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_YogurtSuzme5Kg.toString() == "0") {
                                                                view.tbYogurtSuzme5Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtSuzme5Kg.text = gelenData.zz_YogurtSuzme5Kg.toString()
                                                                view.tbYogurtSuzme5Kg.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_YogurtSuzme900.toString() == "0") {
                                                                view.tbYogurtSuzme900.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtSuzme900.text = gelenData.zz_YogurtSuzme900.toString()
                                                                view.tbYogurtSuzme900.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Lor9Kg.toString() == "0") {
                                                                view.tbLor9Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvLor9Kg.text = gelenData.zz_Lor9Kg.toString()
                                                                view.tbLor9Kg.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Kasar2KgTost.toString() == "0") {
                                                                view.tbTostKasar2kg.visibility = View.GONE
                                                            } else {
                                                                view.tvTostKasar2kg.text = gelenData.zz_Kasar2KgTost.toString()
                                                                view.tbTostKasar2kg.visibility = View.VISIBLE
                                                            }




                                                            if (gelenData.zz_Kasar2KgPide.toString() == "0") {
                                                                view.tbPideKasar2Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvPideKasar2Kg.text = gelenData.zz_Kasar2KgPide.toString()
                                                                view.tbPideKasar2Kg.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Kasar1KgGr.toString() == "0") {
                                                                view.tbKasar1KG.visibility = View.GONE
                                                            } else {
                                                                view.tvKasar1KG.text = gelenData.zz_Kasar1KgGr.toString()
                                                                view.tbKasar1KG.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Kasar400Gr.toString() == "0") {
                                                                view.tbKasar400.visibility = View.GONE
                                                            } else {
                                                                view.tvKasar400.text = gelenData.zz_Kasar400Gr.toString()
                                                                view.tbKasar400.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Kasar700Gr.toString() == "0") {
                                                                view.tbKasar700Gr.visibility = View.GONE
                                                            } else {
                                                                view.tvKasar700Gr.text = gelenData.zz_Kasar700Gr.toString()
                                                                view.tbKasar700Gr.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Ayran150.toString() == "0") {
                                                                view.tbAyran150.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran150.text = gelenData.zz_Ayran150.toString()
                                                                view.tbAyran150.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_Ayran170.toString() == "0") {
                                                                view.tbAyran170.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran170.text = gelenData.zz_Ayran170.toString()
                                                                view.tbAyran170.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Ayran180.toString() == "0") {
                                                                view.tbAyran180.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran180.text = gelenData.zz_Ayran180.toString()
                                                                view.tbAyran180.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Ayran200.toString() == "0") {
                                                                view.tbAyran200.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran200.text = gelenData.zz_Ayran200.toString()
                                                                view.tbAyran200.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_Ayran220.toString() == "0") {
                                                                view.tbAyran220.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran220.text = gelenData.zz_Ayran220.toString()
                                                                view.tbAyran220.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Ayran290.toString() == "0") {
                                                                view.tbAyran290.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran290.text = gelenData.zz_Ayran290.toString()
                                                                view.tbAyran290.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Ayran1Lt.toString() == "0") {
                                                                view.tbAyran1lt.visibility = View.GONE
                                                            } else {
                                                                view.tvAyran1lt.text = gelenData.zz_Ayran1Lt.toString()
                                                                view.tbAyran1lt.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_YogurtAz10KgOzel.toString() == "0") {
                                                                view.tbYogurt10KgAzyagli.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt10KgAzyagli.text = gelenData.zz_YogurtAz10KgOzel.toString()
                                                                view.tbYogurt10KgAzyagli.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_YogurtYarım10KgV.toString() == "0") {
                                                                view.tbYogurt10KgV.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt10KgV.text = gelenData.zz_YogurtYarım10KgV.toString()
                                                                view.tbYogurt10KgV.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_YogurtYarım200V.toString() == "0") {
                                                                view.tbYogurt200V.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt200V.text = gelenData.zz_YogurtYarım200V.toString()
                                                                view.tbYogurt200V.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_YogurtYarım10KgDM.toString() == "0") {
                                                                view.tbYogurt10KgDm.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt10KgDm.text = gelenData.zz_YogurtYarım10KgDM.toString()
                                                                view.tbYogurt10KgDm.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_YogurtYarım4KgDM.toString() == "0") {
                                                                view.tbYogurt4KgDm.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt4KgDm.text = gelenData.zz_YogurtYarım4KgDM.toString()
                                                                view.tbYogurt4KgDm.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_YogurtYarım3KgDM.toString() == "0") {
                                                                view.tbYogurt3KgDm.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt3KgDm.text = gelenData.zz_YogurtYarım3KgDM.toString()
                                                                view.tbYogurt3KgDm.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_YogurtYarım2KgDM.toString() == "0") {
                                                                view.tbYogurt2KgDm.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt2KgDm.text = gelenData.zz_YogurtYarım2KgDM.toString()
                                                                view.tbYogurt2KgDm.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_Yogurt2750KgTava.toString() == "0") {
                                                                view.tbYogurt2750tava.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurt2750tava.text = gelenData.zz_Yogurt2750KgTava.toString()
                                                                view.tbYogurt2750tava.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_YogurtTam9Kg.toString() == "0") {
                                                                view.tbYogurtTam9Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtTam9Kg.text = gelenData.zz_YogurtTam9Kg.toString()
                                                                view.tbYogurtTam9Kg.visibility = View.VISIBLE
                                                            }

                                                            if (gelenData.zz_YogurtTam2Kg.toString() == "0") {
                                                                view.tbYogurtTam2Kg.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtTam2Kg.text = gelenData.zz_YogurtTam2Kg.toString()
                                                                view.tbYogurtTam2Kg.visibility = View.VISIBLE
                                                            }



                                                            if (gelenData.zz_YogurtTam1250.toString() == "0") {
                                                                view.tbYogurttam1250.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurttam1250.text = gelenData.zz_YogurtTam1250.toString()
                                                                view.tbYogurttam1250.visibility = View.VISIBLE
                                                            }


                                                            if (gelenData.zz_YogurtTam500.toString() == "0") {
                                                                view.tbYogurtTam500.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtTam500.text = gelenData.zz_YogurtTam500.toString()
                                                                view.tbYogurtTam500.visibility = View.VISIBLE
                                                            }
                                                            if (gelenData.zz_YogurtTam200.toString() == "0") {
                                                                view.tbYogurtTam200.visibility = View.GONE
                                                            } else {
                                                                view.tvYogurtTam200.text = gelenData.zz_YogurtTam200.toString()
                                                                view.tbYogurtTam200.visibility = View.VISIBLE
                                                            }

                                                            try {
                                                                var sut3ltAdet = gelenData.zz_3litre.toString().toInt()
                                                                var sut3ltFiyat = gelenData.zz_3litreFiyat.toString().toDouble()
                                                                var sut5ltAdet = gelenData.zz_5litre.toString().toInt()
                                                                var sut5ltFiyat = gelenData.zz_5litreFiyat.toString().toDouble()
                                                                var zz_Bp500 = gelenData.zz_Bp500.toString().toInt()
                                                                var zz_Bp500Fiyat = gelenData.zz_Bp500Fiyat.toString().toDouble()
                                                                var zz_Bp800 = gelenData.zz_Bp800.toString().toInt()
                                                                var zz_Bp800Fiyat = gelenData.zz_Bp800Fiyat.toString().toDouble()
                                                                var zz_Bp5Kg = gelenData.zz_Bp5Kg.toString().toInt()
                                                                var zz_Bp5KgFiyat = gelenData.zz_Bp5KgFiyat.toString().toDouble()
                                                                var zz_Bp17Kg = gelenData.zz_Bp17Kg.toString().toInt()
                                                                var zz_Bp17KgFiyat = gelenData.zz_Bp17KgFiyat.toString().toDouble()
                                                                var zz_Kasar400Gr = gelenData.zz_Kasar400Gr.toString().toInt()
                                                                var zz_Kasar400GrFiyat = gelenData.zz_Kasar400GrFiyat.toString().toDouble()
                                                                var zz_Kasar700Gr = gelenData.zz_Kasar700Gr.toString().toInt()
                                                                var zz_Kasar700GrFiyat = gelenData.zz_Kasar700GrFiyat.toString().toDouble()
                                                                var zz_Kasar1KgGr = gelenData.zz_Kasar1KgGr.toString().toInt()
                                                                var zz_Kasar1KgGrFiyat = gelenData.zz_Kasar1KgGrFiyat.toString().toDouble()
                                                                var zz_Kasar2KgPide = gelenData.zz_Kasar2KgPide.toString().toInt()
                                                                var zz_Kasar2KgPideFiyat = gelenData.zz_Kasar2KgPideFiyat.toString().toDouble()
                                                                var zz_Kasar2KgTost = gelenData.zz_Kasar2KgTost.toString().toInt()
                                                                var zz_Kasar2KgTostFiyat = gelenData.zz_Kasar2KgTostFiyat.toString().toDouble()
                                                                var zz_Koy5Kg = gelenData.zz_Koy5Kg.toString().toInt()
                                                                var zz_Koy5KgFiyat = gelenData.zz_Koy5KgFiyat.toString().toDouble()
                                                                var zz_Koy15Kg = gelenData.zz_Koy15Kg.toString().toInt()
                                                                var zz_Koy15KgFiyat = gelenData.zz_Koy15KgFiyat.toString().toDouble()
                                                                var zz_Tulum250 = gelenData.zz_Tulum250.toString().toInt()
                                                                var zz_Tulum250Fiyat = gelenData.zz_Tulum250Fiyat.toString().toDouble()
                                                                var zz_CerkezPeynir = gelenData.zz_CerkezPeynir.toString().toInt()
                                                                var zz_CerkezPeynirFiyat = gelenData.zz_CerkezPeynirFiyat.toString().toDouble()
                                                                var zz_Lor9Kg = gelenData.zz_Lor9Kg.toString().toInt()
                                                                var zz_Lor9KgFiyat = gelenData.zz_Lor9KgFiyat.toString().toDouble()
                                                                var zz_Cokelek = gelenData.zz_Cokelek.toString().toInt()
                                                                var zz_CokelekFiyat = gelenData.zz_CokelekFiyat.toString().toDouble()
                                                                var zz_YogurtTam200 = gelenData.zz_YogurtTam200.toString().toInt()
                                                                var zz_YogurtTam200Fiyat = gelenData.zz_YogurtTam200Fiyat.toString().toDouble()
                                                                var zz_YogurtTam500 = gelenData.zz_YogurtTam500.toString().toInt()
                                                                var zz_YogurtTam500Fiyat = gelenData.zz_YogurtTam500Fiyat.toString().toDouble()
                                                                var zz_YogurtTam1250 = gelenData.zz_YogurtTam1250.toString().toInt()
                                                                var zz_YogurtTam1250Fiyat = gelenData.zz_YogurtTam1250Fiyat.toString().toDouble()
                                                                var zz_YogurtTam2Kg = gelenData.zz_YogurtTam2Kg.toString().toInt()
                                                                var zz_YogurtTam2KgFiyat = gelenData.zz_YogurtTam2KgFiyat.toString().toDouble()
                                                                var zz_YogurtTam9Kg = gelenData.zz_YogurtTam9Kg.toString().toInt()
                                                                var zz_YogurtTam9KgFiyat = gelenData.zz_YogurtTam9KgFiyat.toString().toDouble()
                                                                var zz_YogurtYarım200V = gelenData.zz_YogurtYarım200V.toString().toInt()
                                                                var zz_YogurtYarım200VFiyat = gelenData.zz_YogurtYarım200VFiyat.toString().toDouble()
                                                                var zz_YogurtYarım10KgV = gelenData.zz_YogurtYarım10KgV.toString().toInt()
                                                                var zz_YogurtYarım10KgVFiyat = gelenData.zz_YogurtYarım10KgVFiyat.toString().toDouble()
                                                                var zz_YogurtYarım2KgDM = gelenData.zz_YogurtYarım2KgDM.toString().toInt()
                                                                var zz_YogurtYarım2KgDMFiyat = gelenData.zz_YogurtYarım2KgDMFiyat.toString().toDouble()
                                                                var zz_YogurtYarım3KgDM = gelenData.zz_YogurtYarım3KgDM.toString().toInt()
                                                                var zz_YogurtYarım3KgDMFiyat = gelenData.zz_YogurtYarım3KgDMFiyat.toString().toDouble()
                                                                var zz_YogurtYarım4KgDM = gelenData.zz_YogurtYarım4KgDM.toString().toInt()
                                                                var zz_YogurtYarım4KgDMFiyat = gelenData.zz_YogurtYarım4KgDMFiyat.toString().toDouble()
                                                                var zz_YogurtYarım10KgDM = gelenData.zz_YogurtYarım10KgDM.toString().toInt()
                                                                var zz_YogurtYarım10KgDMFiyat = gelenData.zz_YogurtYarım10KgDMFiyat.toString().toDouble()
                                                                var zz_YogurtAz10KgOzel = gelenData.zz_YogurtAz10KgOzel.toString().toInt()
                                                                var zz_YogurtAz10KgOzelFiyat = gelenData.zz_YogurtAz10KgOzelFiyat.toString().toDouble()
                                                                var zz_YogurtSuzme900 = gelenData.zz_YogurtSuzme900.toString().toInt()
                                                                var zz_YogurtSuzme900Fiyat = gelenData.zz_YogurtSuzme900Fiyat.toString().toDouble()
                                                                var zz_YogurtSuzme5Kg = gelenData.zz_YogurtSuzme5Kg.toString().toInt()
                                                                var zz_YogurtSuzme5KgFiyat = gelenData.zz_YogurtSuzme5KgFiyat.toString().toDouble()
                                                                var zz_Yogurt2750KgTava = gelenData.zz_Yogurt2750KgTava.toString().toInt()
                                                                var zz_Yogurt2750KgTavaFiyat = gelenData.zz_Yogurt2750KgTavaFiyat.toString().toDouble()
                                                                var zz_Ayran1Lt = gelenData.zz_Ayran1Lt.toString().toInt()
                                                                var zz_Ayran1LtFiyat = gelenData.zz_Ayran1LtFiyat.toString().toDouble()
                                                                var zz_Ayran290 = gelenData.zz_Ayran290.toString().toInt()
                                                                var zz_Ayran290Fiyat = gelenData.zz_Ayran290Fiyat.toString().toDouble()
                                                                var zz_Ayran220 = gelenData.zz_Ayran220.toString().toInt()
                                                                var zz_Ayran220Fiyat = gelenData.zz_Ayran220Fiyat.toString().toDouble()
                                                                var zz_Ayran200 = gelenData.zz_Ayran200.toString().toInt()
                                                                var zz_Ayran200Fiyat = gelenData.zz_Ayran200Fiyat.toString().toDouble()
                                                                var zz_Ayran180 = gelenData.zz_Ayran180.toString().toInt()
                                                                var zz_Ayran180Fiyat = gelenData.zz_Ayran180Fiyat.toString().toDouble()
                                                                var zz_Ayran170 = gelenData.zz_Ayran170.toString().toInt()
                                                                var zz_Ayran170Fiyat = gelenData.zz_Ayran170Fiyat.toString().toDouble()
                                                                var zz_Ayran150 = gelenData.zz_Ayran150.toString().toInt()
                                                                var zz_Ayran150Fiyat = gelenData.zz_Ayran150Fiyat.toString().toDouble()
                                                                var zz_Tereyag500 = gelenData.zz_Tereyag500.toString().toInt()
                                                                var zz_Tereyag500Fiyat = gelenData.zz_Tereyag500Fiyat.toString().toDouble()
                                                                var zz_Tereyag1Kg = gelenData.zz_Tereyag1Kg.toString().toInt()
                                                                var zz_Tereyag1KgFiyat = gelenData.zz_Tereyag1KgFiyat.toString().toDouble()
                                                                var zz_Tereyag1800 = gelenData.zz_Tereyag1800.toString().toInt()
                                                                var zz_Tereyag1800Fiyat = gelenData.zz_Tereyag1800Fiyat.toString().toDouble()
                                                                var zz_Tereyag1KgKoy = gelenData.zz_Tereyag1KgKoy.toString().toInt()
                                                                var zz_Tereyag1KgKoyFiyat = gelenData.zz_Tereyag1KgKoyFiyat.toString().toDouble()
                                                                var zz_Kaymak200 = gelenData.zz_Kaymak200.toString().toInt()
                                                                var zz_Kaymak200Fiyat = gelenData.zz_Kaymak200Fiyat.toString().toDouble()




                                                                view.tvFiyat.text = ((sut3ltAdet * sut3ltFiyat) +
                                                                        (sut5ltAdet * sut5ltFiyat) +
                                                                        (zz_Bp500 * zz_Bp500Fiyat) +
                                                                        (zz_Bp800 * zz_Bp800Fiyat) +
                                                                        (zz_Bp5Kg * zz_Bp5KgFiyat) +
                                                                        (zz_Bp17Kg * zz_Bp17KgFiyat) +
                                                                        (zz_Kasar400Gr * zz_Kasar400GrFiyat) +
                                                                        (zz_Kasar700Gr * zz_Kasar700GrFiyat) +
                                                                        (zz_Kasar1KgGr * zz_Kasar1KgGrFiyat) +
                                                                        (zz_Kasar2KgPide * zz_Kasar2KgPideFiyat) +
                                                                        (zz_Kasar2KgTost * zz_Kasar2KgTostFiyat) +
                                                                        (zz_Koy5Kg * zz_Koy5KgFiyat) +
                                                                        (zz_Koy15Kg * zz_Koy15KgFiyat) +
                                                                        (zz_Tulum250 * zz_Tulum250Fiyat) +
                                                                        (zz_CerkezPeynir * zz_CerkezPeynirFiyat) +
                                                                        (zz_Lor9Kg * zz_Lor9KgFiyat) +
                                                                        (zz_Cokelek * zz_CokelekFiyat) +
                                                                        (zz_YogurtTam200 * zz_YogurtTam200Fiyat) +
                                                                        (zz_YogurtTam500 * zz_YogurtTam500Fiyat) +
                                                                        (zz_YogurtTam1250 * zz_YogurtTam1250Fiyat) +
                                                                        (zz_YogurtTam2Kg * zz_YogurtTam2KgFiyat) +
                                                                        (zz_YogurtTam9Kg * zz_YogurtTam9KgFiyat) +
                                                                        (zz_YogurtYarım200V * zz_YogurtYarım200VFiyat) +
                                                                        (zz_YogurtYarım10KgV * zz_YogurtYarım10KgVFiyat) +
                                                                        (zz_YogurtYarım2KgDM * zz_YogurtYarım2KgDMFiyat) +
                                                                        (zz_YogurtYarım3KgDM * zz_YogurtYarım3KgDMFiyat) +
                                                                        (zz_YogurtYarım4KgDM * zz_YogurtYarım4KgDMFiyat) +
                                                                        (zz_YogurtYarım10KgDM * zz_YogurtYarım10KgDMFiyat) +
                                                                        (zz_YogurtAz10KgOzel * zz_YogurtAz10KgOzelFiyat) +
                                                                        (zz_YogurtSuzme900 * zz_YogurtSuzme900Fiyat) +
                                                                        (zz_YogurtSuzme5Kg * zz_YogurtSuzme5KgFiyat) +
                                                                        (zz_Yogurt2750KgTava * zz_Yogurt2750KgTavaFiyat) +
                                                                        (zz_Ayran1Lt * zz_Ayran1LtFiyat) +
                                                                        (zz_Ayran290 * zz_Ayran290Fiyat) +
                                                                        (zz_Ayran220 * zz_Ayran220Fiyat) +
                                                                        (zz_Ayran200 * zz_Ayran200Fiyat) +
                                                                        (zz_Ayran180 * zz_Ayran180Fiyat) +
                                                                        (zz_Ayran170 * zz_Ayran170Fiyat) +
                                                                        (zz_Ayran150 * zz_Ayran150Fiyat) +
                                                                        (zz_Tereyag500 * zz_Tereyag500Fiyat) +
                                                                        (zz_Tereyag1Kg * zz_Tereyag1KgFiyat) +
                                                                        (zz_Tereyag1800 * zz_Tereyag1800Fiyat) +
                                                                        (zz_Tereyag1KgKoy * zz_Tereyag1KgKoyFiyat) +
                                                                        (zz_Kaymak200 * zz_Kaymak200Fiyat)
                                                                        ).toString() + " Tl"
                                                            } catch (e: Exception) {
                                                                ref.child("Hatalar/Mapsactivity/fiyatHatası").setValue("hesap hatası " + e.message.toString())
                                                            }


                                                        } catch (e: Exception) {
                                                            Log.e("haritalar", e.message.toString())
                                                        }




                                                        view.tvSiparisTel.setOnClickListener {
                                                            val arama = Intent(Intent.ACTION_DIAL)//Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
                                                            arama.data = Uri.parse("tel:" + gelenData.siparis_tel)
                                                            startActivity(arama)
                                                        }
                                                        view.tvSiparisAdres.setOnClickListener {
                                                            val intent = Intent(
                                                                Intent.ACTION_VIEW,
                                                                Uri.parse("google.navigation:q= " + gelenData.siparis_mah + " " + gelenData.siparis_adres + " Amasya 0500")
                                                            )
                                                            startActivity(intent)
                                                        }
                                                        view.btnTeslim.setOnClickListener {
                                                            var alert = AlertDialog.Builder(this@MapsActivity).setTitle("Sipariş Teslim Edildi").setMessage("Emin Misin ?")
                                                                .setPositiveButton("Onayla", object : DialogInterface.OnClickListener {
                                                                    override fun onClick(p0: DialogInterface?, p1: Int) {

                                                                        var siparisData = SiparisData(
                                                                            gelenData.musteri_ad_soyad,
                                                                            kullaniciAdi,
                                                                            gelenData.siparis_zamani,
                                                                            gelenData.siparis_teslim_zamani,
                                                                            gelenData.siparis_teslim_tarihi,
                                                                            gelenData.siparis_adres,
                                                                            gelenData.siparis_notu,
                                                                            gelenData.siparis_mah,
                                                                            gelenData.siparis_key,
                                                                            gelenData.siparis_apartman,
                                                                            gelenData.siparis_tel,
                                                                            gelenData.musteri_zkonum,
                                                                            gelenData.musteri_zlat,
                                                                            gelenData.musteri_zlong,
                                                                            gelenData.zz_3litre,
                                                                            gelenData.zz_3litreFiyat,
                                                                            gelenData.zz_5litre,
                                                                            gelenData.zz_5litreFiyat,
                                                                            gelenData.zz_Bp500,
                                                                            gelenData.zz_Bp500Fiyat,
                                                                            gelenData.zz_Bp800,
                                                                            gelenData.zz_Bp800Fiyat,
                                                                            gelenData.zz_Bp5Kg,
                                                                            gelenData.zz_Bp5KgFiyat,
                                                                            gelenData.zz_Bp17Kg,
                                                                            gelenData.zz_Bp17KgFiyat,
                                                                            gelenData.zz_Kasar400Gr,
                                                                            gelenData.zz_Kasar400GrFiyat,
                                                                            gelenData.zz_Kasar700Gr,
                                                                            gelenData.zz_Kasar700GrFiyat,
                                                                            gelenData.zz_Kasar1KgGr,
                                                                            gelenData.zz_Kasar1KgGrFiyat,
                                                                            gelenData.zz_Kasar2KgPide,
                                                                            gelenData.zz_Kasar2KgPideFiyat,
                                                                            gelenData.zz_Kasar2KgTost,
                                                                            gelenData.zz_Kasar2KgTostFiyat,
                                                                            gelenData.zz_Koy5Kg,
                                                                            gelenData.zz_Koy5KgFiyat,
                                                                            gelenData.zz_Koy15Kg,
                                                                            gelenData.zz_Koy15KgFiyat,
                                                                            gelenData.zz_Tulum250,
                                                                            gelenData.zz_Tulum250Fiyat,
                                                                            gelenData.zz_CerkezPeynir,
                                                                            gelenData.zz_CerkezPeynirFiyat,
                                                                            gelenData.zz_Lor9Kg,
                                                                            gelenData.zz_Lor9KgFiyat,
                                                                            gelenData.zz_Cokelek,
                                                                            gelenData.zz_CokelekFiyat,
                                                                            gelenData.zz_YogurtTam200,
                                                                            gelenData.zz_YogurtTam200Fiyat,
                                                                            gelenData.zz_YogurtTam500,
                                                                            gelenData.zz_YogurtTam500Fiyat,
                                                                            gelenData.zz_YogurtTam1250,
                                                                            gelenData.zz_YogurtTam1250Fiyat,
                                                                            gelenData.zz_YogurtTam2Kg,
                                                                            gelenData.zz_YogurtTam2KgFiyat,
                                                                            gelenData.zz_YogurtTam9Kg,
                                                                            gelenData.zz_YogurtTam9KgFiyat,
                                                                            gelenData.zz_YogurtYarım200V,
                                                                            gelenData.zz_YogurtYarım200VFiyat,
                                                                            gelenData.zz_YogurtYarım10KgV,
                                                                            gelenData.zz_YogurtYarım10KgVFiyat,
                                                                            gelenData.zz_YogurtYarım2KgDM,
                                                                            gelenData.zz_YogurtYarım2KgDMFiyat,
                                                                            gelenData.zz_YogurtYarım3KgDM,
                                                                            gelenData.zz_YogurtYarım3KgDMFiyat,
                                                                            gelenData.zz_YogurtYarım4KgDM,
                                                                            gelenData.zz_YogurtYarım4KgDMFiyat,
                                                                            gelenData.zz_YogurtYarım10KgDM,
                                                                            gelenData.zz_YogurtYarım10KgDMFiyat,
                                                                            gelenData.zz_YogurtAz10KgOzel,
                                                                            gelenData.zz_YogurtAz10KgOzelFiyat,
                                                                            gelenData.zz_YogurtSuzme900,
                                                                            gelenData.zz_YogurtSuzme900Fiyat,
                                                                            gelenData.zz_YogurtSuzme5Kg,
                                                                            gelenData.zz_YogurtSuzme5KgFiyat,
                                                                            gelenData.zz_Yogurt2750KgTava,
                                                                            gelenData.zz_Yogurt2750KgTavaFiyat,
                                                                            gelenData.zz_Ayran1Lt,
                                                                            gelenData.zz_Ayran1LtFiyat,
                                                                            gelenData.zz_Ayran290,
                                                                            gelenData.zz_Ayran290Fiyat,
                                                                            gelenData.zz_Ayran220,
                                                                            gelenData.zz_Ayran220Fiyat,
                                                                            gelenData.zz_Ayran200,
                                                                            gelenData.zz_Ayran200Fiyat,
                                                                            gelenData.zz_Ayran180,
                                                                            gelenData.zz_Ayran180Fiyat,
                                                                            gelenData.zz_Ayran170,
                                                                            gelenData.zz_Ayran170Fiyat,
                                                                            gelenData.zz_Ayran150,
                                                                            gelenData.zz_Ayran150Fiyat,
                                                                            gelenData.zz_Tereyag500,
                                                                            gelenData.zz_Tereyag500Fiyat,
                                                                            gelenData.zz_Tereyag1Kg,
                                                                            gelenData.zz_Tereyag1KgFiyat,
                                                                            gelenData.zz_Tereyag1800,
                                                                            gelenData.zz_Tereyag1800Fiyat,
                                                                            gelenData.zz_Tereyag1KgKoy,
                                                                            gelenData.zz_Tereyag1KgKoyFiyat,
                                                                            gelenData.zz_Kaymak200,
                                                                            gelenData.zz_Kaymak200Fiyat
                                                                        )

                                                                        ref.child("Musteriler").child(gelenData.musteri_ad_soyad.toString()).child("siparisleri")
                                                                            .child(gelenData.siparis_key.toString()).setValue(siparisData)
                                                                        ref.child("Musteriler").child(gelenData.musteri_ad_soyad.toString()).child("siparis_son_zaman")
                                                                            .setValue(ServerValue.TIMESTAMP)

                                                                        ref.child("Teslim_siparisler").child(gelenData.siparis_key.toString()).setValue(siparisData)
                                                                        ref.child("Teslim_siparisler").child(gelenData.siparis_key.toString()).child("siparis_teslim_zamani")
                                                                            .setValue(ServerValue.TIMESTAMP)

                                                                        ref.child("Siparisler").child(gelenData.siparis_mah.toString()).child(gelenData.siparis_key.toString()).removeValue()

                                                                        startActivity(Intent(this@MapsActivity, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                                                        Toast.makeText(this@MapsActivity, "Sipariş Teslim Edildi", Toast.LENGTH_LONG).show()
                                                                    }
                                                                })
                                                                .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                                                        p0!!.dismiss()
                                                                    }
                                                                }).create()
                                                            alert.show()
                                                        }
                                                    }
                                                })

                                                bottomSheetDialog.show()
                                                it.isVisible
                                            }


                                        } catch (e: Exception) {

                                        }

                                    }


                                }
                            }

                        })


                    }


                } else {
                    dialogGizle()
                    Toast.makeText(this@MapsActivity, "Sipariş Yok", Toast.LENGTH_LONG).show()
                }
            }


        })

        swKonum.setOnClickListener {
            val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            if (swKonum.isChecked) {
                editor.putBoolean("Konum", true)
                editor.apply()
                mMap.isMyLocationEnabled = true
                Toast.makeText(this, "Konum açıldı", Toast.LENGTH_SHORT).show()
            } else {
                editor.putBoolean("Konum", false)
                editor.apply()
                mMap.isMyLocationEnabled = false
                Toast.makeText(this, "Konum kapalı", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun konumIzni() {
        Dexter.withActivity(this).withPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.INTERNET
        )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {


                    if (report!!.areAllPermissionsGranted()) {

                    }

                    if (report!!.isAnyPermissionPermanentlyDenied) {
                        ref.child("Hatalar/İzin Hatası").push().setValue("İzin Yok")
                        Toast.makeText(this@MapsActivity, "İzinleri kontrol et", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {

                }


            }).check()
    }

    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNav)
        BottomNavigationViewHelper.setupNavigation(this, bottomNav) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNav.menu
        var menuItem = menu.getItem(1)
        menuItem.setChecked(true)
    }

    fun setupKullaniciAdi() {
        ref.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                kullaniciAdi = p0.child("user_name").value.toString()
                handler.postDelayed(Runnable { veri() }, 1250)
            }
        })
    }

    fun convertAddressLat(adres: String): Double? {
        var geoCoder = Geocoder(this)
        try {
            val addressList: List<Address> =
                geoCoder.getFromLocationName(adres.toString(), 1)
            if (addressList != null && addressList.size > 0) {
                val lat: Double = addressList[0].getLatitude()
                val lng: Double = addressList[0].getLongitude()
                return lat
            }
        } catch (ex: Exception) {
            Log.e("Harita Verilerini Conve", ex.toString())
        }
        return null
    }

    fun convertAddressLng(adres: String): Double? {
        var geoCoder = Geocoder(this)
        try {
            val addressList: List<Address> =
                geoCoder.getFromLocationName(adres.toString(), 1)
            if (addressList != null && addressList.size > 0) {
                val lat: Double = addressList[0].getLatitude()
                val lng: Double = addressList[0].getLongitude()
                return lng
            }
        } catch (ex: Exception) {
            Log.e("Harita Verilerini Conve", ex.toString())
        }
        return null
    }
}