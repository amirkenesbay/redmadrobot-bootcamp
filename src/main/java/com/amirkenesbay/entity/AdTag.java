package com.amirkenesbay.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ad_tags")
public class AdTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;
}
