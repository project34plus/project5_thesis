package org.choongang.thesis.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Interest",description = "관심사 API")
@RestController
@RequiredArgsConstructor
public class InterestController {
}
