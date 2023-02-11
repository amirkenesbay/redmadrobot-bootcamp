package com.amirkenesbay.mapper;

import com.amirkenesbay.dto.AdvertisementDTO;
import com.amirkenesbay.entity.AdTag;
import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementMapper {
    public Advertisement mapToEntity(AdvertisementDTO advertisementDto) {
        return Advertisement.builder()
                .description(advertisementDto.getDescription())
                .title(advertisementDto.getTitle())
                .price(advertisementDto.getPrice())
                .isPublished(advertisementDto.isPublished())
                .build();
    }

    public AdvertisementDTO mapToDto(Advertisement advertisement) {
        return new AdvertisementDTO(
                advertisement.getId(),
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.isActive(),
                advertisement.isPublished(),
                advertisement.getImages().stream().map(Image::getImageUrl).toArray(String[]::new),
                advertisement.getAdvertisementTags().stream().map(AdTag::getName).toArray(String[]::new),
                advertisement.getPrice(),
                advertisement.getAdOwner().getId(),
                advertisement.getAdOwner().getUsername()
        );
    }
}
