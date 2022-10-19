package com.example.workingparents
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.bumptech.glide.Glide
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.transition.*
import com.transitionseverywhere.extra.Scale
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Paths

class WriteCafeteriaActivity : BaseActivity() {

    val TAG = "CafeteriaWrite"
    val TYPE ="Cafeteria"
    companion object {
        lateinit var notosans_medium: Typeface
        lateinit var notosans_regular: Typeface
        lateinit var date: String
        var ctype :Int =-1
        lateinit var realUri: Uri
        lateinit var fileName: String
        lateinit var mContext: Context
        lateinit var imageByteArray: ByteArray
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
                    ctype = 0
                }
                R.id.lunch -> {
                    expandable.select_cafateriaTV.text = "점심"
                    ctype = 1
                }
                R.id.afternoon_snack -> {
                    expandable.select_cafateriaTV.text = "오후간식"
                    ctype = 2
                }
                R.id.dinner -> {
                    expandable.select_cafateriaTV.text = "저녁"
                    ctype = 3
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
        val dateformat = SimpleDateFormat("yyyy-MM-dd")
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

            if(ctype==-1 || cafeteria_content.text.toString()=="")
                Toast.makeText(this, "모두 기입하였는지 확인해주세요", Toast.LENGTH_LONG).show()
            else if(!showImageLayout.isVisible)
                Toast.makeText(this, "사진을 첨부했는지 확인해주세요", Toast.LENGTH_LONG).show()
            else {
                uploadCafeteriaImage()
                insertCafeteria()
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

    //이미지를 bitmap을 사용해서 20%로 압축해서 보냄
    fun compressImage(context: Context,imageUri: Uri):ByteArray{
        var inputStream : InputStream? =null;
        try { inputStream = context.contentResolver.openInputStream(imageUri!!) }
        catch(e: IOException){ e.printStackTrace(); }
        var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream);
        var byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)

        imageByteArray = byteArrayOutputStream.toByteArray()

        return byteArrayOutputStream.toByteArray()
    }

    fun uploadCafeteriaImage(){

        val path :String =RealPathUtil.getRealPath(mContext, realUri)
        var file= File(path)
        fileName=file.name

        val requestBody : RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), compressImage(mContext, realUri))
        val uploadFile : MultipartBody.Part  = MultipartBody.Part.createFormData("file", file.name ,requestBody);

        RetrofitBuilder.api.uploadImageFile(uploadFile,TYPE).enqueue(object:
            Callback<FileUploadResponse> {

            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if(response.isSuccessful){
                    val result: FileUploadResponse?= response.body()
                    if(result!=null){
                        Log.d(TAG, "onResponse: uploadImageFile 성공")
                        Log.d(TAG,result.toString())

                    }
                }else{
                    Log.d(TAG, "onResponse: uploadImageFile 실패")
                }
            }
            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.d(TAG, "onFailure : uploadImageFile 에러: " + t.message.toString())
            }
        })
    }

    fun insertCafeteria(){

        val path :String =RealPathUtil.getRealPath(mContext, realUri)
        var file= File(path)
        fileName=file.name
        val content= cafeteria_content.text.toString()

        RetrofitBuilder.api.postCafeteria(1,date,ctype,content,fileName).enqueue(object: Callback<Int>{

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    var result: Int? = response.body()
                    Log.d(TAG, result.toString())
                    Log.d(TAG, "onResponse: postCafeteria 성공")

                 //   CafeteriaFragment.cafeOuterAdapter.cafeData.find{ it.cdate==date }?.images?.put(ctype,fileName)
                 //   CafeteriaFragment.cafeOuterAdapter.cafeData.find{ it.cdate==date }?.contents?.put(ctype,content)
                    var index= -1

                    Log.d(TAG,"size"+ CafeteriaFragment.cafeOuterAdapter.cafeData.size )
                    Log.d(TAG,"date"+ date +" " + CafeteriaFragment.cafeOuterAdapter.cafeData.get(0).cdate)


                    CafeteriaFragment.cafeOuterAdapter.cafeData.filter { it.cdate == date }.
                    forEach {
                        Log.d(TAG,"date찾아서 change한다")
                        index = CafeteriaFragment.cafeOuterAdapter.cafeData.indexOf(it)
                        it.images.put(ctype,fileName)
                        it.contents.put(ctype,content)
                        it.imageBytes.put(ctype,imageByteArray)
                    }

                    if( index!= -1)
                        CafeteriaFragment.cafeOuterAdapter.notifyItemChanged(index)
                    else{
                        //오늘 글 처음 등록한 상태라면
                        Log.d(TAG,"처음등록했잖아 갱신해야지 왜안해")
                        CafeteriaFragment.cafeOuterAdapter.cafeData.add(0, CafeteriaByDate(date))
                        CafeteriaFragment.cafeOuterAdapter.cafeData.get(0).images.put(ctype,fileName)
                        CafeteriaFragment.cafeOuterAdapter.cafeData.get(0).contents.put(ctype,content)
                        CafeteriaFragment.cafeOuterAdapter.cafeData.get(0).imageBytes.put(ctype,imageByteArray)

                        CafeteriaFragment.cafeOuterAdapter.notifyDataSetChanged()

                            //CafeteriaFragment.cafeOuterAdapter.innerAdapter.cafeData.
                            Log.d(TAG,"오늘글은 처음등록이지만 이전글 목록이 있을때")
                        /*    Log.d(TAG,CafeteriaFragment.cafeOuterAdapter.innerAdapter!!.cafeData.toString())
                            CafeteriaFragment.cafeOuterAdapter.innerAdapter!!.cafeData.imageBytes.put(ctype,imageByteArray)
                            CafeteriaFragment.cafeOuterAdapter.innerAdapter!!.cafeData.contents.put(ctype,content)
                            CafeteriaFragment.cafeOuterAdapter.innerAdapter!!.cafeData.images.put(ctype,fileName)
                            CafeteriaFragment.cafeOuterAdapter.notifyItemInserted()*/

                    }

                    finish()


                } else {
                    Log.d(TAG, "onResponse: postCafeteria 실패")
                    Toast.makeText(mContext, "이미 등록된 급식유형입니다.", Toast.LENGTH_LONG).show()
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d(TAG, "onFailure postCafeteria 실패 에러: " + t.message.toString())
            }
        })
    }

}