package com.example.amasyasut.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amasyasut.Adapter.MahalleAdapter
import com.example.amasyasut.BottomNavigationViewHelper

import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.LoadingDialog
import com.example.amasyasut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_siparisler.*
import kotlin.Exception


class SiparislerActivity : AppCompatActivity() {


    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var kullaniciAdi: String
    private val ACTIVITY_NO = 0

    // lateinit var progressDialog: ProgressDialog
    var hndler = Handler()
    var ref = FirebaseDatabase.getInstance().reference

    var loading: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_siparisler)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        //    mAuth.signOut()
        initMyAuthStateListener()
        try {
            setupKullaniciAdi()
        } catch (e: Exception) {
            ref.child("Hatalar/SiparişlerHatası").push().setValue(e.message.toString())
        }

        setupNavigationView()

        zamanAyarı()

        dialogCalistir()

        hndler.postDelayed(Runnable { dialogGizle() }, 1000)

        tvFiyatListesi.setOnClickListener {
            var builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            val view = inflater.inflate(R.layout.dialog_fiyat_listesi, null)

            builder.setView(view)
            builder.setTitle("Fiyat Listesi")

            var dialog: Dialog = builder.create()
            dialog.show()
        }
    }

    fun dialogGizle() {
        loading?.let { if (it.isShowing) it.cancel() }

    }

    fun dialogCalistir() {
        dialogGizle()
        loading = LoadingDialog.startDialog(this)
    }

    fun setupKullaniciAdi() {
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                kullaniciAdi = p0.child("user_name").value.toString()
                hndler.postDelayed(Runnable { veri() }, 750)
            }

        })
    }

    private fun veri() {
        imgSighOut.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ref.child("Siparisler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    val mahalleList = ArrayList<String>()
                    for (ds in p0.children) {
                        try {
                            mahalleList.add(ds.key.toString())
                        } catch (e: Exception) {
                            Log.e("hata", "siparişler activity ${e.message.toString()}")
                            ref.child("Hatalar/SiparişlerHatası124").push().setValue(e.message.toString())

                        }

                    }
                    dialogGizle()
                    val adapter = MahalleAdapter(this@SiparislerActivity, mahalleList, kullaniciAdi)
                    rcMahalleler.layoutManager = LinearLayoutManager(this@SiparislerActivity, LinearLayoutManager.VERTICAL, false)
                    rcMahalleler.adapter = adapter
                }
            }
        })

    }

    private fun zamanAyarı() {
        //   FirebaseMessaging.getInstance().subscribeToTopic("msgNotification");
        FirebaseDatabase.getInstance().reference.child("Zaman").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                var gece3 = p0.child("gece3").value.toString().toLong()
                var suankıZaman = System.currentTimeMillis()

                if (gece3 < suankıZaman) {

                    var guncelGece3 = gece3 + 86400000

                    FirebaseDatabase.getInstance().reference.child("Zaman").child("gece3").setValue(guncelGece3).addOnCompleteListener {
                        FirebaseDatabase.getInstance().reference.child("Zaman").child("gerigece3").setValue(gece3)
                    }

                }
            }
        })
    }

    private fun initMyAuthStateListener() {
        mAuthListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val kullaniciGirisi = p0.currentUser
                if (kullaniciGirisi != null) { //eğer kişi giriş yaptıysa nul gorunmez. giriş yapmadıysa null olur
                } else {
                    startActivity(Intent(this@SiparislerActivity, LoginActivity::class.java))
                }
            }
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
