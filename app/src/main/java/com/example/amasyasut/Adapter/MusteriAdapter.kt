package com.example.amasyasut.Adapter


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
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


                        var siparisNotu = dialogViewSp.etSiparisNotu.text.toString()

                        var siparisKey = ref.child("Siparisler").push().key.toString()


                        var siparisData = SiparisData(
                            musteri_ad_soyad, kullaniciAdi.toString(), null, null, cal.timeInMillis, item.musteri_adres, siparisNotu, item.musteri_mah,
                            siparisKey, item.musteri_apartman.toString(), item.musteri_tel, false, null, null,
                            sut3lt, sut3ltFiyat,
                            sut5lt, sut5ltFiyat,
                            bp500, bp500Fiyat,
                            bp800, bp800Fiyat
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

                            dialogView.swKonumKaydet.setOnClickListener {
                                if (dialogView.swKonumKaydet.isChecked) {
                                    ref.child("Musteriler").child(musteri_ad_soyad).child("musteri_zkonum").setValue(true)
                                    holder.getLocation(musteri_ad_soyad)

                                } else {
                                    holder.locationManager.removeUpdates(holder.myLocationListener)
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
                                        holder.locationManager.removeUpdates(holder.myLocationListener)
                                        dialogMsDznle.dismiss()
                                        Toast.makeText(myContext, "Müşteri Bilgileri Güncellendi", Toast.LENGTH_LONG).show()
                                    }.addOnFailureListener { Toast.makeText(myContext, "Müşteri Bilgileri Güncellenemedi", Toast.LENGTH_LONG).show() }
                                } else {
                                    Toast.makeText(myContext, "Bilgilerde boşluklar var", Toast.LENGTH_LONG).show()
                                }
                            }

                            dialogView.imgBack.setOnClickListener {
                                holder.locationManager.removeUpdates(holder.myLocationListener)
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


    }


}
