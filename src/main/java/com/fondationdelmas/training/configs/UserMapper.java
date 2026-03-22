package com.fondationdelmas.training.configs;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fondationdelmas.training.dtos.CreateUserRequestDTO;
import com.fondationdelmas.training.dtos.UserResponseDTO;
import com.fondationdelmas.training.entities.UserEntity;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper {
    
    CreateUserRequestDTO toDto(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toEntity(CreateUserRequestDTO dto);

    List<UserResponseDTO> toDtoList(List<UserEntity> users);

    UserResponseDTO toDtoResponse(UserEntity entity);

    // Update partiel (met à jour les données de entity à partir de dto et ignore les champs null de dto : non compatible record)
    //void updateEntityFromDto(EntrepriseDTO dto, @MappingTarget Entreprise entity);
}
