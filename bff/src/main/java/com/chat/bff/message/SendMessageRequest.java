package com.chat.bff.message;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(@NotBlank String content) {}