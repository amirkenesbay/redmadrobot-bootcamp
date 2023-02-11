package com.amirkenesbay.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User adOwner;

    @Column
    private boolean isActive;

    @Column
    private boolean isPublished;

    @Column
    private Long price;

    @OneToMany(mappedBy = "advertisement", targetEntity = Message.class)
    private Collection<Message> messages;

    @OneToMany(mappedBy = "advertisement", targetEntity = Message.class)
    private Collection<Image> images;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "advertisement_tags",
            joinColumns = {@JoinColumn(name = "advertisement_id")},
            inverseJoinColumns = {@JoinColumn(name = "advertisement_tag_id")}
    )
    private Set<AdTag> advertisementTags;

    @DateTimeFormat
    private Date adCreated = Date.from(Instant.now());
}
