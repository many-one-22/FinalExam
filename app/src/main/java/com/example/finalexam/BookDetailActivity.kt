package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.finalexam.MypageActivity
import com.example.finalexam.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 툴바 설정 및 뒤로가기 버튼 활성화 (Ch 14)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.bookmoreview)

        // 2. Intent로 전달받은 데이터 가져오기 (Ch 13 액티비티 컴포넌트)
        // 안전한 꺼내기를 위해 기본값(Default Value) 설정 및 Null-safety 유의
        val title = intent.getStringExtra("BOOK_TITLE") ?: "정보 없음"
        val author = intent.getStringExtra("BOOK_AUTHOR") ?: "정보 없음"
        val price = intent.getStringExtra("BOOK_PRICE") ?: "0원"
        val publisher = intent.getStringExtra("BOOK_PUBLISHER") ?: "정보 없음"
        val imageResId = intent.getIntExtra("BOOK_IMAGE", R.drawable.ic_launcher_foreground)

        // 3. 받아온 데이터를 레이아웃 뷰에 적용 (String Template 활용)
        binding.ivDetailCover.setImageResource(imageResId)
        binding.tvDetailTitle.text = title
        binding.tvDetailAuthor.text = "저자: $author"
        binding.tvDetailPublisher.text = "출판사: $publisher"
        binding.tvDetailPrice.text = price

        // onCreate 맨 아래에 추가
        val sharedPref = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LAST_VIEWED_BOOK", title)
            apply() // 비동기로 안전하게 저장
        }

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
                    finish()
                    true
                }
                R.id.nav_cart -> { // 🌟 추가
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_order -> {
                    val intent = Intent(this, OrderActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_mypage -> {
                    val intent = Intent(this, MypageActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // BookDetailActivity.kt의 onCreate 내부 맨 밑에 추가
        binding.btnAddToCart.setOnClickListener {
            // 현재 화면의 도서 정보로 임시 Book 객체 생성
            val currentBook = Book(
                id = intent.getIntExtra("BOOK_IMAGE", 0), // 임시 ID 대용
                title = title,
                author = author,
                price = price,
                publisher = publisher,
                imageResId = imageResId
            )

            // 🌟 장바구니 저장소에 추가
            CartManager.addBook(currentBook)

            // Ch 15 대화상자(AlertDialog) 활용하여 안내 (완성도 확보)
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("장바구니 담기 완료")
                .setMessage("${title}이(가) 장바구니에 담겼습니다. 장바구니 화면으로 이동하시겠습니까?")
                .setPositiveButton("이동") { _, _ ->
                    // 장바구니 액티비티로 이동 (CartActivity 생성 후 주석 해제)
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("쇼핑 계속하기", null)
                .show()
        }
    }

    // 툴바의 뒤로가기 버튼(Up Button)을 눌렀을 때 현재 액티비티 종료
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}