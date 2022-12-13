import kotlin.math.abs
import kotlin.math.sqrt

data class Position(val row: Int, val col: Int) {
    fun distanceFrom (other: Position): Double  {
        val rowOffset = abs(row - other.row)
        val colOffset = abs(col - other.col)
        return sqrt(((rowOffset * rowOffset) + (colOffset * colOffset)).toDouble())
    }
}