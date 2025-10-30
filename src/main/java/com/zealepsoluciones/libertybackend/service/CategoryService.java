package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Category;

import java.util.List;

public interface CategoryService {
    Category save(Category category);
    Category findById(Long id);
    List<Category> findAll();
    void delete(Long id);
}
