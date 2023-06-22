package uz.gita.hk_memory_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.hk_memory_game.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private val binding by viewBinding(ActivityMainBinding::bind)
private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("TTT", "Main activity")
    }

}