package com.amirkenesbay.services;

import com.amirkenesbay.entity.AdTag;

import java.util.List;
import java.util.Optional;

public interface AdTagService {
    Optional<AdTag> findByName(String name);

    List<AdTag> findAll();

    AdTag save(AdTag tag);

    AdTag update(AdTag tag);
}
