package org.choongang.thesis.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.choongang.global.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish", description = "위시리스트 API")
@RestController("/wish")
@RequiredArgsConstructor
public class WishListController {

    //위시 리스트 조회
    @Operation(summary = "위시리스트 목록 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping
    public JSONData list() {
        return null;
    }

    // 추가
    @GetMapping("/{seq}")
    public ResponseEntity<Void> add(@PathVariable("seq") Long seq) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
