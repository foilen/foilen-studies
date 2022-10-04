package com.foilen.studies.controllers;

import com.foilen.studies.services.SpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/speakText")
public class SpeakTextController {

    @Autowired
    private SpeechService speechService;

    @GetMapping(value = "/{cacheId}", produces = "audio/mpeg")
    public Resource getFile(@PathVariable String cacheId) {
        return speechService.getFile(cacheId);
    }

}
