package uz.gita.dictionary

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import uz.gita.dictionary.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myTg.setOnClickListener {
            openTelegramProfile("Primqulov_Bahrom")
        }

        binding.gitaInsta.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/gita.uzofficial"))
            startActivity(intent)
        }

        binding.gitaTg.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/gitauz"))
            startActivity(intent)
        }

        binding.gitaYoutube.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/channel/UCjegZuzSYR4Ew5NDirQk_mg")
            )
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.btnShare).setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + this.applicationContext.packageName
            )
            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }


    private fun openTelegramProfile(telegramUserName: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/$telegramUserName"))
        startActivity(intent)
    }
}