package sk.spacecode.silentkosice

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var codeSent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        button_getVerificationCode.setOnClickListener { sendVerificationCode() }
        button_signIn.setOnClickListener { verifySignInCode() }
    }

    private fun verifySignInCode() {
        val code = edit_textCode.text.toString()
        var credential: PhoneAuthCredential? = null

        try {
            credential = PhoneAuthProvider.getCredential(codeSent, code)
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "Incorrect Verification Code ", Toast.LENGTH_LONG
            ).show()
        }

        credential?.let {
            signInWithPhoneAuthCredential(credential)
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login Successfull", Toast.LENGTH_LONG
                    ).show()
                    goToRecordActivity()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            applicationContext,
                            "Incorrect Verification Code ", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun sendVerificationCode() {

        val phone = edit_textPhone.text.toString()

        if (phone.isEmpty()) {
            edit_textPhone.error = "Phone number is required"
            edit_textPhone.requestFocus()
            return
        }

        if (phone.length < 10) {
            edit_textPhone.error = "Please enter a valid phone"
            edit_textPhone.requestFocus()
            return
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    private fun goToRecordActivity() {
        val intent = Intent(this, RecordActivity::class.java)
        startActivity(intent)
    }


    private var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Toast.makeText(
                    applicationContext,
                    "Number already verified.", Toast.LENGTH_SHORT
                ).show()
                goToRecordActivity()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(
                    applicationContext,
                    "Code sending failed.", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCodeSent(s: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(s, forceResendingToken)

                Toast.makeText(
                    applicationContext,
                    "Code send.", Toast.LENGTH_LONG
                ).show()
                codeSent = s!!
            }
        }
}
