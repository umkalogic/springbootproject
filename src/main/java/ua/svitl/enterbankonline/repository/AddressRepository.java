package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
