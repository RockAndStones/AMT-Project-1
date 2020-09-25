package ch.heigvd.amt.StoneOverflow.domain;

public interface IEntity<ENTITY extends IEntity, ID extends Id> {
    ID getId();
    ENTITY deepClone();
}
