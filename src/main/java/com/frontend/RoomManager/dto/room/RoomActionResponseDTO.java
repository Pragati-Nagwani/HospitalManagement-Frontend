package com.frontend.RoomManager.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomActionResponseDTO {
    private Boolean success;
    private String message;
}
