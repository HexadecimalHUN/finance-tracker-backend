package org.acme.demo.controller;

import org.acme.demo.dto.TransactionDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.entity.Transaction;
import org.acme.demo.repository.TransactionRepository;
import org.acme.demo.service.SpendingCategoryService;
import org.acme.demo.service.TransactionService;
import org.acme.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
@Validated
public class TransactionController {

    private final TransactionService transactionService;
    private final SpendingCategoryService spendingCategoryService;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public  TransactionController(TransactionService transactionService, UserService userService, SpendingCategoryService spendingCategoryService, TransactionRepository transactionRepository){
        this.transactionService = transactionService;
        this.spendingCategoryService = spendingCategoryService;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    //Get All transactions by category for authenticated users
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategory(Principal principal, @PathVariable Long categoryId){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        SpendingCategory category = spendingCategoryService.getUserSpendingCategories(user)
                .stream()
                .filter(sc -> sc.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(()-> new IllegalArgumentException("Category not found or unauthorized"));
        List<TransactionDTO> transactions = transactionService.getTransactionsBySpendingCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactions);
    }

    //Get transactions by category and date range
    @GetMapping("/category/{categoryId}/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByCategoryAndDateRange(Principal principal, @PathVariable Long categoryId, @RequestParam String startDate, @RequestParam String endDate){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        SpendingCategory category = spendingCategoryService.getUserSpendingCategories(user)
                .stream()
                .filter(sc -> sc.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found or unauthorized"));
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<TransactionDTO> transactions = transactionService.getTransactionsBySpendingCategoryAndDate(category, start, end)
                .stream()
                .map(this:: convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactions);
    }

    //Get all transactions by date range for an authenticated user
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByDateRange(Principal principal, @RequestParam String startDate, @RequestParam String endDate){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<TransactionDTO> transactions = transactionService.getTransactionByUserAndDateRange(user, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return  ResponseEntity.ok(transactions);
    }

    //Create Transaction
    @PostMapping ("/category/{categoryId}")
    public ResponseEntity<TransactionDTO> addTransaction(Principal principal, @PathVariable Long categoryId, @RequestBody TransactionDTO transactionDTO){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        SpendingCategory category = spendingCategoryService.getUserSpendingCategories(user)
                .stream()
                .filter(sc -> sc.getId().equals(categoryId))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("Category not found or unauthorized"));
        Transaction newTransaction = transactionService.addTransaction(category,
                transactionDTO.getDescription(),
                transactionDTO.getTransactionDate(),
                transactionDTO.getAmount());
        return ResponseEntity.ok(convertToDTO(newTransaction));
    }

    //Update Transaction
    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransaction(Principal principal, @PathVariable Long transactionId, @RequestBody TransactionDTO transactionDTO){
        AppUser user = userService.findUserByUsername(principal.getName());
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Transaction updateTransaction = transactionService.updateTransaction(transactionId,
                transactionDTO.getDescription(),
                transactionDTO.getTransactionDate(),
                transactionDTO.getAmount());
        return ResponseEntity.ok(convertToDTO(updateTransaction));
    }

    //Delete Transaction
    public void deleteTransaction(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(()-> new IllegalArgumentException("Transaction not found"));
        transactionRepository.delete(transaction);
    }



    private TransactionDTO convertToDTO(Transaction transaction){
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setTransactionDate(transaction.getDate());
        dto.setCategoryId(transaction.getSpendingCategory().getId());
        return dto;
    }


}
