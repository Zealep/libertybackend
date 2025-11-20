package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InstallmentRepository extends CrudRepository<Installment,Long> {

    // Buscar cuotas pendientes con dueDate entre hoy y una fecha máxima (por ejemplo today + daysAhead)
    @Query("SELECT i FROM Installment i WHERE i.status = :status AND i.dueDate BETWEEN :from AND :to")
    List<Installment> findByStatusAndDueDateBetween(@Param("status") InstallmentStatus status,
                                                   @Param("from") LocalDate from,
                                                   @Param("to") LocalDate to);

    // Buscar cuotas pendientes vencidas (dueDate < today)
    @Query("SELECT i FROM Installment i WHERE i.status = :status AND i.dueDate < :today")
    List<Installment> findByStatusAndDueDateBefore(@Param("status") InstallmentStatus status,
                                                   @Param("today") LocalDate today);
}
