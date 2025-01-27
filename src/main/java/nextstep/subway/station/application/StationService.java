package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    @CacheEvict(value = "station", allEntries = true)
    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Cacheable(value = "station", key =  "#pageable")
    public List<StationResponse> findAllStationResponses(Pageable pageable) {
        Page<Station> stations = findAllStations(pageable);
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public Page<Station> findAllStations(Pageable pageable) {
        return stationRepository.findAll(pageable);
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = "station", allEntries = true)
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
