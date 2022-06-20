package com.isaev.isa.repository;

import com.isaev.isa.IsaApplication;
import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.repositories.DiscountRepository;
import com.isaev.isa.repositories.UserRepository;
import com.isaev.isa.util.TestUtil;
import org.junit.After;
import org.junit.Assert;
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
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiscountRepository discountRepository;
    private User testUser;
    private Discount discount;

    @Before
    public void before() {
        testUser = TestUtil.user();
        discount = TestUtil.discount();
        userRepository.save(testUser);
        discountRepository.save(discount);
    }
    @After
    public void clean(){
        userRepository.deleteAll();
    }

    @Test
    public void existUserByUsernameTest(){
        boolean exist = userRepository.existsUserByUsername("user");
        assertEquals(true,exist);
        Assert.assertNotNull(exist);
    }

    @Test
    public void existUserByUsernameFailTest(){
        boolean exist = userRepository.existsUserByUsername("noUser");
        assertEquals(false,exist);
        Assert.assertNotNull(exist);
    }

    @Test
    public void findByUsernameTest() {
        Optional<User> user = userRepository.findByUsername("user");
        if (user.isPresent()) {
            assertEquals(testUser, user.get());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void findByUsernameFailTest(){
        Optional<User> user = userRepository.findByUsername("noUser");
        assertEquals(Optional.empty(),user);
        Assert.assertNotNull(user);
    }

    @Test
    public void updateUserBonusPointsTest(){
        Double bonusPoints = 100.;
        Double oldBonusPoints = userRepository.findById(1L).get().getBonusPoints();
        userRepository.updateUserBonusPoints(100., 1L);
        Double newBonusPoints = userRepository.findById(1L).get().getBonusPoints();
        assertEquals(bonusPoints,newBonusPoints);
        Assert.assertNotEquals(oldBonusPoints,newBonusPoints);
        Assert.assertNotNull(newBonusPoints);
    }

    @Test
    public void updateUserDiscountTest(){
        User user = userRepository.findByUsername("user").get();
        userRepository.updateUserDiscount(discount,user.getId());
        Optional<User> userNew = userRepository.findByUsername("user");
        Discount discountNew = userNew.get().getDiscount();
        assertEquals(discount,discountNew);
        Assert.assertNotNull(userNew);
        Assert.assertNotNull(discountNew);

    }
}
