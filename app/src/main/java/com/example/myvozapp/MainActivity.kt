package com.example.myvozapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.speech.SpeechRecognizer
import android.widget.EditText
import android.widget.ImageView

import android.Manifest;
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.view.MotionEvent
import android.widget.Toast
import androidx.annotation.RequiresPermission.Read
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private var speechRecongnizer: SpeechRecognizer? = null
    private var editText : EditText? = null
    private var ButtonMicrofone : ImageView? = null

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                RecordAudioRequestCode
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.textofalado)
        ButtonMicrofone = findViewById(R.id.buttonMic)
        speechRecongnizer = SpeechRecognizer.createSpeechRecognizer(this)

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        speechRecongnizer!!.setRecognitionListener(object :RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                editText!!.setText("")
                editText!!.setHint("Escutando...")
            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(bundle: Bundle?) {
                ButtonMicrofone!!.setImageResource(R.drawable.mic_off)
                val data = bundle!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                editText!!.setText(data!![0])
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })

        ButtonMicrofone!!.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                speechRecongnizer!!.stopListening()
            }
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                ButtonMicrofone!!.setImageResource(R.drawable.mic_on)
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    checkPermissions()
                } else {
                    speechRecongnizer!!.startListening(speechRecognizerIntent)
                }
            }
            false
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        speechRecongnizer!!.destroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RecordAudioRequestCode
            && grantResults.isNotEmpty()
        ){
            Toast.makeText(this, "Permissao concedida",Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        const val RecordAudioRequestCode = 1
    }
}

