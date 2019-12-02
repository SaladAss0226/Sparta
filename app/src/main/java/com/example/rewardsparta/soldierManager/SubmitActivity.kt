package com.example.rewardsparta.soldierManager

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.rewardsparta.ResponseSubmit
import com.example.rewardsparta.signIn.SignInActivity.Companion.token
import com.example.rewardsparta.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_submit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import okhttp3.RequestBody
import okhttp3.MultipartBody
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.rewardsparta.soldierManager.TakeCaseActivity.Companion.rewardId
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream


class SubmitActivity : AppCompatActivity() {

    var saveUri: Uri? = null
    var realUri: String = ""
    var bitmap: ByteArray? = null;

    companion object {
        val PHOTO_FROM_GALLERY = 0
        val PHOTO_FROM_CAMERA = 1
    }


    inner class SubmitAPIFunction {

        fun sendSubmit(
            token: String,
            itemId: Int,
            file: MultipartBody.Part,
            item: MultipartBody.Part
        ) {

            val call: Call<ResponseSubmit> =
                SignUpActivity.ApiClient.getClient.sendSubmit(token, itemId, file, item)
            call.enqueue(object : Callback<ResponseSubmit> {
                override fun onResponse(
                    call: Call<ResponseSubmit>?,
                    response: Response<ResponseSubmit>?
                ) {

//                    print("item id $itemId")
//                    println("code ${response?.code()} ${response?.errorBody()?.string()}")
//                    val body = response?.body()
//                    println("submit success $body")

                    if (response!!.isSuccessful) {
                        println("submit success")
                        Toast.makeText(this@SubmitActivity,"回報成功",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SubmitActivity,TakeCaseActivity::class.java))
                    } else println("fail")

                }

                override fun onFailure(call: Call<ResponseSubmit>?, t: Throwable?) {
                    println("test $t")     //t會報錯
                    Log.e("error", "test")

                }
            })
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.rewardsparta.R.layout.activity_submit)

        if (savedInstanceState != null) {
            saveUri = Uri.parse(savedInstanceState.getString("saveUri"))
        }
        permission()

        btn_album.setOnClickListener {
            toAlbum()
        }

        btn_camera.setOnClickListener {
            toCamera()
        }

