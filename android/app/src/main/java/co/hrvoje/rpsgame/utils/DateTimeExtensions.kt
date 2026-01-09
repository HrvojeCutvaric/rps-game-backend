import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.formatGameTime(): String {
    val dateTime = Instant
        .fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return "%02d.%02d. %02d:%02dh".format(
        dateTime.dayOfMonth,
        dateTime.monthNumber,
        dateTime.hour,
        dateTime.minute
    )
}
