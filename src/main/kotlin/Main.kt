import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal

fun main(args: Array<String>) {
    runBlocking {
        val source = numbersFrom(1_0000_0000)
        val a2Channel = Channel<Row>()
        val a3Channel = Channel<Row>()
        f1(source,a2Channel,a3Channel)
        val a1Channel = Channel<Row>()

        // a1 = a2 - a3
        f2(a2Channel,1,a1Channel)
        f2(a3Channel,-1,a1Channel)

    }
}

class Row(var x : Int, var y : Int, var v : BigDecimal)

// 模拟查询出来的因子。
fun CoroutineScope.numbersFrom(size: Int) = produce {
    for (i in 0 until size) {
        send(Row(i, i * 9, (i * 99).toBigDecimal()))
    }
}

// 模拟分发给不同的因子
suspend fun f1(source : ReceiveChannel<Row>, a2Channel : SendChannel<Row>,a3Channel: SendChannel<Row>){
    for (row in source){
        if(row.x % 2 == 0){
            a2Channel.send(row)
        }else{
            a2Channel.send(row)
        }
    }
}

// 模拟进行维度变化，然后改变累加值
suspend fun f2(source: ReceiveChannel<Row>,pi : Int, a1Channel: Channel<Row>){
    if(pi == 1){
        for (row in source){
            row.x = 1
            a1Channel.send(row)
        }
    }else{
        for (row in source){
            row.x = 1
            row.v = row.v.negate()
            a1Channel.send(row)
        }
    }
}

// 模拟累加到结果集
suspend fun f3(a1Channel: Channel<Row>){

}

