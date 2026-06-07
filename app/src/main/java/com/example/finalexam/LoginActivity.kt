package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalexam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 클릭 이벤트 처리 (Ch 08)
        binding.btnLoginSubmit.setOnClickListener {
            val id = binding.etId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 1. 공백 체크 (Ch 15 Toast 활용)
            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

                // 2. 메인 화면으로 이동하면서 입력한 ID 데이터를 함께 전달 (Ch 13)
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("USER_ID", id)
                }
                startActivity(intent)

                // 로그인 완료 후 뒤로가기를 눌렀을 때 로그인 창이 다시 뜨지 않도록 종료
                finish()
            }
        }
    }
}