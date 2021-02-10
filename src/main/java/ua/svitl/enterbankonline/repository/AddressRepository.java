package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
