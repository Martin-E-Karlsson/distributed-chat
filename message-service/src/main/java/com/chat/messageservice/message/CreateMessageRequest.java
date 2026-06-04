package com.chat.messageservice.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMessageRequest(
   @NotNull Long senderId,
   @NotBlank String content
) {}
