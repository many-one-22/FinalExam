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

        // 로그인 버튼 클릭 이벤트
        binding.btnLoginSubmit.setOnClickListener {
            // trim()을 사용하여 사용자가 실수로 입력한 앞뒤 공백 제거
            val id = binding.etId.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 메인 화면, 마이페이지 등 앱 전체에서 사용자 아이디를 유지하기 위해 SharedPreferences로 저장
            val sharedID = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
            sharedID.edit().putString("USER_ID", id).apply()

            // 아이디나 비밀번호 중 하나가 입력이 되지 않았으면 둘 다 입력하라는 메시지 출력
            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
            } else {
                // 둘 다 입력된 것이 확인되었을 경우 로그인 성공 메시지를 띄우고 메인 화면으로 이동
                Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                // 사용자가 메인 화면에서 뒤로가기를 눌렀을 때 다시 로그인 화면이 나오는 것을 방지하기 위해 현재 액티비티 종료
                finish()
            }
        }
    }
}