package com.sarc.resources.dto;

import com.sarc.domain.ResourceType;

public class ResourceDTO {
    private String name;
    private ResourceType type;
    private Integer capacity;
    private String localization;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ResourceType getType() { return type; }
    public void setType(ResourceType type) { this.type = type; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getLocalization() { return localization; }
    public void setLocalization(String localization) { this.localization = localization; }
}
