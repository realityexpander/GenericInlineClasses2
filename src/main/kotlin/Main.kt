sealed class Vehicle
data class Car(val make: String, val model: String) : Vehicle()
data class Truck(val make: String, val model: String, val towingCapacity: Int) : Vehicle()

data class Kebab(val meat: String, val vegetables: List<String>)  // note: not a vehicle

@JvmInline
value class HighQualityUnconstrainedType(val thing: Any) // Problem is that harder to use with a specific subtype (must check at runtime)

// Note: Must check that the type passed in is ACTUALLY a vehicle at runtime
fun inspectHQVehicleUnconstrained(pleasePassOnlyVehicle: HighQualityUnconstrainedType) {

    // Must check the type at runtime for unconstrained types of high quality things
    val vehicle: Vehicle = pleasePassOnlyVehicle.thing as? Vehicle
        ?: throw IllegalArgumentException("Not a vehicle")

    when (vehicle) {
        is Car -> println("Unconstrained Car: ${vehicle.make} ${vehicle.model}")
        is Truck -> println("Unconstrained Truck: ${vehicle.make} ${vehicle.model} ${vehicle.towingCapacity}")
    }
}

@JvmInline
value class HighQualityConstrainedType<T>(val thing: T)

// Note: No special runtime checks needed, because the type is constrained and the check is done at compile time
fun inspectHQVehicleConstrained(hqVehicle: HighQualityConstrainedType<Vehicle>) {

    // Note: no runtime check needed, only Vehicles are allowed to be passed in

    when (val vehicle = hqVehicle.thing) {
        is Car -> println("Constrained Car: ${vehicle.make} ${vehicle.model}")
        is Truck -> println("Constrained Truck: ${vehicle.make} ${vehicle.model} ${vehicle.towingCapacity}")
    }
}


fun main() {
    val car = Car("Toyota", "Corolla")
    val truck = Truck("Ford", "F150", 1000)
    val kebab = Kebab("lamb", listOf("tomato", "onion", "cucumber"))

    val carHQUnconstrained = HighQualityUnconstrainedType(car)
    val truckHQUnconstrained = HighQualityUnconstrainedType(truck)
    val kebabHQUnconstrained = HighQualityUnconstrainedType(kebab)

    // Using the HighQualityUnconstrainedType
    try {
        inspectHQVehicleUnconstrained(carHQUnconstrained)     // Car: Toyota Corolla
        inspectHQVehicleUnconstrained(truckHQUnconstrained)   // Truck: Ford F150 1000
        inspectHQVehicleUnconstrained(kebabHQUnconstrained)   // IllegalArgumentException: Not a vehicle
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    println()

    // Using the HighQualityConstrainedType
    inspectHQVehicleConstrained(HighQualityConstrainedType(car))     // Car: Toyota Corolla
    inspectHQVehicleConstrained(HighQualityConstrainedType(truck))   // Truck: Ford F150 1000
    //inspectHQVehicleConstrained(HighQualityConstrainedType(kebab))   // Gives error at compile time (not a vehicle)
}


