package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Store;
import com.fnb.membership.fnbmembership.dto.CheckedStoreDto;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * A service to manage store information.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;


    /**
     * A method for checking the store using its ID.
     * @param storeId
     * @return
     * @throws NoSuchStoreException
     */
    public CheckedStoreDto checkStore(Long storeId) throws NoSuchStoreException {

        log.info("checkStore requested. storeId=" + storeId);

        // Finding the store with brand.
        Optional<Store> store = storeRepository.findById(storeId);

        // If the store already exists, return it.
        if (store.isPresent()) {
            Store validStore = store.get();
            Brand validBrand = validStore.getBrand();

            log.info("store is valid. storeId= " + storeId + " brandId=" + validBrand.getId());

            return CheckedStoreDto.builder()
                    .storeId(validStore.getId())
                    .storeName(validStore.getName())
                    .brandId(validBrand.getId())
                    .brandName(validBrand.getName())
                    .build();

        } else { // If the store doesn't existm, throw a NoSuchStoreException.

            log.error("invalid storeId. storeId=" + storeId);
            throw new NoSuchStoreException();
        }
    }
}
