package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 툴바 설정 (기본 왼쪽 텍스트 숨기기)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 2. 바텀 네비게이션 현재 탭 선택 및 리스너 설정
        binding.bottomNavigation.selectedItemId = R.id.nav_cart
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
                R.id.nav_cart -> true
                else -> false
            }
        }

        // 3. CartManager에서 담긴 데이터 가져와서 RecyclerView에 연결
        val cartItems = CartManager.getCartItems()

        binding.rvCartList.layoutManager = LinearLayoutManager(this)
        // 기존 BookAdapter 재사용, 클릭 시 상세화면 가도록 연결
        binding.rvCartList.adapter = BookAdapter(cartItems) { book ->
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("BOOK_TITLE", book.title)
                putExtra("BOOK_AUTHOR", book.author)
                putExtra("BOOK_PRICE", book.price)
                putExtra("BOOK_PUBLISHER", book.publisher)
                putExtra("BOOK_IMAGE", book.imageResId)
            }
            startActivity(intent)
        }

        // CartActivity.kt의 onCreate 내부 맨 밑에 추가
        binding.btnGoToOrder.setOnClickListener {
            if (CartManager.getCartItems().isEmpty()) {
                android.widget.Toast.makeText(this, "장바구니가 비어있습니다.", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                // 주문서 액티비티로 이동 (OrderActivity 생성 후 주석 해제)
                val intent = Intent(this, OrderActivity::class.java)
                startActivity(intent)
            }
        }
    }
}