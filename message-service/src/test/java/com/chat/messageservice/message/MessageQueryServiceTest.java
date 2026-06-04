package com.chat.messageservice.message;

import com.chat.grpc.UserProfile;
import com.chat.messageservice.grpc.UserProfileClient;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageQueryServiceTest {

    private final MessageService messageService = mock(MessageService.class);
    private final UserProfileClient userProfileClient = mock(UserProfileClient.class);
    private final MessageQueryService messageQueryService =
            new MessageQueryService(messageService, userProfileClient);

    @Test
    void findAllWithSenderAttachesUsername() {
        when(messageService.findAll())
                .thenReturn(List.of(new Message(1L, 42L, "Hello", Instant.parse("2026-01-01T00:00:00Z"))));
        when(userProfileClient.getProfile(42L))
                .thenReturn(UserProfile.newBuilder().setId(42L).setUsername("test_king").build());

        List<MessageWithSender> result = messageQueryService.findAllWithSender();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).message().getContent()).isEqualTo("Hello");
        assertThat(result.get(0).senderUsername()).isEqualTo("test_king");
    }

    @Test
    void findAllWithSenderFallsBackToNullWhenLookupFails() {
        when(messageService.findAll())
                .thenReturn(List.of(new Message(1L, 42L, "Hello", Instant.parse("2026-01-01T00:00:00Z"))));
        when(userProfileClient.getProfile(42L))
                .thenThrow(new RuntimeException("user-service unavailable"));

        List<MessageWithSender> result = messageQueryService.findAllWithSender();

        assertThat(result.get(0).senderUsername()).isNull();
    }
}