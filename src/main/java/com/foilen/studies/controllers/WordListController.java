package com.foilen.studies.controllers;

import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.controllers.models.WordListListResult;
import com.foilen.studies.controllers.models.WordListResult;
import com.foilen.studies.managers.UserManager;
import com.foilen.studies.managers.WordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wordList")
public class WordListController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private WordManager wordManager;

    @GetMapping("/")
    public WordListListResult list(Authentication authentication) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListListResult();
        result.setItems(wordManager.listWordList(userDetails.getId()));
        return result;
    }

    @GetMapping("/{wordListId}")
    public WordListResult list(
            Authentication authentication,
            @PathVariable String wordListId
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListResult();
        result.setItems(wordManager.listWord(userDetails.getId(), wordListId));
        return result;
    }

    @PostMapping("/random")
    public WordListResult random(
            Authentication authentication,
            @RequestBody RandomWordListForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListResult();
        result.setItems(wordManager.randomWord(userDetails.getId(), form));
        return result;
    }

}
