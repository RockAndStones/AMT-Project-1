package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuery;
import ch.heigvd.amt.stoneoverflow.application.answer.AnswerQuerySortBy;
import ch.heigvd.amt.stoneoverflow.domain.answer.Answer;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryAnswerRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryVoteRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)// To not have tests making others fail because runned before
public class InMemoryAnswerRepositoryIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("InMemoryAnswerRepository")
    private InMemoryAnswerRepository inMemoryAnswerRepository;
    @Inject @Named("InMemoryVoteRepository")
    private InMemoryVoteRepository inMemoryVoteRepository;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt");
        return archive;
    }

    private Answer generateAnswer(QuestionId questionId, Date date) {
        return Answer.builder()
                .answerTo(questionId)
                .description("Default description")
                .creatorId(new UserId())
                .creator("Anonymous")
                .date(date).build();
    }

    private Answer generateAnswer(QuestionId questionId) {
        return generateAnswer(questionId, new Date());
    }

    private Answer generateAnswer(Date date) {
        return generateAnswer(new QuestionId(), date);
    }

    private Answer generateAnswer() {
        return generateAnswer(new QuestionId());
    }

    private void addVoteQuestion(AnswerId id){
        inMemoryVoteRepository.save(Vote.builder().votedBy(new UserId()).votedObject(id).build());
    }

    @Test
    public void shouldFindAllAnswers() {

        inMemoryAnswerRepository.save(generateAnswer());
        inMemoryAnswerRepository.save(generateAnswer());
        inMemoryAnswerRepository.save(generateAnswer());

        assertEquals(3, inMemoryAnswerRepository.findAll().size());
    }

    @Test
    public void shouldFindAnswerByAnswerTo() {
        QuestionId questionId = new QuestionId();

        inMemoryAnswerRepository.save(generateAnswer(questionId));
        inMemoryAnswerRepository.save(generateAnswer(questionId));
        inMemoryAnswerRepository.save(generateAnswer(questionId));
        inMemoryAnswerRepository.save(generateAnswer());
        inMemoryAnswerRepository.save(generateAnswer());

        assertEquals(3, inMemoryAnswerRepository.find(
                AnswerQuery.builder().answerTo(questionId).build(),
                0,
                inMemoryAnswerRepository.getRepositorySize()).size());
    }

    @Test
    public void shouldFindAnswersByDate() {
        ArrayList<Answer> answersSortedByDate = new ArrayList<>();
        answersSortedByDate.add(generateAnswer(new Date(System.currentTimeMillis() - 1002)));
        answersSortedByDate.add(generateAnswer(new Date(System.currentTimeMillis() - 1001)));
        answersSortedByDate.add(generateAnswer(new Date(System.currentTimeMillis() - 1000)));

        inMemoryAnswerRepository.save(answersSortedByDate.get(1));
        inMemoryAnswerRepository.save(answersSortedByDate.get(2));
        inMemoryAnswerRepository.save(answersSortedByDate.get(0));

        assertEquals(answersSortedByDate, new ArrayList<>(inMemoryAnswerRepository.find(
                AnswerQuery.builder().sortBy(AnswerQuerySortBy.DATE).build(), 0, 3)));
    }

    @Test
    public void shouldFindAnswersByVotes() {
        ArrayList<Answer> answersSortedByDate = new ArrayList<>();
        answersSortedByDate.add(generateAnswer());
        answersSortedByDate.add(generateAnswer());
        answersSortedByDate.add(generateAnswer());

        inMemoryAnswerRepository.save(answersSortedByDate.get(1));
        inMemoryAnswerRepository.save(answersSortedByDate.get(2));
        inMemoryAnswerRepository.save(answersSortedByDate.get(0));

        for(int i = 0; i < 10; i++){
            addVoteQuestion(answersSortedByDate.get(0).getId());
        }

        for(int i = 0; i < 5; i++){
            addVoteQuestion(answersSortedByDate.get(1).getId());
        }

        for(int i = 0; i < 3; i++){
            addVoteQuestion(answersSortedByDate.get(2).getId());
        }

        assertEquals(answersSortedByDate, new ArrayList<>(inMemoryAnswerRepository.find(
                AnswerQuery.builder().sortBy(AnswerQuerySortBy.VOTES).build(), 0, 3)));
    }

    @Test
    public void shouldGetNumbersOfAnswersOfQuestion() {
        QuestionId questionId = new QuestionId();
        inMemoryAnswerRepository.save(generateAnswer(questionId));
        inMemoryAnswerRepository.save(generateAnswer(questionId));
        inMemoryAnswerRepository.save(generateAnswer());
        inMemoryAnswerRepository.save(generateAnswer());

        assertEquals(2, inMemoryAnswerRepository.getNumberOfAnswers(questionId));
    }

}
