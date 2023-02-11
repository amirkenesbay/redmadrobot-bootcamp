package com.amirkenesbay.mapper;

import com.amirkenesbay.dto.AdTagDTO;
import com.amirkenesbay.entity.AdTag;
import org.springframework.stereotype.Component;

@Component
public class AdTagMapper {
    public AdTag mapToEntity(AdTagDTO adTagDto) {
        return new AdTag(adTagDto.getId(), adTagDto.getName(), adTagDto.getDescription());
    }

    public AdTagDTO mapToDto(AdTag adTag) {
        return new AdTagDTO(adTag.getId(), adTag.getName(), adTag.getDescription());
    }
}
