package com.kcc.groo.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * "yyyy-MM-dd" 형식의 문자열을 해당 날짜의 마지막 시간(23:59:59.999...)으로 설정된 LocalDateTime 객체로 변환하는 Deserializer
 *
 * @author YunSung
 * @created 2025-10-21
 */
public class LocalDateToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * JSON의 날짜 문자열을 LocalDateTime으로 변환
     *
     * @param p    JsonParser
     * @param ctxt DeserializationContext
     * @return 변환된 LocalDateTime 객체
     * @throws IOException 파싱 중 예외 발생 시
     * @author YunSung
     * @created 2025-10-21
     */
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        // "yyyy-MM-dd" 형식의 문자열 읽기
        String dateString = p.getText();

        // 빈 문자열 처리
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // 문자열을 LocalDate로 파싱
        LocalDate date = LocalDate.parse(dateString, FORMATTER);

        // 해당 날짜의 마지막 시간(23:59:59.999...)으로 설정된 LocalDateTime 반환
        return LocalDateTime.of(date, LocalTime.MAX);
    }
}
