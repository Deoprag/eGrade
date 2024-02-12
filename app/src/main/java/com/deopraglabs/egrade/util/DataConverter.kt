package com.deopraglabs.egrade.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DataConverter {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(formatter)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, formatter) }
    }
}