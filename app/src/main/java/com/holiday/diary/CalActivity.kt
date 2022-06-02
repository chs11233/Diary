package com.holiday.diary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.holiday.diary.databinding.ActivityCalBinding
import java.io.FileInputStream
import java.io.FileOutputStream

class CalActivity : AppCompatActivity() {
    private var mBinding: ActivityCalBinding? = null
    private val binding get() = mBinding!!

    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            binding.diaryTextView.visibility = View.VISIBLE
            binding.diaryContent.visibility = View.INVISIBLE
            binding.updateBtn.visibility = View.INVISIBLE
            binding.deleteBtn.visibility = View.INVISIBLE
            binding.diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            checkDay(year, month, dayOfMonth, userID)
        }

//        binding.contextEditText.setOnClickListener {
//            startActivity(Intent(this,CreateActivity::class.java))
//        }
    }

    // 달력 내용 조회, 수정
    fun checkDay(cYear: Int, cMonth: Int, cDay: Int, userID: String) {
        //저장할 파일 이름설정
        fname = "" + userID + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt"

        var fileInputStream: FileInputStream
        try {
            fileInputStream = openFileInput(fname)
            val fileData = ByteArray(fileInputStream.available())
            fileInputStream.read(fileData)
            fileInputStream.close()
            str = String(fileData)
            binding.contextEditText.visibility = View.INVISIBLE
            binding.diaryContent.visibility = View.VISIBLE
            binding.diaryContent.text = str
            binding.updateBtn.visibility = View.VISIBLE
            binding.deleteBtn.visibility = View.VISIBLE
            binding. updateBtn.setOnClickListener {
                binding.contextEditText.visibility = View.VISIBLE
                binding.diaryContent.visibility = View.INVISIBLE
                binding.contextEditText.setText(str)
                binding.updateBtn.visibility = View.INVISIBLE
                binding.deleteBtn.visibility = View.INVISIBLE
                binding.diaryContent.text = binding.contextEditText.text
            }
            binding.deleteBtn.setOnClickListener {
                binding.diaryContent.visibility = View.INVISIBLE
                binding.updateBtn.visibility = View.INVISIBLE
                binding.deleteBtn.visibility = View.INVISIBLE
                binding.contextEditText.setText("")
                binding.contextEditText.visibility = View.VISIBLE
                removeDiary(fname)
            }
            if (binding.diaryContent.text == null) {
                binding.diaryContent.visibility = View.INVISIBLE
                binding.updateBtn.visibility = View.INVISIBLE
                binding.deleteBtn.visibility = View.INVISIBLE
                binding.diaryTextView.visibility = View.VISIBLE
                binding.contextEditText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // 달력 내용 제거
    @SuppressLint("WrongConstant")
    fun removeDiary(readDay: String?) {
        var fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS)
            val content = ""
            fileOutputStream.write(content.toByteArray())
            fileOutputStream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}