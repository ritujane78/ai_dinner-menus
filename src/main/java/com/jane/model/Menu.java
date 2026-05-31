package com.jane.model;

import java.util.List;

public record Menu(String restaurantName, String souceLanguage, String targetLanguage,
                   List<MenuItem> items) {
}
