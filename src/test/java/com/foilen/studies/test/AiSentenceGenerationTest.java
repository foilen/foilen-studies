package com.foilen.studies.test;

import com.foilen.studies.StudiesApplication;
import com.foilen.studies.services.AiGenerationService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AiSentenceGenerationTest {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StudiesApplication.class);
        app.setAdditionalProfiles();
        try (ConfigurableApplicationContext ctx = app.run(args)) {
            AiGenerationService aiGenerationService = ctx.getBean(AiGenerationService.class);
            Environment env = ctx.getBean(Environment.class);

            String modelName = env.getProperty("spring.ai.openai.chat.options.model", "unknown-model").replace(":", "_");

            List<String> words = new ArrayList<>(List.of(
                    "ami",
                    "bonheur",
                    "chat",
                    "chemin",
                    "chien",
                    "coeur",
                    "eau",
                    "école",
                    "famille",
                    "fleur",
                    "jardin",
                    "livre",
                    "lumière",
                    "maison",
                    "matin",
                    "nuit",
                    "pain",
                    "soleil",
                    "travail",
                    "voiture"
            ));

            String reportFile = "_report_sentence_" + modelName + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(reportFile))) {
                for (String word : words) {
                    String sentence;
                    try {
                        sentence = aiGenerationService.generateSentence(Locale.FRENCH, word);
                    } catch (Exception e) {
                        sentence = "ERROR";
                    }
                    String line = word + " => " + sentence;
                    System.out.println(line);
                    writer.println(line);
                }
            } catch (Exception e) {
                System.err.println("Failed to write report: " + e.getMessage());
            }
        }
    }
}
