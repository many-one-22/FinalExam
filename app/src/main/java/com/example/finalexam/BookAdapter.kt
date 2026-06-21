package com.example.finalexam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalexam.databinding.ItemBookBinding

class BookAdapter(
    private val bookList: List<Book>,
    // 어댑터 안에서 Intent 써서 화면 넘기면 코드가 꼬이고 지저분해져서 클릭 이벤트 처리는 액티비티에서 진행하기 위해 람다식 사용
    private val onItemClick: (Book) -> Unit,
    // 길게 클릭했을 때의 이벤트 추가
    private val onItemLongClick: ((Book) -> Unit)? = null
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            // 넘어온 책 데이터들을 xml 레이아웃에 하나씩 매핑해줌
            binding.ivBookCover.setImageResource(book.imageResId)
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.tvBookPrice.text = book.price

            // 책 리스트의 한 칸 중 아무데나 터치해도 클릭 먹히도록 세팅
            // 클릭되면 아까 파라미터로 뚫어놓은 onItemClick으로 선택된 책 데이터를 쏴줌
            binding.root.setOnClickListener { onItemClick(book) }

            // 길게 눌렀을 때 이벤트 처리
            binding.root.setOnLongClickListener {
                onItemLongClick?.invoke(book)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        // item_book.xml 레이아웃을 가져와서 뷰홀더 객체로 찍어냄
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        // 화면 스크롤 할 때마다 재사용되는 뷰홀더에 현재 위치에 맞는 데이터를 연결해줌
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size // 전체 데이터 개수 반환
}