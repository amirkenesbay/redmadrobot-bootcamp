package com.amirkenesbay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDTO {
    private Long id;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    private boolean isActive;
    private boolean isPublished;
    private String[] images;
    private String[] adTags;
    private Long price;
    private Long ownerId;
    private String ownerName;
}
