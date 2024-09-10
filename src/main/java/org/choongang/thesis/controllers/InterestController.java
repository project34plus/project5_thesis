package org.choongang.thesis.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.choongang.global.rests.JSONData;
import org.choongang.thesis.entities.Interests;
import org.choongang.thesis.repositories.InterestsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Interest",description = "관심사 API")
@RestController
@RequiredArgsConstructor
public class InterestController {
    private final InterestsRepository interestsRepository;

    @Operation(summary = "회원 관심사 조회",method = "GET")
    @Parameters
    @GetMapping("/interest/{email}")
    public JSONData interestInfo(@PathVariable("email") String email) {
        List<Interests> interests = interestsRepository.findByEmail(email);

        return new JSONData(interests);
    }
}
