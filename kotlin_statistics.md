
data class Patient(val firstName: String,
                   val lastName: String,
                   val gender: Gender,
                   val birthday: LocalDate,
                   val whiteBloodCellCount: Int)


val patients = listOf(
        Patient("John", "Simone", Gender.MALE, LocalDate.of(1989, 1, 7), 4500),
        Patient("Sarah", "Marley", Gender.FEMALE, LocalDate.of(1970, 2, 5), 6700),
        Patient("Jessica", "Arnold", Gender.FEMALE, LocalDate.of(1980, 3, 9), 3400),
        Patient("Sam", "Beasley", Gender.MALE, LocalDate.of(1981, 4, 17), 8800),
        Patient("Dan", "Forney", Gender.MALE, LocalDate.of(1985, 9, 13), 5400),
        Patient("Lauren", "Michaels", Gender.FEMALE, LocalDate.of(1975, 8, 21), 5000),
        Patient("Michael", "Erlich", Gender.MALE, LocalDate.of(1985, 12, 17), 4100),
        Patient("Jason", "Miles", Gender.MALE, LocalDate.of(1991, 11, 1), 3900),
        Patient("Rebekah", "Earley", Gender.FEMALE, LocalDate.of(1985, 2, 18), 4600),
        Patient("James", "Larson", Gender.MALE, LocalDate.of(1974, 4, 10), 5100),
        Patient("Dan", "Ulrech", Gender.MALE, LocalDate.of(1991, 7, 11), 6000),
        Patient("Heather", "Eisner", Gender.FEMALE, LocalDate.of(1994, 3, 6), 6000),
        Patient("Jasper", "Martin", Gender.MALE, LocalDate.of(1971, 7, 1), 6000)
)

enum class Gender {
    MALE,
    FEMALE
}



fun main(args: Array<String>) {

    val averageWbcc =
            patients.map { it.whiteBloodCellCount }.average()

    val standardDevWbcc =
            patients.map { it.whiteBloodCellCount }.standardDeviation()

    println("Average WBCC: $averageWbcc, Std Dev WBCC: $standardDevWbcc")
}










import org.nield.kotlinstatistics.binByComparable
import java.time.LocalDate


fun main(args: Array<String>) {

    val totalValueByQuarter = sales.binByComparable(
            valueMapper = { it.saleDate.month },
            binIncrements = 3,
            incrementer = { it.plus(1L) },
            groupOp = { it.map(Sale::billingAmount).sum() }
    )

    totalValueByQuarter.forEach(::println)
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
