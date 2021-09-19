package com.stac.eatitdog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stac.eatitdog.databinding.FragmentResultDangerBinding
import kotlinx.android.synthetic.main.fragment_result_danger.*

class ResultFragment_danger : Fragment() {

    private var mBinding: FragmentResultDangerBinding? = null
    private val binding get() = mBinding!!

    companion object {
        const val TAG : String = "로그"
        fun newInstance() : ResultFragment_danger {
            return ResultFragment_danger()
        }
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //뷰가 생성 되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentResultDangerBinding.inflate(inflater, container, false)

        binding.dangerFoodname.text = foodinfo.foodname
        binding.dangerEdible.text = foodinfo.edible
        binding.dangerSymptom.text = foodinfo.symptom

        return binding.root
    }

}