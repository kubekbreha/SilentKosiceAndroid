package sk.spacecode.silentkosice

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_record.*


class RecordActivity : AppCompatActivity() {

    private var i = 0
    var handler: Handler? = null

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
            val tsLong = System.currentTimeMillis() / 1000
            pushDataRecord(tsLong.toString(), "70","+4210902130280", "37.90909", "37.90909")

        }
//
//        button_stop_recording.setOnClickListener {
//            if (state) {
//                mediaRecorder.stop()
//                mediaRecorder.release()
//                state = false
//            } else {
//                Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
//            }
//        }


        database = FirebaseDatabase.getInstance().reference


//
//        handler = Handler()
//        var mRecorder = MediaRecorder()
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//        mRecorder.setOutputFile("/dev/null")
//        try {
//            mRecorder.prepare()
//        } catch (e: IllegalStateException) {
//            e.printStackTrace()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        mRecorder.start()
//
//        handler!!.postDelayed(runnable, 0)

        textView_profile.setOnClickListener {
            val profileIntent = Intent(this@RecordActivity, ProfileActivity::class.java)
            this@RecordActivity.startActivity(profileIntent)
        }
    }

    private fun pushDataRecord(timestamp: String, decibel: String, userNumber: String?, lat: String?, long: String?) {
        val data = DecibelData(timestamp, decibel, userNumber, lat, long)
        val newRef = database.child(userNumber!!).child("records").child(timestamp).setValue(data)
    }


    var runnable: Runnable = object : Runnable {
        override fun run() {
            i++
            textView_currentDB.text = (i.toString())
            handler!!.postDelayed(this, 0)
        }
    }


}
