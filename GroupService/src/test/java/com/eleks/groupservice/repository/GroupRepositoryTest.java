package com.eleks.groupservice.repository;

import com.eleks.groupservice.domain.Currency;
import com.eleks.groupservice.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GroupRepositoryTest {

    @Autowired
    GroupRepository repository;

    Group group;

    @BeforeEach
    void setUp() {
        group = Group.builder()
                .groupName("groupName")
                .currency(Currency.EUR)
                .members(Arrays.asList(1L, 2L, 3L)).build();
    }

    @Test
    void save_SaveGroupWithoutId_ShouldReturnSavedWithId() {
        Group saved = repository.save(group);
        assertNotNull(saved.getId());
    }

    @Test
    void save_UpdateGroupWithNewName_ShouldReturnUpdatedGroup() {
        Group saved = repository.save(group);
        String newName = "newName";

        saved.setGroupName(newName);

        Group updated = repository.save(saved);
        assertEquals(newName, updated.getGroupName());
    }

    @Test
    void save_GroupWithoutName_ShouldThrowDataIntegrityViolationException() {
        group.setGroupName(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(group));
    }

    @Test
    void save_GroupWithoutCurrency_ShouldThrowDataIntegrityViolationException() {
        group.setCurrency(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> repository.save(group));
    }
}