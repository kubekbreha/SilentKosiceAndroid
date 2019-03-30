package sk.spacecode.silentkosice

import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import java.io.IOException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_record.*


class RecordActivity : AppCompatActivity() {

    private var i = 0
    var handler: Handler? = null
    lateinit var mRecorder : MediaRecorder
    private var mEMA = 0.0
    private val EMA_FILTER = 0.6

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

//        output = externalCacheDir?.absolutePath + "/${convertDateToFilename()}.mp3"
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//        mediaRecorder.setOutputFile(output)
//
        button_start_recording.setOnClickListener {
//            try {
//                mediaRecorder.prepare()
//                mediaRecorder.start()
//                state = true
//                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
//            } catch (e: IllegalStateException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
        }

        button_stop_recording.setOnClickListener {
//            if (state) {
//                mediaRecorder.stop()
//                mediaRecorder.release()
//                state = false
//            } else {
//                Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
//            }
            val tsLong = System.currentTimeMillis() / 1000
            pushDataRecord(tsLong.toString(), "70","+4210902130280", "37.90909", "37.90909")
        }


        database = FirebaseDatabase.getInstance().reference


        mRecorder =  MediaRecorder()
        handler = Handler()
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder.setOutputFile("/dev/null")
        try {
            mRecorder.prepare()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mRecorder.start()

        handler!!.postDelayed(runnable, 100)

    }

    private fun pushDataRecord(timestamp: String, decibel: String, userNumber: String?, lat: String?, long: String?) {
        val data = DecibelData(timestamp, decibel, userNumber, lat, long)
        val newRef = database.child(userNumber).child("records").child(timestamp).setValue(data)
    }


    var runnable: Runnable = object : Runnable {
        override fun run() {
            updateTv()
            handler!!.postDelayed(this, 0)
        }
    }

    fun updateTv() {
        textView_currentDB.text = java.lang.Double.toString(getAmplitudeEMA()) + " dB"
        progressBar_volume.progress = getAmplitudeEMA().toInt()
    }

    fun soundDb(ampl: Double): Double {
        return 20 * Math.log10(getAmplitudeEMA() / ampl)
    }

    fun getAmplitude(): Double {
        return if (mRecorder != null)
            mRecorder.maxAmplitude.toDouble()
        else
            0.0
    }

    fun getAmplitudeEMA(): Double {
        val amp = getAmplitude()
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA
        return mEMA
    }
}
