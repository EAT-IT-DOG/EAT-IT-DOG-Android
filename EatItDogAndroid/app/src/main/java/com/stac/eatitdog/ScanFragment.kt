package com.stac.eatitdog

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.Result
import kotlinx.android.synthetic.main.fragment_scan.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanFragment : Fragment(), ZXingScannerView.ResultHandler {
    companion object {
        const val TAG : String = "로그"
        fun newInstance() : ScanFragment {
            return ScanFragment()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ScanFragment - onCreate() called")
    }

    //프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "ScanFragment - onAttach() called")
    }

    private lateinit var mview : View

    private lateinit var scannerView : ZXingScannerView
    //뷰가 생성 되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "ScanFragment - onCreateView() called")
        mview = inflater.inflate(R.layout.fragment_scan, container, false)
        intializeQrScanner()
        return mview.rootView
    }

    private fun intializeQrScanner() {
        scannerView = ZXingScannerView(context)
        scannerView.setAutoFocus(true)
        scannerView.setResultHandler(this)
        mview.containerScanner.addView(scannerView)
        startQRCamera()
    }

    private fun startQRCamera() {
        scannerView.startCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView.stopCamera()
    }

    override fun handleResult(rawResult: Result?) {

    }
}