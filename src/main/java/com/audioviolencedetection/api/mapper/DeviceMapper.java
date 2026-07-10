package com.audioviolencedetection.api.mapper;

import com.audioviolencedetection.api.dto.response.DeviceListResponse;
import com.audioviolencedetection.api.entity.Device;
import org.springframework.stereotype.Component;

@Component
public class DeviceMapper {

    public DeviceListResponse toDeviceListResponse(Device device) {
        return new DeviceListResponse(
                device.getId(),
                device.getMacAddress(),
                device.getName()
        );
    }
}
