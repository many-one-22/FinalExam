package com.example.finalexam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalexam.databinding.ItemBookBinding

class BookAdapter(
    private val bookList: List<Book>,
    private val onItemClick: (Book) -> Unit // 클릭 이벤트를 밖으로 전달하기 위한 람다식
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewBinding을 활용한 ViewHolder 정의
    inner class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.ivBookCover.setImageResource(book.imageResId)
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.tvBookPrice.text = book.price

            // 아이템 클릭 시 리스너 실행 (Ch 08)
            binding.root.setOnClickListener { onItemClick(book) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}