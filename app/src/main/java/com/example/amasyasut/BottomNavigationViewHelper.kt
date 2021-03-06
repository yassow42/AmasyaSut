package com.example.amasyasut

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.example.amasyasut.Activity.MapsActivity
import com.example.amasyasut.Activity.MusterilerActivity
import com.example.amasyasut.Activity.SiparislerActivity
import com.example.amasyasut.Activity.TeslimActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

class BottomNavigationViewHelper {


    companion object {

        fun setupBottomNavigationView(bottomNavigationViewEx: BottomNavigationViewEx) {


            bottomNavigationViewEx.enableAnimation(true)
            bottomNavigationViewEx.enableItemShiftingMode(false)
            bottomNavigationViewEx.enableShiftingMode(false)
            bottomNavigationViewEx.setTextVisibility(true)

        }

        fun setupNavigation(context: Context, bottomNavigationViewEx: BottomNavigationViewEx) {

            bottomNavigationViewEx.onNavigationItemSelectedListener =
                object : BottomNavigationView.OnNavigationItemSelectedListener {
                    override fun onNavigationItemSelected(item: MenuItem): Boolean {

                        when (item.itemId) {

                            R.id.ic_siparisler -> {
                                val intent = Intent(context, SiparislerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)


                            }

                            R.id.ic_teslimedildi -> {
                                val intent = Intent(context, TeslimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)
                            }

                            R.id.ic_musteri -> {
                                val intent = Intent(context, MusterilerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)

                            }


                            R.id.ic_maps -> {
                                val intent = Intent(context, MapsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                                context.startActivity(intent)
                            }

                        }
                        return false
                    }
                }
        }
    }
}