package com.example.workingparents
import kotlinx.android.synthetic.main.fragment_teacher_notice.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.example.workingparents.*


private lateinit var mContext: Activity
private var TAG="Notice"

class NoticeFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is TeacherMainActivity){
            mContext=context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        var view =inflater.inflate(R.layout.fragment_teacher_notice, container, false)
        val writenotice_btn = view.findViewById<ImageButton>(R.id.writenotice_btn)
        writenotice_btn.setOnClickListener{
            Log.d(TAG,"클릭됨")
            val intent = Intent(mContext, WriteNoticeActivity::class.java)
            mContext.startActivity(intent)
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}