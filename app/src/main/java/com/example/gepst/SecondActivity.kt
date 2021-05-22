package com.example.gepst

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gepst.databinding.SecondActivityBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_second.view.*
import kotlinx.android.synthetic.main.second_activity.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("CAST_NEVER_SUCCEEDS")
class SecondActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object{
        const val keyBitmap = "KeyBitmap"
    }
    private val binding by viewBinding(SecondActivityBinding::bind, R.id.sec_layout)
    private lateinit var imageView: ImageView //наша картинка
    private lateinit var backButton: Button //кнопка возврата
    private lateinit var saveButton: Button //кнопка сохранения
    private lateinit var leftButton: Button
    private lateinit var startImage: Button
    private lateinit var bitmap: Bitmap
    private lateinit var startBitmap: Bitmap
    private val rot = RotationImage()
    private val viewModel: ItemViewModel by viewModels()

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

    @SuppressLint("SourceLockedOrientationActivity") //я уже писал на эту тему, попытайтесь вспомнить
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // и про это писал
        nav_view.setNavigationItemSelectedListener(this) // для работы с NavigationView
        imageView = binding.imageMain
        val check: Int = intent.getIntExtra(MainActivity.check, 0) //получаем ключ
        //делаем проверку
        //получаем URI и присваеваем его нашей картинке
        // P.S. URI будет переделано в Bitmap, чтобы мы могли сразу работать картинкой

        if(check==100) {
            val imageUri: Uri? = intent.getStringExtra(MainActivity.keyUriG)?.toUri()
            imageView.setImageURI(imageUri)
            bitmap = (image_main.drawable as BitmapDrawable).bitmap
            imageView.setImageBitmap(bitmap)
        }
        if(check==42){
            val imageUri: Uri? = intent.getStringExtra(MainActivity.keyUriC)?.toUri()
            imageView.setImageURI(imageUri)
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.camtest)

        }
        startImage = binding.startImage
        leftButton = binding.rotLeft
        //работаем с кнопкой возврата
        binding.backBut.also { backButton = it }
        backButton.setOnClickListener{
            clearFragmentsFromContainer()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        startBitmap = bitmap
        saveButton = binding.save
        saveButton.setOnClickListener{
            saveMediaToStorage(bitmap)
            Toast.makeText(this, "Картинка сохранена", Toast.LENGTH_SHORT).show()
        }
        viewModel.selectedItem.observe(this, Observer {
            bitmap = it
        })

    }


    // здесь мы будем вызывать фрагменты для каждого алгоритма
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.id_rot -> {
//                selectScreen(RotFragment.TAG, RotFragment.newInstance())
                clearFragmentsFromContainer()
                startImage.visibility = View.VISIBLE
                leftButton.visibility = View.VISIBLE
                viewModel.selectItem(bitmap)
                leftButton.setOnClickListener{
                    val newBitmap: Bitmap = rot.rotateLeft(bitmap)
                    bitmap = newBitmap
                    imageView.setImageBitmap(bitmap)
                    saveButton.visibility = View.VISIBLE
                }
                startImage.setOnClickListener {
                    imageView.setImageBitmap(startBitmap)
                    saveButton.visibility = View.INVISIBLE
                }

            }
            R.id.id_cor -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
                selectScreen(ColorCorrectFragment.TAG, ColorCorrectFragment.newInstance())

            }
            R.id.id_scal -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
                selectScreen(ScalingImageFragment.TAG, ScalingImageFragment.newInstance())
            }
            R.id.id_rec -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
                selectScreen(FaceRecognitionFragment.TAG, FaceRecognitionFragment.newInstance())
            }
            R.id.id_Dr -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
            }
            R.id.id_ret -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
            }
            R.id.id_mas -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
            }
            R.id.id_Filt -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
            }
            R.id.id_Cub -> {
                startImage.visibility = View.INVISIBLE
                leftButton.visibility = View.INVISIBLE
            }

        }

        return true
    }
    private fun selectScreen(tag:String, fragment: Fragment){
        supportFragmentManager.commit {
            val activeFragment = findActiveFragment()
            val target = supportFragmentManager.findFragmentByTag(tag)

            if(activeFragment != null && target != null && activeFragment == target) return@commit

            if(activeFragment != null){
                hide(activeFragment)
            }
            if(target==null){
                add(R.id.but_layout,fragment,tag)
            }else{
                show(target)
            }
        }


    }

    private fun findActiveFragment() = supportFragmentManager.fragments.find { it.isVisible }

    private fun FragmentActivity.clearFragmentsFromContainer() {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            supportFragmentManager.beginTransaction().remove(fragment).commit()
        }
        //Remove all the previous fragments in back stack
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //getting the contentResolver
            contentResolver?.also { resolver ->


                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            //These for devices running on android < Q
            //So I don't think an explanation is needed here
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            //Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

        }
    }
}