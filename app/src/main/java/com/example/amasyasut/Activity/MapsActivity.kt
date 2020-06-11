package com.example.amasyasut.Activity

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
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.amasyasut.BottomNavigationViewHelper
import com.example.amasyasut.Datalar.SiparisData
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
import kotlinx.android.synthetic.main.dialog_map_item_siparisler.view.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var progressDialog: ProgressDialog


    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var kullaniciAdi: String

    var konum: Boolean = false
    val handler = Handler()

    val ref = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid



        setupNavigationView()
        setupKullaniciAdi()
        konumIzni()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage(" Harita Yükleniyor... Lütfen bekleyin...")
        progressDialog.setCancelable(true)
        progressDialog.show()

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        konum = sharedPreferences.getBoolean("Konum", false)
        swKonum.isChecked = konum
        Toast.makeText(this, "Bazı Siparişler Adres Bulunamadığından gösterilmeyebilir. Dikkatli Ol.!!!", Toast.LENGTH_LONG).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = konum
        // Add a marker in Sydney and move the camera
        val amasya = LatLng(40.6565, 35.8373)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amasya, 12.7f))

        handler.postDelayed({ veriler() }, 1000)
        handler.postDelayed({ progressDialog.dismiss() }, 5000)
    }

    private fun veriler() {

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
                                        var gelenData = ds.getValue(SiparisData::class.java)!!
                                        siparisList.add(gelenData)
                                        if (gelenData.siparis_adres.toString() != "Adres") {
                                            if (gelenData.siparis_teslim_tarihi!!.compareTo(System.currentTimeMillis()) == -1) {
                                                var konumVarMi = gelenData.musteri_zkonum.toString().toBoolean()
                                                if (konumVarMi) {
                                                    try {
                                                        var lat = gelenData.musteri_zlat!!.toDouble()
                                                        var long = gelenData.musteri_zlong!!.toDouble()
                                                        val adres = LatLng(lat, long)
                                                        var myMarker = mMap.addMarker(
                                                            MarkerOptions().position(adres).title(gelenData.musteri_ad_soyad).snippet(gelenData.siparis_adres + " / " + gelenData.siparis_apartman)
                                                        )

                                                        myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.order_map))
                                                        myMarker.tag = gelenData.siparis_key
                                                        progressDialog.dismiss()
                                                    } catch (e: Exception) {
                                                        Toast.makeText(
                                                            this@MapsActivity,
                                                            gelenData.musteri_ad_soyad + " adlı müşterinin konumu hatalı. Lütfen Müşterinin ev konum switch'ini kapatın",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }

                                                } else {
                                                    var lat = convertAddressLat(gelenData.siparis_mah + " mahallesi " + gelenData.siparis_adres + " Amasya 05000")!!.toDouble()
                                                    var long = convertAddressLng(gelenData.siparis_mah + " mahallesi " + gelenData.siparis_adres + " Amasya 39750")!!.toDouble()
                                                    val adres = LatLng(lat, long)
                                                    var myMarker = mMap.addMarker(
                                                        MarkerOptions().position(adres).title(gelenData.musteri_ad_soyad).snippet(gelenData.siparis_adres + " / " + gelenData.siparis_apartman)
                                                    )
                                                    progressDialog.dismiss()
                                                    myMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.order_map))
                                                    myMarker.tag = gelenData.siparis_key
                                                    val id = gelenData.siparis_mah.toString()
                                                    myMarker.snippet = id
                                                }
                                                mMap.setOnMarkerClickListener {
                                                    it.tag

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
                                                                view.tv5ltSut.text = gelenData!!.zz_5litre
                                                                view.tv3ltSut.text = gelenData!!.zz_3litre
                                                            } catch (e: Exception) {
                                                                Log.e("haritalar", e.message.toString())
                                                            }


                                                            var sut3ltFiyat = gelenData.zz_3litre.toString().toInt()
                                                            var sut5ltFiyat = gelenData.zz_5litre.toString().toInt()
                                                            view.tvFiyat.text = ((sut3ltFiyat * 13.5) + (sut5ltFiyat * 22.5)).toString() + " tl"

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
                                                                                gelenData.zz_5litreFiyat
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
                                            }
                                        }
                                    }
                                }
                            }

                        })


                    }


                } else {
                    //sipariş yok
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