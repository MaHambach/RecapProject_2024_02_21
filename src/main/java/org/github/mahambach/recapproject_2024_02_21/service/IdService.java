package org.github.mahambach.recapproject_2024_02_21.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdService {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
