package com.example.daggerhilttest

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Test {

}

fun main() {
    CoroutineScope(Dispatchers.Main).launch {
        f3()
    }
}

suspend fun f3() {
    val v1 = f1()
    val v2 = f2()
    print("completed")
}
suspend fun f1(): String {
  delay(1500)
    return "ans1"
}
suspend fun f2(): String {
    delay(2500)
    return "ans2"
}