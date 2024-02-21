package org.github.mahambach.recapproject_2024_02_21.model;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorMessage(
        String apiPath,
        HttpStatus errorCode,
        String errorMsg,
        LocalDateTime errorTime
) {
}
