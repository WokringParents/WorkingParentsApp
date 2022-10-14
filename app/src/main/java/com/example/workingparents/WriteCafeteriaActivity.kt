package com.example.workingparents
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_write_cafeteria.*

class WriteCafeteriaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_cafeteria)

        expandable.secondLayoutResource = (R.layout.layout_second)

        //뒤로가기
        writeCafeteria_back.setOnClickListener {
            onBackPressed()
        }

        expandable.setOnExpandListener {
            if (it) {
                Toast.makeText(this, "expanded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "collapse", Toast.LENGTH_SHORT).show()
            }
        }

        expandable.parentLayout.setOnClickListener {
            if (expandable.isExpanded) expandable.collapse()
            else expandable.expand()
        }
    }
}
