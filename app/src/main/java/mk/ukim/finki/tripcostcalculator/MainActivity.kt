package mk.ukim.finki.tripcostcalculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import mk.ukim.finki.tripcostcalculator.ui.CalculateFragment
import mk.ukim.finki.tripcostcalculator.ui.CameraFragment
import mk.ukim.finki.tripcostcalculator.ui.HistoryFragment

class MainActivity : AppCompatActivity() {

    private var isDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootLayout = findViewById<View>(R.id.main)
        val themeToggleBtn = findViewById<SwitchCompat>(R.id.themeSwitch)

        themeToggleBtn.setOnClickListener {
            isDarkMode = !isDarkMode

            if (isDarkMode) {
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.themeMistyPurpleDark))
            } else {
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.themePastelBlend))
            }
        }

        val titleText = findViewById<TextView>(R.id.tvTitle)

        themeToggleBtn.setOnClickListener {
            isDarkMode = !isDarkMode

            if (isDarkMode) {
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.themeMistyPurpleDark))
                titleText.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.themePastelBlend))
                titleText.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }

        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, CalculateFragment())
            transaction.commit()
        }

        findViewById<Button>(R.id.btnHistory).setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, HistoryFragment())
            transaction.commit()
        }

        findViewById<Button>(R.id.btnCamera).setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, CameraFragment())
                .commit()
        }

    }
}