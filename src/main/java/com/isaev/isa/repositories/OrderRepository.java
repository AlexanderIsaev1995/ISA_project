package com.isaev.isa.repositories;

import com.isaev.isa.entities.Operation;
import com.isaev.isa.entities.Order;
import com.isaev.isa.entities.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findById(Long id);

    List<Order> findAllByState(State state);

    List<Order> findAllByCarOwnerId(Long id);

    int countAllByCarOwnerId(Long id);

    int countAllByCarOwnerIdAndCreationTimeBetween(Long carOwnerId, Timestamp nowMinusYear, Timestamp now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Order o set o.state = :state where o.id = :id")
    void updateState(@Param("state") State state,
                     @Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Order o set o.cost = :cost where o.id = :id")
    void updateCost(@Param("cost") Double cost,
                    @Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Order o set o.operationList = :operations where o.id = :id")
    void updateOperations(@Param("operations") Set<Operation> operations,
                          @Param("id") Long id);

}
