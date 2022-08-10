package com.example.workingparents

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.workingparents.databinding.FragmentBoardBinding
import kotlinx.android.synthetic.main.fragment_board.*

private val TAG="Board"


class BoardFragment : Fragment(){

    //Fragment의 뷰를 그릴 때 호출되는 콜백
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val bind=FragmentBoardBinding.inflate(layoutInflater)
        val boardfragment = BoardFragment()

        bind.writepostingBtn.setOnClickListener{
            Log.d(TAG,"클릭됨")
            activity?.let{
                val intent = Intent(context, WritePostingActivity::class.java)
                startActivity(intent)
            }
        }
        return  bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}