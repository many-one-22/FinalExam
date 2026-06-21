package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.MypageActivity
import com.example.finalexam.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 하단 메뉴바 초기 세팅
        binding.bottomNavigation.selectedItemId = R.id.nav_cart

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
                    // 이미 현재 페이지이므로 아무 작업도 하지 않음
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

        // CartManager에서 현재 담긴 도서 리스트를 가져옴
        val cartItems = CartManager.getCartItems()

        binding.rvCartList.layoutManager = LinearLayoutManager(this)

        // 도서 목록 화면에서 만들었던 BookAdapter를 장바구니에서도 재사용
        // 장바구니 항목을 눌러도 상세 화면으로 갈 수 있도록 Intent 연결
        binding.rvCartList.adapter = BookAdapter(cartItems, onItemClick = { book ->
            val intent = Intent(this, BookDetailActivity::class.java).apply {
                putExtra("BOOK_TITLE", book.title)
                putExtra("BOOK_AUTHOR", book.author)
                putExtra("BOOK_PRICE", book.price)
                putExtra("BOOK_PUBLISHER", book.publisher)
                putExtra("BOOK_IMAGE", book.imageResId)
            }
            startActivity(intent)
        },
            onItemLongClick = { book ->
                // 길게 클릭하면 삭제 여부를 물어봄
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("장바구니 삭제")
                    .setMessage("${book.title}을(를) 장바구니에서 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ ->
                        // 카트에서 데이터 삭제
                        CartManager.removeBook(book)
                        // 화면을 새로고침하여 수정된 사항 반영
                        binding.rvCartList.adapter?.notifyDataSetChanged()
                        Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        )

        // 주문하기 버튼 클릭 시 주문서 작성 페이지로 이동
        binding.btnGoToOrder.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른 액티비티를 돌다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 선택 상태가 꼬이는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_cart
    }
}