package org.example.expert.domain.log.service;


import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 새로운 트랜잭션 생성
    public void logAction(String action, Long userId, String logDescription){
        Log log = new Log();
        log.setLogTime(LocalDateTime.now());
        log.setAction(action);
        log.setUserId(userId);
        log.setLogDescription(logDescription);
        logRepository.save(log);
    }
}
