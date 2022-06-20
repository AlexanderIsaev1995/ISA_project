package com.isaev.isa.repositories;

import com.isaev.isa.entities.Discount;
import com.isaev.isa.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsername(String username);

    Optional<User> findByUsername(String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User user set user.discount = :discount where user.id = :id")
    void updateUserDiscount(@Param("discount") Discount discount,
                            @Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User user set user.bonusPoints = :bonusPoints where user.id = :id")
    void updateUserBonusPoints(@Param("bonusPoints") Double bonusPoints,
                               @Param("id") Long id);

    @Query(value = "ALTER IDENTITY id RESTART WITH 1L;",
            nativeQuery = true)
    void resetSequence();
}
