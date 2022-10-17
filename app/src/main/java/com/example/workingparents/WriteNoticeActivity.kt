package com.example.workingparents

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workingparents.databinding.ActivityWriteNoticeBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat


private val TAG="NoticeWrite"


var selectedImageUri = ArrayList<Uri>()
var imageAdapter: MultiImageAdapter? = null
lateinit var notices : ArrayList<Notice>
lateinit var imagePreview: ImageView

//1. BaseAcitivty를 상속받고
class WriteNoticeActivity : BaseActivity() {


    //3. 권한 확인 두개, 카메라 요청 하나, 바인딩 생성
    val PERM_STORAGE=9
    val PERM_CAMERA=10
    val REQ_CAMERA=11
    val REQ_GALLERY=12
    val binding by lazy { ActivityWriteNoticeBinding.inflate(layoutInflater) }


    //val adapter = MultiImageAdapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageAdapter= MultiImageAdapter(selectedImageUri,this)
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.imagerecyclerView.layoutManager = layoutManager
        binding.imagerecyclerView.adapter = imageAdapter
        //4.setContentView에 바인딩 전달
        setContentView(binding.root)

        imagePreview= binding.imagePreview


        //5.카메라를 위해선 저장소 권한이 필요하고 이를 거부할시 종료되는 코드. 공용 저장소 권한이 있는지 확인.
        // 이 처리는 BaseAcitivty에서
        requirPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)

        RetrofitBuilder.api.getNotice().enqueue(object : Callback<List<Notice>> {
            override fun onResponse(call: Call<List<Notice>>, response: Response<List<Notice>>) {
                if (response.isSuccessful) {
                    notices = response.body() as ArrayList<Notice>
                    //맨 최근 것 부터 담김
                    Log.d(TAG, notices.toString())
                } else {
                    Log.d(TAG, "onResponse 후 Notice 실패 에러: ")
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }
            override fun onFailure(call: Call<List<Notice>>, t: Throwable) {
                Log.d(TAG, "onFailure 연결 실패 에러 테스트: " + t.message.toString())
            }
        })

        //뒤로가기
        binding.writeNoticeBack.setOnClickListener{
            onBackPressed()
        }

        binding.btnNoticeFinish.setOnClickListener{

            Log.d(TAG,"타이틀"+binding.noticeTitle.text.toString())
            Log.d(TAG,"내용"+binding.noticeContent.text.toString())
            //입력된 제목이 없다면
            if(binding.noticeTitle.text.toString()==""||binding.noticeContent.text.toString()=="") {
                Log.d(TAG, "제목과 내용을 모두 작성했는지 확인해주세요")
                Toast.makeText(this, "제목과 내용을 모두 작성했는지 확인해주세요", Toast.LENGTH_LONG)
            }else if(binding.noticeTitle.text.toString()!=""&&binding.noticeContent.text.toString()!="")
            {
                if(selectedImageUri.size>0) {
                    insertNotice(1,binding.noticeTitle.text.toString(),binding.noticeContent.text.toString(), selectedImageUri.get(0).toString()) }
                else{
                    insertNotice(1,binding.noticeTitle.text.toString(),binding.noticeContent.text.toString(), "")}
            }

        }

    }

    fun insertNotice(tid:Int, ntitle:String, ncontent:String, image:String){
        RetrofitBuilder.api.postNotice(tid,ntitle,ncontent,image).enqueue(object : Callback<Notice> {
            override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                if (response.isSuccessful) {
                    var result: Notice? = response.body()
                    Log.d(TAG, result.toString())
                    Log.d(TAG, "onResponse: Notice 성공")
                } else {
                    Log.d(TAG, "onResponse: Notice 실패")
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                }
            }

            override fun onFailure(call: Call<Notice>, t: Throwable) {
                Log.d(TAG, "onFailure Notice 실패 에러: " + t.message.toString())

            }
        })
    }

    fun initViews(){
        //6. 카메라 요청 시 권한을 먼저 체크하고 승인되었으면 카메라를 연다.
        binding.buttonCamera.setOnClickListener {
            Log.d(TAG,"카메라 클릭")
            requirPermissions(arrayOf(Manifest.permission.CAMERA),PERM_CAMERA)
        }

        //갤러리 버튼이 클릭 되면 갤러리를 연다
        binding.buttonGallery.setOnClickListener {
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
        var intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
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
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_CAMERA -> {
                    Log.d(TAG, "이미지 가져오기")
                    //val bitmap=data?.extras?.get("data") as Bitmap 미리보기 이미지가 들어감. 가져올 때 Bitmap으로 변환
                    //binding.imagePreview.setImageBitmap(bitmap) 비트맵 가져오기
                    //reaulUri가 null이 아닐때
                    realUri?.let { uri ->
                        Log.d(TAG, uri.toString())

                        /*
                        val bitmap = loadBitmap(uri)
                        binding.imagePreview.setImageBitmap(bitmap)
                        //다 쓰고 나서 null로
                        realUri = null
                         */

                        if (uri != null && selectedImageUri.size<10) {
                            selectedImageUri.add(uri)
                            binding.imagerecyclerView.visibility=VISIBLE
                            imageAdapter?.notifyDataSetChanged()
                            Log.d(TAG,"사진 촬영")
                            Log.d(TAG,"사진 촬영 후 사이즈 = "+selectedImageUri.size.toString())
                        }
                    }
                }
                REQ_GALLERY -> {
                    //data로 넘어옴. Uri 형태로 담겨있음
                    if (data?.clipData != null) {
                        Log.d(TAG,"사진 여러개 선택하고 값보냄")
                        // 사진 여러개 선택한 경우
                        val count = data.clipData!!.itemCount
                        Log.d(TAG,"사진 개수"+count.toString())
                        if (count > 10) {
                            Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG)
                            return
                        }
                        for (i in 0 until count) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            if(selectedImageUri.size<10) { selectedImageUri.add(imageUri)
                            Log.d(TAG,i.toString()+"번째 Uri"+imageUri.toString())
                            }
                        }
                        Log.d(TAG,"갤러리 다중 선택 후 사이즈 = "+selectedImageUri.size.toString())
                        uploadMutiImage(this,selectedImageUri)

                    } else { // 단일 선택
                        data?.data?.let { uri ->
                            val imageUri : Uri? = data?.data
                            if (imageUri != null && selectedImageUri.size<10) {
                                selectedImageUri.add(imageUri)
                                uploadSingleImage(this,imageUri)


                            }
                        }
                        Log.d(TAG,"갤러리 단일 선택 후 사이즈 = "+selectedImageUri.size.toString())

                    }
                    binding.imagerecyclerView.visibility=VISIBLE
                    imageAdapter?.notifyDataSetChanged()
                }
            }
        }
    }
}

