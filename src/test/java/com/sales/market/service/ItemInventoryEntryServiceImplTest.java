package com.sales.market.service;

import com.sales.market.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemInventoryEntryServiceImplTest {

    public static final long ID_MALTIN_ITEM = 1L;
    public static final long ID_COCACOLA_ITEM = 2L;
    @Autowired
    ItemInventoryEntryServiceImpl itemInventoryEntryService;

    @Autowired
    ItemInventoryServiceImpl itemInventoryService;

    @Autowired
    ItemInstanceServiceImpl itemInstanceService;

    @Autowired
    ItemServiceImpl itemService;

    @Test
    public void GivenRegisteredOperationItemInventoryEntryBUYWith4CocaColaItemInstancesWithCost7 () {
        Item cocaColaItem = itemService.findById(ID_COCACOLA_ITEM);
        List<ItemInstance> itemInstances = createItemInstances(cocaColaItem, new BigDecimal("7"));
        ItemInventoryEntry itemInventoryEntry = itemInventoryEntryService.registerOperationItemInventoryEntry(itemInstances, MovementType.BUY);
        ItemInventory itemInventory = itemInventoryService.findItemInventoryByItemId(ID_COCACOLA_ITEM);
        assertTrue(new BigDecimal("-28").compareTo(itemInventory.getTotalPrice()) == 0);
        assertTrue(itemInventoryEntry.getQuantity().compareTo(new BigDecimal("4")) == 0);
    }

    @Test
    public void GivenRegisteredOperationItemInventoryEntrySALE2CocaColaItemInstancesWithPriceOfSale10 () {
        ArrayList<ItemInstance> itemInstances = new ArrayList<>();
        ItemInstance itemCocacola1 = itemInstanceService.findById(5L);
        ItemInstance itemCocacola2 = itemInstanceService.findById(6L);
        itemInstances.add(itemCocacola1);
        itemInstances.add(itemCocacola2);
        ItemInventoryEntry itemInventoryEntry = itemInventoryEntryService.registerOperationItemInventoryEntry(itemInstances, MovementType.SALE);
        ItemInventory itemInventory = itemInventoryService.findItemInventoryByItemId(ID_COCACOLA_ITEM);
        ItemInstance itemCocacola1Updated = itemInstanceService.findById(5L);
        ItemInstance itemCocacola2Updated = itemInstanceService.findById(6L);
        assertTrue(new BigDecimal("-8.00").compareTo(itemInventory.getTotalPrice()) == 0);
        assertTrue(new BigDecimal("2").compareTo(itemInventoryEntry.getQuantity()) == 0);
        assertTrue(new BigDecimal("2").compareTo(itemInventory.getStockQuantity()) == 0);
        assertEquals(ItemInstanceStatus.SOLD, itemCocacola1Updated.getItemInstanceStatus());
        assertEquals(ItemInstanceStatus.SOLD, itemCocacola2Updated.getItemInstanceStatus());
    }

    @Test
    public void GivenRegisteredOperationItemInventoryEntryREMOVE1MaltinItemInstance () {
        ItemInstance itemMaltin3 = itemInstanceService.findById(3L);
        ItemInventoryEntry itemInventoryEntry = itemInventoryEntryService.registerOperationItemInventoryEntry(
                Arrays.asList(itemMaltin3), MovementType.REMOVED);
        ItemInventory itemInventory = itemInventoryService.findItemInventoryByItemId(ID_MALTIN_ITEM);
        ItemInstance itemMaltin3Updated = itemInstanceService.findById(3L);
        assertTrue(new BigDecimal("-20").compareTo(itemInventory.getTotalPrice()) == 0);
        assertTrue(new BigDecimal("3").compareTo(itemInventory.getStockQuantity()) == 0);
        assertTrue(new BigDecimal("1").compareTo(itemInventoryEntry.getQuantity()) == 0);
        assertEquals(ItemInstanceStatus.SCREWED, itemMaltin3Updated.getItemInstanceStatus());
    }

    @Test
    public void GivenListOfItemInstancesWithRepeatedSkusWhenRegisteringOperationItemInventoryEntryShouldFailAndShowDuplicatedExceptionErrorMessage () {
        Item maltinItem = itemService.findById(ID_MALTIN_ITEM);
        BigDecimal cost = new BigDecimal("5");
        List<ItemInstance> list = createItemInstances(maltinItem, cost);
        ArrayList<ItemInstance> itemInstances = new ArrayList<>();
        list.forEach(item -> itemInstances.add(item));
        ItemInstance maltinItem5 = createItemInstance(maltinItem, maltinItem.getCode() + "-77721106006202", cost);
        itemInstances.add(maltinItem5);
        try {
            itemInventoryEntryService.registerOperationItemInventoryEntry(itemInstances, MovementType.BUY);
        } catch (DuplicateKeyException e) {
            assertEquals("El item con el identificador " + itemInstances.get(4).getIdentifier() + " ya fue ingresado.\n La operación fue cancelada, revise los datos de los items registrados y vuelva a ingresarlos",
                    e.getMessage());
        }
    }

    @Test
    public void GivenListOfItemInstancesWithRepeatedSkusInTheDatabaseWhenRegisteringOperationItemInventoryEntryBUYShouldFailAndShowDuplicatedExceptionErrorMessage () {
        ItemInstance maltinItem1 = itemInstanceService.findById(1L);
        ItemInstance maltinItem2 = itemInstanceService.findById(2L);
        ItemInstance maltinItem3 = itemInstanceService.findById(3L);
        try {
            itemInventoryEntryService.registerOperationItemInventoryEntry(
                    Arrays.asList(maltinItem1, maltinItem2, maltinItem3), MovementType.BUY);
        } catch (DuplicateKeyException e) {
            assertEquals("Hay items duplicados en la lista de items ingresada para la compra",
                    e.getMessage());
        }
    }

    private List<ItemInstance> createItemInstances(Item maltinItem, BigDecimal cost) {
        ItemInstance maltinItem1 = createItemInstance(maltinItem, maltinItem.getCode() + "-77721106006201", cost);
        ItemInstance maltinItem2 = createItemInstance(maltinItem, maltinItem.getCode() + "-77721106006202", cost);
        ItemInstance maltinItem3 = createItemInstance(maltinItem, maltinItem.getCode()+"-77721106006203", cost);
        ItemInstance maltinItem4 = createItemInstance(maltinItem, maltinItem.getCode()+"-77721106006204", cost);
        return Arrays.asList(maltinItem1, maltinItem2, maltinItem3, maltinItem4);
    }

    private ItemInstance createItemInstance(Item maltinItem, String sku, BigDecimal price) {
        ItemInstance itemInstance = new ItemInstance();
        itemInstance.setItem(maltinItem);
        itemInstance.setFeatured(true);
        itemInstance.setPriceBuy(price);
        itemInstance.setPriceSale(price.add(new BigDecimal("3")));
        itemInstance.setIdentifier(sku);
        itemInstance.setItemInstanceStatus(ItemInstanceStatus.AVAILABLE);
        return itemInstance;
    }

}