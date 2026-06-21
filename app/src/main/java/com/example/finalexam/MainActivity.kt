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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.app_name)

        // 로그인 시 SharedPreferences에 저장해둔 사용자 아이디를 불러와 환영합니다 메시지 출력
        val sharedID = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        val userId = sharedID.getString("USER_ID", "고객")
        binding.tvWelcome.text = "${userId}님 환영합니다!"

        // 도서 목록 화면으로 이동하는 메인 버튼 이벤트
        binding.btnGoToList.setOnClickListener {
            val intent = Intent(this, BookListActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "도서 목록 화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
        }

        // 하단 메뉴바 탭 이동 리스너
        // 화면이 무한히 겹쳐서 메모리가 낭비되는 것을 막기 위해 CLEAR_TOP, SINGLE_TOP 플래그 적용
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 이미 현재 페이지이므로 아무 작업도 하지 않음
                    true
                }
                R.id.nav_list -> {
                    val intent = Intent(this, BookListActivity::class.java).apply{
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
                    val intent = Intent(this, MypageActivity::class.java).apply{
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    // 액티비티가 다시 화면에 나타날 때마다 UI 갱신을 위해 onResume 활용
    override fun onResume() {
        super.onResume()

        // BookDetailActivity에서 SharedPreferences에 저장한 마지막으로 본 도서 내역을 불러옴
        val sharedPref = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        val lastViewedBook = sharedPref.getString("LAST_VIEWED_BOOK", "없음")
        binding.tvDescription.text = "마지막으로 본 도서: $lastViewedBook"

        // 다른 액티비티를 돌다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 선택 상태가 꼬이는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    // 우측 상단 옵션 메뉴(설정) 뷰 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 옵션 메뉴 클릭 이벤트 처리
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