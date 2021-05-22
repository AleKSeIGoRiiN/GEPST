package com.example.gepst

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


@Suppress("CAST_NEVER_SUCCEEDS")
class RotFragment : Fragment(R.layout.fragment_rot) {
//    private val binding by viewBinding(FragmentRotBinding::bind, R.id.fragmentRot)
    private lateinit var butRot: Button
    private val rot = RotationImage()
    private val viewModel: ItemViewModel by activityViewModels()
    lateinit var bitmap: Bitmap


    companion object {
        val TAG = RotFragment::class.java.simpleName
        fun newInstance() = RotFragment()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        val root = inflater.inflate(R.layout.fragment_rot, container, false)
        viewModel.selectedItem.observe(viewLifecycleOwner, Observer {
            bitmap = it
        })

        butRot = root.findViewById<Button>(R.id.rotLeft)
        butRot.setOnClickListener{
            val newBitmap: Bitmap = rot.rotate(bitmap)
            viewModel.selectItem(newBitmap)
        }
        return root
    }
}