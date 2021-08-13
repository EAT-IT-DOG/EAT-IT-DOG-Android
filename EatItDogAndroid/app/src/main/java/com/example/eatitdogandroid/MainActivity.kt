package com.example.eatitdogandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class MainActivity : AppCompatActivity() {

    //
    private lateinit var scanFragment:ScanFragment
    private lateinit var searchFragment:SearchFragment
    private lateinit var hospitalFragment: HospitalFragment

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
                hospitalFragment = HospitalFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_frame, hospitalFragment).commit()
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
}