package com.amirkenesbay.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_sender_id", referencedColumnName = "id")
    private User messageSender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_receiver_id", referencedColumnName = "id")
    private User messageReceiver;

    @Column
    private String messageBody;

    @ManyToOne(optional = false)
    @JoinColumn(name = "advertisement_id", referencedColumnName = "id")
    private Advertisement advertisement;

    @DateTimeFormat
    private Date messageCreated = Date.from(java.time.Instant.now());
}

