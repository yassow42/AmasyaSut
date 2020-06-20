package com.example.amasyasut.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.amasyasut.Datalar.SiparisData
import com.example.amasyasut.R
import com.example.amasyasut.TimeAgo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.item_siparisler.view.*
import kotlinx.android.synthetic.main.item_teslim.view.*
import kotlinx.android.synthetic.main.item_teslim.view.tb3lt
import kotlinx.android.synthetic.main.item_teslim.view.tb5lt
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran150
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran170
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran180
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran1lt
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran200
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran220
import kotlinx.android.synthetic.main.item_teslim.view.tbAyran290
import kotlinx.android.synthetic.main.item_teslim.view.tbBp17Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbBp500
import kotlinx.android.synthetic.main.item_teslim.view.tbBp5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbBp800
import kotlinx.android.synthetic.main.item_teslim.view.tbCerkezPeynir
import kotlinx.android.synthetic.main.item_teslim.view.tbCokelek
import kotlinx.android.synthetic.main.item_teslim.view.tbKasar1KG
import kotlinx.android.synthetic.main.item_teslim.view.tbKasar400
import kotlinx.android.synthetic.main.item_teslim.view.tbKasar700Gr
import kotlinx.android.synthetic.main.item_teslim.view.tbKaymak200Gr
import kotlinx.android.synthetic.main.item_teslim.view.tbKoy15Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbKoy5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbLor9Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbPideKasar2Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbTereyag1800
import kotlinx.android.synthetic.main.item_teslim.view.tbTereyag1Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbTereyag1KgKoy
import kotlinx.android.synthetic.main.item_teslim.view.tbTereyag500
import kotlinx.android.synthetic.main.item_teslim.view.tbTostKasar2kg
import kotlinx.android.synthetic.main.item_teslim.view.tbTulum250
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt10KgAzyagli
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt10KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt10KgV
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt200V
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt2750tava
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt2KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt3KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurt4KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtSuzme5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtSuzme900
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtTam200
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtTam2Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtTam500
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurtTam9Kg
import kotlinx.android.synthetic.main.item_teslim.view.tbYogurttam1250
import kotlinx.android.synthetic.main.item_teslim.view.tv3ltSut
import kotlinx.android.synthetic.main.item_teslim.view.tv5ltSut
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran150
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran170
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran180
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran1lt
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran200
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran220
import kotlinx.android.synthetic.main.item_teslim.view.tvAyran290
import kotlinx.android.synthetic.main.item_teslim.view.tvBp17Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvBp500
import kotlinx.android.synthetic.main.item_teslim.view.tvBp5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvBp800
import kotlinx.android.synthetic.main.item_teslim.view.tvCerkezPeynir
import kotlinx.android.synthetic.main.item_teslim.view.tvCokelek
import kotlinx.android.synthetic.main.item_teslim.view.tvKasar1KG
import kotlinx.android.synthetic.main.item_teslim.view.tvKasar400
import kotlinx.android.synthetic.main.item_teslim.view.tvKasar700Gr
import kotlinx.android.synthetic.main.item_teslim.view.tvKaymak200Gr
import kotlinx.android.synthetic.main.item_teslim.view.tvKoy15Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvKoy5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvLor9Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvPideKasar2Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvSiparisGiren
import kotlinx.android.synthetic.main.item_teslim.view.tvTereyag1800
import kotlinx.android.synthetic.main.item_teslim.view.tvTereyag1Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvTereyag1KgKoy
import kotlinx.android.synthetic.main.item_teslim.view.tvTereyag500
import kotlinx.android.synthetic.main.item_teslim.view.tvTostKasar2kg
import kotlinx.android.synthetic.main.item_teslim.view.tvTulum250
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt10KgAzyagli
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt10KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt10KgV
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt200V
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt2750tava
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt2KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt3KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurt4KgDm
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtSuzme5Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtSuzme900
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtTam200
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtTam2Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtTam500
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurtTam9Kg
import kotlinx.android.synthetic.main.item_teslim.view.tvYogurttam1250
import kotlinx.android.synthetic.main.item_teslim.view.tvZaman

class TeslimEdilenlerAdapter(val myContext: Context, val siparisler: ArrayList<SiparisData>) : RecyclerView.Adapter<TeslimEdilenlerAdapter.SiparisHolder>() {
    lateinit var mAuth: FirebaseAuth
    lateinit var userID: String
    lateinit var saticiYetki: String
    var ref = FirebaseDatabase.getInstance().reference
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeslimEdilenlerAdapter.SiparisHolder {
        val view = LayoutInflater.from(myContext).inflate(R.layout.item_teslim, parent, false)
        mAuth = FirebaseAuth.getInstance()
        userID = mAuth.currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
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
        holder.setData(siparisler[position])
        holder.itemView.setOnLongClickListener {
            if (saticiYetki == "Yönetici") {
                var alert = AlertDialog.Builder(myContext)
                    .setTitle("Teslim Edilen Siparişi Sil")
                    .setMessage("Emin Misin ?")
                    .setPositiveButton("Sil", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {

                            ref.child("Teslim_siparisler").child(siparisler[position].siparis_key.toString()).removeValue()
                            ref.child("Musteriler").child(siparisler[position].musteri_ad_soyad.toString()).child("siparisleri").child(siparisler[position].siparis_key.toString())
                                .removeValue().addOnCompleteListener {
                                    Toast.makeText(myContext,"Sipariş silindi sayfayı yenileyebilirsin...", Toast.LENGTH_SHORT).show()
                                }

                        }
                    })
                    .setNegativeButton("İptal", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            p0!!.dismiss()
                        }
                    }).create()

                alert.show()
            }
            return@setOnLongClickListener true


        }
        holder.adetGosterim(siparisler[position])

    }

    inner class SiparisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val musteriAdSoyad = itemView.tvMusteriAdSoyad
        val teslimEden = itemView.tvSiparisGiren
        val sut3lt = itemView.tv3ltSut
        val sut5lt = itemView.tv5ltSut
        val zaman = itemView.tvZaman


        fun setData(siparisData: SiparisData) {
            musteriAdSoyad.text = siparisData.musteri_ad_soyad
            sut3lt.text = siparisData.zz_3litre
            sut5lt.text = siparisData.zz_5litre


            zaman.text = TimeAgo.getTimeAgo(siparisData.siparis_teslim_zamani.toString().toLong())
            if (!siparisData.siparisi_giren.isNullOrEmpty()) {
                teslimEden.text = siparisData.siparisi_giren.toString()
            } else {
                teslimEden.visibility = View.GONE
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