package com.example.workingparents

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.workingparents.databinding.ActivityMainBinding
import com.example.workingparents.databinding.ActivityWriteNoticeBinding
import kotlinx.android.synthetic.main.activity_write_notice.*
import java.io.IOException
import java.text.SimpleDateFormat

private val TAG="Notice"

//1. BaseAcitivty를 상속받고
class WriteNoticeActivity : BaseActivity() {

    //3. 권한 확인 두개, 카메라 요청 하나, 바인딩 생성
    val PERM_STORAGE=9
    val PERM_CAMERA=10
    val REQ_CAMERA=11
    val REQ_GALLERY=12
    val binding by lazy { ActivityWriteNoticeBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //4.setContentView에 바인딩 전달
        setContentView(binding.root)

        //5.카메라를 위해선 저장소 권한이 필요하고 이를 거부할시 종료되는 코드. 공용 저장소 권한이 있는지 확인.
        // 이 처리는 BaseAcitivty에서
        requirPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)

        //뒤로가기
        binding.writeNoticeBack.setOnClickListener{
            onBackPressed()
        }

    }

    fun initViews(){
        //6. 카메라 요청 시 권한을 먼저 체크하고 승인되었으면 카메라를 연다.
        binding.buttonCamera.setOnClickListener {
            Log.d(TAG,"카메라 클릭")
            requirPermissions(arrayOf(Manifest.permission.CAMERA),PERM_CAMERA)
        }

        //갤러리 버튼이 클릭 되면 갤러리를 연다
        binding.btnNoticeFinish.setOnClickListener {
            openGallery()
        }
    }

    //원본 이미지의 주소를 저장할 변수
    var realUri: Uri?=null

    // 카메라를 찍은 사진을 저장하기 위한 Uri를 넘겨준다
    fun openCamera()
    {
        Log.d(TAG,"카메라 열기")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //10.Uri를 생성. 성공적할 시 반환하는 값이 uri
        createImageUri(newfileName(),"image/jpg")?.let { uri->
            realUri = uri
            //카메라로 찍은 다음 output을 realUri를 써서해
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent,REQ_CAMERA)
        }
    }

    fun openGallery(){
        val intent=Intent(Intent.ACTION_PICK)
        intent.type=MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    //8. 원본 이미지를 저장할 Uri를 MediaStore(데이터베이스)에 생성하는 메서드. 두가지가 필요한데
    //파일 이름과 파일 타입이 필수
    fun createImageUri(filename:String,mimeType:String) : Uri?{
        //key와 value로 name과 mimeType을 넘겨줌. filename에 대한 key와 mimeType에 대한 key가 따로 있다
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)

        //media가 저장되는 테이블의 주소라고 생각하면 된다
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

    //9. 파일 이름을 생성하는 메서드
    fun newfileName():String{
        //파일 이름 중복 방지를 위해 시간값 형태로 만듦
        val sdf=SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        //문자열 템플릿을 사용해서 파일 이름과 함께 확장자도 같이 보냄
        return "${filename}.jpg"
    }

    //11.원본 이미지를 불러오는 메서드
    fun loadBitmap(photoUri: Uri): Bitmap? {
        var image : Bitmap?=null

        //예외처리가 발생할 수 있어서
        try{
             image=if(Build.VERSION.SDK_INT>27){
                 val source=ImageDecoder.createSource(contentResolver,photoUri)
                 ImageDecoder.decodeBitmap(source)
             }else{
                 MediaStore.Images.Media.getBitmap(contentResolver,photoUri)
             }
        }catch(e:IOException){
            e.printStackTrace()
        }
        return image
    }


    //2. ctrl+i로 권한 메소드를 만들어준다.
    override fun permissionGranted(requestCode: Int) {
        when(requestCode){
            //저장소 허용시 initViews 호출
            PERM_STORAGE->initViews()
            //카메라 허용시 openCamera 호출
            PERM_CAMERA ->openCamera()
        }

    }

    override fun permissionDenied(requestCode: Int) {
        when(requestCode){
            PERM_STORAGE->{
                //저장소 권한 거절 시
                Toast.makeText(this, "저장소 권한을 승인해주세요",Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA->{
                //저장소 권한 거절 시
                Toast.makeText(this, "카메라 권한을 승인해주세요",Toast.LENGTH_SHORT).show()
            }
        }

    }

    //7. 카메라 열고 결과값이 여기로 넘어옴. 이미지가 data에 담김.
    //갤러리에서 선택 후 호출
    //data는 미리보기
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== RESULT_OK)
        {
            when(requestCode){
                REQ_CAMERA->{
                    Log.d(TAG,"이미지 가져오기")
                    //val bitmap=data?.extras?.get("data") as Bitmap 미리보기 이미지가 들어감. 가져올 때 Bitmap으로 변환
                    //binding.imagePreview.setImageBitmap(bitmap) 비트맵 가져오기
                    //reaulUri가 null이 아닐때
                    realUri?.let { uri->
                        Log.d(TAG,uri.toString())
                        val bitmap=loadBitmap(uri)
                        binding.imagePreview.setImageBitmap(bitmap)
                        //다 쓰고 나서 null로
                        realUri=null
                    }
                }
                REQ_GALLERY->{
                    //data로 넘어옴. Uri 형태로 담겨있음
                    data?.data?.let { uri ->
                        Log.d(TAG,"여기가 uri"+uri.toString())
                        binding.imagePreview.setImageURI(uri)
                    }
                }
           }
        }
    }
}