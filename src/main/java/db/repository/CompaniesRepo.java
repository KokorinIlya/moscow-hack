//package db.repository;
//
//
//import db.entity.Companies;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
////@RepositoryRestResource(collectionResourceRel = "news", path = "news", excerptProjection = NewsForFeedProjection.class)
//@Repository
//public interface CompaniesRepo extends JpaRepository<Companies, Long> {
//	//@RestResource(exported = false)
//	@Query("select n from News n, Subscription s where n.author = s.onWhom and s.who.id = :userId")
//	List<Companies> getAllForUserId(@Param("userId") long userId, Pageable pagination);
//
//	//@RestResource(exported = false)
//	@Query("select n from News n where n.author.id = :userId")
//	List<Companies> getAllByUserId(@Param("userId") long userId, Pageable pagination);
//
//	//@RestResource(exported = false)
//	@Query("select n from News n where n.id = :newsId")
//	Companies getOneByNewsId(@Param("newsId") long newsId);
//
//	void removeById(Long id);
//
////	@Query(value = "SELECT u.id AS id, u.login AS authorLogin, n.content_preview AS contentPreview, " +
////			"count(lop) AS loopsNumber, count(pop) AS poopsNumber, count(com) AS commentsNumber, " +
////			"count(rep) AS repostsNumber, n.creation_date AS creationDate, n.altering_date AS " +
////			"alteringDate " +
////			"FROM public.news n, public.news_loop lop, public.news_poop pop, public.comment com , " +
////			"public.repost rep, public.user u " +
////			"WHERE u.id = n.id_user AND lop.id_news = n.id AND pop.id_news = n.id AND " +
////			"com.id_news = n.id AND n.id IN ( " +
////			"SELECT rep.id_news " +
////			"FROM public.repost rep, public.subscription sub " +
////			"WHERE rep.id_user = sub.id_on_whom " +
////			")" +
////			"GROUP BY u.id, u.login, n.content_preview, n.creation_date, n.altering_date " +
////			"ORDER BY n.altering_date", nativeQuery = true)
////	List<Object[]> findAllNewsPreviews();
//
//	//@Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
//	//  User findByLastnameOrFirstname(@Param("lastname") String lastname,
//	//                                 @Param("firstname") String firstname);
//}
