package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.MypageActivity
import com.example.finalexam.databinding.ActivityBookListBinding

class BookListActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.booklist)

        // 하단 메뉴바 초기 세팅
        binding.bottomNavigation.selectedItemId = R.id.nav_list

        // 하단 메뉴바 탭 이동 리스너
        // 액티비티가 중복으로 쌓여 메모리가 낭비되는 것을 방지하기 위해 CLEAR_TOP, SINGLE_TOP 플래그 추가
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
                    // 이미 현재 페이지이므로 아무 작업도 하지 않음
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

        val dummyBooks = listOf(
            Book(1, "관계 중심의 사고법 쉽게 배우는 알고리즘", "홍길동", "25,000원", "한빛아카데미", R.drawable.books_book1),
            Book(2, "난생처음 MySQL", "이순신", "28,000원", "한빛아카데미", R.drawable.books_book2),
            Book(3, "쉽게 배우는 PHP 웹 프로그래밍", "김유신", "30,000원", "한빛아카데미", R.drawable.books_book3),
            Book(4, "미라클 HTML5 + CSS3 + 자바스크립트", "강감찬", "22,000원", "한빛아카데미", R.drawable.books_book4)
        )

        // RecyclerView 레이아웃 매니저 설정
        binding.rvBookList.layoutManager = LinearLayoutManager(this)

        // RecyclerView에 어댑터 연결 및 클릭 이벤트 처리
        binding.rvBookList.adapter = BookAdapter(dummyBooks, onItemClick = { book ->

            // AlterDialog로 도서 상세 설명으로 이동할 지 말 지 확인
            AlertDialog.Builder(this)
                .setTitle("도서 선택")
                .setMessage("${book.title}을(를) 선택하셨습니다. 상세 화면으로 이동할까요?")
                .setPositiveButton("확인") { _, _ ->

                    // 선택된 도서 데이터를 Intent에 담아 BookDetailActivity로 전달
                    val intent = Intent(this, BookDetailActivity::class.java).apply {
                        putExtra("BOOK_TITLE", book.title)
                        putExtra("BOOK_AUTHOR", book.author)
                        putExtra("BOOK_PRICE", book.price)
                        putExtra("BOOK_PUBLISHER", book.publisher)
                        putExtra("BOOK_IMAGE", book.imageResId)
                    }
                    startActivity(intent)

                }
                .setNegativeButton("취소", null)
                .show()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        // 다른 액티비티 갔다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 상태가 엉뚱한 곳에 가 있는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_list
    }
}