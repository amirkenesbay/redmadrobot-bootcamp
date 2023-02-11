package com.amirkenesbay.services.impl;

import com.amirkenesbay.entity.Image;
import com.amirkenesbay.repository.ImageRepository;
import com.amirkenesbay.services.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Override
    public List<Image> findAll() {
        List<Image> imageList = new ArrayList<>();
        imageRepository.findAll().iterator().forEachRemaining(imageList::add);
        return imageList;
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }
}
