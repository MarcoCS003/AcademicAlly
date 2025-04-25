package com.example.academically.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.example.academically.uiAcademicAlly.DaysOfWeek
import java.time.LocalTime

data class ScheduleTime(
    val day: DaysOfWeek,
    val hourStart: LocalTime,
    val hourEnd: LocalTime
)

data class Schedule(
    val id: Int,
    val color: Color,
    val name: String,
    val place: String,
    val teacher: String,
    val times: List<ScheduleTime>
)

object SampleScheduleData {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSampleSchedules(): List<Schedule> {
        return listOf(
            Schedule(
                id = 1,
                color = Color(0xFFFFF59D), // Amarillo
                name = "ADMINISTRACIÓN GERENCIAL",
                place = "Aula: 07A",
                teacher = "MARIA JUANA CONTRERAS GUZMAN",
                times = listOf(
                    ScheduleTime(DaysOfWeek.LUNES, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                    ScheduleTime(DaysOfWeek.MIERCOLES, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                )
            ),
            Schedule(
                id = 2,
                color = Color(0xFFCE93D8), // Morado claro
                name = "ADMINISTRACIÓN DE PROYECTOS",
                place = "Aula: 07D",
                teacher = "ANA MARIA SOSA PINTLE",
                times = listOf(
                    ScheduleTime(DaysOfWeek.MARTES, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                    ScheduleTime(DaysOfWeek.JUEVES, LocalTime.of(9, 0), LocalTime.of(11, 0)),
                    ScheduleTime(DaysOfWeek.VIERNES, LocalTime.of(10, 0), LocalTime.of(11, 0))
                )
            ),
            Schedule(
                id = 3,
                color = Color(0xFF80DEEA), // Cian claro
                name = "SISTEMAS WEB Y SERVICIOS ORACLE",
                place = "Aula: 36L8",
                teacher = "BEATRIZ PEREZ ROJAS",
                times = listOf(
                    ScheduleTime(DaysOfWeek.MARTES, LocalTime.of(11, 0), LocalTime.of(13, 0)),
                    ScheduleTime(DaysOfWeek.JUEVES, LocalTime.of(11, 30), LocalTime.of(13, 0)),
                    ScheduleTime(DaysOfWeek.VIERNES, LocalTime.of(12, 0), LocalTime.of(13, 0))
                )
            ),
            Schedule(
                id = 4,
                color = Color(0xFFFFAB91), // Salmón
                name = "CUBOS OLAP PARA INTELIGENCIA EMPRESARIAL",
                place = "Aula: 36L3",
                teacher = "JOSÉ OMAR RAMÍREZ MARTHA",
                times = listOf(
                    ScheduleTime(DaysOfWeek.LUNES, LocalTime.of(13, 0), LocalTime.of(15, 0)),
                    ScheduleTime(DaysOfWeek.MIERCOLES, LocalTime.of(13, 0), LocalTime.of(15, 0)),
                    ScheduleTime(DaysOfWeek.VIERNES, LocalTime.of(13, 0), LocalTime.of(14, 0))
                )
            ),
            Schedule(
                id = 5,
                color = Color(0xFFC5E1A5), // Verde claro
                name = "CIBERSEGURIDAD",
                place = "Aula: 36L8",
                teacher = "ANDRÉS MUÑOZ FLORES",
                times = listOf(
                    ScheduleTime(DaysOfWeek.MARTES, LocalTime.of(15, 0), LocalTime.of(17, 0)),
                    ScheduleTime(DaysOfWeek.JUEVES, LocalTime.of(16, 0), LocalTime.of(17, 0)),
                    ScheduleTime(DaysOfWeek.VIERNES, LocalTime.of(16, 0), LocalTime.of(17, 0))
                )
            )
        )
    }
}