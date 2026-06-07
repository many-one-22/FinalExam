package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.finalexam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.app_name)

        val userId = intent.getStringExtra("USER_ID") ?: "로그인하지 않았습니다."
        binding.tvWelcome.text = "${userId}님 환영합니다!"

        binding.btnGoToList.setOnClickListener {
            val intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "도서 목록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        // 바텀 네비게이션 클릭 이벤트
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 🌟 "이미 홈 화면입니다" 토스트 제거하고 바로 true 반환
                    true
                }
                R.id.nav_list -> {
                    val intent = Intent(this, BookListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_cart -> { // 🌟 추가
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // 1. SharedPreferences 마지막 본 도서 로드
        val sharedPref = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        val lastViewedBook = sharedPref.getString("LAST_VIEWED_BOOK", "없음")
        binding.tvDescription.text = "마지막으로 본 도서: $lastViewedBook"

        // 🌟 2. 도서목록에서 '이전키'로 돌아왔을 때 하단바 선택 상태를 '홈'으로 강제 동기화
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_setting -> {
                Toast.makeText(this, "설정 기능은 현재 준비 중입니다.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}