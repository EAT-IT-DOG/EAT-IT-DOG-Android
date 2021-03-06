package com.stac.eatitdog

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.stac.eatitdog.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_result_danger.*
import kotlinx.android.synthetic.main.fragment_scan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import android.util.Log.d as d1

class MainActivity : AppCompatActivity() {
    private lateinit var scanFragment: ScanFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var mapsFragment: MapsFragment

    private val REQUEST_PERMISSION_LOCATION = 10

    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!

    var locationManager: LocationManager? = null
    private val REQUEST_CODE_LOCATION: Int = 2
    var currentLocation: String = ""
    var latitude: Double? = null
    var longitude: Double? = null

    fun changestring(str: String): String {
        var STR = str.replace("[", "")
        STR = STR.replace("]", "")
        return STR
    }



    fun checkPermissionForLocation(context: Context): Boolean {
        Log.d(TAG, "checkPermissionForLocation()")
        // Android 6.0 Marshmallow ??????????????? ?????? ??????(??????) ????????? ?????? ????????? ????????? ???????????????.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermissionForLocation() ?????? ?????? : O")
                true
            } else {
                // ????????? ???????????? ?????? ?????? ?????? ?????????
                Log.d(TAG, "checkPermissionForLocation() ?????? ?????? : X")
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // ??????????????? ?????? ?????? ??? ????????? ?????? ?????? ??????
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult()")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult() _ ?????? ?????? ??????")

            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ ?????? ?????? ??????")
                Toast.makeText(this, "????????? ?????? GPS ????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*fun resultishelpful(islike : String, foodId : String){
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://52.79.148.59:4000/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val client: ScanService = retrofit.create(ScanService::class.java)

        val call: Call<SearchData> = client.loadNotice(barcodenum.toString())

        call.enqueue(object : Callback<SearchData> {
            override fun onFailure(call: Call<SearchData>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
            override fun onResponse(
                call: Call<SearchData>,
                response: Response<SearchData>
            ) {
                val repo = response.body()

            }
        })
    }*/


    fun Searchserver(foodname: String) {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://52.79.148.59:4000/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val client: SearchService = retrofit.create(SearchService::class.java)

        val call: Call<SearchData> = client.loadNotice(foodname)

        call.enqueue(object : Callback<SearchData> {
            override fun onFailure(call: Call<SearchData>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }

            override fun onResponse(
                call: Call<SearchData>,
                response: Response<SearchData>,
            ) {
                val repo = response.body()
                if (repo != null) {
                    when (repo.food.safetyGrade) {
                        "??????" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.symptom = changestring(repo.food.symptom.toString())
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main_frame, danger.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.main_frame, ResultFragment_danger.newInstance())
                                    .commit()
                            }, 1000)
                        }
                        "??????" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = changestring(repo.food.feedMethod.toString())
                            foodinfo.symptom = changestring(repo.food.symptom.toString())
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main_frame, soso.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.main_frame, ResultFragment_soso.newInstance())
                                    .commit()
                            }, 1000)
                        }
                        "??????" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = changestring(repo.food.feedMethod.toString())
                            foodinfo.ingredient = changestring(repo.food.ingredient.toString())
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main_frame, safe.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.main_frame, ResultFragment_safe.newInstance())
                                    .commit()
                            }, 1000)
                        }
                        else -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.main_frame, ResultFragment_null.newInstance())
                                .commit()
                        }
                    }
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                }
            }
        })
    }

    fun CloseKeyboard() {
        var view = this.currentFocus

        if (view != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun getCurrentLoc() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation: Location? = getLatLng()
        if (userLocation == null) {
            Toast.makeText(this,
                "?????? ????????? ????????? ??? ????????????\n(????????? ?????? ???????????? ?????????????????? ???????????????.)",
                Toast.LENGTH_LONG).show()
        }
        if (userLocation != null) {
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            d1("CheckCurrentLocation", "?????? ??? ?????? ??? : $latitude, $longitude")

            MyLocation.latitude = latitude!!
            MyLocation.longitude = longitude!!

            var mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList: List<Address>? = null
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

    private fun getLatLng(): Location? {
        var currentLatLng: Location? = null
        if (ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                this.REQUEST_CODE_LOCATION
            )
            getLatLng()
        } else {
            val locationProvider = LocationManager.GPS_PROVIDER

            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
            if (currentLatLng == null) {
                return null
            }
        }
        return currentLatLng!!
    }

    companion object {

        const val TAG: String = "??????"

    }

    //???????????? ???????????? ???
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionForLocation(this)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        //??????????????? ??????
        setContentView(binding.root)

        d1(TAG, "MainActivity - onCreate() called")

        bottomNavi.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        scanFragment = ScanFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.main_frame, scanFragment).commit()

    }

    //????????????????????? ????????? ?????? ????????? ??????
    private val onBottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_scan -> {
                    d1(TAG, "MainActivity - ????????????!")
                    scanFragment = ScanFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, scanFragment)
                        .commit()
                }
                R.id.item_serch -> {
                    d1(TAG, "MainActivity - ????????????!")
                    searchFragment = SearchFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frame, searchFragment).commit()
                }
                R.id.item_hospital -> {
                    d1(TAG, "MainActivity - ????????????!")
                    mapsFragment = MapsFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, mapsFragment)
                        .commit()
                    getCurrentLoc()
                }
            }

            true
        }

}

