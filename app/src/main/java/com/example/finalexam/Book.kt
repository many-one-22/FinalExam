package com.example.finalexam

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val price: String,
    val publisher: String,
    val imageResId: Int // res/drawable에 넣은 이미지 ID
)
