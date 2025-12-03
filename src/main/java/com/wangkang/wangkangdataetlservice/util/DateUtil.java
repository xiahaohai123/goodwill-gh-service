package com.wangkang.wangkangdataetlservice.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.List;

public class DateUtil {
    private static final Log log = LogFactory.getLog(DateUtil.class);

    private DateUtil() {
    }

    public static final String PATTERN_YYYY_MM_DD_DASH_SEP = "yyyy-MM-dd";
    public static final String PATTERN_YYYY_MM_DASH_SEP = "yyyy-MM";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_PARSER = new DateTimeFormatterBuilder()
            .appendValue(ChronoField.YEAR, 4)
            .appendPattern("-")
            .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendPattern("-")
            .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
            .toFormatter();
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),  // 你提到的格式
            DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_DASH_SEP),
            DateTimeFormatter.ofPattern("MM-dd-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ISO_LOCAL_DATE, // 额外内置格式
            DateTimeFormatter.ISO_LOCAL_DATE_TIME // 内置 LocalDateTime
    );

    public static String todayUTCYMD() {
        // 获取当前 UTC 时间
        ZonedDateTime utcNow = ZonedDateTime.now(ZoneOffset.UTC);

        // 提取 UTC 时区的 LocalDate
        LocalDate utcDate = utcNow.toLocalDate();

        // 格式化为 yyyy-MM-dd 字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_DASH_SEP);
        return utcDate.format(formatter);
    }

    public static String currentYMDHMS() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_TIME_FORMATTER);
    }

    public static String currentUTCIso() {
        return Instant.now().toString();
    }

    public static String parseDateStr2Iso(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_PARSER);
        LocalDateTime dateTime = date.atStartOfDay();
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    public static String toBeijingStartOfDayFormatted(String date) {
        // 1. 解析无时区日期
        LocalDate localDate = LocalDate.parse(date);

        // 2. 构造该日期在 UTC 的 00:00:00
        OffsetDateTime utcStart = localDate.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();

        // 3. 转换为北京时间
        ZoneId beijingZone = ZoneId.of("Asia/Shanghai");
        OffsetDateTime beijingDateTime = utcStart.atZoneSameInstant(beijingZone).toOffsetDateTime();

        // 4. 去掉时区，只保留日期 + 时间
        LocalDateTime localBeijing = beijingDateTime.toLocalDateTime();

        // 5. 返回 ISO 格式
        return localBeijing.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));  // 输出：2025-11-02 08:00:00
    }


    public static String currentUTCTimeWithZone() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return now.format(formatter);
    }

    /**
     * 将时间转换成 LocalDateTime 类型，无关时区
     * @param inputDateStr 输入的时间字符串
     * @return LocalDateTime
     */
    public static LocalDateTime parseAnyDate(String inputDateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // 先尝试 LocalDate
                LocalDate date = LocalDate.parse(inputDateStr, formatter);
                return date.atStartOfDay();
            } catch (DateTimeParseException e) {
                // 尝试 LocalDateTime
                try {
                    return LocalDateTime.parse(inputDateStr, formatter);
                } catch (DateTimeParseException ignored) {
                    log.debug("Failed to parse date " + inputDateStr + " to " + formatter);
                }
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + inputDateStr);
    }

    public static LocalDateTime parseIso2LocalDateTime(String inputDateStr) {
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(inputDateStr, formatter);
            } catch (DateTimeParseException ignored) {
                log.debug("Failed to parse date " + inputDateStr + " to " + formatter);
            }
        }
        throw new IllegalArgumentException("Unsupported date format: " + inputDateStr);
    }

    /**
     * 方法 2: 将 LocalDateTime 格式化为任意格式
     * @param dateTime LocalDateTime 对象
     * @param pattern  格式化的目标格式（如 "yyyy/M/d"）
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    public static String parseTimeStr2Slash(String timeStr) {
        LocalDateTime dateTime = parseAnyDate(timeStr);
        return formatDate(dateTime, "yyyy/M/d");
    }

    public static String formatTime2YMD(LocalDate dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_DASH_SEP);
        return dateTime.format(formatter);
    }

    /**
     * 将 dd/MM/yyyy 格式的字符串转换为 LocalDate
     * @param dateStr 以 "dd/MM/yyyy" 格式表示的日期字符串
     * @return 解析后的 LocalDate
     * @throws IllegalArgumentException 如果解析失败
     */
    public static LocalDate parseLocalDateFromDDMMYYYY(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误，正确格式应为 dd/MM/yyyy，例如 04/03/2025", e);
        }
    }

    /**
     * 将 yyyy-MM-dd 格式的字符串转换为 LocalDate
     * @param dateStr 以 PATTERN_YYYY_MM_DD_DASH_SEP 格式表示的日期字符串
     * @return 解析后的 LocalDate
     * @throws IllegalArgumentException 如果解析失败
     */
    public static LocalDate parseLocalDateFromYYYYMMDDHorizontalLineDivision(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_DASH_SEP);
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误，正确格式应为 yyyy-MM-dd，例如 2025-03-25", e);
        }
    }

    /**
     * 将 yyyy-MM-dd 格式的字符串转换为 OffsetDateTime
     * @param dateStr 以 PATTERN_YYYY_MM_DD_DASH_SEP 格式表示的日期字符串
     * @return 解析后的 OffsetDateTime
     * @throws IllegalArgumentException 如果解析失败
     */
    public static OffsetDateTime parseYMD2OffsetDateTime(String dateStr) {
        // 1. 解析为 LocalDate
        LocalDate localDate = LocalDate.parse(dateStr); // 默认格式为 yyyy-MM-dd

        // 2. 转换为 OffsetDateTime，补上时间和时区偏移
        return localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    /**
     * 将不带时区的 ISO 时间字符串转换为 OffsetDateTime，并强制设置为 UTC。
     * @param isoString 不带时区的 ISO 格式时间，如 "2025-08-18T12:34:56"
     * @return 转换后的 OffsetDateTime（UTC 时区）
     */
    public static OffsetDateTime parseISOFromUTC(String isoString) {
        return parseISOFromZone(isoString, ZoneOffset.UTC);
    }

    public static OffsetDateTime parseISOFromZone(String isoString, ZoneOffset zoneOffset) {
        // 先解析成 LocalDateTime（没有时区）
        LocalDateTime localDateTime = LocalDateTime.parse(isoString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        // 强制指定为 UTC 时区
        return localDateTime.atOffset(zoneOffset);
    }

    /**
     * 格式化 ISO 标准时间字符串为 yyyy-mm 格式的时间字符串
     * 默认传入的时间字符串为 CST 时区，即 UTC+8
     * @param dateStr 时间字符串
     * @return 格式化后的时间字符串
     */
    public static String formatIsoLocalDateTimeCST2YYYYMM(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        // 解析字符串为 LocalDateTime
        OffsetDateTime offsetDateTime = LocalDateTime.parse(dateStr).atOffset(ZoneOffset.ofHours(8));
        // 格式化为 yyyy-MM
        return offsetDateTime.format(DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DASH_SEP));
    }

    /**
     * 格式化 ISO 标准时间字符串为 yyyy-mm 格式的时间字符串
     * 默认传入的时间字符串为 UTC 时区
     * @param dateStr 时间字符串
     * @return 格式化后的时间字符串
     */
    public static String formatIsoTime2YYYYMM(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        // 解析字符串为 LocalDateTime
        OffsetDateTime offsetDateTime = LocalDateTime.parse(dateStr).atOffset(ZoneOffset.UTC);
        // 格式化为 yyyy-MM
        return formatOffsetDateTime2YM(offsetDateTime);
    }

    /**
     * 格式化 ISO 标准时间字符串为 yyyy-mm 格式的时间字符串
     * 默认传入的时间字符串为 UTC 时区
     * @param dateStr 时间字符串
     * @param days    额外增加天数
     * @return 格式化后的时间字符串
     */
    public static String formatIsoTime2YYYYMMPlusDay(String dateStr, long days) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        // 解析字符串为 LocalDateTime
        OffsetDateTime offsetDateTime = LocalDateTime.parse(dateStr).atOffset(ZoneOffset.UTC).plusDays(days);
        // 格式化为 yyyy-MM
        return formatOffsetDateTime2YM(offsetDateTime);
    }

    /**
     * 将东八区的 ISO 时间字符串转换为 UTC ISO 时间字符串
     * @param dateStr 东八区时间字符串，支持两种情况：
     *                1. 带时区: 2025-08-27T10:30:00+08:00
     *                2. 不带时区: 2025-08-27T10:30:00 （会默认认为是东八区）
     * @return UTC ISO 时间字符串，如 2025-08-27T02:30:00Z
     */
    public static String cst2UtcIso(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null; // 处理 null、""、空格等情况
        }
        OffsetDateTime odt;

        if (dateStr.contains("+") || dateStr.endsWith("Z")) {
            // 已经有时区信息
            odt = OffsetDateTime.parse(dateStr);
        } else {
            // 没有时区信息，默认认为是东八区
            LocalDateTime ldt = LocalDateTime.parse(dateStr);
            odt = ldt.atOffset(ZoneOffset.ofHours(8));
        }

        // 转换为 UTC
        OffsetDateTime utcTime = odt.withOffsetSameInstant(ZoneOffset.UTC);

        // 返回 ISO-8601 格式
        return utcTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String formatOffsetDateTime2YMD(OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_DASH_SEP));
    }

    public static String formatOffsetDateTime2YM(OffsetDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DASH_SEP));
    }

    /**
     * Converts a timeString string from one time zone to another.
     * @param timeString the input timeString, e.g. "2025-07-08T22:19:31.293"
     * @param fromOffset the source time zone ID, e.g. "UTC" or "Asia/Shanghai"
     * @param toOffset   the target time zone ID, e.g. "America/New_York"
     * @return the timeString adjusted to the target zone, formatted as "yyyy-MM-dd'T'HH:mm:ss.SSS"
     */
    public static String convertTimezone(
            String timeString,
            ZoneOffset fromOffset,
            ZoneOffset toOffset) {

        // 1. Parse to a LocalDateTime (no zone info yet)
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(timeString, formatter);

        // 2. 打上源偏移，得到 OffsetDateTime
        OffsetDateTime sourceOdt = localDateTime.atOffset(fromOffset);

        // 3. 转换到目标偏移，保留同一瞬间点
        OffsetDateTime targetOdt = sourceOdt.withOffsetSameInstant(toOffset);

        // 4. 输出本地部分，格式与输入一致
        return targetOdt.toLocalDateTime().format(formatter);
    }

    // 获取这个月的开始日期的 ISO 字符串（时间为 00:00:00）
    public static String getFirstDayOfThisMonthISO() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalDateTime dateTime = firstDay.atStartOfDay();
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    // 获取从上个月开始往前推24个月第一天的 ISO 字符串（时间为 00:00:00）
    public static String getFirstDayOf24MonthsAgoISO() {
        LocalDate firstDay = LocalDate.now().minusMonths(24).withDayOfMonth(1);
        LocalDateTime dateTime = firstDay.atStartOfDay();
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static OffsetDateTime getMonthStartOffset(YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1); // 当月第一天
        return firstDay.atStartOfDay().atOffset(ZoneOffset.UTC); // 或使用指定 ZoneOffset.ofHours(+8) 等
    }

    /**
     * 获取今天起点（OffsetDateTime）
     * @return 今天 00:00 的 OffsetDateTime
     */
    public static OffsetDateTime getStartOfTodayUTC() {
        return OffsetDateTime.now(ZoneOffset.UTC)
                .toLocalDate()   // 取日期部分
                .atStartOfDay()  // 变成当天 00:00
                .atOffset(ZoneOffset.UTC);
    }

    public static OffsetDateTime getMonthEndOffset(YearMonth yearMonth) {
        LocalDate lastDay = yearMonth.atEndOfMonth(); // 当月最后一天
        LocalDateTime endOfDay = LocalDateTime.of(lastDay, LocalTime.MAX); // 23:59:59.999999999
        return endOfDay.atOffset(ZoneOffset.UTC);
    }

    /** 获取 UTC前天 0 点的时间 ISO 格式，但是以东八区的时间返回，多用于与金蝶云的交互 */
    public static String getUtcDayBeforeYesterdayMidnightInUtcPlus8() {
        // UTC 时区
        ZoneId utc = ZoneOffset.UTC;
        // 东八区时区
        ZoneId zone8 = ZoneId.of("UTC+8");

        // 前天 UTC 0点
        ZonedDateTime utcMidnight = LocalDate.now(utc)
                .minusDays(2)
                .atStartOfDay(utc);

        // 转换到东八区
        ZonedDateTime beijingTime = utcMidnight.withZoneSameInstant(zone8);

        // 输出 ISO-8601 格式
        return beijingTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static OffsetDateTime parse2OffsetDateTimeDDMMYYYY(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        // 定义输入格式：dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 先解析成 LocalDate
        LocalDate localDate = LocalDate.parse(dateStr, formatter);

        // 转换为 OffsetDateTime，时间设为 00:00:00，时区用 UTC
        return localDate.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

}
