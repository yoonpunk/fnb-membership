package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Store;
import com.fnb.membership.fnbmembership.dto.CheckedStoreDto;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.repository.BrandRepository;
import com.fnb.membership.fnbmembership.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreServiceTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private  BrandRepository brandRepository;

    private StoreService storeService;

    private Brand existBrand;
    private Store existStore;

    @BeforeEach
    public void init() {

        existBrand = Brand.createBrand("CAFE");
        brandRepository.save(existBrand);

        existStore = Store.createStore("MOONBUCKS", existBrand);
        storeRepository.save(existStore);

        storeService = new StoreService(storeRepository);
    }

    @Test
    void checkStore_존재하는_스토어_조회_성공() {

        // given
        String searchedStoreId = existStore.getId().toString();

        // when
        CheckedStoreDto checkedStoreDto = storeService.checkStore(searchedStoreId);

        // then
        assertThat(checkedStoreDto.getStoreId()).isEqualTo(searchedStoreId);
        assertThat(checkedStoreDto.getStoreName()).isEqualTo(existStore.getName());
        assertThat(checkedStoreDto.getBrandId()).isEqualTo(existBrand.getId().toString());
        assertThat(checkedStoreDto.getBrandName()).isEqualTo(existBrand.getName());
    }

    @Test
    void checkStore_스토어_조회_실패_예외발생() {

        // given
        String notExistStoreId = "12345678-69c9-4712-b769-81af544cb69b";

        // when & then
        Assertions.assertThrows(NoSuchStoreException.class, () -> {
            storeService.checkStore(notExistStoreId);
        });
    }
}