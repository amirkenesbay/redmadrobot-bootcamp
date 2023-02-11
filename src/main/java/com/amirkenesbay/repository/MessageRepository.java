package com.amirkenesbay.repository;

import com.amirkenesbay.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllMessagesByAdvertisementId(Long advertisementId);
}
