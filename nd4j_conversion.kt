import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.time.LocalDate

fun main(args: Array<String>) {

    val matrix = sales.toINDArray(
            {it.saleDate.year.toDouble()},
            {it.saleDate.monthValue.toDouble()},
            {it.saleDate.dayOfMonth.toDouble()},
            {it.billingAmount}
    )

    println(matrix)
}


fun <T> Iterable<T>.toINDArray(vararg valueSelectors: (T) -> Double): INDArray {
    val list = toList()

    return Nd4j.create(
            list.asSequence()
                    .flatMap { item -> valueSelectors.asSequence().map { it(item) } }
                    .toList().toDoubleArray(),
            intArrayOf(list.size, valueSelectors.size)
    )
}

data class Sale(val accountId: Int, val saleDate: LocalDate, val billingAmount: Double)

val sales = listOf(
        Sale(1, LocalDate.of(2016,12,3), 180.0),
        Sale(2, LocalDate.of(2016, 7, 4), 140.2),
        Sale(3, LocalDate.of(2016, 6, 3), 111.4),
        Sale(4, LocalDate.of(2016, 1, 5), 192.7),
        Sale(5, LocalDate.of(2016, 5, 4), 137.9),
        Sale(6, LocalDate.of(2016, 3, 6), 125.6),
        Sale(7, LocalDate.of(2016, 12,4), 164.3),
        Sale(8, LocalDate.of(2016, 7,11), 144.2)
)

enum class Gender {
    MALE,
    FEMALE
}
