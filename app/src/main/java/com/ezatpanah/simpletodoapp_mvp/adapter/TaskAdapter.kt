package com.ezatpanah.simpletodoapp_mvp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ezatpanah.simpletodoapp_mvp.R
import com.ezatpanah.simpletodoapp_mvp.databinding.ItemNotesBinding
import com.ezatpanah.simpletodoapp_mvp.db.TaskEntity
import com.ezatpanah.simpletodoapp_mvp.utils.*
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.DELETE
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.EDIT
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.EDUCATION
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.HEALTH
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.HIGH
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.HOME
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.LOW
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.NORMAL
import com.ezatpanah.simpletodoapp_mvp.utils.Constants.WORK
import javax.inject.Inject

class TaskAdapter @Inject constructor() : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    private lateinit var binding: ItemNotesBinding
    private lateinit var context: Context
    private var noteList = emptyList<TaskEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = noteList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: TaskEntity) {
            binding.apply {
                titleTxt.text = item.title
                descTxt.text = item.desc
                //Priority
                when (item.pr) {
                    HIGH -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    NORMAL -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                    LOW -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.aqua))
                }
                //Category
                when (item.cat) {
                    HOME -> categoryImg.setImageResource(R.drawable.house)
                    WORK -> categoryImg.setImageResource(R.drawable.suitcase)
                    EDUCATION -> categoryImg.setImageResource(R.drawable.education)
                    HEALTH -> categoryImg.setImageResource(R.drawable.heartbeat)
                }
                //Menu
                menuImg.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
                    popupMenu.menuInflater.inflate(R.menu.menu_items, popupMenu.menu)
                    popupMenu.show()
                    //Click
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.itemEdit -> {
                                onItemClickListener?.let {
                                    it(item, EDIT)
                                }
                            }
                            R.id.itemDelete -> {
                                onItemClickListener?.let {
                                    it(item, DELETE)
                                }
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((TaskEntity, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (TaskEntity, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<TaskEntity>) {
        val moviesDiffUtil = NotesDiffUtils(noteList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        noteList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<TaskEntity>, private val newItem: List<TaskEntity>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}