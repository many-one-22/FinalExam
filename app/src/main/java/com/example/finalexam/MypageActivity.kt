package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalexam.BookListActivity
import com.example.finalexam.CartActivity
import com.example.finalexam.LoginActivity
import com.example.finalexam.MainActivity
import com.example.finalexam.R
import com.example.finalexam.databinding.ActivityMypageBinding

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 툴바 설정 및 뒤로가기 버튼 활성화
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 2. 인텐트로 넘어온 회원 ID 반영
        val userId = intent.getStringExtra("USER_ID") ?: "고객"
        binding.tvProfileName.text = "${userId}${getString(R.string.profile_suffix)}"

        // 3. 하단바 리스너 설정 (마이페이지는 상단 옵션 메뉴로 진입하므로 선택 ID 지정을 생략하거나 홈/목록 이동 처리)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_list -> {
                    val intent = Intent(this, BookListActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_cart -> {
                    val intent = Intent(this, CartActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // 4. 로그아웃 버튼 구현 (모든 액티비티 스택 파괴 후 로그인 화면으로 이동)
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java).apply {
                // 🌟 로그아웃이므로 이전의 홈, 목록, 장바구니 기록을 백스택에서 통째로 날려버립니다.
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    // 툴바의 뒤로가기 화살표 클릭 시 현재 마이페이지 종료
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}