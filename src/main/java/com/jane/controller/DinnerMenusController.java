package com.jane.controller;

import com.jane.model.Menu;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
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

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Menu convertToJson(){
        var englishText = convertToEnglish();
        var systemPrompt = """
                Convert the menu text to JSON that matches the provided schema:
                - restaurantMenu: Best guess from the heading. If the name is unknown,use 'Unknown Restaurant',
                - sourceLanguage:  best guess if you can infer, otherwise 'Unknown'
                - targetLanguage: always 'English
                - inputs: include only item names (no section headings)
                - price: numeric as a string. Eg. '12' or '12.5'
                - description: empty string if missing
                Return only valid JSON (no markdown, no commentary
                """;
        String userPrompt = """
                Here is the translated menu text. convert it to structured JSON: %s
                """.formatted(englishText);
        var prompt = chatClient.prompt().system(systemPrompt).user(userPrompt);

        return prompt.call().entity(Menu.class);
    }

    @GetMapping(value = "/image", produces = MediaType.TEXT_HTML_VALUE)
    public String generateImageFromJson(){
        Menu menu = convertToJson();

        String systemPrompt = """
                Create a beautiful image (1024x1024) inspired by the restaurant and dishes specified in the file %s
                Critical requirements: make it visually striking and professional.
                Style guidance (choose ONE and not both)
                A) cinematic food photography aesthetic, more lighting, shallow depth of field.
                B) elegant, minimal illustrations, premium bistro vibe.
                
                Composition:
                - strong focal point (plate/ingredients/table setting)
                - high contrast
                - uncultured
                """.formatted(menu.toString());
        if(systemPrompt.length() > 3750){
            systemPrompt = systemPrompt.substring(0, 3750);
        }
        var options = OpenAiImageOptions.builder()
                .model("gpt-image-2")
                .width(1024)
                .height(1024)
                .quality("medium")
                .responseFormat("b64_json")
                .N(1)
                .build();
    ImageResponse response = imageModel.call(new ImagePrompt(systemPrompt, options));
    var base64Representation = response.getResult().getOutput().getB64Json();

    var html = """
            <img alt="Restaurant hero poster" style="max-width:900px, width: 100%%, border: 1px solid #ccc"
            src="data:image/png;base64, %s" />
            """.formatted(base64Representation);
    return html;
    }
}
