package com.wxsoft.fcare.ui.common

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wxsoft.fcare.R
import kotlinx.android.synthetic.main.item_get_picture.view.*


class GetPictureDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_get_picture_dialog, container, false)
    }

//    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        list.layoutManager = LinearLayoutManager(context)
//        list.adapter = ItemAdapter(3)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) {
            parent as Listener
        } else {
            context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onItemClicked(position: Int)
    }

    class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(inflater.inflate(R.layout.item_get_picture, parent, false)) {

        internal val text: TextView = itemView.text

        init {
            text.setOnClickListener {
//                mListener?.let {
//                    it.onItemClicked(adapterPosition)
//                    dismiss()
//                }
            }
        }
    }

    class ItemAdapter internal constructor() :
        RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = position.toString()
        }

        override fun getItemCount(): Int {
            return 0
        }
    }

}
