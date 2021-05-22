package com.example.gepst

import androidx.fragment.app.Fragment

class FaceRecognitionFragment:Fragment(R.layout.fragment_face) {
    companion object{
        val TAG = FaceRecognitionFragment::class.java.simpleName

        fun newInstance() = FaceRecognitionFragment()
    }
}