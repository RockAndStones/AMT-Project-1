package ch.heigvd.amt.stoneoverflow.application.vote;

import ch.heigvd.amt.stoneoverflow.application.gamification.GamificationFacade;
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
    GamificationFacade gamificationFacade;

    public VoteFacade(IVoteRepository voteRepository, GamificationFacade gamificationFacade) {
        this.voteRepository = voteRepository;
        this.gamificationFacade = gamificationFacade;
    }

    public VoteId addVote(AddVoteCommand command) {
        Vote vote = Vote.builder()
                .votedBy(command.getVotedBy())
                .votedObject(command.getVotedObject())
                .voteType(command.getVoteType()).build();
        voteRepository.save(vote);
        sendEvent(command.getVoteType(), command.getVotedBy());
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

    public void remove(VoteId voteId, UserId userId) {
        voteRepository.remove(voteId);
        gamificationFacade.removeVoteAsync(userId.asString(), null);
        gamificationFacade.stonerRegressAsync(userId.asString(), null);
    }

    public void changeVote(UpdateVoteCommand command) {
        voteRepository.update(Vote.builder()
                .id(command.getId())
                .votedBy(command.getVotedBy())
                .votedObject(command.getVotedObject())
                .voteType(command.getVoteType()).build());
    }

    public void sendEvent(Vote.VoteType type, UserId voteBy){
        if(type == Vote.VoteType.UP){
            gamificationFacade.addVoteAsync(voteBy.asString(), null);
            gamificationFacade.stonerProgressAsync(voteBy.asString(), null);
        } else if(type == Vote.VoteType.DOWN){
            gamificationFacade.removeVoteAsync(voteBy.asString(), null);
            gamificationFacade.stonerRegressAsync(voteBy.asString(), null);
        }
    }
}
