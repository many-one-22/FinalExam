package com.example.finalexam

object CartManager {
    // 장바구니에 담긴 도서 리스트를 보관하는 변수
    private val cartItems = mutableListOf<Book>()

    // 도서 추가 함수
    fun addBook(book: Book) {
        cartItems.add(book)
    }

    // 현재 장바구니 아이템들을 가져오는 함수
    fun getCartItems(): List<Book> {
        return cartItems
    }

    fun removeBook(book: Book){
        cartItems.remove(book)
    }

    // 장바구니에 있는 항목들을 전체 삭제하는 함수
    fun removeBookAll(){
        cartItems.clear()
    }
}