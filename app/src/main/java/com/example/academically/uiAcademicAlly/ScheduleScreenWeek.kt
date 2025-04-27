package com.example.academically.uiAcademicAlly

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academically.data.Schedule
import com.example.academically.data.ScheduleTime
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekNavigationHeader(
    currentDate: LocalDate,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onViewModeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekEnd = weekStart.plusDays(6)
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("es", "ES"))

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${weekStart.format(formatter)} - ${weekEnd.format(formatter)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = weekStart.month.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                    .replaceFirstChar { it.uppercase() } + " ${weekStart.year}",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }


        IconButton(onClick = onViewModeChange) {
            Icon(
                Icons.Default.CalendarViewDay,
                modifier = Modifier.size(38.dp).padding(horizontal = 5.dp),
                contentDescription = "Vista diaria"
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyScheduleView(
    schedules: List<Schedule>,
    currentDate: LocalDate,
    startHour: Int,
    endHour: Int,
    modifier: Modifier = Modifier
) {
    val hourHeight = 70
    val columnWidth = 120
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()

    // Obtener el lunes de la semana actual
    val weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val weekDays = listOf(
        DaysOfWeek.LUNES,
        DaysOfWeek.MARTES,
        DaysOfWeek.MIERCOLES,
        DaysOfWeek.JUEVES,
        DaysOfWeek.VIERNES,
        DaysOfWeek.SABADO,
        DaysOfWeek.DOMINGO
    )

    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        // Columna de horas fija
        Column(
            modifier = Modifier
                .width(20.dp)
                .padding(top = 40.dp) // Espacio para los encabezados de días
        ) {
            // Contenedor con scroll sincronizado para las horas
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(verticalScrollState)
            ) {
                Column {
                    for (hour in startHour..endHour) {
                        Text(
                            text = String.format(Locale.getDefault(), "%02d", hour),
                            modifier = Modifier
                                .height(hourHeight.dp)
                                .padding(end = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }

        // Contenido con scroll horizontal y vertical
        Box(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(horizontalScrollState)
        ) {
            Column {
                // Encabezados de días
                Row {
                    weekDays.forEach { day ->
                        Text(
                            text = day.name.lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                            modifier = Modifier
                                .width(columnWidth.dp)
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Grid de horarios con scroll vertical
                Box(
                    modifier = Modifier
                        .verticalScroll(verticalScrollState)
                ) {
                    // Fondo con líneas de hora
                    WeeklyTimeGridBackground(
                        hourHeight = hourHeight,
                        columnWidth = columnWidth,
                        weekDays = weekDays.size,
                        startHour = startHour,
                        endHour = endHour
                    )

                    // Eventos
                    Row {
                        weekDays.forEach { day ->
                            Box(
                                modifier = Modifier
                                    .width(columnWidth.dp)
                                    .height((hourHeight * (endHour - startHour + 1)).dp)
                            ) {
                                // Filtrar horarios para el día actual
                                val daySchedules = schedules.mapNotNull { schedule ->
                                    schedule.times.find { it.day == day }?.let { time ->
                                        schedule to time
                                    }
                                }

                                daySchedules.forEach { (schedule, time) ->
                                    PositionedWeeklyScheduleCard(
                                        schedule = schedule,
                                        time = time,
                                        hourHeight = hourHeight,
                                        columnWidth = columnWidth,
                                        baseHour = startHour
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WeeklyTimeGridBackground(
    hourHeight: Int,
    columnWidth: Int,
    weekDays: Int,
    startHour: Int,
    endHour: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        for (hour in startHour..endHour) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(hourHeight.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.align(Alignment.TopCenter),
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PositionedWeeklyScheduleCard(
    schedule: Schedule,
    time: ScheduleTime,
    hourHeight: Int,
    columnWidth: Int,
    baseHour: Int,
    modifier: Modifier = Modifier
) {
    val minutesPerPixel = hourHeight / 60f

    // Calcular la posición desde la hora base
    val startMinutesFromBase = (time.hourStart.hour - baseHour) * 60 + time.hourStart.minute
    val topOffset = (startMinutesFromBase * minutesPerPixel).dp

    // Calcular la duración
    val durationMinutes = (time.hourEnd.hour * 60 + time.hourEnd.minute) -
            (time.hourStart.hour * 60 + time.hourStart.minute)
    val cardHeight = (durationMinutes.times(minutesPerPixel)).dp

    WeeklyScheduleCard(
        schedule = schedule,
        time = time,
        modifier = modifier
            .padding(horizontal = 2.dp)
            .offset(y = topOffset)
            .height(cardHeight)
            .width(120.dp)
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeeklyScheduleCard(
    schedule: Schedule,
    time: ScheduleTime,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = schedule.color.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = schedule.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = schedule.place,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 10.sp,
                color = Color.Black
            )

            if (time.hourEnd.hour - time.hourStart.hour > 1) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = schedule.teacher,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            }
        }
    }
}
