package com.amirkenesbay.controller;

import com.amirkenesbay.dto.AdTagDTO;
import com.amirkenesbay.dto.ApiResponse;
import com.amirkenesbay.entity.AdTag;
import com.amirkenesbay.enums.ApiStatus;
import com.amirkenesbay.mapper.AdTagMapper;
import com.amirkenesbay.services.AdTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Api(description = "Controller for advertisement tags")
public class AdTagController {
    private final AdTagService adTagService;
    private final AdTagMapper tagMapper;

    @PostMapping()
    @ApiOperation(value = "Add new tag")
    public ResponseEntity<?> add(@Valid @RequestBody AdTagDTO adTagDto) {
        AdTag tag = adTagService.save(tagMapper.mapToEntity(adTagDto));
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, tagMapper.mapToDto(tag)));
    }

    @GetMapping
    @ApiOperation(value = "Get all tags")
    public ResponseEntity<?> list() {
        List<AdTagDTO> adTagDTOList = adTagService.findAll().stream().map(tagMapper::mapToDto).toList();
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.OK.value(), null, adTagDTOList));
    }

    @PutMapping
    @ApiOperation(value = "Update tag")
    public ResponseEntity<?> update(@RequestBody AdTagDTO adTagDTO) {
        AdTag tag = adTagService.save(tagMapper.mapToEntity(adTagDTO));
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.OK.value(), null, tagMapper.mapToDto(tag)));
    }
}
