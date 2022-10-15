package id.innovanesia.kandignas.backend.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.innovanesia.kandignas.backend.models.News

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    private lateinit var news: ArrayList<News>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int
    {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {

    }
}