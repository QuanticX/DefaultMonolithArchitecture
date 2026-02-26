package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;

/**
 * Generic repository interface for entities extending SuperEntity.
 * <p>
 * This interface extends Spring Data JPA's JpaRepository to provide
 * basic CRUD operations for any entity type that extends SuperEntity.
 *
 * @param <T> the type of the entity, which must extend {@link SuperEntity}
 */
@NoRepositoryBean
public interface SuperRepository<T extends SuperEntity> extends JpaRepository<T, Long> {
}
