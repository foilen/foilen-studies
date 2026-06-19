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
import java.util.Map;

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
                Map<String, String> sentences = aiGenerationService.generateSentences(Locale.FRENCH, words);
                for (String word : words) {
                    String sentence = sentences.getOrDefault(word, "ERROR");
                    String line = word + " => " + sentence;
                    IO.println(line);
                    writer.println(line);
                }
            } catch (Exception e) {
                System.err.println("Failed to write report: " + e.getMessage());
            }
        }
    }
}
