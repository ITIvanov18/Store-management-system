package org.nbu.controller;

import jakarta.servlet.http.HttpSession;
import org.nbu.data.*;
import org.nbu.exceptions.ClosedCashRegisterException;
import org.nbu.exceptions.InsufficientStockException;
import org.nbu.exceptions.NotEnoughFundsException;
import org.nbu.service.CashRegisterService;
import org.nbu.service.ProductService;
import org.nbu.service.ReceiptService;
import org.nbu.service.StoreService;
import org.nbu.util.ReceiptFileWriter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    private final StoreService storeService;
    private final ProductService productService;
    private final ReceiptService receiptService;
    private final CashRegisterService cashRegisterService;

    public PurchaseController(StoreService storeService,
                              ProductService productService,
                              ReceiptService receiptService,
                              CashRegisterService cashRegisterService) {
        this.storeService = storeService;
        this.productService = productService;
        this.receiptService = receiptService;
        this.cashRegisterService = cashRegisterService;
    }

    // Стъпка 1: Избор на магазин
    @GetMapping("/start")
    public String chooseStore(Model model) {
        List<Store> stores = storeService.findAll();
        model.addAttribute("stores", stores);
        return "choose-store";
    }

    // Стъпка 2: Избор на продукти
    @GetMapping("/store/{storeId}/choose-products")
    public String chooseProducts(@PathVariable int storeId,
                                 Model model,
                                 @ModelAttribute("error") String error) {
        Store store = storeService.findById(storeId);
        List<Product> products = productService.findByStoreId(storeId);
        Map<Integer, BigDecimal> productPrices = new HashMap<>();

        for (Product product : products) {
            BigDecimal price = productService.calculateSellingPrice(product, store.getStoreSettings());
            productPrices.put(product.getId(), price);
        }

        model.addAttribute("store", store);
        model.addAttribute("products", products);
        model.addAttribute("productPrices", productPrices);
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }

        return "choose-products";
    }

    // Стъпка 3: Валидиране и добавяне в количката (сесия)
    @PostMapping("/store/{storeId}/purchase")
    public String makePurchase(@PathVariable int storeId,
                               @RequestParam Map<String, String> quantities,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        Store store = storeService.findById(storeId);
        Map<Product, Integer> purchased = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : quantities.entrySet()) {
            String rawValue = entry.getValue();
            if (rawValue == null || rawValue.trim().isEmpty()) continue;

            int requested;
            try {
                requested = Integer.parseInt(rawValue.trim());
            } catch (NumberFormatException e) {
                continue;
            }

            if (requested <= 0) continue;

            int productId = Integer.parseInt(entry.getKey().replaceAll("[^0-9]", ""));
            Product product = productService.findById(productId);

            if (requested > product.getQuantity()) {
                redirectAttributes.addFlashAttribute("error",
                        new InsufficientStockException(product.getProductName(), requested, product.getQuantity()).getMessage());
                return "redirect:/purchase/store/" + storeId + "/choose-products";
            }

            purchased.put(product, requested);
        }

        session.setAttribute("purchased", purchased);
        session.setAttribute("storeId", storeId);

        return "redirect:/purchase/store/" + storeId + "/register";
    }

    // Стъпка 4: Избор на каса
    @GetMapping("/store/{storeId}/register")
    public String showRegisterSelection(@PathVariable int storeId, Model model) {
        Store store = storeService.findById(storeId);
        List<CashRegister> registers = cashRegisterService.findByStoreId(storeId);
        model.addAttribute("store", store);
        model.addAttribute("registers", registers);
        return "choose-register";
    }


    @PostMapping("/store/{storeId}/proceed")
    public String proceedToCheckout(@PathVariable int storeId,
                                    @RequestParam int cashRegisterId,
                                    HttpSession session) {
        CashRegister register = cashRegisterService.findById(cashRegisterId);
        if (register.getCashier() == null) {
            throw new ClosedCashRegisterException(cashRegisterId);
        }

        session.setAttribute("selectedRegisterId", cashRegisterId);
        return "redirect:/purchase/store/" + storeId + "/checkout";
    }

    // Стъпка 5: Checkout преди плащане

    @GetMapping("/store/{storeId}/checkout")
    public String checkoutPage(@PathVariable int storeId,
                               HttpSession session,
                               Model model) {

        Store store = storeService.findById(storeId);
        Map<Product, Integer> purchased = (Map<Product, Integer>) session.getAttribute("purchased");
        Integer selectedRegisterId = (Integer) session.getAttribute("selectedRegisterId");

        if (purchased == null || selectedRegisterId == null) {
            return "redirect:/purchase/store/" + storeId + "/choose-products";
        }

        CashRegister register = cashRegisterService.findById(selectedRegisterId);
        Cashier cashier = register.getCashier();

        Map<Integer, BigDecimal> productPrices = new HashMap<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Map.Entry<Product, Integer> entry : purchased.entrySet()) {
            Product product = entry.getKey();
            int qty = entry.getValue();
            BigDecimal price = productService.calculateSellingPrice(product, store.getStoreSettings());
            productPrices.put(product.getId(), price);
            totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(qty)));
        }

        model.addAttribute("store", store);
        model.addAttribute("register", register);
        model.addAttribute("cashier", cashier);
        model.addAttribute("purchased", purchased);
        model.addAttribute("productPrices", productPrices);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("dateTime", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return "checkout";
    }

    // Стъпка 5: Финализиране на покупката
    @PostMapping("/store/{storeId}/finalize")
    public String finalizePurchase(@PathVariable int storeId, HttpSession session, Model model) {
        Map<Product, Integer> purchased = (Map<Product, Integer>) session.getAttribute("purchased");
        Integer registerId = (Integer) session.getAttribute("selectedRegisterId");

        if (purchased == null || registerId == null) {
            return "redirect:/purchase/store/" + storeId + "/choose-products";
        }

        Store store = storeService.findById(storeId);
        CashRegister register = cashRegisterService.findById(registerId);
        Cashier cashier = register.getCashier();

        Map<Integer, BigDecimal> prices = new HashMap<>();
        double totalPrice = 0.0;

        for (Map.Entry<Product, Integer> entry : purchased.entrySet()) {
            Product product = entry.getKey();
            int boughtQty = entry.getValue();

            // Изваждане на количеството
            product.setQuantity(product.getQuantity() - boughtQty);
            productService.save(product);

            // Изчисление на цена
            BigDecimal price = productService.calculateSellingPrice(product, store.getStoreSettings());
            prices.put(product.getId(), price);
            totalPrice += price.multiply(BigDecimal.valueOf(boughtQty)).doubleValue();
        }

        Receipt fakeReceipt = new Receipt();
        fakeReceipt.setReceiptIssueDate(LocalDateTime.now());
        fakeReceipt.setCashier(cashier);
        fakeReceipt.setTotalAmount(totalPrice);

        ReceiptFileWriter.writeReceiptToFile(fakeReceipt, purchased, prices);

        model.addAttribute("store", store);
        model.addAttribute("cashier", cashier);
        model.addAttribute("register", register);
        model.addAttribute("purchased", purchased);
        model.addAttribute("productPrices", prices);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("showSuccess", true);

        return "checkout";
    }



    // Симулиране на грешка
    @PostMapping("/fail")
    @ResponseBody
    public void simulatePaymentFailure() {
        throw new NotEnoughFundsException();
    }

    // глобален ExceptionHandler
    @ControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(NotEnoughFundsException.class)
        public String handleNotEnoughFunds(NotEnoughFundsException ex, RedirectAttributes redirectAttributes) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/purchase/start";
        }
    }

    // Финална страница след успешно плащане
    @GetMapping("/success")
    public String successPage() {
        return "purchase-success";
    }
}
