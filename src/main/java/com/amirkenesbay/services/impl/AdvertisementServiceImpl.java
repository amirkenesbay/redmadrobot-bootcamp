package com.amirkenesbay.services.impl;

import com.amirkenesbay.dto.AdvertisementDTO;
import com.amirkenesbay.entity.AdTag;
import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Image;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.exception.AdvertisementNotFoundException;
import com.amirkenesbay.exception.DeniedAccessToOrdinaryUsersException;
import com.amirkenesbay.exception.UserEmailNotFoundException;
import com.amirkenesbay.mapper.AdvertisementMapper;
import com.amirkenesbay.repository.AdvertisementRepository;
import com.amirkenesbay.services.AdTagService;
import com.amirkenesbay.services.AdvertisementService;
import com.amirkenesbay.services.ImageService;
import com.amirkenesbay.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {
    private final AdvertisementRepository advertisementRepository;
    private final AdTagService adTagService;
    private final ImageService imageService;
    private final AdvertisementMapper advertisementMapper;
    private final UserService userService;

    @Override
    public Optional<Advertisement> findById(Long id) {
        return advertisementRepository.findById(id);
    }

    @Override
    public List<Advertisement> findAll() {
        List<Advertisement> advertisements = new ArrayList<>();
        advertisementRepository.findAll().iterator().forEachRemaining(advertisements::add);
        return advertisements;
    }

    @Override
    public List<Advertisement> findByEmail(String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserEmailNotFoundException("User with this email " + email + " not found");
        }
        return advertisementRepository.findAdvertisementsById(userOptional.get().getId());
    }

    @Override
    public List<Advertisement> getPublished(List<String> adTags) {
        List<Advertisement> advertisements = new ArrayList<>();
        advertisementRepository.findAll().iterator().forEachRemaining(item -> {
            if (item.isActive() && item.isPublished() &&
                    (adTags == null || item.getAdvertisementTags().stream().anyMatch(tag -> adTags.stream().anyMatch(t -> t.equalsIgnoreCase(tag.getName())))))
                advertisements.add(item);
        });
        return advertisements;
    }

    @Override
    public Advertisement save(AdvertisementDTO advertisementDto, String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            Advertisement advertisementEntity = advertisementMapper.mapToEntity(advertisementDto);
            advertisementEntity.setAdOwner(userOptional.get());
            advertisementEntity.setAdCreated(Date.from(Instant.now()));
            advertisementEntity.setActive(true);
            Set<AdTag> advertisementTags = new HashSet<>();
            if (advertisementDto.getAdTags() != null && advertisementDto.getAdTags().length > 0) {
                Arrays.stream(advertisementDto.getAdTags()).forEach(
                        name -> {
                            Optional<AdTag> adTag = adTagService.findByName(name);
                            adTag.ifPresent(advertisementTags::add);
                        }
                );
            }
            advertisementEntity.setAdvertisementTags(advertisementTags);
            advertisementEntity = advertisementRepository.save(advertisementEntity);

            List<Image> imageList = new ArrayList<>();
            if (advertisementDto.getImages() != null && advertisementDto.getImages().length > 0) {
                Advertisement advertisementModel = advertisementEntity;
                Arrays.stream(advertisementDto.getImages()).forEach(
                        url -> {
                            Image image = imageService.save(
                                    Image.builder()
                                            .imageUrl(url)
                                            .advertisement(advertisementModel)
                                            .imageOwner(userOptional.get())
                                            .build());
                            imageList.add(image);
                        }
                );
            }

            advertisementEntity.setImages(imageList);
            return advertisementEntity;
        }
        throw new UserEmailNotFoundException("User with this email " + email + " not found");
    }

    @Override
    public void deleteById(Long id, String email) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        if (advertisementOptional.isEmpty()) {
            throw new AdvertisementNotFoundException("Advertisement with this id " + id + " not found");
        }

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserEmailNotFoundException("User with this email " + email + " not found");
        }

        Advertisement advertisement = advertisementOptional.get();
        if (!Objects.equals(advertisement.getAdOwner().getId(), userOptional.get().getId()) &&
                userOptional.get().getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase("ADMIN"))) {
            throw new DeniedAccessToOrdinaryUsersException("You are not allowed to delete this advertisement");
        }
        advertisementRepository.deleteById(id);
    }

    @Override
    public Advertisement updateActiveAdvertisement(Long id, boolean isActive) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        if (advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();
            advertisement.setActive(isActive);
            advertisementRepository.save(advertisement);
            return advertisement;
        }
        throw new AdvertisementNotFoundException("Advertisement with this id " + id + " not found");
    }

    @Override
    public Advertisement updatePublishedAdvertisement(Long id, String email, boolean isPublished) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        if (advertisementOptional.isPresent()) {
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                throw new UserEmailNotFoundException("User with this email " + email + " not found");
            }
            Advertisement advertisement = advertisementOptional.get();
            if (Objects.equals(userOptional.get().getId(), advertisement.getAdOwner().getId())) {
                advertisement.setPublished(isPublished);
                advertisementRepository.save(advertisement);
                return advertisement;
            }
            throw new DeniedAccessToOrdinaryUsersException("You are not allowed to update this advertisement");
        }
        throw new AdvertisementNotFoundException("Advertisement with this id " + id + " not found");
    }
}
