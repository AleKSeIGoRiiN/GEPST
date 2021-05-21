package com.example.gepst

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gepst.databinding.ActivityMainBinding
import com.example.gepst.databinding.FragmentRotBinding
import com.example.gepst.databinding.SecondActivityBinding


@Suppress("CAST_NEVER_SUCCEEDS")
class RotFragment : Fragment(R.layout.fragment_rot) {
    private val binding by viewBinding(FragmentRotBinding::bind, R.id.fragmentRot)
    private lateinit var butRot: Button
    private val rot = RotationImage()

    companion object {
        val TAG = RotFragment::class.java.simpleName
        val key = "activity"
        fun newInstance() = RotFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setFragmentResultListener("requestKey") { requestKey, bundle ->
//            val bitmap: Bitmap = bundle.getString("bundleKey") as Bitmap
//            butRot.setOnClickListener {
//                val newBitmap = rot.rotate(bitmap)
//
//            }

        }

}