package db.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.cs.iad.iadcurseproject.dto.*;
import ru.ifmo.cs.iad.iadcurseproject.entity.*;
import ru.ifmo.cs.iad.iadcurseproject.repository.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.data.domain.Sort.by;

@RepositoryRestController
@RequestMapping("/user")
public class UserController {
	private final UserRepo userRepo;
	private final SubscriptionRepo subscriptionRepo;
	private final CommentRepo commentRepo;
	//	private final RepostRepo repostRepo;
	private final NewsRepo newsRepo;
	private final NewsLoopRepo newsLoopRepo;
	private final NewsPoopRepo newsPoopRepo;
	//	private final PerformerRepo performerRepo;
//	private final RepostPoopRepo repostPoopRepo;
	private final CourseRepo courseRepo;
	private final CourseSubscriptionRepo courseSubscriptionRepo;

	@PersistenceContext
	private EntityManager em; // TODO: IS NULL

	private Logger logger = LoggerFactory.getLogger("UserController");

	@Autowired
	public UserController(UserRepo userRepo, CommentRepo commentRepo, /*RepostRepo repostRepo,*/ NewsRepo newsRepo,
	                      NewsLoopRepo newsLoopRepo, NewsPoopRepo newsPoopRepo, /*PerformerRepo performerRepo,*/
			/*RepostPoopRepo repostPoopRepo,*/ CourseRepo courseRepo, SubscriptionRepo subscriptionRepo,
			              CourseSubscriptionRepo courseSubscriptionRepo) {
		this.userRepo = userRepo;
		this.commentRepo = commentRepo;
//		this.repostRepo = repostRepo;
		this.newsRepo = newsRepo;
		this.newsLoopRepo = newsLoopRepo;
		this.newsPoopRepo = newsPoopRepo;
//		this.performerRepo = performerRepo;
//		this.repostPoopRepo = repostPoopRepo;
		this.courseRepo = courseRepo;
		this.courseSubscriptionRepo = courseSubscriptionRepo;
		this.subscriptionRepo = subscriptionRepo;
	}

	@GetMapping("/{profileId}")
	@ResponseBody
	public ResponseEntity getProfileById(@CookieValue(value = "userId") long userId,
	                                     @PathVariable(value = "profileId") long profileId) {
		logger.info("getProfileById=" + profileId);
		Optional<User> user = userRepo.findById(profileId);
		if (!user.isPresent()) {
			return logAndGetBadRequestEntity("userId=" + profileId + " doesn't exist");
		}
		return new ResponseEntity<>(new UserExtendedDTO(user.get(),
						subscriptionRepo.findByWhoIdAndOnWhomId(userId, profileId) != null),
				HttpStatus.OK);
	}

	@GetMapping("/{userId}/news")
	@ResponseBody
	public ResponseEntity getNewsByUserId(
			@PathVariable(value = "userId") long userId,
			@RequestParam(value = "size", required = false, defaultValue = "15") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber) {
		logger.info("getNewsByUserId=" + userId);
		logger.info("bool=" + !userRepo.findById(userId).isPresent());
		logger.info(userRepo.findById(userId).get().toString());
		if (!userRepo.findById(userId).isPresent()) {
			return logAndGetBadRequestEntity("userId=" + userId + " doesn't exist");
		}

		List<News> newsList = newsRepo.getAllByUserId(userId, of(pageNumber, pageSize,
				by(Sort.Order.desc("creationDate"))));
		List<NewsForFeedDTO> newsDTOS = newsList.stream()
				.map((News news) -> new NewsForFeedDTO(news, (long) news.getComments().size(),
						(long) news.getNewsLoops().size(),
						newsLoopRepo.getByNewsIdAndUserId(news.getId(), userId) != null,
						(long) news.getNewsPoops().size(),
						newsPoopRepo.getByNewsIdAndUserId(news.getId(), userId) != null))
				.collect(Collectors.toList());

		return new ResponseEntity<>(newsDTOS, HttpStatus.OK);
	}

