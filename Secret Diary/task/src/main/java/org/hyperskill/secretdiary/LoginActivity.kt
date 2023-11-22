package org.hyperskill.secretdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

const val DEFAULT_PIN = "1234"

class LoginActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		val etPinView = findViewById<EditText>(R.id.etPin)

		findViewById<Button>(R.id.btnLogin).setOnClickListener {
			val userEnteredPin = etPinView.text.toString()
			if (userEnteredPin == DEFAULT_PIN) {
				val intent = Intent(this, MainActivity::class.java)
				startActivity(intent)
				finish()
			}else {
				etPinView.error = "Wrong PIN!"
			}
		}
	}
}