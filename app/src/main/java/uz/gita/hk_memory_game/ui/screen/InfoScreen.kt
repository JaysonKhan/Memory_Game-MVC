package uz.gita.hk_memory_game.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.hk_memory_game.R
import uz.gita.hk_memory_game.databinding.ScreenInfoBinding

class InfoScreen:Fragment(R.layout.screen_info) {
    private val screenInfoBinding by viewBinding(ScreenInfoBinding::bind)
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        screenInfoBinding.apply {
            infoContainer.animate()
                .translationY(0f)
                .setDuration(7000)
                .start()
        }
        scope.launch {
            delay(15000)
            findNavController().navigateUp()
            scope.cancel()
        }
    }
}