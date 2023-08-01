package ru.anime.okami.payload;

import java.util.Date;

public record ErrorDetails(Date timestamp, String message, String details) {
}
