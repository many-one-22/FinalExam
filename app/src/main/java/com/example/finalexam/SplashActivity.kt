package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalexam.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 메인 액티비티에 접근하기 위해 Looper.getMainLooper()를 사용하는 Handler 생성
        Handler(Looper.getMainLooper()).postDelayed({
            // 2.5초 대기 후 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // 사용자가 뒤로가기 버튼을 눌렀을 때 스플래시 화면이 다시 나오지 않도록 액티비티 완전 종료
            finish()
        }, 2500)

        // 상단바 및 하단바 영역과 앱의 UI가 겹치지 않도록 시스템 여백을 동적으로 계산하여 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}