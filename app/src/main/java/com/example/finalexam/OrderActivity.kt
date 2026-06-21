package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.MypageActivity
import com.example.finalexam.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 툴바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 하단 메뉴바 초기 세팅
        binding.bottomNavigation.selectedItemId = R.id.nav_order

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
                    // 이미 현재 페이지이므로 아무 작업도 하지 않음
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

        // CartManager에서 장바구니 데이터를 가져와 recyclerview에 연결
        val orderItems = CartManager.getCartItems()
        binding.rvOrderBooks.layoutManager = LinearLayoutManager(this)

        // 기존 BookAdapter를 활용하면서 주문서 화면에서는 도서 클릭 이벤트가 불필요하므로 빈 람다식을 매개변수로 전달
        binding.rvOrderBooks.adapter = BookAdapter(orderItems, onItemClick = {})

        // 주문 확정 버튼 클릭 이벤트
        binding.btnOrderSubmit.setOnClickListener {
            // 사용자가 실수로 입력한 앞뒤 공백 제거
            val name = binding.etOrderName.text.toString().trim()
            val phone = binding.etOrderPhone.text.toString().trim()
            val address = binding.etOrderAddress.text.toString().trim()

            // 장바구니가 비어있는지 예외 처리
            if (CartManager.getCartItems().isEmpty()){
                Toast.makeText(this, "장바구니가 비어있습니다.", android.widget.Toast.LENGTH_SHORT).show()
            }
            else {
                // 배송지 정보 누락 체크
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    Toast.makeText(this, "배송지 정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    // 주문 최종 확정을 묻는 다이얼로그 출력
                    AlertDialog.Builder(this)
                        .setTitle("주문 확정")
                        .setMessage("입력하신 주소로 주문을 최종 확정하시겠습니까?")
                        .setPositiveButton("확정") { _, _ ->
                            Toast.makeText(this, getString(R.string.order_success), Toast.LENGTH_SHORT).show()

                            // 메인 화면으로 돌아가면서 중간에 쌓인 액티비티 스택(장바구니, 주문 등)을 모두 정리
                            val intent = Intent(this, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }
                            startActivity(intent)

                            // 주문 완료 시 장바구니 데이터 초기화
                            CartManager.removeBookAll()
                        }
                        .setNegativeButton("취소", null)
                        .show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 다른 액티비티를 돌다가 뒤로가기로 돌아왔을 때 하단 메뉴바의 선택 상태가 꼬이는 현상 방지
        binding.bottomNavigation.selectedItemId = R.id.nav_order
    }
}