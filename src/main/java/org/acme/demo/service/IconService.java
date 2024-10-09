package org.acme.demo.service;

import org.acme.demo.entity.Icon;
import org.acme.demo.repository.IconRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IconService {

    private  final IconRepository iconRepository;

    @Autowired
    public IconService(IconRepository iconRepository){
        this.iconRepository = iconRepository;
    }


    private final Map<String, String> predefinedCategoryIconMap = new HashMap<>(){{
        put("Groceries", "faBasketShopping");
        put("Household", "faHouse");
        put("Hobbies", "faPaperPlane");
        put("Uncategorized", "faCube");
    }};

    public List<Icon> getAllIcons(){
        return iconRepository.findAll();
    }

    public Icon getIconByName(String iconName){
        return iconRepository.findByIconName(iconName)
                .orElseThrow(()-> new IllegalArgumentException("Icon not found" + iconName));
    }

    public Icon getPredefinedIconForCategory(String categoryName){
        String iconName = predefinedCategoryIconMap.getOrDefault(categoryName, "faCube");
        return iconRepository.findByIconName(iconName)
                .orElseThrow(()-> new IllegalArgumentException("Icon not found" + iconName));
    }

    public void initializePredefinedIcons(){
        List<String> predefinedIcons = List.of("faBasketShopping", "faHouse", "faPaperPlane", "faCube");
        for (String iconName : predefinedIcons){
            if (iconRepository.findByIconName(iconName).isEmpty()){
                Icon icon = new Icon();
                icon.setIconName(iconName);
                iconRepository.save(icon);
            }
        }
    }

}
