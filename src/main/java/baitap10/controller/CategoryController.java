package baitap10.controller;

import baitap10.entity.Category;
import baitap10.service.ICategoryService;
import baitap10.service.IStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String add(ModelMap model) {
        model.addAttribute("category", new Category());
        return "admin/categories/addOrEdit";
    }

    // Lưu hoặc cập nhật Category
    @PostMapping("/saveOrUpdate")
    public String saveOrUpdate(@Valid @ModelAttribute("category") Category category,
                               BindingResult result,
                               @RequestParam("icon") MultipartFile icon,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/categories/addOrEdit";
        }

        // Upload file icon
        if (!icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            category.setIcon(storageService.getSorageFilename(icon, uuString));
            storageService.store(icon, category.getIcon());
        }

        categoryService.save(category);

        // Thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Category saved successfully!");

        // Redirect về trang phân trang để hiển thị dữ liệu mới nhất
        return "redirect:/admin/categories/searchpaginated";
    }

    // Chỉnh sửa category
    @GetMapping("/edit/{categoryId}")
    public String edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            model.addAttribute("category", optCategory.get());
            return "admin/categories/addOrEdit";
        } else {
            return "redirect:/admin/categories/searchpaginated";
        }
    }

    // Xóa category
    @GetMapping("/delete/{categoryId}")
    public String delete(@PathVariable("categoryId") Long categoryId,
                         RedirectAttributes redirectAttributes) {
        categoryService.deleteById(categoryId);
        redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
        return "redirect:/admin/categories/searchpaginated";
    }

    // Danh sách không phân trang
    @GetMapping("/list")
    public String list(ModelMap model) {
        List<Category> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";
    }

    // Tìm kiếm + phân trang
    @GetMapping("/searchpaginated")
    public String search(ModelMap model,
                         @RequestParam(name = "name", required = false) String name,
                         @RequestParam(name = "page", defaultValue = "1") int page,
                         @RequestParam(name = "size", defaultValue = "5") int size) {

        // Đảm bảo page >= 1
        if (page < 1) page = 1;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("categoryName"));
        Page<Category> resultPage;

        if (StringUtils.hasText(name)) {
            resultPage = categoryService.findByCategoryNameContaining(name, pageable);
            model.addAttribute("name", name);
        } else {
            resultPage = categoryService.findAll(pageable);
        }

        model.addAttribute("categories", resultPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);

        return "admin/categories/searchpaginated";
    }
}
