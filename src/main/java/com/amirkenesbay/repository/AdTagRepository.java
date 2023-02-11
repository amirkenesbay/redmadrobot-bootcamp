package com.amirkenesbay.repository;

import com.amirkenesbay.entity.AdTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdTagRepository extends JpaRepository<AdTag, Long> {
    Optional<AdTag> findAdTagByName(String name);
}
