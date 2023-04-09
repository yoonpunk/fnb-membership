package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Store;
import com.fnb.membership.fnbmembership.dto.CheckedStoreDto;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.repository.BrandRepository;
import com.fnb.membership.fnbmembership.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreServiceTest {

    private StoreService sut;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private  BrandRepository brandRepository;

    @BeforeEach
    public void setUpSut() {
        sut = new StoreService(storeRepository);
    }

    @Test
    void testCheckStoreIfStoreIsValid() {

        // arrange
        Brand expectedBrand = brandRepository.save(Brand.createBrandWithUuid("MOONBUCKS", LocalDateTime.now()));
        Store expectedStore = storeRepository.save(Store.createStoreWithUuid("MOONBUCKS 매탄점", expectedBrand, LocalDateTime.now()));

        // act
        CheckedStoreDto checkedStoreDto = sut.checkStore(expectedStore.getId());

        // assert
        assertCheckStoreDto(checkedStoreDto, expectedBrand, expectedStore);
    }

    @Test
    void testCheckStoreIfStoreIsInvalidAndThrowException() {

        // arrange
        // If there exists a store with ID 100, delete the store and run this test.
        // After the test finished, a rollback will be executed, so the store will not be permanently deleted."
        Long notExistStoreId = 1000L;
        Optional<Store> store = storeRepository.findById(notExistStoreId);
        if (store.isPresent()) {
            storeRepository.deleteById(1000L);
        }

        // act & assert
        Assertions.assertThrows(NoSuchStoreException.class, () -> {
            sut.checkStore(notExistStoreId);
        });
    }

    private void assertCheckStoreDto(CheckedStoreDto checkedStoreDto, Brand expectedBrand, Store expectedStore) {
        assertThat(checkedStoreDto.getBrandId()).isEqualTo(expectedBrand.getId());
        assertThat(checkedStoreDto.getBrandName()).isEqualTo(expectedBrand.getName());
        assertThat(checkedStoreDto.getStoreId()).isEqualTo(expectedStore.getId());
        assertThat(checkedStoreDto.getStoreName()).isEqualTo(expectedStore.getName());
    }
}