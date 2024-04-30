package br.com.one.mapper.custom;

import br.com.one.data.vo.v1.PersonVO;
import br.com.one.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonMapper {

    public PersonVO convertEntityToVo(Person person){
        PersonVO vo = new PersonVO();
        vo.setKey(person.getId());
        vo.setAddress(person.getAddress());
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setGender(person.getGender());
        return vo;
    }

    public Person convertVoToEntity(PersonVO person){
        Person entity = new Person();
        entity.setId(person.getKey());
        entity.setAddress(person.getAddress());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setGender(person.getGender());
//        entity.setBirthDay(new Date());

        return entity;
    }
}
