package com.amirkenesbay.mapper;

import com.amirkenesbay.dto.MessageDTO;
import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Message;
import com.amirkenesbay.entity.User;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
    public Message mapToEntity(MessageDTO messageDto, Advertisement advertisement, User senderUser, User receiverUser) {
        return new Message(
                messageDto.getId(),
                senderUser,
                receiverUser,
                messageDto.getMessageBody(),
                advertisement,
                messageDto.getMessageCreated()
        );
    }

    public MessageDTO mapToDto(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getMessageBody(),
                message.getAdvertisement().getId(),
                message.getMessageSender().getId(),
                message.getMessageSender().getUsername(),
                message.getMessageReceiver().getId(),
                message.getMessageReceiver().getUsername(),
                message.getMessageCreated()
        );
    }
}
