package org.hyperskill.secretdiary

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val PREF_DIARY = "PREF_DIARY"

class MainActivity : AppCompatActivity() {

  @SuppressLint("CutPasteId")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    val sharedPreferences: SharedPreferences =
        getSharedPreferences(PREF_DIARY, Context.MODE_PRIVATE)

    val savedDiary = sharedPreferences.getString("KEY_DIARY_TEXT", "")
    findViewById<TextView>(R.id.tvDiary).text = savedDiary

    val editor = sharedPreferences.edit()
    val entries = ArrayDeque<String>()

    if (!savedDiary.isNullOrEmpty()) {
      val savedDiaryEntries = savedDiary.split("\n\n").toTypedArray()
      for (entry in savedDiaryEntries) {
        entries.addLast(entry)
      }
    }

    findViewById<Button>(R.id.btnSave).setOnClickListener {
      val userEntry = findViewById<EditText>(R.id.etNewWriting).text.toString()
      if (userEntry.isBlank()) {
        Toast.makeText(this, "Empty or blank input cannot be saved", Toast.LENGTH_SHORT).show()
      } else {
        val formattedEntry = CreateDiaryEntry(userEntry)
        entries.addFirst(formattedEntry)
        findViewById<TextView>(R.id.tvDiary).text = GenerateDiary(entries)
        editor
            .putString("KEY_DIARY_TEXT", findViewById<TextView>(R.id.tvDiary).text.toString())
            .apply()
        findViewById<EditText>(R.id.etNewWriting).text.clear()
      }
    }

    findViewById<Button>(R.id.btnUndo).setOnClickListener {
      val currentDiary = findViewById<TextView>(R.id.tvDiary).text.toString()
      AlertDialog.Builder(this)
          .setTitle("Remove last note")
          .setMessage(
              "Do you really want to remove the last writing? This operation cannot be undone!")
          .setPositiveButton("Yes") { _, _ ->
            if (!currentDiary.isBlank()) {
              entries.removeFirst()
              findViewById<TextView>(R.id.tvDiary).text = GenerateDiary(entries)
              editor
                  .putString("KEY_DIARY_TEXT", findViewById<TextView>(R.id.tvDiary).text.toString())
                  .apply()
            }
          }
          .setNegativeButton("No", null)
          .show()
    }
  }
}

fun GenerateDiary(entries: ArrayDeque<String>): String {
  return entries.joinToString("\n\n")
}

fun CreateDiaryEntry(newEntry: String): String {
  val userTimeZone = TimeZone.currentSystemDefault()
  val local: LocalDateTime = Clock.System.now().toLocalDateTime(userTimeZone)
  val timestamp: String =
      "${local.year}-${String.format("%02d", local.monthNumber)}-${String.format("%02d", local.dayOfMonth)} ${String.format("%02d", local.hour)}:${String.format("%02d", local.minute)}:${String.format("%02d", local.second)}"
  return timestamp + "\n" + newEntry
}
