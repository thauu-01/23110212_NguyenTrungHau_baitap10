package baitap10.service;

import baitap10.entity.Category;
import baitap10.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IStorageService storageService;

    @Override
    public <S extends Category> S save(S entity) {
        if (entity.getCategoryId() == null) {
            // Tạo mới
            return categoryRepository.save(entity);
        } else {
            // Cập nhật
            Optional<Category> opt = findById(entity.getCategoryId());
            if (opt.isPresent()) {
                Category existingCategory = opt.get();
                if (StringUtils.hasText(entity.getIcon()) && !entity.getIcon().equals(existingCategory.getIcon())) {
                    // Nếu có icon mới, xóa icon cũ (nếu có) và lưu icon mới
                    if (StringUtils.hasText(existingCategory.getIcon())) {
                        storageService.delete(existingCategory.getIcon());
                    }
                    entity.setIcon(entity.getIcon()); // Giữ icon mới
                } else {
                    // Nếu không có icon mới, giữ icon cũ
                    entity.setIcon(existingCategory.getIcon());
                }
            }
            return categoryRepository.save(entity);
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    @Override
    public List<Category> findAllById(Iterable<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public <S extends Category> Optional<S> findOne(Example<S> example) {
        return categoryRepository.findOne(example);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void delete(Category entity) {
        categoryRepository.delete(entity);
    }

    @Override
    public Optional<Category> findByCategoryName(String name) {
        return categoryRepository.findByCategoryName(name);
    }

    @Override
    public List<Category> findByCategoryNameContaining(String name) {
        return categoryRepository.findByCategoryNameContaining(name);
    }

    @Override
    public Page<Category> findByCategoryNameContaining(String name, Pageable pageable) {
        return categoryRepository.findByCategoryNameContaining(name, pageable);
    }
}