package org.choongang.thesis.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.rests.JSONData;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.services.comment.CommentDeleteService;
import org.choongang.thesis.services.comment.CommentInfoService;
import org.choongang.thesis.services.comment.CommentSaveService;
import org.choongang.thesis.validators.CommentValidator;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentInfoService infoService;
    private final CommentSaveService saveService;
    private final CommentDeleteService deleteService;
    private final CommentValidator validator;
    private final Utils utils;

    @PostMapping("/write")
    public JSONData write(@RequestBody @Valid RequestComment form, Errors errors) {

        return save(form, errors);
    }

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
    @GetMapping("/info/{seq}") // 댓글 하나
    public JSONData getInfo(@PathVariable("seq") Long seq) {
        CommentData item = infoService.get(seq);

        return new JSONData(item);
    }

    @GetMapping("/list/{bSeq}") // 댓글 목록
    public JSONData getList(@PathVariable("bSeq") Long bSeq) {
        List<CommentData> items = infoService.getList(bSeq);

        return new JSONData(items); // 댓글 목록으로 반환값을 내보낸다.
    }

    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        Long bSeq = deleteService.delete(seq);

        List<CommentData> items = infoService.getList(bSeq);

        return new JSONData(items); // 댓글 목록으로 반환값을 내보낸다.

    }
}
