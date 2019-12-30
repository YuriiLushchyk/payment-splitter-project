package com.eleks.groupservice.domain;

import com.eleks.groupservice.converter.LongListToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "members")
    @Convert(converter = LongListToStringConverter.class)
    private List<Long> members;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", fetch = FetchType.EAGER)
    private Set<Payment> payments;
}
