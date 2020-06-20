package com.example.amasyasut.Adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color.RED
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.amasyasut.Activity.SiparislerActivity
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.R
import com.example.amasyasut.TimeAgo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.dialog_siparis_ekle.view.*
import kotlinx.android.synthetic.main.item_siparisler.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MahalleSiparisleriAdapter(val myContext: Context, val siparisler: ArrayList<SiparisData>, val kullaniciAdi: String) : RecyclerView.Adapter<MahalleSiparisleriAdapter.SiparisHolder>() {
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var saticiYetki: String

    var ref = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahalleSiparisleriAdapter.SiparisHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.item_siparisler, parent, false)

        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        //Log.e("sad",userID)
        ref.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                saticiYetki = p0.child("yetki").value.toString()
            }

        })




        return SiparisHolder(view)
    }

    override fun getItemCount(): Int {

        return siparisler.size
    }

    override fun onBindViewHolder(holder: SiparisHolder, position: Int) {
        val item = siparisler[position]

        holder.setData(siparisler[position])
        holder.adetGosterim(siparisler[position])
        holder.fiyatHesaplama(siparisler[position])
        holder.telAdres(siparisler[position])
        holder.itemView.setOnLongClickListener {
            var musteriAd = siparisler[position].musteri_ad_soyad
            val popup = PopupMenu(myContext, holder.itemView)
            popup.inflate(R.menu.popup_menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when (it.itemId) {
                    R.id.popTeslim -> {
                        var alert = AlertDialog.Builder(myContext)
                            .setTitle("Sipariş Teslim Edildi")
                            .setMessage("Emin Misin ?")
                            .setPositiveButton("Onayla", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {


                                    var siparisData = SiparisData(
                                        musteriAd,
                                        kullaniciAdi,
                                        item.siparis_zamani,
                                        item.siparis_teslim_zamani,
                                        item.siparis_teslim_tarihi,
                                        item.siparis_adres,
                                        item.siparis_notu,
                                        item.siparis_mah,
                                        item.siparis_key,
                                        item.siparis_apartman,
                                        item.siparis_tel,
                                        false,
                                        null,
                                        null,
                                        item.zz_3litre,
                                        item.zz_3litreFiyat,
                                        item.zz_5litre,
                                        item.zz_5litreFiyat,
                                        item.zz_Bp500,
                                        item.zz_Bp500Fiyat,
                                        item.zz_Bp800,
                                        item.zz_Bp800Fiyat,
                                        item.zz_Bp5Kg,
                                        item.zz_Bp5KgFiyat,
                                        item.zz_Bp17Kg,
                                        item.zz_Bp17KgFiyat,
                                        item.zz_Kasar400Gr,
                                        item.zz_Kasar400GrFiyat,
                                        item.zz_Kasar700Gr,
                                        item.zz_Kasar700GrFiyat,
                                        item.zz_Kasar1KgGr,
                                        item.zz_Kasar1KgGrFiyat,
                                        item.zz_Kasar2KgPide,
                                        item.zz_Kasar2KgPideFiyat,
                                        item.zz_Kasar2KgTost,
                                        item.zz_Kasar2KgTostFiyat,
                                        item.zz_Koy5Kg,
                                        item.zz_Koy5KgFiyat,
                                        item.zz_Koy15Kg,
                                        item.zz_Koy15KgFiyat,
                                        item.zz_Tulum250,
                                        item.zz_Tulum250Fiyat,
                                        item.zz_CerkezPeynir,
                                        item.zz_CerkezPeynirFiyat,
                                        item.zz_Lor9Kg,
                                        item.zz_Lor9KgFiyat,
                                        item.zz_Cokelek,
                                        item.zz_CokelekFiyat,
                                        item.zz_YogurtTam200,
                                        item.zz_YogurtTam200Fiyat,
                                        item.zz_YogurtTam500,
                                        item.zz_YogurtTam500Fiyat,
                                        item.zz_YogurtTam1250,
                                        item.zz_YogurtTam1250Fiyat,
                                        item.zz_YogurtTam2Kg,
                                        item.zz_YogurtTam2KgFiyat,
                                        item.zz_YogurtTam9Kg,
                                        item.zz_YogurtTam9KgFiyat,
                                        item.zz_YogurtYarım200V,
                                        item.zz_YogurtYarım200VFiyat,
                                        item.zz_YogurtYarım10KgV,
                                        item.zz_YogurtYarım10KgVFiyat,
                                        item.zz_YogurtYarım2KgDM,
                                        item.zz_YogurtYarım2KgDMFiyat,
                                        item.zz_YogurtYarım3KgDM,
                                        item.zz_YogurtYarım3KgDMFiyat,
                                        item.zz_YogurtYarım4KgDM,
                                        item.zz_YogurtYarım4KgDMFiyat,
                                        item.zz_YogurtYarım10KgDM,
                                        item.zz_YogurtYarım10KgDMFiyat,
                                        item.zz_YogurtAz10KgOzel,
                                        item.zz_YogurtAz10KgOzelFiyat,
                                        item.zz_YogurtSuzme900,
                                        item.zz_YogurtSuzme900Fiyat,
                                        item.zz_YogurtSuzme5Kg,
                                        item.zz_YogurtSuzme5KgFiyat,
                                        item.zz_Yogurt2750KgTava,
                                        item.zz_Yogurt2750KgTavaFiyat,
                                        item.zz_Ayran1Lt,
                                        item.zz_Ayran1LtFiyat,
                                        item.zz_Ayran290,
                                        item.zz_Ayran290Fiyat,
                                        item.zz_Ayran220,
                                        item.zz_Ayran220Fiyat,
                                        item.zz_Ayran200,
                                        item.zz_Ayran200Fiyat,
                                        item.zz_Ayran180,
                                        item.zz_Ayran180Fiyat,
                                        item.zz_Ayran170,
                                        item.zz_Ayran170Fiyat,
                                        item.zz_Ayran150,
                                        item.zz_Ayran150Fiyat,
                                        item.zz_Tereyag500,
                                        item.zz_Tereyag500Fiyat,
                                        item.zz_Tereyag1Kg,
                                        item.zz_Tereyag1KgFiyat,
                                        item.zz_Tereyag1800,
                                        item.zz_Tereyag1800Fiyat,
                                        item.zz_Tereyag1KgKoy,
                                        item.zz_Tereyag1KgKoyFiyat,
                                        item.zz_Kaymak200,
                                        item.zz_Kaymak200Fiyat
                                    )

                                    ref.child("Musteriler").child(musteriAd.toString()).child("siparisleri").child(item.siparis_key.toString()).setValue(siparisData)
                                    ref.child("Musteriler").child(musteriAd.toString()).child("siparis_son_zaman").setValue(ServerValue.TIMESTAMP)

                                    ref.child("Teslim_siparisler").child(item.siparis_key.toString()).setValue(siparisData)
                                    ref.child("Teslim_siparisler").child(item.siparis_key.toString()).child("siparis_teslim_zamani").setValue(ServerValue.TIMESTAMP)

                                    ref.child("Siparisler").child(item.siparis_mah.toString()).child(item.siparis_key.toString()).removeValue()

                                    Toast.makeText(myContext, "Sipariş Teslim Edildi", Toast.LENGTH_LONG).show()
                                    myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                }
                            })
                            .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface?, p1: Int) {
                                    p0!!.dismiss()
                                }
                            }).create()

                        alert.show()

                    }
                    R.id.popDüzenle -> {
                        var builder: AlertDialog.Builder = AlertDialog.Builder(this.myContext)
                        var viewDuzenle: View = View.inflate(myContext, R.layout.dialog_siparis_ekle, null)

                        builder.setTitle(musteriAd)
                        builder.setIcon(R.drawable.cow)
                        viewDuzenle.et3lt.setText(siparisler[position].zz_3litre)
                        viewDuzenle.et5lt.setText(siparisler[position].zz_5litre)
                        viewDuzenle.etBp500.setText(siparisler[position].zz_Bp500)
                        viewDuzenle.etBp800.setText(siparisler[position].zz_Bp800)
                        viewDuzenle.etBp5kg.setText(siparisler[position].zz_Bp5Kg)
                        viewDuzenle.etBp17kg.setText(siparisler[position].zz_Bp17Kg)
                        viewDuzenle.etKasar400.setText(siparisler[position].zz_Kasar400Gr)
                        viewDuzenle.etKasar700.setText(siparisler[position].zz_Kasar700Gr)
                        viewDuzenle.etKasar1kg.setText(siparisler[position].zz_Kasar1KgGr)
                        viewDuzenle.etKasar2kgPide.setText(siparisler[position].zz_Kasar2KgPide)
                        viewDuzenle.etKasar2kgTost.setText(siparisler[position].zz_Kasar2KgTost)
                        viewDuzenle.etKoy5kg.setText(siparisler[position].zz_Koy5Kg)
                        viewDuzenle.etKoy15kg.setText(siparisler[position].zz_Koy15Kg)
                        viewDuzenle.etTulum250.setText(siparisler[position].zz_Tulum250)
                        viewDuzenle.etCerkez.setText(siparisler[position].zz_CerkezPeynir)
                        viewDuzenle.et9kgLor.setText(siparisler[position].zz_Lor9Kg)
                        viewDuzenle.etCokelek.setText(siparisler[position].zz_Cokelek)
                        viewDuzenle.etYogurt200.setText(siparisler[position].zz_YogurtTam200)
                        viewDuzenle.etYogurt500.setText(siparisler[position].zz_YogurtTam500)
                        viewDuzenle.etYogurtYarım200V.setText(siparisler[position].zz_YogurtYarım200V)
                        viewDuzenle.etYogurt1250.setText(siparisler[position].zz_YogurtTam1250)
                        viewDuzenle.etYogurtTam2Kg.setText(siparisler[position].zz_YogurtTam2Kg)
                        viewDuzenle.etYogurtTam9Kg.setText(siparisler[position].zz_YogurtTam9Kg)
                        viewDuzenle.etYogurt2750KgTava.setText(siparisler[position].zz_Yogurt2750KgTava)
                        viewDuzenle.etYogurt2KgDM.setText(siparisler[position].zz_YogurtYarım2KgDM)
                        viewDuzenle.etYogurt3KgDM.setText(siparisler[position].zz_YogurtYarım3KgDM)
                        viewDuzenle.etYogurt4KgDM.setText(siparisler[position].zz_YogurtYarım4KgDM)
                        viewDuzenle.etYogurt10KgDM.setText(siparisler[position].zz_YogurtYarım10KgDM)
                        viewDuzenle.etYogurt10KgV.setText(siparisler[position].zz_YogurtYarım10KgV)
                        viewDuzenle.etYogurt10Ozel.setText(siparisler[position].zz_YogurtAz10KgOzel)
                        viewDuzenle.etYogurt900Suzme.setText(siparisler[position].zz_YogurtSuzme900)
                        viewDuzenle.etYogurt5KgSuzme.setText(siparisler[position].zz_YogurtSuzme5Kg)
                        viewDuzenle.etAyran1Lt.setText(siparisler[position].zz_Ayran1Lt)
                        viewDuzenle.etAyran290YY.setText(siparisler[position].zz_Ayran290)
                        viewDuzenle.etAyran220YY.setText(siparisler[position].zz_Ayran220)
                        viewDuzenle.etAyran200YY.setText(siparisler[position].zz_Ayran200)
                        viewDuzenle.etAyran180YY.setText(siparisler[position].zz_Ayran180)
                        viewDuzenle.etAyran170YY.setText(siparisler[position].zz_Ayran170)
                        viewDuzenle.etAyran150YY.setText(siparisler[position].zz_Ayran150)
                        viewDuzenle.etTereyag500.setText(siparisler[position].zz_Tereyag500)
                        viewDuzenle.etTereyag1Kg.setText(siparisler[position].zz_Tereyag1Kg)
                        viewDuzenle.etTereyag1Kgkoy.setText(siparisler[position].zz_Tereyag1KgKoy)
                        viewDuzenle.etTereyag1800.setText(siparisler[position].zz_Tereyag1800)
                        viewDuzenle.etKaymak200.setText(siparisler[position].zz_Kaymak200)


                        viewDuzenle.etSiparisNotu.setText(siparisler[position].siparis_notu)
                        viewDuzenle.tbPeynirler.visibility = View.GONE
                        viewDuzenle.tbYogurtlar.visibility = View.GONE
                        viewDuzenle.tbDigerleri.visibility = View.GONE
                        viewDuzenle.asagiPeynir.setOnClickListener {
                            viewDuzenle.tbPeynirler.visibility = View.VISIBLE
                            viewDuzenle.yukariPeynir.visibility = View.VISIBLE
                            viewDuzenle.asagiPeynir.visibility = View.GONE
                        }
                        viewDuzenle.yukariPeynir.setOnClickListener {
                            viewDuzenle.tbPeynirler.visibility = View.GONE
                            viewDuzenle.yukariPeynir.visibility = View.GONE
                            viewDuzenle.asagiPeynir.visibility = View.VISIBLE

                        }

                        viewDuzenle.asagiYogurt.setOnClickListener {
                            viewDuzenle.tbYogurtlar.visibility = View.VISIBLE
                            viewDuzenle.yukariYogurt.visibility = View.VISIBLE
                            viewDuzenle.asagiYogurt.visibility = View.GONE
                        }
                        viewDuzenle.yukariYogurt.setOnClickListener {
                            viewDuzenle.tbYogurtlar.visibility = View.GONE
                            viewDuzenle.yukariYogurt.visibility = View.GONE
                            viewDuzenle.asagiYogurt.visibility = View.VISIBLE

                        }
                        viewDuzenle.asagiDiger.setOnClickListener {
                            viewDuzenle.tbDigerleri.visibility = View.VISIBLE
                            viewDuzenle.yukariDiger.visibility = View.VISIBLE
                            viewDuzenle.asagiDiger.visibility = View.GONE
                        }
                        viewDuzenle.yukariDiger.setOnClickListener {
                            viewDuzenle.tbDigerleri.visibility = View.GONE
                            viewDuzenle.yukariDiger.visibility = View.GONE
                            viewDuzenle.asagiDiger.visibility = View.VISIBLE

                        }



                        viewDuzenle.tvZamanEkleDialog.text = SimpleDateFormat("HH:mm dd.MM.yyyy").format(item.siparis_teslim_tarihi)
                        var cal = Calendar.getInstance()
                        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, monthOfYear)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                            val myFormat = "HH:mm dd.MM.yyyy" // mention the format you need
                            val sdf = SimpleDateFormat(myFormat, Locale("tr"))
                            viewDuzenle.tvZamanEkleDialog.text = sdf.format(cal.time)
                        }

                        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                            cal.set(Calendar.MINUTE, minute)
                        }

                        viewDuzenle.tvZamanEkleDialog.setOnClickListener {
                            DatePickerDialog(myContext, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                            TimePickerDialog(myContext, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                        }

                        builder.setView(viewDuzenle)


                        builder.setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }

                        })
                        builder.setPositiveButton("Güncelle", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {


                                var sut3lt = "0"
                                if (viewDuzenle.et3lt.text.toString().isNotEmpty()) {
                                    sut3lt = viewDuzenle.et3lt.text.toString()
                                }
                                var sut3ltFiyat = "0"
                                if (viewDuzenle.et3ltFiyat.text.toString().isNotEmpty()) {
                                    sut3ltFiyat = viewDuzenle.et3ltFiyat.text.toString()
                                }

                                var sut5lt = "0"
                                if (viewDuzenle.et5lt.text.toString().isNotEmpty()) {
                                    sut5lt = viewDuzenle.et5lt.text.toString()
                                }

                                var sut5ltFiyat = "0"
                                if (viewDuzenle.et5ltFiyat.text.toString().isNotEmpty()) {
                                    sut5ltFiyat = viewDuzenle.et5ltFiyat.text.toString()
                                }

                                var bp500 = "0"
                                if (viewDuzenle.etBp500.text.toString().isNotEmpty()) {
                                    bp500 = viewDuzenle.etBp500.text.toString()
                                }

                                var bp500Fiyat = "0"
                                if (viewDuzenle.etBp500Fiyat.text.toString().isNotEmpty()) {
                                    bp500Fiyat = viewDuzenle.etBp500Fiyat.text.toString()
                                }
                                var bp800 = "0"
                                if (viewDuzenle.etBp800.text.toString().isNotEmpty()) {
                                    bp800 = viewDuzenle.etBp800.text.toString()
                                }

                                var bp800Fiyat = "0"
                                if (viewDuzenle.etBp800Fiyat.text.toString().isNotEmpty()) {
                                    bp800Fiyat = viewDuzenle.etBp800Fiyat.text.toString()
                                }
                                var bp5kg = "0"
                                if (viewDuzenle.etBp5kg.text.toString().isNotEmpty()) {
                                    bp5kg = viewDuzenle.etBp5kg.text.toString()
                                }

                                var bp5kgFiyat = "0"
                                if (viewDuzenle.etBp5kgFiyat.text.toString().isNotEmpty()) {
                                    bp5kgFiyat = viewDuzenle.etBp5kgFiyat.text.toString()
                                }

                                var bp17kg = "0"
                                if (viewDuzenle.etBp17kg.text.toString().isNotEmpty()) {
                                    bp17kg = viewDuzenle.etBp17kg.text.toString()
                                }
                                var bp17kgFiyat = "0"
                                if (viewDuzenle.etBp17kgFiyat.text.toString().isNotEmpty()) {
                                    bp17kgFiyat = viewDuzenle.etBp17kgFiyat.text.toString()
                                }

                                var kasar400 = "0"
                                if (viewDuzenle.etKasar400.text.toString().isNotEmpty()) {
                                    kasar400 = viewDuzenle.etKasar400.text.toString()
                                }

                                var kasar400Fiyat = "0"
                                if (viewDuzenle.etKasar400Fiyat.text.toString().isNotEmpty()) {
                                    kasar400Fiyat = viewDuzenle.etKasar400Fiyat.text.toString()
                                }


                                var kasar700 = "0"
                                if (viewDuzenle.etKasar700.text.toString().isNotEmpty()) {
                                    kasar700 = viewDuzenle.etKasar700.text.toString()
                                }

                                var kasar700Fiyat = "0"
                                if (viewDuzenle.etKasar700Fiyat.text.toString().isNotEmpty()) {
                                    kasar700Fiyat = viewDuzenle.etKasar700Fiyat.text.toString()
                                }


                                var kasar1kg = "0"
                                if (viewDuzenle.etKasar1kg.text.toString().isNotEmpty()) {
                                    kasar1kg = viewDuzenle.etKasar1kg.text.toString()
                                }

                                var kasar1kgFiyat = "0"
                                if (viewDuzenle.etKasar1kgFiyat.text.toString().isNotEmpty()) {
                                    kasar1kgFiyat = viewDuzenle.etKasar1kgFiyat.text.toString()
                                }

                                var kasar2kgPide = "0"
                                if (viewDuzenle.etKasar2kgPide.text.toString().isNotEmpty()) {
                                    kasar2kgPide = viewDuzenle.etKasar2kgPide.text.toString()
                                }

                                var kasar2kgPideFiyat = "0"
                                if (viewDuzenle.etKasar2kgPideFiyat.text.toString().isNotEmpty()) {
                                    kasar2kgPideFiyat = viewDuzenle.etKasar2kgPideFiyat.text.toString()
                                }

                                var kasar2kgTost = "0"
                                if (viewDuzenle.etKasar2kgTost.text.toString().isNotEmpty()) {
                                    kasar2kgTost = viewDuzenle.etKasar2kgTost.text.toString()
                                }

                                var kasar2kgTostFiyat = "0"
                                if (viewDuzenle.etKasar2kgTostFiyat.text.toString().isNotEmpty()) {
                                    kasar2kgTostFiyat = viewDuzenle.etKasar2kgTostFiyat.text.toString()
                                }

                                var etKoy5kg = "0"
                                if (viewDuzenle.etKoy5kg.text.toString().isNotEmpty()) {
                                    etKoy5kg = viewDuzenle.etKoy5kg.text.toString()
                                }

                                var etKoy5kgFiyat = "0"
                                if (viewDuzenle.etKoy5kgFiyat.text.toString().isNotEmpty()) {
                                    etKoy5kgFiyat = viewDuzenle.etKoy5kgFiyat.text.toString()
                                }
                                var etKoy15kg = "0"
                                if (viewDuzenle.etKoy15kg.text.toString().isNotEmpty()) {
                                    etKoy15kg = viewDuzenle.etKoy15kg.text.toString()
                                }

                                var etKoy15kgFiyat = "0"
                                if (viewDuzenle.etKoy15kgFiyat.text.toString().isNotEmpty()) {
                                    etKoy15kgFiyat = viewDuzenle.etKoy15kgFiyat.text.toString()
                                }

                                var etTulum250 = "0"
                                if (viewDuzenle.etTulum250.text.toString().isNotEmpty()) {
                                    etTulum250 = viewDuzenle.etTulum250.text.toString()
                                }

                                var etTulum250Fiyat = "0"
                                if (viewDuzenle.etTulum250Fiyat.text.toString().isNotEmpty()) {
                                    etTulum250Fiyat = viewDuzenle.etTulum250Fiyat.text.toString()
                                }

                                var etCerkez = "0"
                                if (viewDuzenle.etCerkez.text.toString().isNotEmpty()) {
                                    etCerkez = viewDuzenle.etCerkez.text.toString()
                                }

                                var etCerkezFiyat = "0"
                                if (viewDuzenle.etCerkezFiyat.text.toString().isNotEmpty()) {
                                    etCerkezFiyat = viewDuzenle.etCerkezFiyat.text.toString()
                                }

                                var et9kgLor = "0"
                                if (viewDuzenle.et9kgLor.text.toString().isNotEmpty()) {
                                    et9kgLor = viewDuzenle.et9kgLor.text.toString()
                                }

                                var et9kgLorFiyat = "0"
                                if (viewDuzenle.et9kgLorFiyat.text.toString().isNotEmpty()) {
                                    et9kgLorFiyat = viewDuzenle.et9kgLorFiyat.text.toString()
                                }

                                var etCokelek = "0"
                                if (viewDuzenle.etCokelek.text.toString().isNotEmpty()) {
                                    etCokelek = viewDuzenle.etCokelek.text.toString()
                                }

                                var etCokelekFiyat = "0"
                                if (viewDuzenle.etCokelekFiyat.text.toString().isNotEmpty()) {
                                    etCokelekFiyat = viewDuzenle.etCokelekFiyat.text.toString()
                                }


                                var etYogurt200 = "0"
                                if (viewDuzenle.etYogurt200.text.toString().isNotEmpty()) {
                                    etYogurt200 = viewDuzenle.etYogurt200.text.toString()
                                }

                                var etYogurt200Fiyat = "0"
                                if (viewDuzenle.etYogurt200Fiyat.text.toString().isNotEmpty()) {
                                    etYogurt200Fiyat = viewDuzenle.etYogurt200Fiyat.text.toString()
                                }

                                var etYogurt500 = "0"
                                if (viewDuzenle.etYogurt500.text.toString().isNotEmpty()) {
                                    etYogurt500 = viewDuzenle.etYogurt500.text.toString()
                                }

                                var etYogurt500Fiyat = "0"
                                if (viewDuzenle.etYogurt500Fiyat.text.toString().isNotEmpty()) {
                                    etYogurt500Fiyat = viewDuzenle.etYogurt500Fiyat.text.toString()
                                }


                                var etYogurt200V = "0"
                                if (viewDuzenle.etYogurtYarım200V.text.toString().isNotEmpty()) {
                                    etYogurt200V = viewDuzenle.etYogurtYarım200V.text.toString()
                                }

                                var etYogurt200VFiyat = "0"
                                if (viewDuzenle.etYogurt200VFiyat.text.toString().isNotEmpty()) {
                                    etYogurt200VFiyat = viewDuzenle.etYogurt200VFiyat.text.toString()
                                }

                                var etYogurt1250 = "0"
                                if (viewDuzenle.etYogurt1250.text.toString().isNotEmpty()) {
                                    etYogurt1250 = viewDuzenle.etYogurt1250.text.toString()
                                }

                                var etYogurt1250Fiyat = "0"
                                if (viewDuzenle.etYogurt1250Fiyat.text.toString().isNotEmpty()) {
                                    etYogurt1250Fiyat = viewDuzenle.etYogurt1250Fiyat.text.toString()
                                }

                                var etYogurtTam2Kg = "0"
                                if (viewDuzenle.etYogurtTam2Kg.text.toString().isNotEmpty()) {
                                    etYogurtTam2Kg = viewDuzenle.etYogurtTam2Kg.text.toString()
                                }

                                var etYogurtTam2KgFiyat = "0"
                                if (viewDuzenle.etYogurtTam2KgFiyat.text.toString().isNotEmpty()) {
                                    etYogurtTam2KgFiyat = viewDuzenle.etYogurtTam2KgFiyat.text.toString()
                                }


                                var etYogurtTam9Kg = "0"
                                if (viewDuzenle.etYogurtTam9Kg.text.toString().isNotEmpty()) {
                                    etYogurtTam9Kg = viewDuzenle.etYogurtTam9Kg.text.toString()
                                }

                                var etYogurtTam9KgFiyat = "0"
                                if (viewDuzenle.etYogurtTam9KgFiyat.text.toString().isNotEmpty()) {
                                    etYogurtTam9KgFiyat = viewDuzenle.etYogurtTam9KgFiyat.text.toString()
                                }

                                var etYogurt2750KgTava = "0"
                                if (viewDuzenle.etYogurt2750KgTava.text.toString().isNotEmpty()) {
                                    etYogurt2750KgTava = viewDuzenle.etYogurt2750KgTava.text.toString()
                                }

                                var etYogurt2750KgTavaFiyat = "0"
                                if (viewDuzenle.etYogurt2750KgTavaFiyat.text.toString().isNotEmpty()) {
                                    etYogurt2750KgTavaFiyat = viewDuzenle.etYogurt2750KgTavaFiyat.text.toString()
                                }

                                var etYogurt2KgDM = "0"
                                if (viewDuzenle.etYogurt2KgDM.text.toString().isNotEmpty()) {
                                    etYogurt2KgDM = viewDuzenle.etYogurt2KgDM.text.toString()
                                }

                                var etYogurt2KgDMFiyat = "0"
                                if (viewDuzenle.etYogurt2KgDMFiyat.text.toString().isNotEmpty()) {
                                    etYogurt2KgDMFiyat = viewDuzenle.etYogurt2KgDMFiyat.text.toString()
                                }

                                var etYogurt3KgDM = "0"
                                if (viewDuzenle.etYogurt3KgDM.text.toString().isNotEmpty()) {
                                    etYogurt3KgDM = viewDuzenle.etYogurt2KgDM.text.toString()
                                }

                                var etYogurt3KgDMFiyat = "0"
                                if (viewDuzenle.etYogurt3KgDMFiyat.text.toString().isNotEmpty()) {
                                    etYogurt3KgDMFiyat = viewDuzenle.etYogurt3KgDMFiyat.text.toString()
                                }


                                var etYogurt4KgDM = "0"
                                if (viewDuzenle.etYogurt4KgDM.text.toString().isNotEmpty()) {
                                    etYogurt4KgDM = viewDuzenle.etYogurt4KgDM.text.toString()
                                }

                                var etYogurt4KgDMFiyat = "0"
                                if (viewDuzenle.etYogurt4KgDMFiyat.text.toString().isNotEmpty()) {
                                    etYogurt4KgDMFiyat = viewDuzenle.etYogurt4KgDMFiyat.text.toString()
                                }


                                var etYogurt10KgDM = "0"
                                if (viewDuzenle.etYogurt10KgDM.text.toString().isNotEmpty()) {
                                    etYogurt10KgDM = viewDuzenle.etYogurt10KgDM.text.toString()
                                }

                                var etYogurt10KgDMFiyat = "0"
                                if (viewDuzenle.etYogurt10KgDMFiyat.text.toString().isNotEmpty()) {
                                    etYogurt10KgDMFiyat = viewDuzenle.etYogurt10KgDMFiyat.text.toString()
                                }


                                var etYogurt10KgV = "0"
                                if (viewDuzenle.etYogurt10KgV.text.toString().isNotEmpty()) {
                                    etYogurt10KgV = viewDuzenle.etYogurt10KgV.text.toString()
                                }

                                var etYogurt10KgVFiyat = "0"
                                if (viewDuzenle.etYogurt10KgVFiyat.text.toString().isNotEmpty()) {
                                    etYogurt10KgVFiyat = viewDuzenle.etYogurt10KgVFiyat.text.toString()
                                }


                                var etYogurt10Ozel = "0"
                                if (viewDuzenle.etYogurt10Ozel.text.toString().isNotEmpty()) {
                                    etYogurt10Ozel = viewDuzenle.etYogurt10Ozel.text.toString()
                                }

                                var etYogurt10OzelFiyat = "0"
                                if (viewDuzenle.etYogurt10OzelFiyat.text.toString().isNotEmpty()) {
                                    etYogurt10OzelFiyat = viewDuzenle.etYogurt10OzelFiyat.text.toString()
                                }


                                var etYogurt900Suzme = "0"
                                if (viewDuzenle.etYogurt900Suzme.text.toString().isNotEmpty()) {
                                    etYogurt900Suzme = viewDuzenle.etYogurt900Suzme.text.toString()
                                }

                                var etYogurt900SuzmeFiyat = "0"
                                if (viewDuzenle.etYogurt900SuzmeFiyat.text.toString().isNotEmpty()) {
                                    etYogurt900SuzmeFiyat = viewDuzenle.etYogurt900SuzmeFiyat.text.toString()
                                }


                                var etYogurt5KgSuzme = "0"
                                if (viewDuzenle.etYogurt5KgSuzme.text.toString().isNotEmpty()) {
                                    etYogurt5KgSuzme = viewDuzenle.etYogurt5KgSuzme.text.toString()
                                }

                                var etYogurt5KgSuzmeFiyat = "0"
                                if (viewDuzenle.etYogurt5KgSuzmeFiyat.text.toString().isNotEmpty()) {
                                    etYogurt5KgSuzmeFiyat = viewDuzenle.etYogurt5KgSuzmeFiyat.text.toString()
                                }


                                var etAyran1Lt = "0"
                                if (viewDuzenle.etAyran1Lt.text.toString().isNotEmpty()) {
                                    etAyran1Lt = viewDuzenle.etAyran1Lt.text.toString()
                                }

                                var etAyran1LtFiyat = "0"
                                if (viewDuzenle.etAyran1LtFiyat.text.toString().isNotEmpty()) {
                                    etAyran1LtFiyat = viewDuzenle.etAyran1LtFiyat.text.toString()
                                }

                                var etAyran290YY = "0"
                                if (viewDuzenle.etAyran290YY.text.toString().isNotEmpty()) {
                                    etAyran290YY = viewDuzenle.etAyran290YY.text.toString()
                                }

                                var etAyran290YYFiyat = "0"
                                if (viewDuzenle.etAyran290YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran290YYFiyat = viewDuzenle.etAyran290YYFiyat.text.toString()
                                }


                                var etAyran220YY = "0"
                                if (viewDuzenle.etAyran220YY.text.toString().isNotEmpty()) {
                                    etAyran220YY = viewDuzenle.etAyran220YY.text.toString()
                                }

                                var etAyran220YYFiyat = "0"
                                if (viewDuzenle.etAyran220YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran220YYFiyat = viewDuzenle.etAyran220YYFiyat.text.toString()
                                }


                                var etAyran200YY = "0"
                                if (viewDuzenle.etAyran200YY.text.toString().isNotEmpty()) {
                                    etAyran200YY = viewDuzenle.etAyran200YY.text.toString()
                                }

                                var etAyran200YYFiyat = "0"
                                if (viewDuzenle.etAyran200YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran200YYFiyat = viewDuzenle.etAyran200YYFiyat.text.toString()
                                }

                                var etAyran180YY = "0"
                                if (viewDuzenle.etAyran180YY.text.toString().isNotEmpty()) {
                                    etAyran180YY = viewDuzenle.etAyran180YY.text.toString()
                                }

                                var etAyran180YYFiyat = "0"
                                if (viewDuzenle.etAyran180YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran180YYFiyat = viewDuzenle.etAyran180YYFiyat.text.toString()
                                }

                                var etAyran170YY = "0"
                                if (viewDuzenle.etAyran170YY.text.toString().isNotEmpty()) {
                                    etAyran170YY = viewDuzenle.etAyran170YY.text.toString()
                                }

                                var etAyran170YYFiyat = "0"
                                if (viewDuzenle.etAyran170YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran170YYFiyat = viewDuzenle.etAyran170YYFiyat.text.toString()
                                }


                                var etAyran150YY = "0"
                                if (viewDuzenle.etAyran150YY.text.toString().isNotEmpty()) {
                                    etAyran150YY = viewDuzenle.etAyran150YY.text.toString()
                                }

                                var etAyran150YYFiyat = "0"
                                if (viewDuzenle.etAyran150YYFiyat.text.toString().isNotEmpty()) {
                                    etAyran150YYFiyat = viewDuzenle.etAyran150YYFiyat.text.toString()
                                }


                                var etTereyag500 = "0"
                                if (viewDuzenle.etTereyag500.text.toString().isNotEmpty()) {
                                    etTereyag500 = viewDuzenle.etTereyag500.text.toString()
                                }

                                var etTereyag500Fiyat = "0"
                                if (viewDuzenle.etTereyag500Fiyat.text.toString().isNotEmpty()) {
                                    etTereyag500Fiyat = viewDuzenle.etTereyag500Fiyat.text.toString()
                                }


                                var etTereyag1Kg = "0"
                                if (viewDuzenle.etTereyag1Kg.text.toString().isNotEmpty()) {
                                    etTereyag1Kg = viewDuzenle.etTereyag1Kg.text.toString()
                                }

                                var etTereyag1KgFiyat = "0"
                                if (viewDuzenle.etTereyag1KgFiyat.text.toString().isNotEmpty()) {
                                    etTereyag1KgFiyat = viewDuzenle.etTereyag1KgFiyat.text.toString()
                                }

                                var etTereyag1Kgkoy = "0"
                                if (viewDuzenle.etTereyag1Kgkoy.text.toString().isNotEmpty()) {
                                    etTereyag1Kgkoy = viewDuzenle.etTereyag1Kgkoy.text.toString()
                                }

                                var etTereyag1KgkoyFiyat = "0"
                                if (viewDuzenle.etTereyag1KgkoyFiyat.text.toString().isNotEmpty()) {
                                    etTereyag1KgkoyFiyat = viewDuzenle.etTereyag1KgkoyFiyat.text.toString()
                                }


                                var etTereyag1800 = "0"
                                if (viewDuzenle.etTereyag1800.text.toString().isNotEmpty()) {
                                    etTereyag1800 = viewDuzenle.etTereyag1800.text.toString()
                                }

                                var etTereyag1800Fiyat = "0"
                                if (viewDuzenle.etTereyag1800Fiyat.text.toString().isNotEmpty()) {
                                    etTereyag1800Fiyat = viewDuzenle.etTereyag1800Fiyat.text.toString()
                                }

                                var etKaymak200 = "0"
                                if (viewDuzenle.etKaymak200.text.toString().isNotEmpty()) {
                                    etKaymak200 = viewDuzenle.etKaymak200.text.toString()
                                }

                                var etKaymak200Fiyat = "0"
                                if (viewDuzenle.etKaymak200Fiyat.text.toString().isNotEmpty()) {
                                    etKaymak200Fiyat = viewDuzenle.etKaymak200Fiyat.text.toString()
                                }


                                var ref = FirebaseDatabase.getInstance().reference
                                var not = viewDuzenle.etSiparisNotu.text.toString()
                                var siparisKey = siparisler[position].siparis_key.toString()
                                var siparisVeren = siparisler[position].musteri_ad_soyad.toString()

                                val siparisDataGuncelle = SiparisData(
                                    item.musteri_ad_soyad,
                                    item.siparisi_giren,
                                    item.siparis_zamani,
                                    item.siparis_teslim_zamani,
                                    cal.timeInMillis,
                                    item.siparis_adres,
                                    not,
                                    item.siparis_mah,
                                    item.siparis_key,
                                    item.siparis_apartman,
                                    item.siparis_tel,
                                    item.musteri_zkonum,
                                    item.musteri_zlat,
                                    item.musteri_zlong,
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
                                ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisKey).setValue(siparisDataGuncelle)

                                ref.child("Musteriler").child(siparisVeren).child("siparisleri").child(siparisKey).setValue(siparisDataGuncelle)

                                var intent = Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                myContext.startActivity(intent)
                            }
                        })

                        var dialog: Dialog = builder.create()
                        dialog.show()
                    }

                    R.id.popSil -> {

                        if (saticiYetki == "Yönetici") {

                            var alert = AlertDialog.Builder(myContext)
                                .setTitle("Siparişi Sil")
                                .setMessage("Emin Misin ?")
                                .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {

                                        ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisler[position].siparis_key.toString()).removeValue().addOnCompleteListener {
                                            Toast.makeText(myContext, "Sipariş Silindi...", Toast.LENGTH_LONG).show()
                                        }

                                        ref.child("Musteriler").child(musteriAd.toString()).child("siparisleri").child(siparisler[position].siparis_key.toString()).removeValue()
                                        myContext.startActivity(Intent(myContext, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))

                                    }
                                })
                                .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        p0!!.dismiss()
                                    }
                                }).create()

                            alert.show()

                        } else {
                            Toast.makeText(myContext, "Yetkiniz yok. Yönetici ile iletişime geçin", Toast.LENGTH_LONG).show()

                        }


                    }
                }
                return@OnMenuItemClickListener true
            })
            popup.show()

            return@setOnLongClickListener true
        }


    }

    inner class SiparisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun setData(siparisData: SiparisData) {
            siparisVeren.text = siparisData.musteri_ad_soyad.toString()
            siparisAdres.text = siparisData.siparis_mah + " mahallesi " + siparisData.siparis_adres + " " + siparisData.siparis_apartman
            siparisTel.text = siparisData.siparis_tel
            tvNot.text = siparisData.siparis_notu
            siparisGiren.text = siparisData.siparisi_giren



            if (siparisData.siparis_teslim_tarihi!!.compareTo(System.currentTimeMillis()) == 1) {
                tumLayout.setBackgroundColor(RED)
            }


            siparisData.siparis_teslim_tarihi?.let {
                tvTeslimZaman.text = formatDate(siparisData.siparis_teslim_tarihi).toString()
            }
            siparisData.siparis_zamani?.let {
                tvZaman.text = TimeAgo.getTimeAgo(siparisData.siparis_zamani.toString().toLong())
            }


        }

        fun adetGosterim(siparisData: SiparisData) {

            if (siparisData.zz_3litre.toString() == "0") {
                tb3lt.visibility = View.GONE
            } else {
                tv3ltSut.text = siparisData.zz_3litre.toString()
                tb3lt.visibility = View.VISIBLE
            }


            if (siparisData.zz_5litre.toString() == "0") {
                tb5lt.visibility = View.GONE
            } else {
                tv5ltSut.text = siparisData.zz_5litre.toString()
                tb5lt.visibility = View.VISIBLE
            }


            if (siparisData.zz_Cokelek.toString() == "0") {
                tbCokelek.visibility = View.GONE
            } else {
                tvCokelek.text = siparisData.zz_Cokelek.toString()
                tbCokelek.visibility = View.VISIBLE
            }



            if (siparisData.zz_Tulum250.toString() == "0") {
                tbTulum250.visibility = View.GONE
            } else {
                tvTulum250.text = siparisData.zz_Tulum250.toString()
                tbTulum250.visibility = View.VISIBLE
            }


            if (siparisData.zz_Koy15Kg.toString() == "0") {
                tbKoy15kg.visibility = View.GONE
            } else {
                tvKoy15kg.text = siparisData.zz_Koy15Kg.toString()
                tbKoy15kg.visibility = View.VISIBLE
            }




            if (siparisData.zz_Koy5Kg.toString() == "0") {
                tbKoy5kg.visibility = View.GONE
            } else {
                tvKoy5kg.text = siparisData.zz_Koy5Kg.toString()
                tbKoy5kg.visibility = View.VISIBLE
            }



            if (siparisData.zz_Kaymak200.toString() == "0") {
                tbKaymak200gr.visibility = View.GONE
            } else {
                tvKaymak200gr.text = siparisData.zz_Kaymak200.toString()
                tbKaymak200gr.visibility = View.VISIBLE
            }



            if (siparisData.zz_Bp17Kg.toString() == "0") {
                tbBp17Kg.visibility = View.GONE
            } else {
                tvBp17Kg.text = siparisData.zz_Bp17Kg.toString()
                tbBp17Kg.visibility = View.VISIBLE
            }


            if (siparisData.zz_CerkezPeynir.toString() == "0") {
                tbCerkezPeynir.visibility = View.GONE
            } else {
                tvCerkezPeynir.text = siparisData.zz_CerkezPeynir.toString()
                tbCerkezPeynir.visibility = View.VISIBLE
            }


            if (siparisData.zz_Bp5Kg.toString() == "0") {
                tbBp5Kg.visibility = View.GONE
            } else {
                tvBp5Kg.text = siparisData.zz_Bp5Kg.toString()
                tbBp5Kg.visibility = View.VISIBLE
            }


            if (siparisData.zz_Bp800.toString() == "0") {
                tbBp800.visibility = View.GONE
            } else {
                tvBp800.text = siparisData.zz_Bp800.toString()
                tbBp800.visibility = View.VISIBLE
            }



            if (siparisData.zz_Bp500.toString() == "0") {
                tbBp500.visibility = View.GONE
            } else {
                tvBp500.text = siparisData.zz_Bp500.toString()
                tbBp500.visibility = View.VISIBLE
            }

            if (siparisData.zz_Tereyag1800.toString() == "0") {
                tbTereyag1800.visibility = View.GONE
            } else {
                tvTereyag1800.text = siparisData.zz_Tereyag1800.toString()
                tbTereyag1800.visibility = View.VISIBLE
            }


            if (siparisData.zz_Tereyag1Kg.toString() == "0") {
                tbTereyag1Kg.visibility = View.GONE
            } else {
                tvTereyag1Kg.text = siparisData.zz_Tereyag1Kg.toString()
                tbTereyag1Kg.visibility = View.VISIBLE
            }

            if (siparisData.zz_Tereyag1KgKoy.toString() == "0") {
                tbTereyag1KgKoy.visibility = View.GONE
            } else {
                tvTereyag1KgKoy.text = siparisData.zz_Tereyag1KgKoy.toString()
                tbTereyag1KgKoy.visibility = View.VISIBLE
            }


            if (siparisData.zz_Tereyag500.toString() == "0") {
                tbTereyag500.visibility = View.GONE
            } else {
                tvTereyag500.text = siparisData.zz_Tereyag500.toString()
                tbTereyag500.visibility = View.VISIBLE
            }


            if (siparisData.zz_YogurtSuzme5Kg.toString() == "0") {
                tbYogurtSuzme5Kg.visibility = View.GONE
            } else {
                tvYogurtSuzme5Kg.text = siparisData.zz_YogurtSuzme5Kg.toString()
                tbYogurtSuzme5Kg.visibility = View.VISIBLE
            }

            if (siparisData.zz_YogurtSuzme900.toString() == "0") {
                tbYogurtSuzme900.visibility = View.GONE
            } else {
                tvYogurtSuzme900.text = siparisData.zz_YogurtSuzme900.toString()
                tbYogurtSuzme900.visibility = View.VISIBLE
            }


            if (siparisData.zz_Lor9Kg.toString() == "0") {
                tbLor9Kg.visibility = View.GONE
            } else {
                tvLor9Kg.text = siparisData.zz_Lor9Kg.toString()
                tbLor9Kg.visibility = View.VISIBLE
            }



            if (siparisData.zz_Kasar2KgTost.toString() == "0") {
                tbTostKasar2kg.visibility = View.GONE
            } else {
                tvTostKasar2kg.text = siparisData.zz_Kasar2KgTost.toString()
                tbTostKasar2kg.visibility = View.VISIBLE
            }




            if (siparisData.zz_Kasar2KgPide.toString() == "0") {
                tbPideKasar2Kg.visibility = View.GONE
            } else {
                tvPideKasar2Kg.text = siparisData.zz_Kasar2KgPide.toString()
                tbPideKasar2Kg.visibility = View.VISIBLE
            }


            if (siparisData.zz_Kasar1KgGr.toString() == "0") {
                tbKasar1KG.visibility = View.GONE
            } else {
                tvKasar1KG.text = siparisData.zz_Kasar1KgGr.toString()
                tbKasar1KG.visibility = View.VISIBLE
            }



            if (siparisData.zz_Kasar400Gr.toString() == "0") {
                tbKasar400.visibility = View.GONE
            } else {
                tvKasar400.text = siparisData.zz_Kasar400Gr.toString()
                tbKasar400.visibility = View.VISIBLE
            }


            if (siparisData.zz_Kasar700Gr.toString() == "0") {
                tbKasar700.visibility = View.GONE
            } else {
                tvKasar700.text = siparisData.zz_Kasar700Gr.toString()
                tbKasar700.visibility = View.VISIBLE
            }


            if (siparisData.zz_Ayran150.toString() == "0") {
                tbAyran150.visibility = View.GONE
            } else {
                tvAyran150.text = siparisData.zz_Ayran150.toString()
                tbAyran150.visibility = View.VISIBLE
            }


            if (siparisData.zz_Ayran170.toString() == "0") {
                tbAyran170.visibility = View.GONE
            } else {
                tvAyran170.text = siparisData.zz_Ayran170.toString()
                tbAyran170.visibility = View.VISIBLE
            }

            if (siparisData.zz_Ayran180.toString() == "0") {
                tbAyran180.visibility = View.GONE
            } else {
                tvAyran180.text = siparisData.zz_Ayran180.toString()
                tbAyran180.visibility = View.VISIBLE
            }

            if (siparisData.zz_Ayran200.toString() == "0") {
                tbAyran200.visibility = View.GONE
            } else {
                tvAyran200.text = siparisData.zz_Ayran200.toString()
                tbAyran200.visibility = View.VISIBLE
            }

            if (siparisData.zz_Ayran220.toString() == "0") {
                tbAyran220.visibility = View.GONE
            } else {
                tvAyran220.text = siparisData.zz_Ayran220.toString()
                tbAyran220.visibility = View.VISIBLE
            }



            if (siparisData.zz_Ayran290.toString() == "0") {
                tbAyran290.visibility = View.GONE
            } else {
                tvAyran290.text = siparisData.zz_Ayran290.toString()
                tbAyran290.visibility = View.VISIBLE
            }



            if (siparisData.zz_Ayran1Lt.toString() == "0") {
                tbAyran1lt.visibility = View.GONE
            } else {
                tvAyran1lt.text = siparisData.zz_Ayran1Lt.toString()
                tbAyran1lt.visibility = View.VISIBLE
            }


            if (siparisData.zz_YogurtAz10KgOzel.toString() == "0") {
                tbYogurt10KgAzyagli.visibility = View.GONE
            } else {
                tvYogurt10KgAzyagli.text = siparisData.zz_YogurtAz10KgOzel.toString()
                tbYogurt10KgAzyagli.visibility = View.VISIBLE
            }



            if (siparisData.zz_YogurtYarım10KgV.toString() == "0") {
                tbYogurt10KgV.visibility = View.GONE
            } else {
                tvYogurt10KgV.text = siparisData.zz_YogurtYarım10KgV.toString()
                tbYogurt10KgV.visibility = View.VISIBLE
            }


            if (siparisData.zz_YogurtYarım200V.toString() == "0") {
                tbYogurt200V.visibility = View.GONE
            } else {
                tvYogurt200V.text = siparisData.zz_YogurtYarım200V.toString()
                tbYogurt200V.visibility = View.VISIBLE
            }

            if (siparisData.zz_YogurtYarım10KgDM.toString() == "0") {
                tbYogurt10KgDm.visibility = View.GONE
            } else {
                tvYogurt10KgDm.text = siparisData.zz_YogurtYarım10KgDM.toString()
                tbYogurt10KgDm.visibility = View.VISIBLE
            }

            if (siparisData.zz_YogurtYarım4KgDM.toString() == "0") {
                tbYogurt4KgDm.visibility = View.GONE
            } else {
                tvYogurt4KgDm.text = siparisData.zz_YogurtYarım4KgDM.toString()
                tbYogurt4KgDm.visibility = View.VISIBLE
            }

            if (siparisData.zz_YogurtYarım3KgDM.toString() == "0") {
                tbYogurt3KgDm.visibility = View.GONE
            } else {
                tvYogurt3KgDm.text = siparisData.zz_YogurtYarım3KgDM.toString()
                tbYogurt3KgDm.visibility = View.VISIBLE
            }


            if (siparisData.zz_YogurtYarım2KgDM.toString() == "0") {
                tbYogurt2KgDm.visibility = View.GONE
            } else {
                tvYogurt2KgDm.text = siparisData.zz_YogurtYarım2KgDM.toString()
                tbYogurt2KgDm.visibility = View.VISIBLE
            }



            if (siparisData.zz_Yogurt2750KgTava.toString() == "0") {
                tbYogurt2750tava.visibility = View.GONE
            } else {
                tvYogurt2750tava.text = siparisData.zz_Yogurt2750KgTava.toString()
                tbYogurt2750tava.visibility = View.VISIBLE
            }



            if (siparisData.zz_YogurtTam9Kg.toString() == "0") {
                tbYogurtTam9Kg.visibility = View.GONE
            } else {
                tvYogurtTam9Kg.text = siparisData.zz_YogurtTam9Kg.toString()
                tbYogurtTam9Kg.visibility = View.VISIBLE
            }

            if (siparisData.zz_YogurtTam2Kg.toString() == "0") {
                tbYogurtTam2Kg.visibility = View.GONE
            } else {
                tvYogurtTam2Kg.text = siparisData.zz_YogurtTam2Kg.toString()
                tbYogurtTam2Kg.visibility = View.VISIBLE
            }



            if (siparisData.zz_YogurtTam1250.toString() == "0") {
                tbYogurttam1250.visibility = View.GONE
            } else {
                tvYogurttam1250.text = siparisData.zz_YogurtTam1250.toString()
                tbYogurttam1250.visibility = View.VISIBLE
            }


            if (siparisData.zz_YogurtTam500.toString() == "0") {
                tbYogurtTam500.visibility = View.GONE
            } else {
                tvYogurtTam500.text = siparisData.zz_YogurtTam500.toString()
                tbYogurtTam500.visibility = View.VISIBLE
            }
            if (siparisData.zz_YogurtTam200.toString() == "0") {
                tbYogurtTam200.visibility = View.GONE
            } else {
                tvYogurtTam200.text = siparisData.zz_YogurtTam200.toString()
                tbYogurtTam200.visibility = View.VISIBLE
            }


        }

        fun fiyatHesaplama(item: SiparisData) {
            try {
                var sut3ltAdet = item.zz_3litre.toString().toInt()
                var sut3ltFiyat = item.zz_3litreFiyat.toString().toDouble()
                var sut5ltAdet = item.zz_5litre.toString().toInt()
                var sut5ltFiyat = item.zz_5litreFiyat.toString().toDouble()
                var zz_Bp500 = item.zz_Bp500.toString().toInt()
                var zz_Bp500Fiyat = item.zz_Bp500Fiyat.toString().toDouble()
                var zz_Bp800 = item.zz_Bp800.toString().toInt()
                var zz_Bp800Fiyat = item.zz_Bp800Fiyat.toString().toDouble()
                var zz_Bp5Kg = item.zz_Bp5Kg.toString().toInt()
                var zz_Bp5KgFiyat = item.zz_Bp5KgFiyat.toString().toDouble()
                var zz_Bp17Kg = item.zz_Bp17Kg.toString().toInt()
                var zz_Bp17KgFiyat = item.zz_Bp17KgFiyat.toString().toDouble()
                var zz_Kasar400Gr = item.zz_Kasar400Gr.toString().toInt()
                var zz_Kasar400GrFiyat = item.zz_Kasar400GrFiyat.toString().toDouble()
                var zz_Kasar700Gr = item.zz_Kasar700Gr.toString().toInt()
                var zz_Kasar700GrFiyat = item.zz_Kasar700GrFiyat.toString().toDouble()
                var zz_Kasar1KgGr = item.zz_Kasar1KgGr.toString().toInt()
                var zz_Kasar1KgGrFiyat = item.zz_Kasar1KgGrFiyat.toString().toDouble()
                var zz_Kasar2KgPide = item.zz_Kasar2KgPide.toString().toInt()
                var zz_Kasar2KgPideFiyat = item.zz_Kasar2KgPideFiyat.toString().toDouble()
                var zz_Kasar2KgTost = item.zz_Kasar2KgTost.toString().toInt()
                var zz_Kasar2KgTostFiyat = item.zz_Kasar2KgTostFiyat.toString().toDouble()
                var zz_Koy5Kg = item.zz_Koy5Kg.toString().toInt()
                var zz_Koy5KgFiyat = item.zz_Koy5KgFiyat.toString().toDouble()
                var zz_Koy15Kg = item.zz_Koy15Kg.toString().toInt()
                var zz_Koy15KgFiyat = item.zz_Koy15KgFiyat.toString().toDouble()
                var zz_Tulum250 = item.zz_Tulum250.toString().toInt()
                var zz_Tulum250Fiyat = item.zz_Tulum250Fiyat.toString().toDouble()
                var zz_CerkezPeynir = item.zz_CerkezPeynir.toString().toInt()
                var zz_CerkezPeynirFiyat = item.zz_CerkezPeynirFiyat.toString().toDouble()
                var zz_Lor9Kg = item.zz_Lor9Kg.toString().toInt()
                var zz_Lor9KgFiyat = item.zz_Lor9KgFiyat.toString().toDouble()
                var zz_Cokelek = item.zz_Cokelek.toString().toInt()
                var zz_CokelekFiyat = item.zz_CokelekFiyat.toString().toDouble()
                var zz_YogurtTam200 = item.zz_YogurtTam200.toString().toInt()
                var zz_YogurtTam200Fiyat = item.zz_YogurtTam200Fiyat.toString().toDouble()
                var zz_YogurtTam500 = item.zz_YogurtTam500.toString().toInt()
                var zz_YogurtTam500Fiyat = item.zz_YogurtTam500Fiyat.toString().toDouble()
                var zz_YogurtTam1250 = item.zz_YogurtTam1250.toString().toInt()
                var zz_YogurtTam1250Fiyat = item.zz_YogurtTam1250Fiyat.toString().toDouble()
                var zz_YogurtTam2Kg = item.zz_YogurtTam2Kg.toString().toInt()
                var zz_YogurtTam2KgFiyat = item.zz_YogurtTam2KgFiyat.toString().toDouble()
                var zz_YogurtTam9Kg = item.zz_YogurtTam9Kg.toString().toInt()
                var zz_YogurtTam9KgFiyat = item.zz_YogurtTam9KgFiyat.toString().toDouble()
                var zz_YogurtYarım200V = item.zz_YogurtYarım200V.toString().toInt()
                var zz_YogurtYarım200VFiyat = item.zz_YogurtYarım200VFiyat.toString().toDouble()
                var zz_YogurtYarım10KgV = item.zz_YogurtYarım10KgV.toString().toInt()
                var zz_YogurtYarım10KgVFiyat = item.zz_YogurtYarım10KgVFiyat.toString().toDouble()
                var zz_YogurtYarım2KgDM = item.zz_YogurtYarım2KgDM.toString().toInt()
                var zz_YogurtYarım2KgDMFiyat = item.zz_YogurtYarım2KgDMFiyat.toString().toDouble()
                var zz_YogurtYarım3KgDM = item.zz_YogurtYarım3KgDM.toString().toInt()
                var zz_YogurtYarım3KgDMFiyat = item.zz_YogurtYarım3KgDMFiyat.toString().toDouble()
                var zz_YogurtYarım4KgDM = item.zz_YogurtYarım4KgDM.toString().toInt()
                var zz_YogurtYarım4KgDMFiyat = item.zz_YogurtYarım4KgDMFiyat.toString().toDouble()
                var zz_YogurtYarım10KgDM = item.zz_YogurtYarım10KgDM.toString().toInt()
                var zz_YogurtYarım10KgDMFiyat = item.zz_YogurtYarım10KgDMFiyat.toString().toDouble()
                var zz_YogurtAz10KgOzel = item.zz_YogurtAz10KgOzel.toString().toInt()
                var zz_YogurtAz10KgOzelFiyat = item.zz_YogurtAz10KgOzelFiyat.toString().toDouble()
                var zz_YogurtSuzme900 = item.zz_YogurtSuzme900.toString().toInt()
                var zz_YogurtSuzme900Fiyat = item.zz_YogurtSuzme900Fiyat.toString().toDouble()
                var zz_YogurtSuzme5Kg = item.zz_YogurtSuzme5Kg.toString().toInt()
                var zz_YogurtSuzme5KgFiyat = item.zz_YogurtSuzme5KgFiyat.toString().toDouble()
                var zz_Yogurt2750KgTava = item.zz_Yogurt2750KgTava.toString().toInt()
                var zz_Yogurt2750KgTavaFiyat = item.zz_Yogurt2750KgTavaFiyat.toString().toDouble()
                var zz_Ayran1Lt = item.zz_Ayran1Lt.toString().toInt()
                var zz_Ayran1LtFiyat = item.zz_Ayran1LtFiyat.toString().toDouble()
                var zz_Ayran290 = item.zz_Ayran290.toString().toInt()
                var zz_Ayran290Fiyat = item.zz_Ayran290Fiyat.toString().toDouble()
                var zz_Ayran220 = item.zz_Ayran220.toString().toInt()
                var zz_Ayran220Fiyat = item.zz_Ayran220Fiyat.toString().toDouble()
                var zz_Ayran200 = item.zz_Ayran200.toString().toInt()
                var zz_Ayran200Fiyat = item.zz_Ayran200Fiyat.toString().toDouble()
                var zz_Ayran180 = item.zz_Ayran180.toString().toInt()
                var zz_Ayran180Fiyat = item.zz_Ayran180Fiyat.toString().toDouble()
                var zz_Ayran170 = item.zz_Ayran170.toString().toInt()
                var zz_Ayran170Fiyat = item.zz_Ayran170Fiyat.toString().toDouble()
                var zz_Ayran150 = item.zz_Ayran150.toString().toInt()
                var zz_Ayran150Fiyat = item.zz_Ayran150Fiyat.toString().toDouble()
                var zz_Tereyag500 = item.zz_Tereyag500.toString().toInt()
                var zz_Tereyag500Fiyat = item.zz_Tereyag500Fiyat.toString().toDouble()
                var zz_Tereyag1Kg = item.zz_Tereyag1Kg.toString().toInt()
                var zz_Tereyag1KgFiyat = item.zz_Tereyag1KgFiyat.toString().toDouble()
                var zz_Tereyag1800 = item.zz_Tereyag1800.toString().toInt()
                var zz_Tereyag1800Fiyat = item.zz_Tereyag1800Fiyat.toString().toDouble()
                var zz_Tereyag1KgKoy = item.zz_Tereyag1KgKoy.toString().toInt()
                var zz_Tereyag1KgKoyFiyat = item.zz_Tereyag1KgKoyFiyat.toString().toDouble()
                var zz_Kaymak200 = item.zz_Kaymak200.toString().toInt()
                var zz_Kaymak200Fiyat = item.zz_Kaymak200Fiyat.toString().toDouble()




                tvFiyat.text = ((sut3ltAdet * sut3ltFiyat) +
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
                        ).toString() + " tl"
            } catch (e: Exception) {
                ref.child("Hatalar/MahalleSiparişleriAdapter/fiyatHatası").setValue("hesap hatası " + e.message.toString())
            }
        }

        fun telAdres(siparisData: SiparisData) {
            siparisTel.setOnClickListener {
                val arama = Intent(Intent.ACTION_DIAL)//Bu kod satırımız bizi rehbere telefon numarası ile yönlendiri.
                arama.data = Uri.parse("tel:" + siparisData.siparis_tel)
                myContext.startActivity(arama)
            }

            siparisAdres.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q= " + siparisData.siparis_mah + " " + siparisData.siparis_adres + " Amasya 05000"))
                myContext.startActivity(intent)
            }
        }

        fun formatDate(miliSecond: Long?): String? {
            if (miliSecond == null) return "0"
            val date = Date(miliSecond)
            val sdf = SimpleDateFormat("d MMM", Locale("tr"))
            return sdf.format(date)
        }


        val tumLayout = itemView.tumLayout
        val siparisVeren = itemView.tvSiparisVeren
        val siparisAdres = itemView.tvSiparisAdres
        val siparisTel = itemView.tvSiparisTel
        val tvZaman = itemView.tvZaman
        val tvTeslimZaman = itemView.tvTeslimZamani
        val tvNot = itemView.tvNot
        val tvFiyat = itemView.tvFiyat
        val siparisGiren = itemView.tvSiparisGiren

        val tb3lt = itemView.tb3lt
        val tv3ltSut = itemView.tv3ltSut

        val tb5lt = itemView.tb5lt
        val tv5ltSut = itemView.tv5ltSut

        val tvCokelek = itemView.tvCokelek
        val tbCokelek = itemView.tbCokelek

        val tvTulum250 = itemView.tvTulum250
        val tbTulum250 = itemView.tbTulum250

        val tvKoy15kg = itemView.tvKoy15Kg
        val tbKoy15kg = itemView.tbKoy15Kg

        val tvKoy5kg = itemView.tvKoy5Kg
        val tbKoy5kg = itemView.tbKoy5Kg

        val tvKaymak200gr = itemView.tvKaymak200Gr
        val tbKaymak200gr = itemView.tbKaymak200Gr

        val tvBp17Kg = itemView.tvBp17Kg
        val tbBp17Kg = itemView.tbBp17Kg

        val tvCerkezPeynir = itemView.tvCerkezPeynir
        val tbCerkezPeynir = itemView.tbCerkezPeynir

        val tvBp5Kg = itemView.tvBp5Kg
        val tbBp5Kg = itemView.tbBp5Kg

        val tvBp800 = itemView.tvBp800
        val tbBp800 = itemView.tbBp800

        val tvBp500 = itemView.tvBp500
        val tbBp500 = itemView.tbBp500

        val tvTereyag1800 = itemView.tvTereyag1800
        val tbTereyag1800 = itemView.tbTereyag1800

        val tvTereyag1Kg = itemView.tvTereyag1Kg
        val tbTereyag1Kg = itemView.tbTereyag1Kg

        val tvTereyag1KgKoy = itemView.tvTereyag1KgKoy
        val tbTereyag1KgKoy = itemView.tbTereyag1KgKoy

        val tvTereyag500 = itemView.tvTereyag500
        val tbTereyag500 = itemView.tbTereyag500

        val tvYogurtSuzme5Kg = itemView.tvYogurtSuzme5Kg
        val tbYogurtSuzme5Kg = itemView.tbYogurtSuzme5Kg

        val tvYogurtSuzme900 = itemView.tvYogurtSuzme900
        val tbYogurtSuzme900 = itemView.tbYogurtSuzme900

        val tvLor9Kg = itemView.tvLor9Kg
        val tbLor9Kg = itemView.tbLor9Kg

        val tvTostKasar2kg = itemView.tvTostKasar2kg
        val tbTostKasar2kg = itemView.tbTostKasar2kg

        val tvPideKasar2Kg = itemView.tvPideKasar2Kg
        val tbPideKasar2Kg = itemView.tbPideKasar2Kg

        val tvKasar1KG = itemView.tvKasar1KG
        val tbKasar1KG = itemView.tbKasar1KG

        val tvKasar400 = itemView.tvKasar400
        val tbKasar400 = itemView.tbKasar400

        val tvKasar700 = itemView.tvKasar700Gr
        val tbKasar700 = itemView.tbKasar700Gr

        val tvAyran150 = itemView.tvAyran150
        val tbAyran150 = itemView.tbAyran150

        val tvAyran170 = itemView.tvAyran170
        val tbAyran170 = itemView.tbAyran170

        val tvAyran180 = itemView.tvAyran180
        val tbAyran180 = itemView.tbAyran180

        val tvAyran200 = itemView.tvAyran200
        val tbAyran200 = itemView.tbAyran200

        val tvAyran220 = itemView.tvAyran220
        val tbAyran220 = itemView.tbAyran220

        val tvAyran290 = itemView.tvAyran290
        val tbAyran290 = itemView.tbAyran290

        val tvAyran1lt = itemView.tvAyran1lt
        val tbAyran1lt = itemView.tbAyran1lt

        val tvYogurt10KgAzyagli = itemView.tvYogurt10KgAzyagli
        val tbYogurt10KgAzyagli = itemView.tbYogurt10KgAzyagli

        val tvYogurt10KgV = itemView.tvYogurt10KgV
        val tbYogurt10KgV = itemView.tbYogurt10KgV

        val tvYogurt200V = itemView.tvYogurt200V
        val tbYogurt200V = itemView.tbYogurt200V

        val tvYogurt10KgDm = itemView.tvYogurt10KgDm
        val tbYogurt10KgDm = itemView.tbYogurt10KgDm

        val tvYogurt4KgDm = itemView.tvYogurt4KgDm
        val tbYogurt4KgDm = itemView.tbYogurt4KgDm

        val tvYogurt3KgDm = itemView.tvYogurt3KgDm
        val tbYogurt3KgDm = itemView.tbYogurt3KgDm

        val tvYogurt2KgDm = itemView.tvYogurt2KgDm
        val tbYogurt2KgDm = itemView.tbYogurt2KgDm

        val tvYogurt2750tava = itemView.tvYogurt2750tava
        val tbYogurt2750tava = itemView.tbYogurt2750tava

        val tvYogurtTam9Kg = itemView.tvYogurtTam9Kg
        val tbYogurtTam9Kg = itemView.tbYogurtTam9Kg

        val tvYogurtTam2Kg = itemView.tvYogurtTam2Kg
        val tbYogurtTam2Kg = itemView.tbYogurtTam2Kg

        val tvYogurttam1250 = itemView.tvYogurttam1250
        val tbYogurttam1250 = itemView.tbYogurttam1250

        val tvYogurtTam500 = itemView.tvYogurtTam500
        val tbYogurtTam500 = itemView.tbYogurtTam500

        val tvYogurtTam200 = itemView.tvYogurtTam200
        val tbYogurtTam200 = itemView.tbYogurtTam200


    }


}