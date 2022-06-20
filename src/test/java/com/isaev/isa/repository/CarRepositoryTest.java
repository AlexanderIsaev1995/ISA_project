package com.isaev.isa.repository;

import com.isaev.isa.IsaApplication;
import com.isaev.isa.entities.Car;
import com.isaev.isa.repositories.CarRepository;
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
public class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Before
    public void before() {
        Car car = TestUtil.car();
        carRepository.save(car);
    }

    @After
    public void clean() {
        carRepository.deleteAll();
    }

    @Test
    public void existsCarByNumberTest(){
        boolean exist = carRepository.existsCarByNumber("M298EX152");
        assertEquals(true,exist);
        Assert.assertNotNull(exist);
    }

    @Test
    public void existsCarByNumberFailTest(){
        boolean exist = carRepository.existsCarByNumber("H243AX152");
        assertEquals(false,exist);
        Assert.assertNotNull(exist);
    }

    @Test
    public void findByCarNumberTest(){
        Car car = TestUtil.car();
        Car carFromDB = carRepository.findByNumber("M298EX152").get();
        assertEquals(car,carFromDB);
        Assert.assertNotNull(carFromDB);
    }

    @Test
    public void findByCarNumberFailTest(){
        Optional<Car> carFromDB = carRepository.findByNumber("H243AX152");
        assertEquals(Optional.empty(),carFromDB);
        Assert.assertNotNull(carFromDB);
    }
}
