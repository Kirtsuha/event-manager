package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Registration;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMapper {

    private final UserMapper userMapper;
    private final EventMapper eventMapper;

    public RegistrationMapper(UserMapper userMapper, EventMapper eventMapper) {
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
    }

    public Registration entityToDomain(RegistrationEntity entity) {
        return Registration.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .eventId(entity.getEvent().getId())
                .build();
    }

    public RegistrationEntity domainToEntity(Registration domain) {
        return RegistrationEntity.builder()
                .id(domain.getId())
                // .user(userMapper.domainToEntity(domain.getUser())) REMOVED
                // .event(eventMapper.domainToEntity(domain.getEvent())) REMOVED
                .build();
    }


}
