package com.foilen.studies;

import com.foilen.studies.managers.WordManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordListCreateApplication {

    public static void main(String[] args) {
        System.setProperty("spring.main.web-application-type", "none");
        var app = SpringApplication.run(WordListCreateApplication.class, args);
        app.getBean(WordListCreateApplication.class).run();

    }

    @Autowired
    private WordManager wordManager;

    private void run() {
        String wordsInText = """
                patte
                trente
                carotte
                quarante
                tante
                vite
                toute la
                patate
                cinquante
                soixante
                haute
                forte
                ensuite
                quatre
                cet
                sept
                huit
                """;
        wordManager.createWordList("633a133a02da555d4d99a44a", "Liste 13&14", wordsInText);
    }

}
