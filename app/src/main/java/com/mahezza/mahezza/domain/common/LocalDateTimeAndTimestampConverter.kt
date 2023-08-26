package com.mahezza.mahezza.domain.common

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class LocalDateTimeAndTimestampConverter {
    fun convertLocalDateTimeToTimestamp(localDateTime: LocalDateTime, zone: ZoneId = ZoneId.systemDefault()) : Timestamp {
        val zonedDateTime = ZonedDateTime.of(localDateTime, zone)
        val instant = zonedDateTime.toInstant()
        val seconds = instant.epochSecond
        val nanos = instant.nano
        return Timestamp(seconds, nanos)
    }

    fun convertTimestampToLocalDateTime(timestamp: Timestamp, zone: ZoneId = ZoneId.systemDefault()) : LocalDateTime {
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong())
        return LocalDateTime.ofInstant(instant, zone)
    }
}

