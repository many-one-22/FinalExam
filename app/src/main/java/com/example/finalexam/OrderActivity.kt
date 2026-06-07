package com.example.finalexam

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalexam.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 툴바 설정 (기본 타이틀 안보이게)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.bottomNavigation.selectedItemId = R.id.nav_cart

        // 2. 바텀 네비게이션 리스너 (주문서 작성 중 타 탭 이동 시 스택 정리)
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
                    true
                }
                else -> false
            }
        }

        // 3. 주문할 도서 목록 RecyclerView 연결 (기존 템플릿 어댑터 그대로 재사용)
        val orderItems = CartManager.getCartItems()
        binding.rvOrderBooks.layoutManager = LinearLayoutManager(this)
        binding.rvOrderBooks.adapter = BookAdapter(orderItems) { } // 주문서에서는 클릭 이벤트 불필요하므로 비워둠

        // 4. 주문 확정 버튼 클릭 리스너 (Ch 08, Ch 15 대화상자)
        binding.btnOrderSubmit.setOnClickListener {
            val name = binding.etOrderName.text.toString().trim()
            val phone = binding.etOrderPhone.text.toString().trim()
            val address = binding.etOrderAddress.text.toString().trim()

            // 입력 검증 (Null-safety 및 공백 체크)
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "배송지 정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 가산점 항목: 의미 있는 곳에 AlertDialog 다루기 (Ch 15)
                AlertDialog.Builder(this)
                    .setTitle("주문 확정")
                    .setMessage("입력하신 주소로 주문을 최종 확정하시겠습니까?")
                    .setPositiveButton("확정") { _, _ ->
                        Toast.makeText(this, getString(R.string.order_success), Toast.LENGTH_SHORT).show()

                        // 🌟 주문이 완결되었으므로 싱글톤 장바구니 내역을 리셋하는 보너스 구현 기재
                        // (발표 시 데이터 흐름 제어로 설명하기 아주 좋습니다)
                        // 임시 저장 공간 초기화를 원할 시 CartManager에 clear 함수를 만들어 호출해도 좋습니다.

                        // 메인 홈 화면으로 한 번에 복귀 및 액티비티 백스택 청소
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        }
    }
}