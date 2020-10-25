package stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileCommand;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuery;
import ch.heigvd.amt.stoneoverflow.application.question.QuestionQuerySortBy;
import ch.heigvd.amt.stoneoverflow.application.vote.AddVoteCommand;
import ch.heigvd.amt.stoneoverflow.domain.question.Question;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionType;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryQuestionRepository;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory.InMemoryVoteRepository;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)// To not have tests making others fail because runned before
public class InMemoryQuestionRepositoryIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject @Named("InMemoryQuestionRepository")
    private InMemoryQuestionRepository inMemoryQuestionRepository;
    @Inject @Named("InMemoryVoteRepository")
    private InMemoryVoteRepository inMemoryVoteRepository;

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt");
        return archive;
    }

    @Test
    public void shouldFindAllQuestions() {
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().build());

        assertEquals(inMemoryQuestionRepository.findAll().size(), 3);
    }

    private void addVoteQuestion(QuestionId id){
        inMemoryVoteRepository.save(Vote.builder().votedBy(new UserId()).votedObject(id).build());
    }

    @Test
    public void shouldFindQuestionByVotes() {
        // Set the expected result
        ArrayList<Question> questionsSortedByVotesResult = new ArrayList<>();
        questionsSortedByVotesResult.add(Question.builder().build());
        questionsSortedByVotesResult.add(Question.builder().build());
        questionsSortedByVotesResult.add(Question.builder().build());

        // Add the question not in the same order
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByVotesResult.get(0));

        for(int i = 0; i < 10; i++){
            addVoteQuestion(questionsSortedByVotesResult.get(0).getId());
        }

        for(int i = 0; i < 5; i++){
            addVoteQuestion(questionsSortedByVotesResult.get(1).getId());
        }

        for(int i = 0; i < 3; i++){
            addVoteQuestion(questionsSortedByVotesResult.get(2).getId());
        }

        ArrayList<Question> questionsSortedByVotes = new ArrayList<>(inMemoryQuestionRepository.find(
                QuestionQuery.builder().sortBy(QuestionQuerySortBy.VOTES).build(), 0, 3));

        assertEquals(questionsSortedByVotes, questionsSortedByVotesResult);
    }

    @Test
    public void shouldFindQuestionByViews() {
        ArrayList<Question> questionsSortedByViewsResult = new ArrayList<>();
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(150)).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(50)).build());
        questionsSortedByViewsResult.add(Question.builder().nbViews(new AtomicInteger(3)).build());

        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(2));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(1));
        inMemoryQuestionRepository.save(questionsSortedByViewsResult.get(0));

        ArrayList<Question> questionsSortedByViews = new ArrayList<>(inMemoryQuestionRepository.find(QuestionQuery.builder().sortBy(QuestionQuerySortBy.VIEWS).build(), 0, 3));

        assertEquals(questionsSortedByViews, questionsSortedByViewsResult);
    }

    @Test
    public void shouldFindQueryQuestionsByType() {
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());
        inMemoryQuestionRepository.save(Question.builder().build());
        inMemoryQuestionRepository.save(Question.builder().questionType(QuestionType.SQL).build());

        QuestionQuery questionQuery = QuestionQuery.builder().type(QuestionType.SQL).build();

        inMemoryQuestionRepository.find(questionQuery,0,inMemoryQuestionRepository.getRepositorySize());

        assertEquals(inMemoryQuestionRepository.find(questionQuery,0,inMemoryQuestionRepository.getRepositorySize()).size(), 2);
    }
}
