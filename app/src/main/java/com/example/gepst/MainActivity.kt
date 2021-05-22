package com.example.gepst

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gepst.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind, R.id.mainLayout)

    companion object{                       // констатные значения, в основмном ключи к файлам для их получения и передачи
        const val keyUriG = "keyUriG"
        const val keyUriC = "keyUriC"
        const val check = "check"
        const val pickImage = 100
        const val makeImage = 42
        const val PERMISSION_CODE_READ = 1001
    }

    var name: ImageView? = null         // переменные и функция для мерцания логотипа. Сделал через массив
    var counter: Int = 0
    var timer: Timer? = null
    val imageArray:IntArray = intArrayOf(R.drawable.lname, R.drawable.dname)

    private fun flicker(){
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        name?.setImageResource(imageArray[counter])
                        counter++
                        if (counter == 2) counter = 0
                    }
                }
            }, 0, 1000)
    }

    var imageUri: Uri? = null

    override fun onBackPressed(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Серьёзно?!")
        builder.setMessage("Вы хотите закрыть приложение?")
        builder.setPositiveButton("Да") { _, _: Int ->
            finish()
        }
        builder.setNegativeButton("Нет") { _, _: Int -> }
        builder.show()
    }
    private fun checkPermissionForImage() {
        if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE_READ)
        }
    }

        @SuppressLint("SourceLockedOrientationActivity")  //здесь просто хранится описание некоторый недочётов
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // запрет на поворот экран


        name = binding.namE // мерцание запускаем
        flicker()
            checkPermissionForImage()

            // получение картинки из галереи и камеры
            //сначала мы отправляемся к памяти телефона
        binding.image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        binding.camera.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, makeImage)

        }
    }
    // а уже в этом потоке возврощаемся обратно из памяти к приложению, неся с собой файлы, которые мы запросили
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            //проверяем, использовали ли мы галерею или камеру
            //после проверки получаем URI, конвертируем его в строку и отправляем его на второй экран.
            //также отправлям ключ, чтобы для проверки, что получать
            //переходим на второй экран
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data 
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(keyUriG, imageUri.toString())
            intent.putExtra(check, pickImage)
            startActivity(intent)
        }
        if (resultCode == RESULT_OK && requestCode == makeImage){
            val takenImage = data?.extras?.get("data") as Bitmap
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra(keyUriC, takenImage.toString())
            intent.putExtra(check, makeImage)
            startActivity(intent)
        }
    }

}

