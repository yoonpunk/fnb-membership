package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Store;
import com.fnb.membership.fnbmembership.dto.CheckedStoreDto;
import com.fnb.membership.fnbmembership.exception.NoSuchBrandException;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * 점포 정보를 관리하는 서비스
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

//    /**
//     * 브랜드명을 받아 브랜드를 생성하는 메서드, 브랜드가 존재할 경우 기존에 있는 브랜드 정보를 전달
//     * CheckedStoreDto 생성된 브랜드 정보를 담아 리턴
//     * @param brandName
//     * @return CheckedStoreDto
//     */
//    public CheckedStoreDto createStore(String brandName, String storeName) throws NoSuchBrandException {
//
//        log.info("createStore requested. brandName=" + brandName + " storeName=" + storeName);
//
//        // 동일한 이름의 브랜드가 있는지 조회
//        Optional<Brand> brand = brandRepository.findByName(brandName);
//
//        // 브랜드가 존재하지 않다면, NoSuchBrandException 예외 발생
//        if (brand.isEmpty()) {
//
//            log.error("invalid brand. brandName=" + brandName);
//            throw new NoSuchBrandException();
//
//        }
//
//        Brand validBrand = brand.get();
//        log.info("brand is valid. brandName=" + validBrand.getName());
//
//        // 브랜드가 존재하면, 점포가 이미 존재하는지 확인
//        Optional<Store> store = storeRepository.findByName(storeName);
//
//        // 동일한 이름의 점포가 존재하면, 해당 점포의 정보를 반환
//        if (store.isPresent()) {
//
//            Store validStore = store.get();
//            log.info("store is already exist. storeName=" + validStore.getName());
//
//            return CheckedStoreDto.builder()
//                    .storeId(validStore.getId().toString())
//                    .storeName(validStore.getName())
//                    .brandId(validBrand.getId().toString())
//                    .brandName(validBrand.getName())
//                    .build();
//        } else { // 점포가 없을 경우, 점포 생성
//
//            Store createdStore = Store.createStore(storeName);
//            createdStore = storeRepository.save(createdStore);
//
//            log.info("creating store is completed. storeName=" + storeName);
//            return CheckedStoreDto.builder()
//                    .storeId(createdStore.getId().toString())
//                    .storeName(createdStore.getName())
//                    .brandId(validBrand.getId().toString())
//                    .brandName(validBrand.getName())
//                    .build();
//        }
//    }

    /**
     * 점포 정보가 존재하는지 확인하는 메서드, 점포의 ID를 통해 점포 정보를 조회
     * 점포 정보가 존재할 경우, 점포 정보를 CheckedStoreDto에 담아 리턴
     * 존재하지 않을 경우, NoSuchStoreException 예외 발생
     * @param storeId
     * @return CheckedStoreDto
     */
    public CheckedStoreDto checkStore(String storeId) throws NoSuchStoreException {

        log.info("checkStore requested. storeId=" + storeId);

        // 점포가 있는지 브랜드와 함께 조회
        Optional<Store> store = storeRepository.findById(UUID.fromString(storeId));

        // 점포가 존재한다면, 점포 정보 리턴
        if (store.isPresent()) {
            Store validStore = store.get();
            Brand validBrand = validStore.getBrand();

            log.info("store is valid. storeId= " + storeId + " brandId=" + validBrand.getId());

            return CheckedStoreDto.builder()
                    .storeId(validStore.getId().toString())
                    .storeName(validStore.getName())
                    .brandId(validBrand.getId().toString())
                    .brandName(validBrand.getName())
                    .build();

        } else { // 점포가 존재하지 않다면, NoSuchStoreException 발생

            log.error("invalid storeId. storeId=" + storeId);
            throw new NoSuchStoreException();
        }
    }
}
