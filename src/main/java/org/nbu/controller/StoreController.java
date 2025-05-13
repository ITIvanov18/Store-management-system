package org.nbu.controller;

import jakarta.validation.Valid;
import org.nbu.data.*;
import org.nbu.service.CashierService;
import org.nbu.service.ProductService;
import org.nbu.service.ReceiptService;
import org.nbu.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    private final ProductService productService;
    private final CashierService cashierService;
    private final ReceiptService receiptService;

    public StoreController(StoreService storeService,
                           ProductService productService,
                           CashierService cashierService,
                           ReceiptService receiptService) {
        this.storeService = storeService;
        this.productService = productService;
        this.cashierService = cashierService;
        this.receiptService = receiptService;
    }

    // Форма за създаване на нов магазин
    @GetMapping("/add")
    public String showCreateStoreForm(Model model) {
        Store store = new Store();
        store.setStoreSettings(new StoreSettings());
        model.addAttribute("store", store);
        return "store-create";
    }

    // Създаване на магазин + празни каси
    @PostMapping("/add")
    public String createStore(@Valid @ModelAttribute Store store,
                              BindingResult bindingResult,
                              @RequestParam("registerCount") int registerCount) {
        if (bindingResult.hasErrors()) {
            return "store-create";
        }

        List<CashRegister> registers = new ArrayList<>();
        for (int i = 0; i < registerCount; i++) {
            registers.add(new CashRegister());
        }

        store.setCashRegisters(registers);
        storeService.save(store);
        return "redirect:/store/list";
    }

    // Списък с магазини
    @GetMapping("/list")
    public String listStores(Model model) {
        model.addAttribute("store", storeService.findAll());
        return "store-list";
    }

    // Главна витрина на магазина
    @GetMapping("/{id}")
    public String showStore(@PathVariable int id,
                            Model model,
                            @ModelAttribute("error") String errorMessage) {
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
        model.addAttribute("product", new Product());
        model.addAttribute("cashier", new Cashier());
        model.addAttribute("categories", ProductCategoryEnum.values());

        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("error", errorMessage);
        }

        return "store";
    }

    // Статистика за конкретен магазин
    @GetMapping("/{id}/stats")
    public String showStoreStats(@PathVariable int id, Model model) {
        Store store = storeService.findById(id);
        double turnover = receiptService.getTotalTurnoverByStoreId(id);
        long receiptCount = receiptService.countReceiptsByStoreId(id);

        model.addAttribute("store", store);
        model.addAttribute("turnover", turnover);
        model.addAttribute("receiptCount", receiptCount);

        return "store-stats";
    }
}
