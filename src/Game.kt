import kotlin.jvm.JvmStatic

object Game {
    @JvmStatic
    fun main(args: Array<String>) {
        val b = Board()
        b.printBoard()
    }
}