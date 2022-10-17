package com.example.workingparents
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.workingparents.databinding.ActivityWriteCafeteriaBinding
import kotlinx.android.synthetic.main.activity_write_cafeteria.*
import kotlinx.android.synthetic.main.layout_parent.view.*
import kotlinx.android.synthetic.main.layout_second.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates
import com.bumptech.glide.Glide
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.transition.*
import com.transitionseverywhere.extra.Scale

class WriteCafeteriaActivity : BaseActivity() {

    val TAG = "CafeteriaWrite"

    companion object {
        lateinit var notosans_medium: Typeface
        lateinit var notosans_regular: Typeface
        lateinit var date: String
        var type :Int =-1
        var realUri: Uri? = null
        lateinit var mContext: Context
    }

    //3. 권한 확인 두개, 카메라 요청 하나, 바인딩 생성
    val PERM_STORAGE = 9
    val PERM_CAMERA = 10
    val REQ_CAMERA = 11
    val REQ_GALLERY = 12
    val binding by lazy { ActivityWriteCafeteriaBinding.inflate(layoutInflater) }

    inner class TypeClickListener : View.OnClickListener {
        override fun onClick(v: View?) {

            when (v?.id) {
                R.id.morning_snack -> {
                    expandable.select_cafateriaTV.text = "오전간식"
                    type = 0
                }
                R.id.lunch -> {
                    expandable.select_cafateriaTV.text = "점심"
                    type = 1
                }
                R.id.afternoon_snack -> {
                    expandable.select_cafateriaTV.text = "오후간식"
                    type = 2
                }
                R.id.dinner -> {
                    expandable.select_cafateriaTV.text = "저녁"
                    type = 3
                }
            }

            if (expandable.isExpanded) {
                expandable.collapse()
                expandable.select_cafateriaTV.setTextColor(Color.parseColor("#000000"))
                expandable.select_cafateriaTV.setTypeface(notosans_medium)
            }

        }
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> initViews()
            PERM_CAMERA -> openCamera()
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {
                //저장소 권한 거절 시
                Toast.makeText(this, "저장소 권한을 승인해주세요", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> {
                //저장소 권한 거절 시
                Toast.makeText(this, "카메라 권한을 승인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requirPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)
        mContext= this

        /*UI요소 초기화부분*/

        notosans_medium = Typeface.createFromAsset(getAssets(), "notosans_medium.otf")
        notosans_regular = Typeface.createFromAsset(getAssets(), "notosans_regular.otf")

        //뒤로가기
        writeCafeteria_back.setOnClickListener {
            onBackPressed()
        }
        //삭제버튼
        delimage.setOnClickListener {
            val set = TransitionSet().addTransition(Scale(0.7f)).addTransition(Fade())
            set.setInterpolator(FastOutLinearInInterpolator())
            if(container3!=null){
                TransitionManager.beginDelayedTransition(container3,set)
            }
            showImageLayout.visibility = View.GONE
            selectImageLayout.visibility = View.VISIBLE
        }

        val cal = Calendar.getInstance()
        val dateformat = SimpleDateFormat("yyyy-MM-dd ")
        val titledDateFormat = SimpleDateFormat("MM/dd")

        date = dateformat.format(cal.time)
        titleDate.setText(titledDateFormat.format(cal.time))


        expandable.parentLayout.setOnClickListener {
            if (expandable.isExpanded) expandable.collapse()
            else {
                expandable.select_cafateriaTV.text = "급식 유형을 선택해주세요."
                expandable.select_cafateriaTV.setTextColor(Color.parseColor("#a4a4a4"))
                expandable.select_cafateriaTV.setTypeface(notosans_regular)
                expandable.expand()
            }
        }

        expandable.morning_snack.setOnClickListener(TypeClickListener())
        expandable.lunch.setOnClickListener(TypeClickListener())
        expandable.afternoon_snack.setOnClickListener(TypeClickListener())
        expandable.dinner.setOnClickListener(TypeClickListener())

        btn_cafeteriaFinish.setOnClickListener{

            if(type==-1 || cafeteria_content.text.toString()=="")
                Toast.makeText(this, "급식유형 선택과 식단을 작성했는지 확인해주세요", Toast.LENGTH_LONG).show()
            else if(!showImageLayout.isVisible)
                Toast.makeText(this, "사진을 첨부했는지 확인해주세요", Toast.LENGTH_LONG).show()
            else{
                //insertCafeteria()
            }
        }
    }

    fun initViews() {
        galleryBtn.setOnClickListener {
            openGallery()
        }
        cameraBtn.setOnClickListener {
            requirPermissions(arrayOf(Manifest.permission.CAMERA), PERM_CAMERA)
        }
    }

    fun openGallery() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, REQ_GALLERY)
    }


    fun openCamera() {
        Log.d(TAG, "카메라 열기")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //10.Uri를 생성. 성공적할 시 반환하는 값이 uri
        createImageUri(newfileName(), "image/jpg")?.let { uri ->
            realUri = uri
            //카메라로 찍은 다음 output을 realUri를 써서해
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }

    //원본 이미지를 저장할 Uri를 MediaStore(데이터베이스)에 생성하는 메서드
    fun createImageUri(filename: String, mimeType: String): Uri? {
        //key와 value로 name과 mimeType을 넘겨줌. filename에 대한 key와 mimeType에 대한 key가 따로 있다
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        //media가 저장되는 테이블의 주소
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun newfileName(): String {
        //파일 이름 중복 방지를 위해 시간값 형태로 만듦
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        //문자열 템플릿을 사용해서 파일 이름과 함께 확장자도 같이 보냄
        return "${filename}.jpg"
    }

    //이미지촬영/선택 후 이미지 넘어옴
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== RESULT_OK){

            when(requestCode){
                REQ_CAMERA -> {

                    if(realUri!=null){
                        Glide.with(this).load(realUri)
                            .override(cafeteria_image.width,cafeteria_image.height)
                            .into(cafeteria_image)

                        showImageLayout.visibility = View.VISIBLE
                        selectImageLayout.visibility = View.GONE
                    }
                }
                REQ_GALLERY->{

                    if(data?.clipData!=null){
                        Log.d(TAG,"사진 여러개 선택하고 값보냄")
                        Toast.makeText(mContext, "한장의 사진만 선택해주세요", Toast.LENGTH_LONG).show()
                    }else{

                        if(data!=null && data.data!=null){
                            realUri= data.data!!

                            Glide.with(this).load(realUri)
                                 .into(cafeteria_image)

                            showImageLayout.visibility = View.VISIBLE
                            selectImageLayout.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}