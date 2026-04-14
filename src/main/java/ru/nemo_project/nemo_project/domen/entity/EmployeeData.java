package ru.nemo_project.nemo_project.domen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Table(name = "employee_data")
@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(exclude = {"version"})
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class EmployeeData {

    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", unique = true, updatable = false, nullable = false)
    private UUID employeeId;

    @Column(name = "overtime_minutes_accumulated", nullable = false)
    private BigDecimal overtimeMinutesAccumulated = BigDecimal.ZERO;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EmployeeData that = (EmployeeData) o;
        return getEmployeeId() != null && Objects.equals(getEmployeeId(), that.getEmployeeId());
    }

    @Override
    public final int hashCode() {
        return getEmployeeId() != null ? getEmployeeId().hashCode() : 0;
    }
}
