package com.sarc.repository;

import com.sarc.domain.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Long> {
    List<ScheduleSlot> findByResource_ResourceIdAndDayOfWeek(Long resourceId, int dayOfWeek);
}
