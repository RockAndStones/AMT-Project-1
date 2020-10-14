package ch.heigvd.amt.stoneoverflow.domain;

public interface IEntity<ENTITY extends IEntity, ID extends Id> {
    ID getId();
    ENTITY deepClone();
}
