package com.my.rental.repository;

import com.my.rental.domain.Rental;
import com.my.rental.domain.ReturnedItem;
import java.awt.print.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnedItemRepository extends JpaRepository<ReturnedItem, Long> {}
