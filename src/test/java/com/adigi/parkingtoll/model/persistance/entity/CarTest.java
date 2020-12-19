package com.adigi.parkingtoll.model.persistance.entity;

import com.adigi.parkingtoll.annotation.DataJpaTestJunit;
import com.adigi.parkingtoll.model.enums.EngineType;
import com.adigi.parkingtoll.model.enums.VehicleType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTestJunit
public class CarTest {

    private static final String NAME_UID = "NAME_UID";

    @Autowired
    private TestEntityManager entityManager;

    private Car car;

    @BeforeEach
    public void setUp() {
        car = new Car();
        car.setNameUid(NAME_UID);
        car.setVehicleType(VehicleType.CAR);
    }

    @Test
    public void shouldPersistEngineTypeEnumConvertedValue() {

        // given
        EngineType engineType = EngineType.ELECTRIC_20KW;
        car.setEngineType(engineType);

        // when
        entityManager.persistAndFlush(car);

        Car persistedCar = entityManager.find(Car.class, car.getId());

        // then
        assertEquals(car.getId(), persistedCar.getId());
        assertEquals(NAME_UID, persistedCar.getNameUid());
        assertEquals(engineType, persistedCar.getEngineType());
    }

    @Test
    public void shouldFindCarByEngineType() {

        // given
        EngineType engineType = EngineType.ELECTRIC_20KW;
        car.setEngineType(engineType);
        entityManager.persistAndFlush(car);

        String jpql = "select c from Car c where c.engineType = com.adigi.parkingtoll.model.enums.EngineType.ELECTRIC_20KW";

        // when
        List<Car> cars = entityManager.getEntityManager().createQuery(jpql, Car.class).getResultList();

        // then
        assertEqualLastCarFromList(cars, engineType, NAME_UID);
    }

    @Test
    public void shouldFindCarByEngineTypeParameter() {

        // given
        final String nameTest = "TEST_PARAM";
        EngineType engineType = EngineType.ELECTRIC_50KW;

        updateCar(car, engineType, nameTest);
        entityManager.persistAndFlush(car);

        // when
        List<Car> cars = retrieveCarsByEngineTypeParameter(engineType);

        // then
        assertEqualLastCarFromList(cars, engineType, nameTest);
    }

    private void assertEqualLastCarFromList(List<Car> cars, EngineType engineType, String nameUid) {

        assertTrue(!cars.isEmpty());
        assertEquals(engineType, cars.get(cars.size() - 1).getEngineType());
        assertEquals(nameUid, cars.get(cars.size() - 1).getNameUid());

    }

    private List<Car> retrieveCarsByEngineTypeParameter(EngineType engineType) {

        String jpql = "select c from Car c where c.engineType = :engineType";

        TypedQuery<Car> query = entityManager.getEntityManager().createQuery(jpql, Car.class);
        query.setParameter("engineType", engineType);

        return query.getResultList();
    }


    private void updateCar(Car car, EngineType engineType, String nameUid) {

        if (engineType != null) {
            car.setEngineType(engineType);
        }

        if (nameUid != null) {
            car.setNameUid(nameUid);
        }
    }
}
