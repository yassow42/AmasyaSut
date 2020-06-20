package com.example.amasyasut.Adapter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amasyasut.Activity.AdresBulmaMapsActivity
import com.example.amasyasut.Datalar.MusteriData
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.R
import com.example.amasyasut.TimeAgo
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dialog_gidilen_musteri.view.*
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import kotlinx.android.synthetic.main.item_musteri.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MusteriAdapter(val myContext: Context, val musteriler: ArrayList<MusteriData>, val kullaniciAdi: String?) : RecyclerView.Adapter<MusteriAdapter.MusteriHolder>() {

    lateinit var dialogViewSp: View
    lateinit var dialogMsDznle: Dialog


    var genelFiyat = 0
    var ref = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusteriAdapter.MusteriHolder {
        val myView = LayoutInflater.from(myContext).inflate(R.layout.item_musteri, parent, false)

        return MusteriHolder(myView)
    }

    override fun getItemCount(): Int {
        return musteriler.size
    }

    override fun onBindViewHolder(holder: MusteriAdapter.MusteriHolder, position: Int) {
        var item = musteriler[position]
        var musteri_ad_soyad = musteriler[position].musteri_ad_soyad.toString()
        try {
            holder.setData(musteriler[position])
            holder.btnSiparisEkle.setOnClickListener {
                var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                dialogViewSp = inflate(myContext, R.layout.dialog_siparis_ekle, null)

                dialogViewSp.tvZamanEkleDialog.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(System.currentTimeMillis())
                var cal = Calendar.getInstance()
                val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale("tr"))
                    dialogViewSp.tvZamanEkleDialog.text = sdf.format(cal.time)
                }

                val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                }


                dialogViewSp.tvZamanEkleDialog.setOnClickListener {
                    DatePickerDialog(myContext, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    TimePickerDialog(myContext, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

                dialogViewButton(dialogViewSp)


                builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.dismiss()
                    }

                })
                builder.setPositiveButton("Sipariş Ekle", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                        var sut3lt = "0"
                        if (dialogViewSp.et3lt.text.toString().isNotEmpty()) {
                            sut3lt = dialogViewSp.et3lt.text.toString()
                        }
                        var sut3ltFiyat = "0"
                        if (dialogViewSp.et3ltFiyat.text.toString().isNotEmpty()) {
                            sut3ltFiyat = dialogViewSp.et3ltFiyat.text.toString()
                        }

                        var sut5lt = "0"
                        if (dialogViewSp.et5lt.text.toString().isNotEmpty()) {
                            sut5lt = dialogViewSp.et5lt.text.toString()
                        }

                        var sut5ltFiyat = "0"
                        if (dialogViewSp.et5ltFiyat.text.toString().isNotEmpty()) {
                            sut5ltFiyat = dialogViewSp.et5ltFiyat.text.toString()
                        }

                        var bp500 = "0"
                        if (dialogViewSp.etBp500.text.toString().isNotEmpty()) {
                            bp500 = dialogViewSp.etBp500.text.toString()
                        }

                        var bp500Fiyat = "0"
                        if (dialogViewSp.etBp500Fiyat.text.toString().isNotEmpty()) {
                            bp500Fiyat = dialogViewSp.etBp500Fiyat.text.toString()
                        }
                        var bp800 = "0"
                        if (dialogViewSp.etBp800.text.toString().isNotEmpty()) {
                            bp800 = dialogViewSp.etBp800.text.toString()
                        }

                        var bp800Fiyat = "0"
                        if (dialogViewSp.etBp800Fiyat.text.toString().isNotEmpty()) {
                            bp800Fiyat = dialogViewSp.etBp800Fiyat.text.toString()
                        }
                        var bp5kg = "0"
                        if (dialogViewSp.etBp5kg.text.toString().isNotEmpty()) {
                            bp5kg = dialogViewSp.etBp5kg.text.toString()
                        }

                        var bp5kgFiyat = "0"
                        if (dialogViewSp.etBp5kgFiyat.text.toString().isNotEmpty()) {
                            bp5kgFiyat = dialogViewSp.etBp5kgFiyat.text.toString()
                        }

                        var bp17kg = "0"
                        if (dialogViewSp.etBp17kg.text.toString().isNotEmpty()) {
                            bp17kg = dialogViewSp.etBp17kg.text.toString()
                        }
                        var bp17kgFiyat = "0"
                        if (dialogViewSp.etBp17kgFiyat.text.toString().isNotEmpty()) {
                            bp17kgFiyat = dialogViewSp.etBp17kgFiyat.text.toString()
                        }

                        var kasar400 = "0"
                        if (dialogViewSp.etKasar400.text.toString().isNotEmpty()) {
                            kasar400 = dialogViewSp.etKasar400.text.toString()
                        }

                        var kasar400Fiyat = "0"
                        if (dialogViewSp.etKasar400Fiyat.text.toString().isNotEmpty()) {
                            kasar400Fiyat = dialogViewSp.etKasar400Fiyat.text.toString()
                        }


                        var kasar700 = "0"
                        if (dialogViewSp.etKasar700.text.toString().isNotEmpty()) {
                            kasar700 = dialogViewSp.etKasar700.text.toString()
                        }

                        var kasar700Fiyat = "0"
                        if (dialogViewSp.etKasar700Fiyat.text.toString().isNotEmpty()) {
                            kasar700Fiyat = dialogViewSp.etKasar700Fiyat.text.toString()
                        }


                        var kasar1kg = "0"
                        if (dialogViewSp.etKasar1kg.text.toString().isNotEmpty()) {
                            kasar1kg = dialogViewSp.etKasar1kg.text.toString()
                        }

                        var kasar1kgFiyat = "0"
                        if (dialogViewSp.etKasar1kgFiyat.text.toString().isNotEmpty()) {
                            kasar1kgFiyat = dialogViewSp.etKasar1kgFiyat.text.toString()
                        }

                        var kasar2kgPide = "0"
                        if (dialogViewSp.etKasar2kgPide.text.toString().isNotEmpty()) {
                            kasar2kgPide = dialogViewSp.etKasar2kgPide.text.toString()
                        }

                        var kasar2kgPideFiyat = "0"
                        if (dialogViewSp.etKasar2kgPideFiyat.text.toString().isNotEmpty()) {
                            kasar2kgPideFiyat = dialogViewSp.etKasar2kgPideFiyat.text.toString()
                        }

                        var kasar2kgTost = "0"
                        if (dialogViewSp.etKasar2kgTost.text.toString().isNotEmpty()) {
                            kasar2kgTost = dialogViewSp.etKasar2kgTost.text.toString()
                        }

                        var kasar2kgTostFiyat = "0"
                        if (dialogViewSp.etKasar2kgTostFiyat.text.toString().isNotEmpty()) {
                            kasar2kgTostFiyat = dialogViewSp.etKasar2kgTostFiyat.text.toString()
                        }

                        var etKoy5kg = "0"
                        if (dialogViewSp.etKoy5kg.text.toString().isNotEmpty()) {
                            etKoy5kg = dialogViewSp.etKoy5kg.text.toString()
                        }

                        var etKoy5kgFiyat = "0"
                        if (dialogViewSp.etKoy5kgFiyat.text.toString().isNotEmpty()) {
                            etKoy5kgFiyat = dialogViewSp.etKoy5kgFiyat.text.toString()
                        }
                        var etKoy15kg = "0"
                        if (dialogViewSp.etKoy15kg.text.toString().isNotEmpty()) {
                            etKoy15kg = dialogViewSp.etKoy15kg.text.toString()
                        }

                        var etKoy15kgFiyat = "0"
                        if (dialogViewSp.etKoy15kgFiyat.text.toString().isNotEmpty()) {
                            etKoy15kgFiyat = dialogViewSp.etKoy15kgFiyat.text.toString()
                        }

                        var etTulum250 = "0"
                        if (dialogViewSp.etTulum250.text.toString().isNotEmpty()) {
                            etTulum250 = dialogViewSp.etTulum250.text.toString()
                        }

                        var etTulum250Fiyat = "0"
                        if (dialogViewSp.etTulum250Fiyat.text.toString().isNotEmpty()) {
                            etTulum250Fiyat = dialogViewSp.etTulum250Fiyat.text.toString()
                        }

                        var etCerkez = "0"
                        if (dialogViewSp.etCerkez.text.toString().isNotEmpty()) {
                            etCerkez = dialogViewSp.etCerkez.text.toString()
                        }

                        var etCerkezFiyat = "0"
                        if (dialogViewSp.etCerkezFiyat.text.toString().isNotEmpty()) {
                            etCerkezFiyat = dialogViewSp.etCerkezFiyat.text.toString()
                        }

                        var et9kgLor = "0"
                        if (dialogViewSp.et9kgLor.text.toString().isNotEmpty()) {
                            et9kgLor = dialogViewSp.et9kgLor.text.toString()
                        }

                        var et9kgLorFiyat = "0"
                        if (dialogViewSp.et9kgLorFiyat.text.toString().isNotEmpty()) {
                            et9kgLorFiyat = dialogViewSp.et9kgLorFiyat.text.toString()
                        }

                        var etCokelek = "0"
                        if (dialogViewSp.etCokelek.text.toString().isNotEmpty()) {
                            etCokelek = dialogViewSp.etCokelek.text.toString()
                        }

                        var etCokelekFiyat = "0"
                        if (dialogViewSp.etCokelekFiyat.text.toString().isNotEmpty()) {
                            etCokelekFiyat = dialogViewSp.etCokelekFiyat.text.toString()
                        }


                        var etYogurt200 = "0"
                        if (dialogViewSp.etYogurt200.text.toString().isNotEmpty()) {
                            etYogurt200 = dialogViewSp.etYogurt200.text.toString()
                        }

                        var etYogurt200Fiyat = "0"
                        if (dialogViewSp.etYogurt200Fiyat.text.toString().isNotEmpty()) {
                            etYogurt200Fiyat = dialogViewSp.etYogurt200Fiyat.text.toString()
                        }

                        var etYogurt500 = "0"
                        if (dialogViewSp.etYogurt500.text.toString().isNotEmpty()) {
                            etYogurt500 = dialogViewSp.etYogurt500.text.toString()
                        }

                        var etYogurt500Fiyat = "0"
                        if (dialogViewSp.etYogurt500Fiyat.text.toString().isNotEmpty()) {
                            etYogurt500Fiyat = dialogViewSp.etYogurt500Fiyat.text.toString()
                        }


                        var etYogurt200V = "0"
                        if (dialogViewSp.etYogurtYarım200V.text.toString().isNotEmpty()) {
                            etYogurt200V = dialogViewSp.etYogurtYarım200V.text.toString()
                        }

                        var etYogurt200VFiyat = "0"
                        if (dialogViewSp.etYogurt200VFiyat.text.toString().isNotEmpty()) {
                            etYogurt200VFiyat = dialogViewSp.etYogurt200VFiyat.text.toString()
                        }

                        var etYogurt1250 = "0"
                        if (dialogViewSp.etYogurt1250.text.toString().isNotEmpty()) {
                            etYogurt1250 = dialogViewSp.etYogurt1250.text.toString()
                        }

                        var etYogurt1250Fiyat = "0"
                        if (dialogViewSp.etYogurt1250Fiyat.text.toString().isNotEmpty()) {
                            etYogurt1250Fiyat = dialogViewSp.etYogurt1250Fiyat.text.toString()
                        }

                        var etYogurtTam2Kg = "0"
                        if (dialogViewSp.etYogurtTam2Kg.text.toString().isNotEmpty()) {
                            etYogurtTam2Kg = dialogViewSp.etYogurtTam2Kg.text.toString()
                        }

                        var etYogurtTam2KgFiyat = "0"
                        if (dialogViewSp.etYogurtTam2KgFiyat.text.toString().isNotEmpty()) {
                            etYogurtTam2KgFiyat = dialogViewSp.etYogurtTam2KgFiyat.text.toString()
                        }


                        var etYogurtTam9Kg = "0"
                        if (dialogViewSp.etYogurtTam2Kg.text.toString().isNotEmpty()) {
                            etYogurtTam9Kg = dialogViewSp.etYogurtTam2Kg.text.toString()
                        }

                        var etYogurtTam9KgFiyat = "0"
                        if (dialogViewSp.etYogurtTam9KgFiyat.text.toString().isNotEmpty()) {
                            etYogurtTam9KgFiyat = dialogViewSp.etYogurtTam9KgFiyat.text.toString()
                        }

                        var etYogurt2750KgTava = "0"
                        if (dialogViewSp.etYogurt2750KgTava.text.toString().isNotEmpty()) {
                            etYogurt2750KgTava = dialogViewSp.etYogurt2750KgTava.text.toString()
                        }

                        var etYogurt2750KgTavaFiyat = "0"
                        if (dialogViewSp.etYogurt2750KgTavaFiyat.text.toString().isNotEmpty()) {
                            etYogurt2750KgTavaFiyat = dialogViewSp.etYogurt2750KgTavaFiyat.text.toString()
                        }

                        var etYogurt2KgDM = "0"
                        if (dialogViewSp.etYogurt2KgDM.text.toString().isNotEmpty()) {
                            etYogurt2KgDM = dialogViewSp.etYogurt2KgDM.text.toString()
                        }

                        var etYogurt2KgDMFiyat = "0"
                        if (dialogViewSp.etYogurt2KgDMFiyat.text.toString().isNotEmpty()) {
                            etYogurt2KgDMFiyat = dialogViewSp.etYogurt2KgDMFiyat.text.toString()
                        }

                        var etYogurt3KgDM = "0"
                        if (dialogViewSp.etYogurt3KgDM.text.toString().isNotEmpty()) {
                            etYogurt3KgDM = dialogViewSp.etYogurt2KgDM.text.toString()
                        }

                        var etYogurt3KgDMFiyat = "0"
                        if (dialogViewSp.etYogurt3KgDMFiyat.text.toString().isNotEmpty()) {
                            etYogurt3KgDMFiyat = dialogViewSp.etYogurt3KgDMFiyat.text.toString()
                        }


                        var etYogurt4KgDM = "0"
                        if (dialogViewSp.etYogurt4KgDM.text.toString().isNotEmpty()) {
                            etYogurt4KgDM = dialogViewSp.etYogurt4KgDM.text.toString()
                        }

                        var etYogurt4KgDMFiyat = "0"
                        if (dialogViewSp.etYogurt4KgDMFiyat.text.toString().isNotEmpty()) {
                            etYogurt4KgDMFiyat = dialogViewSp.etYogurt4KgDMFiyat.text.toString()
                        }


                        var etYogurt10KgDM = "0"
                        if (dialogViewSp.etYogurt10KgDM.text.toString().isNotEmpty()) {
                            etYogurt10KgDM = dialogViewSp.etYogurt10KgDM.text.toString()
                        }

                        var etYogurt10KgDMFiyat = "0"
                        if (dialogViewSp.etYogurt10KgDMFiyat.text.toString().isNotEmpty()) {
                            etYogurt10KgDMFiyat = dialogViewSp.etYogurt10KgDMFiyat.text.toString()
                        }


                        var etYogurt10KgV = "0"
                        if (dialogViewSp.etYogurt10KgDM.text.toString().isNotEmpty()) {
                            etYogurt10KgV = dialogViewSp.etYogurt10KgDM.text.toString()
                        }

                        var etYogurt10KgVFiyat = "0"
                        if (dialogViewSp.etYogurt10KgVFiyat.text.toString().isNotEmpty()) {
                            etYogurt10KgVFiyat = dialogViewSp.etYogurt10KgVFiyat.text.toString()
                        }


                        var etYogurt10Ozel = "0"
                        if (dialogViewSp.etYogurt10Ozel.text.toString().isNotEmpty()) {
                            etYogurt10Ozel = dialogViewSp.etYogurt10Ozel.text.toString()
                        }

                        var etYogurt10OzelFiyat = "0"
                        if (dialogViewSp.etYogurt10OzelFiyat.text.toString().isNotEmpty()) {
                            etYogurt10OzelFiyat = dialogViewSp.etYogurt10OzelFiyat.text.toString()
                        }


                        var etYogurt900Suzme = "0"
                        if (dialogViewSp.etYogurt900Suzme.text.toString().isNotEmpty()) {
                            etYogurt900Suzme = dialogViewSp.etYogurt900Suzme.text.toString()
                        }

                        var etYogurt900SuzmeFiyat = "0"
                        if (dialogViewSp.etYogurt900SuzmeFiyat.text.toString().isNotEmpty()) {
                            etYogurt900SuzmeFiyat = dialogViewSp.etYogurt900SuzmeFiyat.text.toString()
                        }


                        var etYogurt5KgSuzme = "0"
                        if (dialogViewSp.etYogurt5KgSuzme.text.toString().isNotEmpty()) {
                            etYogurt5KgSuzme = dialogViewSp.etYogurt5KgSuzme.text.toString()
                        }

                        var etYogurt5KgSuzmeFiyat = "0"
                        if (dialogViewSp.etYogurt5KgSuzmeFiyat.text.toString().isNotEmpty()) {
                            etYogurt5KgSuzmeFiyat = dialogViewSp.etYogurt5KgSuzmeFiyat.text.toString()
                        }


                        var etAyran1Lt = "0"
                        if (dialogViewSp.etAyran1Lt.text.toString().isNotEmpty()) {
                            etAyran1Lt = dialogViewSp.etAyran1Lt.text.toString()
                        }

                        var etAyran1LtFiyat = "0"
                        if (dialogViewSp.etAyran1LtFiyat.text.toString().isNotEmpty()) {
                            etAyran1LtFiyat = dialogViewSp.etAyran1LtFiyat.text.toString()
                        }

                        var etAyran290YY = "0"
                        if (dialogViewSp.etAyran290YY.text.toString().isNotEmpty()) {
                            etAyran290YY = dialogViewSp.etAyran290YY.text.toString()
                        }

                        var etAyran290YYFiyat = "0"
                        if (dialogViewSp.etAyran290YYFiyat.text.toString().isNotEmpty()) {
                            etAyran290YYFiyat = dialogViewSp.etAyran290YYFiyat.text.toString()
                        }


                        var etAyran220YY = "0"
                        if (dialogViewSp.etAyran220YY.text.toString().isNotEmpty()) {
                            etAyran220YY = dialogViewSp.etAyran220YY.text.toString()
                        }

                        var etAyran220YYFiyat = "0"
                        if (dialogViewSp.etAyran220YYFiyat.text.toString().isNotEmpty()) {
                            etAyran220YYFiyat = dialogViewSp.etAyran220YYFiyat.text.toString()
                        }


                        var etAyran200YY = "0"
                        if (dialogViewSp.etAyran200YY.text.toString().isNotEmpty()) {
                            etAyran200YY = dialogViewSp.etAyran200YY.text.toString()
                        }

                        var etAyran200YYFiyat = "0"
                        if (dialogViewSp.etAyran200YYFiyat.text.toString().isNotEmpty()) {
                            etAyran200YYFiyat = dialogViewSp.etAyran200YYFiyat.text.toString()
                        }

                        var etAyran180YY = "0"
                        if (dialogViewSp.etAyran180YY.text.toString().isNotEmpty()) {
                            etAyran180YY = dialogViewSp.etAyran180YY.text.toString()
                        }

                        var etAyran180YYFiyat = "0"
                        if (dialogViewSp.etAyran180YYFiyat.text.toString().isNotEmpty()) {
                            etAyran180YYFiyat = dialogViewSp.etAyran180YYFiyat.text.toString()
                        }

                        var etAyran170YY = "0"
                        if (dialogViewSp.etAyran170YY.text.toString().isNotEmpty()) {
                            etAyran170YY = dialogViewSp.etAyran170YY.text.toString()
                        }

                        var etAyran170YYFiyat = "0"
                        if (dialogViewSp.etAyran170YYFiyat.text.toString().isNotEmpty()) {
                            etAyran170YYFiyat = dialogViewSp.etAyran170YYFiyat.text.toString()
                        }


                        var etAyran150YY = "0"
                        if (dialogViewSp.etAyran150YY.text.toString().isNotEmpty()) {
                            etAyran150YY = dialogViewSp.etAyran150YY.text.toString()
                        }

                        var etAyran150YYFiyat = "0"
                        if (dialogViewSp.etAyran150YYFiyat.text.toString().isNotEmpty()) {
                            etAyran150YYFiyat = dialogViewSp.etAyran150YYFiyat.text.toString()
                        }


                        var etTereyag500 = "0"
                        if (dialogViewSp.etTereyag500.text.toString().isNotEmpty()) {
                            etTereyag500 = dialogViewSp.etTereyag500.text.toString()
                        }

                        var etTereyag500Fiyat = "0"
                        if (dialogViewSp.etTereyag500Fiyat.text.toString().isNotEmpty()) {
                            etTereyag500Fiyat = dialogViewSp.etTereyag500Fiyat.text.toString()
                        }


                        var etTereyag1Kg = "0"
                        if (dialogViewSp.etTereyag1Kg.text.toString().isNotEmpty()) {
                            etTereyag1Kg = dialogViewSp.etTereyag1Kg.text.toString()
                        }

                        var etTereyag1KgFiyat = "0"
                        if (dialogViewSp.etTereyag1KgFiyat.text.toString().isNotEmpty()) {
                            etTereyag1KgFiyat = dialogViewSp.etTereyag1KgFiyat.text.toString()
                        }

                        var etTereyag1Kgkoy = "0"
                        if (dialogViewSp.etTereyag1Kgkoy.text.toString().isNotEmpty()) {
                            etTereyag1Kgkoy = dialogViewSp.etTereyag1Kgkoy.text.toString()
                        }

                        var etTereyag1KgkoyFiyat = "0"
                        if (dialogViewSp.etTereyag1KgkoyFiyat.text.toString().isNotEmpty()) {
                            etTereyag1KgkoyFiyat = dialogViewSp.etTereyag1KgkoyFiyat.text.toString()
                        }


                        var etTereyag1800 = "0"
                        if (dialogViewSp.etTereyag1800.text.toString().isNotEmpty()) {
                            etTereyag1800 = dialogViewSp.etTereyag1800.text.toString()
                        }

                        var etTereyag1800Fiyat = "0"
                        if (dialogViewSp.etTereyag1800Fiyat.text.toString().isNotEmpty()) {
                            etTereyag1800Fiyat = dialogViewSp.etTereyag1800Fiyat.text.toString()
                        }

                        var etKaymak200 = "0"
                        if (dialogViewSp.etKaymak200.text.toString().isNotEmpty()) {
                            etKaymak200 = dialogViewSp.etKaymak200.text.toString()
                        }

                        var etKaymak200Fiyat = "0"
                        if (dialogViewSp.etKaymak200Fiyat.text.toString().isNotEmpty()) {
                            etKaymak200Fiyat = dialogViewSp.etKaymak200Fiyat.text.toString()
                        }


                        var siparisNotu = dialogViewSp.etSiparisNotu.text.toString()
                        var siparisKey = ref.child("Siparisler").push().key.toString()
                        var siparisData = SiparisData(
                            musteri_ad_soyad,
                            kullaniciAdi.toString(),
                            null,
                            null,
                            cal.timeInMillis,
                            item.musteri_adres,
                            siparisNotu,
                            item.musteri_mah,
                            siparisKey,
                            item.musteri_apartman.toString(),
                            item.musteri_tel,
                            false,
                            null,
                            null,
                            sut3lt,
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

                        ref.child("Siparisler").child(item.musteri_mah.toString()).child(siparisKey).setValue(siparisData)
                        ref.child("Siparisler").child(item.musteri_mah.toString()).child(siparisKey).child("siparis_zamani").setValue(ServerValue.TIMESTAMP)
                        ref.child("Siparisler").child(item.musteri_mah.toString()).child(siparisKey).child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)
                        ref.child("Musteriler").child(musteriler[position].musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).setValue(siparisData)
                        ref.child("Musteriler").child(musteriler[position].musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).child("siparis_teslim_zamani")
                            .setValue(ServerValue.TIMESTAMP)
                        ref.child("Musteriler").child(musteriler[position].musteri_ad_soyad.toString()).child("siparisleri").child(siparisKey).child("siparis_zamani").setValue(ServerValue.TIMESTAMP)
                    }
                })

                builder.setTitle(musteriler[position].musteri_ad_soyad)
                builder.setIcon(R.drawable.cow)

                builder.setView(dialogViewSp)
                var dialog: Dialog = builder.create()
                dialog.show()

            }
            holder.itemView.setOnLongClickListener {

                val popup = PopupMenu(myContext, holder.itemView)
                popup.inflate(R.menu.popup_menu_musteri)
                popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popDüzenle -> {
                            var builder: AlertDialog.Builder = AlertDialog.Builder(myContext)

                            var dialogView: View = inflate(myContext, R.layout.dialog_gidilen_musteri, null)
                            builder.setView(dialogView)
                            dialogMsDznle = builder.create()

                            dialogView.imgMaps.setOnClickListener {
                                val intent = Intent(myContext, AdresBulmaMapsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                intent.putExtra("musteriAdi",musteriler[position].musteri_ad_soyad)
                                myContext.startActivity(intent)
                            }

                            dialogView.swKonumKaydet.setOnClickListener {
                                if (dialogView.swKonumKaydet.isChecked) {
                                    //  ref.child("Musteriler").child(musteri_ad_soyad).child("musteri_zkonum").setValue(true)
                                    //   holder.getLocation(musteri_ad_soyad)

                                } else {
                                    //     holder.locationManager.removeUpdates(holder.myLocationListener)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_zkonum").setValue(false)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_zlat").removeValue()
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_zlong").removeValue()

                                }

                            }

                            dialogView.imgCheck.setOnClickListener {

                                if (dialogView.etAdresGidilen.text.toString().isNotEmpty() && dialogView.etTelefonGidilen.text.toString().isNotEmpty()) {
                                    var adres = dialogView.etAdresGidilen.text.toString()
                                    var telefon = dialogView.etTelefonGidilen.text.toString()
                                    var apartman = dialogView.etApartman.text.toString()


                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_adres").setValue(adres)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_apartman").setValue(apartman)
                                    FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteri_ad_soyad).child("musteri_tel").setValue(telefon).addOnCompleteListener {
                                        ///locationsu durduruyrz
                                        //    holder.locationManager.removeUpdates(holder.myLocationListener)
                                        dialogMsDznle.dismiss()
                                        Toast.makeText(myContext, "Müşteri Bilgileri Güncellendi", Toast.LENGTH_LONG).show()
                                    }.addOnFailureListener { Toast.makeText(myContext, "Müşteri Bilgileri Güncellenemedi", Toast.LENGTH_LONG).show() }
                                } else {
                                    Toast.makeText(myContext, "Bilgilerde boşluklar var", Toast.LENGTH_LONG).show()
                                }
                            }

                            dialogView.imgBack.setOnClickListener {
                                //   holder.locationManager.removeUpdates(holder.myLocationListener)
                                dialogMsDznle.dismiss()
                            }

                            dialogView.tvAdSoyad.text = musteriler[position].musteri_ad_soyad.toString()
                            dialogView.tvMahalle.text = musteriler[position].musteri_mah.toString() + " Mahallesi"
                            dialogView.etApartman.setText(musteriler[position].musteri_apartman.toString())
                            ref.child("Musteriler").child(musteri_ad_soyad).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {}
                                override fun onDataChange(p0: DataSnapshot) {
                                    var adres = p0.child("musteri_adres").value.toString()
                                    var telefon = p0.child("musteri_tel").value.toString()
                                    var konum = p0.child("musteri_zkonum").value.toString().toBoolean()

                                    dialogView.swKonumKaydet.isChecked = konum
                                    dialogView.etAdresGidilen.setText(adres)
                                    dialogView.etTelefonGidilen.setText(telefon)

                                    var list = ArrayList<SiparisData>()
                                    list = ArrayList()
                                    if (p0.child("siparisleri").hasChildren()) {
                                        for (ds in p0.child("siparisleri").children) {
                                            var gelenData = ds.getValue(SiparisData::class.java)!!
                                            list.add(gelenData)
                                        }

                                        dialogView.rcSiparisGidilen.layoutManager = LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false)
                                        //        dialogView.rcSiparisGidilen.layoutManager = StaggeredGridLayoutManager(myContext, LinearLayoutManager.VERTICAL, 2)
                                        //     val Adapter = MusteriSiparisleriAdapter(myContext, list)
                                        //      dialogView.rcSiparisGidilen.adapter = Adapter
                                        dialogView.rcSiparisGidilen.setHasFixedSize(true)


                                    }
                                }


                            })
                            dialogMsDznle.setCancelable(false)
                            dialogMsDznle.show()

                        }
                        R.id.popSil -> {

                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Müşteriyi Sil")
                                .setMessage("Emin Misin ?")
                                .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriler[position].musteri_ad_soyad.toString()).removeValue()

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
                    return@OnMenuItemClickListener true
                })
                popup.show()

                return@setOnLongClickListener true
            }

        } catch (e: Exception) {
            Toast.makeText(myContext, "332. satır hatasıMusteriAdapter", Toast.LENGTH_LONG).show()
        }


    }

    private fun dialogViewButton(dialogViewSp: View) {
        dialogViewSp.tbYogurtlar.visibility = View.GONE
        dialogViewSp.tbDigerleri.visibility = View.GONE
        dialogViewSp.tbPeynirler.visibility = View.GONE


        dialogViewSp.asagiPeynir.setOnClickListener {
            dialogViewSp.tbPeynirler.visibility = View.VISIBLE
            dialogViewSp.yukariPeynir.visibility = View.VISIBLE
            dialogViewSp.asagiPeynir.visibility = View.GONE
        }
        dialogViewSp.yukariPeynir.setOnClickListener {
            dialogViewSp.tbPeynirler.visibility = View.GONE
            dialogViewSp.yukariPeynir.visibility = View.GONE
            dialogViewSp.asagiPeynir.visibility = View.VISIBLE

        }

        dialogViewSp.asagiYogurt.setOnClickListener {
            dialogViewSp.tbYogurtlar.visibility = View.VISIBLE
            dialogViewSp.yukariYogurt.visibility = View.VISIBLE
            dialogViewSp.asagiYogurt.visibility = View.GONE
        }
        dialogViewSp.yukariYogurt.setOnClickListener {
            dialogViewSp.tbYogurtlar.visibility = View.GONE
            dialogViewSp.yukariYogurt.visibility = View.GONE
            dialogViewSp.asagiYogurt.visibility = View.VISIBLE

        }
        dialogViewSp.asagiDiger.setOnClickListener {
            dialogViewSp.tbDigerleri.visibility = View.VISIBLE
            dialogViewSp.yukariDiger.visibility = View.VISIBLE
            dialogViewSp.asagiDiger.visibility = View.GONE
        }
        dialogViewSp.yukariDiger.setOnClickListener {
            dialogViewSp.tbDigerleri.visibility = View.GONE
            dialogViewSp.yukariDiger.visibility = View.GONE
            dialogViewSp.asagiDiger.visibility = View.VISIBLE

        }


    }

    inner class MusteriHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var musteriAdi = itemView.tvMusteriAdi
        var btnSiparisEkle = itemView.tvSiparisEkle
        var sonSiparisZamani = itemView.tvMusteriSonZaman
        var swKonumKaydet = itemView.swKonumKaydet

        var musteriAdiGnl = ""
        fun setData(musteriData: MusteriData) {
            musteriAdiGnl = musteriData.musteri_ad_soyad.toString()
            musteriAdi.text = musteriData.musteri_ad_soyad
            sonSiparisZamani.text = TimeAgo.getTimeAgo(musteriData.siparis_son_zaman!!).toString()

        }

/*
        var locationManager = myContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        @SuppressLint("MissingPermission")
        fun getLocation(musteriAdi: String) {
            if (isLocationEnabled(myContext)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 0f, myLocationListener)

            } else {
                Toast.makeText(myContext, "Konumu Açın", Toast.LENGTH_LONG).show()
                dialogMsDznle.dismiss()
            }
        }

        private fun isLocationEnabled(mContext: Context): Boolean {
            val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

        val myLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var Lat = location!!.latitude
                var Long = location!!.longitude

                FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriAdiGnl).child("musteri_zlat").setValue(Lat)
                FirebaseDatabase.getInstance().reference.child("Musteriler").child(musteriAdiGnl).child("musteri_zlong").setValue(Long).addOnCompleteListener {
                    Toast.makeText(myContext, "Konum Kaydedildi.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("Not yet implemented")
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("Not yet implemented")
            }
        }
*/

    }


}
