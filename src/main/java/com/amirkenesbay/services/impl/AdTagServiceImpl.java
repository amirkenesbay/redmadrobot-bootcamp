package com.amirkenesbay.services.impl;

import com.amirkenesbay.entity.AdTag;
import com.amirkenesbay.exception.AdTagNameNotFoundException;
import com.amirkenesbay.exception.AdTagNotFoundException;
import com.amirkenesbay.repository.AdTagRepository;
import com.amirkenesbay.services.AdTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
@RequiredArgsConstructor
public class AdTagServiceImpl implements AdTagService {

    private final AdTagRepository adTagRepository;

    @Override
    public Optional<AdTag> findByName(String name) {
        log.info("Get tag name: " + adTagRepository.findAdTagByName(name));
        return adTagRepository.findAdTagByName(name);
    }

    @Override
    public List<AdTag> findAll() {
        List<AdTag> tagList = new ArrayList<>();
        adTagRepository.findAll().iterator().forEachRemaining(tagList::add);
        log.info("Get all tags: " + tagList);
        return tagList;
    }

    @Override
    public AdTag save(AdTag tag) {
        if (isExists(tag.getName())) {
            log.error("Tag with name + " + tag.getName() + " is already exists");
            throw new AdTagNameNotFoundException("Tag with name + " + tag.getName() + " is already exists");
        }
        return adTagRepository.save(tag);
    }

    @Override
    public AdTag update(AdTag tag) {
        Optional<AdTag> adTag = adTagRepository.findById(tag.getId());
        if (adTag.isPresent()) {
            AdTag tagFromDb = adTag.get();
            tagFromDb.setDescription(tag.getDescription());
            tagFromDb.setName(tag.getName());
            adTagRepository.save(tagFromDb);
            return tagFromDb;
        }
        log.error("Tag with id + " + tag.getId() + " is not found");
        throw new AdTagNotFoundException("Tag with id = " + tag.getId() + " not found");
    }

    private boolean isExists(String name) {
        return adTagRepository.findAdTagByName(name).isPresent();
    }
}