	@GetMapping("/{userId}/courses")
	@ResponseBody
	public ResponseEntity getCoursesByUserId(
			@PathVariable(value = "userId") long userId,
			@RequestParam(value = "size", required = false, defaultValue = "15") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "0") int pageNumber) {
		logger.info("getCoursesByUserId=" + userId);
		if (!userRepo.findById(userId).isPresent()) {
			return logAndGetBadRequestEntity("userId=" + userId + " doesn't exist");
		}

		List<Course> courseList = courseRepo.getAllByUserId(userId, of(pageNumber, pageSize,
				by(Sort.Order.desc("creationDate"))));

		List<CourseInfoDTO> courseDTOS = courseList.stream()
				.map((Course course) -> new CourseInfoDTO(course,
						(long) course.getTasks().stream().mapToInt((CourseTask t) -> t.getTaskLoops().size()).sum(),
						(long) course.getTasks().stream().mapToInt((CourseTask t) -> t.getTaskPoops().size()).sum(),
						(course.getTasks().stream().mapToInt((CourseTask t) ->
								t.getTaskCompletions().size()).sum() + 0.0) / (course.getTasks().size()) + 0.0,
						courseSubscriptionRepo.getByCourseIdAndUserId(course.getId(), userId) != null))
				.collect(Collectors.toList());

		return new ResponseEntity<>(courseDTOS, HttpStatus.OK);
	}

	@GetMapping("/{profileId}/follow")
	@ResponseBody
	public ResponseEntity followUser(@PathVariable(value = "profileId") long profileId,
	                                 @CookieValue(value = "userId") long userId) {
		logger.info("followProfileId=" + profileId + "by userId=" + userId);
		if (subscriptionRepo.findByWhoIdAndOnWhomId(userId, profileId) != null) {
			return logAndGetBadRequestEntity("Already follow userId=" + profileId);
		}
		Subscription subscription = new Subscription();
		subscription.setOnWhom(userRepo.getOne(profileId));
		subscription.setWho(userRepo.getOne(userId));
		subscription.setDate(new Timestamp(System.currentTimeMillis()));
		Subscription subscriptionSaved = subscriptionRepo.saveAndFlush(subscription);
		logger.info("subscription id={}", subscriptionSaved.getId());
		return new ResponseEntity<>(profileId, HttpStatus.OK);
	}

	@GetMapping("/{profileId}/unfollow")
	@ResponseBody
	public ResponseEntity unsubscribeCourse(@PathVariable(value = "profileId") long profileId,
	                                        @CookieValue(value = "userId") long userId) {
		logger.info("unfollowProfileId=" + profileId + "by userId=" + userId);
		Subscription subscription = subscriptionRepo.findByWhoIdAndOnWhomId(userId, profileId);
		if (subscription == null) {
			return logAndGetBadRequestEntity("Already unfollow userId=" + profileId);
		}
		System.out.println(subscription.toString());
		subscriptionRepo.removeById(subscription.getId());
		System.out.println("deleted");
		return new ResponseEntity<>(profileId, HttpStatus.OK);
	}


	@GetMapping("/doesExist")
	@ResponseBody
	public boolean getLoginExistence(@RequestParam(value = "username") String username) {
		logger.info("existence of login=" + username);
		User user = userRepo.findByUsername(username);
		return user != null;
	}

	// TODO: validation for user
	@PostMapping(path="/register", consumes = "application/json", produces = "application/json")
	public ResponseEntity register(@RequestBody UserRegistrationDTO dto) {
		logger.info("register " + dto.toString());
		if (userRepo.findByUsername(dto.getUsername()) != null) {
			return logAndGetBadRequestEntity("Username=" + dto.getUsername() + " already exists");
		}
		User user = dto.makeUser();
		User userSaved = userRepo.saveAndFlush(user);
		logger.info("subscription id={}", userSaved.getId());
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	@GetMapping("/signin")
	@ResponseBody
	public ResponseEntity signin(@RequestParam(value = "username") String username,
	                             @RequestParam(value = "password") String password,
	                             HttpServletResponse response) {
		logger.info("login=" + username + " and password=" + password);
		User user = userRepo.findByUsername(username);
		if (user == null) {
			return logAndGetBadRequestEntity("No such user");
		}
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Set-Cookie","userId=" + user.getId().toString());
		Cookie cookie = new Cookie("userId", user.getId().toString());
		cookie.setPath("/");
		cookie.setMaxAge(3000000);
		response.addCookie(cookie);
		return user.getPassword().equals(password) ? /*ResponseEntity.status(HttpStatus.OK).headers(headers).build()*/
				new ResponseEntity<>("", HttpStatus.OK) :
				new ResponseEntity<>("Wrong username and password", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("change/{type}/to/{content}")
	@ResponseBody
	public ResponseEntity changeProfileInfo(@PathVariable(value = "type") String type,
	                                        @PathVariable(value = "content") String content,
	                                        @CookieValue(value = "userId") long userId) {
		logger.info("change " + type + " to " + content + " for userId=" + userId);
		Optional<User> optional = userRepo.findById(userId);
		if (!optional.isPresent()) {
			return logAndGetBadRequestEntity("Error with cookie");
		}
		User user = optional.get();
		switch (type) {
			case "firstName": user.setFirstName(content); break;
			case "surname": user.setSurname(content); break;
			case "patronymic": user.setPatronymic(content); break;
			case "sex":
				Boolean sex = Boolean.parseBoolean(content);
				if (content.equals("helicopter")) sex = null;
				if (content.equals("male")) sex = true;
				if (content.equals("female")) sex = false;
				user.setSex(sex); break;
//			case "email": user.setEmail(content); break;
			case "username":
			case "login": user.setUsername(content); break;
			case "password": user.setPassword(content); break;
		}
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private ResponseEntity logAndGetBadRequestEntity(String msg) {
		logger.info(msg);
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity logAndGetBadRequestEntityWithId(String msg, Long id) {
		logger.info(msg);
		return new ResponseEntity<>(new IdMessageDTO(id, msg),
				HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity logAndGetBadRequestEntityWithIdValue(String msg, Long id, Long value) {
		logger.info(msg);
		return new ResponseEntity<>(new IdValueMessageDTO(id, value, msg),
				HttpStatus.BAD_REQUEST);
	}
}
