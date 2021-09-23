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
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
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

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val FLAG_PERM_CAMERA = 98

    fun checkPermission() {

        // 1. 위험권한(Camera) 권한 승인상태 가져오기
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            // 카메라 권한이 승인된 상태일 경우
        } else {
            // 카메라 권한이 승인되지 않았을 경우
            requestPermission()
        }
    }

    // 2. 권한 요청
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
    }


    fun checkPermissionForLocation(context: Context): Boolean {
        Log.d(TAG, "checkPermissionForLocation()")
        // Android 6.0 Marshmallow 이상에서는 지리 확보(위치) 권한에 추가 런타임 권한이 필요합니다.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : O")
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : X")
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult()")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 클릭")

            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this , "권한이 없어 GPS 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Scanserver(barcodenum: Long) {
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
                if (repo != null) {
                    when (repo.food.safetyGrade) {
                        "위험" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.symptom = repo.food.symptom.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, danger.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_danger.newInstance()).commit()
                            }, 1000)
                        }
                        "양호" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = repo.food.feedMethod.toString()
                            foodinfo.symptom = repo.food.symptom.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, soso.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_soso.newInstance()).commit()
                            }, 1000)
                        }
                        "안전" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = repo.food.feedMethod.toString()
                            foodinfo.ingredient = repo.food.ingredient.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, safe.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_safe.newInstance()).commit()
                            }, 1000)
                        }
                        else -> {
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                        }
                    }
                }
                else {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                }
            }
        })
    }

    fun Searchserver(foodname : String) {
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
                response: Response<SearchData>
            ) {
                val repo = response.body()
                if (repo != null) {
                    when (repo.food.safetyGrade) {
                        "위험" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.symptom = repo.food.symptom.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, danger.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_danger.newInstance()).commit()
                            }, 1000)
                        }
                        "양호" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = repo.food.feedMethod.toString()
                            foodinfo.symptom = repo.food.symptom.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, soso.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_soso.newInstance()).commit()
                            }, 1000)
                        }
                        "안전" -> {
                            foodinfo.foodname = repo.food.foodName
                            foodinfo.edible = repo.food.edible
                            foodinfo.feed = repo.food.feedMethod.toString()
                            foodinfo.ingredient = repo.food.ingredient.toString()
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, safe.newInstance()).commit()
                            Handler().postDelayed({
                                supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_safe.newInstance()).commit()
                            }, 1000)
                        }
                        else -> {
                            supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                        }
                    }
                }
                else {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, ResultFragment_null.newInstance()).commit()
                }
            }
        })
    }

    fun CloseKeyboard()
    {
        var view = this.currentFocus

        if (view != null)
        {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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

        checkPermission()

        checkPermissionForLocation(this)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        //레이아웃과 연결
        setContentView(binding.root)


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
                Scanserver(result.contents.toLong())
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}

