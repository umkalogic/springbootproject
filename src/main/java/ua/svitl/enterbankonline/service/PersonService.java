package ua.svitl.enterbankonline.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.svitl.enterbankonline.model.PassportData;
import ua.svitl.enterbankonline.model.Person;
import ua.svitl.enterbankonline.model.User;
import ua.svitl.enterbankonline.repository.AddressRepository;
import ua.svitl.enterbankonline.repository.PassportDataRepository;
import ua.svitl.enterbankonline.repository.PersonRepository;
import ua.svitl.enterbankonline.repository.PhoneNumberRepository;
import ua.svitl.enterbankonline.utilities.UserManagementExceptions;

import java.math.BigInteger;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final PassportDataRepository passportDataRepository;

    public PersonService(PersonRepository personRepository, PhoneNumberRepository phoneNumberRepository, AddressRepository addressRepository, PassportDataRepository passportDataRepository) {
        this.personRepository = personRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.passportDataRepository = passportDataRepository;
    }

    public PassportData getFirstDomesticPassportByPerson(Person person) {
        return passportDataRepository.findFirstByPersonByPersonIdAndIsDomesticTrue(person)
                .orElse(passportDataRepository.findFirstByPersonByPersonId(person)
                        .orElseThrow(() -> new UsernameNotFoundException("No passport data by this person!"))
                );
    }


    public Person getPersonByTaxCode(BigInteger taxCode) {
        return personRepository.findPersonByIdNumberTaxCode(taxCode)
                .orElseThrow(() -> new UsernameNotFoundException("Person data not found: no such tax code"));
    }

    public Person getPersonByPersonId(int personId) {
        return personRepository.findPersonByPersonId(personId)
                .orElseThrow(() -> new UsernameNotFoundException("Person data not found: no such person id"));
    }

    private Person getPersonByPersonUsersContains(User user) {
        return personRepository.findPersonByPersonUsersContains(user)
                .orElseThrow(() -> new UsernameNotFoundException("Person not found: no such user"));
    }

    public Person getPersonByUser(User user) {
        try {
            return getPersonByPersonUsersContains(user);
        } catch (UserManagementExceptions ex) {
            //TODO log exception
            return new Person();
        }
    }

}
