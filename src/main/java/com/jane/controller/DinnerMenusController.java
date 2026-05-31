package com.jane.controller;

import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class DinnerMenusController {
    private final ChatClient chatClient;
    private final ImageModel imageModel;
    private final TextToSpeechModel textToSpeechModel;

    public DinnerMenusController(ChatClient.Builder builder, ImageModel imageModel, TextToSpeechModel textToSpeechModel) {
        this.chatClient = builder.build();
        this.imageModel = imageModel;
        this.textToSpeechModel = textToSpeechModel;
    }

    @GetMapping(value = "/text", produces = MediaType.TEXT_PLAIN_VALUE)
    public String convertToText(){
        var menuImage = new FileSystemResource("C:/_repos/menu.jpg");

        String systemPrompt = """
                You are an OCR engine.
                Extract all the readable text from the provided image.
                Return only the RAW text.
                Preserve line breaks where possible.
                """;
        String userPrompt = "Run OCR on this menu image.";

        var prompt = chatClient.prompt().system(systemPrompt)
                .user(u -> u.text(userPrompt).media(MimeTypeUtils.IMAGE_JPEG, menuImage ));
        var message = prompt.call().content();

        return message;

    }

    @GetMapping(value = "/english", produces = MediaType.TEXT_PLAIN_VALUE)
    public String convertToEnglish(){
        String ocrText = convertToText();
        String englishText = "";
        String systemPrompt = """
                You are a professional menu translator.
                Translate the following menu text into English.
                Keep the formatting (line breaks, headings, prices) as close as possible.
                Do not add extra explanations.
                Return the menu in HTML format for easy rendering but don't show the HTML tags.
                """;
        var prompt = chatClient.prompt().system(systemPrompt).user(ocrText);

        englishText = prompt.call().content();

        return englishText;
    }
}
