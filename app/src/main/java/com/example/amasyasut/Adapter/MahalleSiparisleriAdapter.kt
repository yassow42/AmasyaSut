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
                                        item.siparis_mah, item.siparis_key, item.cig_sut, item.cokelek, item.siparis_apartman, item.siparis_tel
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
                        viewDuzenle.etCigSut.setText(siparisler[position].cig_sut)
                        viewDuzenle.etBp500.setText(siparisler[position].cokelek)
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
                                if (viewDuzenle.etCigSut.text.isNotEmpty()) {
                                    sut3lt = viewDuzenle.etCigSut.text.toString()
                                }
                                var sut5lt = "0"
                                if (viewDuzenle.etBp500.text.isNotEmpty()) {
                                    sut5lt = viewDuzenle.etBp500.text.toString()
                                }


                                var ref = FirebaseDatabase.getInstance().reference
                                var not = viewDuzenle.etSiparisNotu.text.toString()
                                var siparisKey = siparisler[position].siparis_key.toString()
                                var siparisVeren = siparisler[position].musteri_ad_soyad.toString()
                                ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisKey).child("sut3lt").setValue(sut3lt)
                                ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisKey).child("sut5lt").setValue(sut5lt)
                                ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisKey).child("siparis_notu").setValue(not)
                                ref.child("Siparisler").child(item.siparis_mah.toString()).child(siparisKey).child("siparis_teslim_tarihi").setValue(cal.timeInMillis)

                                ref.child("Musteriler").child(siparisVeren).child("siparisleri").child(siparisKey).child("sut3lt").setValue(sut3lt)
                                ref.child("Musteriler").child(siparisVeren).child("siparisleri").child(siparisKey).child("sut5lt").setValue(sut5lt)
                                ref.child("Musteriler").child(siparisVeren).child("siparisleri").child(siparisKey).child("siparis_notu").setValue(not)
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
        val cigSut = itemView.tvCigSut
        val cokelek = itemView.tvCokelek
        val tvZaman = itemView.tvZaman
        val tvTeslimZaman = itemView.tvTeslimZamani
        val tvNot = itemView.tvNot
        val tvFiyat = itemView.tvFiyat
        val siparisGiren = itemView.tvSiparisGiren


        fun setData(siparisData: SiparisData) {
            siparisVeren.text = siparisData.musteri_ad_soyad.toString()
            siparisAdres.text = siparisData.siparis_mah + " mahallesi " + siparisData.siparis_adres + " " + siparisData.siparis_apartman
            siparisTel.text = siparisData.siparis_tel
            tvNot.text = siparisData.siparis_notu
            siparisGiren.text = siparisData.siparisi_giren
            siparisData.cig_sut.toString()?.let {
                cigSut.text = it
            }
            siparisData.cokelek.toString()?.let {
                cokelek.text = it
            }
            siparisData.siparis_teslim_tarihi?.let {
                tvTeslimZaman.text = formatDate(siparisData.siparis_teslim_tarihi).toString()
            }
            siparisData.siparis_zamani?.let {
                tvZaman.text = TimeAgo.getTimeAgo(siparisData.siparis_zamani.toString().toLong())
            }





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