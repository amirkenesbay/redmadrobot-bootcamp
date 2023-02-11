package com.amirkenesbay.controller;

import com.amirkenesbay.dto.AdvertisementDTO;
import com.amirkenesbay.dto.ApiResponse;
import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.enums.ApiStatus;
import com.amirkenesbay.mapper.AdvertisementMapper;
import com.amirkenesbay.services.AdvertisementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/advertisements")
@RequiredArgsConstructor
@Api(description = "Controller for advertisements")
public class AdvertisementController {
    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @GetMapping
    @ApiOperation(value = "Gets a list of advertisements")
    public ResponseEntity<?> listAds(@RequestParam(value = "adTag") List<String> adTags) {
        List<AdvertisementDTO> advertisements = advertisementService.getPublished(adTags).stream().map(advertisementMapper::mapToDto).toList();
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisements));
    }

    @GetMapping("/allAds")
    @ApiOperation(value = "Gets a list of all advertisements (published and unpublished)")
    public ResponseEntity<?> getAllAds() {
        List<AdvertisementDTO> advertisements = advertisementService.findAll().stream().map(advertisementMapper::mapToDto).toList();
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisements));
    }

    @GetMapping("/myAds")
    @ApiOperation(value = "Gets a list of all advertisement (logged users only")
    public ResponseEntity<?> getMyAds(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        List<AdvertisementDTO> advertisements = advertisementService.findByEmail(principal.getName()).stream().map(advertisementMapper::mapToDto).toList();
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisements));
    }

    @PostMapping
    @ApiOperation(value = "Add new advertisement")
    public ResponseEntity<?> addAd(@Valid @RequestBody AdvertisementDTO advertisementDto, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Advertisement advertisement = advertisementService.save(advertisementDto, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisementMapper.mapToDto(advertisement)));
    }

    @PutMapping(path = "/{id}/publish")
    @ApiOperation(value = "Publish advertisement")
    public ResponseEntity<?> publishAd(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Advertisement advertisement = advertisementService.updatePublishedAdvertisement(id, principal.getName(), true);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisementMapper.mapToDto(advertisement)));
    }

    @PutMapping("/{id}/unpublish")
    @ApiOperation(value = "Unpublish advertisement")
    public ResponseEntity<?> unpublishAd(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        Advertisement advertisement = advertisementService.updatePublishedAdvertisement(id, principal.getName(), false);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisementMapper.mapToDto(advertisement)));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Activate advertisement")
    public ResponseEntity<?> activateAd(@PathVariable(name = "id") Long id) {
        Advertisement advertisement = advertisementService.updateActiveAdvertisement(id, true);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisementMapper.mapToDto(advertisement)));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Deactivate advertisement")
    public ResponseEntity<?> deactivateAd(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Advertisement advertisement = advertisementService.updateActiveAdvertisement(id, false);
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.CREATED.value(), null, advertisementMapper.mapToDto(advertisement)));
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete advertisement")
    public ResponseEntity<?> deleteAd(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        advertisementService.deleteById(id, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(ApiStatus.OK, HttpStatus.NO_CONTENT.value(), null, null));
    }
}