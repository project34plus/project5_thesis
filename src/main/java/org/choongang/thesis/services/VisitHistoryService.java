package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.thesis.repositories.VisitHistoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitHistoryService {
    private final VisitHistoryRepository repository;


}
