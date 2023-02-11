package com.amirkenesbay.services;

import com.amirkenesbay.entity.Message;

import java.util.List;

public interface MessageService {
    List<Message> findAllMessagesByAdvertisementId(Long advertisementId, String email);

    Message addMessageToAdvertisement(Long advertisementId, Long receiverId, String email, String messageBody);

    void deleteById(Long id, String email);
}
