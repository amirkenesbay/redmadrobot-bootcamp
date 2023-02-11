package com.amirkenesbay.service;

import com.amirkenesbay.entity.AdTag;
import com.amirkenesbay.entity.Advertisement;
import com.amirkenesbay.entity.Role;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.enums.RolesName;
import com.amirkenesbay.mapper.AdvertisementMapper;
import com.amirkenesbay.repository.AdvertisementRepository;
import com.amirkenesbay.services.AdTagService;
import com.amirkenesbay.services.AdvertisementService;
import com.amirkenesbay.services.ImageService;
import com.amirkenesbay.services.UserService;
import com.amirkenesbay.services.impl.AdvertisementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AdvertisementServiceImplTest {
    private Advertisement ad1, ad2, ad3, ad4;
    private User user1, user2, user3;
    private AdTag tag1, tag2, tag3;

    @MockBean
    private UserService userService;

    @MockBean
    private AdvertisementRepository advertisementRepository;

    @MockBean
    private AdTagService adTagService;

    @MockBean
    private ImageService imageService;

    @MockBean
    private AdvertisementMapper advertisementMapper;

    private AdvertisementService advertisementService;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .isActive(true)
                .email("test@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.ADMIN.toString())))
                .build();

        user2 = User.builder()
                .id(2L)
                .isActive(true)
                .email("test2@@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.USER.toString())))
                .build();

        user3 = User.builder()
                .id(3L)
                .isActive(true)
                .email("test2@@gmail.com")
                .roles(Set.of(new Role(1L, RolesName.USER.toString())))
                .build();

        tag1 = new AdTag(1L, "Tag1", null);
        tag2 = new AdTag(2L, "Tag2", null);
        tag3 = new AdTag(3L, "Tag3", null);

        ad1 = Advertisement.builder()
                .id(1L)
                .adOwner(user1)
                .isPublished(true)
                .isActive(false)
                .price(2000L)
                .advertisementTags(Set.of(tag1, tag2))
                .build();

        ad2 = Advertisement.builder()
                .id(2L)
                .isPublished(true)
                .isActive(true)
                .price(3000L)
                .adOwner(user2)
                .advertisementTags(Set.of(tag1, tag3))
                .build();

        ad3 = Advertisement.builder()
                .id(3L)
                .isPublished(false)
                .isActive(true)
                .price(4000L)
                .adOwner(user3)
                .advertisementTags(Set.of(tag3, tag2))
                .build();

        ad4 = Advertisement.builder()
                .id(4L)
                .isPublished(true)
                .isActive(true)
                .price(5000L)
                .adOwner(user3)
                .advertisementTags(Set.of(tag2, tag3))
                .build();

        Mockito.when(userService.findByEmail(user1.getEmail())).thenReturn(Optional.ofNullable(user1));
        Mockito.when(userService.findByEmail(user2.getEmail())).thenReturn(Optional.ofNullable(user2));

        Mockito.when(advertisementRepository.findAll()).thenReturn(List.of(ad1, ad2, ad3, ad4));
        Mockito.when(advertisementRepository.findById(ad1.getId())).thenReturn(Optional.ofNullable(ad1));
        Mockito.when(advertisementRepository.findById(ad2.getId())).thenReturn(Optional.ofNullable(ad2));
        advertisementService = new AdvertisementServiceImpl(advertisementRepository, adTagService, imageService, advertisementMapper, userService);
    }

    @Test
    void deleteById() {
        advertisementService.deleteById(ad1.getId(), user1.getEmail());
    }

    @Test
    void deleteSomeoneElseMessageByAdmin() {
        advertisementService.deleteById(ad2.getId(), user1.getEmail());
    }

    @Test
    void getPublished() {
        List<Advertisement> items = advertisementService.getPublished(null);
        assertEquals(2, items.size());
        assertTrue(items.stream().allMatch(advertisement -> advertisement.isPublished() && advertisement.isActive()));
    }

    @Test
    void getPublishedFilteredByTags() {
        List<Advertisement> items = advertisementService.getPublished(List.of(tag2.getName()));
        assertEquals(1, items.size());
        assertTrue(items.stream().allMatch(advertisement -> advertisement.isPublished() && advertisement.isActive() && advertisement.getAdvertisementTags().contains(tag2)));
    }
}
