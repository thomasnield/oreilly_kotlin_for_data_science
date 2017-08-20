import java.math.BigDecimal

fun main(args: Array<String>) {

    hotelmodel {

        rooms {
            queen(quantity = 60)
            doublequeen(quantity = 60)

            king(quantity = 20)
            doubleking(quantity = 20)
        }

        prices {
            range(daysBeforeStayRange = 0..4, priceRange = 170.01..200.00)
            range(daysBeforeStayRange = 5..10, priceRange = 150.01..170.00)
            range(daysBeforeStayRange = 11..20, priceRange = 110.01..150.00)
            range(daysBeforeStayRange = 21..60, priceRange = 75.00..110.00)
        }

        executeOptimization()
    }
}


fun hotelmodel(op: HotelModel.() -> Unit): HotelModel {
    val newHotelModel = HotelModel()

    newHotelModel.op()

    return newHotelModel
}

class HotelModel {

    private val availableRooms = mutableListOf<Room>()
    private val availablePrices = mutableListOf<Price>()

    fun rooms(op: RoomBuilder.() -> Unit) {
        val builder = RoomBuilder()
        builder.op()
        availableRooms += builder.roomQueue
    }

    fun prices(op: PriceBuilder.() -> Unit) {
        val builder = PriceBuilder()
        builder.op()
        availablePrices += builder.prices
    }

    fun executeOptimization() {
        println("Input contains ${availableRooms.size} rooms and ${availablePrices.size} price ranges.")
        println("Executing optimization operations...")
    }
}

class Room(val bedType: BedType, val isDouble: Boolean = false)

class Price(val daysBeforeStayRange: IntRange, val priceRange: ClosedRange<BigDecimal>)

enum class BedType {
    KING,
    QUEEN
}

class RoomBuilder {
    val roomQueue = mutableListOf<Room>()

    fun queen(quantity: Int) = add(BedType.QUEEN, quantity, false)
    fun king(quantity: Int) = add(BedType.KING, quantity, false)
    fun doublequeen(quantity: Int) = add(BedType.QUEEN, quantity, true)
    fun doubleking(quantity: Int) = add(BedType.KING, quantity, true)

    fun add(bedType: BedType, quantity: Int, isDouble: Boolean)  =
            (1..quantity).asSequence()
                .map { Room(bedType, isDouble) }
                .forEach { roomQueue += it }
}

class PriceBuilder {
    val prices = mutableListOf<Price>()

    fun range(daysBeforeStayRange: IntRange, priceRange: ClosedRange<Double>) {
        prices += Price(
                daysBeforeStayRange,
                BigDecimal.valueOf(priceRange.start)..BigDecimal.valueOf(priceRange.endInclusive)
        )
    }
}
