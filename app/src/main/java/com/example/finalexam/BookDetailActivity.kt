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
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바, BookList의 하위 페이지이므로 뒤로가기 버튼을 툴바에 추가
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvToolbarTitle.text = getString(R.string.bookmoreview)

        // BookList에서 Intent로 쏴준 책 데이터들 받아오기
        // 가끔 데이터가 누락이 돼서 앱이 튕기는 것을 방지하기 위해 엘비스 연산자로 방어 코드 작성
        val title = intent.getStringExtra("BOOK_TITLE") ?: "정보 없음"
        val author = intent.getStringExtra("BOOK_AUTHOR") ?: "정보 없음"
        val price = intent.getStringExtra("BOOK_PRICE") ?: "0원"
        val publisher = intent.getStringExtra("BOOK_PUBLISHER") ?: "정보 없음"
        val imageResId = intent.getIntExtra("BOOK_IMAGE", R.drawable.ic_launcher_foreground)

        // 받아온 안전한 데이터들을 화면에 출력
        binding.ivDetailCover.setImageResource(imageResId)
        binding.tvDetailTitle.text = title
        binding.tvDetailAuthor.text = "저자: $author"
        binding.tvDetailPublisher.text = "출판사: $publisher"
        binding.tvDetailPrice.text = price

        // 메인 화면에서 '마지막으로 본 도서'를 띄워주기 위해 SharedPreferences에 책 제목 저장
        val sharedPref = getSharedPreferences("BookMarketPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LAST_VIEWED_BOOK", title)
            apply()
        }

        // 하단 메뉴바 초기 세팅
        binding.bottomNavigation.selectedItemId = R.id.nav_list

        // 하단 메뉴바 탭 이동 리스너
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

        // 장바구니 담기 버튼 이벤트
        binding.btnAddToCart.setOnClickListener {
            val currentBook = Book(
                id = intent.getIntExtra("BOOK_IMAGE", 0),
                title = title,
                author = author,
                price = price,
                publisher = publisher,
                imageResId = imageResId
            )

            // CartManager에 저장
            CartManager.addBook(currentBook)

            // AlertDialog로 장바구니로 바로 갈 지 더 구경할 지 확인창을 띄움
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("장바구니 담기 완료")
                .setMessage("${title}이(가) 장바구니에 담겼습니다. 장바구니 화면으로 이동하시겠습니까?")
                .setPositiveButton("이동") { _, _ ->
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("쇼핑 계속하기", null)
                .show()
        }
    }

    // 툴바 상단에 달아놓은 뒤로가기 버튼 눌렀을 때 실제로 화면 닫히게 연결
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResume() {
        super.onResume()
        // 다른 액티비티 갔다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 상태가 엉뚱한 곳에 가 있는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_list
    }
}