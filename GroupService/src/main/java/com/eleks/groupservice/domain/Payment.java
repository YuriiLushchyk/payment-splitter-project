package com.eleks.groupservice.domain;

import com.eleks.groupservice.converter.LongListToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_description", nullable = false, length = 200)
    private String paymentDescription;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "co_payers")
    @Convert(converter = LongListToStringConverter.class)
    @Builder.Default
    private List<Long> coPayers = new ArrayList<>();

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp
    private Instant timestamp;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
}
