package org.nbu.controller;

import jakarta.validation.Valid;
import org.nbu.data.Cashier;
import org.nbu.data.Store;
import org.nbu.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/store/{storeId}/cashiers")
public class CashierController {

    private final StoreService storeService;

    public CashierController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/add")
    public String showAddCashierForm(@PathVariable int storeId, Model model) {
        Store store = storeService.findById(storeId);
        model.addAttribute("store", store);
        model.addAttribute("cashier", new Cashier());
        return "cashier-add";
    }

    @PostMapping("/add")
    public String addCashier(@PathVariable int storeId,
                             @ModelAttribute("cashier") @Valid Cashier cashier,
                             BindingResult result,
                             Model model) {
        Store store = storeService.findById(storeId);

        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return "cashier-add";
        }

        store.getCashiers().add(cashier);
        storeService.save(store);
        return "redirect:/store/" + storeId;
    }
}
