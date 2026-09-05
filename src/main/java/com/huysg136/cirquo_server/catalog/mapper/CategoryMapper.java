package com.huysg136.cirquo_server.catalog.mapper;

import com.huysg136.cirquo_server.catalog.dto.request.CategoryRequest;
import com.huysg136.cirquo_server.catalog.dto.response.CategoryResponse;
import com.huysg136.cirquo_server.catalog.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}
