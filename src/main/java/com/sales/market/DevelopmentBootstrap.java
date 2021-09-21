/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market;

import com.sales.market.model.*;
import com.sales.market.repository.BuyRepository;
import com.sales.market.service.*;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
public class DevelopmentBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final BuyRepository buyRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ItemService itemService;
    private final ItemInventoryEntryService itemInventoryEntryService;

    SubCategory beverageSubCat = null;

    public DevelopmentBootstrap(BuyRepository buyRepository, CategoryService categoryService,
                                SubCategoryService subCategoryService, ItemService itemService, ItemInventoryEntryService itemInventoryEntryService) {
        this.buyRepository = buyRepository;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.itemService = itemService;
        this.itemInventoryEntryService = itemInventoryEntryService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("evento de spring");
        persistBuy(BigDecimal.TEN);
        persistBuy(BigDecimal.ONE);
        persistCategoriesAndSubCategories();
        Item maltinItem = persistItems(beverageSubCat, "MALTIN");
        persistItems(beverageSubCat, "COCACOLA");
        persistItemInstances(maltinItem);
    }

    private void persistItemInstances(Item maltinItem) {
        BigDecimal priceMaltin = new BigDecimal("5");
        ItemInstance maltinItem1 = createItem(maltinItem, "SKU-77721106006158", priceMaltin);
        ItemInstance maltinItem2 = createItem(maltinItem, "SKU-77721106006159", priceMaltin);
        ItemInstance maltinItem3 = createItem(maltinItem, "SKU-77721106006160", priceMaltin);
        ItemInstance maltinItem4 = createItem(maltinItem, "SKU-77721106006161", priceMaltin);
        itemInventoryEntryService.registerOperationItemInventoryEntry(
                Arrays.asList(maltinItem1, maltinItem2, maltinItem3, maltinItem4), MovementType.BUY);
    }

    private ItemInstance createItem(Item maltinItem, String sku, BigDecimal price) {
        ItemInstance itemInstance = new ItemInstance();
        itemInstance.setItem(maltinItem);
        itemInstance.setFeatured(true);
        itemInstance.setPriceBuy(price);
        itemInstance.setIdentifier(sku);
        itemInstance.setItemInstanceStatus(ItemInstanceStatus.AVAILABLE);
        return itemInstance;
    }

    private Item persistItems(SubCategory subCategory, String name) {
        Item item = new Item();
        item.setCode("B-"+name);
        item.setName(name);
        item.setSubCategory(subCategory);
        return itemService.save(item);
    }

    private void persistCategoriesAndSubCategories() {
        Category category = persistCategory();
        persistSubCategory("SUBCAT1-NAME", "SUBCAT1-CODE", category);
        beverageSubCat = persistSubCategory("BEVERAGE", "BEVERAGE-CODE", category);
    }

    private Category persistCategory() {
        Category category = new Category();
        category.setName("CAT1-NAME");
        category.setCode("CAT1-CODE");
        return categoryService.save(category);
    }

    private SubCategory persistSubCategory(String name, String code, Category category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCode(code);
        subCategory.setCategory(category);
        return subCategoryService.save(subCategory);
    }

    private void persistBuy(BigDecimal value) {
        Buy buy = new Buy();
        buy.setValue(value);
        buyRepository.save(buy);
    }
}
