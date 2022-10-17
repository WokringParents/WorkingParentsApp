package com.example.workingparents
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.Toast
import com.example.workingparents.databinding.ActivityWriteCafeteriaBinding
import kotlinx.android.synthetic.main.activity_write_cafeteria.*
import kotlinx.android.synthetic.main.layout_parent.view.*
import kotlinx.android.synthetic.main.layout_second.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class WriteCafeteriaActivity : BaseActivity() {

    val TAG="CafeteriaWrite"

    companion object {
        lateinit var notosans_medium: Typeface
        lateinit var notosans_regular: Typeface
        lateinit var date:String
        var type by Delegates.notNull<Int>()
    }

    //3. 권한 확인 두개, 카메라 요청 하나, 바인딩 생성
    val PERM_STORAGE=9
    val PERM_CAMERA=10
    val REQ_CAMERA=11
    val REQ_GALLERY=12
    val binding by lazy{ActivityWriteCafeteriaBinding.inflate(layoutInflater)}

    inner class TypeClickListener : View.OnClickListener {
        override fun onClick(v: View?) {

            when (v?.id) {
                R.id.morning_snack -> {
                    expandable.select_cafateriaTV.text = "오전간식"
                    type=0
                }
                R.id.lunch -> {
                    expandable.select_cafateriaTV.text="점심"
                    type=1
                }
                R.id.afternoon_snack -> {
                    expandable.select_cafateriaTV.text="오후간식"
                    type=2
                }
                R.id.dinner->{
                    expandable.select_cafateriaTV.text="저녁"
                    type=3
                }
            }

            if(expandable.isExpanded){
                expandable.collapse()
                expandable.select_cafateriaTV.setTextColor(Color.parseColor("#000000"))
                expandable.select_cafateriaTV.setTypeface(notosans_medium)
            }

        }
    }

    override fun permissionGranted(requestCode: Int) {

    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE->{
                //저장소 권한 거절 시
                Toast.makeText(this, "저장소 권한을 승인해주세요", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA->{
                //저장소 권한 거절 시
                Toast.makeText(this, "카메라 권한을 승인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
       // setContentView(R.layout.activity_write_cafeteria)
        notosans_medium = Typeface.createFromAsset(getAssets(), "notosans_medium.otf")
        notosans_regular = Typeface.createFromAsset(getAssets(), "notosans_regular.otf")

        requirPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)

        //뒤로가기
        writeCafeteria_back.setOnClickListener {
            onBackPressed()
        }

        val cal = Calendar.getInstance()
        val dateformat = SimpleDateFormat("yyyy-MM-dd ")
        val titledDateFormat= SimpleDateFormat("MM/dd")

        date =dateformat.format(cal.time)
        titleDate.setText(titledDateFormat.format(cal.time))


        expandable.parentLayout.setOnClickListener {
            if (expandable.isExpanded) expandable.collapse()
            else {
                expandable.select_cafateriaTV.text="급식 유형을 선택해주세요."
                expandable.select_cafateriaTV.setTextColor(Color.parseColor("#a4a4a4"))
                expandable.select_cafateriaTV.setTypeface(notosans_regular)
                expandable.expand()
            }
        }

        expandable.morning_snack.setOnClickListener(TypeClickListener())
        expandable.lunch.setOnClickListener(TypeClickListener())
        expandable.afternoon_snack.setOnClickListener(TypeClickListener())
        expandable.dinner.setOnClickListener(TypeClickListener())



    }
}
