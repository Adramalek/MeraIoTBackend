package net.tierion.meraiot;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BeaconService {

    public PositionDTO calculatePosition(List<BeaconDTO> data) {
        List<BeaconData> beaconData = data.stream()
                .map(beaconDTO -> {
                    BeaconData.BeaconDataBuilder beaconDataBuilder = BeaconData.builder();
                    beaconDataBuilder.number(beaconDTO.getNumber())
                            .x(beaconDTO.getX())
                            .y(beaconDTO.getY());
                    int rssiCalib = beaconDTO.getTxPowerLevel() - 41;
                    int distance = (int) (Math.pow(10, (rssiCalib - beaconDTO.getRssi())/30.0));
                    beaconDataBuilder.distance(distance * 100);
                    return beaconDataBuilder.build();
                })
                .sorted(Comparator.comparingInt(BeaconData::getDistance))
                .collect(Collectors.toList());
        return trilateration(beaconData);
    }

    private PositionDTO trilateration(List<BeaconData> data) {
        double[][] positions = data.stream()
                .map(beaconData -> new double[]{beaconData.getX(), beaconData.getY()})
                .toArray(double[][]::new);
        double[] distances = data.stream().mapToDouble(BeaconData::getDistance).toArray();
        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();
        double[] result = optimum.getPoint().toArray();
        return new PositionDTO(result[0], result[1]);
    }
}
