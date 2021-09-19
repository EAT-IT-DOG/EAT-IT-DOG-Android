package com.stac.eatitdog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stac.eatitdog.databinding.FragmentResultSafeBinding

class ResultFragment_safe : Fragment() {

    private var mBinding: FragmentResultSafeBinding? = null
    private val binding get() = mBinding!!

    companion object {
        const val TAG : String = "로그"
        fun newInstance() : ResultFragment_safe {
            return ResultFragment_safe()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    //뷰가 생성 되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentResultSafeBinding.inflate(inflater, container, false)

        binding.safeFoodname.text = foodinfo.foodname
        binding.safeEdible.text = foodinfo.edible
        binding.safeFeed.text = foodinfo.feed
        binding.safeIngredient.text = foodinfo.ingredient

        return binding.root
    }
}