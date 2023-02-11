package com.amirkenesbay.services.impl;

import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Message;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.exception.AdvertisementNotFoundException;
import com.amirkenesbay.exception.DeniedAccessToOrdinaryUsersException;
import com.amirkenesbay.exception.MessageNotFoundException;
import com.amirkenesbay.exception.UserNotFoundException;
import com.amirkenesbay.repository.MessageRepository;
import com.amirkenesbay.services.AdvertisementService;
import com.amirkenesbay.services.MessageService;
import com.amirkenesbay.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final AdvertisementService advertisementService;
    private final UserService userService;

    @Override
    public List<Message> findAllMessagesByAdvertisementId(Long advertisementId, String email) {
        List<Message> messages = messageRepository.findAllMessagesByAdvertisementId(advertisementId);
        return messages.stream().filter(item ->
                item.getMessageReceiver().getEmail().equalsIgnoreCase(email) ||
                        item.getMessageSender().getEmail().equalsIgnoreCase(email)
        ).collect(Collectors.toList());
    }

    @Override
    public Message addMessageToAdvertisement(Long advertisementId, Long receiverId, String email, String messageBody) {
        Optional<User> messageSenderOptional = userService.findByEmail(email);
        Optional<User> messageReceiverOptional = userService.findById(receiverId);
        Optional<Advertisement> optionalAdvertisement = advertisementService.findById(advertisementId);
        if (optionalAdvertisement.isEmpty()) {
            throw new AdvertisementNotFoundException("Advertisement with id " + advertisementId + " not found");
        }
        if (messageSenderOptional.isEmpty()) {
            throw new UserNotFoundException("Sender user with email " + email + " not found");
        }
        if (messageReceiverOptional.isEmpty()) {
            throw new UserNotFoundException("Receiver user with id " + receiverId + " not found");
        }

        Message messageEntity = new Message(
                null,
                messageSenderOptional.get(),
                messageReceiverOptional.get(),
                messageBody,
                optionalAdvertisement.get(),
                Date.from(Instant.now())

        );
        messageRepository.save(messageEntity);

        return messageEntity;
    }

    @Override
    public void deleteById(Long id, String email) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(messageOptional.isEmpty()) {
            throw new MessageNotFoundException("A message with id " + id + " not found");
        }

        Optional<User> optionalUser = userService.findByEmail(email);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        }

        Message message = messageOptional.get();
        if(!Objects.equals(message.getMessageSender().getId(), optionalUser.get().getId()) &&
            optionalUser.get().getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase("ADMIN"))) {
            throw new DeniedAccessToOrdinaryUsersException("You are not allowed to delete this message");
        }
        messageRepository.deleteById(id);
    }
}
