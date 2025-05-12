package org.nbu.controller;

import jakarta.validation.Valid;
import org.nbu.data.*;
import org.nbu.service.CashierService;
import org.nbu.service.ProductService;
import org.nbu.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final ProductService productService;
    private final CashierService cashierService;

    public StoreController(StoreService storeService, ProductService productService, CashierService cashierService) {
        this.storeService = storeService;
        this.productService = productService;
        this.cashierService = cashierService;
    }

    @GetMapping("/add")
    public String showCreateStoreForm(Model model) {
        Store store = new Store();
        store.setStoreSettings(new StoreSettings());
        model.addAttribute("store", store);
        return "store-create";
    }

    @PostMapping("/add")
    public String createStore(@Valid @ModelAttribute Store store, BindingResult bindingResult, @RequestParam("registerCount") int registerCount) {
        if (bindingResult.hasErrors()) {
            return "store-create";
        }
        List<CashRegister> registers = new ArrayList<>();
        for (int i = 0; i < registerCount; i++) {
            registers.add(new CashRegister());
        }

        store.setCashRegisters(registers);
        storeService.save(store);
        return "index";
    }

    @GetMapping("/list")
    public String listStores(Model model) {
        model.addAttribute("store", storeService.findAll());
        return "store-list";
    }

    @GetMapping("/{id}/details")
    public String viewStoreDetails(@PathVariable int id, Model model) {
        Store store = storeService.findById(id);

        List<Product> products = productService.findByStoreId(id);
        List<Cashier> availableCashiers = cashierService.findByStoreIdWhenCashRegisterIsNull(id);

        model.addAttribute("store", store);
        model.addAttribute("products", products);
        model.addAttribute("availableCashiers", availableCashiers);

        return "store-details";
    }

    @GetMapping("/{id}")
    public String showStore(@PathVariable int id, Model model) {
        Store store = storeService.findById(id);
        List<Product> products = productService.findByStoreId(id);
        List<Cashier> availableCashiers = cashierService.findByStoreIdWhenCashRegisterIsNull(id);
        StoreSettings settings = store.getStoreSettings();

        Map<Product, BigDecimal> productPrices = new LinkedHashMap<>();
        for (Product product : products) {
            BigDecimal sellingPrice = productService.calculateSellingPrice(product, settings);
            productPrices.put(product, sellingPrice);
        }

        model.addAttribute("store", store);
        model.addAttribute("productPrices", productPrices);
        model.addAttribute("products", products);
        model.addAttribute("availableCashiers", availableCashiers);

        return "store";
    }
}
