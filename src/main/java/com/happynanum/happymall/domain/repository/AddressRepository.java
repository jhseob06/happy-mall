package com.happynanum.happymall.domain.repository;

import com.happynanum.happymall.domain.entity.Account;
import com.happynanum.happymall.domain.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByAccountAndName(Account account, String name);

    List<Address> findAddressesByAccount(Account account);

    Address findByName(String name);

}
