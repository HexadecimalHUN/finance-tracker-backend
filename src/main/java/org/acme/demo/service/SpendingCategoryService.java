package org.acme.demo.service;

import jakarta.transaction.Transactional;
import org.acme.demo.dto.SpendingCategoryDTO;
import org.acme.demo.entity.AppUser;
import org.acme.demo.entity.Icon;
import org.acme.demo.entity.SpendingCategory;
import org.acme.demo.repository.SpendingCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SpendingCategoryService {


    private final SpendingCategoryRepository spendingCategoryRepository;
    private final IconService iconService;

    @Autowired
    public SpendingCategoryService(SpendingCategoryRepository spendingCategoryRepository, IconService iconService){
        this.spendingCategoryRepository = spendingCategoryRepository;
        this.iconService = iconService;

    }

    private final List<String> predefinedSpendingCategories = List.of("Groceries", "Household", "Hobbies", "Uncategorized");

    public List<SpendingCategory> getUserSpendingCategories(AppUser user){
        return  spendingCategoryRepository.findByUser(user);
    }

    public SpendingCategory addSpendingCategory(AppUser user, String spendingCategoryName, String iconName){
        if (spendingCategoryRepository.findByNameAndUser(spendingCategoryName, user) != null){
            throw new IllegalArgumentException("Category already exists for this user");
        }

        Icon icon = iconService.getIconByName(iconName);

        SpendingCategory newSpendingCategory = new SpendingCategory();
        newSpendingCategory.setName(spendingCategoryName);
        newSpendingCategory.setUser(user);
        newSpendingCategory.setIcon(icon);
        return spendingCategoryRepository.save(newSpendingCategory);
    };

    @Transactional
    public void AddPredefinedSpendingCategories(AppUser user){
        for(String spendingCategoryName : predefinedSpendingCategories){
            if (spendingCategoryRepository.findByNameAndUser(spendingCategoryName, user) == null){
                SpendingCategory spendingCategory = new SpendingCategory();
                spendingCategory.setName(spendingCategoryName);
                spendingCategory.setUser(user);
                Icon icon = iconService.getPredefinedIconForCategory(spendingCategoryName);
                spendingCategory.setIcon(icon);

                spendingCategoryRepository.save(spendingCategory);
            }
        }
    }

    //Update Category Name and Icon
    public SpendingCategory updateSpendingCategory(Long spendingCategoryId, SpendingCategoryDTO categoryDTO, AppUser user){
        SpendingCategory spendingCategory = spendingCategoryRepository.findById(spendingCategoryId)
                .orElseThrow(()-> new IllegalArgumentException("Category not found"));
        if(!spendingCategory.getUser().equals(user)){
            throw new IllegalArgumentException("Unauthorized to update this category");
        }
        spendingCategory.setName(categoryDTO.getName());

        Icon newIcon = iconService.getIconByName(categoryDTO.getIconName());
        spendingCategory.setIcon(newIcon);

        return spendingCategoryRepository.save(spendingCategory);
    }



    //Delete Category
    public void deleteSpendingCategory(Long spendingCategoryId, AppUser user){
        SpendingCategory spendingCategory = spendingCategoryRepository.findById(spendingCategoryId)
                .orElseThrow(()-> new IllegalArgumentException("Category not found"));
        if (!spendingCategory.getUser().equals(user)) {
            throw new IllegalArgumentException("Unauthorized to delete this category");
        }
        spendingCategoryRepository.delete(spendingCategory);
    }




}
