# AI Dinner Menus

An AI-powered restaurant menu assistant built with **Spring Boot** and **Spring AI** that transforms menu images into structured, translated, accessible dining experiences.

The application can:

-  Extract text from restaurant menu images
-  Translate menu content into a user's preferred language
-  Convert unstructured menu text into structured JSON output
- ️ Generate food imagery after translation
-  Convert translated menu content into speech for accessibility
-  Improve menu accessibility for travelers, tourists, and users with visual or language barriers

---

## Features

### 1. Image to Text Extraction
Upload a restaurant menu image and extract readable text using AI-powered image analysis.

### 2. Menu Translation
Translate menu items into a target language, helping users understand foreign-language menus.

### 3. Structured Menu Generation
Transform raw OCR output into structured JSON that can be consumed by applications, mobile clients, or AI agents.

Example:

```json
{
  "dishName": "Margherita Pizza",
  "description": "Classic pizza with tomato sauce and mozzarella",
  "price": "$12"
}
```

### 4. AI Image Generation
Generate visual representations of menu dishes after translation and structuring.

### 5. Text-to-Speech Support
Convert translated menu content into audio for a more accessible dining experience.

### 6. Accessibility-Focused Experience
Designed to make restaurant menus easier to understand for:

- International travelers
- Tourists
- Users with visual impairments
- Users who prefer audio assistance

---
## AI Models Used

### GPT-5.4 Mini
Used for:
- Menu image understanding
- OCR-style text extraction from menu images
- Structured menu analysis
- JSON menu generation
- Menu translation
- Multilingual content generation
- Language adaptation for international users

### GPT-Image-2
Used for:
- Generating realistic food and dish images
- Visualizing translated menu items

### GPT-4o Mini TTS
Used for:
- Converting translated menu text into speech
- Accessibility-focused audio narration

### Spring AI
The application uses Spring AI as the orchestration layer to integrate:
- Vision models
- Language models
- Image generation models
- Text-to-speech services

## ️ Tech Stack

### Backend
- Java 25
- Spring Boot 3.5.x
- Spring AI 1.1.7
- Maven

### AI Capabilities
- OCR / Image Understanding
- Language Translation
- Structured Data Generation
- Image Generation
- Text-to-Speech

---

##  Project Structure

```text
ai_dinner-menus/
│
├── src/                 # Application source code
├── audio/               # Generated audio outputs
├── screenshots/         # Demo screenshots and assets
├── .mvn/                # Maven wrapper configuration
├── pom.xml              # Project dependencies
├── mvnw
├── mvnw.cmd
└── README.md
```

---

##  Getting Started

### Prerequisites

- Java 25
- Maven (or Maven Wrapper)
- OpenAI API Key

### Clone the Repository

```bash
git clone https://github.com/ritujane78/ai_dinner-menus.git
cd ai_dinner-menus
```

### Configure Environment Variables

Set your OpenAI API key.

### Run the Application

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

## Screenshots

Place application screenshots inside the `screenshots/` directory and reference them here.

```md
![Home](screenshots/home.png)
```
