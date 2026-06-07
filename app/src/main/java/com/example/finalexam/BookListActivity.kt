package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.databinding.ActivityBookListBinding

class BookListActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.booklist)

        // 🌟 [추가] 도서목록 화면이므로 하단바에서 '도서목록' 탭이 선택되어 있도록 설정
        binding.bottomNavigation.selectedItemId = R.id.nav_list

        // 🌟 [추가] 도서목록 화면의 하단바 클릭 이벤트
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
                    // 이미 도서목록이므로 아무 작업도 하지 않음
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

        // 2. 최소 4개 이상의 더미 데이터 생성 (과제 필수 조건)
        val dummyBooks = listOf(
            Book(1, "처음 만나는 코틀린", "홍길동", "25,000원", "새싹출판사", R.drawable.ic_launcher_foreground), // ⚠️ 실제 이미지 리소스로 교체 권장
            Book(2, "안드로이드 미스터리", "이순신", "28,000원", "장군출판사", R.drawable.ic_launcher_foreground),
            Book(3, "모바일 소프트웨어 공학", "김유신", "30,000원", "대학서림", R.drawable.ic_launcher_foreground),
            Book(4, "자바 없는 안드로이드 세상", "강감찬", "22,000원", "고려미디어", R.drawable.ic_launcher_foreground)
        )

        // 3. RecyclerView 설정 (LinearLayoutManager 사용)
        binding.rvBookList.layoutManager = LinearLayoutManager(this)

        // 어댑터 연결 및 클릭 이벤트 처리 (Ch 08)

        binding.rvBookList.adapter = BookAdapter(dummyBooks) { book ->
            // 가산점 항목: AlertDialog 활용 (Ch 15)
            AlertDialog.Builder(this)
                .setTitle("도서 선택")
                .setMessage("${book.title}을(를) 선택하셨습니다. 상세 화면으로 이동할까요?")
                .setPositiveButton("확인") { _, _ ->

                    // 💡 Intent를 생성하여 선택된 도서의 데이터를 전달 (Ch 13, 과제 필수 조건)
                    val intent = Intent(this, BookDetailActivity::class.java).apply {
                        putExtra("BOOK_TITLE", book.title)
                        putExtra("BOOK_AUTHOR", book.author)
                        putExtra("BOOK_PRICE", book.price)
                        putExtra("BOOK_PUBLISHER", book.publisher)
                        putExtra("BOOK_IMAGE", book.imageResId)
                    }
                    startActivity(intent) // 상세 화면으로 이동

                }
                .setNegativeButton("취소", null)
                .show()
        }
    }

    // 툴바의 뒤로가기 버튼 클릭 시 액티비티 종료 (Ch 13 화면 이동)
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}