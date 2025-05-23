package org.nbu.controller;

import jakarta.validation.Valid;
import org.nbu.data.Product;
import org.nbu.data.ProductCategoryEnum;
import org.nbu.data.Store;
import org.nbu.exceptions.InvalidProductCategoryException;
import org.nbu.service.ProductService;
import org.nbu.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/store/{storeId}/products")
public class ProductController {

    private final StoreService storeService;
    private final ProductService productService;

    public ProductController(StoreService storeService, ProductService productService) {
        this.storeService = storeService;
        this.productService = productService;
    }

    @GetMapping("/add")
    public String showAddProductForm(@PathVariable int storeId, Model model) {
        Store store = storeService.findById(storeId);
        Product product = new Product();
        product.setStore(store);
        model.addAttribute("product", product);
        model.addAttribute("categories", ProductCategoryEnum.values());
        model.addAttribute("store", store);
        return "product-add";
    }

    @PostMapping("/add")
    public String handleAddProductForm(@ModelAttribute("product") @Valid Product product,
                                       BindingResult bindingResult,
                                       @PathVariable int storeId,
                                       Model model,
                                       RedirectAttributes redirectAttributes) {

        Store store = storeService.findById(storeId);
        product.setStore(store);

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", ProductCategoryEnum.values());
            model.addAttribute("store", store);
            return "product-add";
        }

        try {
            productService.save(product);
        } catch (InvalidProductCategoryException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/store/" + storeId;
    }

    @GetMapping("/list")
    public String listProducts(Model model, @PathVariable String storeId) {
        model.addAttribute("products", productService.findAll());
        return "product-list";
    }
}
