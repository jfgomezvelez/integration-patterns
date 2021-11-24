package co.edu.eafit.mongo;

import co.edu.eafit.model.statistic.gateway.StatisticRepository;
import co.edu.eafit.model.statistic.Process;
import co.edu.eafit.mongo.helper.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Process, ProcessData, String, MongoDBRepository> implements StatisticRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.mapBuilder(d, Process.ProcessBuilder.class).build());
    }
}
