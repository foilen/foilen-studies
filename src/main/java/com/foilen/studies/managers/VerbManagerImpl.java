package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.restapi.services.FormValidationTools;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.studies.controllers.models.VerbListResult;
import com.foilen.studies.controllers.models.VerbResult;
import com.foilen.studies.data.VerbRepository;
import com.foilen.studies.data.verb.Verb;
import com.foilen.studies.data.vocabulary.Language;
import com.foilen.studies.data.vocabulary.SpeakText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VerbManagerImpl extends AbstractBasics implements VerbManager {

    @Autowired
    private VerbRepository verbRepository;

    @Override
    public VerbListResult listVerb(String userId) {
        var result = new VerbListResult();
        verbRepository.findAllByOwnerUserIdOrderByName(userId).forEach(result.getItems()::add);
        return result;
    }

    @Override
    public VerbResult getVerb(String userId, String verbId) {
        var result = new VerbResult();
        result.setItem(verbRepository.findByIdAndOwnerUserId(verbId, userId));
        return result;
    }

    @Override
    public FormResult saveVerb(String userId, Verb form) {
        // Validation
        FormResult formResult = new FormResult();
        FormValidationTools.validateMandatory(formResult, "name", form.getName());
        if (CollectionsTools.isNullOrEmpty(form.getVerbLines())) {
            CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), "verbLines", String.class).add("The value is mandatory");
        }

        if (!formResult.isSuccess()) {
            return formResult;
        }

        // Get or create
        var verb = verbRepository.findByIdAndOwnerUserId(form.getId(), userId);
        if (verb == null) {
            verb = new Verb();
            verb.setOwnerUserId(userId);
        }
        verb.setName(form.getName());
        verb.setVerbLines(form.getVerbLines());
        verb.getVerbLines().forEach(it -> {
            SpeakText speakText = it.getSpeakText();
            if (speakText == null) {
                speakText = new SpeakText();
                it.setSpeakText(speakText);
            }
            speakText.setLanguage(Language.FR);

            // If pronoun ends with apostrophe, no space
            String text;
            if (it.getPronoun().endsWith("'")) {
                text = it.getPronoun() + it.getWord();
            } else {
                text = it.getPronoun() + " " + it.getWord();
            }
            speakText.setText(text);

            speakText.computeCacheId();
        });

        verbRepository.save(verb);
        return formResult;
    }

    @Override
    public FormResult deleteVerb(String userId, String verbId) {
        var verb = verbRepository.findByIdAndOwnerUserId(verbId, userId);
        FormResult result = new FormResult();
        if (verb == null) {
            result.getGlobalErrors().add("Verb does not exist");
            return result;
        }

        logger.info("Deleting verb {} for user {}", verbId, userId);
        verbRepository.delete(verb);

        return result;
    }
}
