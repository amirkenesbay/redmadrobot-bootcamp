package com.amirkenesbay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendingMessageDTO {
    @NotBlank(message = "Message cannot be empty")
    private String messageBody;

    private Long messageReceiverId;
}
