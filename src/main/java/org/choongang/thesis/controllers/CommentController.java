package org.choongang.thesis.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.rests.JSONData;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.services.comment.CommentDeleteService;
import org.choongang.thesis.services.comment.CommentInfoService;
import org.choongang.thesis.services.comment.CommentSaveService;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Comment",description = "댓글 API")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentInfoService infoService;
    private final CommentSaveService saveService;
    private final CommentDeleteService deleteService;
    private final Utils utils;


    @Operation(summary = "댓글 작성", method = "POST")
    @ApiResponse(responseCode = "201")
    @PostMapping("/write")
    public JSONData write(@RequestBody @Valid RequestComment form, Errors errors) {

        return save(form, errors);
    }

    @Operation(summary = "댓글 한개 수정", method = "PATCH")
    @ApiResponse(responseCode = "201")
    @PatchMapping("/update")
    public  JSONData update(@RequestBody @Valid RequestComment form, Errors errors) {
        return save(form, errors);
    }

    public JSONData save(RequestComment form, Errors errors) {
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
        saveService.save(form);
        System.out.println("RequestComment: " + form);

        List<CommentData> items = infoService.getList(form.getTid());

        return new JSONData(items); // 댓글 목록으로 반환값을 내보낸다.
    }

    @Operation(summary = "댓글 한개 조회", method = "GET", description = "수정, 삭제 버튼 등장?")
    @ApiResponse(responseCode = "200")
    @Parameter(name = "seq", required = true, description = "경로변수, 댓글 번호", example = "100")
    @GetMapping("/info/{seq}") // 댓글 하나
    public JSONData getInfo(@PathVariable("seq") Long seq) {
        CommentData item = infoService.get(seq);

        return new JSONData(item);
    }

    @Operation(summary = "댓글 목록", method = "GET", description = "댓글 모두 조회")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name = "seq", description = "경로변수, 논문 번호", example = "100")
    })
    @GetMapping("/list/{seq}") // 댓글 목록
    public JSONData getList(@PathVariable("seq") Long seq) {
        List<CommentData> items = infoService.getList(seq);

        return new JSONData(items); // 댓글 목록으로 반환값을 내보낸다.
    }

    @Operation(summary = "댓글 삭제", method = "DELETE")
    @ApiResponse(responseCode = "200")
    @Parameter(name = "seq", required = true, description = "경로변수, 댓글 번호", example = "100")
    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        Long bSeq = deleteService.delete(seq);

        List<CommentData> items = infoService.getList(bSeq);

        return new JSONData(items); // 댓글 목록으로 반환값을 내보낸다.
    }
}
