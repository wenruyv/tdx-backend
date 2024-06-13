package edu.hit.tdxbackend.controller;

import edu.hit.tdxbackend.entity.ResultInfo;
import edu.hit.tdxbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @DeleteMapping("/{categoryId}")
    public ResultInfo delete(@PathVariable("categoryId") Integer categoryId) {
        ResultInfo info = new ResultInfo();
        if (categoryId == null || categoryId <= 0) {
            info.setFlag(false);
            info.setErrorMsg("删除商品分类失败");
        } else {
            if (categoryService.deleteCategory(categoryId)) {
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg("删除商品分类失败");
            }
        }
        return info;
    }

    @GetMapping("/homePageCategory")
    public ResultInfo homePageCategory() {
        ResultInfo info = new ResultInfo();
        info.setData(categoryService.categoryAndProduct());
        if (info.getData() != null) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("获取首页分类失败");
        }
        return info;
    }

    @GetMapping("/listAllCategories")
    public ResultInfo listAllCategories() {
        ResultInfo info = new ResultInfo();
        info.setData(categoryService.listAllCategories());
        if (info.getData() != null) {
            info.setFlag(true);
        } else {
            info.setFlag(false);
            info.setErrorMsg("获取所有分类失败");
        }
        return info;
    }

    @PostMapping("/add")
    public ResultInfo add(String categoryName) {
        ResultInfo info = new ResultInfo();
        if (categoryName == null || categoryName.isEmpty()) {
            info.setFlag(false);
            info.setErrorMsg("添加商品分类失败");
        } else {
            info.setData(categoryService.addCategory(categoryName));
            if (info.getData() != null && (Integer) info.getData() != 0) {
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg((Integer) info.getData() == 0 ? "该分类已存在" : "添加商品分类失败");
            }
        }
        System.out.println(info.getData());
        return info;
    }

    @GetMapping("/searchCategoryProperty/{cid}")
    public ResultInfo searchCategoryProperty(@PathVariable("cid") Integer cid) {
        ResultInfo info = new ResultInfo();
        if (cid == null || cid <= 0) {
            info.setFlag(false);
            info.setErrorMsg("根据分类cid获取商品内容失败");
        } else {
            info.setData(categoryService.cidCategoryProperty(cid));
            if (info.getData() != null) {
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg("根据分类cid获取商品内容失败");
            }
        }
        return info;
    }
}