package uz.gita.hk_memory_game.repository

import uz.gita.hk_memory_game.R
import uz.gita.hk_memory_game.data.MyCard

class Repository {
    private val cardList = ArrayList<MyCard>()

    init {
        cardList.add(MyCard(R.drawable.image_1, 1))
        cardList.add(MyCard(R.drawable.image_2, 2))
        cardList.add(MyCard(R.drawable.image_3, 3))
        cardList.add(MyCard(R.drawable.image_4, 4))
        cardList.add(MyCard(R.drawable.image_5, 5))
        cardList.add(MyCard(R.drawable.image_6, 6))
        cardList.add(MyCard(R.drawable.image_7, 7))
        cardList.add(MyCard(R.drawable.image_8, 8))
        cardList.add(MyCard(R.drawable.image_9, 9))
        cardList.add(MyCard(R.drawable.image_10, 10))
        cardList.add(MyCard(R.drawable.image_11, 11))
        cardList.add(MyCard(R.drawable.image_12, 12))
        cardList.add(MyCard(R.drawable.image_13, 13))
        cardList.add(MyCard(R.drawable.image_14, 14))
        cardList.add(MyCard(R.drawable.image_15, 15))
        cardList.add(MyCard(R.drawable.image_16, 16))
        cardList.add(MyCard(R.drawable.image_17, 17))
        cardList.add(MyCard(R.drawable.image_18, 18))
        cardList.add(MyCard(R.drawable.image_19, 19))
        cardList.add(MyCard(R.drawable.image_20, 20))
        cardList.add(MyCard(R.drawable.image_21, 21))
        cardList.add(MyCard(R.drawable.image_22, 22))
        cardList.add(MyCard(R.drawable.image_23, 23))
        cardList.add(MyCard(R.drawable.image_24, 24))
        cardList.add(MyCard(R.drawable.image_25, 25))
        cardList.add(MyCard(R.drawable.image_26, 26))
        cardList.add(MyCard(R.drawable.image_27, 27))
        cardList.add(MyCard(R.drawable.image_28, 28))
        cardList.add(MyCard(R.drawable.image_29, 29))
        cardList.add(MyCard(R.drawable.image_30, 30))
    }

    fun getData(count: Int):List<MyCard>{
        cardList.shuffle()
        val ls = cardList.subList(0, count/2)
        val result = ArrayList<MyCard>(count)
        result.addAll(ls)
        result.addAll(ls)
        result.shuffle()
        result.shuffle()
        return result
    }
}