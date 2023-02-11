package com.amirkenesbay.service;

import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Message;
import com.amirkenesbay.entity.Role;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.enums.RolesName;
import com.amirkenesbay.repository.MessageRepository;
import com.amirkenesbay.services.AdvertisementService;
import com.amirkenesbay.services.MessageService;
import com.amirkenesbay.services.UserService;
import com.amirkenesbay.services.impl.MessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MessageServiceImplTest {
    private Message message1, message2, message3;
    private Advertisement ad1, ad2, ad3;
    private User user1, user2, user3;

    @MockBean
    private MessageRepository messageRepository;

    private MessageService messageService;

    @MockBean
    private AdvertisementService advertisementService;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .isActive(true)
                .email("test@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.ADMIN.toString())))
                .build();

        user2 = User.builder()
                .id(2L)
                .isActive(true)
                .email("test2@@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.USER.toString())))
                .build();

        user3 = User.builder()
                .id(3L)
                .isActive(true)
                .email("test2@@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.USER.toString())))
                .build();

        ad1 = Advertisement.builder()
                .id(1L)
                .adOwner(user2)
                .price(2000L)
                .build();

        ad2 = Advertisement.builder()
                .id(2L)
                .adOwner(user2)
                .price(2000L)
                .build();

        message1 = new Message(1L, user1, user2, "Test", ad1, Date.from(Instant.now()));
        message2 = new Message(2L, user2, user1, "Test", ad1, Date.from(Instant.now()));
        message3 = new Message(3L, user2, user3, "Test", ad1, Date.from(Instant.now()));

        Mockito.when(messageRepository.findAll()).thenReturn(List.of(message1, message2, message3));
        Mockito.when(messageRepository.findById(message1.getId())).thenReturn(Optional.ofNullable(message1));
        Mockito.when(messageRepository.findById(message2.getId())).thenReturn(Optional.ofNullable(message2));
        Mockito.when(messageRepository.findAllMessagesByAdvertisementId(ad1.getId())).thenReturn(List.of(message1, message2, message3));
        Mockito.when(userService.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userService.findByEmail(user2.getEmail())).thenReturn(Optional.ofNullable(user2));
        messageService = new MessageServiceImpl(messageRepository, advertisementService, userService);
    }

    @Test
    void findAllMyMessagesByAdvertisementId() {
        List<Message> items = messageService.findAllMessagesByAdvertisementId(ad1.getId(), user1.getEmail());
        boolean isMyMessages = items.stream().allMatch(msg ->
                msg.getMessageSender().getEmail().equalsIgnoreCase(user1.getEmail()) ||
                        msg.getMessageReceiver().getEmail().equalsIgnoreCase(user1.getEmail())
        );

        assertEquals(2, items.size());
        assertTrue(isMyMessages);
    }

    @Test
    void deleteMessageByOwner() {
        messageService.deleteById(message2.getId(), user2.getEmail());
    }

    @Test
    void deleteSomeoneElseMessageByAdmin() {
        messageService.deleteById(message2.getId(), user1.getEmail());
    }
}
