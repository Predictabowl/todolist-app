package it.aldinucci.todoapp.adapter.out.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Calendar;
import java.util.List;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class JPAEntityConstraintsTest {

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_deleteUser_cascadeOnProjects() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persistAndFlush(user);
		ProjectJPA project1 = new ProjectJPA(null, "project 1", user);
		entityManager.persistAndFlush(project1);
		ProjectJPA project2 = new ProjectJPA(null, "project 2", user);
		entityManager.persistAndFlush(project2);
		user.getProjects().add(project1);
		user.getProjects().add(project2);
		entityManager.flush();

		entityManager.remove(user);
		entityManager.flush();

		List<ProjectJPA> resultList = entityManager.getEntityManager().createQuery("from ProjectJPA", ProjectJPA.class)
				.getResultList();
		assertThat(resultList).isEmpty();
	}

	@Test
	void test_deleteProject_cascadeOnTasks() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persist(user);
		ProjectJPA project = new ProjectJPA(null, "project 1", user);
		entityManager.persist(project);
		user.getProjects().add(project);

		TaskJPA task1 = new TaskJPA(null, "task 1", "description 1", false, project);
		TaskJPA task2 = new TaskJPA(null, "task 2", "description 2", true, project);
		project.getTasks().add(task1);
		project.getTasks().add(task2);
		entityManager.persist(task1);
		entityManager.persistAndFlush(task2);
		entityManager.flush();

		entityManager.remove(project);
		user.getProjects().remove(project);
		entityManager.flush();

		ProjectJPA project2 = entityManager.find(ProjectJPA.class, project.getId());
		assertThat(project2).isNull();
		UserJPA user2 = entityManager.find(UserJPA.class, user.getId());
		assertThat(user2.getProjects()).isEmpty();
		List<TaskJPA> resultList = entityManager.getEntityManager().createQuery("from TaskJPA", TaskJPA.class)
				.getResultList();
		assertThat(resultList).isEmpty();
	}

	@Test
	void test_deleteVerificationToken_shouldNotCascade_onUser() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persist(user);
		VerificationTokenJPA token = new VerificationTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persist(token);
		entityManager.flush();
		
		entityManager.remove(token);
		entityManager.flush();
		
		entityManager.refresh(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	void test_cannotDeleteUser_beforeDeletingVerificationToken() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persist(user);
		VerificationTokenJPA token = new VerificationTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persist(token);
		entityManager.flush();
		
		entityManager.remove(user);
		assertThatThrownBy(() -> entityManager.flush())
			.isInstanceOf(PersistenceException.class);
		
	}
	
	@Test
	void test_deleteResetPasswordToken_shouldNotCascade_onUser() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persist(user);
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persist(token);
		entityManager.flush();
		
		entityManager.remove(token);
		entityManager.flush();
		
		entityManager.refresh(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	void test_cannotDeleteUser_beforeDeletingRestPasswordToken() {
		UserJPA user = new UserJPA(null, "mail@email", "username", "password");
		entityManager.persist(user);
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA(user, Calendar.getInstance().getTime());
		entityManager.persist(token);
		entityManager.flush();
		
		entityManager.remove(user);
		assertThatThrownBy(() -> entityManager.flush())
			.isInstanceOf(PersistenceException.class);
	}

}