        btn_send.setOnClickListener {

            if (bitmap == null) return@setOnClickListener
            //發送api
            var requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), bitmap!!)//file)
            var body: MultipartBody.Part =
                MultipartBody.Part.createFormData("img", "sample.png", requestFile)
            var descript: MultipartBody.Part = MultipartBody.Part.createFormData(
                "reported_descript",
                et_reported_descript.text.toString()
            )
            SubmitAPIFunction().sendSubmit(token,rewardId, body, descript)

        }

        btn_send.isEnabled = false

    }


    fun permission() {
        val permissionList = arrayListOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        var size = permissionList.size
        var i = 0
        while (i < size) {         //將三項存取權用迴圈一個一個判斷使用者是否同意存取
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permissionList[i]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.removeAt(i)
                i -= 1
                size -= 1
            }
            i += 1
        }
        val array = arrayOfNulls<String>(permissionList.size)
        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions(
            this,
            permissionList.toArray(array),
            0
        )
    }

    fun toAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")                                    //intent 必須 setType ，透過指定的 MIME Type 取得相對應類型的檔案
        startActivityForResult(intent, PHOTO_FROM_GALLERY)
    }

    fun toCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        //設置並提供 Camera 存放照片的 Uri

        val tmpFile = File(
            Environment.getExternalStorageDirectory().toString(),
            System.currentTimeMillis().toString() + ".jpg"
        )
        realUri = tmpFile.path
        val uriForCamera =
            FileProvider.getUriForFile(this, "com.example.rewardsparta.fileprovider", tmpFile)
        //取得file: getUriForFile(context,authorities,file)
        saveUri = uriForCamera               //將 Uri 存進變數供後續 onActivityResult 使用
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            uriForCamera
        )       //透過 intent.putExtra 的方式 通知相機新照片的儲存位置（Uri） KEY 為官方設定好的 MediaStore.EXTRA_OUTPUT
        startActivityForResult(intent, PHOTO_FROM_CAMERA)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveUri != null) {
            val uriString = saveUri.toString()
            outState.putString("saveUri", uriString)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {                                     //requestCode：前面設置用來辨別是哪個 intent 回傳的結果
            PHOTO_FROM_GALLERY -> {
                when (resultCode) {                              //resultCode：確認 intent 成功與否
                    Activity.RESULT_OK -> {
                        val uri = data!!.data               //data：intent帶回的資料
                        imgv_upload.setImageURI(uri)             //相簿會以Intent(action:String, uri:Uri) 的方式回傳 Uri 須用data.data 的方式取得Uri

                        Glide
                            .with(this)
                            .asBitmap()
                            .load(uri)
                            .downsample(DownsampleStrategy.DEFAULT)
                            .listener(object : RequestListener<Bitmap> {
                                override fun onResourceReady(
                                    resource: Bitmap?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {

                                    val stream = ByteArrayOutputStream();
                                    resource?.compress(Bitmap.CompressFormat.JPEG, 60, stream);

                                    val byteArray = stream.toByteArray();
                                    println("**************** ${byteArray.size}")

                                    resource?.recycle()
                                    runOnUiThread {
                                        bitmap = byteArray
                                        if (bitmap != null) {
                                            btn_send.isEnabled = true
                                        }
                                    }

                                    return true
                                }

                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Bitmap>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                    return true
                                }

                            })
                            .submit()
//                        var thePath = "no-path-found"
//                        val filePathColumn = arrayOf(MediaStore.Images.Media.DISPLAY_NAME)
//                        val cursor =
//                            contentResolver.query(uri!!, filePathColumn, null, null, null)
//                        if (cursor!!.moveToFirst()) {
//                            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                            thePath = cursor.getString(columnIndex)
//                        }
//                        cursor.close()

//                        var file =  File(thePath)
//                        println("*********${thePath}")


                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }
            }

            PHOTO_FROM_CAMERA -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        imgv_upload.setImageURI(saveUri)
                        val b = Bitmap.createBitmap(
                            imgv_upload.width,
                            imgv_upload.height,
                            Bitmap.Config.ARGB_8888
                        )
                        val c = Canvas(b)
                        imgv_upload.also {
                            it.layout(it.left, it.top, it.right, it.bottom)
                            it.draw(c)
                        }

                        val stream = ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.JPEG, 60, stream);

                        val byteArray = stream.toByteArray();
                        println("**************** ${byteArray.size}")

                        b.recycle()
                        bitmap = byteArray

                        if (bitmap != null) {
                            btn_send.isEnabled = true
                        }

//                        btn_send.setOnClickListener {

//                            val b = Bitmap.createBitmap(imgv_upload.width, imgv_upload.height, Bitmap.Config.ARGB_8888)
//                            val c = Canvas(b)
//                            imgv_upload.also {
//                                it.layout(it.left, it.top, it.right, it.bottom)
//                                it.draw(c)
//                            }
//
//                            val stream = ByteArrayOutputStream();
//                            b.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//
//                            val byteArray = stream.toByteArray();
//                            println("**************** ${byteArray.size}")
//
//                            b.recycle()
//                                bitmap = byteArray


//                            println("*********** ${file.absolutePath}")
////                            println("*********** ${file.path}")
////                            println("*********** ${file.toURI()}")
//
////                            val f = File(file.toURI())
////                            println("*********** ${f}")
////                            println("********${realUri}")
//                            var file =File(realUri)
//                            var requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), bitmap!!)
//
//                            var body: MultipartBody.Part = MultipartBody.Part.createFormData("img", file.getName(), requestFile)
//
//                            var descript:MultipartBody.Part = MultipartBody.Part.createFormData("reported_descript",et_reported_descript.text.toString())
//                            SubmitAPIFunction().sendSubmit(token,2,body,descript)
//                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.wtf("getImageResult", resultCode.toString())
                    }
                }

            }
        }
    }

    fun dailog() {
        AlertDialog.Builder(this)
            .setTitle("提醒")
            .setMessage("相機功能將無法使用")
    }
}
