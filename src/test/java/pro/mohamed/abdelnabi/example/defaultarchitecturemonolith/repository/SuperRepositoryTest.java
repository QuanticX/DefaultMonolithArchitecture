package pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository;


import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.SuperEntityTest;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.entity.base.SuperEntity;
import pro.mohamed.abdelnabi.example.defaultarchitecturemonolith.repository.base.SuperRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public abstract class SuperRepositoryTest <E extends SuperEntity,R extends SuperRepository<E>> implements SuperEntityTest<E> {


    protected R repository;

    public SuperRepositoryTest(R repository) {
        this.repository = repository;
    }


    @Test
    void injectedComponentIsNotNull() {
        assertThat(repository).isNotNull();
    }

    @Test
    void whenSavedThenFindsById() {
        E entity = generateEntity();
        entity = repository.save(entity);
        E found = repository.findById(entity.getId()).get();
        assertThat(found.getId()).isEqualTo(entity.getId());
    }

    @Test
    void whenSavedThenFindAll() {
        E entity = generateEntity();
        repository.save(entity);
        assertThat(repository.findAll().size()).isGreaterThan(0);
    }

    @Test
    void whenSavedThenDelete() {
        E entity = generateEntity();
        entity = repository.save(entity);
        repository.delete(entity);
        assertThat(repository.findById(entity.getId())).isEmpty();
    }

    @Test
    void whenSavedThenUpdate() {
        E entity = generateEntity();
        E toSave = cloneAndModifyEntity(entity);
        repository.save(toSave);
        E found = repository.findById(toSave.getId()).get();
        assertThat(found).isNotEqualTo(entity);
    }



}
