package com.stac.eatitdog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_result_danger.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    var suggestions = arrayOf("")

    fun Searchlistserver() {
        val builder: Retrofit.Builder = Retrofit.Builder()
            .baseUrl("http://52.79.148.59:4000/")
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit: Retrofit = builder.build()

        val client: SearchlistService = retrofit.create(SearchlistService::class.java)

        val call: Call<Searchlist> = client.loadNotice()

        call.enqueue(object : Callback<Searchlist> {
            override fun onFailure(call: Call<Searchlist>, t: Throwable) {
                Log.e("debugTest", "error:(${t.message})")
            }
            override fun onResponse(
                call: Call<Searchlist>,
                response: Response<Searchlist>
            ) {
                suggestions = response.body()!!.foods.toTypedArray()
                autoCompleteTextView.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions))
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Searchlistserver()
        //val suggestions = arrayOf("초콜릿", "닭발", "블루베리") //자동완성 되는 검색어들
        //val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
        autoCompleteTextView.setDropDownBackgroundResource(R.color.gray)
        //autoCompleteTextView.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions))

        Search_View.setOnClickListener {
            (activity as MainActivity).Searchserver(autoCompleteTextView.text.toString())
        }
        autoCompleteTextView.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KEYCODE_ENTER) {
                (activity as MainActivity).Searchserver(autoCompleteTextView.text.toString())
                (activity as MainActivity).CloseKeyboard()
            }
            true
        }
    }

    companion object {
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