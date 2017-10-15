package com.turbo;

import com.turbo.model.DeviceType;

import java.util.List;
import java.util.Set;

public class TransferPost {

    private Long id;
    private String name;
    private List<Long> imageIds; // value is description
    private DeviceType deviceType;
    private Set<String> tags;
    private boolean visible;
    private String description;
}
