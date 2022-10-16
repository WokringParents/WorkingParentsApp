package com.example.workingparents

import android.R
import com.example.workingparents.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.workingparents.UserData.connectedChild
import kotlinx.android.synthetic.main.fragment_mypage.view.*


private lateinit var mContext: Activity

class MypageFragment : Fragment() {

    private val TAG="Mypage"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is MainActivity){
            mContext=context
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        var view =inflater.inflate(com.example.workingparents.R.layout.fragment_mypage, container, false)

        val profile_picture = view.findViewById<ImageView>(com.example.workingparents.R.id.profile_picture)
        val user_name = view.findViewById<TextView>(com.example.workingparents.R.id.user_name)
        val user_sex = view.findViewById<TextView>(com.example.workingparents.R.id.user_sex)
        val user_id = view.findViewById<TextView>(com.example.workingparents.R.id.user_id)

        val couplePageBtn = view.findViewById<LinearLayout>(com.example.workingparents.R.id.couplePage)
        val childPageBtn = view.findViewById<LinearLayout>(com.example.workingparents.R.id.childPage)


        Log.d(TAG, "마이페이지 리프래쉬")



        if(UserData.sex=="M") {
            //사용자가 남자일 때
            view.profile_picture.setImageResource(com.example.workingparents.R.drawable.dad_left)
            view.user_name.text = UserData.name
            view.user_sex.text = "워킹대디"
            view.user_id.text = UserData.id

        }else{
            //사용자가 여자일 때
            view.user_name.text = UserData.name
            view.user_id.text = UserData.id

        }

        if(UserData.connectedCouple()) {
            //부부연결이 된 상태라면
            Log.d(TAG, "부부 연결 완료")
            view.spouse_state.text = "연결중"
            view.spouse_state.setTextColor(Color.parseColor("#ff9769"))
            view.spouse_name.text = UserData.spouseName
            if (UserData.sex == "M") {
                view.spouse_image.setImageResource(com.example.workingparents.R.drawable.mom_left)
            }
            view.spouse_image.visibility = View.VISIBLE

        }

        if(UserData.connectedChild()) {
            //아이연결이 된 상태라면
            Log.d(TAG, "아이 연결 완료")
            view.child_state.text = "연결중"
            view.child_state.setTextColor(Color.parseColor("#ff9769"))
            view.child_image.visibility = View.VISIBLE
            view.child_name.text = UserData.childName
            Log.d("아이등록",UserData.childName)
        }


        couplePageBtn.setOnClickListener {
            if (UserData.connectedCouple()) {
                //부부연결된 상태라면
                Toast.makeText(getActivity(), "이미 부부가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(mContext, CoupleConnectActivity::class.java)
                mContext.startActivity(intent)
            }
        }

            childPageBtn.setOnClickListener {
                if (UserData.connectedCouple()) {
                    //부부연결 되었을 때
                    if (UserData.connectedChild()) {
                        //아이연결이 된 상태라면
                        Toast.makeText(getActivity(), "이미 아이가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(mContext, RegisterChildActivity::class.java)
                        mContext.startActivity(intent)
                    }
                } else {
                    Toast.makeText(getActivity(), "먼저 커플등록을 해주세요", Toast.LENGTH_SHORT).show()

                }
            }


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onResume() {
        super.onResume()


        val couplePageBtn = view?.findViewById<LinearLayout>(com.example.workingparents.R.id.couplePage)
        val childPageBtn = view?.findViewById<LinearLayout>(com.example.workingparents.R.id.childPage)

        if(UserData.connectedCouple()) {
            //부부연결이 된 상태라면
            Log.d(TAG, "부부 연결 완료")
            view?.spouse_state!!.text = "연결중"
            view?.spouse_state!!.setTextColor(Color.parseColor("#ff9769"))
            view?.spouse_name!!.text = UserData.spouseName
            if (UserData.sex == "M") {
                view?.spouse_image!!.setImageResource(com.example.workingparents.R.drawable.mom_left)
            }
            view?.spouse_image!!.visibility = View.VISIBLE

        }

        if(UserData.connectedChild()) {
            //아이연결이 된 상태라면
            Log.d(TAG, "아이 연결 완료")
            view?.child_state!!.text = "연결중"
            view?.child_state!!.setTextColor(Color.parseColor("#ff9769"))
            view?.child_image!!.visibility = View.VISIBLE
            view?.child_name!!.text = UserData.childName
            Log.d("아이등록",UserData.childName)
        }

        couplePageBtn?.setOnClickListener {
            if (UserData.connectedCouple()) {
                //부부연결된 상태라면
                Toast.makeText(getActivity(), "이미 부부가 등록되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(mContext, CoupleConnectActivity::class.java)
                mContext.startActivity(intent)
            }
        }

            childPageBtn?.setOnClickListener {
                if (UserData.connectedCouple()) {
                    //부부연결 되었을 때
                    if (UserData.connectedChild()) {
                        //아이연결이 된 상태라면
                        Toast.makeText(getActivity(), "이미 아이가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(mContext, RegisterChildActivity::class.java)
                        mContext.startActivity(intent)
                    }
                } else {
                    Toast.makeText(getActivity(), "먼저 커플등록을 해주세요", Toast.LENGTH_SHORT).show()

                }

        }
    }

    fun getInstance(): MypageFragment? {
        return MypageFragment()
    }
}