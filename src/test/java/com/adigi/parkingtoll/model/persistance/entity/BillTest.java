package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.annotation.DataJpaTestJunit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTestJunit
public class BillTest {


    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveBill_shouldSaveWithDefaultSqlValues() {
        Bill bill = new Bill();
        //TODO
//        bill = userRepository.save(bill);
//
//        assertEquals(bill.getName(), "John Snow");
//        assertEquals(bill.getAge(), 25);
//        assertFalse(bill.getLocked());
    }

}
