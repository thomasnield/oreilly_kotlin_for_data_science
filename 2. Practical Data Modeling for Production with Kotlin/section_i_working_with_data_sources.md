# Section I: Reading Data Sources

Reading a text file (that is under 2GB) is pretty simple in Kotlin.


## 1-1A: Reading a File

```kotlin
import java.io.File
import java.time.LocalDate


fun main(args: Array<String>) {

    val lines = File("/home/thomas/Desktop/customer_orders.csv").readLines()

    for (l in lines) {
        println(l)
    }
}
```


## 1-1B: Reading a File


Of course, you can also use a `forEach()` with a lambda:

```kotlin
import java.io.File
import java.time.LocalDate


fun main(args: Array<String>) {

    val lines = File("/home/thomas/Desktop/customer_orders.csv").readLines()

    lines.forEach {
        println(it)
    }
}
```

## 1-1C: Reading a Large File

For files larger than 2GB, you may want to use a `BufferedReader` instead.

```Kotlin
import java.io.File
import java.time.LocalDate


fun main(args: Array<String>) {

    val reader = File("/home/thomas/Desktop/customer_orders.csv").bufferedReader()

    reader.forEachLine {
        println(it)
    }
}
```

## 1-1D: Turning Each Record into a Data Class Instance

We can turn each line of the file into an instance of a `CustomerOrder` data class.

```kotlin
import java.io.File
import java.time.LocalDate


fun main(args: Array<String>) {

    val reader = File("/home/thomas/Desktop/customer_orders.csv").bufferedReader()

    val orders = reader.readLines()
            .drop(1)  //skip header line
            .map { it.split(",") }
            .map {
                CustomerOrder(
                        customerOrderId = it[0].toInt(),
                        customerId = it[1].toInt(),
                        orderDate = LocalDate.parse(it[2]),
                        productId = it[3].toInt(),
                        quantity = it[4].toInt()
                )
            }

    orders.forEach {
        println(it)
    }
}

data class CustomerOrder(
        val customerOrderId: Int,
        val customerId: Int,
        val orderDate: LocalDate,
        val productId: Int,
        val quantity: Int
)
```

This would be more efficient if we used Sequences (which effectively work like assembly lines) rather than creating all these intermediary collections with collection operators, which we will learn about in the next section.

## 1-2: Writing to a File

You can also write to files. There are functional, advanced ways to express this in fewer lines, but here is the most basic way:

```Kotlin
import java.io.File
import java.time.LocalDate


fun main(args: Array<String>) {

    val writer = File("/home/thomas/Desktop/output.csv").bufferedWriter()

    val orders = listOf(
            CustomerOrder(2, 5, LocalDate.of(2017,1,1), 15, 110),
            CustomerOrder(12, 5, LocalDate.of(2017,1,1), 10, 90),
            CustomerOrder(26, 5, LocalDate.of(2017,1,2), 7, 120)
    )

    orders.forEach {
                writer.write("${it.customerOrderId},${it.customerId},${it.orderDate},${it.productId},${it.quantity}")
                writer.newLine()
            }

    writer.flush()
}

data class CustomerOrder(
        val customerOrderId: Int,
        val customerId: Int,
        val orderDate: LocalDate,
        val productId: Int,
        val quantity: Int
)
```


## 1-3A: Reading a SQL Query

To work with data sources, you typically use a JDBC connection (Java's standard way of connecting to a database). You will need to get the proper JDBC driver and bring it into your Maven dependencies.

In this example, we are going to use SQLite. Add the following dependency to your Maven `pom.xml`:

```xml
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.20.0</version>
</dependency>
```

Unfortunately,the Kotlin standard library has no streamlined utilities for JDBC so there are a few steps you will need to do. Of course, you can create your own extensions or use other Kotlin libraries, but here is the vanilla way to create a List of `CustomerOrder`s off a SQL query:


```Kotlin
import java.sql.DriverManager
import java.time.LocalDate


fun main(args: Array<String>) {
    val conn = DriverManager.getConnection("jdbc:sqlite:/home/thomas/Desktop/thunderbird_manufacturing.db")

    val ps = conn.prepareStatement("SELECT * FROM CUSTOMER_ORDER")
    val rs = ps.executeQuery()

    val orders = mutableListOf<CustomerOrder>()

    while (rs.next()) {
        orders += CustomerOrder(
                rs.getInt("CUSTOMER_ORDER_ID"),
                rs.getInt("CUSTOMER_ID"),
                LocalDate.parse(rs.getString("ORDER_DATE")),
                rs.getInt("PRODUCT_ID"),
                rs.getInt("QUANTITY")
        )
    }

    ps.close()

    orders.forEach { println(it) }
}

data class CustomerOrder(
        val customerOrderId: Int,
        val customerId: Int,
        val orderDate: LocalDate,
        val productId: Int,
        val quantity: Int
)
```

## 1-3B: Passing Parameters to a query


To pass parameters to a query, use a `PreparedStatement` to safely inject them. This will avoid inadvertent or malicious SQL injection.


```kotlin
import java.sql.DriverManager
import java.time.LocalDate


fun main(args: Array<String>) {

    val orders = ordersForCustomerId(5)
    orders.forEach { println(it) }
}

val conn = DriverManager.getConnection("jdbc:sqlite:/home/thomas/Desktop/thunderbird_manufacturing.db")

fun ordersForCustomerId(customerId: Int): List<CustomerOrder> {
    val ps = conn.prepareStatement("SELECT * FROM CUSTOMER_ORDER WHERE CUSTOMER_ID = ?")

    ps.setInt(1,customerId)

    val rs = ps.executeQuery()

    val orders = mutableListOf<CustomerOrder>()

    while (rs.next()) {
        orders += CustomerOrder(
                rs.getInt("CUSTOMER_ORDER_ID"),
                rs.getInt("CUSTOMER_ID"),
                LocalDate.parse(rs.getString("ORDER_DATE")),
                rs.getInt("PRODUCT_ID"),
                rs.getInt("QUANTITY")
        )
    }

    ps.close()
    return orders
}

data class CustomerOrder(
        val customerOrderId: Int,
        val customerId: Int,
        val orderDate: LocalDate,
        val productId: Int,
        val quantity: Int
)
```

## 1-4A: Working with web requests

Kotlin should be able to retrieve any remote resources using standard protocols.

For instance, you can process an HTTP request fairly easily. Below, we read a Gist on GitHub containing a text file of the 50 U.S. states.

```kotlin
import java.net.URL

fun main(args: Array<String>) {

    val usStates = URL("https://goo.gl/S0xuOi").readText().split(Regex("\\r?\\n"))

    println(usStates)
}
```

## 1-4B: Lazy Properties

Sometimes it can be helpful to lazily initialize a property, especially for large expensive data sets. The benefit of lazy properties is they will not eagerly initialize when the containing Kotlin file is used. Rather, they will construct the moment that property is called.

Lazy properties are enabled by Kotlin's property delegation, which we will not dive into in depth but you can read about in the [Kotlin Reference](https://kotlinlang.org/docs/reference/delegated-properties.html). For our purposes, all you need to do is call `by lazy { }` next to your property declaration and the data set will go into that lambda argument.


```kotlin
import java.net.URL

fun main(args: Array<String>) {

    println("Application started without loading U.S. states")
    println(usStates) //usStates will load now that its called
}

val usStates by lazy {
    println("property called, loading and caching!")
    URL("https://goo.gl/S0xuOi").readText().split(Regex("\\r?\\n"))
}
```
