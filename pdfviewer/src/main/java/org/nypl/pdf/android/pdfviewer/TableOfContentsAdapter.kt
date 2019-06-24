package org.nypl.pdf.android.pdfviewer

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.table_of_contents_element.view.*
import org.nypl.pdf.android.api.TableOfContentsFragmentListenerType

class TableOfContentsAdapter(
    private val contents: List<TableOfContentsItemWrapper>,
    private val listener: TableOfContentsFragmentListenerType
) :
    RecyclerView.Adapter<TableOfContentsAdapter.ContentsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentsHolder {
        val inflatedView = parent.inflate(R.layout.table_of_contents_element, false)
        return ContentsHolder(inflatedView, listener)
    }

    override fun getItemCount(): Int {
        return contents.size
    }

    override fun onBindViewHolder(holder: ContentsHolder, position: Int) {
        val content = contents[position]
        holder.bindContents(content)
    }

    class ContentsHolder(v: View, private var listener: TableOfContentsFragmentListenerType) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        private var view: View = v
        private var content: TableOfContentsItemWrapper? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (content != null) {
                listener.onTableOfContentsItemSelected(content!!.pageNumber)
            }
        }

        fun bindContents(content: TableOfContentsItemWrapper) {
            this.content = content
            view.reader_toc_element_title.text = content.title
            view.reader_toc_element_page_number.text = content.pageNumber.toString()

            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val leftIndent = content.indent * 64

            params.setMargins(leftIndent, 0, 0, 0)

            view.layoutParams = params
        }
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}
