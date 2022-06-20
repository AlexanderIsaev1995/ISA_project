package com.isaev.isa.services.impl;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import com.isaev.isa.entities.enums.DiscountStatus;
import com.isaev.isa.entities.enums.Role;
import com.isaev.isa.exсeptions.DiscountAlreadyExistException;
import com.isaev.isa.exсeptions.DiscountNotFoundException;
import com.isaev.isa.repositories.DiscountRepository;
import com.isaev.isa.repositories.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class DiscountServiceImplTest {

    @InjectMocks
    private DiscountServiceImpl discountService;

    @Mock
    private  OrderRepository orderRepository;

    @Mock
    private  DiscountRepository discountRepository;

    @Test
    public void checkDiscountWhenOrdersEqualsFiveTest(){
        User user = new User();
        Discount expectedDiscount = new Discount(DiscountStatus.NO_DISCOUNT);

        Mockito.when(orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(5);
        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.NO_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.checkDiscount(user);

        Assert.assertSame(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(orderRepository,Mockito.times(1)).countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.NO_DISCOUNT);
    }

    @Test
    public void checkDiscountWhenOrdersEqualsSevenTest(){
        User user = new User();
        Discount expectedDiscount = new Discount(DiscountStatus.SILVER_DISCOUNT);

        Mockito.when(orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(7);
        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.SILVER_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.checkDiscount(user);

        Assert.assertSame(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(orderRepository,Mockito.times(1)).countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.SILVER_DISCOUNT);
    }

    @Test
    public void checkDiscountWhenOrdersEqualsFourteenTest(){
        User user = new User();
        Discount expectedDiscount = new Discount(DiscountStatus.GOLD_DISCOUNT);

        Mockito.when(orderRepository.countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(14);
        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.GOLD_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.checkDiscount(user);

        Assert.assertSame(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(orderRepository,Mockito.times(1)).countAllByCarOwnerIdAndCreationTimeBetween(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.GOLD_DISCOUNT);
    }

    @Test
    public void newDiscountWhenRoleIsWorkerTest(){
        Role role = Role.WORKER;
        Discount expectedDiscount = new Discount(DiscountStatus.EMPLOYEE_DISCOUNT);

        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.EMPLOYEE_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.newDiscount(role);

        Assert.assertEquals(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.EMPLOYEE_DISCOUNT);
    }

    @Test
    public void newDiscountWhenRoleIsNotWorkerTest(){
        Role role = Role.USER;
        Discount expectedDiscount = new Discount(DiscountStatus.NO_DISCOUNT);

        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.NO_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.newDiscount(role);

        Assert.assertEquals(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.NO_DISCOUNT);
    }

    @Test
    public void addDiscountWhenDiscountNotExistTest(){
        Discount discount = new Discount(DiscountStatus.NO_DISCOUNT);
        Discount expectedDiscount = new Discount(DiscountStatus.NO_DISCOUNT);

        Mockito.when(discountRepository.existsDiscountByDiscountStatus(discount.getDiscountStatus())).thenReturn(false);
        Mockito.when(discountRepository.findByDiscountStatus(DiscountStatus.NO_DISCOUNT)).thenReturn(Optional.of(expectedDiscount));
        Discount resultDiscount = discountService.addDiscount(discount);

        Assert.assertSame(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(DiscountStatus.NO_DISCOUNT);
        Mockito.verify(discountRepository,Mockito.times(1)).existsDiscountByDiscountStatus(DiscountStatus.NO_DISCOUNT);
    }

    @Test
    public void addDiscountWhenDiscountExistTest(){
        Discount discount = new Discount(DiscountStatus.NO_DISCOUNT);

        Mockito.when(discountRepository.existsDiscountByDiscountStatus(discount.getDiscountStatus())).thenReturn(true);
        try{
            Discount resultDiscount = discountService.addDiscount(discount);
        } catch (DiscountAlreadyExistException e){
            e.printStackTrace();
        }

        Mockito.verify(discountRepository,Mockito.times(1)).existsDiscountByDiscountStatus(DiscountStatus.NO_DISCOUNT);
    }

    @Test
    public void deleteDiscountByDiscountStatusWhenDiscountExistTest(){
        DiscountStatus discountStatus = DiscountStatus.NO_DISCOUNT;
        String expectedString = String.format("Discount with discount status - {%s} has been removed", discountStatus.name());

        Mockito.when(discountRepository.existsDiscountByDiscountStatus(discountStatus)).thenReturn(true);

        String resultString = discountService.deleteByDiscountStatus(discountStatus);

        Assert.assertEquals(expectedString,resultString);
        Assert.assertNotNull(resultString);

        Mockito.verify(discountRepository,Mockito.times(1)).existsDiscountByDiscountStatus(discountStatus);
    }

    @Test
    public void deleteDiscountByDiscountStatusWhenDiscountNotExistTest(){
        DiscountStatus discountStatus = DiscountStatus.NO_DISCOUNT;

        Mockito.when(discountRepository.existsDiscountByDiscountStatus(discountStatus)).thenReturn(false);

        try{
            String resultString = discountService.deleteByDiscountStatus(discountStatus);
        } catch (DiscountNotFoundException e){
            e.printStackTrace();
        }

        Mockito.verify(discountRepository,Mockito.times(1)).existsDiscountByDiscountStatus(discountStatus);
    }

    @Test
    public void findDiscountWhenDiscountExistTest(){
        DiscountStatus discountStatus = DiscountStatus.NO_DISCOUNT;
        Discount expectedDiscount = new Discount(DiscountStatus.NO_DISCOUNT);

        Mockito.when(discountRepository.findByDiscountStatus(discountStatus)).thenReturn(Optional.of(expectedDiscount));

        Discount resultDiscount = discountService.findDiscount(discountStatus);

        Assert.assertSame(expectedDiscount,resultDiscount);
        Assert.assertNotNull(resultDiscount);

        Mockito.verify(discountRepository,Mockito.times(1)).findByDiscountStatus(discountStatus);
    }

    @Test
    public void findDiscountWhenDiscountNotExistTest(){
        DiscountStatus discountStatus = DiscountStatus.NO_DISCOUNT;

        try{
            Discount resultDiscount = discountService.findDiscount(discountStatus);
        } catch (DiscountNotFoundException e){
            e.printStackTrace();
        }
    }
}
