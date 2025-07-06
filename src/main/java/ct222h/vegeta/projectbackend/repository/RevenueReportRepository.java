package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.RevenueReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RevenueReportRepository extends MongoRepository<RevenueReport, String> {
    List<RevenueReport> findByReportType(String reportType);
    List<RevenueReport> findByReportTypeOrderByPeriodStartDesc(String reportType);
    
    @Query("{'reportType': ?0, 'periodStart': {$gte: ?1, $lte: ?2}}")
    List<RevenueReport> findByReportTypeAndPeriodRange(String reportType, Date startDate, Date endDate);
    
    @Query("{'periodStart': {$gte: ?0, $lte: ?1}}")
    List<RevenueReport> findByPeriodRange(Date startDate, Date endDate);
    
    @Query(value = "{}", sort = "{'periodStart': -1}")
    List<RevenueReport> findAllOrderByPeriodStartDesc();
}
