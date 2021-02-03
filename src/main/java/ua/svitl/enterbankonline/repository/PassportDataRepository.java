package ua.svitl.enterbankonline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.svitl.enterbankonline.model.PassportData;
import ua.svitl.enterbankonline.model.User;


public interface PassportDataRepository extends JpaRepository<PassportData, Integer> {
    PassportData readPassportDataByPassportSeriesAndPassportNumber(String passportSeries, int passportNumber);
    PassportData findPassportDataByPassportSeriesAndPassportNumber(String passportSeries, int passportNumber);
    PassportData queryAllByUserByUserId(User user);
}
