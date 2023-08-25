package com.clyzhi.storybuilder.x

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private lateinit var userInputEditText: EditText
    private lateinit var userTalkListView: RecyclerView
    private lateinit var talkArrayList: ArrayList<TalkItem>

    private val onSendAction: TextView.OnEditorActionListener = TextView.OnEditorActionListener { _, actionId, _ ->
        Log.d("TAG", "onCreate: $actionId")
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            val userInput = userInputEditText.text.toString().trim()
            if (userInput.isEmpty()) return@OnEditorActionListener false
            userInputEditText.text.clear()
            // 在这里执行你想要的操作，例如向服务器发送消息或进行其他处理
            talkArrayList.add(TalkItem("",userInput))
            reloadTalkList()
            return@OnEditorActionListener true
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(mainActivityToolbar)
        mainActivityToolbar.setBackgroundColor(this.getColor(androidx.appcompat.R.color.button_material_dark))
        mainActivityToolbar.subtitle = this.getString(R.string.app_name)
        userInputEditText = findViewById(R.id.UserInputEditText)
        userInputEditText.setOnEditorActionListener (onSendAction)
        userTalkListView = findViewById(R.id.TalkListView)
        reloadTalkList()

    }
    private fun reloadTalkList(){
        userTalkListView.layoutManager = LinearLayoutManager(this)
        userTalkListView.adapter = TalkListAdapter(talkArrayList)
    }

    class TalkListAdapter(private val list: ArrayList<TalkItem>) : RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var name : TextView
            var text : TextView
            init{
                name = v.findViewById(R.id.name)
                text = v.findViewById(R.id.content)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.talk_list_item_view, parent, false))

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val el = list[position]
            val name = holder.name
            val text = holder.text
            name.text = el.name
            text.text = el.content
        }
        override fun getItemCount(): Int = list.size
    }

    data class TalkItem(var name:String,var content:String)
}

