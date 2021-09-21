/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.service;

import com.sales.market.model.*;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInstanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemInstanceServiceImpl extends GenericServiceImpl<ItemInstance> implements ItemInstanceService {
    private final ItemInstanceRepository repository;
    private final ItemService itemService;
    private final ItemInventoryService itemInventoryService;
    private final MailService mailService;

    public ItemInstanceServiceImpl(ItemInstanceRepository repository, ItemService itemService, ItemInventoryService itemInventoryService, MailService mailService) {
        this.repository = repository;
        this.itemService = itemService;
        this.itemInventoryService = itemInventoryService;
        this.mailService = mailService;
    }

    @Override
    protected GenericRepository<ItemInstance> getRepository() {
        return repository;
    }

    @Override
    public ItemInstance bunchSave(ItemInstance itemInstance) {
        // here make all objects save other than this resource
        if (itemInstance.getItem() != null) {
            // todo habria que distinguir si permitiremos guardar y  actualizar o ambos mitando el campo id
            itemService.save(itemInstance.getItem());
        }
        return super.bunchSave(itemInstance);
    }



    @Override
    public ItemInstance saveItemInstance(ItemInstance itemInstance, MovementType movementType) {
        Item item = itemService.findById(itemInstance.getItem().getId());
        if (item == null) {
            item = itemService.save(itemInstance.getItem());
        }
        ItemInventory itemInventory = itemInventoryService.findItemInventoryByItemId(item.getId());
        if (itemInventory == null) {
            itemInventory = itemInventoryService.saveInitialItemInventory(item);
        }
        if (movementType == MovementType.BUY) {
            itemInventory.setStockQuantity(itemInventory.getStockQuantity().add(BigDecimal.ONE));
            itemInventory.setTotalPrice(itemInventory.getTotalPrice().subtract(itemInstance.getPriceBuy()));
        } else if (movementType == MovementType.SALE) {
            itemInventory.setStockQuantity(itemInventory.getStockQuantity().subtract(BigDecimal.ONE));
            itemInventory.setTotalPrice(itemInventory.getTotalPrice().add(itemInstance.getPriceSale()));
            itemInstance.setItemInstanceStatus(ItemInstanceStatus.SOLD);
        } else if (movementType == MovementType.REMOVED) {
            itemInventory.setStockQuantity(itemInventory.getStockQuantity().subtract(BigDecimal.ONE));
            itemInstance.setPriceSale(BigDecimal.ZERO);
            itemInstance.setItemInstanceStatus(ItemInstanceStatus.SCREWED);
        }
        ItemInventory itemInventoryUpdated = itemInventoryService.save(itemInventory);
        if (itemInventoryUpdated.getStockQuantity().compareTo(itemInventoryUpdated.getLowerBoundThreshold().add(BigDecimal.ONE)) == -1) {
            notifySupervisorStockLowerThanMininumAllowed(item.getName());
        }
        return super.bunchSave(itemInstance);
    }

    @Override
    public boolean verifyItemsToBeRegisteredAreNotDuplicated(List<String> itemInstances) {
        List<String> skusRegistered = repository.getItemInstancesSkus();
        for (String itemSku : itemInstances) {
            if (skusRegistered.contains(itemSku)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean notifySupervisorStockLowerThanMininumAllowed(String itemName) {
        return mailService.sendMessageForItemInventoryLowerBoundThreshold(itemName);
    }
}
