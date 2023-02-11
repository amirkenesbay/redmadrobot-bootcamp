package com.amirkenesbay.controller;

import com.amirkenesbay.dto.ApiResponse;
import com.amirkenesbay.dto.MessageDTO;
import com.amirkenesbay.dto.SendingMessageDTO;
import com.amirkenesbay.entity.Message;
import com.amirkenesbay.enums.ApiStatus;
import com.amirkenesbay.mapper.MessageMapper;
import com.amirkenesbay.services.MessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(description = "Controller for messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/api/advertisement/{id}/messages")
    public ResponseEntity<?> add(@PathVariable(name = "id") Long id, @Valid @RequestBody SendingMessageDTO sendingMessageDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Message message = messageService.addMessageToAdvertisement(id, sendingMessageDto.getMessageReceiverId(), principal.getName(), sendingMessageDto.getMessageBody());
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, messageMapper.mapToDto(message)));
    }

    @GetMapping("/api/advertisement/{id}/messages")
    public ResponseEntity<?> messageList(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        List<MessageDTO> messageDTOList = messageService.findAllMessagesByAdvertisementId(id, principal.getName()).stream().map(messageMapper::mapToDto).toList();
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, messageDTOList));
    }

    @DeleteMapping("/api/advertisement/{id}/messages/{messageId}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id, @PathVariable(name = "messageId") Long messageId, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        messageService.deleteById(messageId, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, null));
    }
}
