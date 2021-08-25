package com.example.eatitdogandroid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var scanFragment:ScanFragment
    private lateinit var searchFragment:SearchFragment
    private lateinit var mapsFragment: MapsFragment


    companion object{

        const val TAG : String = "로그"

    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //레이아웃과 연결
        setContentView(R.layout.activity_main)


        Log.d(TAG, "MainActivity - onCreate() called")

        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        scanFragment = ScanFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.main_frame, scanFragment).commit()
    }

    //바텀네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.item_scan ->{
                Log.d(TAG, "MainActivity - 스캔버튼!")
                scanFragment = ScanFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, scanFragment).commit()
            }
            R.id.item_serch ->{
                Log.d(TAG, "MainActivity - 검색버튼!")
                searchFragment = SearchFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, searchFragment).commit()
            }
            R.id.item_hospital ->{
                Log.d(TAG, "MainActivity - 병원버튼!")
                mapsFragment = MapsFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, mapsFragment).commit()
            }
        }

        true
    }

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "MainActivity - onNavigationItemSelected() called")

        when(item.itemId){
            R.id.item_scan ->{
                Log.d(TAG, "MainActivity - 스캔버튼!")
            }
            R.id.item_serch ->{
                Log.d(TAG, "MainActivity - 검색버튼!")
            }
            R.id.item_hospital ->{
                Log.d(TAG, "MainActivity - 병원버튼!")
            }
        }

        return true
    }*/
    fun startBarcodeReader(view: View){
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("바코드를 스캔하여 주세요");
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.contents != null){
                Toast.makeText(this, "scanned : ${result.contents} format : ${result.formatName}", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}