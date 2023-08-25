package com.clyzhi.storybuilder.x

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nex3z.flowlayout.FlowLayout


class MainActivity : AppCompatActivity() {
    private lateinit var userInputEditText: EditText
    private lateinit var userTalkListView: RecyclerView
    private var talkArrayList: ArrayList<TalkItem> = ArrayList()
    private lateinit var adapter: TalkListAdapter

    private val TAG = "MainActivity"

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_NAME = 1
        const val TYPE_CLOSE_TEXT = ""
        const val TYPE_CLOSE_NAME = 0

        const val NAME_DEFAULT = "您"
        const val NAME_STORY_SAY = "旁白"
    }


    private val onSendAction: TextView.OnEditorActionListener =
        TextView.OnEditorActionListener { _, actionId, _ ->
            Log.d(TAG, "onCreate: $actionId")
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                return@OnEditorActionListener onInputEnter()
            }
            false
        }

    fun onInputEnter():Boolean {
        val userInput = userInputEditText.text.toString().trim()
        if (userInput.isEmpty()) return false
        userInputEditText.text.clear()
        // 在这里执行你想要的操作，例如向服务器发送消息或进行其他处理
        talkArrayList.add(
            TalkItem(
                NAME_DEFAULT, arrayListOf(TalkContent(userInput))
            )
        )
        adapter.notifyItemInserted(talkArrayList.size + 1)
        userTalkListView.smoothScrollToPosition(talkArrayList.size - 1)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainActivityToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(mainActivityToolbar)
        mainActivityToolbar.setBackgroundColor(this.getColor(androidx.appcompat.R.color.button_material_dark))
        mainActivityToolbar.subtitle = this.getString(R.string.app_name)
        userInputEditText = findViewById(R.id.UserInputEditText)
        userInputEditText.setOnEditorActionListener(onSendAction)
        userTalkListView = findViewById(R.id.TalkListView)
        userTalkListView.layoutManager = LinearLayoutManager(this)
        adapter = TalkListAdapter(talkArrayList)
        userTalkListView.adapter = adapter
    }

    class TalkListAdapter(private val list: ArrayList<TalkItem>) :
        RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var name: TextView
            var content: FlowLayout

            init {
                name = v.findViewById(R.id.name)
                content = v.findViewById(R.id.content)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.talk_list_item_view, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val el = list[position]
            val name = holder.name
            val content = holder.content
            name.text = el.name
            el.content.forEach() {
                val tv = TextView(content.context)
                tv.text = it.toString()
                content.addView(tv)
            }
        }

        override fun getItemCount(): Int = list.size
    }

    data class TalkItem(var name: String, var content: ArrayList<TalkContent>)
    data class TalkContent(var type: Int, var text: String, var tag: Int) {
        constructor(tag: Int) : this(TYPE_NAME, TYPE_CLOSE_TEXT, tag)
        constructor(text: String) : this(TYPE_TEXT, text, TYPE_CLOSE_NAME)
    }
}

