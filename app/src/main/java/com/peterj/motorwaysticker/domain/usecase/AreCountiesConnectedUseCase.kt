package com.peterj.motorwaysticker.domain.usecase

import javax.inject.Inject

class AreCountiesConnectedUseCase @Inject constructor() {
    private val neighborCounties = mapOf(
        "Bács-Kiskun" to listOf("Baranya", "Tolna", "Jász-Nagykun-Szolnok", "Csongrád", "Pest", "Fejér"),
        "Baranya" to listOf("Bács-Kiskun", "Tolna", "Somogy"),
        "Békés" to listOf("Csongrád", "Jász-Nagykun-Szolnok", "Hajdú-Bihar"),
        "Borsod-Abaúj-Zemplén" to listOf("Heves", "Szabolcs-Szatmár-Bereg", "Hajdú-Bihar", "Nógrád"),
        "Csongrád" to listOf("Bács-Kiskun", "Békés", "Jász-Nagykun-Szolnok"),
        "Fejér" to listOf("Komárom-Esztergom", "Pest", "Tolna", "Veszprém", "Bács-Kiskun", "Somogy"),
        "Győr-Moson-Sopron" to listOf("Komárom-Esztergom", "Vas", "Veszprém"),
        "Hajdú-Bihar" to listOf("Békés", "Jász-Nagykun-Szolnok", "Szabolcs-Szatmár-Bereg", "Borsod-Abaúj-Zemplén"),
        "Heves" to listOf("Nógrád", "Borsod-Abaúj-Zemplén", "Jász-Nagykun-Szolnok", "Pest",),
        "Jász-Nagykun-Szolnok" to listOf("Heves", "Pest", "Bács-Kiskun", "Békés", "Hajdú-Bihar", "Csongrád"),
        "Komárom-Esztergom" to listOf("Győr-Moson-Sopron", "Fejér", "Pest", "Veszprém"),
        "Nógrád" to listOf("Heves", "Pest", "Borsod-Abaúj-Zemplén"),
        "Pest" to listOf("Komárom-Esztergom", "Fejér", "Bács-Kiskun", "Jász-Nagykun-Szolnok", "Heves", "Nógrád",),
        "Somogy" to listOf("Baranya", "Tolna", "Zala", "Fejér", "Veszprém"),
        "Szabolcs-Szatmár-Bereg" to listOf("Borsod-Abaúj-Zemplén", "Hajdú-Bihar"),
        "Tolna" to listOf("Fejér", "Bács-Kiskun", "Baranya", "Somogy"),
        "Vas" to listOf("Győr-Moson-Sopron", "Zala", "Veszprém"),
        "Veszprém" to listOf("Fejér", "Győr-Moson-Sopron", "Somogy", "Zala", "Vas", "Komárom-Esztergom"),
        "Zala" to listOf("Vas", "Somogy", "Veszprém"),
    )

    fun execute(counties: List<String>): Boolean {
        if (counties.isEmpty()) return false

        val visited = mutableSetOf<String>()
        val stack = ArrayDeque<String>()
        val set = counties.toSet()

        stack.add(counties.first())

        while (stack.isNotEmpty()) {
            val county = stack.removeLast()
            if (county !in visited) {
                visited.add(county)
                stack.addAll((neighborCounties[county] ?: emptyList()).filter { it in set })
            }
        }

        return visited.size == counties.size
    }
}