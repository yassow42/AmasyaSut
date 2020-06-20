package com.example.amasyasut.Activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amasyasut.Adapter.MusteriAdapter
import com.example.amasyasut.BottomNavigationViewHelper

import com.example.amasyasut.Datalar.MusteriData
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_musteriler.*
import kotlinx.android.synthetic.main.dialog_musteri_ekle.view.*
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*


import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MusterilerActivity : AppCompatActivity() {


    private val ACTIVITY_NO = 3
    var secilenMah: String? = null
    lateinit var musteriList: ArrayList<MusteriData>
    lateinit var musteriAdList: ArrayList<String>
    lateinit var dialogViewSpArama: View

    lateinit var dialogView: View

    lateinit var mahalleler: ArrayList<String>
    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var userID: String
    var kullaniciAdi: String? = null

    lateinit var progressDialog: ProgressDialog
    var hndler = Handler()
    var ref = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteriler)
        setupNavigationView()
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        setupKullaniciAdi()
        musteriList = ArrayList()
        musteriAdList = ArrayList()
        mahalleler = ArrayList()
        mahalleEkle()
        setupBtn()
        setupSpinner()


    }

    private fun mahalleEkle() {

        mahalleler.add("Market")
        mahalleler.add("Akbilek")
        mahalleler.add("Aşağı")
        mahalleler.add("Bahçeleriçi")
        mahalleler.add("Beyazıtpaşa")
        mahalleler.add("Boğazköy")
        mahalleler.add("Çakallar")
        mahalleler.add("Demet Evler")
        mahalleler.add("Dere")
        mahalleler.add("Ellibeşevler")
        mahalleler.add("Fethiye")
        mahalleler.add("Fındıklı")
        mahalleler.add("Gökmedrese")
        mahalleler.add("Göllü Bağları")
        mahalleler.add("Gümüşlü")
        mahalleler.add("Hacı İlyas")
        mahalleler.add("Hacılar Meydanı")
        mahalleler.add("Hatuniye")
        mahalleler.add("Helvacı")
        mahalleler.add("Hızır Paşa")
        mahalleler.add("İhsaniye")
        mahalleler.add("Karasenir")
        mahalleler.add("Kirazlı Dere")
        mahalleler.add("Koza")
        mahalleler.add("Kurşunlu")
        mahalleler.add("Mehmet Paşa")

    }

    private fun setupVeri() {
        musteriList.clear()
        ref.child("Musteriler").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(p0: DataSnapshot) {

                if (p0.hasChildren()) {
                    for (ds in p0.children) {
                        try {
                            var gelenData = ds.getValue(MusteriData::class.java)!!
                            var musteriAdlari = gelenData.musteri_ad_soyad
                            musteriList.add(gelenData)
                            musteriAdList.add(musteriAdlari.toString())
                        } catch (e: Exception) {
                            ref.child("Hatalar/musteriDataHata").push().setValue(e.message.toString())
                        }
                    }
                    var adapterSearch = ArrayAdapter<String>(this@MusterilerActivity, android.R.layout.simple_expandable_list_item_1, musteriAdList)
                    searchMs.setAdapter(adapterSearch)
                    tvMusteri.text = "Müşteriler " + "(" + (musteriList.size) + ")"
                    setupRecyclerViewMusteriler()
                    progressDialog.dismiss()
                } else {
                    Toast.makeText(this@MusterilerActivity, "Müşteri Bilgisi Alınamadı.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupBtn() {
        imgMusteriEkle.setOnClickListener {
            var builder: AlertDialog.Builder = AlertDialog.Builder(this)
            var inflater: LayoutInflater = layoutInflater
            dialogView = inflater.inflate(R.layout.dialog_musteri_ekle, null)

            builder.setView(dialogView)
            builder.setTitle("Müşteri Ekle")
//////////////////////spinner

            var adapterMah = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mahalleler)
            adapterMah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogView.spnMahler.adapter = adapterMah
            dialogView.spnMahler.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(this@MusterilerActivity, "Lütfen Mahalle Seç", Toast.LENGTH_LONG).show()
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                    secilenMah = dialogView.spnMahler.selectedItem.toString()

                }

            }

//////////////////////spinner

            dialogView.etAdres.addTextChangedListener(watcherAdres)

            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                }

            })
            builder.setPositiveButton("Ekle", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {


                    var musteriAdi = "Müşteri"
                    if (!dialogView.etMusteriAdSoyad.text.toString().isNullOrEmpty()) {
                        musteriAdi = dialogView.etMusteriAdSoyad.text.toString().trim().capitalize()
                    } else if (musteriAdi == "Müşteri") {
                        Toast.makeText(this@MusterilerActivity, "Müşteri Adı Girmedin", Toast.LENGTH_LONG).show()
                    }
                    var musteriAdres = "Adres"
                    if (!dialogView.etAdres.text.toString().isNullOrEmpty()) {
                        musteriAdres = dialogView.etAdres.text.toString().trim()
                    } else if (musteriAdres == "Adres") {
                        Toast.makeText(this@MusterilerActivity, "Adres Girmedin", Toast.LENGTH_LONG).show()
                    }

                    var musteriApt = "Apartman"
                    if (!dialogView.etApartman.text.toString().isNullOrEmpty()) {
                        musteriApt = dialogView.etApartman.text.toString().trim()
                    } else if (musteriAdres == "Adres") {
                        Toast.makeText(this@MusterilerActivity, "Adres Girmedin", Toast.LENGTH_LONG).show()
                    }

                    var musteriTel = "Tel"
                    if (!dialogView.etTelefon.text.toString().isNullOrEmpty()) {
                        musteriTel = dialogView.etTelefon.text.toString()
                    } else if (musteriTel == "Tel") {
                        Toast.makeText(this@MusterilerActivity, "Tel Girmedin", Toast.LENGTH_LONG).show()
                    }


                    var musteriBilgileri = MusteriData(musteriAdi, secilenMah, musteriAdres, musteriApt, musteriTel, null, false, null, null)
                    ref.child("Musteriler").child(musteriAdi).setValue(musteriBilgileri).addOnCompleteListener {
                        ref.child("Musteriler").child(musteriAdi).child("siparis_son_zaman").setValue(ServerValue.TIMESTAMP)
                        setupVeri()
                    }

                }
            })

            var dialog: Dialog = builder.create()
            dialog.show()

        }

        imgMusteriAra.setOnClickListener {
            var arananMusteriAdi = searchMs.text.toString()
            //  Log.e("ass1",arananMusteriAdi)
            val arananMusteriVarMi = musteriAdList.containsAll(listOf(arananMusteriAdi))

            if (arananMusteriVarMi) {
                var ref = FirebaseDatabase.getInstance().reference
                ref.child("Musteriler").child(arananMusteriAdi).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        if (p0.hasChildren()) {
                            var musteriData = p0.getValue(MusteriData::class.java)!!
                            var builder: AlertDialog.Builder = AlertDialog.Builder(this@MusterilerActivity)
                            dialogViewSpArama = inflate(this@MusterilerActivity, R.layout.dialog_siparis_ekle, null)

                            dialogViewSpArama.tbYogurtlar.visibility = View.GONE
                            dialogViewSpArama.tbDigerleri.visibility = View.GONE
                            dialogViewSpArama.tbPeynirler.visibility = View.GONE

                            dialogViewSpArama.asagiPeynir.setOnClickListener {
                                dialogViewSpArama.tbPeynirler.visibility = View.VISIBLE
                                dialogViewSpArama.yukariPeynir.visibility = View.VISIBLE
                                dialogViewSpArama.asagiPeynir.visibility = View.GONE
                            }
                            dialogViewSpArama.yukariPeynir.setOnClickListener {
                                dialogViewSpArama.tbPeynirler.visibility = View.GONE
                                dialogViewSpArama.yukariPeynir.visibility = View.GONE
                                dialogViewSpArama.asagiPeynir.visibility = View.VISIBLE

                            }

                            dialogViewSpArama.asagiYogurt.setOnClickListener {
                                dialogViewSpArama.tbYogurtlar.visibility = View.VISIBLE
                                dialogViewSpArama.yukariYogurt.visibility = View.VISIBLE
                                dialogViewSpArama.asagiYogurt.visibility = View.GONE
                            }
                            dialogViewSpArama.yukariYogurt.setOnClickListener {
                                dialogViewSpArama.tbYogurtlar.visibility = View.GONE
                                dialogViewSpArama.yukariYogurt.visibility = View.GONE
                                dialogViewSpArama.asagiYogurt.visibility = View.VISIBLE

                            }
                            dialogViewSpArama.asagiDiger.setOnClickListener {
                                dialogViewSpArama.tbDigerleri.visibility = View.VISIBLE
                                dialogViewSpArama.yukariDiger.visibility = View.VISIBLE
                                dialogViewSpArama.asagiDiger.visibility = View.GONE
                            }
                            dialogViewSpArama.yukariDiger.setOnClickListener {
                                dialogViewSpArama.tbDigerleri.visibility = View.GONE
                                dialogViewSpArama.yukariDiger.visibility = View.GONE
                                dialogViewSpArama.asagiDiger.visibility = View.VISIBLE

                            }



                            dialogViewSpArama.tvZamanEkleDialog.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(System.currentTimeMillis())
                            var cal = Calendar.getInstance()
                            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                                cal.set(Calendar.YEAR, year)
                                cal.set(Calendar.MONTH, monthOfYear)
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                                val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
                                val sdf = SimpleDateFormat(myFormat, Locale("tr"))
                                dialogViewSpArama.tvZamanEkleDialog.text = sdf.format(cal.time)
                            }
                            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                cal.set(Calendar.MINUTE, minute)
                            }
                            dialogViewSpArama.tvZamanEkleDialog.setOnClickListener {
                                DatePickerDialog(this@MusterilerActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                                TimePickerDialog(this@MusterilerActivity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                            }

                            builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog!!.dismiss()
                                }

                            })
                            builder.setPositiveButton("Sipariş Ekle", object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {


                                    var sut3lt = "0"
                                    if (dialogViewSpArama.et3lt.text.toString().isNotEmpty()) {
                                        sut3lt = dialogViewSpArama.et3lt.text.toString()
                                    }
                                    var sut3ltFiyat = "0"
                                    if (dialogViewSpArama.et3ltFiyat.text.toString().isNotEmpty()) {
                                        sut3ltFiyat = dialogViewSpArama.et3ltFiyat.text.toString()
                                    }

                                    var sut5lt = "0"
                                    if (dialogViewSpArama.et5lt.text.toString().isNotEmpty()) {
                                        sut5lt = dialogViewSpArama.et5lt.text.toString()
                                    }

                                    var sut5ltFiyat = "0"
                                    if (dialogViewSpArama.et5ltFiyat.text.toString().isNotEmpty()) {
                                        sut5ltFiyat = dialogViewSpArama.et5ltFiyat.text.toString()
                                    }

                                    var bp500 = "0"
                                    if (dialogViewSpArama.etBp500.text.toString().isNotEmpty()) {
                                        bp500 = dialogViewSpArama.etBp500.text.toString()
                                    }

                                    var bp500Fiyat = "0"
                                    if (dialogViewSpArama.etBp500Fiyat.text.toString().isNotEmpty()) {
                                        bp500Fiyat = dialogViewSpArama.etBp500Fiyat.text.toString()
                                    }
                                    var bp800 = "0"
                                    if (dialogViewSpArama.etBp800.text.toString().isNotEmpty()) {
                                        bp800 = dialogViewSpArama.etBp800.text.toString()
                                    }

                                    var bp800Fiyat = "0"
                                    if (dialogViewSpArama.etBp800Fiyat.text.toString().isNotEmpty()) {
                                        bp800Fiyat = dialogViewSpArama.etBp800Fiyat.text.toString()
                                    }
                                    var bp5kg = "0"
                                    if (dialogViewSpArama.etBp5kg.text.toString().isNotEmpty()) {
                                        bp5kg = dialogViewSpArama.etBp5kg.text.toString()
                                    }

                                    var bp5kgFiyat = "0"
                                    if (dialogViewSpArama.etBp5kgFiyat.text.toString().isNotEmpty()) {
                                        bp5kgFiyat = dialogViewSpArama.etBp5kgFiyat.text.toString()
                                    }

                                    var bp17kg = "0"
                                    if (dialogViewSpArama.etBp17kg.text.toString().isNotEmpty()) {
                                        bp17kg = dialogViewSpArama.etBp17kg.text.toString()
                                    }
                                    var bp17kgFiyat = "0"
                                    if (dialogViewSpArama.etBp17kgFiyat.text.toString().isNotEmpty()) {
                                        bp17kgFiyat = dialogViewSpArama.etBp17kgFiyat.text.toString()
                                    }

                                    var kasar400 = "0"
                                    if (dialogViewSpArama.etKasar400.text.toString().isNotEmpty()) {
                                        kasar400 = dialogViewSpArama.etKasar400.text.toString()
                                    }

                                    var kasar400Fiyat = "0"
                                    if (dialogViewSpArama.etKasar400Fiyat.text.toString().isNotEmpty()) {
                                        kasar400Fiyat = dialogViewSpArama.etKasar400Fiyat.text.toString()
                                    }


                                    var kasar700 = "0"
                                    if (dialogViewSpArama.etKasar700.text.toString().isNotEmpty()) {
                                        kasar700 = dialogViewSpArama.etKasar700.text.toString()
                                    }

                                    var kasar700Fiyat = "0"
                                    if (dialogViewSpArama.etKasar700Fiyat.text.toString().isNotEmpty()) {
                                        kasar700Fiyat = dialogViewSpArama.etKasar700Fiyat.text.toString()
                                    }


                                    var kasar1kg = "0"
                                    if (dialogViewSpArama.etKasar1kg.text.toString().isNotEmpty()) {
                                        kasar1kg = dialogViewSpArama.etKasar1kg.text.toString()
                                    }

                                    var kasar1kgFiyat = "0"
                                    if (dialogViewSpArama.etKasar1kgFiyat.text.toString().isNotEmpty()) {
                                        kasar1kgFiyat = dialogViewSpArama.etKasar1kgFiyat.text.toString()
                                    }

                                    var kasar2kgPide = "0"
                                    if (dialogViewSpArama.etKasar2kgPide.text.toString().isNotEmpty()) {
                                        kasar2kgPide = dialogViewSpArama.etKasar2kgPide.text.toString()
                                    }

                                    var kasar2kgPideFiyat = "0"
                                    if (dialogViewSpArama.etKasar2kgPideFiyat.text.toString().isNotEmpty()) {
                                        kasar2kgPideFiyat = dialogViewSpArama.etKasar2kgPideFiyat.text.toString()
                                    }

                                    var kasar2kgTost = "0"
                                    if (dialogViewSpArama.etKasar2kgTost.text.toString().isNotEmpty()) {
                                        kasar2kgTost = dialogViewSpArama.etKasar2kgTost.text.toString()
                                    }

                                    var kasar2kgTostFiyat = "0"
                                    if (dialogViewSpArama.etKasar2kgTostFiyat.text.toString().isNotEmpty()) {
                                        kasar2kgTostFiyat = dialogViewSpArama.etKasar2kgTostFiyat.text.toString()
                                    }

                                    var etKoy5kg = "0"
                                    if (dialogViewSpArama.etKoy5kg.text.toString().isNotEmpty()) {
                                        etKoy5kg = dialogViewSpArama.etKoy5kg.text.toString()
                                    }

                                    var etKoy5kgFiyat = "0"
                                    if (dialogViewSpArama.etKoy5kgFiyat.text.toString().isNotEmpty()) {
                                        etKoy5kgFiyat = dialogViewSpArama.etKoy5kgFiyat.text.toString()
                                    }
                                    var etKoy15kg = "0"
                                    if (dialogViewSpArama.etKoy15kg.text.toString().isNotEmpty()) {
                                        etKoy15kg = dialogViewSpArama.etKoy15kg.text.toString()
                                    }

                                    var etKoy15kgFiyat = "0"
                                    if (dialogViewSpArama.etKoy15kgFiyat.text.toString().isNotEmpty()) {
                                        etKoy15kgFiyat = dialogViewSpArama.etKoy15kgFiyat.text.toString()
                                    }

                                    var etTulum250 = "0"
                                    if (dialogViewSpArama.etTulum250.text.toString().isNotEmpty()) {
                                        etTulum250 = dialogViewSpArama.etTulum250.text.toString()
                                    }

                                    var etTulum250Fiyat = "0"
                                    if (dialogViewSpArama.etTulum250Fiyat.text.toString().isNotEmpty()) {
                                        etTulum250Fiyat = dialogViewSpArama.etTulum250Fiyat.text.toString()
                                    }

                                    var etCerkez = "0"
                                    if (dialogViewSpArama.etCerkez.text.toString().isNotEmpty()) {
                                        etCerkez = dialogViewSpArama.etCerkez.text.toString()
                                    }

                                    var etCerkezFiyat = "0"
                                    if (dialogViewSpArama.etCerkezFiyat.text.toString().isNotEmpty()) {
                                        etCerkezFiyat = dialogViewSpArama.etCerkezFiyat.text.toString()
                                    }

                                    var et9kgLor = "0"
                                    if (dialogViewSpArama.et9kgLor.text.toString().isNotEmpty()) {
                                        et9kgLor = dialogViewSpArama.et9kgLor.text.toString()
                                    }

                                    var et9kgLorFiyat = "0"
                                    if (dialogViewSpArama.et9kgLorFiyat.text.toString().isNotEmpty()) {
                                        et9kgLorFiyat = dialogViewSpArama.et9kgLorFiyat.text.toString()
                                    }

                                    var etCokelek = "0"
                                    if (dialogViewSpArama.etCokelek.text.toString().isNotEmpty()) {
                                        etCokelek = dialogViewSpArama.etCokelek.text.toString()
                                    }

                                    var etCokelekFiyat = "0"
                                    if (dialogViewSpArama.etCokelekFiyat.text.toString().isNotEmpty()) {
                                        etCokelekFiyat = dialogViewSpArama.etCokelekFiyat.text.toString()
                                    }


                                    var etYogurt200 = "0"
                                    if (dialogViewSpArama.etYogurt200.text.toString().isNotEmpty()) {
                                        etYogurt200 = dialogViewSpArama.etYogurt200.text.toString()
                                    }

                                    var etYogurt200Fiyat = "0"
                                    if (dialogViewSpArama.etYogurt200Fiyat.text.toString().isNotEmpty()) {
                                        etYogurt200Fiyat = dialogViewSpArama.etYogurt200Fiyat.text.toString()
                                    }

                                    var etYogurt500 = "0"
                                    if (dialogViewSpArama.etYogurt500.text.toString().isNotEmpty()) {
                                        etYogurt500 = dialogViewSpArama.etYogurt500.text.toString()
                                    }

                                    var etYogurt500Fiyat = "0"
                                    if (dialogViewSpArama.etYogurt500Fiyat.text.toString().isNotEmpty()) {
                                        etYogurt500Fiyat = dialogViewSpArama.etYogurt500Fiyat.text.toString()
                                    }


                                    var etYogurt200V = "0"
                                    if (dialogViewSpArama.etYogurtYarım200V.text.toString().isNotEmpty()) {
                                        etYogurt200V = dialogViewSpArama.etYogurtYarım200V.text.toString()
                                    }

                                    var etYogurt200VFiyat = "0"
                                    if (dialogViewSpArama.etYogurt200VFiyat.text.toString().isNotEmpty()) {
                                        etYogurt200VFiyat = dialogViewSpArama.etYogurt200VFiyat.text.toString()
                                    }

                                    var etYogurt1250 = "0"
                                    if (dialogViewSpArama.etYogurt1250.text.toString().isNotEmpty()) {
                                        etYogurt1250 = dialogViewSpArama.etYogurt1250.text.toString()
                                    }

                                    var etYogurt1250Fiyat = "0"
                                    if (dialogViewSpArama.etYogurt1250Fiyat.text.toString().isNotEmpty()) {
                                        etYogurt1250Fiyat = dialogViewSpArama.etYogurt1250Fiyat.text.toString()
                                    }

                                    var etYogurtTam2Kg = "0"
                                    if (dialogViewSpArama.etYogurtTam2Kg.text.toString().isNotEmpty()) {
                                        etYogurtTam2Kg = dialogViewSpArama.etYogurtTam2Kg.text.toString()
                                    }

                                    var etYogurtTam2KgFiyat = "0"
                                    if (dialogViewSpArama.etYogurtTam2KgFiyat.text.toString().isNotEmpty()) {
                                        etYogurtTam2KgFiyat = dialogViewSpArama.etYogurtTam2KgFiyat.text.toString()
                                    }


                                    var etYogurtTam9Kg = "0"
                                    if (dialogViewSpArama.etYogurtTam9Kg.text.toString().isNotEmpty()) {
                                        etYogurtTam9Kg = dialogViewSpArama.etYogurtTam9Kg.text.toString()
                                    }

                                    var etYogurtTam9KgFiyat = "0"
                                    if (dialogViewSpArama.etYogurtTam9KgFiyat.text.toString().isNotEmpty()) {
                                        etYogurtTam9KgFiyat = dialogViewSpArama.etYogurtTam9KgFiyat.text.toString()
                                    }

                                    var etYogurt2750KgTava = "0"
                                    if (dialogViewSpArama.etYogurt2750KgTava.text.toString().isNotEmpty()) {
                                        etYogurt2750KgTava = dialogViewSpArama.etYogurt2750KgTava.text.toString()
                                    }

                                    var etYogurt2750KgTavaFiyat = "0"
                                    if (dialogViewSpArama.etYogurt2750KgTavaFiyat.text.toString().isNotEmpty()) {
                                        etYogurt2750KgTavaFiyat = dialogViewSpArama.etYogurt2750KgTavaFiyat.text.toString()
                                    }

                                    var etYogurt2KgDM = "0"
                                    if (dialogViewSpArama.etYogurt2KgDM.text.toString().isNotEmpty()) {
                                        etYogurt2KgDM = dialogViewSpArama.etYogurt2KgDM.text.toString()
                                    }

                                    var etYogurt2KgDMFiyat = "0"
                                    if (dialogViewSpArama.etYogurt2KgDMFiyat.text.toString().isNotEmpty()) {
                                        etYogurt2KgDMFiyat = dialogViewSpArama.etYogurt2KgDMFiyat.text.toString()
                                    }

                                    var etYogurt3KgDM = "0"
                                    if (dialogViewSpArama.etYogurt3KgDM.text.toString().isNotEmpty()) {
                                        etYogurt3KgDM = dialogViewSpArama.etYogurt2KgDM.text.toString()
                                    }

                                    var etYogurt3KgDMFiyat = "0"
                                    if (dialogViewSpArama.etYogurt3KgDMFiyat.text.toString().isNotEmpty()) {
                                        etYogurt3KgDMFiyat = dialogViewSpArama.etYogurt3KgDMFiyat.text.toString()
                                    }


                                    var etYogurt4KgDM = "0"
                                    if (dialogViewSpArama.etYogurt4KgDM.text.toString().isNotEmpty()) {
                                        etYogurt4KgDM = dialogViewSpArama.etYogurt4KgDM.text.toString()
                                    }

                                    var etYogurt4KgDMFiyat = "0"
                                    if (dialogViewSpArama.etYogurt4KgDMFiyat.text.toString().isNotEmpty()) {
                                        etYogurt4KgDMFiyat = dialogViewSpArama.etYogurt4KgDMFiyat.text.toString()
                                    }


                                    var etYogurt10KgDM = "0"
                                    if (dialogViewSpArama.etYogurt10KgDM.text.toString().isNotEmpty()) {
                                        etYogurt10KgDM = dialogViewSpArama.etYogurt10KgDM.text.toString()
                                    }

                                    var etYogurt10KgDMFiyat = "0"
                                    if (dialogViewSpArama.etYogurt10KgDMFiyat.text.toString().isNotEmpty()) {
                                        etYogurt10KgDMFiyat = dialogViewSpArama.etYogurt10KgDMFiyat.text.toString()
                                    }


                                    var etYogurt10KgV = "0"
                                    if (dialogViewSpArama.etYogurt10KgV.text.toString().isNotEmpty()) {
                                        etYogurt10KgV = dialogViewSpArama.etYogurt10KgV.text.toString()
                                    }

                                    var etYogurt10KgVFiyat = "0"
                                    if (dialogViewSpArama.etYogurt10KgVFiyat.text.toString().isNotEmpty()) {
                                        etYogurt10KgVFiyat = dialogViewSpArama.etYogurt10KgVFiyat.text.toString()
                                    }


                                    var etYogurt10Ozel = "0"
                                    if (dialogViewSpArama.etYogurt10Ozel.text.toString().isNotEmpty()) {
                                        etYogurt10Ozel = dialogViewSpArama.etYogurt10Ozel.text.toString()
                                    }

                                    var etYogurt10OzelFiyat = "0"
                                    if (dialogViewSpArama.etYogurt10OzelFiyat.text.toString().isNotEmpty()) {
                                        etYogurt10OzelFiyat = dialogViewSpArama.etYogurt10OzelFiyat.text.toString()
                                    }


                                    var etYogurt900Suzme = "0"
                                    if (dialogViewSpArama.etYogurt900Suzme.text.toString().isNotEmpty()) {
                                        etYogurt900Suzme = dialogViewSpArama.etYogurt900Suzme.text.toString()
                                    }

                                    var etYogurt900SuzmeFiyat = "0"
                                    if (dialogViewSpArama.etYogurt900SuzmeFiyat.text.toString().isNotEmpty()) {
                                        etYogurt900SuzmeFiyat = dialogViewSpArama.etYogurt900SuzmeFiyat.text.toString()
                                    }


                                    var etYogurt5KgSuzme = "0"
                                    if (dialogViewSpArama.etYogurt5KgSuzme.text.toString().isNotEmpty()) {
                                        etYogurt5KgSuzme = dialogViewSpArama.etYogurt5KgSuzme.text.toString()
                                    }

                                    var etYogurt5KgSuzmeFiyat = "0"
                                    if (dialogViewSpArama.etYogurt5KgSuzmeFiyat.text.toString().isNotEmpty()) {
                                        etYogurt5KgSuzmeFiyat = dialogViewSpArama.etYogurt5KgSuzmeFiyat.text.toString()
                                    }


                                    var etAyran1Lt = "0"
                                    if (dialogViewSpArama.etAyran1Lt.text.toString().isNotEmpty()) {
                                        etAyran1Lt = dialogViewSpArama.etAyran1Lt.text.toString()
                                    }

                                    var etAyran1LtFiyat = "0"
                                    if (dialogViewSpArama.etAyran1LtFiyat.text.toString().isNotEmpty()) {
                                        etAyran1LtFiyat = dialogViewSpArama.etAyran1LtFiyat.text.toString()
                                    }

                                    var etAyran290YY = "0"
                                    if (dialogViewSpArama.etAyran290YY.text.toString().isNotEmpty()) {
                                        etAyran290YY = dialogViewSpArama.etAyran290YY.text.toString()
                                    }

                                    var etAyran290YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran290YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran290YYFiyat = dialogViewSpArama.etAyran290YYFiyat.text.toString()
                                    }


                                    var etAyran220YY = "0"
                                    if (dialogViewSpArama.etAyran220YY.text.toString().isNotEmpty()) {
                                        etAyran220YY = dialogViewSpArama.etAyran220YY.text.toString()
                                    }

                                    var etAyran220YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran220YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran220YYFiyat = dialogViewSpArama.etAyran220YYFiyat.text.toString()
                                    }


                                    var etAyran200YY = "0"
                                    if (dialogViewSpArama.etAyran200YY.text.toString().isNotEmpty()) {
                                        etAyran200YY = dialogViewSpArama.etAyran200YY.text.toString()
                                    }

                                    var etAyran200YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran200YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran200YYFiyat = dialogViewSpArama.etAyran200YYFiyat.text.toString()
                                    }

                                    var etAyran180YY = "0"
                                    if (dialogViewSpArama.etAyran180YY.text.toString().isNotEmpty()) {
                                        etAyran180YY = dialogViewSpArama.etAyran180YY.text.toString()
                                    }

                                    var etAyran180YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran180YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran180YYFiyat = dialogViewSpArama.etAyran180YYFiyat.text.toString()
                                    }

                                    var etAyran170YY = "0"
                                    if (dialogViewSpArama.etAyran170YY.text.toString().isNotEmpty()) {
                                        etAyran170YY = dialogViewSpArama.etAyran170YY.text.toString()
                                    }

                                    var etAyran170YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran170YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran170YYFiyat = dialogViewSpArama.etAyran170YYFiyat.text.toString()
                                    }


                                    var etAyran150YY = "0"
                                    if (dialogViewSpArama.etAyran150YY.text.toString().isNotEmpty()) {
                                        etAyran150YY = dialogViewSpArama.etAyran150YY.text.toString()
                                    }

                                    var etAyran150YYFiyat = "0"
                                    if (dialogViewSpArama.etAyran150YYFiyat.text.toString().isNotEmpty()) {
                                        etAyran150YYFiyat = dialogViewSpArama.etAyran150YYFiyat.text.toString()
                                    }


                                    var etTereyag500 = "0"
                                    if (dialogViewSpArama.etTereyag500.text.toString().isNotEmpty()) {
                                        etTereyag500 = dialogViewSpArama.etTereyag500.text.toString()
                                    }

                                    var etTereyag500Fiyat = "0"
                                    if (dialogViewSpArama.etTereyag500Fiyat.text.toString().isNotEmpty()) {
                                        etTereyag500Fiyat = dialogViewSpArama.etTereyag500Fiyat.text.toString()
                                    }


                                    var etTereyag1Kg = "0"
                                    if (dialogViewSpArama.etTereyag1Kg.text.toString().isNotEmpty()) {
                                        etTereyag1Kg = dialogViewSpArama.etTereyag1Kg.text.toString()
                                    }

                                    var etTereyag1KgFiyat = "0"
                                    if (dialogViewSpArama.etTereyag1KgFiyat.text.toString().isNotEmpty()) {
                                        etTereyag1KgFiyat = dialogViewSpArama.etTereyag1KgFiyat.text.toString()
                                    }

                                    var etTereyag1Kgkoy = "0"
                                    if (dialogViewSpArama.etTereyag1Kgkoy.text.toString().isNotEmpty()) {
                                        etTereyag1Kgkoy = dialogViewSpArama.etTereyag1Kgkoy.text.toString()
                                    }

                                    var etTereyag1KgkoyFiyat = "0"
                                    if (dialogViewSpArama.etTereyag1KgkoyFiyat.text.toString().isNotEmpty()) {
                                        etTereyag1KgkoyFiyat = dialogViewSpArama.etTereyag1KgkoyFiyat.text.toString()
                                    }


                                    var etTereyag1800 = "0"
                                    if (dialogViewSpArama.etTereyag1800.text.toString().isNotEmpty()) {
                                        etTereyag1800 = dialogViewSpArama.etTereyag1800.text.toString()
                                    }

                                    var etTereyag1800Fiyat = "0"
                                    if (dialogViewSpArama.etTereyag1800Fiyat.text.toString().isNotEmpty()) {
                                        etTereyag1800Fiyat = dialogViewSpArama.etTereyag1800Fiyat.text.toString()
                                    }

                                    var etKaymak200 = "0"
                                    if (dialogViewSpArama.etKaymak200.text.toString().isNotEmpty()) {
                                        etKaymak200 = dialogViewSpArama.etKaymak200.text.toString()
                                    }

                                    var etKaymak200Fiyat = "0"
                                    if (dialogViewSpArama.etKaymak200Fiyat.text.toString().isNotEmpty()) {
                                        etKaymak200Fiyat = dialogViewSpArama.etKaymak200Fiyat.text.toString()
                                    }




                                    var siparisNotu = dialogViewSpArama.etSiparisNotu.text.toString()
                                    var siparisKey = ref.child("Siparisler").push().key.toString()
                                    var siparisData = SiparisData(
                                        musteriData.musteri_ad_soyad.toString(), kullaniciAdi.toString(), null, null, cal.timeInMillis, musteriData.musteri_adres,
                                        siparisNotu, musteriData.musteri_mah, siparisKey, musteriData.musteri_apartman.toString(), musteriData.musteri_tel.toString(),
                                        false, null, null, sut3lt,
                                        sut3ltFiyat,
                                        sut5lt,
                                        sut5ltFiyat,
                                        bp500,
                                        bp500Fiyat,
                                        bp800,
                                        bp800Fiyat,
                                        bp5kg,
                                        bp5kgFiyat,
                                        bp17kg,
                                        bp17kgFiyat,
                                        kasar400,
                                        kasar400Fiyat,
                                        kasar700,
                                        kasar700Fiyat,
                                        kasar1kg,
                                        kasar1kgFiyat,
                                        kasar2kgPide,
                                        kasar2kgPideFiyat,
                                        kasar2kgTost,
                                        kasar2kgTostFiyat,
                                        etKoy5kg,
                                        etKoy5kgFiyat,
                                        etKoy15kg,
                                        etKoy15kgFiyat,
                                        etTulum250,
                                        etTulum250Fiyat,
                                        etCerkez,
                                        etCerkezFiyat,
                                        et9kgLor,
                                        et9kgLorFiyat,
                                        etCokelek,
                                        etCokelekFiyat,
                                        etYogurt200,
                                        etYogurt200Fiyat,
                                        etYogurt500,
                                        etYogurt500Fiyat,
                                        etYogurt1250,
                                        etYogurt1250Fiyat,
                                        etYogurtTam2Kg,
                                        etYogurtTam2KgFiyat,
                                        etYogurtTam9Kg,
                                        etYogurtTam9KgFiyat,
                                        etYogurt200V,
                                        etYogurt200VFiyat,
                                        etYogurt10KgV,
                                        etYogurt10KgVFiyat,
                                        etYogurt2KgDM,
                                        etYogurt2KgDMFiyat,
                                        etYogurt3KgDM,
                                        etYogurt3KgDMFiyat,
                                        etYogurt4KgDM,
                                        etYogurt4KgDMFiyat,
                                        etYogurt10KgDM,
                                        etYogurt10KgDMFiyat,
                                        etYogurt10Ozel,
                                        etYogurt10OzelFiyat,
                                        etYogurt900Suzme,
                                        etYogurt900SuzmeFiyat,
                                        etYogurt5KgSuzme,
                                        etYogurt5KgSuzmeFiyat,
                                        etYogurt2750KgTava,
                                        etYogurt2750KgTavaFiyat,
                                        etAyran1Lt,
                                        etAyran1LtFiyat,
                                        etAyran290YY,
                                        etAyran290YYFiyat,
                                        etAyran220YY,
                                        etAyran220YYFiyat,
                                        etAyran200YY,
                                        etAyran200YYFiyat,
                                        etAyran180YY,
                                        etAyran180YYFiyat,
                                        etAyran170YY,
                                        etAyran170YYFiyat,
                                        etAyran150YY,
                                        etAyran150YYFiyat,
                                        etTereyag500,
                                        etTereyag500Fiyat,
                                        etTereyag1Kg,
                                        etTereyag1KgFiyat,
                                        etTereyag1800,
                                        etTereyag1800Fiyat,
                                        etTereyag1Kgkoy,
                                        etTereyag1KgkoyFiyat,
                                        etKaymak200,
                                        etKaymak200Fiyat
                                    )

                                    ref.child("Siparisler").child(musteriData.musteri_mah.toString()).child(siparisKey).setValue(siparisData)
                                    ref.child("Siparisler").child(musteriData.musteri_mah.toString()).child(siparisKey).child("siparis_zamani").setValue(ServerValue.TIMESTAMP)
                                    ref.child("Siparisler").child(musteriData.musteri_mah.toString()).child(siparisKey).child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)

                                    ref.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).setValue(siparisData)
                                    ref.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)
                                    ref.child("Musteriler").child(musteriData.musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).child("siparis_zamani").setValue(ServerValue.TIMESTAMP)


                                }
                            })

                            builder.setTitle(musteriData.musteri_ad_soyad)
                            builder.setIcon(R.drawable.cow)

                            builder.setView(dialogViewSpArama)
                            var dialog: Dialog = builder.create()
                            dialog.show()

                        }


                    }

                })


                //  Log.e("ass2","true")
            } else {
                Toast.makeText(this, "Böyle Bir Müşteri Yok", Toast.LENGTH_SHORT).show()
                //  Log.e("ass2", "Böyle Bir Müşteri Yok")
            }


        }
    }

    private fun setupSpinner() {
        var siraList = ArrayList<String>()
        siraList.add("İsme A -> Z")
        siraList.add("İsme Z -> A")
        siraList.add("Zamana")
        siraList.add("Zamana ters")



        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, siraList)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var secilenMarka = siraList[position]
                if (secilenMarka == "İsme A -> Z") {
                    musteriList.sortBy { it.musteri_ad_soyad }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)
                } else if (secilenMarka == "İsme Z -> A") {
                    musteriList.sortByDescending { it.musteri_ad_soyad }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)

                } else if (secilenMarka == "Zamana") {
                    musteriList.sortByDescending { it.siparis_son_zaman }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)
                } else if (secilenMarka == "Zamana ters") {
                    musteriList.sortBy { it.siparis_son_zaman }
                    rcMusteri.layoutManager = LinearLayoutManager(this@MusterilerActivity, LinearLayoutManager.VERTICAL, false)
                    val Adapter = MusteriAdapter(this@MusterilerActivity, musteriList, kullaniciAdi)
                    rcMusteri.adapter = Adapter
                    rcMusteri.setHasFixedSize(true)
                }
            }
        }
    }

    private fun setupRecyclerViewMusteriler() {
        rcMusteri.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val Adapter = MusteriAdapter(this, musteriList, kullaniciAdi)
        rcMusteri.adapter = Adapter
        rcMusteri.setHasFixedSize(true)
    }

    fun setupKullaniciAdi() {
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.child("user_name").value.toString()?.let {
                    kullaniciAdi = it
                }
                progressDialog = ProgressDialog(this@MusterilerActivity)
                progressDialog.setMessage("Müşteriler Yükleniyor.")
                progressDialog.setCancelable(false)
                progressDialog.show()

                hndler.postDelayed({ setupVeri() }, 500)
                hndler.postDelayed({ progressDialog.dismiss() }, 5000)

            }
        })
    }


    var watcherAdres = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length >= 0) {

                dialogView.tvAdresTam.text = secilenMah + " mahallesi " + s.toString()

            } else {

                dialogView.tvAdresTam.text = "Sadece Sokak ve No Girilecek  Örnek: Topuz Siteleri Sokak No 5 "
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
