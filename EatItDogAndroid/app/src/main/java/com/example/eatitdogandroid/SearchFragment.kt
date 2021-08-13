package com.example.eatitdogandroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var suggestions = arrayOf("apple", "banana", "grape", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteTextView.setDropDownBackgroundResource(R.color.gray)
        autoCompleteTextView.setAdapter(adapter)

    }

    companion object{
        const val TAG : String = "로그"

        fun newInstance() : SearchFragment {
            return SearchFragment()
        }
    }
    //메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "SearchFragment - onCreate() called")

    }

    //프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "SearchFragment - onAttach() called")

    }

    //뷰가 생성 되었을 때
    //프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "SearchFragment - onCreateView() called")

        val view = inflater.inflate(R.layout.fragment_search, container, false)



        return view
    }







}