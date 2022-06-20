package com.isaev.isa.repository;

import com.isaev.isa.IsaApplication;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.repositories.DiscountRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = IsaApplication.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DiscountRepositoryTest {

    @Autowired
    private DiscountRepository discountRepository;

    @Before
    public void before() {
        Discount discount = new Discount(DiscountStatus.EMPLOYEE_DISCOUNT);
        discountRepository.save(discount);
    }
    @After
    public void clean(){
        discountRepository.deleteAll();
    }

    @Test
    public void existsDiscountByDiscountStatusTest(){
        boolean exist = discountRepository.existsDiscountByDiscountStatus(DiscountStatus.EMPLOYEE_DISCOUNT);
        assertEquals(true,exist);
        Assert.notNull(exist);
    }

    @Test
    public void existsDiscountByDiscountStatusFailTest(){
        boolean exist = discountRepository.existsDiscountByDiscountStatus(DiscountStatus.SILVER_DISCOUNT);
        assertEquals(false,exist);
        Assert.notNull(exist);
    }

    @Test
    public void findByDiscountStatusTest(){
        Discount discount = new Discount(DiscountStatus.EMPLOYEE_DISCOUNT);
        Discount discountFromDB = discountRepository.findByDiscountStatus(DiscountStatus.EMPLOYEE_DISCOUNT).get();
        assertEquals(discount,discountFromDB);
        Assert.notNull(discountFromDB);
    }

    @Test
    public void findByDiscountStatusFailTest(){
        Optional<Discount> discountFromDB = discountRepository.findByDiscountStatus(DiscountStatus.SILVER_DISCOUNT);
        assertEquals(Optional.empty(),discountFromDB);
        Assert.notNull(discountFromDB);
    }
}
