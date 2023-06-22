package uz.gita.hk_memory_game.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.hk_memory_game.R
import uz.gita.hk_memory_game.data.LevelEnum
import uz.gita.hk_memory_game.databinding.ScreenLevelBinding

class LevelScreen:Fragment(R.layout.screen_level) {
    private val screenLevelBinding by viewBinding(ScreenLevelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        screenLevelBinding.easy.setOnClickListener {
            openGameScreen(LevelEnum.EASY)
        }
        screenLevelBinding.medium.setOnClickListener {
            openGameScreen(LevelEnum.MEDIUM)
        }
        screenLevelBinding.hard.setOnClickListener {
            openGameScreen(LevelEnum.HARD)
        }
        screenLevelBinding.apply {
            easy.animate()
                .translationY(-300f)
                .setDuration(1500)
                .withEndAction {
                    easy.animate()
                        .translationX(100f)
                }
                .start()
            hard.animate()
                .translationY(300f)
                .setDuration(1500)
                .withEndAction {
                    hard.animate()
                        .translationX(-100f)
                }
                .start()
            medium.animate()
                .withStartAction {
                    medium.animate()
                        .scaleXBy(2f)
                        .scaleYBy(1.5f)
                        .setDuration(1000)
                        .start()
                }
                .withEndAction {
                    medium.animate()
                        .scaleXBy(1f)
                        .scaleYBy(1f)
                        .setDuration(1000)
                        .start()
                }
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private fun openGameScreen(level: LevelEnum) {
        findNavController().navigate(LevelScreenDirections.actionLevelScreenToGameScreen(level))
    }

}