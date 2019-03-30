package sk.spacecode.silentkosice

import android.media.MediaRecorder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_record.*
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RecordActivity : AppCompatActivity() {
    private var output = ""
    private var mediaRecorder = MediaRecorder()
    private var state = false
    private var recordingStopped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        output = externalCacheDir?.absolutePath + "/${convertDateToFilename()}.mp3"
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder.setOutputFile(output)

        button_start_recording.setOnClickListener {
            try {
                mediaRecorder.prepare()
                mediaRecorder.start()
                state = true
                Toast.makeText(this, "Recording started!", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        button_stop_recording.setOnClickListener {
            if (state) {
                mediaRecorder.stop()
                mediaRecorder.release()
                state = false
            } else {
                Toast.makeText(this, "You are not recording right now!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertDateToFilename(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss")

        return current.format(formatter)
    }

}
