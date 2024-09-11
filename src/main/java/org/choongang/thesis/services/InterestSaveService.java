package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.thesis.entities.Interests;
import org.choongang.thesis.repositories.InterestsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterestSaveService {
    private final InterestsRepository interestsRepository;

    //    관심사 저장
    public void save(List<Interests> _interests, String email) {

        List<Interests> list = interestsRepository.findAllByEmail(email);
        if (!list.isEmpty()) {//기존 관심사가 존재하는 경우
            interestsRepository.deleteByEmail(email);//삭제 후
            interestsRepository.flush();
            _interests.forEach(interestsRepository::saveAndFlush); //저장
        } else {//기존 관심사가 존재하지 않는 경우
            _interests.forEach(interestsRepository::saveAndFlush); //저장
        }
    }
}
