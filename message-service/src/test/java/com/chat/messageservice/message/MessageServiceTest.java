package com.chat.messageservice.message;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    private final MessageRepository messageRepository = mock(MessageRepository.class);
    private final MessageService messageService = new MessageService(messageRepository);

    @Test
    void createSavesMessageWithSenderContentAndTimestamp() {
        when(messageRepository.save(any(Message.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        messageService.create(42L, "Hello");

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());
        Message savedMessage = captor.getValue();

        assertThat(savedMessage.getSenderId()).isEqualTo(42L);
        assertThat(savedMessage.getContent()).isEqualTo("Hello");
        assertThat(savedMessage.getCreatedAt()).isNotNull();
    }

    @Test
    void findAllReturnsMessagesNewestFirst() {
        List<Message> messages = List.of(new Message(1L, 42L, "Hi", Instant.now()));
        when(messageRepository.findAllByOrderByCreatedAtDesc()).thenReturn(messages);

        assertThat(messageService.findAll()).isEqualTo(messages);
    }
}
