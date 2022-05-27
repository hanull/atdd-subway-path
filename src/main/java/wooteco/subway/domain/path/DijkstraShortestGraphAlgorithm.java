package wooteco.subway.domain.path;

import java.util.HashSet;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

public class DijkstraShortestGraphAlgorithm implements ShortestPathAlgorithm<Station, ShortestPathEdge> {

    private final DijkstraShortestPath<Station, ShortestPathEdge> shortestPath;

    private DijkstraShortestGraphAlgorithm(final DijkstraShortestPath<Station, ShortestPathEdge> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public static DijkstraShortestGraphAlgorithm of(final Sections sections) {
        final WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(
                ShortestPathEdge.class);
        for (Station station : findAllStationByDistinct(sections)) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
        return new DijkstraShortestGraphAlgorithm(new DijkstraShortestPath<>(graph));
    }

    private static Set<Station> findAllStationByDistinct(final Sections sections) {
        Set<Station> stations = new HashSet<>();
        stations.addAll(sections.findUpStations());
        stations.addAll(sections.findDownStations());
        return stations;
    }

    @Override
    public GraphPath<Station, ShortestPathEdge> getPath(final Station source, final Station sink) {
        return this.shortestPath.getPath(source, sink);
    }

    @Override
    public double getPathWeight(final Station source, final Station sink) {
        return this.shortestPath.getPathWeight(source, sink);
    }

    @Override
    public SingleSourcePaths<Station, ShortestPathEdge> getPaths(final Station source) {
        return this.shortestPath.getPaths(source);
    }
}
