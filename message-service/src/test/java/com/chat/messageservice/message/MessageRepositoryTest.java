package com.chat.messageservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void savesAndRetrievesMessage() {
        Message savedMessage = messageRepository.save(
                new Message(null, 42L, "Hello world", Instant.now()));

        Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());

        assertThat(foundMessage).isPresent();
        assertThat(foundMessage.get().getContent()).isEqualTo("Hello world");
        assertThat(foundMessage.get().getSenderId()).isEqualTo(42L);
    }
}
