package db.repository;


import db.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompaniesRepo extends JpaRepository<Company, Long> {
//	@Query("select n from News n, Subscription s where n.author = s.onWhom and s.who.id = :userId")
//	List<Company> getAllForUserId(@Param("userId") long userId, Pageable pagination);
//
//	@Query("select n from News n where n.author.id = :userId")
//	List<Company> getAllByUserId(@Param("userId") long userId, Pageable pagination);
//
//	@Query("select n from News n where n.id = :newsId")
//	Company getOneByNewsId(@Param("newsId") long newsId);
//
//	void removeById(Long id);
}
