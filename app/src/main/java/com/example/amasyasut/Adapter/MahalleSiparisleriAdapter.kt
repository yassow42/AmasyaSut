package com.example.amasyasut.Adapter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
                                        musteriAd, kullaniciAdi, item.siparis_zamani, item.siparis_teslim_zamani, item.siparis_teslim_tarihi, item.siparis_adres, item.siparis_notu,
                                        item.siparis_mah, item.siparis_key, item.siparis_apartman, item.siparis_tel, false, null, null,
                                        item.zz_3litre,
                                        item.zz_3litreFiyat,
                                        item.zz_5litre,
                                        item.zz_5litreFiyat
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
                        //   view.tvMusteriAdSoyad.setText(siparisler[position].siparis_veren)
                        viewDuzenle.et3lt.setText(siparisler[position].zz_3litre)
                        viewDuzenle.et5lt.setText(siparisler[position].zz_5litre)
                        viewDuzenle.etSiparisNotu.setText(siparisler[position].siparis_notu)

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



                                var ref = FirebaseDatabase.getInstance().reference
                                var not = viewDuzenle.etSiparisNotu.text.toString()
                                var siparisKey = siparisler[position].siparis_key.toString()
                                var siparisVeren = siparisler[position].musteri_ad_soyad.toString()

                                val siparisDataGuncelle = SiparisData(item.musteri_ad_soyad,item.siparisi_giren,item.siparis_zamani,item.siparis_teslim_zamani,cal.timeInMillis,item.siparis_adres,not,
                                item.siparis_mah,item.siparis_key,item.siparis_apartman,item.siparis_tel,item.musteri_zkonum,item.musteri_zlat,item.musteri_zlong,sut3lt,sut3ltFiyat,sut5lt,sut5ltFiyat)
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

        val tvTulum250  = itemView.tvTulum250
        val tbTulum250  = itemView.tbTulum250

        val tvKoy15kg  = itemView.tvKoy15Kg
        val tbKoy15kg  = itemView.tbKoy15Kg

        val tvKoy5kg  = itemView.tvKoy5Kg
        val tbKoy5kg  = itemView.tbKoy5Kg

        val tvKaymak200gr  = itemView.tvKaymak200Gr
        val tbKaymak200gr  = itemView.tbKaymak200Gr

        val tvBp17Kg  = itemView.tvBp17Kg
        val tbBp17Kg  = itemView.tbBp17Kg

        val tvCerkezPeynir  = itemView.tvCerkezPeynir
        val tbCerkezPeynir  = itemView.tbCerkezPeynir

        val tvBp5Kg  = itemView.tvBp5Kg
        val tbBp5Kg  = itemView.tbBp5Kg

        val tvBp800  = itemView.tvBp800
        val tbBp800  = itemView.tbBp800

        val tvBp500  = itemView.tvBp500
        val tbBp500  = itemView.tbBp500

        val tvTereyag1800  = itemView.tvTereyag1800
        val tbTereyag1800  = itemView.tbTereyag1800

        val tvTereyag1Kg  = itemView.tvTereyag1Kg
        val tbTereyag1Kg  = itemView.tbTereyag1Kg

        val tvTereyag1KgKoy  = itemView.tvTereyag1KgKoy
        val tbTereyag1KgKoy  = itemView.tbTereyag1KgKoy

        val tvTereyag500  = itemView.tvTereyag500
        val tbTereyag500  = itemView.tbTereyag500

        val tvYogurtSuzme5Kg  = itemView.tvYogurtSuzme5Kg
        val tbYogurtSuzme5Kg  = itemView.tbYogurtSuzme5Kg

        val tvYogurtSuzme900  = itemView.tvYogurtSuzme900
        val tbYogurtSuzme900  = itemView.tbYogurtSuzme900

        val tvLor9Kg  = itemView.tvLor9Kg
        val tbLor9Kg  = itemView.tbLor9Kg

        val tvTostKasar2kg  = itemView.tvTostKasar2kg
        val tbTostKasar2kg  = itemView.tbTostKasar2kg

        val tvPideKasar2Kg  = itemView.tvPideKasar2Kg
        val tbPideKasar2Kg  = itemView.tbPideKasar2Kg

        val tvKasar1KG  = itemView.tvKasar1KG
        val tbKasar1KG  = itemView.tbKasar1KG

        val tvKasar400  = itemView.tvKasar400
        val tbKasar400  = itemView.tbKasar400

        val tvKasar700  = itemView.tvKasar700Gr
        val tbKasar700  = itemView.tbKasar700Gr

        val tvAyran150  = itemView.tvAyran150
        val tbAyran150  = itemView.tbAyran150

        val tvAyran170  = itemView.tvAyran170
        val tbAyran170  = itemView.tbAyran170

        val tvAyran180  = itemView.tvAyran180
        val tbAyran180  = itemView.tbAyran180

        val tvAyran200  = itemView.tvAyran200
        val tbAyran200  = itemView.tbAyran200

        val tvAyran220  = itemView.tvAyran220
        val tbAyran220  = itemView.tbAyran220

        val tvAyran290  = itemView.tvAyran290
        val tbAyran290  = itemView.tbAyran290

        val tvAyran1lt  = itemView.tvAyran1lt
        val tbAyran1lt  = itemView.tbAyran1lt

        val tvYogurt10KgAzyagli  = itemView.tvYogurt10KgAzyagli
        val tbYogurt10KgAzyagli  = itemView.tbYogurt10KgAzyagli

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

        val tbYogurtTam500 = itemView.tbYogurtTam500
        val tvYogurtTam500 = itemView.tvYogurtTam500

        val tbYogurtTam200 = itemView.tbYogurtTam200
        val tvYogurtTam200 = itemView.tvYogurtTam200


        fun setData(siparisData: SiparisData) {
            siparisVeren.text = siparisData.musteri_ad_soyad.toString()
            siparisAdres.text = siparisData.siparis_mah + " mahallesi " + siparisData.siparis_adres + " " + siparisData.siparis_apartman
            siparisTel.text = siparisData.siparis_tel
            tvNot.text = siparisData.siparis_notu
            siparisGiren.text = siparisData.siparisi_giren

            tb3lt.visibility = View.GONE
            tb5lt.visibility = View.GONE




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
            }else {
                tv3ltSut.text = siparisData.zz_3litre.toString()
                tb3lt.visibility = View.VISIBLE
            }
            if (siparisData.zz_5litre.toString() == "0") {
                tb5lt.visibility = View.GONE
            }else {
                tv5ltSut.text = siparisData.zz_5litre.toString()
                tb5lt.visibility = View.VISIBLE
            }





        }

        fun fiyatHesaplama(siparisData: SiparisData) {

            var sut3ltAdet = siparisData.zz_3litre.toString().toInt()
            var sut3ltFiyat = siparisData.zz_3litreFiyat.toString().toDouble()
            var sut5ltAdet = siparisData.zz_5litre.toString().toInt()
            var sut5ltFiyat = siparisData.zz_5litreFiyat.toString().toDouble()

            tvFiyat.text = ((sut3ltAdet * sut3ltFiyat) + (sut5ltAdet * sut5ltFiyat)).toString() + " TL"
          //  Log.e("sad", ((sut3ltAdet * sut3ltFiyat) + (sut5ltAdet * sut5ltFiyat)).toString())
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

    }


}