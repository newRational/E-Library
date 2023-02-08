package ru.perevoshchikov.springcourse.util.mappers;

import org.springframework.stereotype.Component;
import ru.perevoshchikov.springcourse.models.Person;

@Component
public class PersonMapper implements Mapper<Person, Person> {

    @Override
    public void map(Person dest, Person source) {
        dest.setId(source.getId());
        dest.setFullName(source.getFullName());
        dest.setYearOfBirth(source.getYearOfBirth());
    }
}
