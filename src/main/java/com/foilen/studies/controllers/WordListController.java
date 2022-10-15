package com.foilen.studies.controllers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.*;
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
    public WordListWithScoreListResult list(Authentication authentication) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListWithScoreListResult();
        result.setItems(wordManager.listWordList(userDetails.getId()));
        return result;
    }

    @PostMapping("/")
    public FormResult save(
            Authentication authentication,
            @RequestBody WordListExpended form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        return wordManager.saveWordList(userDetails.getId(), form);
    }

    @GetMapping("/{wordListId}")
    public WordListSingleResult get(
            Authentication authentication,
            @PathVariable String wordListId
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListSingleResult();
        result.setItem(wordManager.getWordListExpended(userDetails.getId(), wordListId));
        return result;
    }

    @DeleteMapping("/{wordListId}")
    public FormResult delete(
            Authentication authentication,
            @PathVariable String wordListId
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);
        return wordManager.deleteWordList(userDetails.getId(), wordListId);
    }

    @PostMapping("/bulkSplit")
    public WordListResult bulkSplit(
            Authentication authentication,
            @RequestBody BulkSplitForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        var result = new WordListResult();
        result.setItems(wordManager.bulkSplit(userDetails.getId(), form.getAll()));
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

    @PostMapping("/track")
    public FormResult track(
            Authentication authentication,
            @RequestBody TrackForm form
    ) {
        var userDetails = userManager.getOrCreateUser(authentication);

        return wordManager.track(userDetails.getId(), form);
    }

}
