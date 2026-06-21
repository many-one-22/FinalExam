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

    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 하단 메뉴바 초기 세팅
        binding.bottomNavigation.selectedItemId = R.id.nav_mypage

        // 로그인 시 SharedPreferences에 저장했던 아이디를 불러와서 프로필 이름으로 설정
        val sharedID = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        val userId = sharedID.getString("USER_ID","고객")
        binding.tvProfileName.text = "${userId}${getString(R.string.profile_suffix)}"

        // 하단 메뉴바 탭 이동 리스너
        // 화면이 무한히 겹쳐서 메모리가 낭비되는 것을 막기 위해 CLEAR_TOP, SINGLE_TOP 플래그 적용
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
                    val intent = Intent(this, CartActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_order -> {
                    val intent = Intent(this, OrderActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                R.id.nav_mypage -> {
                    // 이미 현재 페이지이므로 아무 작업도 하지 않음
                    true
                }
                else -> false
            }
        }

        // 로그아웃 버튼 클릭 이벤트 처리
        binding.btnLogout.setOnClickListener {
            Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_SHORT).show()

            // 로그아웃 시 기존의 화면 이동 기록을 완전히 초기화하여 뒤로가기 버튼으로 앱에 재진입하는 것을 차단
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른 액티비티를 돌다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 선택 상태가 꼬이는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_mypage
    }
}