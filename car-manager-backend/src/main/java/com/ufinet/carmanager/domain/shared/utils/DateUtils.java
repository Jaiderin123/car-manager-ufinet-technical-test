package com.ufinet.carmanager.domain.shared.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {
    private static final ZoneId COLOMBIA_ZONE = ZoneId.of("America/Bogota");

    private DateUtils() {}

    public static boolean isFutureYear(int year){
        return year > LocalDate.now().getYear() + 1;
    }

    public static LocalDateTime nowColombia(){
        return LocalDateTime.now(COLOMBIA_ZONE);
    }
}
