package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.domain.Id;
import ch.heigvd.amt.stoneoverflow.domain.UserMessageType;
import ch.heigvd.amt.stoneoverflow.domain.answer.AnswerId;
import ch.heigvd.amt.stoneoverflow.domain.question.QuestionId;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.domain.vote.IVoteRepository;
import ch.heigvd.amt.stoneoverflow.domain.vote.Vote;
import ch.heigvd.amt.stoneoverflow.domain.vote.VoteId;

import java.util.List;
import java.util.Optional;

public class VoteFacade {
    private IVoteRepository voteRepository;

    public VoteFacade(IVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public VoteId addVote(AddVoteCommand command) {
        Vote vote = Vote.builder()
                .votedBy(command.getVotedBy())
                .votedObject(command.getVotedObject())
                .voteType(command.getVoteType()).build();
        voteRepository.save(vote);
        return vote.getId();
    }

    public VoteDTO getVote(VoteId id, UserMessageType userMessageType) {
        Optional<Vote> vote = voteRepository.findVote(id, userMessageType);

        return vote.map(value -> VoteDTO.builder()
                .uuid(value.getId().asString())
                .votedBy(value.getVotedBy().asString())
                .voteType(value.getVoteType()).build())
                .orElse(null);
    }

    public VoteDTO getVote(Id id, UserId userId, UserMessageType userMessageType) {
        Optional<Vote> vote = voteRepository.findVote(id, userId, userMessageType);

        return vote.map(value -> VoteDTO.builder()
                .uuid(value.getId().asString())
                .votedBy(value.getVotedBy().asString())
                .voteType(value.getVoteType()).build())
                .orElse(null);
    }

    public int getNumberOfVotes(QuestionId id) {
        return getNumberOfVotes(id, UserMessageType.QUESTION);
    }

    public int getNumberOfVotes(AnswerId id) {
        return getNumberOfVotes(id, UserMessageType.ANSWER);
    }

    private int getNumberOfVotes(Id id, UserMessageType userMessageType) {
        return voteRepository.findNbVotes(id, userMessageType);
    }

    public void remove(VoteId voteId) {
        voteRepository.remove(voteId);
    }

    public void changeVote(UpdateVoteCommand command) {
        voteRepository.update(Vote.builder()
                .id(command.getId())
                .votedBy(command.getVotedBy())
                .votedObject(command.getVotedObject())
                .voteType(command.getVoteType()).build());
    }
}
