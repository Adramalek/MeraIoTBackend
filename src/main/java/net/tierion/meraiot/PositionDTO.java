package net.tierion.meraiot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class PositionDTO {
    private final double x;
    private final double y;
}
