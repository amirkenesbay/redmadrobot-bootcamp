package com.amirkenesbay.services;

import com.amirkenesbay.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageService {
    List<Image> findAll();

    Image save(Image image);
}
