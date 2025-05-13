package org.nbu.controller;

import org.nbu.data.Store;
import org.nbu.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    private final StoreService storeService;

    public PurchaseController(StoreService storeService) {
        this.storeService = storeService;
    }

    // Показва списък с магазини, визуализирани като балончета
    @GetMapping("/start")
    public String chooseStore(Model model) {
        List<Store> stores = storeService.findAll();
        model.addAttribute("stores", stores);
        return "choose-store";
    }
}
