package org.nbu.controller;

import org.nbu.data.CashRegister;
import org.nbu.data.Cashier;
import org.nbu.service.CashRegisterService;
import org.nbu.service.CashierService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/cashRegister")
public class CashRegisterController {

    private final CashRegisterService registerService;
    private final CashierService cashierService;

    public CashRegisterController(CashRegisterService registerService, CashierService cashierService) {
        this.registerService = registerService;
        this.cashierService = cashierService;
    }

    // POST заявка за присвояване на касиер към каса чрез id
    @PostMapping("/{id}/assign")
    public String assignCashier(@PathVariable int id, @RequestParam int cashierId) {
        CashRegister register = registerService.findById(id);
        Cashier cashier = cashierService.findById(cashierId);

        register.setCashier(cashier);
        registerService.save(register);

        return "redirect:/store/" + register.getStore().getId();
    }

    // POST заявка за премахване на касиера от каса чрез id
    @PostMapping("/{id}/unassign")
    public String unassignCashier(@PathVariable int id) {
        CashRegister register = registerService.findById(id);
        register.setCashier(null);
        registerService.save(register);

        return "redirect:/store/" + register.getStore().getId();
    }
}

