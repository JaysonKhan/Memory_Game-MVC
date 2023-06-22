package uz.gita.hk_memory_game.ui.screen

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.hk_memory_game.R
import uz.gita.hk_memory_game.data.LevelEnum
import uz.gita.hk_memory_game.data.MyCard
import uz.gita.hk_memory_game.databinding.ScreenGameBinding
import uz.gita.hk_memory_game.repository.Repository
import java.util.ArrayList

class GameScreen : Fragment(R.layout.screen_game) {
    private val screenGameBinding by viewBinding(ScreenGameBinding::bind)
    private var defLevel = LevelEnum.EASY
    private val args by navArgs<GameScreenArgs>()
    private val repository = Repository()
    private var _width = 0
    private var _height = 0
    private var images = ArrayList<ImageView>()

    var count:Int = 0
    lateinit var ls:List<MyCard>

    private var firsImage: ImageView? = null
    private var secondImage: ImageView? = null

    private var openedImgId = 0
    private var isStart = false

    private var full_time = 120
    private var attemp = 0
    private var level:Int = 1
    private var scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private lateinit var job: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        defLevel = args.level

        screenGameBinding.space.post {
            _height = screenGameBinding.container.height / defLevel.vertical
            _width = screenGameBinding.container.width / defLevel.horizontal
            restart()
        }
        screenGameBinding.menu.setOnClickListener {
            scope.cancel()
             findNavController().navigateUp()
        }
        super.onViewCreated(view, savedInstanceState);
    }

    private fun startProgress(time: Int) {
        scope.cancel()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        var _time = time
        val process = screenGameBinding.progress
        job = scope.launch {
            while (_time!=0){
                process.progress = _time--
                screenGameBinding.txtTime.text = "$_time"
                delay(1000)
            }
            lose()
        }
    }

    private fun describeLevel(lvl: Int){
        when(lvl){
            1->screenGameBinding.imgLevel.setImageResource(R.drawable.level1)
            2->screenGameBinding.imgLevel.setImageResource(R.drawable.level2)
            3->screenGameBinding.imgLevel.setImageResource(R.drawable.level3)
            4->screenGameBinding.imgLevel.setImageResource(R.drawable.level4)
            5->screenGameBinding.imgLevel.setImageResource(R.drawable.level5)
            6->screenGameBinding.imgLevel.setImageResource(R.drawable.level6)
            7->screenGameBinding.imgLevel.setImageResource(R.drawable.level7)
            8->screenGameBinding.imgLevel.setImageResource(R.drawable.level8)
            9->screenGameBinding.imgLevel.setImageResource(R.drawable.level9)
            else->screenGameBinding.imgLevel.setImageResource(R.drawable.level10)
        }
    }
    private fun restart(){
        attemp = 0
        count = defLevel.vertical * defLevel.horizontal
        ls = repository.getData(count)
        isStart = true
        describeMyCards(ls)
        describeSituation()
        scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
        startProgress(full_time)
    }
    fun describeSituation(){
        screenGameBinding.attempt.text = "$attemp"
        screenGameBinding.levelText.text = "$level/10"
    }

    private fun describeMyCards(list: List<MyCard>) {
        isStart = false
        describeLevel(level)
        screenGameBinding.container.removeAllViews()

        lifecycleScope.launch {
            for (i in 0 until defLevel.horizontal) {
                for (j in 0 until defLevel.vertical) {

                    val image = ImageView(requireContext())
                    screenGameBinding.container.addView(image)
                    val lp = image.layoutParams as ConstraintLayout.LayoutParams

                    lp.apply {
                        width = _width
                        height = _height
                    }
                    image.layoutParams = lp
                    image.tag = list[i * defLevel.vertical + j]
                    image.setImageResource(R.drawable.image_back)
                    image.animate()
                        .x(i * _width * 1f)
                        .y(j * _height * 0.85f)
                        .setDuration(250L * i + 100 * j)
                        .withEndAction {

                        }
                        .start()
                    images.add(image)
                }
            }
            delay(2000)
            openFirst(images)
            delay(1000)
        }
        addClickListener()
    }
    private fun openFirst(images: ArrayList<ImageView>) {
        isStart = false
        images.forEach { imageView ->
            val data = imageView.tag as MyCard

            imageView.animate()
                .setDuration(250)
                .rotationY(90f)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction {
                    imageView.setImageResource(data.imageRes)
                    imageView.rotationY = -90f
                    imageView.animate()
                        .setDuration(700)
                        .rotationY(0f)
                        .setInterpolator(DecelerateInterpolator())
                        .withEndAction {
                            lifecycleScope.launch {
                                delay(1000)
                                imageView.animate()
                                    .setInterpolator(AccelerateInterpolator())
                                    .setDuration(500)
                                    .rotationY(90f)
                                    .withEndAction {
                                        imageView.setImageResource(R.drawable.image_back)
                                        imageView.rotationY = -90f
                                        imageView.animate()
                                            .setInterpolator(DecelerateInterpolator())
                                            .setDuration(350)
                                            .rotationY(0f)
                                            .start()
                                        isStart = true
                                        }
                                        .start()
                                }
                            }
                            .start()
                    }
                .start()
            }
    }

    private fun addClickListener() {
        images.forEach { imageView ->
            imageView.setOnClickListener {
                if (firsImage == null && isStart) {
                    firsImage = imageView
                    openFirstCard(imageView)
                    imageView.isClickable = false
                } else if (secondImage == null && isStart) {
                    secondImage = imageView
                    openSecondCard(imageView)
                    imageView.isClickable = false
                }
            }

        }
    }

    private fun openFirstCard(imageView: ImageView) {
        val data = imageView.tag as MyCard
        openedImgId = data.id
        imageView.animate()
            .setDuration(300)
            .rotationY(89f)
            .withEndAction {
                imageView.setImageResource(data.imageRes)
                imageView.rotationY = -89f
                imageView.animate()
                    .setDuration(300)
                    .rotationY(0f)
                    .withEndAction {

                    }
                    .start()
            }
            .start()

    }
    private fun openSecondCard(imageView: ImageView) {
        secondImage = imageView
        imageView.animate()
            .setDuration(300)
            .rotationY(89f)
            .withEndAction {
                val data = imageView.tag as MyCard
                imageView.setImageResource(data.imageRes)
                imageView.rotationY = -89f
                imageView.animate()
                    .setDuration(300)
                    .rotationY(0f)
                    .withEndAction {
                        attemp++
                        if (data.id == openedImgId) {
                            imageView.animate()
                                .setDuration(300)
                                .rotation(360f)
                                .withEndAction {
                                    screenGameBinding.container.removeView(imageView)
                                    screenGameBinding.container.removeView(firsImage)
                                    firsImage = null
                                    secondImage = null
                                    if (screenGameBinding.container.childCount == 0) {
                                        winner(full_time)
                                        Toast.makeText(
                                            requireContext(),
                                            "You win",

                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                                .start()
                            firsImage!!.animate()
                                .setDuration(300)
                                .rotation(360f)
                                .start()
                            describeSituation()
                        } else {
                            closeCard(firsImage!!)
                            closeCard(imageView)
                        }
                    }
                    .start()
            }
            .start()
    }

    private fun winner(time:Int) {
        scope.cancel()

        val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_item)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val star1:ImageView = dialog.findViewById(R.id.star1)
        val star2:ImageView = dialog.findViewById(R.id.star2)
        val star3:ImageView = dialog.findViewById(R.id.star3)
        val stars = arrayListOf<ImageView>(star1, star2, star3)

        val reply:ImageView = dialog.findViewById(R.id.btn_replay)
        val next:ImageView = dialog.findViewById(R.id.btn_next)
        val menu:ImageView = dialog.findViewById(R.id.btn_menu)
        var numlvl:Int = when(defLevel){
            LevelEnum.EASY -> 1
            LevelEnum.MEDIUM -> 2
            else -> 3
        }
        val present = 100*(20*numlvl + time)/120
        if(80<=present){
            stars.forEach { star->
                star.animate()
                    .withStartAction {
                        star.setImageResource(R.drawable.blackstar)
                        star.animate()
                            .rotation(360f)
                            .setDuration(500)
                            .start()
                    }
                    .rotationY(360f)
                    .withEndAction{
                        star.setImageResource(R.drawable.yellowstar)
                        star.animate()
                            .rotation(-360f)
                            .setDuration(500)
                            .start()
                    }
                    .setDuration(1000)
                    .start()

            }
        }else if(60<=present){
            stars.remove(star3)
            stars.forEach { star->
                star.animate()
                    .withStartAction {
                        star.setImageResource(R.drawable.blackstar)
                        star.animate()
                            .rotation(360f)
                            .setDuration(500)
                            .start()
                    }
                    .rotationY(360f)
                    .withEndAction{
                        star.setImageResource(R.drawable.yellowstar)
                        star.animate()
                            .rotation(-360f)
                            .setDuration(500)
                            .start()
                    }
                    .setDuration(1000)
                    .start()

            }
        }else{
            stars.clear()
            star1.animate()
                .withStartAction {
                    star1.setImageResource(R.drawable.blackstar)
                    star1.animate()
                        .rotation(360f)
                        .setDuration(500)
                        .start()
                }
                .rotationY(360f)
                .withEndAction{
                    star1.setImageResource(R.drawable.yellowstar)
                    star1.animate()
                        .rotation(-360f)
                        .setDuration(500)
                        .start()
                }
                .setDuration(1000)
                .start()

        }

        reply.setOnClickListener {
            isStart = true
            describeMyCards(ls)
            dialog.dismiss()
            startProgress(full_time)
        }


        menu.setOnClickListener {
            findNavController().navigateUp()
            dialog.dismiss()
        }
        dialog.show()

        if (level>=10){
            next.isClickable = false
            next.visibility = View.INVISIBLE
            dialog.dismiss()
            findNavController().navigate(R.id.action_gameScreen_to_infoScreen)
        }else{
            next.setOnClickListener {
                level++
                describeLevel(level)
                full_time -= 10
                restart()
                dialog.dismiss()
            }
        }

    }

    private fun lose() {

                val dialog: Dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_item)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        (dialog.findViewById(R.id.star1) as ImageView).setImageResource(R.drawable.blackstar)
        (dialog.findViewById(R.id.star2) as ImageView).setImageResource(R.drawable.blackstar)
        (dialog.findViewById(R.id.star3) as ImageView).setImageResource(R.drawable.blackstar)
        (dialog.findViewById(R.id.img_congras) as ImageView).setImageResource(R.drawable.game_over)

        val reply:ImageView = dialog.findViewById(R.id.btn_replay)
        val next:ImageView = dialog.findViewById(R.id.btn_next)
        val menu:ImageView = dialog.findViewById(R.id.btn_menu)
        next.isClickable = false
        next.visibility = View.INVISIBLE

        if (level>=3)
            level--

        reply.setOnClickListener {
            Log.d("TTT", "$full_time")
            startProgress(full_time)
            isStart = true
            describeMyCards(ls)
            dialog.dismiss()
        }

        menu.setOnClickListener {
            findNavController().navigateUp()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun closeCard(imageView: ImageView) {
        describeSituation()
        imageView.animate()
            .setDuration(300)
            .rotationY(-89f)
            .withEndAction {

                imageView.setImageResource(R.drawable.image_back)
                imageView.rotationY = 89f
                imageView.animate()
                    .setDuration(300)
                    .rotationY(0f)
                    .withEndAction {
                        firsImage = null
                        secondImage = null
                        imageView.isClickable = true
                        openedImgId = 0
                    }
                    .start()
            }
            .start()
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

}