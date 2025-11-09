package hospital.emr.common.services;

import hospital.emr.common.entities.SimpleRequest;
import hospital.emr.common.repos.SimpleRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SimpleRequestService {
    private SimpleRequestRepository simpleRequestRepository;


}
