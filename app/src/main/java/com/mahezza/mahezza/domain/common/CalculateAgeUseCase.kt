package com.mahezza.mahezza.domain.common

import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CalculateAgeUseCase {
    operator fun invoke(date : LocalDate): Double {
        val currentDate = LocalDate.now()
        val daysBetween = ChronoUnit.DAYS.between(date, currentDate)
        val ageInYears = daysBetween / 365.25 // Consider leap years
        return ageInYears
    }
}