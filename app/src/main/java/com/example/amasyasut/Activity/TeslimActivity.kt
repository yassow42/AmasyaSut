package com.example.amasyasut.Activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amasyasut.Adapter.TeslimEdilenlerAdapter
import com.example.amasyasut.BottomNavigationViewHelper
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


import kotlinx.android.synthetic.main.activity_teslim.*
import java.text.SimpleDateFormat
import java.util.*

import kotlin.collections.ArrayList

class TeslimActivity : AppCompatActivity() {
    private val ACTIVITY_NO = 2
    lateinit var suankiTeslimList: ArrayList<SiparisData>
    lateinit var butunTeslimList: ArrayList<SiparisData>

    lateinit var progressDialog: ProgressDialog
    val hndler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teslim)
        setupNavigationView()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setupBtn()

        suankiTeslimList = ArrayList()
        butunTeslimList = ArrayList()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Lütfen bekleyin...")
        progressDialog.setCancelable(false)
        progressDialog.show()



        hndler.postDelayed(Runnable { setupVeri() }, 2000)
        hndler.postDelayed(Runnable { progressDialog.dismiss() }, 5000)



    }


    private fun setupBtn() {
        tvZamandan.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(System.currentTimeMillis())
        tvZamana.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(System.currentTimeMillis())

        var calZamandan = Calendar.getInstance()
        val dateSetListenerZamandan = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calZamandan.set(Calendar.YEAR, year)
            calZamandan.set(Calendar.MONTH, monthOfYear)
            calZamandan.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale("tr"))
            tvZamandan.text = sdf.format(calZamandan.time)
        }

        val timeSetListenerZamandan = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calZamandan.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calZamandan.set(Calendar.MINUTE, minute)
        }

        var calZamana = Calendar.getInstance()
        val dateSetListenerZamana = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calZamana.set(Calendar.YEAR, year)
            calZamana.set(Calendar.MONTH, monthOfYear)
            calZamana.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale("tr"))
            tvZamana.text = sdf.format(calZamana.time)
        }

        val timeSetListenerZamana = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calZamana.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calZamana.set(Calendar.MINUTE, minute)
        }



        tvZamandan.setOnClickListener {
            DatePickerDialog(this, dateSetListenerZamandan, calZamandan.get(Calendar.YEAR), calZamandan.get(Calendar.MONTH), calZamandan.get(Calendar.DAY_OF_MONTH)).show()
            TimePickerDialog(this, timeSetListenerZamandan, calZamandan.get(Calendar.HOUR_OF_DAY), calZamandan.get(Calendar.MINUTE), true).show()

        }
        tvZamana.setOnClickListener {
            DatePickerDialog(this, dateSetListenerZamana, calZamana.get(Calendar.YEAR), calZamana.get(Calendar.MONTH), calZamana.get(Calendar.DAY_OF_MONTH)).show()
            TimePickerDialog(this, timeSetListenerZamana, calZamana.get(Calendar.HOUR_OF_DAY), calZamana.get(Calendar.MINUTE), true).show()
        }


        imgTarih.setOnClickListener {
            suankiTeslimList.clear()

            var sut3ltSayisi = 0
            var sut5ltSayisi = 0
            var yumurtaSayisi = 0

            for (ds in butunTeslimList) {


                if (calZamandan.timeInMillis < ds.siparis_teslim_zamani!!.toLong() && ds.siparis_teslim_zamani!!.toLong() < calZamana.timeInMillis) {
                    suankiTeslimList.add(ds)
//                    sut3ltSayisi = ds.cig_sut!!.toInt() + sut3ltSayisi
      //              sut5ltSayisi = ds.cokelek!!.toInt() + sut5ltSayisi
                }

                suankiTeslimList.sortByDescending { it.siparis_teslim_zamani }
                tvFiyatGenelTeslim.text = ((sut3ltSayisi * 16) + (sut5ltSayisi * 22) + yumurtaSayisi).toString() + " tl"
                setupRecyclerView()

            }
        }
        /*
        imgOptions.setOnClickListener {

            val popup = PopupMenu(this, imgOptions)
            popup.inflate(R.menu.popup_menu_teslim)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                var sut3ltSayisi = 0
                var sut5ltSayisi = 0
                var yumurtaSayisi = 0
                when (it.itemId) {
                    R.id.samet -> {
                        suankiTeslimList.clear()

                        for (ds in butunTeslimList) {
                            if (calZamandan.timeInMillis < ds.siparis_teslim_zamani!!.toLong() && ds.siparis_teslim_zamani!!.toLong() < calZamana.timeInMillis) {
                                if (ds.siparisi_giren == "Samet") {
                                    suankiTeslimList.add(ds)
                                    sut3ltSayisi = ds.zz_3litre!!.toInt() + sut3ltSayisi
                                    sut5ltSayisi = ds.zz_5litre!!.toInt() + sut5ltSayisi

                                }
                            }
                        }


                        tvFiyatGenelTeslim.text = ((sut3ltSayisi * 16) + (sut5ltSayisi * 22) + yumurtaSayisi).toString() + " tl"
                        suankiTeslimList.sortByDescending { it.siparis_teslim_zamani }
                        setupRecyclerView()
                    }
                    R.id.Engin -> {
                        suankiTeslimList.clear()

                        for (ds in butunTeslimList) {
                            if (calZamandan.timeInMillis < ds.siparis_teslim_zamani!!.toLong() && ds.siparis_teslim_zamani!!.toLong() < calZamana.timeInMillis) {

                                if (ds.siparisi_giren == "Umit") {
                                    suankiTeslimList.add(ds)
                                    sut3ltSayisi = ds.zz_3litre!!.toInt() + sut3ltSayisi
                                    sut5ltSayisi = ds.zz_5litre!!.toInt() + sut5ltSayisi

                                }

                            }
                        }


                        tvFiyatGenelTeslim.text = ((sut3ltSayisi * 16) + (sut5ltSayisi * 22) + yumurtaSayisi).toString() + " tl"
                        suankiTeslimList.sortByDescending { it.siparis_teslim_zamani }
                        setupRecyclerView()
                    }

                    R.id.nihat -> {
                        suankiTeslimList.clear()

                        for (ds in butunTeslimList) {
                            if (calZamandan.timeInMillis < ds.siparis_teslim_zamani!!.toLong() && ds.siparis_teslim_zamani!!.toLong() < calZamana.timeInMillis) {

                                if (ds.siparisi_giren == "Nihat") {
                                    suankiTeslimList.add(ds)
                                    sut3ltSayisi = ds.zz_3litre!!.toInt() + sut3ltSayisi
                                    sut5ltSayisi = ds.zz_5litre!!.toInt() + sut5ltSayisi

                                }

                            }
                        }


                        tvFiyatGenelTeslim.text = ((sut3ltSayisi * 16) + (sut5ltSayisi * 22) + yumurtaSayisi).toString() + " tl"
                        suankiTeslimList.sortByDescending { it.siparis_teslim_zamani }
                        setupRecyclerView()
                    }


                }
                return@OnMenuItemClickListener true
            })
            popup.show()

        }
*/
    }

    private fun setupVeri() {
        FirebaseDatabase.getInstance().reference.child("Teslim_siparisler").orderByChild("siparis_teslim_zamani").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(data: DataSnapshot) {
                if (data.hasChildren()) {
                    var sut3ltSayisi = 0
                    var sut5ltSayisi = 0
                    var yumurtaSayisi = 0
                    FirebaseDatabase.getInstance().reference.child("Zaman").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            var gece3GelenZaman = p0.child("gece3").value.toString().toLong()
                            var suankıZaman = System.currentTimeMillis()

                            if (gece3GelenZaman < suankıZaman) {
                                var guncelGece3 = gece3GelenZaman + 86400000
                                FirebaseDatabase.getInstance().reference.child("Zaman").child("gece3").setValue(guncelGece3)
                            }
                            for (ds in data.children) {
                                var gelenData = ds.getValue(SiparisData::class.java)!!
                                butunTeslimList.add(gelenData)
                                if (gece3GelenZaman - 86400000 < gelenData.siparis_teslim_zamani!!.toLong() && gelenData.siparis_teslim_zamani!!.toLong() < gece3GelenZaman) {
                                    suankiTeslimList.add(gelenData)
                                    sut3ltSayisi = gelenData.zz_3litre!!.toInt() + sut3ltSayisi
                                    sut5ltSayisi = gelenData.zz_5litre!!.toInt() + sut5ltSayisi

                                }
                            }
                            progressDialog.dismiss()
                            suankiTeslimList.sortByDescending { it.siparis_teslim_zamani }
                            tvFiyatGenelTeslim.text = ((sut3ltSayisi * 16) + (sut5ltSayisi * 22) + yumurtaSayisi).toString() + " tl"
                            setupRecyclerView()
                        }
                    })
                }
                else{
                    progressDialog.setMessage("Veri Alınamıyor...")
                    hndler.postDelayed(Runnable { progressDialog.dismiss() }, 2000)

                }
            }
        })
    }

    private fun setupRecyclerView() {
        rcTeslimEdilenler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val Adapter = TeslimEdilenlerAdapter(this, suankiTeslimList)
        rcTeslimEdilenler.adapter = Adapter
        rcTeslimEdilenler.setHasFixedSize(true)
    }

    fun setupNavigationView() {

        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavt)
        BottomNavigationViewHelper.setupNavigation(this, bottomNavt) // Bottomnavhelper içinde setupNavigationda context ve nav istiyordu verdik...
        var menu = bottomNavt.menu
        var menuItem = menu.getItem(ACTIVITY_NO)
        menuItem.setChecked(true)
    }
}
