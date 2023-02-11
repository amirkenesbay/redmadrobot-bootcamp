package com.amirkenesbay.services;

import com.amirkenesbay.dto.AdvertisementDTO;
import com.amirkenesbay.entity.Advertisement;

import java.util.List;
import java.util.Optional;

public interface AdvertisementService {
    Optional<Advertisement> findById(Long id);

    List<Advertisement> findAll();

    List<Advertisement> findByEmail(String email);

    List<Advertisement> getPublished(List<String> adTags);

    Advertisement save(AdvertisementDTO advertisement, String email);

    void deleteById(Long id, String email);

    Advertisement updateActiveAdvertisement(Long id, boolean isActive);

    Advertisement updatePublishedAdvertisement(Long id, String email, boolean isPublished);
}
