package cn.quibbler.coroutine.learn

class Learn2 {

    suspend fun level_0() {
        print("I'm in level 0")
        level_1()
        level_2()
    }

    fun level_1() {
        //Suspend function 'level_2' should be called only from a coroutine or another suspend function
        //level_2()
    }

    suspend fun level_2() {

    }
}