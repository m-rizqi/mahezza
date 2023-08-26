package com.mahezza.mahezza.domain.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FormatDateUseCase {
    companion object {
        const val DEFAULT_PATTERN = "dd MMM yyyy"
    }
    fun format(date : LocalDate) : String {
        return DateTimeFormatter
            .ofPattern(DEFAULT_PATTERN)
            .format(date)
    }
    fun parse(string: String): LocalDate? {
        return try {
            LocalDate.parse(string, DateTimeFormatter.ofPattern(DEFAULT_PATTERN))
        } catch (e: DateTimeParseException) {
            null
        }
    }
}