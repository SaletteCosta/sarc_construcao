package com.user.service;

import com.user.dto.ItemDTO;
import com.user.entity.Item;
import com.user.enums.ItemType;
import com.user.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return convertToDTO(item);
    }

    @Transactional(readOnly = true)
    public ItemDTO getItemByCode(String code) {
        Item item = itemRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return convertToDTO(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByType(String type) {
        ItemType itemType = ItemType.valueOf(type.toUpperCase());
        return itemRepository.findByType(itemType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getAvailableItems() {
        return itemRepository.findByAvailable(true).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        if (itemRepository.existsByCode(itemDTO.getCode())) {
            throw new RuntimeException("Item code already exists");
        }

        Item item = new Item();
        item.setCode(itemDTO.getCode());
        item.setType(ItemType.valueOf(itemDTO.getType().toUpperCase()));
        item.setName(itemDTO.getName());
        item.setAvailable(itemDTO.getAvailable() != null ? itemDTO.getAvailable() : true);

        Item saved = itemRepository.save(item);
        return convertToDTO(saved);
    }

    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (itemDTO.getName() != null) {
            item.setName(itemDTO.getName());
        }
        if (itemDTO.getType() != null) {
            item.setType(ItemType.valueOf(itemDTO.getType().toUpperCase()));
        }
        if (itemDTO.getAvailable() != null) {
            item.setAvailable(itemDTO.getAvailable());
        }

        Item updated = itemRepository.save(item);
        return convertToDTO(updated);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found");
        }
        itemRepository.deleteById(id);
    }

    private ItemDTO convertToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setCode(item.getCode());
        dto.setType(item.getType().name());
        dto.setName(item.getName());
        dto.setAvailable(item.getAvailable());
        return dto;
    }
}
