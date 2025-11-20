package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Category;
import com.zealepsoluciones.libertybackend.repository.CategoryRepository;
import com.zealepsoluciones.libertybackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
     private final CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        category.setActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return (List<Category>)categoryRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        Category category = this.findById(id);
        if(category != null){
            category.setActive(false);
            categoryRepository.save(category);
        }
    }
}
