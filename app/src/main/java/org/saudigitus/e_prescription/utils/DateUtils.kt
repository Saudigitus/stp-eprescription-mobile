package org.saudigitus.e_prescription.utils

/**
 * @DHIS2
 */

import android.os.Build
import androidx.annotation.RequiresApi
import org.hisp.dhis.android.core.event.EventStatus
import org.hisp.dhis.android.core.period.PeriodType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class DateUtils {
    private var currentDateCalendar: Calendar? = null
    fun getDateFromDateAndPeriod(date: Date, period: Period?): Array<Date> {
        return when (period) {
            Period.YEARLY -> arrayOf(getFirstDayOfYear(date), getLastDayOfYear(date))
            Period.MONTHLY -> arrayOf(getFirstDayOfMonth(date), getLastDayOfMonth(date))
            Period.WEEKLY -> arrayOf(getFirstDayOfWeek(date), getLastDayOfWeek(date))
            Period.DAILY -> arrayOf(getDate(date), getDate(date))
            else -> arrayOf(getDate(date), getDate(date))
        }
    }

    val today: Date
        /**********************
         * CURRENT PEDIOD REGION */
        get() = calendar.time

    /**********************
     * SELECTED PEDIOD REGION */
    private fun getDate(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    private fun getNextDate(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    private fun getFirstDayOfWeek(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek
        return calendar.time
    }

    private fun getLastDayOfWeek(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_WEEK] = calendar.firstDayOfWeek
        calendar.add(Calendar.WEEK_OF_YEAR, 1) //Move to next week
        calendar.add(Calendar.DAY_OF_MONTH, -1) //Substract one day to get last day of current week
        return calendar.time
    }

    private fun getFirstDayOfMonth(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_MONTH] = 1
        return calendar.time
    }

    private fun getLastDayOfMonth(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

    private fun getFirstDayOfYear(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_YEAR] = 1
        return calendar.time
    }

    private fun getLastDayOfYear(date: Date): Date {
        val calendar = calendar
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        calendar[Calendar.DAY_OF_YEAR] = 1
        calendar.add(Calendar.YEAR, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.time
    }

    /**********************
     * FORMAT REGION */
    fun formatDate(dateToFormat: Date?): String {
        return uiDateFormat().format(dateToFormat)
    }

    val calendar: Calendar
        get() {
            if (currentDateCalendar != null) return currentDateCalendar as Calendar
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return calendar
        }

    fun getCalendarByDate(date: Date?): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar
    }

    fun setCurrentDate(date: Date?) {
        currentDateCalendar = calendar
        currentDateCalendar!!.time = date
        currentDateCalendar!![Calendar.HOUR_OF_DAY] = 0
        currentDateCalendar!![Calendar.MINUTE] = 0
        currentDateCalendar!![Calendar.SECOND] = 0
        currentDateCalendar!![Calendar.MILLISECOND] = 0
    }

    /**
     * Check if an event is expired today.
     *
     * @param eventDate         Date of the event (Can be either eventDate or dueDate, but can not be null).
     * @param completeDate      date that event was completed (can be null).
     * @param status            status of event (ACTIVE,COMPLETED,SCHEDULE,OVERDUE,SKIPPED,VISITED).
     * @param compExpDays       extra days to edit event when completed .
     * @param programPeriodType period in which the event can be edited.
     * @param expDays           extra days after period to edit event.
     * @return true or false
     */
    fun isEventExpired(
        eventDate: Date?,
        completeDate: Date?,
        status: EventStatus,
        compExpDays: Int,
        programPeriodType: PeriodType?,
        expDays: Int
    ): Boolean {
        if (status == EventStatus.COMPLETED && completeDate == null) return false
        val expiredBecouseOfPeriod: Boolean
        var expiredBecouseOfCompletion = false
        expiredBecouseOfCompletion = if (status == EventStatus.COMPLETED) isEventExpired(
            null,
            eventDate,
            compExpDays
        ) else false
        return if (programPeriodType != null) {
            var expDate: Date? =
                getNextPeriod(programPeriodType, eventDate, 1) //Initial date of next period
            val currentDate = calendar.time
            if (expDays > 0) {
                val calendar = calendar
                calendar.time = expDate
                calendar.add(Calendar.DAY_OF_YEAR, expDays)
                expDate = calendar.time
            }
            expiredBecouseOfPeriod = expDate != null && expDate.compareTo(currentDate) <= 0
            expiredBecouseOfPeriod || expiredBecouseOfCompletion
        } else expiredBecouseOfCompletion
    }

    /**
     * Check if an event is expired in a date
     *
     * @param currentDate  date or today if null
     * @param completedDay date that event was completed
     * @param compExpDays  days of expiration of an event
     * @return true or false
     */
    fun isEventExpired(currentDate: Date?, completedDay: Date?, compExpDays: Int): Boolean {
        val calendar = calendar
        if (currentDate != null) calendar.time = currentDate
        val date = calendar.time
        return completedDay != null && compExpDays > 0 && completedDay.time + TimeUnit.DAYS.toMillis(
            compExpDays.toLong()
        ) < date.time
    }

    /**
     * @param period      Period in which the date will be selected
     * @param currentDate Current selected date
     * @param page        1 for next, 0 for now, -1 for previous
     * @return Next/Previous date calculated from the currentDate and Period
     */
    fun getNextPeriod(period: PeriodType?, currentDate: Date?, page: Int): Date {
        var period = period
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        val extra: Int
        if (period == null) period = PeriodType.Daily
        when (period) {
            PeriodType.Daily -> calendar.add(Calendar.DAY_OF_YEAR, page)
            PeriodType.Weekly -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }

            PeriodType.WeeklyWednesday -> {
                calendar.firstDayOfWeek = Calendar.WEDNESDAY
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.WEDNESDAY
            }

            PeriodType.WeeklyThursday -> {
                calendar.firstDayOfWeek = Calendar.THURSDAY
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.THURSDAY
            }

            PeriodType.WeeklySaturday -> {
                calendar.firstDayOfWeek = Calendar.SATURDAY
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.SATURDAY
            }

            PeriodType.WeeklySunday -> {
                calendar.firstDayOfWeek = Calendar.SUNDAY
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
            }

            PeriodType.BiWeekly -> {
                extra = if (calendar[Calendar.WEEK_OF_YEAR] % 2 == 0) 1 else 2
                calendar.add(Calendar.WEEK_OF_YEAR, page * extra)
                calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }

            PeriodType.Monthly -> {
                calendar.add(Calendar.MONTH, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.BiMonthly -> {
                extra = if ((calendar[Calendar.MONTH] + 1) % 2 == 0) 1 else 2
                calendar.add(Calendar.MONTH, page * extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.Quarterly -> {
                extra = 3 * page - calendar[Calendar.MONTH] % 3
                calendar.add(Calendar.MONTH, extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.SixMonthly -> {
                extra = 6 * page - calendar[Calendar.MONTH] % 6
                calendar.add(Calendar.MONTH, extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.SixMonthlyApril -> {
                if (calendar[Calendar.MONTH] < Calendar.APRIL) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.OCTOBER
                } else if (calendar[Calendar.MONTH] >= Calendar.APRIL && calendar[Calendar.MONTH] < Calendar.OCTOBER) calendar[Calendar.MONTH] =
                    Calendar.APRIL else calendar[Calendar.MONTH] = Calendar.OCTOBER
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar.add(Calendar.MONTH, page * 6)
            }

            PeriodType.SixMonthlyNov -> {
                if (calendar[Calendar.MONTH] < Calendar.MAY) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.NOVEMBER
                } else if (calendar[Calendar.MONTH] >= Calendar.MAY && calendar[Calendar.MONTH] < Calendar.NOVEMBER) calendar[Calendar.MONTH] =
                    Calendar.MAY else calendar[Calendar.MONTH] = Calendar.NOVEMBER
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar.add(Calendar.MONTH, page * 6)
            }

            PeriodType.Yearly -> {
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_YEAR] = 1
            }

            PeriodType.FinancialApril -> {
                if (calendar[Calendar.MONTH] < Calendar.APRIL) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.APRIL
                } else calendar[Calendar.MONTH] = Calendar.APRIL
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.FinancialJuly -> {
                if (calendar[Calendar.MONTH] < Calendar.JULY) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.JULY
                } else calendar[Calendar.MONTH] = Calendar.JULY
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.FinancialOct -> {
                if (calendar[Calendar.MONTH] < Calendar.OCTOBER) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.OCTOBER
                } else calendar[Calendar.MONTH] = Calendar.OCTOBER
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.FinancialNov -> {
                if (calendar[Calendar.MONTH] < Calendar.NOVEMBER) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.NOVEMBER
                } else calendar[Calendar.MONTH] = Calendar.NOVEMBER
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            else -> {}
        }
        return calendar.time
    }

    /**
     * @param period      Period in which the date will be selected
     * @param currentDate Current selected date
     * @param page        1 for next, 0 for now, -1 for previous
     * @return Next/Previous date calculated from the currentDate and Period
     */
    fun getNextPeriod(period: PeriodType?, currentDate: Date?, page: Int, lastDate: Boolean): Date {
        var period = period
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        val extra: Int
        if (period == null) period = PeriodType.Daily
        when (period) {
            PeriodType.Daily -> calendar.add(Calendar.DAY_OF_YEAR, page)
            PeriodType.Weekly -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.MONDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.SUNDAY
            }

            PeriodType.WeeklyWednesday -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.WEDNESDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.THURSDAY
            }

            PeriodType.WeeklyThursday -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.THURSDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.MONDAY
            }

            PeriodType.WeeklySaturday -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.SATURDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.FRIDAY
            }

            PeriodType.WeeklySunday -> {
                calendar.add(Calendar.WEEK_OF_YEAR, page)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.SUNDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.SATURDAY
            }

            PeriodType.BiWeekly -> {
                extra = if (calendar[Calendar.WEEK_OF_YEAR] % 2 == 0) 1 else 2
                calendar.add(Calendar.WEEK_OF_YEAR, page * extra)
                if (!lastDate) calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.MONDAY else calendar[Calendar.DAY_OF_WEEK] =
                    Calendar.SUNDAY
            }

            PeriodType.Monthly -> {
                calendar.add(Calendar.MONTH, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.BiMonthly -> {
                extra = if ((calendar[Calendar.MONTH] + 1) % 2 == 0) 1 else 2
                calendar.add(Calendar.MONTH, page * extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.Quarterly -> {
                extra = 1 + 4 - (calendar[Calendar.MONTH] + 1) % 4
                calendar.add(Calendar.MONTH, page * extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.SixMonthly -> {
                extra = 1 + 6 - (calendar[Calendar.MONTH] + 1) % 6
                calendar.add(Calendar.MONTH, page * extra)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.SixMonthlyApril -> {
                if (calendar[Calendar.MONTH] < Calendar.APRIL) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.OCTOBER
                } else if (calendar[Calendar.MONTH] >= Calendar.APRIL && calendar[Calendar.MONTH] < Calendar.OCTOBER) calendar[Calendar.MONTH] =
                    Calendar.APRIL else calendar[Calendar.MONTH] = Calendar.OCTOBER
                calendar[Calendar.DAY_OF_MONTH] = 1
                calendar.add(Calendar.MONTH, page * 6)
            }

            PeriodType.Yearly -> {
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_YEAR] = 1
            }

            PeriodType.FinancialApril -> {
                if (calendar[Calendar.MONTH] < Calendar.APRIL) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.APRIL
                } else calendar[Calendar.MONTH] = Calendar.APRIL
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.FinancialJuly -> {
                if (calendar[Calendar.MONTH] < Calendar.JULY) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.JULY
                } else calendar[Calendar.MONTH] = Calendar.JULY
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            PeriodType.FinancialOct -> {
                if (calendar[Calendar.MONTH] < Calendar.OCTOBER) {
                    calendar.add(Calendar.YEAR, -1)
                    calendar[Calendar.MONTH] = Calendar.OCTOBER
                } else calendar[Calendar.MONTH] = Calendar.OCTOBER
                calendar.add(Calendar.YEAR, page)
                calendar[Calendar.DAY_OF_MONTH] = 1
            }

            else -> {}
        }
        return calendar.time
    }

    companion object {
        var instance: DateUtils? = null
            get() {
                if (field == null) field = DateUtils()
                return field
            }
            private set
        const val DATABASE_FORMAT_EXPRESSION = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val DATABASE_FORMAT_EXPRESSION_NO_MILLIS = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATABASE_FORMAT_EXPRESSION_NO_SECONDS = "yyyy-MM-dd'T'HH:mm"
        const val DATE_TIME_FORMAT_EXPRESSION = "yyyy-MM-dd HH:mm"
        const val DATE_FORMAT_EXPRESSION = "yyyy-MM-dd"
        const val WEEKLY_FORMAT_EXPRESSION = "w yyyy"
        const val MONTHLY_FORMAT_EXPRESSION = "MMM yyyy"
        const val YEARLY_FORMAT_EXPRESSION = "yyyy"
        const val SIMPLE_DATE_FORMAT = "d/M/yyyy"
        const val TIME_12H_EXPRESSION = "hh:mm a"
        fun uiDateFormat(): SimpleDateFormat {
            return SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.US)
        }

        fun oldUiDateFormat(): SimpleDateFormat {
            return SimpleDateFormat(DATE_FORMAT_EXPRESSION, Locale.US)
        }

        fun timeFormat(): SimpleDateFormat {
            return SimpleDateFormat("HH:mm", Locale.US)
        }

        fun dateTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat(DATE_TIME_FORMAT_EXPRESSION, Locale.US)
        }

        fun databaseDateFormat(): SimpleDateFormat {
            return SimpleDateFormat(DATABASE_FORMAT_EXPRESSION, Locale.US)
        }

        fun databaseDateFormatNoMillis(): SimpleDateFormat {
            return SimpleDateFormat(DATABASE_FORMAT_EXPRESSION_NO_MILLIS, Locale.US)
        }

        fun databaseDateFormatNoSeconds(): SimpleDateFormat {
            return SimpleDateFormat(DATABASE_FORMAT_EXPRESSION_NO_SECONDS, Locale.US)
        }

        fun dateHasNoSeconds(dateTime: String?): Boolean {
            return try {
                databaseDateFormatNoSeconds().parse(dateTime)
                true
            } catch (e: ParseException) {
                false
            }
        }

        fun twelveHourTimeFormat(): SimpleDateFormat {
            return SimpleDateFormat(TIME_12H_EXPRESSION, Locale.US)
        }

        /**********************
         * COMPARE DATES REGION */
        fun getDifference(startDate: Date, endDate: Date): IntArray {
            val interval =
                org.joda.time.Period(startDate.time, endDate.time, org.joda.time.PeriodType.yearMonthDayTime())
            return intArrayOf(interval.years, interval.months, interval.days)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDateTimePattern(): DateTimeFormatter = DateTimeFormatter
            .ofPattern(Constants.DATETIME_FORMAT)


        @RequiresApi(Build.VERSION_CODES.O)
        fun getDatePattern(): DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

        private val dateFormat: SimpleDateFormat = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)

        fun formatDate(date: Long): String? {
            return try {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                formatter.format(Date(date))
            } catch (e: Exception) {
                null
            }
        }

        fun formatDate(date: Date?): String? {
            if (date == null) {
                return null
            }
            val dateFormat = SimpleDateFormat(Constants.DATETIME_FORMAT, Locale.US)
            return dateFormat.format(date)
        }

        fun formatSimpleDate(date: Date): String? {
            return dateFormat.format(date)
        }

        @Throws(ParseException::class)
        fun parseSimpleDate(date: String): Date? {
            return dateFormat.parse(date)
        }

        /**
         * Returns the start of today in milliseconds
         */
        fun getDefaultDateInMillis(): Long {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val date = cal.get(Calendar.DATE)
            cal.clear()
            cal.set(year, month, date)
            return cal.timeInMillis
        }

        fun formatDateWithWeekDay(date: String): String? {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("PT"))
                val outputFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale("PT"))

                val inputDate: Date = inputFormat.parse(date)!!
                outputFormat.format(inputDate)
            } catch (e: Exception) {
                null
            }
        }

        fun String.toLocalDate(): LocalDate? {
            var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            formatter = formatter.withLocale(Locale("PT"))

            return LocalDate.parse(this, formatter)
        }
    }
}