package com.example.amasyasut.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
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
import kotlinx.android.synthetic.main.item_teslim.view.*

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
                    .setTitle("Siparişi Sil")
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

    }

    inner class SiparisHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val musteriAdSoyad = itemView.tvMusteriAdSoyad
        val teslimEden = itemView.tvSiparisGiren
        val sut3lt = itemView.tv3lt
        val sut5lt = itemView.tv5lt
        val zaman = itemView.tvZaman


        fun setData(siparisData: SiparisData) {
            musteriAdSoyad.text = siparisData.musteri_ad_soyad
            sut3lt.text = siparisData.sut3lt
            sut5lt.text = siparisData.sut5lt
            zaman.text = TimeAgo.getTimeAgo(siparisData.siparis_teslim_zamani.toString().toLong())
            if (!siparisData.siparisi_giren.isNullOrEmpty()) {
                teslimEden.text = siparisData.siparisi_giren.toString()
            } else {
                teslimEden.visibility = View.GONE
            }


        }

    }


}