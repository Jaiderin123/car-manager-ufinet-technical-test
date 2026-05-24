package com.ufinet.carmanager.infrastructure.adapters.persistence.user.mapper;

import com.ufinet.carmanager.domain.user.model.AppUser;
import com.ufinet.carmanager.infrastructure.adapters.persistence.user.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AppUser toDomain(UserEntity userEntity);;
}
