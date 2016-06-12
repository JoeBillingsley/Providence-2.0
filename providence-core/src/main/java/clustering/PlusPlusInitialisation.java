package clustering;

import dataset.Project;
import javafx.util.Pair;
import utils.BoundedRandom;
import utils.Distance;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.stream.Collectors;

/**
 * Implementation of the initialisation algorithm from KMeans++ where the initial centers are placed so as to be as
 * spread out as possible.
 * <p>
 * Created by Joseph Billingsley on 22/03/2016.
 */
public class PlusPlusInitialisation implements IMeanInitialiser {

    @Override
    public List<Double[]> initialiseMeans(int k, List<Project> dataPoints) {

        // region Argument checks
        if (k > dataPoints.size())
            throw new IndexOutOfBoundsException("The number of centers requested is larger than the number of candidate points." +
                    " k: " + k +
                    " points: " + dataPoints.size());
        // endregion

        // First center is picked randomly out of data points
        int idx = BoundedRandom.nextInt(0, dataPoints.size() - 1);

        List<Double[]> centers = new ArrayList<>();

        Double[] initialCenter = dataPoints.get(idx).getData();
        centers.add(initialCenter);

        while (centers.size() < k) {
            List<Pair<Double[], Double>> distanceToCenters = dataPoints.stream()
                    .map(dataPoint -> distanceToNearestCenter(dataPoint.getData(), centers))
                    .collect(Collectors.toList());

            double totalDistance = distanceToCenters.stream().mapToDouble(Pair::getValue).sum();

            Double[] center = chooseCenterFromDistribution(distanceToCenters, totalDistance);
            centers.add(center);
        }

        return centers;
    }

    /**
     * Finds the distance from a point to it's nearest center.
     *
     * @param point   The point to find the distance to it's nearest center.
     * @param centers The list of centers to find the distance towards.
     * @return A pair of the point and the distance to it's nearest center
     */
    private Pair<Double[], Double> distanceToNearestCenter(Double[] point, List<Double[]> centers) {

        Double distanceToNearestCenter = Double.MAX_VALUE;

        for (Double[] center : centers) {
            Double distance = Distance.getEuclideanDistance(point, center);

            if (distance < distanceToNearestCenter) {
                distanceToNearestCenter = distance;
            }
        }

        return new Pair<>(point, distanceToNearestCenter);
    }

    private Double[] chooseCenterFromDistribution(List<Pair<Double[], Double>> distanceToCenters, Double totalDistance) {
        double target = BoundedRandom.nextDouble(0, 1);

        double sumProbability = 0;

        for (Pair<Double[], Double> distanceToCenter : distanceToCenters) {
            sumProbability += distanceToCenter.getValue() / totalDistance;

            if (sumProbability >= target)
                return distanceToCenter.getKey();
        }

        // This should never occur
        throw new Error();
    }
}
