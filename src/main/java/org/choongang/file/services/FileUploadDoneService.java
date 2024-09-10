package org.choongang.file.services;

import lombok.RequiredArgsConstructor;
import org.choongang.global.Utils;
import org.choongang.global.exceptions.BadRequestException;
import org.choongang.global.rests.ApiRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadDoneService {
    private final ApiRequest apiRequest;
    private final Utils utils;

    public void process(String gid) {
        ApiRequest request = apiRequest.request("/done/" + gid, "file-service");
        if (request.getStatus().is2xxSuccessful()) {
            throw new BadRequestException(utils.getMessage("Fail.file.done"));
        }
    }
}
