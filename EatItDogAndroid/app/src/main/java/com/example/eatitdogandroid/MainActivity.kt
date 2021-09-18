package com.example.eatitdogandroid

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.sip.SipSession.State.toString
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Integer.toString
import java.lang.invoke.MethodHandleInfo.toString
import java.util.*
import java.util.Objects.toString
import android.util.Log.d as d1


class MainActivity : AppCompatActivity() {
    private lateinit var scanFragment: ScanFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var mapsFragment: MapsFragment

    var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION: Int = 2
    var currentLocation: String = ""
    var latitude: Double? = null
    var longitude: Double? = null

    fun CloseKeyboard()
    {
        var view = this.currentFocus

        if(view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun Search() {
        //Toast.makeText(this, autoCompleteTextView.text.toString(), Toast.LENGTH_SHORT).show()
        //서버 통신 넣고
        when (autoCompleteTextView.text.toString()) {
            "초콜릿" -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_danger.newInstance()).commit()
            }
            "블루베리" -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_soso.newInstance()).commit()
            }
            "닭발" -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_safe.newInstance()).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
            }
        }
    }

     private fun getCurrentLoc() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation : Location = getLatLng()
        if (userLocation != null) {
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            d1("CheckCurrentLocation", "현재 내 위치 값 : $latitude, $longitude")

            MyLocation.latitude = latitude!!
            MyLocation.longitude = longitude!!

            var mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList : List<Address>? = null
            try {
                mResultList = mGeocoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (mResultList != null) {
                d1("CheckCurrentLocation", mResultList[0].getAddressLine(0))
                currentLocation = mResultList[0].getAddressLine(0)
                currentLocation = currentLocation.substring(11)
            }
        }
    }

    private fun getLatLng() : Location {
        var currentLatLng : Location? = null
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                this.REQUEST_CODE_LOCATION
            )
            getLatLng()
        } else {
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        }
        return currentLatLng!!
    }

    companion object {

        const val TAG: String = "로그"

    }


    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //레이아웃과 연결
        setContentView(R.layout.activity_main)

        /*val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("https://eatitdog.loca.lt/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val client: SearchService = retrofit.create(SearchService::class.java)

        val call: Call<SearchData> = client.loadNotice("초콜릿")

        call.enqueue(object : Callback<SearchService> {
            override fun onFailure(call: Call<SearchService>, t: Throwable) {
                Log.e("debugTest","error:(${t.message})")
            }

            override fun onResponse(){

            }*/

        d1(TAG, "MainActivity - onCreate() called")

        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        scanFragment = ScanFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.main_frame, scanFragment).commit()

    }

    //바텀네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_scan -> {
                    d1(TAG, "MainActivity - 스캔버튼!")
                    scanFragment = ScanFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, scanFragment).commit()
                }
                R.id.item_serch -> {
                    d1(TAG, "MainActivity - 검색버튼!")
                    searchFragment = SearchFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, searchFragment).commit()
                }
                R.id.item_hospital -> {
                    d1(TAG, "MainActivity - 병원버튼!")
                    mapsFragment = MapsFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, mapsFragment).commit()
                    getCurrentLoc()
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
    fun startBarcodeReader(view: View) {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("음식의 바코드에 카메라를 비춰달라멍")
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                when(result.contents){
                    "8809602800020" -> {
                        Handler().postDelayed({
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_safe.newInstance()).commit()
                        }, 500)
                    }
                    "50108732" -> {
                        Handler().postDelayed({
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_soso.newInstance()).commit()
                        }, 500)
                    }
                    "034000102532" -> {
                        Handler().postDelayed({
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_danger.newInstance()).commit()
                        }, 500)
                    }
                    else -> {
                        Handler().postDelayed({
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                        }, 500)
                    }
                }
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

