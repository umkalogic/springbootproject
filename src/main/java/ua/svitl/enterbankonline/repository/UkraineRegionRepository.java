package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.UkraineRegion;

public interface UkraineRegionRepository extends JpaRepository<UkraineRegion, Integer> {
    UkraineRegion findUkraineRegionByUkraineRegionId(int regionId);
}