fun temporalSetImgView(fileName: String){

    RetrofitBuilder.api.loadFilebyName(fileName).enqueue(object:Callback<ResponseBody>{
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            if(response.isSuccessful){
                val byteArray : ByteArray? = response.body()?.bytes()
              //  val byteArray : ByteArray = result!!.toByteArray()
               if(byteArray!=null){
                   val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                   imagePreview.setImageBitmap(Bitmap.createScaledBitmap(bmp, imagePreview.width,
                       imagePreview.height,false));

                   Log.d(TAG, "onResponse: loadFilebyName 성공")
               }
            }
        }

        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.d(TAG, "onFailure: loadFilebyName 실패"+ t.message)
        }


    })



}


fun deleteImageAdapter(position : Int){
    imageAdapter?.notifyItemRemoved(position)
    selectedImageUri.removeAt(position)
    imageAdapter?.notifyItemChanged(position, selectedImageUri.size)
    Log.d(TAG,"갤러리 삭제 후 사이즈 = "+selectedImageUri.size.toString())
    for (i in 0 until selectedImageUri.size) {
        Log.d(TAG,i.toString()+"번째 Uri"+selectedImageUri.get(i).toString())
    }
}


fun compressImage(context: Context,imageUri: Uri):ByteArray{

    //이미지를 bitmap을 사용해서 20%로 압축해서 보냄
    var inputStream : InputStream? =null;
    try { inputStream = context.contentResolver.openInputStream(imageUri!!) }
    catch(e:IOException){ e.printStackTrace(); }
    var bitmap : Bitmap = BitmapFactory.decodeStream(inputStream);
    var byteArrayOutputStream : ByteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)


    return byteArrayOutputStream.toByteArray()
}


fun uploadSingleImage(context: Context, imageUri: Uri){

    val path :String =RealPathUtil.getRealPath(context,imageUri)
    var file = File(path)
    Log.d(TAG,file.name)

    //파일을 MediaType으로 만들고 이를 RequestBody로 생성
    //name: 서버자체의 key이름, 파일의 지칭명, 파일자체의 내용=RequestBody   --> 3가지 버무려서 만들어진 MulipartBody.body를 서버로 보낸다!
    val requestBody : RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), compressImage(context,imageUri))
    val uploadFile : MultipartBody.Part  = MultipartBody.Part.createFormData("file", file.name ,requestBody);


    RetrofitBuilder.api.uploadImageFile(uploadFile).enqueue(object: Callback<FileUploadResponse>{

        override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
            if(response.isSuccessful){
                val result: FileUploadResponse?= response.body()
                if(result!=null){
                    Log.d(TAG, "onResponse: uploadImageFile 성공")
                    Log.d(TAG,result.toString())

                    temporalSetImgView(file.name)
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

fun uploadMutiImage(context: Context,imageUri: List<Uri>){

    // 여러 file들을 담아줄 ArrayList
    val uploadFileList = ArrayList<MultipartBody.Part>()

    for(uri: Uri in imageUri){
        val path = RealPathUtil.getRealPath(context,uri)
        val requestBody:RequestBody= RequestBody.create(MediaType.parse("image/jpeg"),compressImage(context,uri))
        val uploadFile: MultipartBody.Part = MultipartBody.Part.createFormData("files", File(path).name, requestBody)
        uploadFileList.add(uploadFile)
    }

    RetrofitBuilder.api.uploadMultipleFiles(uploadFileList).enqueue(object: Callback<List<FileUploadResponse>>{

        override fun onResponse(call: Call<List<FileUploadResponse>>, response: Response<List<FileUploadResponse>>) {
            if(response.isSuccessful){
                val result: List<FileUploadResponse>?= response.body()
                if(result!=null){
                    Log.d(TAG, "onResponse: uploadMultipleFiles 성공")
                    Log.d(TAG,result.toString())
                }else
                    Log.d(TAG,"onResponse: uploadMultipleFiles nul날라옴")

            }else{
                Log.d(TAG, "onResponse: uploadMultipleFiles 실패")
            }
        }

        override fun onFailure(call: Call<List<FileUploadResponse>>, t: Throwable) {
            Log.d(TAG, "onFailure : uploadMultipleFiles 에러 " + t.message.toString())
        }

    })

}


