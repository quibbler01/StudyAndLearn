package cn.quibbler.coroutine.kotlin.contract

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * https://blog.csdn.net/HJXASLZYY/article/details/122676634
 */

interface Animal

class Bird : Animal {

    fun fly() {

    }

}

fun move(animal: Animal) {
    if (animal is Bird) {
        animal.fly()
    }
}


//fun isBird(animal: Animal) = animal is Bird

@OptIn(ExperimentalContracts::class)
fun isBird(animal: Animal): Boolean {
    contract {
        returns(true) implies (animal is Bird)
    }
    return animal is Bird
}

@ExperimentalContracts
fun move2(animal: Animal) {
    if (isBird(animal)) {
        animal.fly()
    }
}
