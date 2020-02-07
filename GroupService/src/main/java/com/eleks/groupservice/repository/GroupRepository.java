package com.eleks.groupservice.repository;

import com.eleks.groupservice.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("FROM user_group WHERE concat(';', members, ';') LIKE concat('%;', :id, ';%')")
    List<Group> findAllWithMember(@Param("id") Long memberId);
}