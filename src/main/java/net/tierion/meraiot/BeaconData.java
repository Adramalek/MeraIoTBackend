package net.tierion.meraiot;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class BeaconData {
    private final int number;
    private final int distance;
    private final int x;
    private final int y;
}
