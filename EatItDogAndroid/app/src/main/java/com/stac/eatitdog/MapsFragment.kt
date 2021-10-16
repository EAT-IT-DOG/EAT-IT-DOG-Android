package com.stac.eatitdog

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment() {

    companion object {
        fun newInstance() : MapsFragment {
            return MapsFragment()
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val makerOptions = MarkerOptions()
        if (MyLocation.latitude == 0.0 && MyLocation.longitude == 0.0) {
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(LatLng(MyLocation.latitude!!, MyLocation.longitude!!))
                .title("기본 위치 값") // 타이틀.
        }
        else {
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(LatLng(MyLocation.latitude!!, MyLocation.longitude!!))
                .title("내 위치") // 타이틀.
        }

        // 2. 마커 생성 (마커를 나타냄)
        googleMap.addMarker(makerOptions).showInfoWindow()

        val makerOptions1 = MarkerOptions()
        makerOptions1 // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
            .position(LatLng(35.65839845196616, 128.41294259098387))
            .title("초록 동물병원") // 타이틀.
            .snippet("053-638-1558")

        // 2. 마커 생성 (마커를 나타냄)
        googleMap.addMarker(makerOptions1).showInfoWindow()

        val makerOptions2 = MarkerOptions()
        makerOptions2 // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
            .position(LatLng(35.65618989500108, 128.4188453166363))
            .title("동물병원 2") // 타이틀.

        // 2. 마커 생성 (마커를 나타냄)
        googleMap.addMarker(makerOptions2).showInfoWindow()

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(MyLocation.latitude, MyLocation.longitude), 15.0F))
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val adapter = RecyclerViewAdapter()
        recyclerView.adapter = adapter
    }
}